package cn.qatime.player.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.neliveplayer.NEMediaPlayer;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.view.NEMediaController;
import cn.qatime.player.view.NEVideoView;

public class NEVideoPlayerActivity extends BaseActivity {
    public final static String TAG = "NEVideoPlayerActivity";
    public NEVideoView mVideoView;  //用于画面显示
    private View mBuffer; //用于指示缓冲状态
    private NEMediaController mMediaController; //用于控制播放

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
    private boolean mHardware = true;
    private ImageButton mPlayBack;
    private TextView mFileName; //文件名称
    private String mTitle;
    private Uri mUri;
    private boolean pauseInBackgroud = false;

    private RelativeLayout mPlayToolbar;

    NEMediaPlayer mMediaPlayer = new NEMediaPlayer();
    private String url = "rtmp://va0a19f55.live.126.net/live/dd47decf2e1a40108775d4318d61cc35";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //接收MainActivity传过来的参数
//        mMediaType = getIntent().getStringExtra("media_type");
//        mDecodeType = getIntent().getStringExtra("decode_type");
//        mVideoPath = getIntent().getStringExtra("videoPath");

//        LogUtils.e(TAG, "playType = " + mMediaType);
//        LogUtils.e(TAG, "decodeType = " + mDecodeType);
//        LogUtils.e(TAG, "videoPath = " + mVideoPath);

//        Intent intent = getIntent();
//        String intentAction = intent.getAction();
//        if (!TextUtils.isEmpty(intentAction) && intentAction.equals(Intent.ACTION_VIEW)) {
//            mVideoPath = intent.getDataString();
//            LogUtils.e(TAG, "videoPath = " + mVideoPath);
//        }
//
//        if (mDecodeType.equals("hardware")) {
//            mHardware = true;
//        } else if (mDecodeType.equals("software")) {
//            mHardware = false;
//        }

        mPlayBack = (ImageButton) findViewById(R.id.player_exit);//退出播放
        mPlayBack.getBackground().setAlpha(0);
        mFileName = (TextView) findViewById(R.id.file_name);

//        mUri = Uri.parse(mVideoPath);
//        if (mUri != null) { //获取文件名，不包括地址
//            List<String> paths = mUri.getPathSegments();
//            String name = paths == null || paths.isEmpty() ? "null" : paths.get(paths.size() - 1);
//            setFileName(name);
//        }

        mPlayToolbar = (RelativeLayout) findViewById(R.id.play_toolbar);
        mPlayToolbar.setVisibility(View.INVISIBLE);

        mBuffer = findViewById(R.id.buffering_prompt);
        mMediaController = new NEMediaController(this);

        mVideoView = (NEVideoView) findViewById(R.id.video_view);

//        if (mMediaType.equals("livestream")) {
            mVideoView.setBufferStrategy(0); //直播低延时
//        } else {
//            mVideoView.setBufferStrategy(1); //点播抗抖动
//        }
        mVideoView.setMediaController(mMediaController);
        mVideoView.setBufferPrompt(mBuffer);
        mVideoView.setMediaType("livestream");//直播livestream  点播videoondemand
        mVideoView.setHardwareDecoder(false);//是否硬解码
        mVideoView.setPauseInBackground(pauseInBackgroud);
        mVideoView.setVideoPath(url);
        //TODO log级别
        mMediaPlayer.setLogLevel(NELP_LOG_DEBUG); //设置log级别
        mVideoView.requestFocus();
        mVideoView.start();
        LogUtils.e(TAG, "Version = " + mMediaPlayer.getVersion()); //获取解码库版本号

        mPlayBack.setOnClickListener(mOnClickEvent); //监听退出播放的事件响应
        mMediaController.setOnShownListener(mOnShowListener); //监听mediacontroller是否显示
        mMediaController.setOnHiddenListener(mOnHiddenListener); //监听mediacontroller是否隐藏
    }


    @Override
    protected void onResume() {
        LogUtils.e(TAG, "NEVideoPlayerActivity onResume");
        if (pauseInBackgroud && !mVideoView.isPaused()) {
            mVideoView.start(); //锁屏打开后恢复播放
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtils.e(TAG, "NEVideoPlayerActivity onPause");

        if (pauseInBackgroud)
            mVideoView.pause(); //锁屏时暂停
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LogUtils.e(TAG, "NEVideoPlayerActivity onDestroy");
        mVideoView.release_resource();
        super.onDestroy();
    }

    View.OnClickListener mOnClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.player_exit) {
                mVideoView.release_resource();
                onDestroy();
                finish();
            }
        }
    };

    NEMediaController.OnShownListener mOnShowListener = new NEMediaController.OnShownListener() {

        @Override
        public void onShown() {
            mPlayToolbar.setVisibility(View.VISIBLE);
            mPlayToolbar.requestLayout();
            mVideoView.invalidate();
            mPlayToolbar.postInvalidate();
        }
    };

    NEMediaController.OnHiddenListener mOnHiddenListener = new NEMediaController.OnHiddenListener() {

        @Override
        public void onHidden() {
            mPlayToolbar.setVisibility(View.INVISIBLE);
        }
    };

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
