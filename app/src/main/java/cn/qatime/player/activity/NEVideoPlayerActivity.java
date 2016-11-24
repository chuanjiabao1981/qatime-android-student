package cn.qatime.player.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
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
import com.zhy.android.percent.support.PercentRelativeLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.barrage.DanmakuView;
import cn.qatime.player.barrage.DanmuControl;
import cn.qatime.player.barrage.model.Status;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.fragment.VideoFloatFragment;
import libraryextra.bean.Announcements;
import cn.qatime.player.fragment.FragmentPlayerAnnouncements;
import cn.qatime.player.fragment.FragmentPlayerMessage;
import cn.qatime.player.fragment.FragmentPlayerLiveDetails;
import cn.qatime.player.fragment.FragmentPlayerMembers;
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

public class NEVideoPlayerActivity extends BaseFragmentActivity implements VideoActivityInterface, VideoLayout.OnDoubleClickListener {
//    private View mBuffer; //用于指示缓冲状态

    private boolean isSubBig = true;//副窗口是否是大的
    private boolean ismain = true;//video1 是否在主显示view上
    private int orientation = Configuration.ORIENTATION_PORTRAIT;//当前屏幕横竖屏状态
    private boolean isSubOpen = true;//副窗口开关

    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private View inputLayout;

    private int id;
    private FragmentPlayerMessage fragment2;
    private String sessionId;
    private SessionTypeEnum sessionType = SessionTypeEnum.Team;
    private EditText content;
    private boolean isMute = false;//当前用户 是否被禁言

    private RelativeLayout mainVideo;
    private RelativeLayout mainView;
    private DanmakuView danmuView;
    private VideoLayout floatingWindow;
    private RelativeLayout subVideo;
    private VideoFloatFragment floatFragment;
    private RelativeLayout whole;
    private NEVideoView video1;
    private NEVideoView video2;
    private DanmuControl danMuController;
    private int screenH;
    private int screenW;
    private View window2;
    private View window1;
    private ImageView videoNoData1;
    private ImageView videoNoData2;
    private AnimationDrawable bufferAnimation1;
    private AnimationDrawable bufferAnimation2;
    private PercentRelativeLayout buffering1;
    private PercentRelativeLayout buffering2;


