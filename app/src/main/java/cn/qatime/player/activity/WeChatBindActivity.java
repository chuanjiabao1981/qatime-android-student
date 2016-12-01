package cn.qatime.player.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2016/12/1 11:29
 * @Describe 微信绑定页面
 */

public class WeChatBindActivity extends BaseActivity implements View.OnClickListener {
    private EditText phone;
    private EditText code;
    private Button getCode;
    private EditText password;
    private TextView grade;
    private CheckBox checkBox;
    private TextView agreement;
    private Button next;
    private TimeCount time;

    private void assignViews() {
        phone = (EditText) findViewById(R.id.phone);
        code = (EditText) findViewById(R.id.code);
        getCode = (Button) findViewById(R.id.get_code);
        password = (EditText) findViewById(R.id.password);
        grade = (TextView) findViewById(R.id.grade);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        agreement = (TextView) findViewById(R.id.agreement);
        next = (Button) findViewById(R.id.next);

        time = new TimeCount(60000, 1000);
        getCode.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                next.setEnabled(isChecked);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_wechat);
        setTitle("绑定");
        assignViews();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code://获取验证码
                if (StringUtils.isPhone(phone.getText().toString().trim())) {
                    time.start();
                    // TODO: 2016/12/1 获取验证码
                } else {
                    Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.next:
                next();
                break;
            case R.id.agreement:
                //// TODO: 2016/8/24 点击协议查看
                break;
        }
    }

    private void next() {
        if (StringUtils.isNullOrBlanK(phone.getText().toString().trim())) {//账号为空
            Toast.makeText(this, getResources().getString(R.string.account_can_not_be_empty), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }

        if (!StringUtils.isPhone(phone.getText().toString().trim())) {//手机号不正确
            Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }
        if (StringUtils.isNullOrBlanK(code.getText().toString().trim())) { //验证码
            Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }
        if (!StringUtils.isGoodPWD(password.getText().toString().trim())) {
            Toast.makeText(this, getResources().getString(R.string.password_6_16), Toast.LENGTH_LONG).show();
            next.setClickable(true);
            return;
        }
    }

    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            getCode.setText(getResources().getString(R.string.get_verification_code));
            getCode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            getCode.setEnabled(false);//防止重复点击
            getCode.setText(millisUntilFinished / 1000 + "s");
        }
    }
}
