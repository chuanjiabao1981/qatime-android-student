package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

public class SecurityManagerActivity extends BaseActivity implements View.OnClickListener {

    private View change_password;
    private View parent_phone;
    private View bind_email;
    private View bind_phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_manager);

        initView();
        initData();
    }

    private void initData() {

    }


    private void initView() {
        setTitle(getResources().getString(R.string.security_management));
        bind_phone_number = findViewById(R.id.bind_phone_number);
        bind_email = findViewById(R.id.bind_email);
        parent_phone = findViewById(R.id.parent_phone_number);
        change_password = findViewById(R.id.change_password);
        //TODO 获取号码信息设置TEXTVIEW
        bind_phone_number.setOnClickListener(this);
        bind_email.setOnClickListener(this);
        parent_phone.setOnClickListener(this);
        change_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_phone_number://绑定手机
                Intent intent = new Intent(this, UnbindPhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.bind_email://绑定邮箱
                intent = new Intent(this, BindEmailActivity.class);
                startActivity(intent);
                break;
            case R.id.parent_phone_number://家长手机
                intent = new Intent(this, ParentPhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.change_password://修改密码
                intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
        }
    }
}
