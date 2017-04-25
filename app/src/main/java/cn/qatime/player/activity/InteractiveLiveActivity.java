package cn.qatime.player.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.constant.AVChatAudioEffectMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatResCode;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.constant.AVChatVideoScalingType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatOptionalConfig;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoRender;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.rts.RTSCallback;
import com.netease.nimlib.sdk.rts.RTSChannelStateObserver;
import com.netease.nimlib.sdk.rts.RTSManager2;
import com.netease.nimlib.sdk.rts.constant.RTSTunnelType;
import com.netease.nimlib.sdk.rts.model.RTSData;
import com.netease.nimlib.sdk.rts.model.RTSTunData;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nrtc.sdk.NRtcParameters;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.InputPanel;
import cn.qatime.player.fragment.FragmentInteractiveAnnouncements;
import cn.qatime.player.fragment.FragmentInteractiveBoard;
import cn.qatime.player.fragment.FragmentInteractiveDetails;
import cn.qatime.player.fragment.FragmentInteractiveMembers;
import cn.qatime.player.fragment.FragmentInteractiveMessage;
import cn.qatime.player.im.cache.ChatRoomMemberCache;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.doodle.MsgHelper;
import cn.qatime.player.im.doodle.Transaction;
import cn.qatime.player.im.doodle.TransactionCenter;
import cn.qatime.player.im.model.MeetingOptCommand;
import cn.qatime.player.im.view.DialogMaker;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.MPermission;
import cn.qatime.player.utils.MPermissionUtil;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.annotation.OnMPermissionDenied;
import cn.qatime.player.utils.annotation.OnMPermissionGranted;
import cn.qatime.player.utils.annotation.OnMPermissionNeverAskAgain;
import cn.qatime.player.view.VideoLayout;
import libraryextra.bean.InteractCourseDetailBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * @author lungtify
 * @Time 2017/3/28 10:22
 * @Describe 互动直播
 */

