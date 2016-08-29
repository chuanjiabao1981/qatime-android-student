package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.utils.StringUtils;

/**
 * Created by lenovo on 2016/8/17.
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextView forgetPassword;
    private EditText password;
    private EditText newPassword;
    private EditText confirmNewPassword;
    private ImageView matchPwd1;
    private ImageView matchPwd2;
    private Button buttonOver;


    private void assignViews() {
        password = (EditText) findViewById(R.id.password);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        matchPwd1 = (ImageView) findViewById(R.id.match_pwd1);
        confirmNewPassword = (EditText) findViewById(R.id.confirm_new_password);
        matchPwd2 = (ImageView) findViewById(R.id.match_pwd2);
        buttonOver = (Button) findViewById(R.id.button_over);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
    }

    private void initView() {
        setTitle(getResources().getString(R.string.change_password));
        assignViews();

        password.setHint(StringUtils.getSpannedString(this, R.string.hint_input_current_password));
        newPassword.setHint(StringUtils.getSpannedString(this, R.string.hint_6_16_password));
        confirmNewPassword.setHint(StringUtils.getSpannedString(this, R.string.hint_input_again));

        matchPwd1 = (ImageView) findViewById(R.id.match_pwd1);
        matchPwd2 = (ImageView) findViewById(R.id.match_pwd2);
        forgetPassword.setOnClickListener(this);
        buttonOver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forget_password:
                Intent intent = new Intent(this, ForgetPasswordActivity.class);
                intent.putExtra("status_login", true);
                startActivity(intent);
                break;
            case R.id.button_over:
                intent = new Intent(this, SecurityManagerActivity.class);
                startActivity(intent);
                break;
        }
    }
}
