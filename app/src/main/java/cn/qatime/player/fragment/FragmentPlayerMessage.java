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
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.Container;
import cn.qatime.player.bean.MessageListPanel;
import cn.qatime.player.bean.ModuleProxy;
import cn.qatime.player.im.SimpleCallback;
import cn.qatime.player.im.cache.FriendDataCache;
import cn.qatime.player.im.cache.TeamDataCache;

public class FragmentPlayerMessage extends BaseFragment implements ModuleProxy {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_player_message, null);
        initView(view);
        hasLoad = true;
        registerTeamUpdateObserver(true);
        return view;
    }

    private void initView(View view) {
        tipText = (TextView) view.findViewById(R.id.tip);

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
                if (messageListPanel.isMyMessage(message) && (message.getMsgType() == MsgTypeEnum.text || message.getMsgType() == MsgTypeEnum.notification || message.getMsgType() == MsgTypeEnum.image)) {
                    if (message.getAttachment() instanceof NotificationAttachment) {//收到公告更新通知消息,通知公告页面刷新公告
                        if (((NotificationAttachment) message.getAttachment()).getType() == NotificationType.UpdateTeam) {
                            UpdateTeamAttachment a = (UpdateTeamAttachment) message.getAttachment();
                            for (Map.Entry<TeamFieldEnum, Object> field : a.getUpdatedFields().entrySet()) {
                                if (field.getKey() == TeamFieldEnum.Announcement) {
                                    EventBus.getDefault().post("announcement");
                                }
                            }
                        }
                    }
                    addedListItems.add(message);
                    needRefresh = true;
                }
            }

            if (needRefresh) {
                if (chatCallback != null) {
                    chatCallback.back(addedListItems);
                }
            }

        messageListPanel.onIncomingMessage(messages);

    }
};

/**
 * 请求群基本信息
 */

public void requestTeamInfo(){
        Team team=TeamDataCache.getInstance().getTeamById(sessionId);
        if(team!=null){
        updateTeamInfo(team);
        }else{
        TeamDataCache.getInstance().fetchTeamById(sessionId,new SimpleCallback<Team>(){
@Override
public void onResult(boolean success,Team result){
        if(success&&result!=null){
        updateTeamInfo(result);
        }else{
        Toast.makeText(getActivity(),getResourceString(R.string.failed_to_obtain_group_information),Toast.LENGTH_SHORT).show();
        getActivity().finish();
        }
        }
        });
        }
        }

/**
 * 更新群信息
 */
private void updateTeamInfo(final Team d){
        if(d==null){
        return;
        }
        team=d;
        if(chatCallback!=null){
        chatCallback.updateTeam(team);
        }
//
        hd.postDelayed(runnable,200);
        }


/**
 * 注册群信息更新监听
 *
 * @param register
 */
public void registerTeamUpdateObserver(boolean register){
        registerObservers(register);
        if(register){
        TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
        TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        }else{
        TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
        TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        }
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver,register);
        }


        /**
         * 群资料变动通知和移除群的通知（包括自己退群和群被解散）
         */
        TeamDataCache.TeamDataChangedObserver teamDataChangedObserver=new TeamDataCache.TeamDataChangedObserver(){
@Override
public void onUpdateTeams(List<Team>teams){
        if(team==null){
        return;
        }
        for(Team t:teams){
        if(t.getId().equals(team.getId())){
        updateTeamInfo(t);
        break;
        }
        }
        }

@Override
public void onRemoveTeam(Team team){
        if(team==null){
        return;
        }
        if(team.getId().equals(FragmentPlayerMessage.this.team.getId())){
        updateTeamInfo(team);
        }
        }
        };

        /**
         * 群成员资料变动通知和移除群成员通知
         */
        TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver=new TeamDataCache.TeamMemberDataChangedObserver(){

@Override
public void onUpdateTeamMember(List<TeamMember>members){
        messageListPanel.refreshMessageList();
        }

@Override
public void onRemoveTeamMember(TeamMember member){
        }
        };

        FriendDataCache.FriendDataChangedObserver friendDataChangedObserver=new FriendDataCache.FriendDataChangedObserver(){
@Override
public void onAddedOrUpdatedFriends(List<String>accounts){
        messageListPanel.refreshMessageList();
        }

@Override
public void onDeletedFriends(List<String>accounts){
        messageListPanel.refreshMessageList();
        }

@Override
public void onAddUserToBlackList(List<String>account){
        messageListPanel.refreshMessageList();
        }

@Override
public void onRemoveUserFromBlackList(List<String>account){
        messageListPanel.refreshMessageList();
        }
        };

public void setSessionId(String sessionId){
        this.sessionId=sessionId;
        }

public void setChatCallBack(Callback c){
        this.chatCallback=c;
        }

public void setOwner(String owner){
        if(messageListPanel!=null){
        messageListPanel.setOwner(owner);
        }
        }

public void onMsgSend(IMMessage message){
        messageListPanel.onMsgSend(message);
        }

public void scrollToBottom(){
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
        registerTeamUpdateObserver(false);
    }
}
