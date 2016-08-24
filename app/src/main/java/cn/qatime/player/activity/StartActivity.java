package cn.qatime.player.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.orhanobut.logger.Logger;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import libraryextra.bean.Profile;
import libraryextra.utils.SPUtils;
import libraryextra.utils.StringUtils;

/**
 * 起始页
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
                    Logger.e("第一次登陆");
                    StartActivity.this.startActivity(new Intent(StartActivity.this, GuideActivity.class));
                    StartActivity.this.finish();

                } else {
                    Logger.e("no第一次登陆");
                    BaseApplication.setProfile(SPUtils.getObject(StartActivity.this,"profile", Profile.class));
                    if (!StringUtils.isNullOrBlanK(BaseApplication.getProfile().getToken())) {//token不空  直接自动登录到mianactivity
                        StartActivity.this.startActivity(new Intent(StartActivity.this, MainActivity.class));
                        StartActivity.this.finish();
                    } else {
                        StartActivity.this.startActivity(new Intent(StartActivity.this, LoginActivity.class));
                        StartActivity.this.finish();
                    }
                }
            }
        }, 2000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }
}
