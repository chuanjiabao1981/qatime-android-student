package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.utils.StringUtils;

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {

    private TextView textGetcode;
    private Button buttonOver;
    private EditText targetPhone;
    private EditText code;
    private TimeCount time;


    private void assignViews() {
        targetPhone = (EditText) findViewById(R.id.target_phone);
        code = (EditText) findViewById(R.id.code);
        textGetcode = (TextView) findViewById(R.id.text_getcode);
        buttonOver = (Button) findViewById(R.id.button_over);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        initView();
        time = new TimeCount(60000, 1000);
    }

    private void initView() {
        setTitle(getResources().getString(R.string.bind_phone_number));

        assignViews();

        targetPhone.setHint(StringUtils.getSpannedString(this,R.string.hint_input_new_phone));
        code.setHint(StringUtils.getSpannedString(this,R.string.hint_input_code));


        textGetcode.setOnClickListener(this);
        buttonOver.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_getcode:
                //TODO 发送验证短信
                String phone = targetPhone.getText().toString().trim();
                time.start();
                break;
            case R.id.button_over:
                //TODO 绑定手机
                Intent intent = new Intent(this, SecurityManagerActivity.class);
                startActivity(intent);
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
