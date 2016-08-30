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
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.utils.CheckUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.SPUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.CheckView;

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
    private Profile profile;

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
//            if (!StringUtils.isNullOrBlanK(SPUtils.get(LoginActivity.this, "password", ""))) {
//                password.setText(SPUtils.get(LoginActivity.this, "password", "").toString());
//            }
        }
        String sign = getIntent().getStringExtra("sign");//从系统设置退出登录页面跳转而来，清除用户登录信息
        if (!StringUtils.isNullOrBlanK(sign) && sign.equals("exit_login")) {
            username.setText("");
            password.setText("");
        }
//        username.setText("15617685965@163.com");
//        password.setText("123456");
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
        Map<String, String> map = new HashMap<>();
        map.put("login_account", username.getText().toString());
        map.put("password", password.getText().toString());
        map.put("client_type", "app");
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
                                if (data.getString("result") != null && data.getString("result").equals("failed")) {
                                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.account_or_password_error), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                profile = JsonUtils.objectFromJson(response.toString(), Profile.class);
                                BaseApplication.setProfile(profile);
                                DaYiJsonObjectRequest request1 = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", null, new VolleyListener(LoginActivity.this) {
                                    @Override
                                    protected void onTokenOut() {

                                    }

                                    @Override
                                    protected void onSuccess(JSONObject response) {
                                        PersonalInformationBean bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);
                                        Logger.e(bean.toString());
                                        String name = bean.getData().getName();
                                        String grade = bean.getData().getGrade();
                                        if (StringUtils.isNullOrBlanK(name) || StringUtils.isNullOrBlanK(grade)) {
                                            Intent intent = new Intent(LoginActivity.this, RegisterPerfectActivity.class);
                                            Toast.makeText(LoginActivity.this, "请先完善个人信息", Toast.LENGTH_SHORT).show();
                                            intent.putExtra("username", username.getText().toString().trim());
                                            intent.putExtra("password", password.getText().toString().trim());
                                            startActivityForResult(intent, Constant.REGIST);
                                        } else {
                                            Logger.e("登录", response.toString());
                                            SPUtils.put(LoginActivity.this, "username", username.getText().toString());

                                            if (profile != null && !TextUtils.isEmpty(profile.getData().getRemember_token())) {
                                                SPUtils.putObject(LoginActivity.this, "profile", profile);
                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                //没有数据或token
                                            }
                                        }

                                    }

                                    @Override
                                    protected void onError(JSONObject response) {
                                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {

                                    }
                                });
                                addToRequestQueue(request1);
                            }
                        } catch (JSONException e) {

//                            e.printStackTrace();
//                            LogUtils.e("error"+e.getMessage());
                        }


                    }

                    @Override
                    protected void onError(JSONObject response) {
                        login.setClickable(true);
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
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
