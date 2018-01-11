package cn.qatime.player.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netease.neliveplayer.NELivePlayer;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.barrage.DanmakuView;
import cn.qatime.player.barrage.DanmuControl;
import cn.qatime.player.barrage.model.Status;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.BusEvent;
import cn.qatime.player.bean.InputPanel;
import cn.qatime.player.bean.LiveStatusBean;
import cn.qatime.player.bean.VideoState;
import cn.qatime.player.fragment.FragmentAnnouncements;
import cn.qatime.player.fragment.FragmentPlayerLiveDetails;
import cn.qatime.player.fragment.FragmentPlayerMembers;
import cn.qatime.player.fragment.FragmentPlayerMessage;
import cn.qatime.player.fragment.VideoFloatFragment;
import cn.qatime.player.im.SimpleCallback;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.presenter.VideoControlPresenter;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VideoActivityInterface;
import cn.qatime.player.view.NEVideoView;
import cn.qatime.player.view.VideoLayout;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.NetUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.FragmentLayoutWithLine;

public class NEVideoPlayerActivity extends BaseFragmentActivity implements VideoActivityInterface, VideoLayout.OnDoubleClickListener, InputPanel.InputPanelListener {

    private boolean isSubBig = true;//副窗口是否是大的
    private boolean isMain = true;//video1 是否在主显示view上
    //    private int orientation = Configuration.ORIENTATION_PORTRAIT;//当前屏幕横竖屏状态
    private boolean isSubOpen = true;//副窗口开关
    public List<IMMessage> limitMessage = new ArrayList<>();//用于限制2s内发送消息

    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    private int id;
    private FragmentPlayerMessage fragment2;
    private String sessionId = "";
    private SessionTypeEnum sessionType = SessionTypeEnum.Team;
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
    private RelativeLayout window2;
    private RelativeLayout window1;
    private ImageView videoNoData1;
    private ImageView videoNoData2;
    private ImageView buffering1;
    private ImageView buffering2;

