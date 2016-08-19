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

/**
 * Created by lenovo on 2016/8/17.
 */
public class BindEmailActivity extends BaseActivity implements View.OnClickListener {
    private TimeCount time;
    private TextView getcode;
    private Button button_over;
    private EditText new_email;
    private EditText confirm_email;
    private EditText code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_email);

        initView();
        time = new TimeCount(60000, 1000);
    }

    private void initView() {
        setTitle(getResources().getString(R.string.bind_email));
        getcode = (TextView) findViewById(R.id.text_getcode);
        button_over = (Button) findViewById(R.id.button_over);
        code = (EditText) findViewById(R.id.code);
        new_email = (EditText) findViewById(R.id.input_new_email);
        confirm_email = (EditText) findViewById(R.id.confirm_new_email);

        //TODO 获取手机号 设置TextView
        getcode.setOnClickListener(this);
        button_over.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_getcode:
                //TODO 发送验证短信
                time.start();
                break;
            case R.id.button_over:
                //TODO 绑定邮箱
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
