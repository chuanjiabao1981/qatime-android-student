package cn.qatime.player.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.netease.neliveplayer.NELivePlayer;
import com.orhanobut.logger.Logger;

import cn.qatime.player.R;
import libraryextra.utils.ScreenUtils;


/**
 * 对播放器进行封装
 * <p/>
 * 需顺序调用
 * setVideoPath(url);
 * start();
 */
public class QaVideoPlayer extends FrameLayout implements NELivePlayer.OnBufferingUpdateListener, NELivePlayer.OnCompletionListener, NELivePlayer.OnPreparedListener, NELivePlayer.OnErrorListener, View.OnClickListener {
    private int flag;// 用户原始是否可旋转，退出是需将用户设置还原
    private NEVideoView videoView;
    private View mMediaController;
    private ImageView mPlayBack;//返回键
    private View mBuffer;
//    private NEMediaController mMediaController;

    private static final int sDefaultTimeout = 5000;//默认隐藏时间

    /**
     * 定时隐藏操作框
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            if (comment.is)
            mMediaController.setVisibility(View.GONE);

            if (barrageSettingLayout.getVisibility() == VISIBLE) {//大布局gone时，弹幕设置布局也要gone
                barrageSettingLayout.setVisibility(GONE);
            }
            if (radiogroup.getVisibility() == VISIBLE) {
                radiogroup.setVisibility(GONE);
            }
        }
    };

    private boolean pauseInBackgroud = false;

    public static final int NELP_LOG_UNKNOWN = 0; //!< log输出模式：输出详细
    public static final int NELP_LOG_DEFAULT = 1; //!< log输出模式：输出详细
    public static final int NELP_LOG_VERBOSE = 2; //!< log输出模式：输出详细
    public static final int NELP_LOG_DEBUG = 3; //!< log输出模式：输出调试信息
    public static final int NELP_LOG_INFO = 4; //!< log输出模式：输出标准信息
    public static final int NELP_LOG_WARN = 5; //!< log输出模式：输出警告
    public static final int NELP_LOG_ERROR = 6; //!< log输出模式：输出错误
    public static final int NELP_LOG_FATAL = 7; //!< log输出模式：一些错误信息，如头文件找不到，非法参数使用
    public static final int NELP_LOG_SILENT = 8; //!< log输出模式：不输出
    private ControlListener controlListener;
    private Handler hd = new Handler();
    private ImageView play;
    private ImageView zoom;
    private View toolbarLayout;
    private TextView videoName;
    private View definition;
    private RadioGroup radiogroup;
    private View commentLayout;
    private View refresh;
    private EditText comment;
    private Button commit;
    private TextView barrage;
    private View barrageSetting;
    private View zoomLayout;
    private View playToolbar;
    private View bottomLayout;
    private TextView viewCount;
    private View barrageSettingLayout;
    private SeekBar brightness;
    private Context context;
    private SeekBar barrageTransparent;
    private SeekBar barrageSize;
    private SeekBar barrageSpeed;
    private BarrageView barrageView;


//    NEMediaPlayer mMediaPlayer = new NEMediaPlayer();

    public QaVideoPlayer(Context context) {
        super(context);
        init();
    }

    public QaVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public QaVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        flag = Settings.System.getInt(((Activity) QaVideoPlayer.this.getContext()).getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        videoView = new NEVideoView(getContext());
        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        videoView.setLayoutParams(params);
        this.addView(videoView);
        mMediaController = View.inflate(this.getContext(), R.layout.video_media_controller, null);
        //上部分
        playToolbar = mMediaController.findViewById(R.id.play_toolbar);
        mPlayBack = (ImageView) mMediaController.findViewById(R.id.player_exit);//退出播放
        toolbarLayout = mMediaController.findViewById(R.id.toolbar_layout);
        viewCount = (TextView) mMediaController.findViewById(R.id.view_count);
        videoName = (TextView) mMediaController.findViewById(R.id.video_name);
        definition = mMediaController.findViewById(R.id.definition);//清晰度点击按钮
        radiogroup = (RadioGroup) mMediaController.findViewById(R.id.radiogroup);

        //下部分
        bottomLayout = mMediaController.findViewById(R.id.bottom_layout);
        play = (ImageView) mMediaController.findViewById(R.id.play);
        play.setOnClickListener(this);
        zoomLayout = mMediaController.findViewById(R.id.zoom_layout);
        zoom = (ImageView) mMediaController.findViewById(R.id.zoom);
        zoom.setOnClickListener(this);
        commentLayout = mMediaController.findViewById(R.id.comment_layout);//评论大布局
        refresh = mMediaController.findViewById(R.id.refresh);//刷新
        comment = (EditText) mMediaController.findViewById(R.id.comment);
        commit = (Button) mMediaController.findViewById(R.id.commit);
        barrage = (TextView) mMediaController.findViewById(R.id.barrage);//弹幕开关
        barrageSetting = mMediaController.findViewById(R.id.barrage_setting);//弹幕设置
        barrageSettingLayout = mMediaController.findViewById(R.id.barrage_setting_layout);//弹幕设置布局

        brightness = (SeekBar) mMediaController.findViewById(R.id.brightness);//屏幕亮度调节
        barrageTransparent = (SeekBar) mMediaController.findViewById(R.id.barrage_transparent);
        barrageSize = (SeekBar) mMediaController.findViewById(R.id.barrage_size);
        barrageSpeed = (SeekBar) mMediaController.findViewById(R.id.barrage_speed);

        brightness.setProgress(ScreenUtils.getScreenBrightness(context));
//        barrageView = new BarrageView(getContext());
//        barrageView.setVisibility(GONE);
        this.addView(mMediaController);
//        mBuffer = View.inflate(this.getContext(), R.layout.video_play_toolbar, null);
//        this.addView(mBuffer);

        videoView.setBufferStrategy(0); //直播低延时

        videoView.setBufferPrompt(mBuffer);
        videoView.setMediaType("livestream");//直播livestream  点播videoondemand
        videoView.setHardwareDecoder(false);//是否硬解码
        videoView.setPauseInBackground(pauseInBackgroud);

        //TODO log级别
//        mMediaPlayer.setLogLevel(NELP_LOG_DEBUG); //设置log级别
        videoView.requestFocus();

//        mPlayBack.setOnClickListener(mOnClickEvent); //监听退出播放的事件响应
//        mMediaController.setOnShownListener(mOnShowListener); //监听mediacontroller是否显示
//        mMediaController.setOnHiddenListener(mOnHiddenListener); //监听mediacontroller是否隐藏
        videoView.setOnBufferingUpdateListener(this);
        videoView.setOnCompletionListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnErrorListener(this);

        hd.postDelayed(runnable, sDefaultTimeout);

        comment.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hd.removeCallbacks(runnable);
                }
            }
        });
        mMediaController.setOnClickListener(this);
        definition.setOnClickListener(this);
        barrageSetting.setOnClickListener(this);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ScreenUtils.setScreenBrightness(context, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                hd.removeCallbacks(runnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                hd.postDelayed(runnable, sDefaultTimeout);
            }
        });
    }

    public void setData(String data) {
        if (barrageView.getVisibility() == GONE) {
            return;
        }
        barrageView.addItem(data);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mMediaController.getVisibility() == View.GONE) {
                hd.removeCallbacks(runnable);
                mMediaController.setVisibility(View.VISIBLE);
                hd.postDelayed(runnable, sDefaultTimeout);
                return false;
            } else {
//                mMediaController.setVisibility(GONE);
//                hd.removeCallbacks(runnable);
                return super.dispatchTouchEvent(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    public void setVideoPath(String url) {
        videoView.setVideoPath(url);
    }

    public void start() {
        videoView.start();
    }

    public void pause() {
        videoView.pause();
    }

    public boolean isPauseInBackgroud() {
        return pauseInBackgroud;
    }

    public boolean isPaused() {
        return videoView.isPaused();
    }

    public boolean isPlaying() {
        return videoView.isPlaying();
    }

    public void release_resource() {
        videoView.release_resource();
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
            landscape();
            videoView.setVideoScalingMode(true);
//            zoom.setImageResource(R.mipmap.nemediacontroller_scale02);
            // 全屏
            if (Build.VERSION.SDK_INT >= 11) {
                try {
                    ((Activity) getContext()).getActionBar().hide();
                } catch (Exception e) {
                }
            }
            ((Activity) getContext()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            vertical();
//            zoom.setImageResource(R.mipmap.nemediacontroller_scale01);
            videoView.setVideoScalingMode(false);
            exitFull();
        }
    }

    private void vertical() {
        toolbarLayout.setVisibility(GONE);
        viewCount.setVisibility(VISIBLE);
        playToolbar.setBackgroundColor(0x00000000);

        commentLayout.setVisibility(GONE);
        zoomLayout.setVisibility(VISIBLE);
        bottomLayout.setBackgroundColor(0x00000000);

        if (barrageSettingLayout.getVisibility() == VISIBLE) {//大布局gone时，弹幕设置布局也要gone
            barrageSettingLayout.setVisibility(GONE);
        }
        if (radiogroup.getVisibility() == VISIBLE) {
            radiogroup.setVisibility(GONE);
        }
//        barrageView.setVisibility(GONE);
    }

    private void landscape() {

        toolbarLayout.setVisibility(VISIBLE);
        viewCount.setVisibility(GONE);
        playToolbar.setBackgroundColor(0xff999999);

        commentLayout.setVisibility(VISIBLE);
        zoomLayout.setVisibility(GONE);
        bottomLayout.setBackgroundColor(0xff999999);

//        barrageView.setVisibility(VISIBLE);
    }

    /**
     * 退出全屏
     */
    private void exitFull() {
// 竖屏
//        if (!videoView.isPlaying()) {
//            Settings.System.putInt(((Activity) getContext()).getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, flag);
//        }
        // 显示导航栏和状态栏
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                ((Activity) getContext()).getActionBar().show();
            } catch (Exception e) {
            }
        }
        WindowManager.LayoutParams attrs = ((Activity) getContext()).getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity) getContext()).getWindow().setAttributes(attrs);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // 取消全屏设置
        ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 缓冲状态改变
     *
     * @param neLivePlayer
     * @param i
     */
    @Override
    public void onBufferingUpdate(NELivePlayer neLivePlayer, int i) {
        if (controlListener != null) {
            controlListener.onBufferingUpdate(neLivePlayer, i);
        }
    }

    /**
     * 播放完成
     *
     * @param neLivePlayer
     */
    @Override
    public void onCompletion(NELivePlayer neLivePlayer) {
        if (controlListener != null) {
            controlListener.onCompletion(neLivePlayer);
        }
    }

    /**
     * 准备完成
     *
     * @param neLivePlayer
     */
    @Override
    public void onPrepared(NELivePlayer neLivePlayer) {
        if (controlListener != null) {
            controlListener.onPrepared(neLivePlayer);
        }
    }

    /**
     * 播放。。。。。。。。。。。。。。。。。。错误
     *
     * @param neLivePlayer
     * @param i
     * @param i1
     * @return
     */
    @Override
    public boolean onError(NELivePlayer neLivePlayer, int i, int i1) {
        Logger.e("播放。。。。。。。。。。。。。。。。。。错误");
        if (controlListener != null) {
            return controlListener.onError(neLivePlayer, i, i1);
        }
        return false;
    }

    public void setOnControlListener(ControlListener listener) {
        this.controlListener = listener;

    }

