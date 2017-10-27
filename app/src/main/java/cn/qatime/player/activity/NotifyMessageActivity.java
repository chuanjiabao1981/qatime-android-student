package cn.qatime.player.activity;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.umeng.analytics.MobclickAgent;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.config.UserPreferences;
import cn.qatime.player.utils.SPUtils;

/**
 * Created by lenovo on 2016/8/22.
 */
public class NotifyMessageActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox voice;
    private CheckBox shake;
    private SwitchCompat notifyStatus;
    /**
     * 状态是否初始化完成
     */
    private boolean initOver;


    private void assignViews() {
        voice = (CheckBox) findViewById(R.id.voice);
        shake = (CheckBox) findViewById(R.id.shake);
        notifyStatus = (SwitchCompat) findViewById(R.id.notify_status);
        voice.setOnCheckedChangeListener(this);
        shake.setOnCheckedChangeListener(this);
        notifyStatus.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView() {
        setContentView(R.layout.activity_notify_message);
        setTitles(getResourceString(R.string.notify_message));
        assignViews();
        shake.setChecked(UserPreferences.getVibrateToggle());
        voice.setChecked(UserPreferences.getRingToggle());
        notifyStatus.setChecked(UserPreferences.getNotificationToggle());
        initOver = true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!initOver) {//避免重复设置
            return;
        }
        switch (buttonView.getId()) {
            case R.id.voice:
                UserPreferences.setRingToggle(isChecked);
                BaseApplication.setOptions(isChecked, UserPreferences.getVibrateToggle());
                break;
            case R.id.shake:
                UserPreferences.setVibrateToggle(isChecked);
                BaseApplication.setOptions(UserPreferences.getRingToggle(), isChecked);
                break;
            case R.id.notify_status://消息提醒开关
                UserPreferences.setNotificationToggle(isChecked);
                NIMClient.toggleNotification(isChecked);
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
