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
                if (phone.getText().toString().length() == 11) {
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

        if (TextUtils.isEmpty(phone.getText().toString())) {
            Toast.makeText(this, getResources().getString(R.string.account_can_not_be_empty), Toast.LENGTH_SHORT).show();
            next.setClickable(true);
            return;
        } else {
            if (phone.getText().toString().length() == 11) {
                //密码
                if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(this, getResources().getString(R.string.password_can_not_be_empty), Toast.LENGTH_SHORT).show();
                    next.setClickable(true);
                    return;
                } else {
                    if (!password.getText().toString().equals(repassword.getText().toString())) {
                        Toast.makeText(this, getResources().getString(R.string.password_and_repassword_are_incongruous), Toast.LENGTH_SHORT).show();
                        next.setClickable(true);
                        return;
                    } else {
                        //验证码
                        if (TextUtils.isEmpty(code.getText().toString())) {
                            Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
                            next.setClickable(true);
                            return;
                        } else {

                            //注册码
                            if (TextUtils.isEmpty(registercode.getText().toString())) {
                                Toast.makeText(this, getResources().getString(R.string.enter_the_register_code), Toast.LENGTH_SHORT).show();
                                next.setClickable(true);
                                return;
                            } else {
                                //下一步
                                Intent intent = new Intent(RegisterActivity.this, RegisterPerfectActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }
            } else {//手机号不为11位
                Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
                next.setClickable(true);
                return;
            }
        }
    }
}