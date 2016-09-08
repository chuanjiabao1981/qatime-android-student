package cn.qatime.player.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netease.neliveplayer.NELivePlayer;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.Announcements;
import cn.qatime.player.fragment.FragmentNEVideoPlayer1;
import cn.qatime.player.fragment.FragmentNEVideoPlayer2;
import cn.qatime.player.fragment.FragmentNEVideoPlayer3;
import cn.qatime.player.fragment.FragmentNEVideoPlayer4;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.BiaoQingView;
import cn.qatime.player.view.QaVideoPlayer;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.KeyBoardUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.FragmentLayoutWithLine;

public class NEVideoPlayerActivity extends BaseFragmentActivity implements QaVideoPlayer.ControlListener {
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

    private QaVideoPlayer videoPlayer;
    private View bottom;

    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private FragmentLayoutWithLine fragmentLayout;
    private View inputLayout;

    private int id;
    private FragmentNEVideoPlayer2 fragment2;
    private String sessionId;
    private SessionTypeEnum sessionType = SessionTypeEnum.Team;
    private ImageView emoji;
    private EditText content;
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
        id = getIntent().getIntExtra("id", 0);//从前一页进来的id 获取详情用
        if (id == 0) {
            Toast.makeText(this, "暂无辅导班信息", Toast.LENGTH_SHORT).show();
        }
        sessionId = getIntent().getStringExtra("sessionId");
        String url = getIntent().getStringExtra("url");
//        Logger.e(url);
        videoPlayer = (QaVideoPlayer) findViewById(R.id.video_player);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenWidth(this) * 9 / 16);
        videoPlayer.setLayoutParams(params);

        if (!StringUtils.isNullOrBlanK(url)) {
            videoPlayer.setVideoPath(url);
            videoPlayer.setOnControlListener(this);
            videoPlayer.start();
        }
        final String finalUrl = url;
        videoPlayer.setOnVideoRefreshListener(new QaVideoPlayer.VideoRefreshListener() {
            @Override
            public void onRefresh() {
                if (!StringUtils.isNullOrBlanK(finalUrl)) {
                    videoPlayer.release_resource();
                    videoPlayer.setVideoPath(finalUrl);
                    videoPlayer.setOnControlListener(NEVideoPlayerActivity.this);
                    videoPlayer.start();
                }
            }
        });
        initView();
        getAnnouncementsData();
        initData();
    }

    private void getAnnouncementsData() {
        if (id != 0) {
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlRemedialClass + "/" + id + "/realtime", null,
                    new VolleyListener(NEVideoPlayerActivity.this) {
                        @Override
                        protected void onSuccess(JSONObject response) {
                            Announcements data = JsonUtils.objectFromJson(response.toString(), Announcements.class);
                            if (data != null) {
                                if (data.getData() != null) {
                                    if (data.getData().getMembers() != null) {
                                        ((FragmentNEVideoPlayer4) fragBaseFragments.get(3)).setData(data.getData().getMembers());
                                    }
                                    if (data.getData().getAnnouncements() != null) {
                                        ((FragmentNEVideoPlayer1) fragBaseFragments.get(0)).setData(data.getData().getAnnouncements());
                                    }
                                }

                            }
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
                    KeyBoardUtils.closeKeybord(NEVideoPlayerActivity.this);
                    inputLayout.setVisibility(View.GONE);
                }
            }
        });
        fragmentLayout.setAdapter(fragBaseFragments, R.layout.tablayout_nevideo_player, 0x0102);
        fragmentLayout.getViewPager().setOffscreenPageLimit(3);
        fragment2 = (FragmentNEVideoPlayer2) fragBaseFragments.get(1);
        fragment2.setSessionId(sessionId);
        fragment2.requestTeamInfo();
        fragment2.setChatCallBack(new FragmentNEVideoPlayer2.Callback() {
            @Override
            public void back(List<IMMessage> result) {
                videoPlayer.addDanmaku(result);
            }
        });

        content = (EditText) findViewById(R.id.content);
        emoji = (ImageView) findViewById(R.id.emoji);

        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(content.getText().toString().trim(), false);
                content.setText("");
            }
        });
        BiaoQingView bq = (BiaoQingView) findViewById(R.id.biaoQingView);
        bq.init(content,emoji);
        videoPlayer.setChatCallback(new QaVideoPlayer.ChatCallback() {
            @Override
            public void back(String result) {
                Logger.e(result + "result");
                sendMessage(result, true);
            }
        });
    }

    /**
     * 發送消息
     *
     * @param comment       聊天內容
     * @param isSendToDanmu 是否将消息展示弹幕
     */
    private void sendMessage(String comment, boolean isSendToDanmu) {
        if (!fragment2.isAllowSendMessage()) {
            Toast.makeText(NEVideoPlayerActivity.this, "您已不在该群,不能发送消息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNullOrBlanK(comment)) {
            Toast.makeText(NEVideoPlayerActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        // 创建文本消息
        IMMessage message = MessageBuilder.createTextMessage(
                sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                sessionType, // 聊天类型，单聊或群组
                comment.trim() // 文本内容
        );
        // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
        NIMClient.getService(MsgService.class).sendMessage(message, true);
        //横屏状态下,将发送的消息展示到弹幕去
        if (isSendToDanmu) {
            videoPlayer.addDanmaku(message, 0);
        }
        fragment2.items.add(message);
        fragment2.adapter.notifyDataSetChanged();
        fragment2.listView.getRefreshableView().setSelection(fragment2.items.size() - 1);
    }

    private void initData() {
        if (id != 0) {
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlRemedialClass + "/" + id + "/play_info", null,
                    new VolleyListener(NEVideoPlayerActivity.this) {
                        @Override
                        protected void onSuccess(JSONObject response) {
                            RemedialClassDetailBean data = JsonUtils.objectFromJson(response.toString(), RemedialClassDetailBean.class);
                            if (data != null) {
                                ((FragmentNEVideoPlayer3) fragBaseFragments.get(2)).setData(data);
//                                if (data.getData() != null && data.getData().getChat_team() != null && data.getData().getChat_team().getAccounts() != null) {
//                                    ((FragmentNEVideoPlayer4) fragBaseFragments.get(3)).setData(data.getData().getChat_team().getAccounts());
//                                }
//                                ((FragmentNEVideoPlayer1) fragBaseFragments.get(0)).setTeamId(data.getData().getChat_team_id());
                            }
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
    }

    @Override
    protected void onResume() {
        if (videoPlayer.isPauseInBackgroud() && !videoPlayer.isPaused()) {
            videoPlayer.start(); //锁屏打开后恢复播放
        }
        super.onResume();
        videoPlayer.BarrageResume();
        fragment2.registerObservers(true);
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
            bottom.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = videoPlayer.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoPlayer.setLayoutParams(params);
        } else {
            bottom.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = videoPlayer.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ScreenUtils.getScreenWidth(this) * 9 / 16;
            videoPlayer.setLayoutParams(params);
        }
//        videoPlayer.release_resource();
//        videoPlayer.start();
    }


    @Override
    protected void onPause() {
        videoPlayer.pause(); //锁屏时暂停
        super.onPause();
        videoPlayer.BarragePause();
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
    }


    @Override
    protected void onDestroy() {
        videoPlayer.release_resource();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        videoPlayer.BarrageDestory();
//        hd.removeCallbacks(runnable);
        super.onDestroy();
        fragment2.registerObservers(false);
        fragment2.registerTeamUpdateObserver(false);
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
