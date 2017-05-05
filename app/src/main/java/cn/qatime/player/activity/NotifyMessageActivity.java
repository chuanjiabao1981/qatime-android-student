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
        boolean shakeStatus = (boolean) SPUtils.get(this, "shake_status", true);
        shake.setChecked(shakeStatus);
        boolean voiceStatus = (boolean) SPUtils.get(this, "voice_status", true);
        voice.setChecked(voiceStatus);
        boolean nofityStatus = (boolean) SPUtils.get(this, "notify_status", true);
        notifyStatus.setChecked(nofityStatus);
        initOver = true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!initOver){//避免重复设置
            return;
        }
        switch (buttonView.getId()) {
            case R.id.voice:
                SPUtils.put(this, "voice_status", isChecked);
                BaseApplication.setOptions(isChecked, (boolean) SPUtils.get(this, "shake_status", true));
                break;
            case R.id.shake:
                SPUtils.put(this, "shake_status", isChecked);
                BaseApplication.setOptions((boolean) SPUtils.get(this, "voice_status", true), isChecked);
                break;
            case R.id.notify_status://消息提醒开关
                BaseApplication.setChatMessageNotifyStatus(isChecked);
                /**
                 * 设置最近联系人的消息为已读
                 *
                 * @param account,    聊天对象帐号，或者以下两个值：
                 *                    {@link #MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
                 *                    {@link #MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
                 */
                NIMClient.getService(MsgService.class).setChattingAccount(BaseApplication.isChatMessageNotifyStatus()?MsgService.MSG_CHATTING_ACCOUNT_NONE:MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
                SPUtils.put(this, "notify_status", isChecked);
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
