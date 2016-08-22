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
    private CheckBox iv_voice;
    private CheckBox iv_shake;
    private boolean voice_status;
    private boolean shake_status;

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
        voice = findViewById(R.id.voice);
        shake = findViewById(R.id.shake);
        iv_voice = (CheckBox) findViewById(R.id.cb_voice);
        iv_shake = (CheckBox) findViewById(R.id.cb_shake);

        setVoiceStatus();
        setShakeStatus();

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
        shake_status = (boolean) SPUtils.get(this, "shake_status", true);

        if(shake_status){
             SPUtils.put(this, "shake_status", false);
        }else{
            SPUtils.put(this, "shake_status", true);
        }
    }

    private void setVoiceStatus() {
        voice_status = (boolean) SPUtils.get(this, "voice_status", true);
        if(voice_status){
            SPUtils.put(this, "voice_status", false);
        }else{
            SPUtils.put(this, "voice_status", true);
        }
    }
}
