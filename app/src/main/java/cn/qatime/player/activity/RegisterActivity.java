package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import cn.qatime.player.utils.SPUtils;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.Profile;
import libraryextra.utils.AppUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    EditText phone;
    EditText code;
    Button getcode;
    EditText password;
    EditText repassword;
    CheckBox checkBox;
    TextView agreement;
    Button next;
    private TimeCount time;
    private Profile profile;
    private String captchaPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitles(getResources().getString(R.string.set_for_login));
        initView();
        time = new TimeCount(60000, 1000);
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.phone);
        code = (EditText) findViewById(R.id.code);
        getcode = (Button) findViewById(R.id.get_code);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        next = (Button) findViewById(R.id.next);
        agreement = (TextView) findViewById(R.id.agreement);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isPhone(phone.getText().toString().trim())) {
                    if(!time.ticking){//如果不在计时，允许点击
                        getcode.setEnabled(true);
                    }
                } else {
                    getcode.setEnabled(false);
                    if(phone.getText().toString().length()==11) {
                        Toast.makeText(RegisterActivity.this, R.string.phone_number_is_incorrect, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        phone.setHint(StringUtils.getSpannedString(getResources().getString(R.string.hint_phone_number)));
        code.setHint(StringUtils.getSpannedString(getResources().getString(R.string.hint_input_verification_code)));
        password.setHint(StringUtils.getSpannedString(getResources().getString(R.string.hint_input_password)));
        repassword.setHint(StringUtils.getSpannedString(getResources().getString(R.string.hint_confirm_password)));
//        registercode.setHint(StringUtils.getSpannedString(this, getResources().getString(R.string.hint_qatime_register_code)));

        getcode.setOnClickListener(this);
        next.setOnClickListener(this);
        agreement.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                next.setEnabled(isChecked);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code:
                if (StringUtils.isPhone(phone.getText().toString().trim())) {
                    time.start();
//                    发送验证码
                    captchaPhone = phone.getText().toString().trim();
                    Map<String, String> map = new HashMap<>();
                    map.put("send_to", captchaPhone);
                    map.put("key", "register_captcha");
                    DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGetCode, map), null, new VolleyListener(this) {
                        @Override
                        protected void onTokenOut() {
                            tokenOut();
                        }

                        @Override
                        protected void onSuccess(JSONObject response) {
                            Toast.makeText(RegisterActivity.this, getResourceString(R.string.code_send_success), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        protected void onError(JSONObject response) {
                            Toast.makeText(RegisterActivity.this, getResourceString(R.string.code_send_failed), Toast.LENGTH_SHORT).show();
                        }


                    }, new VolleyErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            super.onErrorResponse(volleyError);
                            Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                        }
                    });
                    addToRequestQueue(request);

                } else {
                    Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.next:
                next();
                break;
            case R.id.agreement:
                Intent intent= new Intent(this,AgreementActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class TimeCount extends CountDownTimer {
        public boolean ticking;

        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            ticking = false;
            getcode.setText(getResources().getString(R.string.get_verification_code));
            getcode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            ticking =true;
            getcode.setEnabled(false);//防止重复点击
            getcode.setText(millisUntilFinished / 1000 + "s");
        }
    }

    private void next() {
        if (StringUtils.isNullOrBlanK(phone.getText().toString().trim())) {//账号为空
            Toast.makeText(this, getResources().getString(R.string.account_can_not_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isPhone(phone.getText().toString().trim())) {//手机号不正确
            Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phone.getText().toString().trim().equals(captchaPhone)) { //验证手机是否一致
            Toast.makeText(this, getResources().getString(R.string.captcha_phone_has_changed), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNullOrBlanK(code.getText().toString().trim())) { //验证码
            Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isGoodPWD(password.getText().toString().trim())) {
            Toast.makeText(this, getResources().getString(R.string.password_format_error), Toast.LENGTH_LONG).show();
            return;
        }
        if (StringUtils.isNullOrBlanK(repassword.getText().toString().trim())) {  //确认密码为空
            Toast.makeText(this, getResources().getString(R.string.repassword_can_not_be_empty), Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.getText().toString().trim().equals(repassword.getText().toString().trim())) {//前后不一致
            Toast.makeText(this, getResources().getString(R.string.password_and_repassword_are_incongruous), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkBox.isChecked()) {   //协议勾选
            Toast.makeText(this, getResources().getString(R.string.agree_agreement), Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("login_mobile", captchaPhone);
        map.put("captcha_confirmation", code.getText().toString().trim());
        map.put("password", password.getText().toString().trim());
        map.put("password_confirmation", repassword.getText().toString().trim());//确认密码
        map.put("accept", "" + (checkBox.isChecked() ? 1 : 0));
        map.put("type", "Student");
        map.put("client_type", "app");


        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlRegister, map), null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {

                Toast.makeText(RegisterActivity.this, getResourceString(R.string.please_set_information), Toast.LENGTH_SHORT).show();
                Map<String, String> map = new HashMap<>();
                map.put("login_account", phone.getText().toString().trim());
                map.put("password", password.getText().toString().trim());
                map.put("client_type", "app");
                map.put("client_cate", "student_client");
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlLogin, map), null,
                        new VolleyListener(RegisterActivity.this) {
                            @Override
                            protected void onTokenOut() {
                                tokenOut();
                            }

                            @Override
                            protected void onSuccess(JSONObject response) {
                                try {
                                    JSONObject data = response.getJSONObject("data");
                                    if (data.has("result")) {
                                        if (data.getString("result") != null && data.getString("result").equals("failed")) {
                                            Toast.makeText(RegisterActivity.this, getResources().getString(R.string.account_or_password_error), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Logger.e("登录", response.toString());
                                        SPUtils.put(RegisterActivity.this, "username", phone.getText().toString());
                                        Profile profile = JsonUtils.objectFromJson(response.toString(), Profile.class);
                                        if (profile != null && profile.getData() != null && profile.getData().getUser() != null && profile.getData().getUser().getId() != 0) {
                                            initPushAgent(profile);
                                        }
                                        if (profile != null && !TextUtils.isEmpty(profile.getData().getRemember_token())) {

                                            BaseApplication.getInstance().setProfile(profile);
                                            //登录云信
                                            loginAccount();
                                        } else {
                                            //没有数据或token
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }



                            @Override
                            protected void onError(JSONObject response) {
                                Toast.makeText(RegisterActivity.this, getResourceString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                            }
                        }, new VolleyErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                    }
                });
                addToRequestQueue(request);

            }

            @Override
            protected void onError(JSONObject response) {

                String result = "";
                try {
                    result = response.getJSONObject("error").getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.contains("已经被使用")) {
//                    Toast.makeText(RegisterActivity.this, getResourceString(R.string.phone_already_used), Toast.LENGTH_SHORT).show();
                    dialogReTry();
                } else if (result.contains("Captcha confirmation")) {
                    Toast.makeText(RegisterActivity.this, getResourceString(R.string.code_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, getResourceString(R.string.register_failed), Toast.LENGTH_SHORT).show();
                }
            }

        }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });

        addToRequestQueue(request);
    }
    private void initPushAgent(Profile profile) {
        PushAgent.getInstance(RegisterActivity.this).addAlias(String.valueOf(profile.getData().getUser().getId()), "student", new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean b, String s) {

            }
        });
        String deviceToken = PushAgent.getInstance(RegisterActivity.this).getRegistrationId();
        if (!StringUtils.isNullOrBlanK(deviceToken)) {
            Map<String, String> m = new HashMap<>();
            m.put("user_id", String.valueOf(profile.getData().getUser().getId()));
            m.put("device_token", deviceToken);
            m.put("device_model", Build.MODEL);
            try {
                m.put("app_name", URLEncoder.encode(AppUtils.getAppName(RegisterActivity.this), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            m.put("app_version", AppUtils.getVersionName(RegisterActivity.this));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlDeviceInfo, m), null,
                    new VolleyListener(RegisterActivity.this) {

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
    /**
     * 手机号已注册提示
     */
    private void dialogReTry() {
        final AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        View view = View.inflate(RegisterActivity.this, R.layout.dialog_cancel_or_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(R.string.this_phone_number_already_regist);
        ((TextView) view.findViewById(R.id.cancel)).setText(R.string.use_a_new_number);
        ((TextView) view.findViewById(R.id.confirm)).setText(R.string.login);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                clearData();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
    }

    /**
     * 清空数据
     */
    private void clearData() {
        phone.setText("");
        code.setText("");
        password.setText("");
        repassword.setText("");
    }

    private void loginAccount() {
        String account = BaseApplication.getInstance().getAccount();
        String token = BaseApplication.getInstance().getAccountToken();

        if (!StringUtils.isNullOrBlanK(account) && !StringUtils.isNullOrBlanK(token)) {
            NIMClient.getService(AuthService.class).login(new LoginInfo(account, token)).setCallback(new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo o) {
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
                }

                @Override
                public void onFailed(int code) {
                    Logger.e(code + "code");
                }

                @Override
                public void onException(Throwable throwable) {
                    Logger.e(throwable.getMessage());
                }
            });
        }
        //下一步跳转，完善信息
        Intent intent = new Intent(RegisterActivity.this, RegisterPerfectActivity.class);
        int register_action = getIntent().getIntExtra("register_action", Constant.REGIST_1);
        intent.putExtra("register_action",register_action);
        startActivityForResult(intent, register_action);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == Constant.REGIST_1||requestCode == Constant.REGIST_2) && resultCode == Constant.RESPONSE) {
            setResult(resultCode);
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}


