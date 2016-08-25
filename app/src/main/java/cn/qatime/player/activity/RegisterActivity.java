package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyListener;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    EditText phone;
    EditText code;
    EditText registercode;
    Button getcode;
    EditText password;
    EditText repassword;
    CheckBox checkBox;
    TextView agreement;
    Button next;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(getResources().getString(R.string.set_for_login));
        initView();
        time = new TimeCount(60000, 1000);
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.phone);
        code = (EditText) findViewById(R.id.code);
        getcode = (Button) findViewById(R.id.get_code);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        registercode = (EditText) findViewById(R.id.register_code);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        next = (Button) findViewById(R.id.next);
        agreement = (TextView) findViewById(R.id.agreement);

        phone.setHint(StringUtils.getSpannedString(this, getResources().getString(R.string.hint_phone_number)));
        code.setHint(StringUtils.getSpannedString(this, getResources().getString(R.string.hint_input_verification_code)));
        password.setHint(StringUtils.getSpannedString(this, getResources().getString(R.string.hint_input_password)));
        repassword.setHint(StringUtils.getSpannedString(this, getResources().getString(R.string.hint_confirm_password)));
        registercode.setHint(StringUtils.getSpannedString(this, getResources().getString(R.string.hint_qatime_register_code)));

        getcode.setOnClickListener(this);
        next.setOnClickListener(this);
        agreement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code:
                if (StringUtils.isPhone(phone.getText().toString())) {
                    time.start();
//                    发送验证码
                    Map<String, String> map = new HashMap<>();

                    map.put("send_to", phone.getText().toString());
                    map.put("key", "register_captcha");
                    DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGetCode, map), null, new VolleyListener(this) {
                        @Override
                        protected void onTokenOut() {

                        }

                        @Override
                        protected void onSuccess(JSONObject response) {
                            //TODO 已经注册过的呢？

                            try {
                                if (response.getInt("status") == 1) {
                                    Logger.e("验证码发送成功" + phone.getText().toString() + "---" + response.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        protected void onError(JSONObject response) {

                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

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
                //// TODO: 2016/8/24 点击协议查看
                break;
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
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
            getcode.setText(millisUntilFinished / 1000 + "s");
        }
    }

    private void next() {

        if (StringUtils.isNullOrBlanK(phone.getText().toString())) {//账号为空
            Toast.makeText(this, getResources().getString(R.string.account_can_not_be_empty), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }

        if (!StringUtils.isPhone(phone.getText().toString())) {//手机号不正确
            Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }
        if (StringUtils.isNullOrBlanK(password.getText().toString())) {  //密码为空
            Toast.makeText(this, getResources().getString(R.string.password_can_not_be_empty), Toast.LENGTH_LONG).show();
            next.setClickable(true);
            return;
        }
        if (StringUtils.isNullOrBlanK(repassword.getText().toString())) {  //确认密码为空
            Toast.makeText(this, getResources().getString(R.string.repassword_can_not_be_empty), Toast.LENGTH_LONG).show();
            next.setClickable(true);
            return;
        }
        if (!password.getText().toString().equals(repassword.getText().toString())) {//前后不一致
            Toast.makeText(this, getResources().getString(R.string.password_and_repassword_are_incongruous), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }
        if (StringUtils.isNullOrBlanK(code.getText().toString())) { //验证码
            Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }
        if (StringUtils.isNullOrBlanK(registercode.getText().toString())) {   //注册码
            Toast.makeText(this, getResources().getString(R.string.enter_the_register_code), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }
        if (!checkBox.isChecked()) {   //协议勾选
            Toast.makeText(this, getResources().getString(R.string.agree_agreement), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("login_mobile", phone.getText().toString());
        map.put("captcha_confirmation", code.getText().toString());
        map.put("password", password.getText().toString());
        map.put("password_confirmation", repassword.getText().toString());//确认密码
        map.put("captcha_confirmation", code.getText().toString());//验证码
        map.put("register_code_value", repassword.getText().toString());
        map.put("password_confirmation", repassword.getText().toString());
        map.put("client_type", "app");

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlRegister, map), null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {

            }

            @Override
            protected void onSuccess(JSONObject response) {

                try {
                    String token = response.getJSONObject("data").getString("remember_token");
                    int id = response.getJSONObject("data").getInt("id");

                    BaseApplication.getProfile().getData().setRemember_token(token);
                    BaseApplication.getProfile().getData().getUser().setId(id);

                    Logger.e("注册成功" + response);
                    //下一步跳转
                    Intent intent = new Intent(RegisterActivity.this, RegisterPerfectActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            protected void onError(JSONObject response) {

                String resault = null;
                try {
                    resault = "code:" + response.getJSONObject("error").getInt("code") + "    msg:" + response.getJSONObject("error").getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Logger.e("注册失败--" + resault);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        addToRequestQueue(request);
//下一步跳转
//        Intent intent = new Intent(RegisterActivity.this, RegisterPerfectActivity.class);
//        startActivity(intent);
    }


}


