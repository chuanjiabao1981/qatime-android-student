package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.Constant;

/**
 * @author luntify
 * @date 2016/8/10 10:34
 * @Description
 */
public class SystemSettingActivity extends BaseActivity implements View.OnClickListener {
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        setTitle(getResources().getString(R.string.system_setting));
        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:// 退出登录
                setResult(Constant.RESPONSE_EXIT_LOGIN);
                BaseApplication.clearToken();
                Intent intent = new Intent(SystemSettingActivity.this,LoginActivity.class);
                intent.putExtra("sign","exit_login");
                startActivity(intent);
                finish();
                break;
        }

    }
}
