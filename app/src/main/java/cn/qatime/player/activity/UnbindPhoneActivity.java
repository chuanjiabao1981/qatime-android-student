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
public class UnbindPhoneActivity extends BaseActivity implements View.OnClickListener {

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
        setContentView(R.layout.activity_unbind_phone);
        initView();
        time = new TimeCount(60000, 1000);
    }

    private void initView() {
        setTitle(getResources().getString(R.string.bind_phone_number));
        assignViews();

        currentPhone.setText(BaseApplication.getProfile().getData().getUser().getLogin_mobile() + "");
        code.setHint(StringUtils.getSpannedString(this, R.string.hint_input_code));

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

                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        Logger.e("验证码发送成功" + currentPhone.getText().toString().trim() + "---" + response.toString());
                        Toast.makeText(getApplicationContext(), "验证码已经发送至" + currentPhone.getText().toString().trim() + "，请注意查收", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }));


                time.start();
                break;
            case R.id.button_next:
                map = new HashMap<>();
                map.put("send_to", currentPhone.getText().toString().trim());
                map.put("captcha", code.getText().toString().trim());

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGetCode + "/verify", map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {

                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        try {

                            if (response.isNull("data")) {
                                Logger.e("验证成功");
                                Intent intent = new Intent(UnbindPhoneActivity.this, BindPhoneActivity.class);
                                startActivity(intent);
                            } else {
                                JSONObject data = response.getJSONObject("data");
                                Toast.makeText(UnbindPhoneActivity.this, data.getString("result") + ": 验证失败\n" + data.getString("error"), Toast.LENGTH_SHORT).show();
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
            textGetcode.setText("获取验证码");
            textGetcode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            textGetcode.setEnabled(false);//防止重复点击
            textGetcode.setText(millisUntilFinished / 1000 + "s后重新获取");
        }
    }
}