public class InteractiveLiveActivity extends BaseActivity implements View.OnClickListener, AVChatStateObserver, InputPanel.InputPanelListener {
    private FragmentLayoutWithLine fragmentlayout;
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4, R.id.tab_text5};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private AbortableFuture<EnterChatRoomResultData> enterRequest;
    private final int LIVE_PERMISSION_REQUEST_CODE = 100;
    private List<String> userJoinedList = new ArrayList<>(); // 已经onUserJoined的用户
    private AVChatVideoRender masterRender;

    private RelativeLayout viewLayout;
    private FrameLayout masterVideoLayout;
    private ImageView fullScreenImage;
    private RelativeLayout backLayout;
    private TextView roomIdText;

    private ImageView videoPermission;
    private ImageView audioPermission;
    //    private TextView onlineStatus;
    private FrameLayout fullScreenLayout;
    private FrameLayout fullScreenView;
    private ImageView cancelFullScreenImage;

    private VideoLayout videoLayout;
    /**
     * 聊天室基本信息
     */
    private String roomId;
    private String sessionId;
    //    private ChatRoomInfo roomInfo;
    private FragmentInteractiveBoard rtsFragment;
    private Handler hd = new Handler();
    private Runnable hideBackLayout = new Runnable() {
        @Override
        public void run() {
            backLayout.setVisibility(View.GONE);
        }
    };
    private boolean isMute = false;
    private View rootView;
    private InputPanel inputPanel;
    private int id;
    private FragmentInteractiveMessage messageFragment;
    private String sessionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.activity_interactive_live, null);
        setContentView(rootView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        sessionId = getIntent().getStringExtra("teamId");
        sessionId = "28054274";
        initView();


        // 注册监听
//        registerObservers(true);
        updateControlUI();

        requestLivePermission();
        id = getIntent().getIntExtra("id", 0);
        id = 2;
//        initData();
       hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                roomId = "12345678";
                enterRoom();
            }
        }, 5000);
    }

    private void initData() {
        if (id != 0) {
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlInteractCourses + id, null,
                    new VolleyListener(InteractiveLiveActivity.this) {
                        @Override
                        protected void onSuccess(JSONObject response) {
                            InteractCourseDetailBean data = JsonUtils.objectFromJson(response.toString(), InteractCourseDetailBean.class);
                            if (data != null && data.getData() != null) {
                                ((FragmentInteractiveDetails) fragBaseFragments.get(3)).setData(data.getData());
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
        viewLayout = (RelativeLayout) findViewById(R.id.view_layout);
        masterVideoLayout = (FrameLayout) findViewById(R.id.master_video_layout);
        fullScreenImage = (ImageView) findViewById(R.id.full_screen_image);
        backLayout = (RelativeLayout) findViewById(R.id.back_layout);
        roomIdText = (TextView) findViewById(R.id.room_id);
        videoPermission = (ImageView) findViewById(R.id.video_permission);
        audioPermission = (ImageView) findViewById(R.id.audio_permission);
//        onlineStatus = (TextView) findViewById(R.id.online_status);
        fullScreenLayout = (FrameLayout) findViewById(R.id.full_screen_layout);
        fullScreenView = (FrameLayout) findViewById(R.id.full_screen_view);
        cancelFullScreenImage = (ImageView) findViewById(R.id.cancel_full_screen_image);
        videoLayout = (VideoLayout) findViewById(R.id.video_layout);
        if (!StringUtils.isNullOrBlanK(sessionId)) {
            TeamMember team = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount());
            if (team != null) {
                isMute = team.isMute();
//                floatFragment.setMute(isMute);
            }
        }
        inputPanel = new InputPanel(this, this, rootView, false, sessionId);
        inputPanel.setMute(isMute);

        fragBaseFragments.add(new FragmentInteractiveBoard());
        fragBaseFragments.add(new FragmentInteractiveMessage());
        fragBaseFragments.add(new FragmentInteractiveAnnouncements());
        fragBaseFragments.add(new FragmentInteractiveDetails());
        fragBaseFragments.add(new FragmentInteractiveMembers());

        fragmentlayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(false);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4, 0xffff5842);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
                if (position == 1) {
                    inputPanel.visibilityInput();
                } else {
                    inputPanel.goneInput();
                }
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_interactive_live, 0x0912);
        fragmentlayout.getViewPager().setOffscreenPageLimit(4);

        rtsFragment = (FragmentInteractiveBoard) fragBaseFragments.get(0);
        messageFragment = (FragmentInteractiveMessage) fragBaseFragments.get(1);
        viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backLayout.getVisibility() == View.GONE) {
                    backLayout.setVisibility(View.VISIBLE);
                    hd.postDelayed(hideBackLayout, 3000);
                } else {
                    backLayout.setVisibility(View.GONE);
                    hd.removeCallbacks(hideBackLayout);
                }
            }
        });
        hd.postDelayed(hideBackLayout, 3000);

        videoPermission.setOnClickListener(this);
        audioPermission.setOnClickListener(this);

        messageFragment.setChatCallBack(new FragmentInteractiveMessage.Callback() {
            @Override
            public void back(List<IMMessage> result) {
                TeamMember team = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount());
                if (team != null) {
                    isMute = team.isMute();
                    inputPanel.setMute(isMute);
                }
            }

            @Override
            public void shouldCollapseInputPanel() {
                inputPanel.closeEmojiAndInput();
            }

            @Override
            public void updateTeam(Team team) {
                inputPanel.setTeam(team);
//                floatFragment.setTeam(team);
            }
        });
