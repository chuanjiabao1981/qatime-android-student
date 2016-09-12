package cn.qatime.player.activity;

import android.content.Intent;
import android.graphics.Color;
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

/**
 * Created by lenovo on 2016/8/17.
 */
public class ParentPhoneActivity extends BaseActivity implements View.OnClickListener {
    private TextView textGetcode;
    private Button buttonOver;
    private EditText newParentPhone;
    private EditText code;
    private EditText password;
    private TimeCount time;
    private TextView currentParentPhone;
    private String phone;


    private void assignViews() {
        currentParentPhone = (TextView) findViewById(R.id.current_parent_phone);
        password = (EditText) findViewById(R.id.password);
        newParentPhone = (EditText) findViewById(R.id.new_parent_phone);
        code = (EditText) findViewById(R.id.code);
        textGetcode = (TextView) findViewById(R.id.text_getcode);
        buttonOver = (Button) findViewById(R.id.button_over);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_phone);

        initView();
        time = new TimeCount(60000, 1000);
    }

    private void initView() {
        setTitle(getResources().getString(R.string.parent_phone_number));
        assignViews();

        password.setHint(StringUtils.getSpannedString(this, R.string.hint_input_password));
        newParentPhone.setHint(StringUtils.getSpannedString(this, R.string.new_parent_phone));
        code.setHint(StringUtils.getSpannedString(this, R.string.hint_input_code));

        String phoneP = getIntent().getStringExtra("phoneP");
        currentParentPhone.setText(phoneP);
        if (!phoneP.equals("未绑定")) {
            currentParentPhone.setTextColor(Color.BLACK);
        } else {
            currentParentPhone.setTextColor(Color.RED);
        }
        textGetcode.setOnClickListener(this);
        buttonOver.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        phone = newParentPhone.getText().toString().trim();
        if (!StringUtils.isPhone(phone)) {//手机号不正确
            Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {

            case R.id.text_getcode:
                Map<String, String> map = new HashMap<>();
                map.put("send_to", phone);
                map.put("key", "send_captcha");

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
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }));

                time.start();
                break;
            case R.id.button_over:
                if (!StringUtils.isGoodPWD(password.getText().toString().trim())) {
                    Toast.makeText(this, getResources().getString(R.string.password_6_16), Toast.LENGTH_LONG).show();
                    return;
                }

                if (StringUtils.isNullOrBlanK(code.getText().toString().trim())) { //验证码
                    Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
                    return;
                }

                map = new HashMap<>();
                map.put("id", "" + BaseApplication.getUserId());
                map.put("parent_phone", phone);
                map.put("current_password", password.getText().toString().trim());
                map.put("captcha_confirmation", code.getText().toString().trim());

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.getUrl(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/parent_phone", map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {


                        if (!response.isNull("data")) {
                            Logger.e("验证成功");
                            Toast.makeText(ParentPhoneActivity.this, getResourceString(R.string.bind_parent_phone_success), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ParentPhoneActivity.this, SecurityManagerActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ParentPhoneActivity.this, getResourceString(R.string.bind_parent_phone_failed), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Logger.e(response.toString());
                        try {
                            JSONObject error = response.getJSONObject("error");
                            if (error.getString("msg").contains("是无效的")) {
                                Toast.makeText(ParentPhoneActivity.this, getResourceString(R.string.password_error), Toast.LENGTH_SHORT).show();
                            } else if (error.getString("msg").contains("与确认值不匹配")) {
                                Toast.makeText(ParentPhoneActivity.this, getResourceString(R.string.code_error), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ParentPhoneActivity.this, getResourceString(R.string.bind_parent_phone_failed), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            textGetcode.setText(getResourceString(R.string.getcode));
            textGetcode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            textGetcode.setEnabled(false);//防止重复点击
            textGetcode.setText(millisUntilFinished / 1000 + getResourceString(R.string.time_after_acquisition));
        }
    }
}