//    @Override
//    public void onVideoSizeChanged(NELivePlayer neLivePlayer, int i, int i1, int i2, int i3) {
//        if (controlListener != null) {
//            controlListener.onVideoSizeChanged(neLivePlayer, i, i1, i2, i3);
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play://播放暂停
                if (videoView.isPlaying()) {
                    videoView.pause();
                    play.setImageResource(R.mipmap.nemediacontroller_pause);
                } else {
                    videoView.start();
                    play.setImageResource(R.mipmap.nemediacontroller_play);
                }
                break;
            case R.id.zoom://横竖屏

                if (((Activity) getContext()).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {//
//                    videoView.setVideoScalingMode(1, false);
                    ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
//                    videoView.setVideoScalingMode(0, false);
                    ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

                break;
//            case R.id.player_exit:
//                if (((Activity) getContext()).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {//
////                    videoView.setVideoScalingMode(1, false);
//                    ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                } else {
//                }
//                break;
            case R.id.controller://控制大布局
                mMediaController.setVisibility(GONE);

                if (barrageSettingLayout.getVisibility() == VISIBLE) {//大布局gone时，弹幕设置布局也要gone
                    barrageSettingLayout.setVisibility(GONE);
                }
                if (radiogroup.getVisibility() == VISIBLE) {
                    radiogroup.setVisibility(GONE);
                }
                break;
            case R.id.definition://清晰度
                hd.removeCallbacks(runnable);
                if (radiogroup.getVisibility() == VISIBLE) {
                    radiogroup.setVisibility(GONE);
                } else {
                    radiogroup.setVisibility(VISIBLE);
                }
                break;
            case R.id.barrage_setting://弹幕设置
                hd.removeCallbacks(runnable);
                if (barrageSettingLayout.getVisibility() == VISIBLE) {
                    barrageSettingLayout.setVisibility(GONE);
                } else {
                    barrageSettingLayout.setVisibility(VISIBLE);
                }
                break;
        }
    }

//    public void setLayoutParam(int width, int high) {
//        setLayoutParams(new LinearLayout.LayoutParams(width, high));
//        videoView.setLayoutParams(new FrameLayout.LayoutParams(width,high));
//
//    }

    public interface ControlListener {
//        void onVideoSizeChanged(NELivePlayer mp, int width, int height, int sarNum, int sarDen);

        void onBufferingUpdate(NELivePlayer neLivePlayer, int i);

        void onCompletion(NELivePlayer neLivePlayer);

        void onPrepared(NELivePlayer neLivePlayer);

        boolean onError(NELivePlayer neLivePlayer, int i, int i1);
    }
}
