package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.utils.SPUtils;

/**
 * Created by lenovo on 2016/8/22.
 */
public class NotifyMessageActivity extends BaseActivity implements View.OnClickListener {

    private View voice;
    private View shake;
    private CheckBox cbVoice;
    private CheckBox cbShake;
    private boolean voiceStatus;
    private boolean shakeStatus;


    private void assignViews() {
        voice = findViewById(R.id.voice);
        cbVoice = (CheckBox) findViewById(R.id.cb_voice);
        shake = findViewById(R.id.shake);
        cbShake = (CheckBox) findViewById(R.id.cb_shake);

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
        setContentView(R.layout.activity_notify_message);
        setTitle("消息提醒");
        assignViews();
        voice.setOnClickListener(this);
        shake.setOnClickListener(this);
        shakeStatus = (boolean) SPUtils.get(this, "shake_status", true);
        cbShake.setChecked(shakeStatus);
        voiceStatus = (boolean) SPUtils.get(this, "voice_status", true);
        cbVoice.setChecked(voiceStatus);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.voice:
                //TODO 开关声音
                setVoiceStatus();

                break;
            case R.id.shake:
                //TODO 开关震动

                setShakeStatus();

                break;
        }
    }

    private void setShakeStatus() {
        shakeStatus = (boolean) SPUtils.get(this, "shake_status", true);
        if (shakeStatus) {
            SPUtils.put(this, "shake_status", false);
            cbShake.setChecked(false);
        } else {
            SPUtils.put(this, "shake_status", true);
            cbShake.setChecked(true);
        }
    }

    private void setVoiceStatus() {
        voiceStatus = (boolean) SPUtils.get(this, "voice_status", true);
        if (voiceStatus) {
            SPUtils.put(this, "voice_status", false);
            cbVoice.setChecked(false);
        } else {
            SPUtils.put(this, "voice_status", true);
            cbVoice.setChecked(true);
        }
    }
}
