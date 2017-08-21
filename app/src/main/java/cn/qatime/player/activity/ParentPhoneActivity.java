package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

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
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2016/8/17.
 */
public class ParentPhoneActivity extends BaseActivity implements View.OnClickListener {
    private TextView textGetcode;
    private Button buttonOver;
    private EditText newParentPhone;
    private EditText code;
    private TimeCount time;
    private TextView currentParentPhone;
    private String captchaPhone;
    private View currentParentPhoneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_phone);

        initView();
        time = new TimeCount(60000, 1000);
    }

    private void assignViews() {
        currentParentPhone = (TextView) findViewById(R.id.current_parent_phone);
        currentParentPhoneLayout = findViewById(R.id.current_parent_phone_layout);
        newParentPhone = (EditText) findViewById(R.id.new_parent_phone);
        code = (EditText) findViewById(R.id.code);
        textGetcode = (TextView) findViewById(R.id.text_getcode);
        buttonOver = (Button) findViewById(R.id.button_over);

        newParentPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isPhone(newParentPhone.getText().toString().trim())) {
                    if (!time.ticking)
                        textGetcode.setEnabled(true);
                } else {
                    textGetcode.setEnabled(false);
                }
            }
        });
    }


    private void initView() {
        setTitles(getResources().getString(R.string.parent_phone_number));
        assignViews();

        newParentPhone.setHint(StringUtils.getSpannedString(this, R.string.new_parent_phone));
        code.setHint(StringUtils.getSpannedString(this, R.string.hint_input_verification_code));

        String phoneP = getIntent().getStringExtra("phoneP");
        currentParentPhone.setText(phoneP);
        if (!StringUtils.isNullOrBlanK(phoneP) && StringUtils.isPhone(phoneP)) {
            currentParentPhoneLayout.setVisibility(View.VISIBLE);
        } else {
            currentParentPhoneLayout.setVisibility(View.GONE);
        }
        textGetcode.setOnClickListener(this);
        buttonOver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.text_getcode:
                captchaPhone = newParentPhone.getText().toString().trim();
                if (!StringUtils.isPhone(captchaPhone)) {//手机号不正确
                    Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("send_to", captchaPhone);
                map.put("key", "send_captcha");

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGetCode, map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        Logger.e("验证码发送成功" + captchaPhone + "---" + response.toString());
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.code_send_success), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.code_send_failed), Toast.LENGTH_LONG).show();
                    }
                }, new VolleyErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }));

                time.start();
                break;
            case R.id.button_over:
                String phone = newParentPhone.getText().toString().trim();
                if (!StringUtils.isPhone(phone)) {//手机号不正确
                    Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!phone.equals(captchaPhone)) { //验证手机是否一致
                    Toast.makeText(this, getResources().getString(R.string.captcha_phone_has_changed), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isNullOrBlanK(code.getText().toString().trim())) { //验证码
                    Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
                    return;
                }

                map = new HashMap<>();
                map.put("id", "" + BaseApplication.getInstance().getUserId());
                map.put("ticket_token", getIntent().getStringExtra("ticket_token"));
                map.put("parent_phone", captchaPhone);
                map.put("captcha_confirmation", code.getText().toString().trim());

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.getUrl(UrlUtils.urlPersonalInformation + BaseApplication.getInstance().getUserId() + "/parent_phone_ticket_token", map), null, new VolleyListener(this) {
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
                            if (error.getString("msg").contains("Current password")) {
                                Toast.makeText(ParentPhoneActivity.this, getResourceString(R.string.password_error), Toast.LENGTH_SHORT).show();
                            } else if (error.getString("msg").contains("Captcha confirmation")) {
                                Toast.makeText(ParentPhoneActivity.this, getResourceString(R.string.code_error), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ParentPhoneActivity.this, getResourceString(R.string.bind_parent_phone_failed), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new VolleyErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }));

                break;
        }
    }

    class TimeCount extends CountDownTimer {
        public boolean ticking;

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            ticking = false;
            textGetcode.setText(getResourceString(R.string.getcode));
            textGetcode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            ticking = true;
            textGetcode.setEnabled(false);//防止重复点击
            textGetcode.setText(millisUntilFinished / 1000 + getResourceString(R.string.time_after_acquisition));
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
