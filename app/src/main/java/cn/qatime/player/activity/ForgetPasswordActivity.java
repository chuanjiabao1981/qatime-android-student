package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    EditText number;
    EditText code;
    TextView getcode;
    EditText newpass;
    EditText confirmNewpass;
    Button submit;
    private TimeCount time;
    private boolean statusLogin;
    private View currentPhoneView;
    private TextView currentPhone;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initView();
        setTitle(getResources().getString(R.string.find_password));
        time = new TimeCount(60000, 1000);

    }

    private void initView() {
        number = (EditText) findViewById(R.id.number);
        code = (EditText) findViewById(R.id.code);
        getcode = (TextView) findViewById(R.id.get_code);
        newpass = (EditText) findViewById(R.id.new_pass);
        submit = (Button) findViewById(R.id.submit);
        currentPhoneView = findViewById(R.id.current_phone_view);
        currentPhone = (TextView) findViewById(R.id.current_phone);
        confirmNewpass = (EditText) findViewById(R.id.confirm_new_password);

        number.setHint(StringUtils.getSpannedString(this, R.string.hint_phone_number_forget));
        code.setHint(StringUtils.getSpannedString(this, R.string.hint_input_verification_code));
        newpass.setHint(StringUtils.getSpannedString(this, R.string.hint_password_forget));
        confirmNewpass.setHint(StringUtils.getSpannedString(this, R.string.confirm_new_password));

        getcode.setOnClickListener(this);
        submit.setOnClickListener(this);

        statusLogin = getIntent().getBooleanExtra("status_login", false);
        if (statusLogin) {
            currentPhoneView.setVisibility(View.VISIBLE);
            number.setVisibility(View.GONE);
            phone = BaseApplication.getProfile().getData().getUser().getLogin_mobile() + "";
            currentPhone.setText(phone);
        } else {
            number.setVisibility(View.VISIBLE);
            currentPhoneView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (!statusLogin) {
            phone = number.getText().toString().trim();
        }
        switch (v.getId()) {
            case R.id.get_code:
                if (!StringUtils.isPhone(phone)) {
                    Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("send_to", phone);
                map.put("key", "get_password_back");
                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGetCode, map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        Logger.e("验证码发送成功" + phone + "---" + response.toString());
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.code_send_success), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.code_send_failed), Toast.LENGTH_LONG).show();

                    }
                }, new VolleyErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }));
                time.start();
                break;
            case R.id.submit:
                if (!StringUtils.isPhone(phone)) {//手机号不正确
                    Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();

                    return;
                }
                if (StringUtils.isNullOrBlanK(code.getText().toString().trim())) { //验证码
                    Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();

                    return;
                }
                if (StringUtils.isNullOrBlanK(newpass.getText().toString().trim())) { //验证码
                    Toast.makeText(this, getResources().getString(R.string.password_can_not_be_empty), Toast.LENGTH_SHORT).show();

                    return;
                }
                if (!StringUtils.isGoodPWD(newpass.getText().toString().trim())) {//密码格式不正确
                    Toast.makeText(this, getResources().getString(R.string.password_6_16), Toast.LENGTH_LONG).show();
                    return;
                }

                if (!newpass.getText().toString().trim().equals(confirmNewpass.getText().toString().trim())) {//前后不一致
                    Toast.makeText(this, getResources().getString(R.string.password_and_repassword_are_incongruous), Toast.LENGTH_SHORT).show();
                    return;
                }
                map = new HashMap<>();
                map.put("login_account", phone);
                map.put("captcha_confirmation", code.getText().toString().trim());
                map.put("password", newpass.getText().toString().trim());
                map.put("password_confirmation", confirmNewpass.getText().toString().trim());
                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.getUrl(UrlUtils.urlfindPassword, map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        if (response.isNull("data")) {
                            Toast.makeText(ForgetPasswordActivity.this, getResourceString(R.string.phone_not_exist), Toast.LENGTH_SHORT).show();
                        } else {
                            if (statusLogin) {
                                Logger.e("找回成功");
                                BaseApplication.clearToken();
                                Toast.makeText(ForgetPasswordActivity.this, getResourceString(R.string.change_password_success), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ForgetPasswordActivity.this, MainActivity.class);
                                intent.putExtra("sign", "exit_login");
                                startActivity(intent);
                            } else {
                                finish();
                            }

                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(ForgetPasswordActivity.this, getResourceString(R.string.code_error), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }));
                break;

        }
    }


    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            getcode.setText(getResources().getString(R.string.get_verification_code));
            getcode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            getcode.setEnabled(false);//防止重复点击
            getcode.setText(millisUntilFinished / 1000 + getResourceString(R.string.time_after_acquisition));
        }
    }
}
