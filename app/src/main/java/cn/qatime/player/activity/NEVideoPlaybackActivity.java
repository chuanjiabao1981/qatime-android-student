package cn.qatime.player.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.fragment.PlayBackFloatFragment;
import cn.qatime.player.presenter.PlayBackVideoPresenter;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.PlayBackVideoInterface;
import cn.qatime.player.utils.ScreenSwitchUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.NEVideoView;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
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
    ListView listView;
    private PlayBackFloatFragment playBackFloatFragment;
    private ScreenSwitchUtils screenSwitchUtils;
    private NEVideoView video;
    private int id;
    private CommonAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nevideo_playback);
        id = getIntent().getIntExtra("id", 0);
        screenSwitchUtils = ScreenSwitchUtils.init(this.getApplicationContext());

        initView();
        initData();
        video.setVideoPath("http://192.168.1.125:8080/media/28.mp4");
        video.start();
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlRemedialClass + "/" + id, null,
                new VolleyListener(NEVideoPlaybackActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
//                        data = JsonUtils.objectFromJson(response.toString(), RemedialClassDetailBean.class);
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }

                , new VolleyErrorListener() {
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
        listView = (ListView) findViewById(R.id.listView);

        PlayBackVideoPresenter playBackVideoPresenter = new PlayBackVideoPresenter(this);
        playBackFloatFragment = new PlayBackFloatFragment(playBackVideoPresenter);
        getSupportFragmentManager().beginTransaction().replace(R.id.control, playBackFloatFragment).commit();

        ViewGroup.LayoutParams mainVideoParam = videolayout.getLayoutParams();
        mainVideoParam.width = -1;
        mainVideoParam.height = screenW * 9 / 16;
        videolayout.setLayoutParams(mainVideoParam);
        playBackFloatFragment.setMediaPlayer(video);

        adapter = new CommonAdapter<String>(this, null, R.layout.item_playback) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.number, getNumber(position));

                if (!StringUtils.isNullOrBlanK(video.getVideoPath()) && video.getVideoPath().equals(item)) {
                    ((TextView) holder.getView(R.id.number)).setTextColor(0xffbef0f0);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xffbef0f0);
                } else {
                    ((TextView) holder.getView(R.id.number)).setTextColor(0xff333333);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff333333);
                }
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                video.setVideoPath("");
                video.start();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private String getNumber(int position) {
        position += 1;
        if (position < 10) {
            return "0" + position;
        }
        return String.valueOf(position);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                screenSwitchUtils.start(NEVideoPlaybackActivity.this);
            }
        }, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        screenSwitchUtils.stop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (screenSwitchUtils.isPortrait()) {//竖屏
            playBackFloatFragment.setPortrait(true);
            ViewGroup.LayoutParams params = videolayout.getLayoutParams();
            params.width = -1;
            params.height = ScreenUtils.getScreenWidth(NEVideoPlaybackActivity.this) * 9 / 16;
            videolayout.setLayoutParams(params);
        } else {//横屏
            playBackFloatFragment.setPortrait(false);
            ViewGroup.LayoutParams params = videolayout.getLayoutParams();
            params.width = -1;
            params.height = -1;
            videolayout.setLayoutParams(params);
        }
    }

    @Override
    public void onBackPressed() {
        if (!screenSwitchUtils.isPortrait()) {
            Logger.e("orta 返回竖屏");
            screenSwitchUtils.toggleScreen();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void fullScreen() {
        if (screenSwitchUtils != null) {
            screenSwitchUtils.toggleScreen();
        }
    }

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
