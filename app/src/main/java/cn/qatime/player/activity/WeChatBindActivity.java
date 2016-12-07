package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2016/12/1 11:29
 * @Describe 微信绑定页面
 */

public class WeChatBindActivity extends BaseActivity implements View.OnClickListener {
    private EditText phone;
    private EditText code;
    private Button getCode;
    private EditText password;
    private TextView grade;
    private CheckBox checkBox;
    private TextView agreement;
    private Button next;
    private TimeCount time;
    private String openid;

    private void assignViews() {
        phone = (EditText) findViewById(R.id.phone);
        code = (EditText) findViewById(R.id.code);
        getCode = (Button) findViewById(R.id.get_code);
        password = (EditText) findViewById(R.id.password);
        grade = (TextView) findViewById(R.id.grade);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        agreement = (TextView) findViewById(R.id.agreement);
        next = (Button) findViewById(R.id.next);

        time = new TimeCount(60000, 1000);
        getCode.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                next.setEnabled(isChecked);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_wechat);
        setTitle("绑定");
        openid = getIntent().getStringExtra("openid");
        assignViews();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code://获取验证码
                if (StringUtils.isPhone(phone.getText().toString().trim())) {
                    time.start();
                    getCode();
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

    private void getCode() {
        Map<String, String> map = new HashMap<>();
        map.put("send_to", phone.getText().toString().trim());
        map.put("key", "register_captcha");
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGetCode, map), null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                Toast.makeText(WeChatBindActivity.this, getResourceString(R.string.code_send_success), Toast.LENGTH_SHORT).show();
                Logger.e("验证码发送成功" + phone.getText().toString().trim() + "---" + response.toString());
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(WeChatBindActivity.this, getResourceString(R.string.code_send_failed), Toast.LENGTH_SHORT).show();
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

    private void next() {
        if (StringUtils.isNullOrBlanK(phone.getText().toString().trim())) {//账号为空
            Toast.makeText(this, getResources().getString(R.string.account_can_not_be_empty), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }

        if (!StringUtils.isPhone(phone.getText().toString().trim())) {//手机号不正确
            Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }
        if (StringUtils.isNullOrBlanK(code.getText().toString().trim())) { //验证码
            Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }
        if (!StringUtils.isGoodPWD(password.getText().toString().trim())) {
            Toast.makeText(this, getResources().getString(R.string.password_6_16), Toast.LENGTH_LONG).show();
            next.setClickable(true);
            return;
        }
        if (StringUtils.isNullOrBlanK(grade.getText().toString().trim())) {
            Toast.makeText(this, getResourceString(R.string.grade_can_not_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("login_mobile", phone.getText().toString().trim());
        map.put("captcha_confirmation", code.getText().toString().trim());
        map.put("password", password.getText().toString().trim());
        map.put("accept", "" + (checkBox.isChecked() ? 1 : 0));
        map.put("type", "Student");
        map.put("client_type", "app");
        map.put("grade", "app");
        map.put("openid", openid);


        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlWeChatRegister, map),
                null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {

//                try {
//                    String token = response.getJSONObject("data").getString("remember_token");
//                    int id = response.getJSONObject("data").getJSONObject("user").getInt("id");
//
//
//                    Toast.makeText(WeChatBindActivity.this, getResourceString(R.string.please_set_information), Toast.LENGTH_SHORT).show();
//                    Logger.e("注册成功" + response);
//                    //下一步跳转
//                    Intent intent = new Intent(WeChatBindActivity.this, RegisterPerfectActivity.class);
//                    intent.putExtra("username", phone.getText().toString().trim());
//                    intent.putExtra("password", password.getText().toString().trim());
//                    intent.putExtra("token", token);
//                    intent.putExtra("userId", id);
//                    startActivityForResult(intent, Constant.REGIST);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


            }

            @Override
            protected void onError(JSONObject response) {

                String result = "";
                try {
                    result = response.getJSONObject("error").getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Logger.e("注册失败--" + result);
                if (result.contains("已经被使用")) {
                    Toast.makeText(WeChatBindActivity.this, getResourceString(R.string.phone_already_used), Toast.LENGTH_SHORT).show();
                } else if (result.contains("与确认值不匹配")) {
                    Toast.makeText(WeChatBindActivity.this, getResourceString(R.string.code_error), Toast.LENGTH_SHORT).show();
                } else if (result.contains("注册码")) {
                    Toast.makeText(WeChatBindActivity.this, getResourceString(R.string.register_code_error), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WeChatBindActivity.this, getResourceString(R.string.register_failed), Toast.LENGTH_SHORT).show();
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

    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            getCode.setText(getResources().getString(R.string.get_verification_code));
            getCode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            getCode.setEnabled(false);//防止重复点击
            getCode.setText(millisUntilFinished / 1000 + "s");
        }
    }
}
