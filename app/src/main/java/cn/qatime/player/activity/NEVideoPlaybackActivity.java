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

        video.setVideoPath("http://192.168.1.125:8080/media/28.mp4");
        video.start();
        Logger.e("*********" + (6311368 * 1000 / 1000.0));
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
        playBackFloatFragment.setMediaPlayer(video);
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
            playBackFloatFragment.setPlayOrPause(false);
            video.pause();
        } else {
            video.start();
            playBackFloatFragment.setPlayOrPause(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        video.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        video.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        video.release_resource();
    }
}
