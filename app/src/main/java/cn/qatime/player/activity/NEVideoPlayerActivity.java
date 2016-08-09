package cn.qatime.player.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.neliveplayer.NELivePlayer;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.fragment.FragmentNEVideoPlayer1;
import cn.qatime.player.fragment.FragmentNEVideoPlayer2;
import cn.qatime.player.fragment.FragmentNEVideoPlayer3;
import cn.qatime.player.fragment.FragmentNEVideoPlayer4;
import cn.qatime.player.fragment.FragmentRemedialClassDetail1;
import cn.qatime.player.fragment.FragmentRemedialClassDetail2;
import cn.qatime.player.fragment.FragmentRemedialClassDetail3;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.ScreenUtils;
import cn.qatime.player.view.FragmentLayoutWithLine;
import cn.qatime.player.view.QaVideoPlayer;

public class NEVideoPlayerActivity extends BaseFragmentActivity implements QaVideoPlayer.ControlListener {
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

    private String url = "rtmp://va0a19f55.live.126.net/live/dd47decf2e1a40108775d4318d61cc35";
    private QaVideoPlayer videoPlayer;
    private View bottom;

    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private FragmentLayoutWithLine fragmentLayout;
    private View inputLayout;

    private Handler hd = new Handler();
    private int i = 0;
//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            hd.postDelayed(this, 50);
//            videoPlayer.setData(i + "彈幕");
//            i++;
//            if (i>20){
//                hd.removeCallbacks(this);
//            }
//            LogUtils.e(i);
//        }
//    };
//    int flag = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        videoPlayer = (QaVideoPlayer) findViewById(R.id.video_player);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this) / 3);
        videoPlayer.setLayoutParams(params);

        videoPlayer.setVideoPath(url);
        videoPlayer.setOnControlListener(this);
        videoPlayer.start();
        initView();
    }

    private void initView() {
        bottom = findViewById(R.id.bottom);
        inputLayout = findViewById(R.id.input_layout);
        fragBaseFragments.add(new FragmentNEVideoPlayer1());
        fragBaseFragments.add(new FragmentNEVideoPlayer2());
        fragBaseFragments.add(new FragmentNEVideoPlayer3());
        fragBaseFragments.add(new FragmentNEVideoPlayer4());

        fragmentLayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentLayout.setScorllToNext(true);
        fragmentLayout.setScorll(true);
        fragmentLayout.setWhereTab(1);
        fragmentLayout.setTabHeight(6, 0xff000000);
        fragmentLayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int positon, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff858585);
                ((TextView) currentTabView.findViewById(tab_text[positon])).setTextColor(0xff222222);
                lastTabView.setBackgroundColor(0xffffffff);
                currentTabView.setBackgroundColor(0xffeeeeee);
                if (positon == 1) {
                    inputLayout.setVisibility(View.VISIBLE);
                } else {
                    inputLayout.setVisibility(View.GONE);
                }
            }
        });
        fragmentLayout.setAdapter(fragBaseFragments, R.layout.tablayout_nevideo_player, 0x0102);
        fragmentLayout.getViewPager().setOffscreenPageLimit(3);
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
            //TODO 測試数据
//            hd.postDelayed(runnable, 1000);
            bottom.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = videoPlayer.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoPlayer.setLayoutParams(params);
        } else {
            bottom.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = videoPlayer.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ScreenUtils.getScreenHeight(this) / 3;
            videoPlayer.setLayoutParams(params);
        }
//        videoPlayer.release_resource();
//        videoPlayer.start();
    }


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
//        hd.removeCallbacks(runnable);
        super.onDestroy();
    }


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

//    @Override
//    public void onVideoSizeChanged(NELivePlayer mp, int width, int height, int sarNum, int sarDen) {
////        videoPlayer.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(NEVideoPlayerActivity.this), (ScreenUtils.getScreenWidth(NEVideoPlayerActivity.this)-ScreenUtils.getStatusHeight(NEVideoPlayerActivity.this)) * 3 / 5));
//    }

    @Override
    public void onBufferingUpdate(NELivePlayer neLivePlayer, int i) {

    }

    @Override
    public void onCompletion(NELivePlayer neLivePlayer) {

    }

    @Override
    public void onPrepared(NELivePlayer neLivePlayer) {

    }

    @Override
    public boolean onError(NELivePlayer neLivePlayer, int i, int i1) {
        return false;
    }
}
