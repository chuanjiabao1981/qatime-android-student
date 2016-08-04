package cn.qatime.player.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.netease.neliveplayer.NEMediaPlayer;

import cn.qatime.player.R;
import cn.qatime.player.utils.LogUtils;


/**
 * 对播放器进行封装
 *
 * 需顺序调用
 *   setVideoPath(url);
    start();
 */
public class QaVideoPlayer extends FrameLayout {
    private int flag;// 用户原始是否可旋转，退出是需将用户设置还原
    private NEVideoView videoView;
    private RelativeLayout play_toolbar;
    private ImageButton mPlayBack;//返回键
    private View mBuffer;
    private NEMediaController mMediaController;

    private boolean pauseInBackgroud = false;

    public static final int NELP_LOG_UNKNOWN = 0; //!< log输出模式：输出详细
    public static final int NELP_LOG_DEFAULT = 1; //!< log输出模式：输出详细
    public static final int NELP_LOG_VERBOSE = 2; //!< log输出模式：输出详细
    public static final int NELP_LOG_DEBUG   = 3; //!< log输出模式：输出调试信息
    public static final int NELP_LOG_INFO    = 4; //!< log输出模式：输出标准信息
    public static final int NELP_LOG_WARN    = 5; //!< log输出模式：输出警告
    public static final int NELP_LOG_ERROR   = 6; //!< log输出模式：输出错误
    public static final int NELP_LOG_FATAL   = 7; //!< log输出模式：一些错误信息，如头文件找不到，非法参数使用
    public static final int NELP_LOG_SILENT  = 8; //!< log输出模式：不输出


    NEMediaPlayer mMediaPlayer = new NEMediaPlayer();

    public QaVideoPlayer(Context context) {
        super(context);
        init();
    }

    public QaVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QaVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        flag = Settings.System.getInt(((Activity) QaVideoPlayer.this.getContext()).getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        videoView = new NEVideoView(this.getContext());
        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        videoView.setLayoutParams(params);
        this.addView(videoView);
//        controller = View.inflate(this.getContext(), R.layout.media_controller_portrait, null);
        play_toolbar = (RelativeLayout) View.inflate(this.getContext(), R.layout.video_play_toolbar,null);
        mPlayBack = (ImageButton)play_toolbar. findViewById(R.id.player_exit);//退出播放
        mPlayBack.getBackground().setAlpha(0);
        this.addView(play_toolbar);
        mBuffer = View.inflate(this.getContext(), R.layout.video_play_toolbar,null);
        this.addView(mBuffer);
        mMediaController = new NEMediaController(getContext());

        videoView.setBufferStrategy(0); //直播低延时


        videoView.setMediaController(mMediaController);
        videoView.setBufferPrompt(mBuffer);
        videoView.setMediaType("livestream");//直播livestream  点播videoondemand
        videoView.setHardwareDecoder(false);//是否硬解码
        videoView.setPauseInBackground(pauseInBackgroud);

        //TODO log级别
        mMediaPlayer.setLogLevel(NELP_LOG_DEBUG); //设置log级别
        videoView.requestFocus();

//        mPlayBack.setOnClickListener(mOnClickEvent); //监听退出播放的事件响应
        mMediaController.setOnShownListener(mOnShowListener); //监听mediacontroller是否显示
        mMediaController.setOnHiddenListener(mOnHiddenListener); //监听mediacontroller是否隐藏
    }


//    View.OnClickListener mOnClickEvent = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.player_exit) {
//                videoView.release_resource();
//                onDestroy();
//                finish();
//            }
//        }
//    };


    NEMediaController.OnShownListener mOnShowListener = new NEMediaController.OnShownListener() {

        @Override
        public void onShown() {
            play_toolbar.setVisibility(View.VISIBLE);
            play_toolbar.requestLayout();
            videoView.invalidate();
            play_toolbar.postInvalidate();
        }
    };

    NEMediaController.OnHiddenListener mOnHiddenListener = new NEMediaController.OnHiddenListener() {

        @Override
        public void onHidden() {
            play_toolbar.setVisibility(View.INVISIBLE);
        }
    };
    public void setVideoPath(String url){
        videoView.setVideoPath(url);
    }
public void start(){
    videoView.start();
}
public void pause(){
    videoView.pause();
}
    public boolean isPauseInBackgroud() {
        return pauseInBackgroud;
    }
public boolean isPaused(){
    return videoView.isPaused();
}
public boolean isPlaying(){
    return videoView.isPlaying();
}
    public void release_resource(){
        videoView.release_resource();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
            // 全屏
            if (Build.VERSION.SDK_INT >= 11) {
                try {
                    ((Activity)getContext()).getActionBar().hide();
                } catch (Exception e) {
                }
            }
            ((Activity)getContext()).getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            exitFull();
        }
    }

    /**
     * 退出全屏
     */
    private void exitFull() {
// 竖屏
        if (!videoView.isPlaying()) {
            Settings.System.putInt(((Activity)getContext()).getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, flag);
        }
        // 显示导航栏和状态栏
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                ((Activity)getContext()). getActionBar().show();
            } catch (Exception e) {
            }
        }
        WindowManager.LayoutParams attrs = ((Activity)getContext()).getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity)getContext()).getWindow().setAttributes(attrs);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // 取消全屏设置
        ((Activity)getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

}
