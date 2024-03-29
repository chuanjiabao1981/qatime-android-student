package cn.qatime.player.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
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
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
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
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.BusEvent;
import cn.qatime.player.bean.InputPanel;
import cn.qatime.player.bean.InteractiveLiveStatusBean;
import cn.qatime.player.fragment.FragmentAnnouncements;
import cn.qatime.player.fragment.FragmentInteractiveBoard;
import cn.qatime.player.fragment.FragmentInteractiveDetails;
import cn.qatime.player.fragment.FragmentInteractiveMembers;
import cn.qatime.player.fragment.FragmentInteractiveMessage;
import cn.qatime.player.im.SimpleCallback;
import cn.qatime.player.im.cache.ChatRoomMemberCache;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.doodle.Transaction;
import cn.qatime.player.im.doodle.TransactionCenter;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.LogCatHelper;
import cn.qatime.player.utils.MPermission;
import cn.qatime.player.utils.MPermissionUtil;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.annotation.OnMPermissionDenied;
import cn.qatime.player.utils.annotation.OnMPermissionGranted;
import cn.qatime.player.utils.annotation.OnMPermissionNeverAskAgain;
import cn.qatime.player.view.VideoFrameLayout;
import libraryextra.bean.InteractCourseDetailBean;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.NetUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * @author lungtify
 * @Time 2017/3/28 10:22
 * @Describe 互动直播
 */

