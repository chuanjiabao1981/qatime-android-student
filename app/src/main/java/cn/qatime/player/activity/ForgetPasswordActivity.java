package cn.qatime.player.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

public class ForgetPasswordActivity extends BaseActivity {
    EditText number;
    EditText code;
    TextView getcode;
    EditText newpass;
    Button submit;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
        time = new TimeCount(60000, 1000);

    }

    private void initView() {
        number = (EditText) findViewById(R.id.number);
        code = (EditText) findViewById(R.id.code);
        getcode = (TextView) findViewById(R.id.get_code);
        newpass = (EditText) findViewById(R.id.new_pass);
        submit = (Button) findViewById(R.id.submit);
        getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.start();
            }
        });
    }


    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            getcode.setText("获取验证码");
            getcode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            getcode.setClickable(false);//防止重复点击
            getcode.setText(millisUntilFinished / 1000 + "s");
        }
    }
}
