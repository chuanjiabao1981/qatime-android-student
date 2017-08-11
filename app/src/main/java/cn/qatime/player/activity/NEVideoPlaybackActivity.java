package cn.qatime.player.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import libraryextra.utils.NetUtils;
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
        String type = getIntent().getStringExtra("type");

        initView();
        if (!StringUtils.isNullOrBlanK(name)) {
            playBackFloatFragment.setName(name);
        }
        if (lessonId != 0) {
            initData(type);
        }
    }

    private void initData(String type) {
        if (StringUtils.isNullOrBlanK(type)) return;
        String url = "";
        if (type.equals("live")) {
            url = UrlUtils.lessons;//直播
        } else if (type.equals("exclusive")) {
            url = UrlUtils.getBaseUrl() + "api/v1/live_studio/scheduled_lessons/";
        }
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(url + lessonId + "/replay", null,
                new VolleyListener(NEVideoPlaybackActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        final PlayBackBean data = JsonUtils.objectFromJson(response.toString(), PlayBackBean.class);
                        if (NetUtils.isConnected(NEVideoPlaybackActivity.this)) {
                            if (NetUtils.isMobile(NEVideoPlaybackActivity.this)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(NEVideoPlaybackActivity.this);
                                final AlertDialog alertDialog = builder.create();
                                View view = View.inflate(NEVideoPlaybackActivity.this, R.layout.dialog_cancel_or_confirm, null);
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
                                        if (data != null && data.getData() != null && data.getData().getReplay() != null && !StringUtils.isNullOrBlanK(data.getData().getReplay().getOrig_url())) {
                                            video.setVideoPath(data.getData().getReplay().getOrig_url());
                                            video.start();
                                        }
                                    }
                                });
                                alertDialog.show();
                                alertDialog.setContentView(view);
                            } else if (NetUtils.isWifi(NEVideoPlaybackActivity.this)) {
                                if (data != null && data.getData() != null && data.getData().getReplay() != null && !StringUtils.isNullOrBlanK(data.getData().getReplay().getOrig_url())) {
                                    video.setVideoPath(data.getData().getReplay().getOrig_url());
                                    video.start();
                                }
                            }
                        } else {
                            Toast.makeText(NEVideoPlaybackActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
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
        video = (NEVideoView) findViewById(R.id.video);
        video.setmBufferStrategy(1);
        control = (RelativeLayout) findViewById(R.id.control);
        videolayout = (RelativeLayout) findViewById(R.id.video_layout);

        PlayBackVideoPresenter playBackVideoPresenter = new PlayBackVideoPresenter(this);
        playBackFloatFragment = new PlayBackFloatFragment(playBackVideoPresenter);
        getSupportFragmentManager().beginTransaction().replace(R.id.control, playBackFloatFragment).commit();

        playBackFloatFragment.setMediaPlayer(video);

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
            if (NetUtils.isConnected(NEVideoPlaybackActivity.this)) {
                if (NetUtils.isMobile(NEVideoPlaybackActivity.this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NEVideoPlaybackActivity.this);
                    final AlertDialog alertDialog = builder.create();
                    View view = View.inflate(NEVideoPlaybackActivity.this, R.layout.dialog_cancel_or_confirm, null);
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
                            video.start();
                            playBackFloatFragment.setPlayOrPause(true);
                        }
                    });
                    alertDialog.show();
                    alertDialog.setContentView(view);
                } else if (NetUtils.isWifi(NEVideoPlaybackActivity.this)) {
                    video.start();
                    playBackFloatFragment.setPlayOrPause(true);
                }
            } else {
                Toast.makeText(NEVideoPlaybackActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
            }

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
