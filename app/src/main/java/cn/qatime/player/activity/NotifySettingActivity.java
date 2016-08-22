package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * Created by lenovo on 2016/8/22.
 */
public class NotifySettingActivity extends BaseActivity implements View.OnClickListener {

    private View notify_message;
    private View notify_classes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        setContentView(R.layout.activity_notify_setting);
        setTitle("提醒设置");
        notify_message = findViewById(R.id.notify_message);
        notify_classes = findViewById(R.id.notify_classes);

        notify_classes.setOnClickListener(this);
        notify_message.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notify_message:
                Intent intent = new Intent(this, NotifyMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.notify_classes:
                intent = new Intent(this, NotifyClassesActivity.class);
                startActivity(intent);
                break;
        }
    }
}
