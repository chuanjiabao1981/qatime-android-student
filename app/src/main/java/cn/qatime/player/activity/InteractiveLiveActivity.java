package cn.qatime.player.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatAudioEffectMode;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatOptionalConfig;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData;
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData;
import com.netease.nimlib.sdk.rts.RTSManager2;
import com.netease.nrtc.sdk.NRtcParameters;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.fragment.FragmentInteractiveAnnouncements;
import cn.qatime.player.fragment.FragmentInteractiveBoard;
import cn.qatime.player.fragment.FragmentInteractiveDetails;
import cn.qatime.player.fragment.FragmentInteractiveMembers;
import cn.qatime.player.fragment.FragmentInteractiveMessage;
import cn.qatime.player.im.cache.ChatRoomMemberCache;
import cn.qatime.player.im.view.DialogMaker;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * @author lungtify
 * @Time 2017/3/28 10:22
 * @Describe 互动直播
 */

public class InteractiveLiveActivity extends BaseActivity {
    private FragmentLayoutWithLine fragmentlayout;
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4, R.id.tab_text5};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private AbortableFuture<EnterChatRoomResultData> enterRequest;

    /**
     * 聊天室基本信息
     */
    private String roomId;
    private ChatRoomInfo roomInfo;
    private FragmentInteractiveBoard rtsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interactive_live);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();

        // 注册监听
//        registerObservers(true);

    }

    private void initView() {

        fragBaseFragments.add(new FragmentInteractiveBoard());
        fragBaseFragments.add(new FragmentInteractiveMessage());
        fragBaseFragments.add(new FragmentInteractiveAnnouncements());
        fragBaseFragments.add(new FragmentInteractiveDetails());
        fragBaseFragments.add(new FragmentInteractiveMembers());

        fragmentlayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4, 0xffff5842);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_interactive_live, 0x0912);

        rtsFragment = (FragmentInteractiveBoard) fragBaseFragments.get(0);
    }

    private void enterRoom() {
        DialogMaker.showProgressDialog(this, null, "", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (enterRequest != null) {
                    enterRequest.abort();
                    onLoginDone();
                    finish();
                }
            }
        }).setCanceledOnTouchOutside(false);
        EnterChatRoomData data = new EnterChatRoomData(roomId);
        enterRequest = NIMClient.getService(ChatRoomService.class).enterChatRoom(data);
        enterRequest.setCallback(new RequestCallback<EnterChatRoomResultData>() {
            @Override
            public void onSuccess(EnterChatRoomResultData result) {
                onLoginDone();
                roomInfo = result.getRoomInfo();
                ChatRoomMember member = result.getMember();
                member.setRoomId(roomInfo.getRoomId());
                ChatRoomMemberCache.getInstance().saveMyMember(member);
//                if (roomInfo.getExtension() != null) {
//                    shareUrl = (String) roomInfo.getExtension().get(KEY_SHARE_URL);
//                }
                initLiveVideo();
                rtsFragment.initRTSView(roomInfo);
//                registerRTSObservers(roomInfo.getRoomId(), true);
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == ResponseCode.RES_CHATROOM_BLACKLIST) {
                    Toast.makeText(InteractiveLiveActivity.this, "你已被拉入黑名单，不能再进入", Toast.LENGTH_SHORT).show();
                } else if (code == ResponseCode.RES_ENONEXIST) {
                    Toast.makeText(InteractiveLiveActivity.this, "该聊天室不存在", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InteractiveLiveActivity.this, "enter chat room failed, code=" + code, Toast.LENGTH_SHORT).show();
                }
                finish();
            }

            @Override
            public void onException(Throwable exception) {
                onLoginDone();
                Toast.makeText(InteractiveLiveActivity.this, "enter chat room exception, e=" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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
        switchHandsUpLayout();
        updateVideoAudioUI();
    }

    private void onLoginDone() {
        enterRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    private void registerObservers(boolean register) {
//        NIMClient.getService(ChatRoomServiceObserver.class).observeOnlineStatus(onlineStatus, register);
//        NIMClient.getService(ChatRoomServiceObserver.class).observeKickOutEvent(kickOutObserver, register);
//        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

//    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
//        @Override
//        public void onEvent(StatusCode statusCode) {
//            if (statusCode.wontAutoLogin()) {
//                NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
//                onKickOut();
//            }
//        }
//    };

//    Observer<ChatRoomStatusChangeData> onlineStatus = new Observer<ChatRoomStatusChangeData>() {
//        @Override
//        public void onEvent(ChatRoomStatusChangeData chatRoomStatusChangeData) {
//            if (chatRoomStatusChangeData.status == StatusCode.CONNECTING) {
//                DialogMaker.updateLoadingMessage("连接中...");
//            } else if (chatRoomStatusChangeData.status == StatusCode.UNLOGIN) {
//                if (NIMClient.getService(ChatRoomService.class).getEnterErrorCode(roomId) == ResponseCode.RES_CHATROOM_STATUS_EXCEPTION) {
//                    // 聊天室连接状态异常
//                    Toast.makeText(InteractiveLiveActivity.this, R.string.chatroom_status_exception, Toast.LENGTH_SHORT).show();
//                    NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
//                    onKickOut();
//                } else {
//                    Toast.makeText(InteractiveLiveActivity.this, R.string.nim_status_unlogin, Toast.LENGTH_SHORT).show();
//                    onOnlineStatusChanged(false);
//                }
//            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINING) {
//                DialogMaker.updateLoadingMessage("登录中...");
//            } else if (chatRoomStatusChangeData.status == StatusCode.LOGINED) {
//                onOnlineStatusChanged(true);
//            } else if (chatRoomStatusChangeData.status.wontAutoLogin()) {
//            } else if (chatRoomStatusChangeData.status == StatusCode.NET_BROKEN) {
//                Toast.makeText(InteractiveLiveActivity.this, R.string.net_broken, Toast.LENGTH_SHORT).show();
//                onOnlineStatusChanged(false);
//            }
//        }
//    };
//
//    Observer<ChatRoomKickOutEvent> kickOutObserver = new Observer<ChatRoomKickOutEvent>() {
//        @Override
//        public void onEvent(ChatRoomKickOutEvent chatRoomKickOutEvent) {
//            if (chatRoomKickOutEvent.getReason() == ChatRoomKickOutEvent.ChatRoomKickOutReason.CHAT_ROOM_INVALID) {
//                if (!roomInfo.getCreator().equals(DemoCache.getAccount()))
//                    Toast.makeText(InteractiveLiveActivity.this, R.string.meeting_closed, Toast.LENGTH_SHORT).show();
//            } else
//            if (chatRoomKickOutEvent.getReason() == ChatRoomKickOutEvent.ChatRoomKickOutReason.KICK_OUT_BY_MANAGER) {
//                Toast.makeText(InteractiveLiveActivity.this, R.string.kick_out_by_master, Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(InteractiveLiveActivity.this, "被踢出聊天室，reason:" + chatRoomKickOutEvent.getReason(), Toast.LENGTH_SHORT).show();
//            }
//
//            onKickOut();
//        }
//    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);

        if (roomId != null) {
            NIMClient.getService(ChatRoomService.class).exitChatRoom(roomId);
            clearChatRoom();
        }
    }

    private void clearChatRoom() {
        AVChatManager.getInstance().leaveRoom(null);
        RTSManager2.getInstance().leaveSession(roomId, null);
        ChatRoomMemberCache.getInstance().clearRoomCache(roomId);
    }
}
