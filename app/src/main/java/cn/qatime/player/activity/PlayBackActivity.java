package cn.qatime.player.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.IOException;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.PlayBackInfoBean;
import cn.qatime.player.fragment.FragmentPlayBack;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import libraryextra.utils.DateUtils;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2017/6/12 14:39
 * @Describe
 */

public class PlayBackActivity extends BaseFragmentActivity implements SurfaceHolder.Callback, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, View.OnClickListener, MediaPlayer.OnInfoListener {
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
    private boolean isCreated = false;
    private PlayBackInfoBean data;
    private View buffering;
    private View noData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_back);
        if (!Vitamio.isInitialized(this)) {
            Toast.makeText(this, "播放器初始化失败", Toast.LENGTH_SHORT).show();
            return;
        }
        initView();
        int id = getIntent().getIntExtra("id", 0);
        if (id != 0) {
            initData(id);
        }
    }

    private void initData(int id) {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlHomeReplays + "/" + id + "/replay", null, new VolleyListener(PlayBackActivity.this) {
            @Override
            protected void onTokenOut() {

            }

            @Override
            protected void onSuccess(JSONObject response) {
                data = JsonUtils.objectFromJson(response.toString(), PlayBackInfoBean.class);
                if (data != null) {
                    name.setText(data.getData().getLive_studio_lesson().getName());
                    gradeSubject.setText(data.getData().getTeacher().getCategory() + data.getData().getTeacher().getSubject());
                    Glide.with(PlayBackActivity.this).load(data.getData().getTeacher().getEx_big_avatar_url()).placeholder(R.mipmap.photo).into(image);
                    teacher.setText(data.getData().getTeacher().getName());
                    sex.setImageResource("male".equals(data.getData().getTeacher().getGender()) ? R.mipmap.male : R.mipmap.female);
                    videoLength.setText(DateUtils.stringForTime(data.getData().getVideo_duration(), true));
                    playBackCount.setText(String.valueOf(data.getData().getReplay_times()));
                    play(data.getData().getVideo_url());
                }
            }

            @Override
            protected void onError(JSONObject response) {

            }
        }, new VolleyErrorListener());
        addToRequestQueue(request);
    }

    private void initView() {
        video = (RelativeLayout) findViewById(R.id.video);
        SurfaceView mPreview = (SurfaceView) findViewById(R.id.surfaceView);
        buffering = findViewById(R.id.buffering);
        noData = findViewById(R.id.video_no_data);
        findViewById(R.id.teacher_layout).setOnClickListener(this);
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
        isCreated = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isCreated = false;
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
            params.height = -1  ;
            video.setLayoutParams(params);
        }
        floatFragment.refreshZoom();
    }

    private void play(String nameUrl) {
        releaseMediaPlayer();
        if (isCreated) {
            try {
                createMedia(nameUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createMedia(String path) throws IOException {
        mMediaPlayer = new MediaPlayer(this);
        mMediaPlayer.setDataSource(path);
        mMediaPlayer.setDisplay(holder);
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setScreenOnWhilePlaying(true);
//        mMediaPlayer.setOnVideoSizeChangedListener(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        floatFragment.setBuffering(percent, mp.getDuration());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        buffering.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMediaPlayer != null) {
            resumeMedia();
        }
    }

    private void resumeMedia() {
        if (isCreated) {
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.start();
            floatFragment.setPlayOrPause(true);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    resumeMedia();
                }
            }, 50);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            floatFragment.setPlayOrPause(false);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        noData.setVisibility(View.GONE);
        mMediaPlayer.start();
        floatFragment.setPlayOrPause(mp.isPlaying());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teacher_layout:
                if (data != null && data.getData() != null && data.getData().getTeacher() != null) {
                    Intent intent = new Intent(this, TeacherDataActivity.class);
                    intent.putExtra("teacherId", data.getData().getTeacher().getId());
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (mMediaPlayer != null) {
            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                mMediaPlayer.pause();
                floatFragment.setPlayOrPause(false);
                buffering.setVisibility(View.VISIBLE);
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                mMediaPlayer.start();
                floatFragment.setPlayOrPause(true);
                buffering.setVisibility(View.GONE);
            }
        }
        return true;
    }
}
