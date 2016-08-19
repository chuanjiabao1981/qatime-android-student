package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.AppUtils;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DataCleanUtils;

/**
 * @author luntify
 * @date 2016/8/10 10:34
 * @Description
 */
public class SystemSettingActivity extends BaseActivity implements View.OnClickListener {
    private Button exit;
    private View notify_setting;
    private View check_update;
    private View clean_cache;
    private View learning_process;
    private View feedback;
    private TextView version;
    private TextView cache_size;
    private String totalCacheSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        initView();
        initData();
    }

    private void initView() {
        setTitle(getResources().getString(R.string.system_setting));
        exit = (Button) findViewById(R.id.exit);
        notify_setting = findViewById(R.id.notify_setting);
        check_update = findViewById(R.id.check_update);
        clean_cache = findViewById(R.id.clean_cache);
        learning_process = findViewById(R.id.learning_process);
        feedback = findViewById(R.id.feedback);

        version = (TextView) findViewById(R.id.version);
        cache_size = (TextView) findViewById(R.id.cache_size);


        exit.setOnClickListener(this);
        notify_setting.setOnClickListener(this);
        check_update.setOnClickListener(this);
        clean_cache.setOnClickListener(this);
        learning_process.setOnClickListener(this);
        feedback.setOnClickListener(this);

    }

    private void initData() {
        version.setText(getResources().getString(R.string.current_version) + AppUtils.getVersionName(this));
        setCache();
    }

    private void setCache() {
        try {
            totalCacheSize = DataCleanUtils.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cache_size.setText(totalCacheSize);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:// 退出登录
                setResult(Constant.RESPONSE_EXIT_LOGIN);
                BaseApplication.clearToken();
                Intent intent = new Intent(SystemSettingActivity.this, LoginActivity.class);
                intent.putExtra("sign", "exit_login");
                startActivity(intent);
                finish();
                break;
            case R.id.notify_setting:
                break;
            case R.id.check_update:
                //TODO 检查版本，进行更新



                break;
            case R.id.clean_cache:
                //TODO 弹出对话框提示

                DataCleanUtils.clearAllCache(this);
                setCache();
                break;
            case R.id.learning_process:

                break;
            case R.id.feedback:

                break;
        }

    }
}
