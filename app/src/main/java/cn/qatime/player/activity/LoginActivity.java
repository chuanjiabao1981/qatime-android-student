package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.orhanobut.logger.Logger;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.config.UserPreferences;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.cache.UserInfoCache;
import libraryextra.utils.AppUtils;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.utils.CheckUtil;
import libraryextra.utils.DialogUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.SPUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.CheckView;
import libraryextra.view.CustomProgressDialog;

/**
 * 登陆页
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private int errornum = 0;
    private CheckView checkview;
    private int[] checkNum = null;
    private View checklayout;
    private EditText checkcode;
    private Button login;
    private CustomProgressDialog progress;
    private Profile profile;
    public static boolean reenter = false;//用于标示是否是游客身份从主页跳转过来,    是  (如果是点击直接进入,就直接finish,否则跳转)
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checklayout = findViewById(R.id.checklayout);
        checkview = (CheckView) findViewById(R.id.checkview);
        checkcode = (EditText) findViewById(R.id.checkcode);
        username = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login);
        TextView register = (TextView) findViewById(R.id.register);
        TextView enter = (TextView) findViewById(R.id.enter);
        View loginerror = findViewById(R.id.login_error);//忘记密码
        View reload = findViewById(R.id.reload);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        enter.setOnClickListener(this);
        loginerror.setOnClickListener(this);
        reload.setOnClickListener(this);
        checkview.setOnClickListener(this);

        if (!StringUtils.isNullOrBlanK(SPUtils.get(LoginActivity.this, "username", ""))) {
            username.setText(SPUtils.get(LoginActivity.this, "username", "").toString());
        }
        String sign = getIntent().getStringExtra("sign");
        if (!StringUtils.isNullOrBlanK(sign)) {
            if (sign.equals("exit_login")) {//从系统设置退出登录页面跳转而来，清除用户登录信息
                password.setText("");
            } else if (sign.equals(Constant.VISITORTOLOGIN)) {//游客身份转到登录页
                reenter = true;
                action = getIntent().getStringExtra("action");
                enter.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login://登陆
                login.setClickable(false);
                if (checklayout.getVisibility() == View.VISIBLE) {
                    if (CheckUtil.checkNum(checkcode.getText().toString(), checkNum)) {
                        login();
                    } else {
                        login.setClickable(true);
                        Toast.makeText(this, getResources().getString(R.string.verification_code_is_incorrect), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    login();
                }

                break;
            case R.id.register://注册
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, Constant.REGIST);
                break;
            case R.id.login_error://忘记密码
                intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.reload://重新换验证码
                initCheckNum();
                break;
            case R.id.checkview://重新换验证码
                initCheckNum();
                break;
            case R.id.enter://直接进入
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void login() {

        if (TextUtils.isEmpty(username.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.account_can_not_be_empty), Toast.LENGTH_SHORT).show();
            login.setClickable(true);
            return;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.password_can_not_be_empty), Toast.LENGTH_SHORT).show();
            login.setClickable(true);
            return;
        }
        progress = DialogUtils.startProgressDialog(progress, LoginActivity.this, getResourceString(R.string.landing));
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);

        Map<String, String> map = new HashMap<>();
        map.put("login_account", username.getText().toString().trim());
        map.put("password", password.getText().toString().trim());
        map.put("client_type", "app");
        map.put("client_cate", "student_client");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlLogin, map), null,
                new VolleyListener(LoginActivity.this) {
                    @Override
                    protected void onTokenOut() {
                        login.setClickable(true);
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        login.setClickable(true);
                        try {
                            JSONObject data = response.getJSONObject("data");
                            if (data.has("result")) {
                                DialogUtils.dismissDialog(progress);
                                if (data.getString("result") != null && data.getString("result").equals("failed")) {
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.account_or_password_error), Toast.LENGTH_SHORT).show();
                                    DialogUtils.dismissDialog(progress);
                                }
                            } else {
                                Logger.e("登录", response.toString());
                                SPUtils.put(LoginActivity.this, "username", username.getText().toString());
                                profile = JsonUtils.objectFromJson(response.toString(), Profile.class);
                                if (profile != null && profile.getData() != null && profile.getData().getUser() != null && profile.getData().getUser().getId() != 0) {
                                    PushAgent.getInstance(LoginActivity.this).addAlias(String.valueOf(profile.getData().getUser().getId()), "student", new UTrack.ICallBack() {
                                        @Override
                                        public void onMessage(boolean b, String s) {

                                        }
                                    });
                                    String deviceToken = PushAgent.getInstance(LoginActivity.this).getRegistrationId();
                                    if (!StringUtils.isNullOrBlanK(deviceToken)) {
                                        Map<String, String> m = new HashMap<>();
                                        m.put("user_id", String.valueOf(profile.getData().getUser().getId()));
                                        m.put("device_token", deviceToken);
                                        m.put("device_model", Build.MODEL);
                                        m.put("app_name", AppUtils.getAppName(LoginActivity.this));
                                        m.put("app_version", AppUtils.getVersionName(LoginActivity.this));
                                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlDeviceInfo, m), null,
                                                new VolleyListener(LoginActivity.this) {

                                                    @Override
                                                    protected void onSuccess(JSONObject response) {
                                                    }

                                                    @Override
                                                    protected void onError(JSONObject response) {

                                                    }

                                                    @Override
                                                    protected void onTokenOut() {
                                                        tokenOut();
                                                    }

                                                }, new VolleyErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                                super.onErrorResponse(volleyError);
                                            }
                                        });
                                        addToRequestQueue(request);
                                    }
                                }
                                if (profile != null && !TextUtils.isEmpty(profile.getData().getRemember_token())) {
//                                   跳转mainActivity时再setProfile
//                                   BaseApplication.setProfile(profile);
                                    checkUserInfo();
                                } else {
                                    //没有数据或token
                                }
                            }
                        } catch (JSONException e) {
                            DialogUtils.dismissDialog(progress);
                            BaseApplication.clearToken();
                        }


                    }

                    @Override
                    protected void onError(JSONObject response) {
                        DialogUtils.dismissDialog(progress);
                        BaseApplication.clearToken();
                        login.setClickable(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DialogUtils.dismissDialog(progress);
                BaseApplication.clearToken();
                Toast.makeText(LoginActivity.this, getResourceString(R.string.after_try_again), Toast.LENGTH_SHORT).show();
                login.setClickable(true);
                password.setText("");
                //当密码错误5次以上，开始使用验证码
                errornum++;
                if (errornum >= 5) {
                    checklayout.setVisibility(View.VISIBLE);
                    initCheckNum();
                }
            }
        });
        addToRequestQueue(request);
    }

    /**
     * 检查用户信息是否完整
     */
    private void checkUserInfo() {

        DaYiJsonObjectRequest request1 = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + profile.getData().getUser().getId() + "/info", null, new VolleyListener(LoginActivity.this) {
            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                PersonalInformationBean bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);
                String name = bean.getData().getName();
                String grade = bean.getData().getGrade();
                if (StringUtils.isNullOrBlanK(name) || StringUtils.isNullOrBlanK(grade)) {
                    DialogUtils.dismissDialog(progress);
                    Intent intent = new Intent(LoginActivity.this, RegisterPerfectActivity.class);
                    Toast.makeText(LoginActivity.this, getResourceString(R.string.please_set_information), Toast.LENGTH_SHORT).show();
                    intent.putExtra("username", username.getText().toString().trim());
                    intent.putExtra("password", password.getText().toString().trim());
                    intent.putExtra("token", profile.getToken());
                    intent.putExtra("userId", profile.getData().getUser().getId());
                    startActivityForResult(intent, Constant.REGIST);
                } else {
                    Logger.e("登录", response.toString());
                    //登录成功且有个人信息  设置profile
                    BaseApplication.setProfile(profile);
                    SPUtils.put(LoginActivity.this, "username", username.getText().toString());
                    loginAccount();//登陆云信
                }

            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(LoginActivity.this, getResourceString(R.string.login_failed), Toast.LENGTH_SHORT).show();
//                BaseApplication.clearToken();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                BaseApplication.clearToken();
            }
        }) {
            /**
             * 由于没有登陆没有token，重写getHeaders方法 手动设置访问token
             * @return
             * @throws AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Remember-Token", profile.getToken());
                return map;
            }
        };
        addToRequestQueue(request1);
    }

    /**
     * 登陆云信
     */
    private void loginAccount() {
        String account = BaseApplication.getAccount();
        String token = BaseApplication.getAccountToken();

        if (!StringUtils.isNullOrBlanK(account) && !StringUtils.isNullOrBlanK(token)) {
            NIMClient.getService(AuthService.class).login(new LoginInfo(account, token)).setCallback(new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo o) {
                    DialogUtils.dismissDialog(progress);
                    Logger.e("云信登录成功" + o.getAccount());
                    // 初始化消息提醒
                    NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

                    NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());
                    //缓存
                    UserInfoCache.getInstance().clear();
                    TeamDataCache.getInstance().clear();
                    //                FriendDataCache.getInstance().clear();

                    UserInfoCache.getInstance().buildCache();
                    TeamDataCache.getInstance().buildCache();
                    //好友维护,目前不需要
                    //                FriendDataCache.getInstance().buildCache();

                    UserInfoCache.getInstance().registerObservers(true);
                    TeamDataCache.getInstance().registerObservers(true);
//                                                FriendDataCache.getInstance().registerObservers(true);
//
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    DialogUtils.dismissDialog(progress);
//                    finish();
                }

                @Override
                public void onFailed(int code) {
                    DialogUtils.dismissDialog(progress);
//                    BaseApplication.clearToken();
                    profile.getData().setRemember_token("");
                    SPUtils.putObject(LoginActivity.this, "profile", profile);
                    Logger.e(code + "code");
//                    if (code == 302 || code == 404) {
//                        Toast.makeText(LoginActivity.this, R.string.account_or_password_error, Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(LoginActivity.this, getResourceString(R.string.login_failed) + code, Toast.LENGTH_SHORT).show();
//                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    DialogUtils.dismissDialog(progress);
                    Logger.e(throwable.getMessage());
//                    BaseApplication.clearToken();
                    profile.getData().setRemember_token("");
                    SPUtils.putObject(LoginActivity.this, "profile", profile);
                }
            });
        }

        if (reenter) {
            if (!StringUtils.isNullOrBlanK(action)) {
                Intent data = new Intent();
                data.putExtra("action", action);
                setResult(Constant.VISITORLOGINED, data);//游客从主页到登录页,点击登录,通知会main initview
            } else {
                setResult(Constant.VISITORLOGINED);//游客从主页到登录页,点击登录,通知会main initview
            }
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DialogUtils.dismissDialog(progress);
        finish();
    }

    /**
     * 刷新验证码
     */
    private void initCheckNum() {
        checkNum = CheckUtil.getCheckNum();
        checkview.setCheckNum(checkNum);
        checkview.invaliChenkNum();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constant.REGIST) {
            finish();
        }
    }
}
