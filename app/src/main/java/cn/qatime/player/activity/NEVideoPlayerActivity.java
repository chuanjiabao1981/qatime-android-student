package cn.qatime.player.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.barrage.DanmakuView;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.Announcements;
import cn.qatime.player.fragment.PlayerAnnouncementsF;
import cn.qatime.player.fragment.PlayerMessageF;
import cn.qatime.player.fragment.PlayerLiveDetailsF;
import cn.qatime.player.fragment.PlayerMembersF;
import cn.qatime.player.fragment.VideoFloatFragment;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.VideoLayout;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.KeyBoardUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.FragmentLayoutWithLine;

public class NEVideoPlayerActivity extends BaseFragmentActivity {
//    public NEVideoView mVideoView;  //用于画面显示
//    private View mBuffer; //用于指示缓冲状态
//    private NEMediaController mMediaController; //用于控制播放

    private View bottom;

    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private View inputLayout;

    private int id;
    private PlayerMessageF fragment2;
    private String sessionId;
    private SessionTypeEnum sessionType = SessionTypeEnum.Team;
    private ImageView emoji;
    private EditText content;
    private boolean isMute = false;//当前用户 是否被禁言
    private String url = "";


    private RelativeLayout mainVideo;
    private RelativeLayout mainView;
//    private RelativeLayout control;
    private DanmakuView danmuView;
    private VideoLayout floatingWindow;
    private RelativeLayout subVideo;

    private void assignViews() {
        mainVideo = (RelativeLayout) findViewById(R.id.main_video);
        mainView = (RelativeLayout) findViewById(R.id.main_view);
//        control = (RelativeLayout) findViewById(R.id.control);
        danmuView = (DanmakuView) findViewById(R.id.danmuView);
        floatingWindow = (VideoLayout) findViewById(R.id.floating_window);
        subVideo = (RelativeLayout) findViewById(R.id.sub_video);
        //控制框
        VideoFloatFragment floatFragment = new VideoFloatFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.control,floatFragment).commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
//        id = getIntent().getIntExtra("id", 0);//从前一页进来的id 获取详情用
//        if (id == 0) {
//            Toast.makeText(this, getResourceString(R.string.no_course_information), Toast.LENGTH_SHORT).show();
//        }
//        sessionId = getIntent().getStringExtra("sessionId");
//        url = getIntent().getStringExtra("url");
////        Logger.e(url);
//        videoPlayer = (QaVideoPlayer) findViewById(R.id.video_player);
//        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenWidth(this) * 9 / 16);
//        videoPlayer.setLayoutParams(params);
//
//        if (!StringUtils.isNullOrBlanK(url)) {
//            videoPlayer.setVideoPath(url);
//            videoPlayer.setOnControlListener(this);
//            videoPlayer.start();
//        }
        assignViews();
        initView();
//        getAnnouncementsData();
//        initData();
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
                                        ((PlayerMembersF) fragBaseFragments.get(3)).setData(data.getData().getMembers());
                                    }
                                    if (data.getData().getAnnouncements() != null) {
                                        ((PlayerAnnouncementsF) fragBaseFragments.get(0)).setData(data.getData().getAnnouncements());
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
                    }, new VolleyErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    super.onErrorResponse(volleyError);
                }
            });
            addToRequestQueue(request);
        }
    }

    private void initView() {
//        TeamMember team = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount());
//        if (team != null) {
//            isMute = team.isMute();
//        }
        bottom = findViewById(R.id.bottom);

        inputLayout = findViewById(R.id.input_layout);
        fragBaseFragments.add(new PlayerAnnouncementsF());
        fragBaseFragments.add(new PlayerMessageF());
        fragBaseFragments.add(new PlayerLiveDetailsF());
        fragBaseFragments.add(new PlayerMembersF());

        FragmentLayoutWithLine fragmentLayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentLayout.setScorllToNext(true);
        fragmentLayout.setScorll(true);
        fragmentLayout.setWhereTab(1);
        fragmentLayout.setTabHeight(4, 0xffff9999);
        fragmentLayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
                if (position == 1) {
                    inputLayout.setVisibility(View.VISIBLE);
                } else {
                    KeyBoardUtils.closeKeybord(NEVideoPlayerActivity.this);
                    inputLayout.setVisibility(View.GONE);
                }
            }
        });
        fragmentLayout.setAdapter(fragBaseFragments, R.layout.tablayout_nevideo_player, 0x0102);
        fragmentLayout.getViewPager().setOffscreenPageLimit(3);
