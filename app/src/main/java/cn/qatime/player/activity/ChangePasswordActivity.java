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

/**
 * Created by lenovo on 2016/8/17.
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextView forget_password;
    private EditText password;
    private EditText new_password;
    private EditText confirm_new_password;
    private ImageView match_pwd1;
    private ImageView match_pwd2;
    private Button button_over;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
    }

    private void initView() {
        setTitle(getResources().getString(R.string.change_password));
        forget_password = (TextView) findViewById(R.id.forget_password);
        button_over = (Button) findViewById(R.id.button_over);
        password = (EditText) findViewById(R.id.password);
        new_password = (EditText) findViewById(R.id.new_password);
        confirm_new_password = (EditText) findViewById(R.id.confirm_new_password);
        match_pwd1 = (ImageView) findViewById(R.id.match_pwd1);
        match_pwd2 = (ImageView) findViewById(R.id.match_pwd2);
        forget_password.setOnClickListener(this);
        button_over.setOnClickListener(this);
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
