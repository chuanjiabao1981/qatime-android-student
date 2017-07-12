package cn.qatime.player.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.NotificationType;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;

import org.greenrobot.eventbus.EventBus;

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

/**
 * @author lungtify
 * @Time 2017/3/28 11:23
 * @Describe
 */
public class FragmentInteractiveMessage extends BaseFragment implements ModuleProxy {
    private TextView tipText;
    public List<IMMessage> items = new ArrayList<>();

    private Team team;

    private String sessionId = "";
    private Callback chatCallback;

    private Handler hd = new Handler();
    private boolean hasLoad = false;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (hasLoad) {
                if (tipText != null) {
                    tipText.setText(team.getType() == TeamTypeEnum.Normal ? R.string.you_have_quit_the_group : R.string.you_have_quit_the_group);
                    tipText.setVisibility(team.isMyTeam() ? View.GONE : View.VISIBLE);
                    hd.removeCallbacks(this);
                } else {
                    hd.postDelayed(this, 200);
                }
            } else {
                hd.postDelayed(this, 200);
            }
        }
    };
    private MessageListPanel messageListPanel;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.fragment_interactive_message, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        hasLoad = true;
        registerTeamUpdateObserver(true);
    }

    private void initView() {
        tipText = (TextView) findViewById(R.id.tip);

        Container container = new Container(getActivity(), sessionId, this);
        messageListPanel = new MessageListPanel(container, view);
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
                if (messageListPanel.isMyMessage(message)) {
                    if (message.getMsgType() == MsgTypeEnum.notification) {//收到公告更新通知消息,通知公告页面刷新公告
                        if (((NotificationAttachment) message.getAttachment()).getType() == NotificationType.UpdateTeam) {
                            UpdateTeamAttachment a = (UpdateTeamAttachment) message.getAttachment();
                            if (a.getUpdatedFields().containsKey(TeamFieldEnum.Announcement)) {
                                EventBus.getDefault().post(BusEvent.ANNOUNCEMENT);
                            }
                        }
                    }
//                    else if (message.getMsgType() == MsgTypeEnum.custom) {
//                        message.setStatus(MsgStatusEnum.read);
//                        if (!StringUtils.isNullOrBlanK(message.getContent())) {
//                            if (message.getContent().equals(InteractiveDeskShareStatus.desktop))//desktop board
//                                EventBus.getDefault().post(BusEvent.desktop);
//                            else if (message.getContent().equals(InteractiveDeskShareStatus.board))
//                                EventBus.getDefault().post(BusEvent.board);
//                            else if (message.getContent().equals(InteractiveDeskShareStatus.request)) {
//                                if (message.getFromAccount().equals(BaseApplication.getInstance().getAccount())) {
//                                    EventBus.getDefault().post(BusEvent.request);
//                                }
//                            }
//                        }
//                    }
                }
                addedListItems.add(message);
                needRefresh = true;
            }

            if (needRefresh) {
                if (chatCallback != null) {
                    chatCallback.back(addedListItems);
                }
                messageListPanel.onIncomingMessage(addedListItems);
            }
        }
    };

    /**
     * 请求群基本信息
     */

    public void requestTeamInfo() {
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
            if (team.getId().equals(FragmentInteractiveMessage.this.team.getId())) {
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
            messageListPanel.refreshMessageList();
        }

        @Override
        public void onRemoveTeamMember(TeamMember member) {
        }
    };

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setChatCallBack(Callback c) {
        this.chatCallback = c;
    }

    public void setOwner(String owner) {
        if (messageListPanel != null) {
            messageListPanel.setOwner(owner);
        }
    }

    public void onMsgSend(IMMessage message) {
        messageListPanel.onMsgSend(message);
    }

    public void scrollToBottom() {
        messageListPanel.scrollToBottom();
    }

    public interface Callback {
        void back(List<IMMessage> result);//发送消息

        void shouldCollapseInputPanel();//收起输入法回调

        void updateTeam(Team team);//再输入栏中判断是否在该群组

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        messageListPanel.onDestroy();
        registerTeamUpdateObserver(false);
    }
}
