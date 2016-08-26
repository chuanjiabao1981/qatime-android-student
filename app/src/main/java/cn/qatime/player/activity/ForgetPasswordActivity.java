package cn.qatime.player.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import libraryextra.utils.StringUtils;

public class ForgetPasswordActivity extends BaseActivity {
    EditText number;
    EditText code;
    TextView getcode;
    EditText newpass;
    Button submit;
    private TimeCount time;
    private boolean statusLogin;
    private View currentPhoneView;
    private TextView currentPhone;

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

        getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.start();
            }
        });

        statusLogin = getIntent().getBooleanExtra("status_login", false);
        if (statusLogin) {
            currentPhoneView.setVisibility(View.VISIBLE);
            number.setVisibility(View.GONE);

            currentPhone.setText(BaseApplication.getProfile().getData().getUser().getLogin_mobile() + "");


        } else {
            number.setVisibility(View.VISIBLE);
            currentPhoneView.setVisibility(View.GONE);
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