    private void assignViews() {
        screenW = ScreenUtils.getScreenWidth(NEVideoPlayerActivity.this);
        screenH = ScreenUtils.getScreenHeight(NEVideoPlayerActivity.this);

        window1 = findViewById(R.id.window1);
        window2 = findViewById(R.id.window2);

        buffering1 = (PercentRelativeLayout) findViewById(R.id.buffering1);
        buffering2 = (PercentRelativeLayout) findViewById(R.id.buffering2);

        ImageView bufferImage1 = (ImageView) findViewById(R.id.buffer_image1);
        ImageView bufferImage2 = (ImageView) findViewById(R.id.buffer_image2);

        bufferAnimation1 = (AnimationDrawable) bufferImage1.getBackground();
        bufferAnimation1.start();
        bufferAnimation2 = (AnimationDrawable) bufferImage2.getBackground();
        bufferAnimation2.start();

        videoNoData1 = (ImageView) findViewById(R.id.video_no_data1);
        videoNoData2 = (ImageView) findViewById(R.id.video_no_data2);

        video1 = (NEVideoView) findViewById(R.id.video1);
        video2 = (NEVideoView) findViewById(R.id.video2);
        video1.setBufferPrompt(buffering1);
        video2.setBufferPrompt(buffering2);


        video1.setOnErrorListener(new NELivePlayer.OnErrorListener() {
            @Override
            public boolean onError(NELivePlayer neLivePlayer, int i, int i1) {
                buffering1.setVisibility(View.GONE);
                bufferAnimation1.stop();
                videoNoData1.setVisibility(View.VISIBLE);
                return true;
            }
        });
        video2.setOnErrorListener(new NELivePlayer.OnErrorListener() {
            @Override
            public boolean onError(NELivePlayer neLivePlayer, int i, int i1) {
                buffering2.setVisibility(View.GONE);
                bufferAnimation2.stop();
                videoNoData2.setVisibility(View.VISIBLE);
                return true;
            }
        });

        whole = (RelativeLayout) findViewById(R.id.whole);
        mainVideo = (RelativeLayout) findViewById(R.id.main_video);
        mainView = (RelativeLayout) findViewById(R.id.main_view);
        danmuView = (DanmakuView) findViewById(R.id.danmuView);
        danMuController = new DanmuControl(this);
        danMuController.setDanmakuView(danmuView);

        floatingWindow = (VideoLayout) findViewById(R.id.floating_window);
        subVideo = (RelativeLayout) findViewById(R.id.sub_video);
        //控制框
        VideoControlPresenter controlPresenter = new VideoControlPresenter(this);
        floatFragment = new VideoFloatFragment();
        floatFragment.setCallback(controlPresenter);
        getSupportFragmentManager().beginTransaction().replace(R.id.control, floatFragment).commit();

        ViewGroup.LayoutParams mainVideoParam = mainVideo.getLayoutParams();
        mainVideoParam.width = -1;
        mainVideoParam.height = screenW * 9 / 16;
        mainVideo.setLayoutParams(mainVideoParam);
        ViewGroup.LayoutParams danmuViewParam = danmuView.getLayoutParams();
        danmuViewParam.width = -1;
        danmuViewParam.height = screenW * 9 / 16;
        danmuView.setLayoutParams(danmuViewParam);
        ViewGroup.LayoutParams subVideoParam = subVideo.getLayoutParams();
        subVideoParam.width = -1;
        subVideoParam.height = screenW * 9 / 16;
        subVideo.setLayoutParams(subVideoParam);
        ViewGroup.LayoutParams floatingWindowParam = floatingWindow.getLayoutParams();
        floatingWindowParam.width = screenW * 2 / 5;
        floatingWindowParam.height = floatingWindowParam.width * 9 / 16;
        floatingWindow.setLayoutParams(floatingWindowParam);
        floatingWindow.setOnDoubleClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        id = getIntent().getIntExtra("id", 0);//从前一页进来的id 获取详情用
        if (id == 0) {
            Toast.makeText(this, getResourceString(R.string.no_course_information), Toast.LENGTH_SHORT).show();
        }
        sessionId = getIntent().getStringExtra("sessionId");

        String camera = getIntent().getStringExtra("camera");
        String board = getIntent().getStringExtra("board");

        assignViews();
        initView();
        getAnnouncementsData();
        initData();
//        camera = "rtmp://va0a19f55.live.126.net/live/02dce8e380034cf9b2ef1f9c26c4234c";
//        board = "rtmp://va0a19f55.live.126.net/live/1243a663c3e54b099d1cc35ee83a7921";
        if (!StringUtils.isNullOrBlanK(camera)) {
            video2.setVideoPath(camera);
            video2.start();
        } else {
            buffering2.setVisibility(View.GONE);
            bufferAnimation2.stop();
            videoNoData2.setVisibility(View.VISIBLE);
        }
        if (!StringUtils.isNullOrBlanK(board)) {
            video1.setVideoPath(board);
            video1.start();
        } else {
            buffering1.setVisibility(View.GONE);
            bufferAnimation1.stop();
            videoNoData1.setVisibility(View.VISIBLE);
        }
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
                                        ((FragmentPlayerMembers) fragBaseFragments.get(3)).setData(data.getData().getMembers());
                                    }
                                    if (data.getData().getAnnouncements() != null) {
                                        ((FragmentPlayerAnnouncements) fragBaseFragments.get(0)).setData(data.getData().getAnnouncements());
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
        if (!StringUtils.isNullOrBlanK(sessionId)) {
            TeamMember team = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount());
            if (team != null) {
                isMute = team.isMute();
                floatFragment.setMute(isMute);
            }
        }

