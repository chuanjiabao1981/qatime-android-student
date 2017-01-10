package cn.qatime.player.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.fragment.PlayBackFloatFragment;
import cn.qatime.player.presenter.PlayBackVideoPresenter;
import cn.qatime.player.utils.PlayBackVideoInterface;
import cn.qatime.player.utils.ScreenSwitchUtils;
import cn.qatime.player.view.NEVideoView;
import libraryextra.utils.ScreenUtils;

/**
 * @author lungtify
 * @Time 2017/1/10 9:50
 * @Describe 回放页面
 */

public class NEVideoPlaybackActivity extends BaseActivity implements PlayBackVideoInterface {
    RelativeLayout control;
    RelativeLayout videolayout;
    ListView listView;
    private PlayBackFloatFragment playBackFloatFragment;
    private ScreenSwitchUtils screenSwitchUtils;
    private NEVideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nevideo_playback);
        screenSwitchUtils = ScreenSwitchUtils.init(this.getApplicationContext());

        initView();

        video.setVideoPath("http://vodb98fi13b.vod.126.net/vodb98fi13b/0de484f0121f46818f2e912773d08132_1484023388416_1484025548690_1314330-00002.mp4");
        video.start();
    }

    private void initView() {
        int screenW = ScreenUtils.getScreenWidth(NEVideoPlaybackActivity.this);

        video = (NEVideoView) findViewById(R.id.video);
        video.setmBufferStrategy(1);
        control = (RelativeLayout) findViewById(R.id.control);
        videolayout = (RelativeLayout) findViewById(R.id.video_layout);
        listView = (ListView) findViewById(R.id.listView);

        PlayBackVideoPresenter playBackVideoPresenter = new PlayBackVideoPresenter(this);
        playBackFloatFragment = new PlayBackFloatFragment(playBackVideoPresenter);
        getSupportFragmentManager().beginTransaction().replace(R.id.control, playBackFloatFragment).commit();

        ViewGroup.LayoutParams mainVideoParam = videolayout.getLayoutParams();
        mainVideoParam.width = -1;
        mainVideoParam.height = screenW * 9 / 16;
        videolayout.setLayoutParams(mainVideoParam);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                screenSwitchUtils.start(NEVideoPlaybackActivity.this);
            }
        }, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        screenSwitchUtils.stop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (screenSwitchUtils.isPortrait()) {//竖屏
            playBackFloatFragment.setPortrait(true);
            ViewGroup.LayoutParams params = videolayout.getLayoutParams();
            params.width = -1;
            params.height = ScreenUtils.getScreenWidth(NEVideoPlaybackActivity.this) * 9 / 16;
            videolayout.setLayoutParams(params);
        } else {//横屏
            playBackFloatFragment.setPortrait(false);
            ViewGroup.LayoutParams params = videolayout.getLayoutParams();
            params.width = -1;
            params.height = -1;
            videolayout.setLayoutParams(params);
        }
    }

    @Override
    public void onBackPressed() {
        if (!screenSwitchUtils.isPortrait()) {
            Logger.e("orta 返回竖屏");
            screenSwitchUtils.toggleScreen();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void fullScreen() {
        if (screenSwitchUtils != null) {
            screenSwitchUtils.toggleScreen();
        }
    }

    @Override
    public void exit() {
        onBackPressed();
    }

    @Override
    public void playOrPause() {
        if (video.isPlaying()) {
            video.pause();
        } else {
            video.start();
        }
    }
}
