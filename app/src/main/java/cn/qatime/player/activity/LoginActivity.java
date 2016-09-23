package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.orhanobut.logger.Logger;

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
        Button register = (Button) findViewById(R.id.register);
        View loginerror = findViewById(R.id.login_error);//忘记密码
        View reload = findViewById(R.id.reload);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        loginerror.setOnClickListener(this);
        reload.setOnClickListener(this);
        checkview.setOnClickListener(this);

        if (!StringUtils.isNullOrBlanK(SPUtils.get(LoginActivity.this, "username", ""))) {
            username.setText(SPUtils.get(LoginActivity.this, "username", "").toString());
        }
        String sign = getIntent().getStringExtra("sign");//从系统设置退出登录页面跳转而来，清除用户登录信息
        if (!StringUtils.isNullOrBlanK(sign) && sign.equals("exit_login")) {
//            username.setText("");
            password.setText("");
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
//                                profile = JsonUtils.objectFromJson(response.toString(), Profile.class);
                                Logger.e("登录", response.toString());
                                SPUtils.put(LoginActivity.this, "username", username.getText().toString());
                                Profile profile = JsonUtils.objectFromJson(response.toString(), Profile.class);
                                if (profile != null && !TextUtils.isEmpty(profile.getData().getRemember_token())) {

                                    BaseApplication.setProfile(profile);

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

        DaYiJsonObjectRequest request1 = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", null, new VolleyListener(LoginActivity.this) {
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
                    Intent intent = new Intent(LoginActivity.this, RegisterPerfectActivity.class);
                    Toast.makeText(LoginActivity.this, getResourceString(R.string.please_set_information), Toast.LENGTH_SHORT).show();
                    intent.putExtra("username", username.getText().toString().trim());
                    intent.putExtra("password", password.getText().toString().trim());
                    startActivityForResult(intent, Constant.REGIST);
                } else {
                    Logger.e("登录", response.toString());
                    SPUtils.put(LoginActivity.this, "username", username.getText().toString());
                    loginAccount();//登陆云信
                }

            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(LoginActivity.this, getResourceString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                BaseApplication.clearToken();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                BaseApplication.clearToken();
            }
        });
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

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    DialogUtils.dismissDialog(progress);
                    finish();
                }

                @Override
                public void onFailed(int code) {
                    DialogUtils.dismissDialog(progress);
//                    BaseApplication.clearToken();
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
                    BaseApplication.clearToken();
                }
            });
        }
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
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
