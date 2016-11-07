package cn.qatime.player.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netease.neliveplayer.NELivePlayer;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.barrage.DanmakuView;
import cn.qatime.player.barrage.DanmuControl;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.Announcements;
import cn.qatime.player.fragment.PlayerAnnouncementsF;
import cn.qatime.player.fragment.PlayerMessageF;
import cn.qatime.player.fragment.PlayerLiveDetailsF;
import cn.qatime.player.fragment.PlayerMembersF;
import cn.qatime.player.fragment.VideoFloatFragment;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.presenter.VideoControlPresenter;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VideoActivityInterface;
import cn.qatime.player.view.BiaoQingView;
import cn.qatime.player.view.NEVideoView;
import cn.qatime.player.view.VideoLayout;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.KeyBoardUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.FragmentLayoutWithLine;

public class NEVideoPlayerActivity extends BaseFragmentActivity implements VideoActivityInterface, NELivePlayer.OnPreparedListener {
//    private View mBuffer; //用于指示缓冲状态

    private boolean isSubBig = true;//副窗口是否是大的
    private boolean ismain = true;//video1 是否在主显示view上

    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private View inputLayout;

    private int id;
    private PlayerMessageF fragment2;
    private String sessionId;
    private SessionTypeEnum sessionType = SessionTypeEnum.Team;
    private EditText content;
    private boolean isMute = false;//当前用户 是否被禁言
    private String url = "";


    private RelativeLayout mainVideo;
    private RelativeLayout mainView;
    //    private RelativeLayout control;
    private DanmakuView danmuView;
    private VideoLayout floatingWindow;
    private RelativeLayout subVideo;
    private VideoControlPresenter controlPresenter;
    private VideoFloatFragment floatFragment;
    private RelativeLayout whole;
    private NEVideoView video1;
    private NEVideoView video2;
    private DanmuControl danMuController;

