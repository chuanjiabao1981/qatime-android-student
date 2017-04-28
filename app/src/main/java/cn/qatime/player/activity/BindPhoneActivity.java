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

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {

    private TextView textGetcode;
    private Button buttonOver;
    private EditText targetPhone;
    private EditText code;
    private TimeCount time;
    private String currentphone;


    private void assignViews() {
        targetPhone = (EditText) findViewById(R.id.target_phone);
        code = (EditText) findViewById(R.id.code);
        textGetcode = (TextView) findViewById(R.id.text_getcode);
        buttonOver = (Button) findViewById(R.id.button_over);

        targetPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isPhone(targetPhone.getText().toString().trim())) {
                    textGetcode.setEnabled(true);
                } else {
                    textGetcode.setEnabled(false);
                    if(targetPhone.getText().toString().length()==11) {
                        Toast.makeText(BindPhoneActivity.this, R.string.phone_number_is_incorrect, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        initView();
        time = new TimeCount(60000, 1000);
    }

    private void initView() {
        setTitles(getResources().getString(R.string.bind_phone_number));

        assignViews();

        targetPhone.setHint(StringUtils.getSpannedString(this, R.string.hint_input_new_phone));
        code.setHint(StringUtils.getSpannedString(this, R.string.hint_input_verification_code));


        textGetcode.setOnClickListener(this);
        buttonOver.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        currentphone = targetPhone.getText().toString().trim();
        switch (v.getId()) {
            case R.id.text_getcode:
                if (!StringUtils.isPhone(currentphone)) {//手机号不正确
                    Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, String> map = new HashMap<>();
                map.put("send_to", currentphone);
                map.put("key", "send_captcha");

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGetCode, map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.code_send_success), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(BindPhoneActivity.this, getResourceString(R.string.code_send_failed), Toast.LENGTH_SHORT).show();

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

                if (!StringUtils.isPhone(currentphone)) {//手机号不正确
                    Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (StringUtils.isNullOrBlanK(code.getText().toString())) { //验证码
                    Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
                    return;
                }
                map = new HashMap<>();
                map.put("id", "" + BaseApplication.getUserId());
                map.put("login_mobile", currentphone);
                map.put("captcha_confirmation", code.getText().toString().trim());

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.getUrl(UrlUtils.urlUser + BaseApplication.getUserId() + "/login_mobile", map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        Logger.e("验证成功");
                        Toast.makeText(BindPhoneActivity.this, getResourceString(R.string.bind_phone_success), Toast.LENGTH_SHORT).show();
                        BaseApplication.clearToken();
                        Intent intent = new Intent(BindPhoneActivity.this, MainActivity.class);
                        intent.putExtra("sign", "exit_login");
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    protected void onError(JSONObject response) {
                        try {
                            JSONObject error = response.getJSONObject("error");
                            if (error.getString("msg").contains("Captcha confirmation")) {
                                Toast.makeText(BindPhoneActivity.this, getResourceString(R.string.code_error), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BindPhoneActivity.this, getResourceString(R.string.phone_already_bind), Toast.LENGTH_SHORT).show();
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
