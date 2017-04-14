package cn.qatime.player.activity;

import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.fragment.VideoCoursesFloatFragment;
import cn.qatime.player.utils.ScreenSwitchUtils;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.ScreenUtils;

/**
 * @author lungtify
 * @Time 2017/4/11 15:45
 * @Describe 视频课观看页面
 */

public class VideoCoursesPlayActivity extends BaseFragmentActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {

    private VideoCoursesFloatFragment floatFragment;
    private SurfaceHolder holder;
    private boolean isCreated = false;
    private MediaPlayer mMediaPlayer;
    private boolean isPrepared = false;
    private ScreenSwitchUtils screenSwitchUtils;
    private RelativeLayout video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_courses_play);
        if (!Vitamio.isInitialized(this)) {
            Toast.makeText(this, "播放器初始化失败", Toast.LENGTH_SHORT).show();
            return;
        }
        initView();

        screenSwitchUtils = ScreenSwitchUtils.init(this.getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isCreated) {
                        mMediaPlayer.setDataSource("http://192.168.1.136:8080/media/28.mp4");
                        mMediaPlayer.prepareAsync();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                mMediaPlayer.start();
            }
        }, 5000);
    }

    private void initView() {
        video = (RelativeLayout) findViewById(R.id.video);
        SurfaceView mPreview = (SurfaceView) findViewById(R.id.surfaceView);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888);
        floatFragment = new VideoCoursesFloatFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.control, floatFragment).commit();
        floatFragment.setCallback(new VideoCoursesFloatFragment.CallBack() {
            @Override
            public void exit() {
                onBackPressed();
            }

            @Override
            public void pause() {
                mMediaPlayer.pause();
            }

            @Override
            public void play() {
                mMediaPlayer.start();
            }

            @Override
            public boolean isPlaying() {
                return mMediaPlayer != null && mMediaPlayer.isPlaying();
            }

            @Override
            public boolean isPortrait() {
                return screenSwitchUtils.isPortrait();
            }

            @Override
            public long getCurrentPosition() {
                return mMediaPlayer == null ? 0 : mMediaPlayer.getCurrentPosition();
            }

            @Override
            public long getDuration() {
                return mMediaPlayer == null ? 0 : mMediaPlayer.getDuration();

            }

            @Override
            public void seekTo(long position) {
                mMediaPlayer.seekTo(position);
            }

            @Override
            public void zoom() {
                screenSwitchUtils.toggleScreen();
            }

            @Override
            public boolean isPrepared() {
                return isPrepared;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!screenSwitchUtils.isPortrait()) {
            Logger.e("orta 返回竖屏");
            screenSwitchUtils.toggleScreen();
//            if (floatFragment != null) {
//                floatFragment.setPortrait(true);
//            }
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isCreated = true;
        // Create a new media player and set the listeners
        mMediaPlayer = new MediaPlayer(this);
//        mMediaPlayer.setDataSource(path);
        mMediaPlayer.setDisplay(holder);
//        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setScreenOnWhilePlaying(true);
//        mMediaPlayer.setOnVideoSizeChangedListener(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isCreated = false;
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            isPrepared = false;
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        mMediaPlayer.start();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        floatFragment.setBuffering(percent, mp.getDuration());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (screenSwitchUtils.isPortrait()) {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            // 取消全屏设置
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            floatFragment.setPortrait(true);
            ViewGroup.LayoutParams params = video.getLayoutParams();
            params.height = DensityUtils.dp2px(this, 260);
            video.setLayoutParams(params);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            floatFragment.setPortrait(false);
            ViewGroup.LayoutParams params = video.getLayoutParams();
            params.height = -1;
            video.setLayoutParams(params);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                screenSwitchUtils.start(VideoCoursesPlayActivity.this);
            }
        }, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        screenSwitchUtils.stop();
    }

}