    private Handler hd = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            queryVideoState();
            hd.postDelayed(this, 5000);
        }
    };
    private VideoState videoState;
    private View rootView;
    private InputPanel inputPanel;
    private String camera;
    private String board;
    private boolean canLoop = false;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (NetUtils.checkRecordAudioPermission(this)) {
                } else {
                    Toast.makeText(this, "未取得录音权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void assignViews() {
        int screenW = ScreenUtils.getScreenWidth(NEVideoPlayerActivity.this);

        window1 = (RelativeLayout) findViewById(R.id.window1);
        window2 = (RelativeLayout) findViewById(R.id.window2);

        buffering1 = (ImageView) findViewById(R.id.buffering1);
        buffering2 = (ImageView) findViewById(R.id.buffering2);

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
                videoNoData1.setVisibility(View.VISIBLE);
                return true;
            }
        });
        video2.setOnErrorListener(new NELivePlayer.OnErrorListener() {
            @Override
            public boolean onError(NELivePlayer neLivePlayer, int i, int i1) {
                buffering2.setVisibility(View.GONE);
                videoNoData2.setImageResource(R.mipmap.video_no_data);
                videoNoData2.setVisibility(View.VISIBLE);
                return true;
            }
        });

        video1.setOnCompletionListener(new NELivePlayer.OnCompletionListener() {
            @Override
            public void onCompletion(NELivePlayer neLivePlayer) {
                video1.release_resource();
            }
        });
        video2.setOnCompletionListener(new NELivePlayer.OnCompletionListener() {
            @Override
            public void onCompletion(NELivePlayer neLivePlayer) {
                video2.release_resource();
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
        rootView = View.inflate(this, R.layout.activity_player, null);
        setContentView(rootView);

        id = getIntent().getIntExtra("id", 0);//从前一页进来的id 获取详情用
        if (id == 0) {
            Toast.makeText(this, getResourceString(R.string.no_course_information), Toast.LENGTH_SHORT).show();
        }
        EventBus.getDefault().register(this);
        assignViews();
        initView();
        initData();

    }

    private void refreshState() {
        if (!StringUtils.isNullOrBlanK(camera)) {
            if (videoState == VideoState.PLAYING) {
                if (video2 != null && !video2.isPlaying()) {
                    if (videoNoData2.getVisibility() == View.VISIBLE) {
                        videoNoData2.setVisibility(View.GONE);
                    }
                    video2.setVideoPath(camera);
                    video2.start();
                }
            } else if (videoState == VideoState.CLOSED) {
                if (videoNoData2.getVisibility() == View.GONE) {
                    videoNoData2.setImageResource(R.mipmap.video_closed);
                    videoNoData2.setVisibility(View.VISIBLE);//摄像头关闭
                }
            }
        } else {
            if (buffering2.getVisibility() == View.VISIBLE) {
                buffering2.setVisibility(View.GONE);
            }
            if (videoNoData2.getVisibility() == View.GONE) {
                videoNoData2.setImageResource(R.mipmap.video_no_data);
                videoNoData2.setVisibility(View.VISIBLE);
            }
        }

        if (!StringUtils.isNullOrBlanK(board)) {
            if (video1 != null && !video1.isPlaying()) {
                if (videoNoData1.getVisibility() == View.VISIBLE) {
                    videoNoData1.setVisibility(View.GONE);
                }
                floatFragment.setPlaying(true);
                video1.setVideoPath(board);
                video1.start();
            }
        } else {
            if (buffering1.getVisibility() == View.VISIBLE) {
                buffering1.setVisibility(View.GONE);
            }
            if (videoNoData1.getVisibility() == View.GONE) {
                videoNoData1.setVisibility(View.VISIBLE);
            }
        }
    }


    private void getAnnouncementsData() {
        if (StringUtils.isNullOrBlanK(sessionId)) {
            return;
        }
        Team team = TeamDataCache.getInstance().getTeamById(sessionId);
        if (team != null) {
            updateTeamInfo(team);
        } else {
            TeamDataCache.getInstance().fetchTeamById(sessionId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result) {
                    if (success && result != null) {
                        updateTeamInfo(result);
                    } else {
                        Toast.makeText(BaseApplication.getInstance(), getResourceString(R.string.failed_to_obtain_group_information), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void updateTeamInfo(Team result) {
        String announcement = result.getAnnouncement();
        ((FragmentAnnouncements) fragBaseFragments.get(1)).setAnnouncements(announcement);
    }

    private void initView() {

        fragBaseFragments.add(new FragmentPlayerMessage());
        fragBaseFragments.add(new FragmentAnnouncements());
        fragBaseFragments.add(new FragmentPlayerLiveDetails());
        fragBaseFragments.add(new FragmentPlayerMembers());

        FragmentLayoutWithLine fragmentLayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentLayout.setScorllToNext(true);
        fragmentLayout.setScorll(true);
        fragmentLayout.setWhereTab(1);
        fragmentLayout.setTabHeight(4, getResources().getColor(R.color.colorPrimary));
        fragmentLayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xffC4483C);
                if (position == 0) {
                    if (inputPanel != null)
                        inputPanel.visibilityInput();
                    ((FragmentPlayerMessage) fragBaseFragments.get(0)).scrollToBottom();
                } else {
                    if (inputPanel != null)
                        inputPanel.goneInput();
                }
                if (isSubBig) {
                    floatFragment.setSubOpen(true);
                    isSubOpen = true;
                    changeSubSmall();
                    floatFragment.setSubBig(false);
                }
            }
        });
        fragmentLayout.setAdapter(fragBaseFragments, R.layout.tablayout_nevideo_player, 0x0102);
        fragmentLayout.getViewPager().setOffscreenPageLimit(3);
    }

    private void initSessionId() {

        getAnnouncementsData();
        floatFragment.setSessionId(sessionId);
        if (!StringUtils.isNullOrBlanK(sessionId)) {
            TeamMember team = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getInstance().getAccount());
            if (team != null) {
                isMute = team.isMute();
                floatFragment.setMute(isMute);
            }
        }

        inputPanel = new InputPanel(this, this, rootView, true, sessionId);
        inputPanel.setMute(isMute);

        fragment2 = (FragmentPlayerMessage) fragBaseFragments.get(0);

        fragment2.setChatCallBack(new FragmentPlayerMessage.Callback() {
            @Override
            public void back(List<IMMessage> result) {
                TeamMember team = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getInstance().getAccount());
                if (team != null) {
                    isMute = team.isMute();
                    floatFragment.setMute(isMute);
                    inputPanel.setMute(isMute);
                }
                if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || isSubBig) {
//                    Logger.e("弹幕显示" + danmuView.getVisibility());
                    danMuController.addDanmuList(result);
                }
            }

            @Override
            public void shouldCollapseInputPanel() {
                inputPanel.closeEmojiAndInput();
            }

            @Override
            public void updateTeam(Team team) {
                inputPanel.setTeam(team);
                floatFragment.setTeam(team);
            }
        });

        fragment2.setSessionId(id, sessionId);

        inputPanel.setOnInputShowListener(new InputPanel.OnInputShowListener() {
            @Override
            public void OnInputShow() {
                if (isSubBig) {
                    floatFragment.setSubOpen(true);
                    isSubOpen = true;
                    changeSubSmall();
                    floatFragment.setSubBig(false);
                }
                fragment2.scrollToBottom();
            }
        });

    }

    /**
     * 發送消息
     *
     * @param message       聊天內容
     * @param isSendToDanmu 是否将消息展示弹幕
     */
    private void sendMessages(IMMessage message, boolean isSendToDanmu) {
        if (StringUtils.isNullOrBlanK(sessionId)) {
            Toast.makeText(NEVideoPlayerActivity.this, getResourceString(R.string.team_not_exist), Toast.LENGTH_SHORT).show();
            return;
        }
//        // 创建文本消息
//        IMMessage message = MessageBuilder.createTextMessage(
//                sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
//                sessionType, // 聊天类型，单聊或群组
//                comment // 文本内容
//        );
        // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
        NIMClient.getService(MsgService.class).sendMessage(message, true);
        //横屏状态下,将发送的消息展示到弹幕去
        if (isSendToDanmu) {
            danMuController.addDanmu(message, 0);
        }
        fragment2.onMsgSend(message);
    }

    private void initData() {
        if (id != 0) {
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlCourses + id + "/play_info", null,
                    new VolleyListener(NEVideoPlayerActivity.this) {
                        @Override
                        protected void onSuccess(JSONObject response) {
                            RemedialClassDetailBean data = JsonUtils.objectFromJson(response.toString(), RemedialClassDetailBean.class);
                            if (data != null) {
                                ((FragmentPlayerLiveDetails) fragBaseFragments.get(2)).setData(data);
                                if (data.getData() != null) {
                                    camera = data.getData().getCamera();
                                    board = data.getData().getBoard();
                                    canLoop = true;
                                    hd.post(runnable);
                                    sessionId = data.getData().getChat_team_id();
                                    ((FragmentPlayerMembers) fragBaseFragments.get(3)).setData(sessionId);
                                }
                            }
                            initSessionId();
                        }

                        @Override
                        protected void onError(JSONObject response) {
                            initSessionId();
                        }

                        @Override
                        protected void onTokenOut() {
                            initSessionId();
                            tokenOut();
                        }
                    }, new VolleyErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    super.onErrorResponse(volleyError);
                    initSessionId();
                }
            });
            addToRequestQueue(request);
        } else {
            initSessionId();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canLoop) {
            hd.removeCallbacks(runnable);
            hd.post(runnable);
        }
        video1.start();
        video2.start();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (video1 != null && video1.isPlaying()) {
                    floatFragment.setPlaying(true);
                }
            }
        }, 300);
        if (!StringUtils.isNullOrBlanK(sessionId)) {
            NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
        } else {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        }
        MobclickAgent.onResume(this);
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int screenWidth = ScreenUtils.getScreenWidth(NEVideoPlayerActivity.this);
        int screenHeight = ScreenUtils.getScreenHeight(NEVideoPlayerActivity.this);

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) { // 横屏
            floatFragment.setPortrait(false);

            inputPanel.clearInputValue();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            ViewGroup.LayoutParams param = mainVideo.getLayoutParams();
            param.width = ViewGroup.LayoutParams.MATCH_PARENT;
            param.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mainVideo.setLayoutParams(param);
            mainView.setLayoutParams(param);
            if (isMain) {
                whole.removeView(danmuView);
                mainVideo.addView(danmuView, 1);
            } else {
                video2.setSelfSize(screenWidth, screenHeight);
            }

            danmuView.setLayoutParams(param);
            //横屏时会切换为小窗口,切换时已经对弹幕做了改变
//            if (danmuView.getVisibility() == View.GONE) {
//                danmuView.setVisibility(View.VISIBLE);
//            }
            if (isSubBig) {//  横屏时会切换为小窗口,切换时已经对弹幕做了改变
                floatFragment.setSubOpen(true);
                isSubOpen = true;
                changeSubSmall();
                floatFragment.setSubBig(false);
            } else {//  本来就小窗口时,弹幕开启
                danmuView.setVisibility(View.VISIBLE);
            }
            //横屏时打开弹幕
            Logger.e("弹幕状态" + danMuController.getStatus().toString());
            if (danMuController.getStatus() == Status.HIDE) {
                showDanmaku();
                floatFragment.setDanmuOn(true);
            }
        } else {
            floatFragment.setPortrait(true);
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
            if (isMain) {
                RelativeLayout.LayoutParams danmuParam = null;
                mainVideo.removeView(danmuView);
                whole.addView(danmuView);
                danmuParam = new RelativeLayout.LayoutParams(param.width, param.height);
                danmuParam.addRule(RelativeLayout.BELOW, R.id.main_video);
                danmuView.setLayoutParams(danmuParam);
            } else {
                video2.setSelfSize(mainView.getLayoutParams().width, mainView.getLayoutParams().height);
            }
            danmuView.setVisibility(isSubBig ? View.VISIBLE : View.GONE);
        }

        //如果悬浮窗口在屏幕外,切换时移动到屏幕内
        float resultX = floatingWindow.getX();
        float resultY = floatingWindow.getY();
        if (resultX < 0) {
            resultX = 0;
        } else if (resultX >= screenWidth - floatingWindow.getWidth()) {
            resultX = screenWidth - floatingWindow.getWidth();
        }

        if (resultY < 0) {
            resultY = 0;
        } else if (resultY >= screenHeight - floatingWindow.getHeight()) {
            resultY = screenHeight - floatingWindow.getHeight();
        }
        floatingWindow.setX(resultX);
        floatingWindow.setY(resultY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputPanel.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        hd.removeCallbacks(runnable);//停止查询播放状态
        if (video1 != null)
            video1.pause();
        if (video2 != null)
            video2.pause();
        if (floatFragment != null)
            floatFragment.setPlaying(false);
        if (inputPanel != null)
            inputPanel.onPause();
        super.onPause();
        MobclickAgent.onPause(this);
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
    }


    @Override
    protected void onDestroy() {
//        Logger.e("退出轮询");
        if (video1 != null)
            video1.release_resource();
        if (video2 != null)
            video2.release_resource();
        video1 = null;
        video2 = null;

        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (danMuController != null)
            danMuController.destroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            Logger.e("orta 返回竖屏");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            if (floatFragment != null) {
//                floatFragment.setPortrait(true);
//            }
            return;
        }
        if (inputPanel != null && inputPanel.isEmojiShow()) {
            inputPanel.closeEmojiAndInput();
            return;
        }
        super.onBackPressed();
    }

    /**
     * 设置直播状态 {@see <a herf="https://github.com/chuanjiabao1981/qadoc/blob/master/live_status.md">直播状态</a> }
     *
     * @param videoState
     */
    private void setVideoState(VideoState videoState) {
//        Logger.e("videoState" + videoState.toString());
//        if (videoState == VideoState.INIT) {//初始化状态下查询状态
//            hd.removeCallbacks(runnable);
//            if (this.videoState == videoState) {
//                return;
//            }
//            playingReQuery = 0;//异常退出重新查询用
//            this.videoState = videoState;
//            queryVideoState();
//        } else
        if (videoState == VideoState.UNPLAY) {//未直播状态下 开始轮询
            this.videoState = videoState;
//            playingReQuery = 0;//异常退出重新查询用
//            hd.removeCallbacks(runnable);
//            hd.postDelayed(runnable, 30000);
            if (videoNoData1.getVisibility() == View.GONE) {
                videoNoData1.setVisibility(View.VISIBLE);
                if (buffering1.getVisibility() == View.VISIBLE) {
                    buffering1.setVisibility(View.GONE);
                }
            }
            if (videoNoData2.getVisibility() == View.GONE) {
                videoNoData2.setImageResource(R.mipmap.video_no_data);
                videoNoData2.setVisibility(View.VISIBLE);
                if (buffering2.getVisibility() == View.VISIBLE) {
                    buffering2.setVisibility(View.GONE);
                }
            }
        } else if (videoState == VideoState.PLAYING) {//直播状态下 停止轮询等待完成
            this.videoState = videoState;
//            if (playingReQuery < 1) {
////                Logger.e("重新查询");
//                hd.postDelayed(runnable, 15000);
//            } else {
////                Logger.e("不再查询");
//                hd.removeCallbacks(runnable);
//                playingReQuery = 0;
//            }
//            playingReQuery++;
            if (NetUtils.isConnected(NEVideoPlayerActivity.this)) {
                if (NetUtils.isMobile(NEVideoPlayerActivity.this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NEVideoPlayerActivity.this);
                    final AlertDialog alertDialog = builder.create();
                    View view = View.inflate(NEVideoPlayerActivity.this, R.layout.dialog_cancel_or_confirm, null);
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
                            refreshState();
                        }
                    });
                    alertDialog.show();
                    alertDialog.setContentView(view);
                } else if (NetUtils.isWifi(NEVideoPlayerActivity.this)) {
                    refreshState();
                }
            } else {
                Toast.makeText(NEVideoPlayerActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
            }

        } else if (videoState == VideoState.CLOSED) {//关闭状态   摄像头关闭
            this.videoState = videoState;
            refreshState();
        }
    }

    private void queryVideoState() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlCourses + id + "/status",
                null, new VolleyListener(NEVideoPlayerActivity.this) {
            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
//              JSONObject data = response.getJSONObject("data");
                LiveStatusBean data = JsonUtils.objectFromJson(response.toString(), LiveStatusBean.class);
                if (data != null && data.getData() != null && data.getData().getLive_info() != null) {
                    int board = data.getData().getLive_info().getBoard();
                    int camera = data.getData().getLive_info().getCamera();

                    if (camera == 0 && board == 0) {
                        setVideoState(VideoState.UNPLAY);
                        floatFragment.setNameAndCount("", 1);
                    } else if (camera == 2 && board == 1) {
                        setVideoState(VideoState.CLOSED);
//                        ((FragmentPlayerMembers) fragBaseFragments.get(3)).setOnlineInfo(data.getData().getOnline_users());
                        floatFragment.setNameAndCount(data.getData().getLive_info().getName(), data.getData().getOnline_users().size());
                    } else if (camera == 1 && board == 1) {
                        setVideoState(VideoState.PLAYING);
//                        ((FragmentPlayerMembers) fragBaseFragments.get(3)).setOnlineInfo(data.getData().getOnline_users());
                        floatFragment.setNameAndCount(data.getData().getLive_info().getName(), data.getData().getOnline_users().size());
                    }

                }
            }

            @Override
            protected void onError(JSONObject response) {
            }
        }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }


    @Subscribe
    public void onEvent(BusEvent event) {
        if (event == BusEvent.ANNOUNCEMENT) {
            if (StringUtils.isNullOrBlanK(sessionId)) return;
            getAnnouncementsData();
        }
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
        if (this.videoState == VideoState.PLAYING || this.videoState == VideoState.CLOSED) {
            if (video1 != null) {
                if (video1.isPlaying()) {
                    video1.seekTo(video1.getCurrentPosition());
                } else {
                    video1.release_resource();
                    if (StringUtils.isNullOrBlanK(board)) return;
                    video1.setVideoPath(board);
                    video1.start();
                }
            }
            if (video2 != null) {
                if (video2.isPlaying()) {
                    video2.seekTo(video2.getCurrentPosition());
                } else {
                    video2.release_resource();
                    if (StringUtils.isNullOrBlanK(camera)) return;
                    video2.setVideoPath(camera);
                    video2.start();
                }
            }
        }
    }

    @Override
    public void zoom() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public boolean isPortrait() {
        return getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    @Override
    public void changeSubSmall() {
        isSubBig = false;
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            danmuView.setVisibility(View.GONE);
        } else {
            danmuView.setVisibility(View.VISIBLE);
        }
        if (isMain) {
            subVideo.removeView(window2);
            video2.setZOrderOnTop(true);
            floatingWindow.addView(window2);
            video2.setSelfSize(floatingWindow.getLayoutParams().width, floatingWindow.getLayoutParams().height);
        } else {
            subVideo.removeView(window1);
            video1.setZOrderOnTop(true);
            floatingWindow.addView(window1);
        }
        if (isSubOpen) {
            floatingWindow.setVisibility(View.VISIBLE);
        } else {
            floatingWindow.setVisibility(View.GONE);
        }
        subVideo.setVisibility(View.GONE);
    }

    @Override
    public void changeSubBig() {
        inputPanel.closeEmojiAndInput();
        isSubBig = true;
        if (isMain) {
            floatingWindow.removeView(window2);
            video2.setZOrderOnTop(false);
            subVideo.addView(window2);
            video2.setSelfSize(subVideo.getLayoutParams().width, subVideo.getLayoutParams().height);
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
        isMain = true;
        mainView.removeView(window2);
        floatingWindow.removeView(window1);
        video1.setZOrderOnTop(false);
        mainView.addView(window1);
        video2.setZOrderOnTop(true);
        floatingWindow.addView(window2);
        video2.setSelfSize(floatingWindow.getLayoutParams().width, floatingWindow.getLayoutParams().height);

        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            mainVideo.removeView(danmuView);
            whole.addView(danmuView);
            RelativeLayout.LayoutParams danmuParam = new RelativeLayout.LayoutParams(-1, ScreenUtils.getScreenWidth(NEVideoPlayerActivity.this) * 9 / 16);
            danmuParam.addRule(RelativeLayout.BELOW, R.id.main_video);
            danmuView.setLayoutParams(danmuParam);
            danmuView.setVisibility(View.GONE);
        } else {
            if (danmuView.getVisibility() == View.GONE) {
                danmuView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void changeMain2Sub() {
        isMain = false;
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
        isMain = false;
        mainView.removeView(window1);
        floatingWindow.removeView(window2);
        video2.setZOrderOnTop(false);

        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            whole.removeView(danmuView);
            mainVideo.addView(danmuView, 1);
        }

        mainView.addView(window2);
        video2.setSelfSize(mainView.getLayoutParams().width, mainView.getLayoutParams().height);
        video1.setZOrderOnTop(true);
        floatingWindow.addView(window1);
    }

    @Override
    public void changeSub2Main() {
        isMain = true;
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
                if (isMain) {
                    video2.setVisibility(View.VISIBLE);
                    window2.setVisibility(View.VISIBLE);
                } else {
                    video1.setVisibility(View.VISIBLE);
                    window1.setVisibility(View.VISIBLE);
                }
                floatingWindow.setVisibility(View.VISIBLE);
            }
        } else {
            if (isSubBig) {
                danmuView.setVisibility(View.GONE);
                subVideo.setVisibility(View.GONE);
            } else {
                if (isMain) {
                    video2.setVisibility(View.GONE);
                    window2.setVisibility(View.GONE);
                } else {
                    video1.setVisibility(View.GONE);
                    window1.setVisibility(View.GONE);
                }
                floatingWindow.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void sendMessage(IMMessage message) {
//        Logger.e("message" + message);
        sendMessages(message, message.getMsgType() == MsgTypeEnum.text && getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT || isSubBig);
    }

    @Override
    public void play() {
        if (NetUtils.isConnected(NEVideoPlayerActivity.this)) {
            if (NetUtils.isMobile(NEVideoPlayerActivity.this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NEVideoPlayerActivity.this);
                final AlertDialog alertDialog = builder.create();
                View view = View.inflate(NEVideoPlayerActivity.this, R.layout.dialog_cancel_or_confirm, null);
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
                        video1.start();
                        video2.start();
                    }
                });
                alertDialog.show();
                alertDialog.setContentView(view);
            } else if (NetUtils.isWifi(NEVideoPlayerActivity.this)) {
                video1.start();
                video2.start();
            }
        } else {
            Toast.makeText(NEVideoPlayerActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean isPlaying() {
        return (video1 != null && video1.isPlaying()) || (video2 != null && video2.isPlaying());
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

    /**
     * InputPanel  InputPanelListener返回的发送信息
     *
     * @param message
     */
    @Override
    public void ChatMessage(IMMessage message) {
        sendMessages(message, message.getMsgType() == MsgTypeEnum.text && isSubBig);
    }

    @Override
    public boolean isShowTime() {
        return false;
    }
}
