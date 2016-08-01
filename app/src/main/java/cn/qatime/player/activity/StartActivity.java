package cn.qatime.player.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;

import cn.qatime.player.R;
import cn.qatime.player.utils.LogUtils;

/**
 *起始页
 */
public class StartActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getSharedPreferences("first", MODE_PRIVATE).getBoolean("firstlogin", true)) {
                    LogUtils.e("第一次登陆");
                    StartActivity.this.startActivity(new Intent(StartActivity.this, GuideActivity.class));
                    StartActivity.this.finish();

                } else {
                    LogUtils.e("no第一次登陆");
                    StartActivity.this.startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    StartActivity.this.finish();
                }
            }
        }, 2000);
    }
}