        inputLayout = findViewById(R.id.input_layout);
        fragBaseFragments.add(new FragmentPlayerAnnouncements());
        fragBaseFragments.add(new FragmentPlayerMessage());
        fragBaseFragments.add(new FragmentPlayerLiveDetails());
        fragBaseFragments.add(new FragmentPlayerMembers());

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
                if (isSubBig) {
                    changeSubSmall();
                    floatFragment.setSubBig(false);
                }
            }
        });
        fragmentLayout.setAdapter(fragBaseFragments, R.layout.tablayout_nevideo_player, 0x0102);
        fragmentLayout.getViewPager().setOffscreenPageLimit(3);
        fragment2 = (FragmentPlayerMessage) fragBaseFragments.get(1);
        fragment2.setSessionId(sessionId);
        fragment2.requestTeamInfo();
        fragment2.setChatCallBack(new FragmentPlayerMessage.Callback() {
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
                if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && isSubBig) | getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    danMuController.addDanmuList(result);
                }
            }
        });

        content = (EditText) findViewById(R.id.content);
        ImageView emoji = (ImageView) findViewById(R.id.emoji);

        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(content.getText().toString().trim(), isSubBig);
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
        if (StringUtils.isNullOrBlanK(sessionId)) {
            Toast.makeText(NEVideoPlayerActivity.this, getResourceString(R.string.team_not_exist), Toast.LENGTH_SHORT).show();
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
        if (isSendToDanmu) {
            danMuController.addDanmu(message, 0);
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
                                ((FragmentPlayerLiveDetails) fragBaseFragments.get(2)).setData(data);
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
        super.onResume();
        assert danMuController != null;
        danMuController.resume();
        fragment2.registerObservers(true);
        if (!StringUtils.isNullOrBlanK(sessionId)) {
            NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
        } else {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        }
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) { // 横屏
            orientation = Configuration.ORIENTATION_LANDSCAPE;

            content.setText("");
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            ViewGroup.LayoutParams param = mainVideo.getLayoutParams();
            param.width = ViewGroup.LayoutParams.MATCH_PARENT;
            param.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mainVideo.setLayoutParams(param);
            mainView.setLayoutParams(param);
            if (ismain) {
                video1.setVideoScalingMode(true);
                whole.removeView(danmuView);
                mainVideo.addView(danmuView, 1);
            } else {
                video2.setVideoScalingMode(true);
            }

            danmuView.setLayoutParams(param);
            //横屏时会切换为小窗口,切换时已经对弹幕做了改变
//            if (danmuView.getVisibility() == View.GONE) {
//                danmuView.setVisibility(View.VISIBLE);
//            }
            if (isSubBig) {
                changeSubSmall();
                floatFragment.setSubBig(false);
                floatFragment.setSubOpen(true);
            }
            //横屏时打开弹幕
            Logger.e("弹幕状态" + danMuController.getStatus().toString());
            if (danMuController.getStatus() == Status.HIDE) {
                showDanmaku();
                floatFragment.setDanmuOn(true);
            }
        } else {
            orientation = Configuration.ORIENTATION_PORTRAIT;
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
            if (ismain) {
                RelativeLayout.LayoutParams danmuParam = null;
                mainVideo.removeView(danmuView);
                whole.addView(danmuView);
                danmuParam = new RelativeLayout.LayoutParams(param.width, param.height);
                danmuParam.addRule(RelativeLayout.BELOW, R.id.main_video);
                danmuView.setLayoutParams(danmuParam);
            }
            danmuView.setVisibility(isSubBig ? View.VISIBLE : View.GONE);
        }

        //如果悬浮窗口在屏幕外,切换时移动到屏幕内
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
        video1 = null;
        video2 = null;

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        danMuController.destroy();
        fragment2.registerObservers(false);
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
        video1.seekTo(video1.getCurrentPosition());
        video2.seekTo(video2.getCurrentPosition());
    }

    @Override
    public void setOrientation(int orientation) {
        setRequestedOrientation(orientation);
    }

    @Override
    public void changeSubSmall() {
        isSubBig = false;
        boolean needReStart = false;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            danmuView.setVisibility(View.GONE);
        } else {
            danmuView.setVisibility(View.VISIBLE);
        }
        if (ismain) {
            if (buffering2.getVisibility() == View.VISIBLE && bufferAnimation2.isRunning()) {
                needReStart = true;
                bufferAnimation2.stop();
            }
            subVideo.removeView(window2);
            video2.setZOrderOnTop(true);
            floatingWindow.addView(window2);
            video2.setVideoScalingMode(true);
            if (needReStart) {
                bufferAnimation2.start();
            }
        } else {
            if (buffering1.getVisibility() == View.VISIBLE && bufferAnimation1.isRunning()) {
                needReStart = true;
                bufferAnimation1.stop();
            }
            subVideo.removeView(window1);
            video1.setZOrderOnTop(true);
            floatingWindow.addView(window1);
            if (needReStart) {
                bufferAnimation1.start();
            }
        }
        floatingWindow.setVisibility(View.VISIBLE);
        subVideo.setVisibility(View.GONE);
    }

    @Override
    public void changeSubBig() {
        isSubBig = true;
        if (ismain) {
            floatingWindow.removeView(window2);
            video2.setZOrderOnTop(false);
            subVideo.addView(window2);
        } else {
            floatingWindow.removeView(window1);
            video1.setZOrderOnTop(false);
            subVideo.addView(window1);
        }
        floatingWindow.setVisibility(View.GONE);
        subVideo.setVisibility(isSubOpen ? View.VISIBLE : View.GONE);
        danmuView.setVisibility(isSubOpen ? View.VISIBLE : View.GONE);
    }

    @Override
    public void changeFloating2Main() {
        ismain = true;
        mainView.removeView(window2);
        floatingWindow.removeView(window1);
        video1.setZOrderOnTop(false);
        mainView.addView(window1);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            video1.setVideoScalingMode(true);
        }
        video2.setZOrderOnTop(true);
        floatingWindow.addView(window2);

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mainVideo.removeView(danmuView);
            whole.addView(danmuView);
            RelativeLayout.LayoutParams danmuParam = new RelativeLayout.LayoutParams(-1, ScreenUtils.getScreenWidth(NEVideoPlayerActivity.this) * 9 / 16);
            danmuParam.addRule(RelativeLayout.BELOW, R.id.main_video);
            danmuView.setLayoutParams(danmuParam);
            danmuView.setVisibility(View.GONE);
        }
    }

    @Override
    public void changeMain2Sub() {
        ismain = false;
        mainView.removeView(window1);
        subVideo.removeView(window2);
        video1.setZOrderOnTop(false);
        video2.setZOrderOnTop(false);

        whole.removeView(danmuView);
        mainVideo.addView(danmuView, 1);
        mainView.addView(window2);
        subVideo.addView(window1);
    }

    @Override
    public void changeMain2Floating() {
        ismain = false;
        mainView.removeView(window1);
        floatingWindow.removeView(window2);
        video2.setZOrderOnTop(false);

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            whole.removeView(danmuView);
            mainVideo.addView(danmuView, 1);
        }

