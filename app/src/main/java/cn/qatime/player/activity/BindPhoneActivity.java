package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener {

    private Button getcode;
    private Button button_over;
    private EditText target_phone;
    private EditText code;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        initView();
        time = new TimeCount(60000, 1000);
    }
    private void initView() {
        setTitle(getResources().getString(R.string.bind_phone_number));
        getcode = (Button) findViewById(R.id.button_getcode);
        button_over = (Button) findViewById(R.id.button_over);
        code = (EditText) findViewById(R.id.code);
        target_phone = (EditText) findViewById(R.id.target_phone);
        //TODO 获取手机号 设置TextView


        getcode.setOnClickListener(this);
        button_over.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_getcode:
                //TODO 发送验证短信
                String phone = target_phone.getText().toString().trim();
                time.start();
                break;
            case R.id.button_over:
                //TODO 绑定手机
                Intent intent=new Intent(this,SecurityManagerActivity.class);
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
            getcode.setText("获取验证码");
            getcode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            getcode.setEnabled(false);//防止重复点击
            getcode.setText(millisUntilFinished / 1000 + "s后重新获取");
        }
    }
}
