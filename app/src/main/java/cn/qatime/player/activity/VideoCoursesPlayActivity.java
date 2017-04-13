package cn.qatime.player.activity;

import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.IOException;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.fragment.VideoCoursesFloatFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_courses_play);
        initView();
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
                mMediaPlayer.start();
            }
        }, 5000);
    }

    private void initView() {
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
                return false;
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isCreated = true;
        // Create a new media player and set the listeners
        mMediaPlayer = new MediaPlayer();
//        mMediaPlayer.setDataSource(path);
        mMediaPlayer.setDisplay(holder);
//        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
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
}
