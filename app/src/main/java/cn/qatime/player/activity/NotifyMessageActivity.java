package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.umeng.analytics.MobclickAgent;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import libraryextra.utils.SPUtils;

/**
 * Created by lenovo on 2016/8/22.
 */
public class NotifyMessageActivity extends BaseActivity implements View.OnClickListener {

    private CheckBox cbVoice;
    private CheckBox cbShake;
    private View shake;
    private View voice;
    private boolean voiceStatus;
    private boolean shakeStatus;


    private void assignViews() {
        //TODO 待修改
        voice = findViewById(R.id.voice);
        cbVoice = (CheckBox) findViewById(R.id.cb_voice);
        shake = findViewById(R.id.shake);
        cbShake = (CheckBox) findViewById(R.id.cb_shake);
//        cbVoice.setChecked(UserPreferences.getRingToggle());
//        cbShake.setChecked(UserPreferences.getVibrateToggle());
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
        setTitle(getResourceString(R.string.notify_message));
        assignViews();
        voice.setOnClickListener(this);
        shake.setOnClickListener(this);
        boolean shakeStatus = (boolean) SPUtils.get(this, "shake_status", true);
        cbShake.setChecked(shakeStatus);
        boolean voiceStatus = (boolean) SPUtils.get(this, "voice_status", true);
        cbVoice.setChecked(voiceStatus);
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
//        boolean check = UserPreferences.getVibrateToggle();
//        cbShake.setChecked(!check);
//        UserPreferences.setVibrateToggle(!check);

        shakeStatus = !(boolean) SPUtils.get(this, "shake_status", true);
        BaseApplication.setOptions((boolean) SPUtils.get(this, "voice_status", true), shakeStatus);
        if (shakeStatus) {
            SPUtils.put(this, "shake_status", true);
            cbShake.setChecked(true);
        } else {
            SPUtils.put(this, "shake_status", false);
            cbShake.setChecked(false);
        }
    }

    private void setVoiceStatus() {
//        boolean check = UserPreferences.getRingToggle();
//        cbVoice.setChecked(!check);
//        UserPreferences.setRingToggle(!check)
        voiceStatus = !(boolean) SPUtils.get(this, "voice_status", true);
        BaseApplication.setOptions(voiceStatus, (boolean) SPUtils.get(this, "shake_status", true));
        if (voiceStatus) {
            SPUtils.put(this, "voice_status", true);
            cbVoice.setChecked(true);
        } else {
            SPUtils.put(this, "voice_status", false);
            cbVoice.setChecked(false);
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