//        mainView.addView(video2);
        mainView.addView(window2);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            video2.setVideoScalingMode(true);
        }
        video1.setZOrderOnTop(true);
//        floatingWindow.addView(video1);
        floatingWindow.addView(window1);
    }

    @Override
    public void changeSub2Main() {
        ismain = true;
//        mainView.removeView(video2);
//        subVideo.removeView(video1);
        mainView.removeView(window2);
        subVideo.removeView(window1);
        video1.setZOrderOnTop(false);
        video2.setZOrderOnTop(false);

        mainVideo.removeView(danmuView);
        whole.addView(danmuView);
        RelativeLayout.LayoutParams danmuParam = new RelativeLayout.LayoutParams(-1, ScreenUtils.getScreenWidth(NEVideoPlayerActivity.this) * 9 / 16);
        danmuParam.addRule(RelativeLayout.BELOW, R.id.main_video);
        danmuView.setLayoutParams(danmuParam);
        danmuView.setVisibility(View.VISIBLE);
//        mainView.addView(video1);
//        subVideo.addView(video2);
        mainView.addView(window1);
        subVideo.addView(window2);
    }

    @Override
    public void exit() {
        onBackPressed();
    }

    @Override
    public void changeSubOpen(boolean open) {
        this.isSubOpen = open;
        if (open) {
            if (isSubBig) {
                danmuView.setVisibility(View.VISIBLE);
                subVideo.setVisibility(View.VISIBLE);
            } else {
                if (ismain) {
                    window2.setVisibility(View.VISIBLE);
                } else {
                    window1.setVisibility(View.VISIBLE);
                }
                floatingWindow.setVisibility(View.VISIBLE);
            }
        } else {
            if (isSubBig) {
                danmuView.setVisibility(View.GONE);
                subVideo.setVisibility(View.GONE);
            } else {
                if (ismain) {
                    window2.setVisibility(View.GONE);
                } else {
                    window1.setVisibility(View.GONE);
                }
                floatingWindow.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void sendMessage(String message) {
        Logger.e("message" + message);
        sendMessage(message, (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && isSubBig) | getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    @Override
    public void play() {
        video1.start();
        video2.start();
    }

    @Override
    public boolean isPlaying() {
        return video1.isPlaying() | video2.isPlaying();
    }

    @Override
    public void pause() {
        video1.pause();
        video2.pause();
    }

    /**
     * 悬浮窗双击效果
     */
    @Override
    public void onDoubleClick() {
        if (floatFragment == null) {
            return;
        }
        floatFragment.switchVideo();
    }
}
