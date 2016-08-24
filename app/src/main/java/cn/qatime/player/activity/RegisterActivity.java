package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.utils.StringUtils;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    EditText phone;
    EditText code;
    EditText registercode;
    Button getcode;
    EditText password;
    EditText repassword;
    CheckBox checkBox;
    TextView agreement;
    Button next;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle(getResources().getString(R.string.set_for_login));
        initView();
        time = new TimeCount(60000, 1000);
    }

    private void initView() {
        phone = (EditText) findViewById(R.id.phone);
        code = (EditText) findViewById(R.id.code);
        getcode = (Button) findViewById(R.id.get_code);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        registercode = (EditText) findViewById(R.id.register_code);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        next = (Button) findViewById(R.id.next);
        agreement = (TextView) findViewById(R.id.agreement);
        getcode.setOnClickListener(this);
        next.setOnClickListener(this);
        agreement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code:
                if (StringUtils.isPhone(phone.getText().toString())) {
                    time.start();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.next:

                next();
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

    private void next() {

        if (StringUtils.isNullOrBlanK(phone.getText().toString())) {//账号为空
            Toast.makeText(this, getResources().getString(R.string.account_can_not_be_empty), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }
        if (!StringUtils.isPhone(phone.getText().toString())) {//手机号不正确
            Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }
        if (StringUtils.isNullOrBlanK(password.getText().toString())) {  //密码为空
            Toast.makeText(this, getResources().getString(R.string.password_can_not_be_empty), Toast.LENGTH_LONG).show();
            next.setClickable(true);
            return;
        }
        if (StringUtils.isNullOrBlanK(repassword.getText().toString())) {  //确认密码为空
            Toast.makeText(this, getResources().getString(R.string.password_can_not_be_empty), Toast.LENGTH_LONG).show();
            next.setClickable(true);
            return;
        }
        if (!password.getText().toString().equals(repassword.getText().toString())) {//前后不一致
            Toast.makeText(this, getResources().getString(R.string.password_and_repassword_are_incongruous), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }

        if (StringUtils.isNullOrBlanK(code.getText().toString())) { //验证码
            Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }

        if (StringUtils.isNullOrBlanK(registercode.getText().toString())) {   //注册码
            Toast.makeText(this, getResources().getString(R.string.enter_the_register_code), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        }
        //下一步
        Intent intent = new Intent(RegisterActivity.this, RegisterPerfectActivity.class);
        startActivity(intent);
    }


}


