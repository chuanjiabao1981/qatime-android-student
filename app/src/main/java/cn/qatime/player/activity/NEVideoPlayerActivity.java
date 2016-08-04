package cn.qatime.player.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.neliveplayer.NEMediaPlayer;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.ScreenUtils;
import cn.qatime.player.view.NEMediaController;
import cn.qatime.player.view.NEVideoView;
import cn.qatime.player.view.QaVideoPlayer;

public class NEVideoPlayerActivity extends BaseActivity {
    public final static String TAG = "NEVideoPlayerActivity";
//    public NEVideoView mVideoView;  //用于画面显示
//    private View mBuffer; //用于指示缓冲状态
//    private NEMediaController mMediaController; //用于控制播放

    public static final int NELP_LOG_UNKNOWN = 0; //!< log输出模式：输出详细
    public static final int NELP_LOG_DEFAULT = 1; //!< log输出模式：输出详细
    public static final int NELP_LOG_VERBOSE = 2; //!< log输出模式：输出详细
    public static final int NELP_LOG_DEBUG = 3; //!< log输出模式：输出调试信息
    public static final int NELP_LOG_INFO = 4; //!< log输出模式：输出标准信息
    public static final int NELP_LOG_WARN = 5; //!< log输出模式：输出警告
    public static final int NELP_LOG_ERROR = 6; //!< log输出模式：输出错误
    public static final int NELP_LOG_FATAL = 7; //!< log输出模式：一些错误信息，如头文件找不到，非法参数使用
    public static final int NELP_LOG_SILENT = 8; //!< log输出模式：不输出

//    private String mVideoPath; //文件路径
//    private String mDecodeType;//解码类型，硬解或软解
//    private String mMediaType; //媒体类型
//    private boolean mHardware = true;
//    private ImageButton mPlayBack;
//    private TextView mFileName; //文件名称
//    private String mTitle;
//    private Uri mUri;
//    private boolean pauseInBackgroud = false;

//    private RelativeLayout mPlayToolbar;

//    NEMediaPlayer mMediaPlayer = new NEMediaPlayer();
    private String url = "rtmp://va0a19f55.live.126.net/live/dd47decf2e1a40108775d4318d61cc35";
    private QaVideoPlayer videoPlayer;
    private View bottom;

//    int flag = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
 videoPlayer = (QaVideoPlayer) findViewById(R.id.video_player);
        bottom = findViewById(R.id.bottom);
videoPlayer.setVideoPath(url);
        videoPlayer.start();
//        mPlayBack = (ImageButton) findViewById(R.id.player_exit);//退出播放
//        mPlayBack.getBackground().setAlpha(0);
//        mFileName = (TextView) findViewById(R.id.file_name);
//
////        mUri = Uri.parse(mVideoPath);
////        if (mUri != null) { //获取文件名，不包括地址
////            List<String> paths = mUri.getPathSegments();
////            String name = paths == null || paths.isEmpty() ? "null" : paths.get(paths.size() - 1);
////            setFileName(name);
////        }
//
//        mPlayToolbar = (RelativeLayout) findViewById(R.id.play_toolbar);
//        mPlayToolbar.setVisibility(View.INVISIBLE);
//
//        mBuffer = findViewById(R.id.buffering_prompt);
//        mMediaController = new NEMediaController(this);
//
//        mVideoView = (NEVideoView) findViewById(R.id.video_view);
//
////        if (mMediaType.equals("livestream")) {
//            mVideoView.setBufferStrategy(0); //直播低延时
////        } else {
////            mVideoView.setBufferStrategy(1); //点播抗抖动
////        }
//        mVideoView.setMediaController(mMediaController);
//        mVideoView.setBufferPrompt(mBuffer);
//        mVideoView.setMediaType("livestream");//直播livestream  点播videoondemand
//        mVideoView.setHardwareDecoder(false);//是否硬解码
//        mVideoView.setPauseInBackground(pauseInBackgroud);
//        mVideoView.setVideoPath(url);
//        //TODO log级别
//        mMediaPlayer.setLogLevel(NELP_LOG_DEBUG); //设置log级别
//        mVideoView.requestFocus();
//        mVideoView.start();
//        LogUtils.e(TAG, "Version = " + mMediaPlayer.getVersion()); //获取解码库版本号
//
//        mPlayBack.setOnClickListener(mOnClickEvent); //监听退出播放的事件响应
//        mMediaController.setOnShownListener(mOnShowListener); //监听mediacontroller是否显示
//        mMediaController.setOnHiddenListener(mOnHiddenListener); //监听mediacontroller是否隐藏
    }


    @Override
    protected void onResume() {
        LogUtils.e(TAG, "NEVideoPlayerActivity onResume");
        if (videoPlayer.isPauseInBackgroud() && !videoPlayer.isPaused()) {
            videoPlayer.start(); //锁屏打开后恢复播放
        }
        super.onResume();
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
            bottom.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = videoPlayer.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoPlayer.setLayoutParams(params);
        }else {
            bottom.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = videoPlayer.getLayoutParams();
            params.height = ScreenUtils.getScreenHeight(this)/3;
            videoPlayer.setLayoutParams(params);

        }
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
//            // 全屏
//            if (Build.VERSION.SDK_INT >= 11) {
//                try {
//                    getActionBar().hide();
//                } catch (Exception e) {
//                }
//            }
//            getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        } else {
//            exitFull();
//        }
    }

//    /**
//     * 退出全屏
//     */
//    private void exitFull() {
//// 竖屏
//        if (!videoPlayer.isPlaying()) {
//            Settings.System.putInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, flag);
//        }
//        // 显示导航栏和状态栏
//        if (Build.VERSION.SDK_INT >= 11) {
//            try {
//                getActionBar().show();
//            } catch (Exception e) {
//            }
//        }
//        WindowManager.LayoutParams attrs = getWindow().getAttributes();
//        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setAttributes(attrs);
//        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        // 取消全屏设置
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//    }

    @Override
    protected void onPause() {
        LogUtils.e(TAG, "NEVideoPlayerActivity onPause");

        if (videoPlayer.isPauseInBackgroud())
            videoPlayer.pause(); //锁屏时暂停
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LogUtils.e(TAG, "NEVideoPlayerActivity onDestroy");
        videoPlayer.release_resource();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onDestroy();
    }

//    View.OnClickListener mOnClickEvent = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if (v.getId() == R.id.player_exit) {
//                mVideoView.release_resource();
//                onDestroy();
//                finish();
//            }
//        }
//    };

//    NEMediaController.OnShownListener mOnShowListener = new NEMediaController.OnShownListener() {
//
//        @Override
//        public void onShown() {
//            mPlayToolbar.setVisibility(View.VISIBLE);
//            mPlayToolbar.requestLayout();
//            mVideoView.invalidate();
//            mPlayToolbar.postInvalidate();
//        }
//    };
//
//    NEMediaController.OnHiddenListener mOnHiddenListener = new NEMediaController.OnHiddenListener() {
//
//        @Override
//        public void onHidden() {
//            mPlayToolbar.setVisibility(View.INVISIBLE);
//        }
//    };

    @Override
    public void backClick(View v) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        super.backClick(v);
    }
    @Override
    public void onBackPressed() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }

        super.onBackPressed();
    }

}
