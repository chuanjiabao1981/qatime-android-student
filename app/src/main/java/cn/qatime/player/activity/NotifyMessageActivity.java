package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.utils.SPUtils;

/**
 * Created by lenovo on 2016/8/22.
 */
public class NotifyMessageActivity extends BaseActivity implements View.OnClickListener {

    private View voice;
    private View shake;
    private ImageView iv_voice;
    private ImageView iv_shake;
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
        iv_voice = (ImageView) findViewById(R.id.iv_voice);
        iv_shake = (ImageView) findViewById(R.id.iv_shake);

        setVoiceStatus();
        setShakeStatus();
        voice.setOnClickListener(this);
        shake.setOnClickListener(this);
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
            iv_shake.setImageResource(R.mipmap.pay_success);
        }else{
            SPUtils.put(this, "shake_status", true);
            iv_shake.setImageResource(R.mipmap.pay_faild);
        }
    }

    private void setVoiceStatus() {
        voice_status = (boolean) SPUtils.get(this, "voice_status", true);
        if(voice_status){
            SPUtils.put(this, "voice_status", false);
            iv_voice.setImageResource(R.mipmap.pay_success);
        }else{
            SPUtils.put(this, "voice_status", true);
            iv_voice.setImageResource(R.mipmap.pay_faild);
        }
    }
}
