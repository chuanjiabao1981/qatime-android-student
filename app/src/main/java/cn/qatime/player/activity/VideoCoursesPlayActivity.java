package cn.qatime.player.activity;

import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.VideoCoursesDetailsBean;
import cn.qatime.player.fragment.FragmentVideoCoursesDetail;
import cn.qatime.player.fragment.FragmentVideoCoursesList;
import cn.qatime.player.fragment.VideoCoursesFloatFragment;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.ScreenSwitchUtils;
import cn.qatime.player.utils.UrlUtils;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * @author lungtify
 * @Time 2017/4/11 15:45
 * @Describe 视频课观看页面
 */

public class VideoCoursesPlayActivity extends BaseFragmentActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {

    private VideoCoursesFloatFragment floatFragment;
    private boolean isCreated = false;
    private MediaPlayer mMediaPlayer;
    private boolean isPrepared = false;
    private ScreenSwitchUtils screenSwitchUtils;
    private RelativeLayout video;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    private int id;
    private VideoCoursesDetailsBean data;
    private SurfaceHolder holder;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_courses_play);
        if (!Vitamio.isInitialized(this)) {
            Toast.makeText(this, "播放器初始化失败", Toast.LENGTH_SHORT).show();
            return;
        }
        id = getIntent().getIntExtra("id", 0);
//        id=3;
        initView();

        screenSwitchUtils = ScreenSwitchUtils.init(this.getApplicationContext());
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if (isCreated) {
//                        createMedia("http://192.168.1.136:8080/media/28.mp4");
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 5000);
        initData();
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlVideoCourses + "/" + id, null,
                new VolleyListener(VideoCoursesPlayActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), VideoCoursesDetailsBean.class);

                        if (data != null && data.getData() != null) {
                            ((FragmentVideoCoursesList) fragBaseFragments.get(0)).setData(data.getData().getVideo_lessons());
                            ((FragmentVideoCoursesDetail) fragBaseFragments.get(1)).setData(data);
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
                if(!StringUtils.isNullOrBlanK(url)){
                    mMediaPlayer.start();
                }else{
//                    播放到进度
                    String url = "";
                    if (data.getData().getClosed_lessons_count()==data.getData().getPreset_lesson_count()) {//全部看完,放第一集
                        url = data.getData().getVideo_lessons().get(0).getVideo().getName_url();
                    }else{
                        url = data.getData().getVideo_lessons().get(data.getData().getClosed_lessons_count()).getVideo().getName_url();
                    }
                    playCourses(url);
                }
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

        fragBaseFragments.add(new FragmentVideoCoursesList());
        fragBaseFragments.add(new FragmentVideoCoursesDetail());
        FragmentLayoutWithLine fragmentLayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentLayout.setScorllToNext(true);
        fragmentLayout.setScorll(true);
        fragmentLayout.setWhereTab(1);
        fragmentLayout.setTabHeight(4, 0xffff5842);
        fragmentLayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xffff5842);
            }
        });
        fragmentLayout.setAdapter(fragBaseFragments, R.layout.tablayout_video_courses, 0x0120);
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
    }

    private void createMedia(String path) throws IOException {
        mMediaPlayer = new MediaPlayer(this);
        mMediaPlayer.setDataSource(path);
        mMediaPlayer.setDisplay(holder);
        mMediaPlayer.prepareAsync();
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
        floatFragment.setPlayOrPause(mp.isPlaying());
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

    public void playCourses(String url) {
        if (!StringUtils.isNullOrBlanK(url)) {
            this.url=url;
            releaseMediaPlayer();
            if (isCreated) {
                try {
                    createMedia(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
