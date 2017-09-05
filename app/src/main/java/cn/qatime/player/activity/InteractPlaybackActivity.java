package cn.qatime.player.activity;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.IOException;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.InteractPlayBackBean;
import cn.qatime.player.fragment.InteractPlayBackFloatFragment;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.NetUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2017/1/10 9:50
 * @Describe 回放页面
 */

public class InteractPlaybackActivity extends BaseActivity implements SurfaceHolder.Callback, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener {
    RelativeLayout control;
    RelativeLayout videolayout;
    private int lessonId;
    private RelativeLayout video;
    private SurfaceHolder holder;
    private boolean isCreated;
    private boolean isPrepared = false;
    private MediaPlayer mMediaPlayer;
    private InteractPlayBackFloatFragment floatFragment;
    public InteractPlayBackBean.Replay playingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact_playback);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (!Vitamio.isInitialized(this)) {
            Toast.makeText(this, "播放器初始化失败", Toast.LENGTH_SHORT).show();
            return;
        }
        lessonId = getIntent().getIntExtra("id", 0);
        String name = getIntent().getStringExtra("name");
        String type = getIntent().getStringExtra("type");

        initView();

        if (lessonId != 0) {
            initData(type);
        }
    }

    private void initData(String type) {
        if (StringUtils.isNullOrBlanK(type)) return;
        String url = UrlUtils.getBaseUrl() + "api/v1/live_studio/interactive_lessons/";
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(url + lessonId + "/replay", null,
                new VolleyListener(InteractPlaybackActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        final InteractPlayBackBean data = JsonUtils.objectFromJson(response.toString(), InteractPlayBackBean.class);
                        floatFragment.setData(data.getData().getReplay());
                        if (!StringUtils.isNullOrBlanK(data.getData().getName())) {
                            floatFragment.setVideoName(data.getData().getName());
                        }
                        if (NetUtils.isConnected(InteractPlaybackActivity.this)) {
                            if (NetUtils.isMobile(InteractPlaybackActivity.this)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(InteractPlaybackActivity.this);
                                final AlertDialog alertDialog = builder.create();
                                View view = View.inflate(InteractPlaybackActivity.this, R.layout.dialog_cancel_or_confirm, null);
                                TextView text = (TextView) view.findViewById(R.id.text);
                                text.setText("您当前正在使用移动网络，继续播放将消耗流量");
                                Button cancel = (Button) view.findViewById(R.id.cancel);
                                Button confirm = (Button) view.findViewById(R.id.confirm);
                                cancel.setText("停止播放");
                                confirm.setText("继续播放");
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                    }
                                });
                                confirm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog.dismiss();
                                        if (data != null && data.getData() != null && data.getData().getReplay() != null && data.getData().getReplay().size() > 0) {
                                            playCourses(data.getData().getReplay().get(0));
                                        }
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setContentView(view);
                            } else if (NetUtils.isWifi(InteractPlaybackActivity.this)) {
                                if (data != null && data.getData() != null && data.getData().getReplay() != null && data.getData().getReplay().size() > 0) {
                                    playCourses(data.getData().getReplay().get(0));
                                }
                            }
                        } else {
                            Toast.makeText(InteractPlaybackActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    private void initView() {

        video = (RelativeLayout) findViewById(R.id.video);
        SurfaceView mPreview = (SurfaceView) findViewById(R.id.surfaceView);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888);
        floatFragment = new InteractPlayBackFloatFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.control, floatFragment).commit();
        floatFragment.setCallback(new InteractPlayBackFloatFragment.CallBack() {
            @Override
            public void exit() {
                onBackPressed();
            }

            @Override
            public void pause() {
                if (mMediaPlayer != null) {
                    mMediaPlayer.pause();
                }
            }

            @Override
            public void play() {
                if (NetUtils.isConnected(InteractPlaybackActivity.this)) {
                    if (NetUtils.isMobile(InteractPlaybackActivity.this)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(InteractPlaybackActivity.this);
                        final AlertDialog alertDialog = builder.create();
                        View view = View.inflate(InteractPlaybackActivity.this, R.layout.dialog_cancel_or_confirm, null);
                        TextView text = (TextView) view.findViewById(R.id.text);
                        text.setText("您当前正在使用移动网络，继续播放将消耗流量");
                        Button cancel = (Button) view.findViewById(R.id.cancel);
                        Button confirm = (Button) view.findViewById(R.id.confirm);
                        cancel.setText("停止播放");
                        confirm.setText("继续播放");
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                        confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                if (mMediaPlayer != null) {
                                    mMediaPlayer.start();
                                } else {
                                    playCourses(playingData);
                                }
                            }
                        });
                        alertDialog.show();
                        alertDialog.setContentView(view);
                    } else if (NetUtils.isWifi(InteractPlaybackActivity.this)) {
                        if (mMediaPlayer != null) {
                            mMediaPlayer.start();
                        } else {
                            playCourses(playingData);
                        }
                    }
                } else {
                    Toast.makeText(InteractPlaybackActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public boolean isPlaying() {
                return mMediaPlayer != null && mMediaPlayer.isPlaying();
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
            public boolean isPrepared() {
                return isPrepared;
            }

        });
    }

    public void playCourses(InteractPlayBackBean.Replay playingData) {
        if (playingData == null) {
            Toast.makeText(this, "回放视频无效", Toast.LENGTH_SHORT).show();
            return;
        }
        this.playingData = playingData;
        play(playingData.getShd_url());
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
    public void surfaceCreated(SurfaceHolder holder) {
        isCreated = true;
        // Create a new media player and set the listeners
    }

    private void createMedia(String path) throws IOException {
        mMediaPlayer = new MediaPlayer(this);
        mMediaPlayer.setDataSource(path);
//        mMediaPlayer.setDataSource("http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/f73262e4321524a5ca7329d67cc30938.mp4");
        mMediaPlayer.setDisplay(holder);
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setScreenOnWhilePlaying(true);
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
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        floatFragment.setBuffering(percent, mp.getDuration());
    }


    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (mMediaPlayer != null) {
            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                mMediaPlayer.pause();
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                mMediaPlayer.start();
            }
        }
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        mMediaPlayer.start();
        floatFragment.setPlayOrPause(mp.isPlaying());
    }
}