    private void assignViews() {
        int width = ScreenUtils.getScreenWidth(this);
        video1 = new NEVideoView(this);
        video2 = new NEVideoView(this);
        ViewGroup.LayoutParams videoLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        video1.setLayoutParams(videoLayoutParam);
        video2.setLayoutParams(videoLayoutParam);
        video1.setOnPreparedListener(this);
        video2.setOnPreparedListener(this);

        whole = (RelativeLayout) findViewById(R.id.whole);
        mainVideo = (RelativeLayout) findViewById(R.id.main_video);
        mainView = (RelativeLayout) findViewById(R.id.main_view);
        danmuView = (DanmakuView) findViewById(R.id.danmuView);
        danMuController = new DanmuControl(this);
        danMuController.setDanmakuView(danmuView);

        floatingWindow = (VideoLayout) findViewById(R.id.floating_window);
        subVideo = (RelativeLayout) findViewById(R.id.sub_video);
        //控制框
        controlPresenter = new VideoControlPresenter(this);
        floatFragment = new VideoFloatFragment();
        floatFragment.setCallback(controlPresenter);
        getSupportFragmentManager().beginTransaction().replace(R.id.control, floatFragment).commit();

        ViewGroup.LayoutParams mainVideoParam = mainVideo.getLayoutParams();
        mainVideoParam.width = -1;
        mainVideoParam.height = width * 9 / 16;
        mainVideo.setLayoutParams(mainVideoParam);
        ViewGroup.LayoutParams danmuViewParam = danmuView.getLayoutParams();
        danmuViewParam.width = -1;
        danmuViewParam.height = width * 9 / 16;
        danmuView.setLayoutParams(danmuViewParam);
        ViewGroup.LayoutParams subVideoParam = subVideo.getLayoutParams();
        subVideoParam.width = -1;
        subVideoParam.height = width * 9 / 16;
        subVideo.setLayoutParams(subVideoParam);

        mainView.addView(video1);
        subVideo.addView(video2);

        video1.setVideoURI(Uri.parse("http://va0a19f55.live.126.net/live/834c6312006e4ffe927795a11fd317af.flv?netease=va0a19f55.live.126.net"));
        video2.setVideoURI(Uri.parse("http://va0a19f55.live.126.net/live/3d8d1d438b554741944ea809f1704a5e.flv?netease=va0a19f55.live.126.net"));
        video1.start();
        video2.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        id = getIntent().getIntExtra("id", 0);//从前一页进来的id 获取详情用
        //TODO
        id = 18;
        if (id == 0) {
            Toast.makeText(this, getResourceString(R.string.no_course_information), Toast.LENGTH_SHORT).show();
        }
        sessionId = getIntent().getStringExtra("sessionId");
        //TODO
        sessionId = "7964473";
        url = getIntent().getStringExtra("url");

        assignViews();
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
        TeamMember team = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount());
        if (team != null) {
            isMute = team.isMute();
            floatFragment.setMute(isMute);
        }

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
        fragment2 = (PlayerMessageF) fragBaseFragments.get(1);
        fragment2.setSessionId(sessionId);
        fragment2.requestTeamInfo();
        fragment2.setChatCallBack(new PlayerMessageF.Callback() {
            @Override
            public void back(List<IMMessage> result) {
                TeamMember team = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount());
                if (team != null) {
                    isMute = team.isMute();
                    floatFragment.setMute(isMute);
                }
                if (isMute) {
                    content.setHint(R.string.have_muted);
                } else {
                    content.setHint("");
                }
//                videoPlayer.addDanmaku(result);
            }
        });

        content = (EditText) findViewById(R.id.content);
        ImageView emoji = (ImageView) findViewById(R.id.emoji);

        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(content.getText().toString().trim(), false);
                content.setText("");
            }
        });
        BiaoQingView bq = (BiaoQingView) findViewById(R.id.biaoQingView);
        bq.init(content, emoji);
        if (isMute) {
            content.setHint(R.string.have_muted);
        } else {
            content.setHint("");
        }
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
        isMute = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount()).isMute();
        floatFragment.setMute(isMute);
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
                    }, new VolleyErrorListener() {
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
        danMuController.resume();
        fragment2.registerObservers(true);
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int screenW = ScreenUtils.getScreenWidth(NEVideoPlayerActivity.this);
        int screenH = ScreenUtils.getScreenHeight(NEVideoPlayerActivity.this);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
            // 全屏
            if (Build.VERSION.SDK_INT >= 11) {
                try {
                    getActionBar().hide();
                } catch (Exception e) {
                }
            }
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            if (isSubBig) {
                changeSubSmall();
                floatFragment.setSubBig(false);
            }
            ViewGroup.LayoutParams param = mainVideo.getLayoutParams();
            param.width = ViewGroup.LayoutParams.MATCH_PARENT;
            param.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mainVideo.setLayoutParams(param);
            mainView.setLayoutParams(param);
            if (ismain) {
                video1.setVideoScalingMode(true);
            } else {
                video2.setVideoScalingMode(true);
            }

            Logger.e("screenW" + screenW);
            Logger.e("screenH" + screenH);
            Logger.e("mainVideo.getWidth()" + mainVideo.getWidth());
            Logger.e("mainVideo.getHeight()" + mainVideo.getHeight());
            Logger.e("mainView.getWidth()" + mainView.getWidth());
            Logger.e("mainView.getHeight()" + mainView.getHeight());
            Logger.e("video1.getWidth()" + video1.getWidth());
            Logger.e("video1.getHeight()" + video1.getHeight());
            Logger.e("param.width" + param.width);
            Logger.e("param.height" + param.height);
            whole.removeView(danmuView);

            mainVideo.addView(danmuView, 1);
            danmuView.setLayoutParams(param);
            if (danmuView.getVisibility() == View.GONE) {
                danmuView.setVisibility(View.VISIBLE);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 11) {
                try {
                    getActionBar().show();
                } catch (Exception e) {
                }
            }
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            // 取消全屏设置
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            ViewGroup.LayoutParams param = mainVideo.getLayoutParams();
            param.width = -1;
            param.height = ScreenUtils.getScreenWidth(NEVideoPlayerActivity.this) * 9 / 16;
            mainView.setLayoutParams(param);
            mainVideo.setLayoutParams(param);
            mainVideo.removeView(danmuView);
            whole.addView(danmuView);
            RelativeLayout.LayoutParams danmuParam = new RelativeLayout.LayoutParams(param.width, param.height);
            danmuParam.addRule(RelativeLayout.BELOW, R.id.main_video);
            danmuView.setLayoutParams(danmuParam);
            danmuView.setVisibility(isSubBig ? View.VISIBLE : View.GONE);
        }


        float resultX = floatingWindow.getX();
        float resultY = floatingWindow.getY();
        if (resultX < 0) {
            resultX = 0;
        } else if (resultX >= screenW - floatingWindow.getWidth()) {
            resultX = screenW - floatingWindow.getWidth();
        }
        if (resultY < 0) {
            resultY = 0;
        } else if (resultY >= screenH - floatingWindow.getHeight()) {
            resultY = screenH - floatingWindow.getHeight();
        }
        floatingWindow.setX(resultX);
        floatingWindow.setY(resultY);
    }


    @Override
    protected void onPause() {
        video1.pause();
        video2.pause();
        floatFragment.setPlaying(false);

        super.onPause();
        danMuController.pause();
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
    }


    @Override
    protected void onDestroy() {
        video1.release_resource();
        video2.release_resource();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        danMuController.destroy();
        fragment2.registerObservers(false);
        fragment2.registerTeamUpdateObserver(false);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (floatFragment != null) {
                floatFragment.setPortrait();
            }
            return;
        }
        super.onBackPressed();
    }


    /**************************
     * VideoActivityInterface *
     **************************/
    @Override
    public void showDanmaku() {
        Logger.e("弹幕开启");
        danMuController.show();
    }

    @Override
    public void shutDanmaku() {
        Logger.e("弹幕关闭");
        danMuController.hide();
    }

    @Override
    public void refresh() {
        video1.seekTo(video1.getDuration());
        video2.seekTo(video2.getDuration());
    }

    @Override
    public void setOrientation(int orientation) {
        setRequestedOrientation(orientation);
    }

    @Override
    public void changeSubSmall() {
        isSubBig = false;
        floatingWindow.setVisibility(View.VISIBLE);
        subVideo.setVisibility(View.GONE);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            danmuView.setVisibility(View.GONE);
        } else {
            danmuView.setVisibility(View.VISIBLE);
        }
        if (ismain) {
            subVideo.removeView(video2);
            video2.setZOrderOnTop(true);
            floatingWindow.addView(video2);
        } else {
            subVideo.removeView(video1);
            video1.setZOrderOnTop(true);
            floatingWindow.addView(video1);
        }
    }

    @Override
    public void changeSubBig() {
        isSubBig = true;
        floatingWindow.setVisibility(View.GONE);
        subVideo.setVisibility(View.VISIBLE);
        danmuView.setVisibility(View.VISIBLE);
        if (ismain) {
            floatingWindow.removeView(video2);
            video2.setZOrderOnTop(true);
            subVideo.addView(video2);
        } else {
            floatingWindow.removeView(video1);
            video1.setZOrderOnTop(true);
            subVideo.addView(video1);
        }
    }

    @Override
    public void changeFloating2Main() {
        ismain = true;
        mainView.removeView(video2);
        floatingWindow.removeView(video1);
        video1.setZOrderOnTop(false);
        mainView.addView(video1);
        video2.setZOrderOnTop(true);
        floatingWindow.addView(video2);
    }

    @Override
    public void changeMain2Sub() {
        ismain = false;
        mainView.removeView(video1);
        subVideo.removeView(video2);
        video1.setZOrderOnTop(false);
        video2.setZOrderOnTop(false);
        mainView.addView(video2);
        subVideo.addView(video1);
    }

    @Override
    public void changeMain2Floating() {
        ismain = false;
        mainView.removeView(video1);
        floatingWindow.removeView(video2);
        video2.setZOrderOnTop(false);
        mainView.addView(video2);
        video1.setZOrderOnTop(true);
        floatingWindow.addView(video1);
    }

    @Override
    public void changeSub2Main() {
        ismain = true;
        mainView.removeView(video2);
        subVideo.removeView(video1);
        video1.setZOrderOnTop(false);
        video2.setZOrderOnTop(false);
        mainView.addView(video1);
        subVideo.addView(video2);
    }

    @Override
    public void exit() {
        onBackPressed();
    }

    @Override
    public void changeSubOpen(boolean open) {
        if (open) {
            if (isSubBig) {
                danmuView.setVisibility(View.VISIBLE);
                subVideo.setVisibility(View.VISIBLE);
            } else {
                if (ismain) {
                    video2.setVisibility(View.VISIBLE);
                } else {
                    video1.setVisibility(View.VISIBLE);
                }
                floatingWindow.setVisibility(View.VISIBLE);
            }
        } else {
            if (isSubBig) {
                danmuView.setVisibility(View.GONE);
                subVideo.setVisibility(View.GONE);
            } else {
                if (ismain) {
                    video2.setVisibility(View.GONE);
                } else {
                    video1.setVisibility(View.GONE);
                }
                floatingWindow.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void sendMessage(String message) {
        Logger.e("message" + message);
        sendMessage(message, true);
    }

    @Override
    public void play() {
        floatFragment.setPlaying(true);
    }

    @Override
    public void pause() {
        floatFragment.setPlaying(false);
    }

    @Override
    public void onPrepared(NELivePlayer neLivePlayer) {
    }
}
