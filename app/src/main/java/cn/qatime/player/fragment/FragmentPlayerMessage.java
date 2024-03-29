package cn.qatime.player.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.NotificationType;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.BusEvent;
import cn.qatime.player.bean.Container;
import cn.qatime.player.bean.MessageListPanel;
import cn.qatime.player.bean.ModuleProxy;
import cn.qatime.player.im.SimpleCallback;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentPlayerMessage extends BaseFragment implements ModuleProxy {
    private TextView tipText;
    public List<IMMessage> items = new ArrayList<>();

    private Team team;
    private String sessionId = "";
    private Callback chatCallback;

    private Handler hd = new Handler();
    private boolean hasLoad = false;
    private boolean hasJoin = false;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (hasLoad) {
                if (tipText != null) {
                    tipText.setText(R.string.you_have_quit_the_group);
                    if (team.isMyTeam()) {
                        tipText.setVisibility(View.GONE);
                    } else {
                        tipText.setVisibility(View.VISIBLE);
                        if (!hasJoin) {
                            joinTeam();
                        }
                    }
                    hd.removeCallbacks(this);
                } else {
                    hd.postDelayed(this, 200);
                }
            } else {
                hd.postDelayed(this, 200);
            }
        }
    };

    private void joinTeam() {
        if (id != 0) {
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlCourses + id + "/join", null,
                    new VolleyListener(getActivity()) {
                        @Override
                        protected void onSuccess(JSONObject response) {
                            hasJoin = true;
                            hd.postDelayed(runnable, 200);
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

    private MessageListPanel messageListPanel;
    private View view;
    private int id = 0;
//    private String owner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment_player_message, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(view);
        hasLoad = true;
        registerTeamUpdateObserver(true);
    }

    private void initView(View view) {
        tipText = (TextView) view.findViewById(R.id.tip);
    }

    @Override
    public void shouldCollapseInputPanel() {
        chatCallback.shouldCollapseInputPanel();
    }

    @Override
    public boolean isLongClickEnabled() {
        return false;
    }


    /**
     * ****************** 观察者 **********************
     */

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(receiveMessageObserver, register);
    }


    Observer<List<IMMessage>> receiveMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            List<IMMessage> addedListItems = new ArrayList<>(messages.size());
            boolean needRefresh = false;
            for (IMMessage message : messages) {
                //做一下去重
                for (IMMessage item : items) {
                    if (item.isTheSame(message)) {
                        items.remove(item);
                        break;
                    }
                }
                if (message.getMsgType() == MsgTypeEnum.notification) {//收到公告更新通知消息,通知公告页面刷新公告
                    if (((NotificationAttachment) message.getAttachment()).getType() == NotificationType.UpdateTeam) {
                        UpdateTeamAttachment a = (UpdateTeamAttachment) message.getAttachment();
                        if (a.getUpdatedFields().containsKey(TeamFieldEnum.Announcement)) {
                            EventBus.getDefault().post(BusEvent.ANNOUNCEMENT);
                        }
                    }
                }
                if (isMyMessage(message)) {
                    addedListItems.add(message);
                    needRefresh = true;
                }
            }

            if (needRefresh) {
                if (chatCallback != null) {
                    chatCallback.back(addedListItems);
                }
                if (messageListPanel != null) {
                    messageListPanel.onIncomingMessage(addedListItems);
                }
            }
        }
    };

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == SessionTypeEnum.Team && !StringUtils.isNullOrBlanK(sessionId) && message.getSessionId().equals(sessionId);
    }

    /**
     * 请求群基本信息
     */

    private void requestTeamInfo() {
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

    /**
     * 更新群信息
     */
    private void updateTeamInfo(final Team d) {
        if (d == null) {
            return;
        }
        team = d;
        if (chatCallback != null) {
            chatCallback.updateTeam(team);
        }
//
        hd.postDelayed(runnable, 200);
    }


    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    public void registerTeamUpdateObserver(boolean register) {
        registerObservers(register);
        if (register) {
            TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
            TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
            TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        }
    }


    /**
     * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
     */
    TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {
        @Override
        public void onUpdateTeams(List<Team> teams) {
            if (team == null) {
                return;
            }
            for (Team t : teams) {
                if (t.getId().equals(team.getId())) {
                    updateTeamInfo(t);
                    break;
                }
            }
        }

        @Override
        public void onRemoveTeam(Team team) {
            if (team == null) {
                return;
            }
            if (team.getId().equals(FragmentPlayerMessage.this.team.getId())) {
                updateTeamInfo(team);
            }
        }
    };

    /**
     * 群成员资料变动通知和移除群成员通知
     */
    TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamDataCache.TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            if (messageListPanel != null) {
                messageListPanel.refreshMessageList();
            }
        }

        @Override
        public void onRemoveTeamMember(TeamMember member) {
        }
    };


    public void setSessionId(int id, String sessionId) {
        this.id = id;
        this.sessionId = sessionId;
        requestTeamInfo();
        Container container = new Container(getActivity(), sessionId, this);
        messageListPanel = new MessageListPanel(container, view);
//        if (!StringUtils.isNullOrBlanK(owner)) {
//            messageListPanel.setOwner(owner);
//        }
    }

    public void setChatCallBack(Callback c) {
        this.chatCallback = c;
    }

//    public void setOwner(String owner) {
//        if (messageListPanel != null) {
//            messageListPanel.setOwner(owner);
//        } else
//            this.owner = owner;
//    }

    public void onMsgSend(IMMessage message) {
        if (messageListPanel != null) {
            messageListPanel.onMsgSend(message);
        }
    }

    public void scrollToBottom() {
        if (messageListPanel != null) {
            messageListPanel.scrollToBottom();
        }
    }

    public interface Callback {
        void back(List<IMMessage> result);//发送消息

        void shouldCollapseInputPanel();//收起输入法回调

        void updateTeam(Team team);//再输入栏中判断是否在该群组

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (messageListPanel != null)
            messageListPanel.onDestroy();
        registerTeamUpdateObserver(false);
    }
}
