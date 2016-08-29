package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.config.UserPreferences;
import libraryextra.utils.SPUtils;

/**
 * Created by lenovo on 2016/8/22.
 */
public class NotifyMessageActivity extends BaseActivity implements View.OnClickListener {

    private CheckBox cbVoice;
    private CheckBox cbShake;


    private void assignViews() {
        View voice = findViewById(R.id.voice);
        cbVoice = (CheckBox) findViewById(R.id.cb_voice);
        View shake = findViewById(R.id.shake);
        cbShake = (CheckBox) findViewById(R.id.cb_shake);
        cbVoice.setChecked(UserPreferences.getRingToggle());
        cbShake.setChecked(UserPreferences.getVibrateToggle());
        voice.setOnClickListener(this);
        shake.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView() {
        setContentView(R.layout.activity_notify_message);
        setTitle("消息提醒");
        assignViews();

        setVoiceStatus();
        setShakeStatus();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voice:
                setVoiceStatus();
                break;
            case R.id.shake:
                setShakeStatus();

                break;
        }
    }

    private void setShakeStatus() {
        cbShake.setChecked(!UserPreferences.getVibrateToggle());
        UserPreferences.setVibrateToggle(!UserPreferences.getVibrateToggle());
    }

    private void setVoiceStatus() {
       cbVoice.setChecked(!UserPreferences.getRingToggle());
        UserPreferences.setRingToggle(!UserPreferences.getRingToggle());
    }
}