//        messageFragment.setSessionId(sessionId);
//        messageFragment.requestTeamInfo();

        inputPanel.setOnInputShowListener(new InputPanel.OnInputShowListener() {
            @Override
            public void OnInputShow() {
                messageFragment.scrollToBottom();
            }
        });
    }

    private void enterRoom() {
//        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                if (enterRequest != null) {
//                    enterRequest.abort();
//                    onLoginDone();
//                    finish();
//                }
//            }
//        }).setCanceledOnTouchOutside(false);
//        EnterChatRoomData data = new EnterChatRoomData(roomId);
//        enterRequest = NIMClient.getService(ChatRoomService.class).enterChatRoom(data);
//        enterRequest.setCallback(new RequestCallback<EnterChatRoomResultData>() {
//            @Override
//            public void onSuccess(EnterChatRoomResultData result) {
//                onLoginDone();
//                roomInfo = result.getRoomInfo();
//                ChatRoomMember member = result.getMember();
//                member.setRoomId(roomInfo.getRoomId());
//                ChatRoomMemberCache.getInstance().saveMyMember(member);
//                if (roomInfo.getExtension() != null) {
//                    shareUrl = (String) roomInfo.getExtension().get(KEY_SHARE_URL);
//                }
        initLiveVideo();
        rtsFragment.initRTSView(roomId);
        joinRTSSession();
        registerRTSObservers(roomId, true);
//            }
//
//            @Override
//            public void onFailed(int code) {
//                onLoginDone();
//                if (code == ResponseCode.RES_CHATROOM_BLACKLIST) {
//                    Toast.makeText(InteractiveLiveActivity.this, "你已被拉入黑名单，不能再进入", Toast.LENGTH_SHORT).show();
//                } else if (code == ResponseCode.RES_ENONEXIST) {
//                    Toast.makeText(InteractiveLiveActivity.this, "该聊天室不存在", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(InteractiveLiveActivity.this, "enter chat room failed, code=" + code, Toast.LENGTH_SHORT).show();
//                }
//                finish();
//            }
//
//            @Override
//            public void onException(Throwable exception) {
//                onLoginDone();
//                Toast.makeText(InteractiveLiveActivity.this, "enter chat room exception, e=" + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
    }

    // 加入多人白板session
    private void joinRTSSession() {
        RTSManager2.getInstance().joinSession(roomId, true, new RTSCallback<RTSData>() {
            @Override
            public void onSuccess(RTSData rtsData) {
                Logger.e("rts extra:" + rtsData.getExtra());
                // 主播的白板默认为开启状态
                ChatRoomMemberCache.getInstance().setRTSOpen(true);
                updateRTSFragment();
            }

            @Override
            public void onFailed(int i) {
                Logger.e("join rts session failed, code:" + i);
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    private void updateRTSFragment() {
        if (rtsFragment != null) {
            rtsFragment.initView();
        } else {
            hd.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateRTSFragment();
                }
            }, 50);
        }
    }

    private void initLiveVideo() {
        AVChatOptionalConfig avChatOptionalParam = new AVChatOptionalConfig();
        avChatOptionalParam.setLivePIPMode(2);
        avChatOptionalParam.setAudioEffectNSMode(AVChatAudioEffectMode.PLATFORM_BUILTIN);
        avChatOptionalParam.enableAudienceRole(true);
        AVChatManager.getInstance().joinRoom(roomId, AVChatType.VIDEO, avChatOptionalParam, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData avChatData) {
                Logger.e("join channel success, extra:" + avChatData.getExtra());
                // 设置音量信号监听, 通过AVChatStateObserver的onReportSpeaker回调音量大小
                AVChatParameters avChatParameters = new AVChatParameters();
                avChatParameters.setBoolean(NRtcParameters.KEY_AUDIO_REPORT_SPEAKER, true);
                AVChatManager.getInstance().setParameters(avChatParameters);
            }

            @Override
            public void onFailed(int i) {
                Logger.e("join channel failed, code:" + i);
                Toast.makeText(InteractiveLiveActivity.this, "join channel failed, code:" + i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });

        updateControlUI();
//        HandsUp();
        updateVideoAudioUI();
    }

    private void updateVideoAudioUI() {
        videoPermission.setBackgroundResource(!AVChatManager.getInstance().isLocalVideoMuted()
                ? R.mipmap.video_on : R.mipmap.video_off);
        audioPermission.setBackgroundResource(!AVChatManager.getInstance().isLocalAudioMuted()
                ? R.mipmap.audio_on : R.mipmap.audio_off);
    }

    private void updateControlUI() {
        if (ChatRoomMemberCache.getInstance().hasPermission(roomId, BaseApplication.getAccount())) {
            videoPermission.setVisibility(View.VISIBLE);
            audioPermission.setVisibility(View.VISIBLE);
        } else {
            videoPermission.setVisibility(View.GONE);
            audioPermission.setVisibility(View.GONE);
        }
    }

    /**
     * 没有互动的用户交互，只有内部主动申请互动
     */
//    private void HandsUp() {
//        MsgHelper.getInstance().sendP2PCustomNotification(roomId, MeetingOptCommand.SPEAK_REQUEST.getValue(), roomInfo.getCreator(), null);
//        ChatRoomMemberCache.getInstance().saveMyHandsUpDown(roomId, true);
//    }
    private void onLoginDone() {
        enterRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    private void registerObservers(boolean register) {
//        NIMClient.getService(ChatRoomServiceObserver.class).observeOnlineStatus(onlineStatus, register);
//        NIMClient.getService(ChatRoomServiceObserver.class).observeKickOutEvent(kickOutObserver, register);
//        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);

        AVChatManager.getInstance().observeAVChatState(this, register);
        ChatRoomMemberCache.getInstance().registerMeetingControlObserver(meetingControlObserver, register);
        ChatRoomMemberCache.getInstance().registerRoomMemberChangedObserver(roomMemberChangedObserver, register);
        ChatRoomMemberCache.getInstance().registerRoomInfoChangedObserver(roomInfoChangedObserver, register);
    }

    ChatRoomMemberCache.MeetingControlObserver meetingControlObserver = new ChatRoomMemberCache.MeetingControlObserver() {
        @Override
        public void onAccept(String roomID) {
            if (checkRoom(roomID)) {
                return;
            }
            chooseSpeechType();
        }

        @Override
        public void onReject(String roomID) {

        }

        @Override
        public void onPermissionResponse(String roomId, List<String> accounts) {
            if (checkRoom(roomId)) {
                return;
            }
            for (String a : accounts) {
                Logger.e("on permission response, account:" + a);
                ChatRoomMemberCache.getInstance().savePermissionMemberbyId(roomId, a);
                onVideoOn(a);
            }
        }

        @Override
        public void onSendMyPermission(String roomID, String toAccount) {
            if (checkRoom(roomID)) {
                return;
            }

            if (ChatRoomMemberCache.getInstance().hasPermission(roomID, BaseApplication.getAccount())) {
                List<String> accounts = new ArrayList<>(1);
                accounts.add(BaseApplication.getAccount());
                MsgHelper.getInstance().sendP2PCustomNotification(roomID, MeetingOptCommand.STATUS_RESPONSE.getValue(), toAccount, accounts);
            }
        }

        @Override
        public void onSaveMemberPermission(String roomID, List<String> accounts) {
            if (checkRoom(roomID)) {
                return;
            }
//            saveMemberPermission(accounts);
        }

        @Override
        public void onHandsUp(String roomID, String account) {
            if (checkRoom(roomID)) {
                return;
            }
            ChatRoomMemberCache.getInstance().saveMemberHandsUpDown(roomId, account, true);
//            onTabChange(true);
        }

        @Override
        public void onHandsDown(String roomID, String account) {
            if (checkRoom(roomID)) {
                return;
            }
            ChatRoomMemberCache.getInstance().saveMemberHandsUpDown(roomID, account, false);
//            onTabChange(false);
            if (ChatRoomMemberCache.getInstance().hasPermission(roomID, account)) {
//                removeMemberPermission(account);
            }
        }

        @Override
        public void onStatusNotify(String roomID, List<String> accounts) {
            if (checkRoom(roomID)) {
                return;
            }
//            onPermissionChange(accounts);
            updateControlUI();
        }
    };


    // 将有权限的成员添加到画布
    public void onVideoOn(String account) {
        Map<Integer, String> imageMap = ChatRoomMemberCache.getInstance().getImageMap(roomId);
        if (imageMap == null) {
            imageMap = new HashMap<>();
        }

        showView(imageMap, account);

        ChatRoomMemberCache.getInstance().saveImageMap(roomId, imageMap);
    }

    // 显示成员图像
    private void showView(Map<Integer, String> imageMap, String a) {
        if (userJoinedList != null && userJoinedList.contains(a)
//                && !roomInfo.getCreator().equals(a)
                && !imageMap.containsValue(a) && imageMap.size() < 1) {
            if (!imageMap.containsKey(0)) {
                AVChatVideoRender render = new AVChatVideoRender(InteractiveLiveActivity.this);
                boolean isSetup = false;
                try {
                    isSetup = AVChatManager.getInstance().setupRemoteVideoRender(a, render, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
//                    Logger.e("setup render, creator account:" + roomInfo.getCreator() + ", render account:" + a + ", isSetup:" + isSetup);
                } catch (Exception e) {
                    Logger.e("set up video render error:" + e.getMessage());
                    e.printStackTrace();
                }
                if (isSetup) {
                    imageMap.put(0, a);
                    addIntoPreviewLayout(render, videoLayout);
                }
            }
        }
    }

    // 将被取消权限的成员从画布移除, 并将角色置为初始状态
    public void onVideoOff(String account) {
        Map<Integer, String> imageMap = ChatRoomMemberCache.getInstance().getImageMap(roomId);
        if (imageMap == null) {
            return;
        }
        removeView(imageMap, account);
//        resetRole(account);
    }

    // 移除成员图像
    private void removeView(Map<Integer, String> imageMap, String account) {
        Iterator<Map.Entry<Integer, String>> it = imageMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, String> entry = it.next();
            if (entry.getValue().equals(account)) {
                videoLayout.removeAllViews();
                it.remove();
                break;
            }
        }
    }

    // 添加到成员显示的画布
    private void addIntoPreviewLayout(SurfaceView surfaceView, ViewGroup viewLayout) {
        if (surfaceView == null) {
            return;
        }
        if (surfaceView.getParent() != null)
            ((ViewGroup) surfaceView.getParent()).removeView(surfaceView);
        viewLayout.addView(surfaceView);
        surfaceView.setZOrderMediaOverlay(true);
    }

    // 选择发言方式
    private void chooseSpeechType() {
        if (!ChatRoomMemberCache.getInstance().hasPermission(roomId, BaseApplication.getAccount())) {
            return;
        }
        AVChatManager.getInstance().startLive();
        AVChatManager.getInstance().enableAudienceRole(false);

        AVChatManager.getInstance().muteLocalAudio(false);

        AVChatManager.getInstance().muteLocalVideo(false);

        ChatRoomMemberCache.getInstance().setRTSOpen(true);

//      videoListener.onAcceptConfirm();
        updateControlUI();
        updateVideoAudioUI();
    }

    private boolean checkRoom(String roomID) {
        return TextUtils.isEmpty(roomId) || !roomId.equals(roomID);
    }

    ChatRoomMemberCache.RoomMemberChangedObserver roomMemberChangedObserver = new ChatRoomMemberCache.RoomMemberChangedObserver() {
        @Override
        public void onRoomMemberIn(ChatRoomMember member) {
            onMasterJoin(member.getAccount());

//            if (BaseApplication.getAccount().equals(roomInfo.getCreator())
//                    && !member.getAccount().equals(BaseApplication.getAccount())) {
//                // 主持人点对点通知有权限的成员列表
//                // 主持人自己进来，不需要通知自己
//                MsgHelper.getInstance().sendP2PCustomNotification(roomId, MeetingOptCommand.ALL_STATUS.getValue(),
//                        member.getAccount(), ChatRoomMemberCache.getInstance().getPermissionMems(roomId));
//            }
//
//            if (member.getAccount().equals(roomInfo.getCreator())) {
//                // 主持人重新进来,观众要取消自己的举手状态
//                ChatRoomMemberCache.getInstance().saveMyHandsUpDown(roomId, false);
//            }
//
//            if (member.getAccount().equals(roomInfo.getCreator()) && BaseApplication.getAccount().equals(roomInfo.getCreator())) {
//                // 主持人自己重新进来，清空观众的举手状态
//                ChatRoomMemberCache.getInstance().clearAllHandsUp(roomId);
//                // 重新向所有成员请求权限
////                requestPermissionMembers();
//            }
        }

        @Override
        public void onRoomMemberExit(ChatRoomMember member) {
            // 主持人要清空离开成员的举手
//            if (BaseApplication.getAccount().equals(roomInfo.getCreator())) {
//                ChatRoomMemberCache.getInstance().removeHandsUpMem(roomId, member.getAccount());
//            }
//
//            // 用户离开频道，如果是有权限用户，移除下画布
//            if (member.getAccount().equals(roomInfo.getCreator())) {
//                masterVideoLayout.removeAllViews();
//            } else if (ChatRoomMemberCache.getInstance().hasPermission(roomId, member.getAccount())) {
////                removeMemberPermission(member.getAccount());
//            }
        }
    };

    ChatRoomMemberCache.RoomInfoChangedObserver roomInfoChangedObserver = new ChatRoomMemberCache.RoomInfoChangedObserver() {
        @Override
        public void onRoomInfoUpdate(IMMessage message) {
            ChatRoomNotificationAttachment attachment = (ChatRoomNotificationAttachment) message.getAttachment();
            if (attachment != null && attachment.getExtension() != null) {
                Map<String, Object> ext = attachment.getExtension();
//                switchFullScreen(ext);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        if (!TextUtils.isEmpty(sessionName)) {
            registerRTSObservers(sessionName, false);
        }

        if (roomId != null) {
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            clearChatRoom();
        }
        hd.removeCallbacks(hideBackLayout);
    }

    private void clearChatRoom() {
        AVChatManager.getInstance().leaveRoom(null);
        RTSManager2.getInstance().leaveSession(roomId, null);
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_permission:
                if (ChatRoomMemberCache.getInstance().hasPermission(roomId, BaseApplication.getAccount())) {
                    setVideoState();
                }
                break;
            case R.id.audio_permission:
                if (ChatRoomMemberCache.getInstance().hasPermission(roomId, BaseApplication.getAccount())) {
                    setAudioState();
                }
                break;
        }
    }

    // 设置自己的摄像头是否开启
    private void setVideoState() {
        if (AVChatManager.getInstance().isLocalVideoMuted()) {
            videoPermission.setBackgroundResource(R.mipmap.video_on);
            AVChatManager.getInstance().muteLocalVideo(false);
        } else {
            videoPermission.setBackgroundResource(R.mipmap.video_off);
            AVChatManager.getInstance().muteLocalVideo(true);
        }
    }

    // 设置自己的录音是否开启
    private void setAudioState() {
        if (AVChatManager.getInstance().isLocalAudioMuted()) {
            audioPermission.setBackgroundResource(R.mipmap.audio_on);
            AVChatManager.getInstance().muteLocalAudio(false);
        } else {
            audioPermission.setBackgroundResource(R.mipmap.audio_off);
            AVChatManager.getInstance().muteLocalAudio(true);
        }
    }

    /**************************
     * 音视频权限控制
     ******************************/

    // 权限控制
    private static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO};

    private void requestLivePermission() {
        MPermission.with(InteractiveLiveActivity.this)
                .addRequestCode(LIVE_PERMISSION_REQUEST_CODE)
                .permissions(LIVE_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionGranted() {
        Toast.makeText(InteractiveLiveActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, LIVE_PERMISSIONS);
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法开启直播";
        Toast.makeText(InteractiveLiveActivity.this, tip, Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionNeverAskAgain(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, LIVE_PERMISSIONS);
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, LIVE_PERMISSIONS);
        StringBuilder sb = new StringBuilder();
        sb.append("无法开启直播，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }

        Toast.makeText(InteractiveLiveActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
    }

    /************************** 音视频权限控制 end ******************************/


    /*****************************
     * AVChatStateObserver
     *********************************/

    @Override
    public void onTakeSnapshotResult(String s, boolean b, String s1) {

    }

    @Override
    public void onConnectionTypeChanged(int i) {

    }

    @Override
    public void onAVRecordingCompletion(String s, String s1) {

    }

    @Override
    public void onAudioRecordingCompletion(String s) {

    }

    @Override
    public void onLowStorageSpaceWarning(long l) {

    }

    @Override
    public void onFirstVideoFrameAvailable(String s) {

    }

    @Override
    public void onVideoFpsReported(String s, int i) {

    }

    @Override
    public void onJoinedChannel(int i, String s, String s1) {
        Logger.e("onJoinedChannel, res:" + i);
        if (i != AVChatResCode.JoinChannelCode.OK) {
            Toast.makeText(InteractiveLiveActivity.this, "joined channel:" + i, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLeaveChannel() {
//        userJoinedList.remove(BaseApplication.getAccount());
    }

    @Override
    public void onUserJoined(String s) {
        userJoinedList.add(s);
        onMasterJoin(s);
//        if (ChatRoomMemberCache.getInstance().hasPermission(roomId, s) && !s.equals(roomInfo.getCreator())) {
        onVideoOn(s);
//        }
    }

    @Override
    public void onUserLeave(String s, int i) {
        // 用户离开频道，如果是有权限用户，移除下画布
//        if (ChatRoomMemberCache.getInstance().hasPermission(roomId, s) && !s.equals(roomInfo.getCreator())) {
//            onVideoOff(s);
//        } else if (s.equals(roomInfo.getCreator())) {
//            masterVideoLayout.removeAllViews();
//        }
        ChatRoomMemberCache.getInstance().removePermissionMem(roomId, s);
//        videoListener.onUserLeave(s);
        userJoinedList.remove(s);
    }

    @Override
    public void onProtocolIncompatible(int i) {

    }

    @Override
    public void onDisconnectServer() {

    }

    @Override
    public void onNetworkQuality(String account, int i) {

    }

    @Override
    public void onCallEstablished() {
        userJoinedList.add(BaseApplication.getAccount());
        onMasterJoin(BaseApplication.getAccount());
    }

    @Override
    public void onDeviceEvent(int i, String s) {

    }

    @Override
    public void onFirstVideoFrameRendered(String s) {

    }

    @Override
    public void onVideoFrameResolutionChanged(String s, int i, int i1, int i2) {

    }

    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame avChatVideoFrame) {
        return false;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame avChatAudioFrame) {
        return false;
    }

    @Override
    public void onAudioDeviceChanged(int i) {

    }


    @Override
    public void onReportSpeaker(Map<String, Integer> map, int i) {
//        videoListener.onReportSpeaker(map);
    }

    @Override
    public void onStartLiveResult(int i) {
    }

    @Override
    public void onStopLiveResult(int i) {
    }

    @Override
    public void onAudioMixingEvent(int i) {

    }

    /****************************
     * AVChatStateObserver end
     ****************************/
    // 主持人进入频道
    private void onMasterJoin(String s) {
//        if (userJoinedList != null && userJoinedList.contains(s) && s.equals(roomInfo.getCreator())) {
//            if (masterRender == null) {
//                masterRender = new AVChatVideoRender(InteractiveLiveActivity.this);
//            }
//            boolean isSetup = setupMasterRender(s, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
//            if (isSetup && masterRender != null) {
//                addIntoMasterPreviewLayout(masterRender);
//                ChatRoomMemberCache.getInstance().savePermissionMemberbyId(roomId, roomInfo.getCreator());
////                updateDeskShareUI();
//            }
//        }
    }

    // 将主持人添加到主持人画布
    private void addIntoMasterPreviewLayout(SurfaceView surfaceView) {
        if (surfaceView.getParent() != null)
            ((ViewGroup) surfaceView.getParent()).removeView(surfaceView);
        masterVideoLayout.addView(surfaceView);
        surfaceView.setZOrderMediaOverlay(true);
    }

    private boolean setupMasterRender(String s, int mode) {
        boolean isSetup = false;
        try {
            isSetup = AVChatManager.getInstance().setupRemoteVideoRender(s, masterRender, false, mode);
        } catch (Exception e) {
            Logger.e("set up video render error:" + e.getMessage());
            e.printStackTrace();
        }
        return isSetup;
    }

    private void updateDeskShareUI() {
//        Map<String, Object> ext = roomInfo.getExtension();
//        if (ext != null && ext.containsKey(MeetingConstant.FULL_SCREEN_TYPE)) {
//            int fullScreenType = (int) ext.get(MeetingConstant.FULL_SCREEN_TYPE);
//            if (fullScreenType == FullScreenType.CLOSE.getValue()) {
//                fullScreenImage.setVisibility(View.GONE);
//            } else if (fullScreenType == FullScreenType.OPEN.getValue()) {
//                fullScreenImage.setVisibility(View.VISIBLE);
//            }
//        }
    }

    @Override
    public void ChatMessage(IMMessage message) {
        if (StringUtils.isNullOrBlanK(sessionId)) {
            Toast.makeText(InteractiveLiveActivity.this, getResourceString(R.string.team_not_exist), Toast.LENGTH_SHORT).show();
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
        messageFragment.onMsgSend(message);
    }

    private void registerRTSObservers(String sessionName, boolean register) {
        this.sessionName = sessionName;
        RTSManager2.getInstance().observeChannelState(sessionName, channelStateObserver, register);
        RTSManager2.getInstance().observeReceiveData(sessionName, receiveDataObserver, register);
    }

    /**
     * 监听当前会话的状态
     */
    private RTSChannelStateObserver channelStateObserver = new RTSChannelStateObserver() {

        @Override
        public void onConnectResult(String localSessionId, RTSTunnelType tunType, long channelId, int code, String recordFile) {
            Toast.makeText(InteractiveLiveActivity.this, "onConnectResult, tunType=" + tunType.toString() +
                    ", channelId=" + channelId + ", code=" + code, Toast.LENGTH_SHORT).show();
            if (code != 200) {
                RTSManager2.getInstance().leaveSession(sessionId, null);
                return;
            }

            List<Transaction> cache = new ArrayList<>(1);
            // 非主播进入房间，发送同步请求，请求主播向他同步之前的白板笔记
            Toast.makeText(InteractiveLiveActivity.this, "send sync request", Toast.LENGTH_SHORT).show();
            TransactionCenter.getInstance().onNetWorkChange(sessionId, false);
            cache.add(new Transaction().makeSyncRequestTransaction());
            TransactionCenter.getInstance().sendToRemote(sessionId, null, cache);
        }

        @Override
        public void onChannelEstablished(String sessionId, RTSTunnelType tunType) {
            Toast.makeText(InteractiveLiveActivity.this, "onCallEstablished,tunType=" + tunType.toString(), Toast.LENGTH_SHORT).show();

            if (tunType == RTSTunnelType.AUDIO) {
                RTSManager2.getInstance().setSpeaker(sessionId, true); // 默认开启扬声器
            }
        }

        @Override
        public void onUserJoin(String sessionId, RTSTunnelType tunType, String account) {
            Logger.e("On User Join, account:" + account);
        }

        @Override
        public void onUserLeave(String sessionId, RTSTunnelType tunType, String account, int event) {
            Logger.e("On User Leave, account:" + account);
        }

        @Override
        public void onDisconnectServer(String sessionId, RTSTunnelType tunType) {
            Toast.makeText(InteractiveLiveActivity.this, "onDisconnectServer, tunType=" + tunType.toString(), Toast
                    .LENGTH_SHORT).show();
            if (tunType == RTSTunnelType.DATA) {
                // 如果数据通道断了，那么关闭会话
                Toast.makeText(InteractiveLiveActivity.this, "TCP通道断开，自动结束会话", Toast.LENGTH_SHORT).show();
                RTSManager2.getInstance().leaveSession(sessionId, null);
            } else if (tunType == RTSTunnelType.AUDIO) {
            }
        }

        @Override
        public void onError(String sessionId, RTSTunnelType tunType, int code) {
            Toast.makeText(InteractiveLiveActivity.this, "onError, tunType=" + tunType.toString() + ", error=" + code,
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNetworkStatusChange(String sessionId, RTSTunnelType tunType, int value) {
            // 网络信号强弱
            Logger.e("network status:" + value);
        }
    };

    /**
     * 监听收到对方发送的通道数据
     */
    private Observer<RTSTunData> receiveDataObserver = new Observer<RTSTunData>() {
        @Override
        public void onEvent(RTSTunData rtsTunData) {
            Logger.e("receive data");
            String data = "[parse bytes error]";
            try {
                data = new String(rtsTunData.getData(), 0, rtsTunData.getLength(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            TransactionCenter.getInstance().onReceive(roomId, rtsTunData.getAccount(), data);
        }
    };
}
