package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.utils.StringUtils;

/**
 * Created by lenovo on 2016/8/17.
 */
public class ParentPhoneActivity extends BaseActivity implements View.OnClickListener {
    private Button buttonOver;
    private EditText password;



    private void assignViews() {
        password = (EditText) findViewById(R.id.password);
        buttonOver = (Button) findViewById(R.id.button_over);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_phone);
        initView();
    }

    private void initView() {
        setTitles(getResources().getString(R.string.parent_phone_number));
        assignViews();
        password.setHint(StringUtils.getSpannedString(this, R.string.hint_input_password));
        buttonOver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_over:
                if (!StringUtils.isGoodPWD(password.getText().toString().trim())) {
                    Toast.makeText(this, getResources().getString(R.string.password_format_error), Toast.LENGTH_LONG).show();
                    return;
                }
                // TODO: 2017/3/9 验证密码
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
