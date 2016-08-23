package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * Created by lenovo on 2016/8/17.
 */
public class UnbindPhoneActivity extends BaseActivity implements View.OnClickListener {

    private TextView getcode;
    private Button button_next;
    private TextView current_phone;
    private EditText code;
    private TimeCount time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unbind_phone);
        initView();
        time = new TimeCount(60000, 1000);
    }

    private void initView() {
        setTitle(getResources().getString(R.string.bind_phone_number));
        getcode = (TextView) findViewById(R.id.text_getcode);
        button_next= (Button) findViewById(R.id.button_next);
        current_phone = (TextView) findViewById(R.id.current_phone);
        code = (EditText) findViewById(R.id.code);
        //TODO 获取手机号 设置TextView

        getcode.setOnClickListener(this);
        button_next.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_getcode:
                //TODO 发送验证短信
                Toast.makeText(getApplicationContext(),"验证码已经发送至137****5678，请注意查收",Toast.LENGTH_LONG).show();
                time.start();
                break;
            case R.id.button_next:
                //TODO 验证验证码跳转页面
                Intent intent=new Intent(this,BindPhoneActivity.class);
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
