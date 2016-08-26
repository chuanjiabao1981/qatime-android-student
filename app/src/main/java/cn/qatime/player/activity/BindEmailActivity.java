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
import cn.qatime.player.base.BaseApplication;
import libraryextra.utils.StringUtils;

/**
 * Created by lenovo on 2016/8/17.
 */
public class BindEmailActivity extends BaseActivity implements View.OnClickListener {
    private TimeCount time;
    private TextView textGetcode;
    private Button buttonOver;
    private EditText inputNewEmail;
    private EditText confirmNewEmail;
    private EditText code;
    private TextView currentPhone;


    private void assignViews() {
        currentPhone = (TextView) findViewById(R.id.current_phone);
        code = (EditText) findViewById(R.id.code);
        textGetcode = (TextView) findViewById(R.id.text_getcode);
        inputNewEmail = (EditText) findViewById(R.id.input_new_email);
        confirmNewEmail = (EditText) findViewById(R.id.confirm_new_email);
        buttonOver = (Button) findViewById(R.id.button_over);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_email);

        initView();
        time = new TimeCount(60000, 1000);
    }

    private void initView() {
        setTitle(getResources().getString(R.string.bind_email));
        assignViews();

        currentPhone.setText(BaseApplication.getProfile().getData().getUser().getLogin_mobile() + "");
        inputNewEmail.setHint(StringUtils.getSpannedString(this,R.string.hint_input_email));
        confirmNewEmail.setHint(StringUtils.getSpannedString(this,R.string.hint_input_again));
        code.setHint(StringUtils.getSpannedString(this,R.string.hint_input_code));

        //TODO 获取手机号 设置TextView
        textGetcode.setOnClickListener(this);
        buttonOver.setOnClickListener(this);
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
            textGetcode.setText(getResources().getString(R.string.getcode));
            textGetcode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            textGetcode.setEnabled(false);//防止重复点击
            textGetcode.setText(millisUntilFinished / 1000 + "s后重新获取");
        }
    }
}
