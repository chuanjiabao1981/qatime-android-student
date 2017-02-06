package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.PlayBackBean;
import cn.qatime.player.fragment.PlayBackFloatFragment;
import cn.qatime.player.presenter.PlayBackVideoPresenter;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.PlayBackVideoInterface;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.NEVideoView;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2017/1/10 9:50
 * @Describe 回放页面
 */

public class NEVideoPlaybackActivity extends BaseActivity implements PlayBackVideoInterface {
    RelativeLayout control;
    RelativeLayout videolayout;
    private PlayBackFloatFragment playBackFloatFragment;
    private NEVideoView video;
    private int lessonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nevideo_playback);

        lessonId = getIntent().getIntExtra("id", 0);
        String name = getIntent().getStringExtra("name");

        initView();
        if (!StringUtils.isNullOrBlanK(name)) {
            playBackFloatFragment.setName(name);
        }
        if (lessonId != 0) {
            initData();
        }
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.lessons + "/" + lessonId + "/replay", null,
                new VolleyListener(NEVideoPlaybackActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        PlayBackBean data = JsonUtils.objectFromJson(response.toString(), PlayBackBean.class);
                        if (data != null && data.getData() != null && data.getData().getReplay() != null && !StringUtils.isNullOrBlanK(data.getData().getReplay().getOrig_url())) {
                            video.setVideoPath(data.getData().getReplay().getOrig_url());
                            video.start();
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
        int screenW = ScreenUtils.getScreenWidth(NEVideoPlaybackActivity.this);

        video = (NEVideoView) findViewById(R.id.video);
        video.setmBufferStrategy(1);
        control = (RelativeLayout) findViewById(R.id.control);
        videolayout = (RelativeLayout) findViewById(R.id.video_layout);

        PlayBackVideoPresenter playBackVideoPresenter = new PlayBackVideoPresenter(this);
        playBackFloatFragment = new PlayBackFloatFragment(playBackVideoPresenter);
        getSupportFragmentManager().beginTransaction().replace(R.id.control, playBackFloatFragment).commit();

//        ViewGroup.LayoutParams mainVideoParam = videolayout.getLayoutParams();
//        mainVideoParam.width = -1;
//        mainVideoParam.height = screenW * 9 / 16;
//        videolayout.setLayoutParams(mainVideoParam);
        playBackFloatFragment.setMediaPlayer(video);

    }


//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (screenSwitchUtils.isPortrait()) {//竖屏
//            playBackFloatFragment.setPortrait(true);
//            ViewGroup.LayoutParams params = videolayout.getLayoutParams();
//            params.width = -1;
//            params.height = ScreenUtils.getScreenWidth(NEVideoPlaybackActivity.this) * 9 / 16;
//            videolayout.setLayoutParams(params);
//        } else {//横屏
//            playBackFloatFragment.setPortrait(false);
//            ViewGroup.LayoutParams params = videolayout.getLayoutParams();
//            params.width = -1;
//            params.height = -1;
//            videolayout.setLayoutParams(params);
//        }
//    }

//    @Override
//    public void onBackPressed() {
//        if (!screenSwitchUtils.isPortrait()) {
//            Logger.e("orta 返回竖屏");
//            screenSwitchUtils.toggleScreen();
//            return;
//        }
//        super.onBackPressed();
//    }

    //    @Override
//    public void fullScreen() {
//        if (screenSwitchUtils != null) {
//            screenSwitchUtils.toggleScreen();
//        }
//    }
//
    @Override
    public void exit() {
        onBackPressed();
    }

    @Override
    public void playOrPause() {
        if (video.isPlaying()) {
            playBackFloatFragment.setPlayOrPause(false);
            video.pause();
        } else {
            video.start();
            playBackFloatFragment.setPlayOrPause(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        video.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        video.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        video.release_resource();
    }
}
