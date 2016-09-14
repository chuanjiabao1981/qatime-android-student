package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * Created by lenovo on 2016/8/22.
 */
public class NotifySettingActivity extends BaseActivity {

    private View notifyMessage;
    private View notifyClasses;


    private void assignViews() {
        notifyMessage = (LinearLayout) findViewById(R.id.notify_message);
        notifyClasses = (LinearLayout) findViewById(R.id.notify_classes);
    }

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
        setTitle(getResourceString(R.string.notify_setting));
        assignViews();

        notifyClasses.setOnClickListener(this::onClick);
        notifyMessage.setOnClickListener(this::onClick);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notify_message:
                Intent intent = new Intent(this, NotifyMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.notify_classes:
                intent = new Intent(this, NotifyCourseActivity.class);
                startActivity(intent);
                break;
        }
    }
}