//        fragment2 = (PlayerMessageF) fragBaseFragments.get(1);
//        fragment2.setSessionId(sessionId);
//        fragment2.requestTeamInfo();
//        fragment2.setChatCallBack(new PlayerMessageF.Callback() {
//            @Override
//            public void back(List<IMMessage> result) {
//                TeamMember team = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount());
//                if (team != null) {
//                    isMute = team.isMute();
//                }
//                if (isMute) {
//                    content.setHint(R.string.have_muted);
//                } else {
//                    content.setHint("");
//                }
////                videoPlayer.addDanmaku(result);
//            }
//        });

//        content = (EditText) findViewById(R.id.content);
//        emoji = (ImageView) findViewById(R.id.emoji);
//
//        Button send = (Button) findViewById(R.id.send);
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendMessage(content.getText().toString().trim(), false);
//                content.setText("");
//            }
//        });
//        BiaoQingView bq = (BiaoQingView) findViewById(R.id.biaoQingView);
//        bq.init(content, emoji);
//        videoPlayer.setChatCallback(new QaVideoPlayer.ChatCallback() {
//            @Override
//            public void back(String result) {
//                Logger.e(result + "result");
//                sendMessage(result, true);
//            }
//        });
//        if (isMute) {
//            content.setHint(R.string.have_muted);
//        } else {
//            content.setHint("");
//        }
    }

    /**
     * 發送消息
     *
     * @param comment       聊天內容
     * @param isSendToDanmu 是否将消息展示弹幕
     */
    private void sendMessage(String comment, boolean isSendToDanmu) {
        if (!fragment2.isAllowSendMessage()) {
            Toast.makeText(NEVideoPlayerActivity.this, getResourceString(R.string.team_send_message_not_allow), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNullOrBlanK(comment)) {
            Toast.makeText(NEVideoPlayerActivity.this, getResourceString(R.string.message_can_not_null), Toast.LENGTH_SHORT).show();
            return;
        }
//        isMute = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount()).isMute();
        if (isMute) {
            Toast.makeText(NEVideoPlayerActivity.this, getResources().getString(R.string.have_muted), Toast.LENGTH_SHORT).show();
            content.setText("");
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
//        if (isSendToDanmu) {
//            videoPlayer.addDanmaku(message, 0);
//        }
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
                                ((PlayerLiveDetailsF) fragBaseFragments.get(2)).setData(data);
//                                if (data.getData() != null && data.getData().getChat_team() != null && data.getData().getChat_team().getAccounts() != null) {
//                                    ((PlayerMembersF) fragBaseFragments.get(3)).setData(data.getData().getChat_team().getAccounts());
//                                }
//                                ((PlayerAnnouncementsF) fragBaseFragments.get(0)).setTeamId(data.getData().getChat_team_id());
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
//        if (videoPlayer.isPauseInBackgroud() && !videoPlayer.isPaused()) {
//            videoPlayer.start(); //锁屏打开后恢复播放
//        }
        super.onResume();
//        videoPlayer.BarrageResume();
//        fragment2.registerObservers(true);
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
//            bottom.setVisibility(View.GONE);
//            ViewGroup.LayoutParams params = videoPlayer.getLayoutParams();
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//            videoPlayer.setLayoutParams(params);
//        } else {
//            bottom.setVisibility(View.VISIBLE);
//            ViewGroup.LayoutParams params = videoPlayer.getLayoutParams();
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            params.height = ScreenUtils.getScreenWidth(this) * 9 / 16;
//            videoPlayer.setLayoutParams(params);
//        }
    }


    @Override
    protected void onPause() {
//        videoPlayer.pause(); //锁屏时暂停
        super.onPause();
//        videoPlayer.BarragePause();
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
    }


    @Override
    protected void onDestroy() {
//        videoPlayer.release_resource();
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
//        videoPlayer.BarrageDestory();
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
        if (findViewById(R.id.viewPager) != null && findViewById(R.id.viewPager).getVisibility() == View.VISIBLE) {
            findViewById(R.id.viewPager).setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

}