public class InteractiveLiveActivity extends BaseActivity implements View.OnClickListener, AVChatStateObserver, InputPanel.InputPanelListener, FragmentInteractiveBoard.SwitchListener {
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4, R.id.tab_text5};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private final int LIVE_PERMISSION_REQUEST_CODE = 100;
    private List<String> userJoinedList = new ArrayList<>(); // 已经onUserJoined的用户
    private AVChatVideoRender masterRender;

    private RelativeLayout viewLayout;
    private FrameLayout masterVideoLayout;
    private VideoFrameLayout videoLayout;
    private RelativeLayout backLayout;

    private TextView roomIdText;
    private ImageView videoPermission;
    private ImageView audioPermission;
    /**
     * 聊天室基本信息
     */
    private String roomId;
    private String sessionId = "";
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
    private Runnable loopStatus = new Runnable() {
        @Override
        public void run() {
            loopStatus();
        }
    };
    private long loopDelay = 10000;
    private boolean isShowTime = false;
    private ImageView zoom;
    private boolean isLocalVideoMuted = false;

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

    private void loopStatus() {
        if (id != 0) {
            Map<String, String> map = new HashMap<>();
            map.put("t", String.valueOf(System.currentTimeMillis()));
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlInteractCourses + id + "/live_status", map), null,
                    new VolleyListener(InteractiveLiveActivity.this) {

                        @Override
                        protected void onSuccess(JSONObject response) {
                            InteractiveLiveStatusBean data = JsonUtils.objectFromJson(response.toString(), InteractiveLiveStatusBean.class);
                            if (data != null && data.getData() != null && data.getData().getLive_info() != null && data.getData().getLive_info().getStatus().equals("teaching") && !StringUtils.isNullOrBlanK(data.getData().getLive_info().getRoom_id())) {
                                roomId = data.getData().getLive_info().getRoom_id();
                                enterRoom();
                                if (!StringUtils.isNullOrBlanK(data.getData().getLive_info().getName())) {
                                    roomIdText.setText(data.getData().getLive_info().getName());
                                }
                            } else {
                                hd.postDelayed(loopStatus, loopDelay);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.activity_interactive_live, null);
        setContentView(rootView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initView();
        EventBus.getDefault().register(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (NetUtils.checkPermission(this).size() > 0) {
                requestLivePermission();
            } else {
                id = getIntent().getIntExtra("id", 0);
                if (id == 0) {
                    Toast.makeText(this, getResourceString(R.string.no_course_information), Toast.LENGTH_SHORT).show();
                }
                initData();
                hd.postDelayed(loopStatus, 500);
            }
        } else {
            id = getIntent().getIntExtra("id", 0);
            if (id == 0) {
                Toast.makeText(this, getResourceString(R.string.no_course_information), Toast.LENGTH_SHORT).show();
            }
            initData();
            hd.postDelayed(loopStatus, 500);
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
        ((FragmentAnnouncements) fragBaseFragments.get(2)).setAnnouncements(announcement);
    }

    private void initData() {
        if (id != 0) {
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlInteractCourses + id + "/detail", null,
                    new VolleyListener(InteractiveLiveActivity.this) {
                        @Override
                        protected void onSuccess(JSONObject response) {
                            InteractCourseDetailBean data = JsonUtils.objectFromJson(response.toString(), InteractCourseDetailBean.class);
                            if (data != null && data.getData() != null) {
                                ((FragmentInteractiveDetails) fragBaseFragments.get(3)).setData(data.getData());
                                if (data.getData().getInteractive_course() != null && data.getData().getInteractive_course().getChat_team() != null) {
                                    sessionId = data.getData().getInteractive_course().getChat_team().getTeam_id();
                                    ((FragmentInteractiveMembers) fragBaseFragments.get(4)).setData(sessionId);

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

    private void initView() {
        viewLayout = (RelativeLayout) findViewById(R.id.view_layout);
        ViewGroup.LayoutParams params = viewLayout.getLayoutParams();
        params.height = ScreenUtils.getScreenWidth(this) * 3 / 5;
        viewLayout.setLayoutParams(params);
        roomIdText = (TextView) findViewById(R.id.room_id);
        masterVideoLayout = (FrameLayout) findViewById(R.id.master_video_layout);
        backLayout = (RelativeLayout) findViewById(R.id.back_layout);
        videoPermission = (ImageView) findViewById(R.id.video_permission);
        audioPermission = (ImageView) findViewById(R.id.audio_permission);
        zoom = (ImageView) findViewById(R.id.zoom);
        ImageView switchCamera = (ImageView) findViewById(R.id.switch_camera);
        videoLayout = (VideoFrameLayout) findViewById(R.id.video_layout);


        fragBaseFragments.add(new FragmentInteractiveBoard());
        fragBaseFragments.add(new FragmentInteractiveMessage());
        fragBaseFragments.add(new FragmentAnnouncements());
        fragBaseFragments.add(new FragmentInteractiveDetails());
        fragBaseFragments.add(new FragmentInteractiveMembers());
        messageFragment = (FragmentInteractiveMessage) fragBaseFragments.get(1);

        FragmentLayoutWithLine fragmentlayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(false);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4, getResources().getColor(R.color.colorPrimary));
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
                if (position == 1) {
                    if (inputPanel != null) {
                        inputPanel.visibilityInput();
                    }
                    if (messageFragment != null) {
                        messageFragment.scrollToBottom();
                    }
                } else {
                    if (inputPanel != null) {
                        inputPanel.goneInput();
                    }
                    if (position == 0) {
                        hd.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rtsFragment.refreshView();
                            }
                        }, 300);
                    }
                }
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_interactive_live, 0x0912);
        fragmentlayout.getViewPager().setOffscreenPageLimit(4);
        rtsFragment = (FragmentInteractiveBoard) fragBaseFragments.get(0);
        videoPermission.setOnClickListener(this);
        audioPermission.setOnClickListener(this);
        zoom.setOnClickListener(this);
        switchCamera.setOnClickListener(this);
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

    }

    private void initSessionId() {
        getAnnouncementsData();
        if (!StringUtils.isNullOrBlanK(sessionId)) {
            TeamMember team = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getInstance().getAccount());
            if (team != null) {
                isMute = team.isMute();
            }
        }
        inputPanel = new InputPanel(this, this, rootView, false, sessionId);
        inputPanel.setMute(isMute);

        messageFragment.setChatCallBack(new FragmentInteractiveMessage.Callback() {
            @Override
            public void back(List<IMMessage> result) {
                TeamMember team = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getInstance().getAccount());
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
            }
        });
        messageFragment.setSessionId(sessionId);

        inputPanel.setOnInputShowListener(new InputPanel.OnInputShowListener() {
            @Override
            public void OnInputShow() {
                messageFragment.scrollToBottom();
            }
        });
    }

    private void enterRoom() {
        isShowTime = true;
        registerObservers(true);
        registerRTSObservers(roomId, true);
        initLiveVideo();
        rtsFragment.initRTSView(roomId, this);
    }

    // 加入多人白板session
    private void joinRTSSession() {
        RTSManager2.getInstance().joinSession(roomId, true, new RTSCallback<RTSData>() {
            @Override
            public void onSuccess(RTSData rtsData) {
                Logger.e("rts extra:" + rtsData.getExtra());
                ChatRoomMemberCache.getInstance().setRTSOpen(true);
                updateRTSFragment();
            }

            @Override
            public void onFailed(int i) {
                Logger.e("join rts session failed, code:" + i);
                LogCatHelper.getInstance(null).log("join rts session failed, code:" + i);
                ChatRoomMemberCache.getInstance().setRTSOpen(false);
                updateRTSFragment();
                Toast.makeText(InteractiveLiveActivity.this, "白板加入失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    private void updateRTSFragment() {
        if (rtsFragment != null) {
            rtsFragment.initView(true);
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
        avChatOptionalParam.enableVideoRotate(false);
        AVChatManager.getInstance().joinRoom(roomId, AVChatType.VIDEO, avChatOptionalParam, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData avChatData) {
                Logger.e("join channel success, extra:" + avChatData.getExtra());
                // 设置音量信号监听, 通过AVChatStateObserver的onReportSpeaker回调音量大小
                AVChatParameters avChatParameters = new AVChatParameters();
                avChatParameters.setBoolean(NRtcParameters.KEY_AUDIO_REPORT_SPEAKER, true);
                AVChatManager.getInstance().setParameters(avChatParameters);
                chooseSpeechType();
                joinRTSSession();
            }

            @Override
            public void onFailed(int i) {
                Logger.e("join channel failed, code:" + i);
                LogCatHelper.getInstance(null).log("join channel failed, code:" + i);
                hd.removeCallbacks(loopStatus);
                hd.postDelayed(loopStatus, 15000);
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    private void updateVideoAudioUI() {
        videoPermission.setImageResource(!AVChatManager.getInstance().isLocalVideoMuted() ? R.mipmap.video_on : R.mipmap.video_off);
        audioPermission.setImageResource(!AVChatManager.getInstance().isLocalAudioMuted() ? R.mipmap.audio_on : R.mipmap.audio_off);
    }

    private void updateControlUI() {
        videoPermission.setVisibility(View.VISIBLE);
        audioPermission.setVisibility(View.VISIBLE);
    }

    private void registerObservers(boolean register) {
        AVChatManager.getInstance().observeAVChatState(this, register);
    }

    // 将有权限的成员添加到画布
    public void onVideoOn(String account) {
        showView(account);
    }

    // 显示成员图像
    private void showView(String a) {
        if (userJoinedList != null && userJoinedList.contains(a) && a.equals(BaseApplication.getInstance().getAccount())) {
            AVChatVideoRender render = new AVChatVideoRender(InteractiveLiveActivity.this);
            boolean isSetup = false;
            try {
                isSetup = AVChatManager.getInstance().setupLocalVideoRender(render, false, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
//                    Logger.e("setup render, creator account:" + roomInfo.getCreator() + ", render account:" + a + ", isSetup:" + isSetup);
            } catch (Exception e) {
                Logger.e("set up video render error:" + e.getMessage());
                e.printStackTrace();
            }
            if (isSetup) {
                addIntoPreviewLayout(render, videoLayout);
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
        viewLayout.addView(surfaceView, 0);
        surfaceView.setZOrderMediaOverlay(true);
    }

    // 选择发言方式
    private void chooseSpeechType() {
        AVChatManager.getInstance().startLive();
        AVChatManager.getInstance().enableAudienceRole(false);

        AVChatManager.getInstance().muteLocalAudio(false);

        AVChatManager.getInstance().muteLocalVideo(false);

        updateControlUI();
        updateVideoAudioUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (roomId != null) {
            clearChatRoom();
        }
        hd.removeCallbacks(hideBackLayout);
        hd.removeCallbacks(loopStatus);
        EventBus.getDefault().unregister(this);
    }

    private void clearChatRoom() {
        isShowTime = false;
        registerObservers(false);
        registerRTSObservers(roomId, false);
        AVChatManager.getInstance().leaveRoom(null);
        RTSManager2.getInstance().leaveSession(roomId, null);
        ChatRoomMemberCache.getInstance().setRTSOpen(false);
        roomId = "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_permission:
                setVideoState();
                break;
            case R.id.audio_permission:
                setAudioState();
                break;
            case R.id.zoom:
                zoom();
                break;
            case R.id.switch_camera:
                switchCamera();
                break;
        }
    }

    private void switchCamera() {
        if (isShowTime) {
            AVChatManager.getInstance().switchCamera();
        } else {
            Toast.makeText(this, "请开始互动后切换", Toast.LENGTH_SHORT).show();
        }
    }

    private void zoom() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    // 设置自己的摄像头是否开启
    private void setVideoState() {
        if (AVChatManager.getInstance().isLocalVideoMuted()) {
            videoPermission.setBackgroundResource(R.mipmap.video_on);
            AVChatManager.getInstance().muteLocalVideo(false);
            isLocalVideoMuted = false;
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                if (videoLayout.getVisibility() == View.GONE) {
                    videoLayout.setVisibility(View.VISIBLE);
                    if (videoLayout.getChildCount() == 1) {
                        videoLayout.getChildAt(0).setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            videoPermission.setBackgroundResource(R.mipmap.video_off);
            AVChatManager.getInstance().muteLocalVideo(true);
            isLocalVideoMuted = true;
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                if (videoLayout.getVisibility() == View.VISIBLE) {
                    if (videoLayout.getChildCount() == 1) {
                        videoLayout.getChildAt(0).setVisibility(View.GONE);
                    }
                    videoLayout.setVisibility(View.GONE);
                }
            }
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


    @OnMPermissionGranted(LIVE_PERMISSION_REQUEST_CODE)
    public void onLivePermissionGranted() {
//        Toast.makeText(InteractiveLiveActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
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
        if (i == AVChatResCode.JoinChannelCode.OK) {
            AVChatManager.getInstance().setSpeaker(true);
        }
    }

    @Override
    public void onLeaveChannel() {
        userJoinedList.remove(BaseApplication.getInstance().getAccount());
    }

    @Override
    public void onUserJoined(String s) {
        userJoinedList.add(s);
        onMasterJoin(s);
        onVideoOn(s);
    }

    @Override
    public void onUserLeave(String s, int i) {
        // 用户离开频道，如果是有权限用户，移除下画布
        masterVideoLayout.removeAllViews();
        if (videoLayout.getChildCount() == 1) {
            videoLayout.removeViewAt(0);
        }
        userJoinedList.clear();
        clearChatRoom();
        updateRTSFragment();
        hd.postDelayed(loopStatus, loopDelay);
        videoPermission.setVisibility(View.GONE);
        audioPermission.setVisibility(View.GONE);

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
        userJoinedList.add(BaseApplication.getInstance().getAccount());
        onVideoOn(BaseApplication.getInstance().getAccount());
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
        if (userJoinedList != null && userJoinedList.contains(s) && !s.equals(BaseApplication.getInstance().getAccount())) {
            if (masterRender == null) {
                masterRender = new AVChatVideoRender(InteractiveLiveActivity.this);
                masterRender.setKeepScreenOn(true);
            }
            boolean isSetup = setupMasterRender(s, AVChatVideoScalingType.SCALE_ASPECT_BALANCED);
            if (isSetup && masterRender != null) {
                addIntoMasterPreviewLayout(masterRender);
//                updateDeskShareUI();
            }
        }
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

    @Override
    public boolean isShowTime() {
        return isShowTime;
    }

    private void registerRTSObservers(String sessionName, boolean register) {
        RTSManager2.getInstance().observeChannelState(sessionName, channelStateObserver, register);
        RTSManager2.getInstance().observeReceiveData(sessionName, receiveDataObserver, register);
    }

    /**
     * 监听当前会话的状态
     */
    private RTSChannelStateObserver channelStateObserver = new RTSChannelStateObserver() {

        @Override
        public void onConnectResult(String localSessionId, RTSTunnelType tunType, long channelId, int code, String recordFile) {
//            Toast.makeText(InteractiveLiveActivity.this, "onConnectResult, tunType=" + tunType.toString() +
//                    ", channelId=" + channelId + ", code=" + code, Toast.LENGTH_SHORT).show();
            if (code != 200) {
                RTSManager2.getInstance().leaveSession(roomId, null);
                return;
            }

            List<Transaction> cache = new ArrayList<>(1);
            // 非主播进入房间，发送同步请求，请求主播向他同步之前的白板笔记
            TransactionCenter.getInstance().onNetWorkChange(roomId, false);
            cache.add(new Transaction().makeSyncRequestTransaction());
            TransactionCenter.getInstance().sendToRemote(roomId, null, cache);
            LogCatHelper.getInstance(null).log("send sync request");
        }

        @Override
        public void onChannelEstablished(String sessionId, RTSTunnelType tunType) {
//            Toast.makeText(InteractiveLiveActivity.this, "onCallEstablished,tunType=" + tunType.toString(), Toast.LENGTH_SHORT).show();

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
//            Logger.e("receive data");
            String data = "[parse bytes error]";
            try {
                data = new String(rtsTunData.getData(), 0, rtsTunData.getLength(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            TransactionCenter.getInstance().onReceive(roomId, rtsTunData.getAccount(), data);
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        int screenWidth = ScreenUtils.getScreenWidth(InteractiveLiveActivity.this);
//        int screenHeight = ScreenUtils.getScreenHeight(InteractiveLiveActivity.this);
        if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            zoom.setImageResource(R.mipmap.enlarge);
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            // 取消全屏设置
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            ViewGroup.LayoutParams params = viewLayout.getLayoutParams();
            params.height = ScreenUtils.getScreenWidth(InteractiveLiveActivity.this) * 3 / 5;
            params.width = ScreenUtils.getScreenWidth(InteractiveLiveActivity.this);
            viewLayout.setLayoutParams(params);
            if (!isLocalVideoMuted) {
                if (videoLayout.getVisibility() == View.GONE) {
                    videoLayout.setVisibility(View.VISIBLE);
                    if (videoLayout.getChildCount() == 1) {
                        videoLayout.getChildAt(0).setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            zoom.setImageResource(R.mipmap.narrow);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ViewGroup.LayoutParams params = viewLayout.getLayoutParams();
            params.height = -1;
            params.width = -1;
            viewLayout.setLayoutParams(params);

            if (videoLayout.getVisibility() == View.VISIBLE) {
                if (videoLayout.getChildCount() == 1) {
                    videoLayout.getChildAt(0).setVisibility(View.GONE);
                }
                videoLayout.setVisibility(View.GONE);
            }
        }
//        float resultX = videoLayout.getX();
//        float resultY = videoLayout.getY();
//        if (resultX < 0) {
//            resultX = 0;
//        } else if (resultX >= screenWidth - videoLayout.getWidth()) {
//            resultX = screenWidth - videoLayout.getWidth();
//        }
//
//        if (resultY < 0) {
//            resultY = 0;
//        } else if (resultY >= screenHeight - videoLayout.getHeight()) {
//            resultY = screenHeight - videoLayout.getHeight();
//        }
//        videoLayout.setX(resultX);
//        videoLayout.setY(resultY);
    }

    @Override
    public void onBackPressed() {
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            Logger.e("orta 返回竖屏");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        if (inputPanel != null && inputPanel.isEmojiShow()) {
            inputPanel.closeEmojiAndInput();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputPanel.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe
    public void onEvent(BusEvent event) {
        if (event == BusEvent.ANNOUNCEMENT) {
            if (StringUtils.isNullOrBlanK(sessionId)) return;
            getAnnouncementsData();
        }
//         else if (event == BusEvent.request) {
//            masterVideoLayout.removeAllViews();
//            if (videoLayout.getChildCount() == 1) {
//                videoLayout.removeViewAt(0);
//            }
//            userJoinedList.clear();
//            clearChatRoom();
//            updateRTSFragment();
//            videoPermission.setVisibility(View.GONE);
//            audioPermission.setVisibility(View.GONE);
//
//            checkToken();
//        }
    }

    private void checkToken() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getInstance().getUserId() + "/info", null,
                new VolleyListener(InteractiveLiveActivity.this) {


                    @Override
                    protected void onSuccess(JSONObject response) {
                        PersonalInformationBean bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);
                        if (bean != null && bean.getData() != null) {
                            setValue(bean);
                        }
                        reConnect();
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

    /**
     * 重新连接一对一
     */
    private void reConnect() {
        View view = View.inflate(this, R.layout.dialog_cancel_or_confirm, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText("您的账号正在使用其他客户端学习此课程,当前连接已被迫断开");
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setText("重连");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                hd.post(loopStatus);
            }
        });
        Button confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setText("关闭");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
    }

    private void setValue(PersonalInformationBean bean) {
        Profile profile = BaseApplication.getInstance().getProfile();
        Profile.Data data = profile.getData();
        if (data != null) {
            Profile.User user = data.getUser();
            if (user != null) {
                user.setLogin_mobile(bean.getData().getLogin_mobile());
                user.setEmail(bean.getData().getEmail());
                user.setAvatar_url(bean.getData().getAvatar_url());
                user.setOpenid(bean.getData().getOpenid());
                user.setName(bean.getData().getName());
                data.setUser(user);
            }
            profile.setData(data);
        }
        BaseApplication.getInstance().setProfile(profile);
    }

    @Override
    public void onSwitch(boolean isOpen) {
        if (!isOpen) {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                zoom.setImageResource(R.mipmap.enlarge);
            }
        } else {
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                zoom.setImageResource(R.mipmap.narrow);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isShowTime) {
            for (String s : userJoinedList) {
                if (!s.equals(BaseApplication.getInstance().getAccount())) {
                    AVChatManager.getInstance().muteRemoteAudio(s, false);
                    AVChatManager.getInstance().muteRemoteVideo(s, false);
                }
            }
        }
        MobclickAgent.onResume(this);
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, SessionTypeEnum.Team);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isShowTime) {
            for (String s : userJoinedList) {
                if (!s.equals(BaseApplication.getInstance().getAccount())) {
                    AVChatManager.getInstance().muteRemoteAudio(s, true);
                    AVChatManager.getInstance().muteRemoteVideo(s, true);
                }
            }
        }
        if (inputPanel != null)
            inputPanel.onPause();
        MobclickAgent.onPause(this);
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
    }

    @Override
    public void backClick(View v) {
        onBackPressed();
    }
}
