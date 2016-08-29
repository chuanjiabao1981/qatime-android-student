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

/**
 * Created by lenovo on 2016/8/17.
 */
public class ParentPhoneActivity extends BaseActivity implements View.OnClickListener {
    private TextView textGetcode;
    private Button buttonOver;
    private EditText newParentPhone;
    private EditText code;
    private EditText password;
    private TimeCount time;
    private TextView currentParentPhone;


    private void assignViews() {
        currentParentPhone = (TextView) findViewById(R.id.current_parent_phone);
        password = (EditText) findViewById(R.id.password);
        newParentPhone = (EditText) findViewById(R.id.new_parent_phone);
        code = (EditText) findViewById(R.id.code);
        textGetcode = (TextView) findViewById(R.id.text_getcode);
        buttonOver = (Button) findViewById(R.id.button_over);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_phone);

        initView();
        time = new TimeCount(60000, 1000);
    }

    private void initView() {
        setTitle(getResources().getString(R.string.parent_phone_number));
        assignViews();

        password.setHint(StringUtils.getSpannedString(this, R.string.hint_input_password));
        newParentPhone.setHint(StringUtils.getSpannedString(this, R.string.new_parent_phone));
        code.setHint(StringUtils.getSpannedString(this, R.string.hint_input_code));
        //TODO 获取家长手机号 设置TextView


        textGetcode.setOnClickListener(this);
        buttonOver.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_getcode:
                //TODO 发送验证短信
                String phone = newParentPhone.getText().toString().trim();
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
