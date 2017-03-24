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
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

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
public class VerifyPhoneActivity extends BaseActivity implements View.OnClickListener {

    private TextView textGetcode;
    private Button buttonNext;
    private TextView currentPhone;
    private EditText code;
    private TimeCount time;

    private void assignViews() {
        currentPhone = (TextView) findViewById(R.id.current_phone);
        code = (EditText) findViewById(R.id.code);
        textGetcode = (TextView) findViewById(R.id.text_getcode);
        buttonNext = (Button) findViewById(R.id.button_next);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_bind_phone);
        initView();
        time = new TimeCount(60000, 1000);
    }

    private void initView() {
        setTitles(getResources().getString(R.string.verify_phone_number));
        assignViews();

        currentPhone.setText(BaseApplication.getProfile().getData().getUser().getLogin_mobile() + "");
        code.setHint(StringUtils.getSpannedString(this, R.string.hint_input_verification_code));

        textGetcode.setOnClickListener(this);
        buttonNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_getcode:
                Map<String, String> map = new HashMap<>();
                map.put("send_to", currentPhone.getText().toString().trim());
                map.put("key", "send_captcha");

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGetCode, map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        Logger.e("验证码发送成功" + currentPhone.getText().toString().trim() + "---" + response.toString());
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
            case R.id.button_next:
                if (StringUtils.isNullOrBlanK(code.getText().toString())) { //验证码
                    Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
                    return;
                }
                map = new HashMap<>();
                map.put("send_to", currentPhone.getText().toString().trim());
                map.put("captcha", code.getText().toString().trim());

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGetCode + "/verify", map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {

                        if (response.isNull("data")) {
                            Logger.e("验证成功");
                            String next = getIntent().getStringExtra("next");
                            if ("phone".equals(next)) {
                                Intent intent = new Intent(VerifyPhoneActivity.this, BindPhoneActivity.class);
                                startActivity(intent);
                            } else if ("email".equals(next)) {
                                Intent intent = new Intent(VerifyPhoneActivity.this, BindEmailActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(VerifyPhoneActivity.this, getResourceString(R.string.code_error), Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(VerifyPhoneActivity.this, getResourceString(R.string.code_error), Toast.LENGTH_SHORT).show();
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
