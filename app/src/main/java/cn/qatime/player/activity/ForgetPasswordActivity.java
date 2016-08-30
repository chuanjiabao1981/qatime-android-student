package cn.qatime.player.activity;

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
import libraryextra.utils.VolleyListener;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    EditText number;
    EditText code;
    TextView getcode;
    EditText newpass;
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

        number.setHint(StringUtils.getSpannedString(this, R.string.hint_phone_number_forget));
        code.setHint(StringUtils.getSpannedString(this, R.string.hint_input_verification_code));
        newpass.setHint(StringUtils.getSpannedString(this, R.string.hint_password_forget));

        getcode.setOnClickListener(this);

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

                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        Logger.e("验证码发送成功" + phone + "---" + response.toString());
                        Toast.makeText(getApplicationContext(), "验证码已经发送至" + phone + "，请注意查收", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "验证码发送失败：" + phone, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "服务器异常，请检查网络", Toast.LENGTH_LONG).show();
                    }
                }));
                time.start();
                break;
            case R.id.submit:
                // TODO: 2016/8/29 找回密码接口
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
}
