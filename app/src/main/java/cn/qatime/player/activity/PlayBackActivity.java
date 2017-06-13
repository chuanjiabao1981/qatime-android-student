package cn.qatime.player.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.fragment.FragmentPlayBack;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import libraryextra.utils.DensityUtils;

/**
 * @author lungtify
 * @Time 2017/6/12 14:39
 * @Describe
 */

public class PlayBackActivity extends BaseFragmentActivity implements SurfaceHolder.Callback {
    private RelativeLayout video;
    private SurfaceHolder holder;
    private MediaPlayer mMediaPlayer;
    private FragmentPlayBack floatFragment;
    private boolean isPrepared = false;
    private TextView name;
    private TextView gradeSubject;
    private ImageView image;
    private TextView teacher;
    private ImageView sex;
    private TextView videoLength;
    private TextView playBackCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_back);
        if (!Vitamio.isInitialized(this)) {
            Toast.makeText(this, "播放器初始化失败", Toast.LENGTH_SHORT).show();
            return;
        }
        initView();
    }

    private void initView() {
        video = (RelativeLayout) findViewById(R.id.video);
        SurfaceView mPreview = (SurfaceView) findViewById(R.id.surfaceView);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888);
        floatFragment = new FragmentPlayBack();

        getSupportFragmentManager().beginTransaction().replace(R.id.control, floatFragment).commit();
        floatFragment.setCallback(new FragmentPlayBack.CallBack() {
            @Override
            public void exit() {
                onBackPressed();
            }

            @Override
            public boolean isPlaying() {
                return mMediaPlayer != null && mMediaPlayer.isPlaying();
            }

            @Override
            public void pause() {
                if (mMediaPlayer != null) {
                    mMediaPlayer.pause();
                }
            }

            @Override
            public void play() {
                if (mMediaPlayer != null) {
                    mMediaPlayer.start();
                }
            }

            @Override
            public boolean isPrepared() {
                return isPrepared;
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
            public void seekTo(long progress) {
                mMediaPlayer.seekTo(progress);
            }

            @Override
            public boolean isPortrait() {
                return getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }

            @Override
            public void zoomSmall() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void zoomBig() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        });

        name = (TextView) findViewById(R.id.name);
        gradeSubject = (TextView) findViewById(R.id.grade_subject);
        image = (ImageView) findViewById(R.id.image);
        teacher = (TextView) findViewById(R.id.teacher);
        sex = (ImageView) findViewById(R.id.sex);
        videoLength = (TextView) findViewById(R.id.video_length);
        playBackCount = (TextView) findViewById(R.id.play_back_count);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

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
    public void onBackPressed() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            // 取消全屏设置
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            ViewGroup.LayoutParams params = video.getLayoutParams();
            params.height = DensityUtils.dp2px(this, 260);
            video.setLayoutParams(params);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ViewGroup.LayoutParams params = video.getLayoutParams();
            params.height = -1;
            video.setLayoutParams(params);
        }
    }
}
