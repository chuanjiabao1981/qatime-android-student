package cn.qatime.player.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.NotificationType;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.adapter.MessageAdapter;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.im.SimpleCallback;
import cn.qatime.player.im.cache.FriendDataCache;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.view.listview.AutoRefreshListView;
import cn.qatime.player.view.listview.ListViewUtil;
import cn.qatime.player.view.listview.MessageListView;

public class FragmentPlayerMessage extends BaseFragment implements MessageAdapter.EventListener {
    private TextView tipText;
    private MessageListView messageListView;
    private MessageAdapter adapter;
    public List<IMMessage> items = new ArrayList<>();

    private Team team;

    // 从服务器拉取消息记录
    private boolean remote = false;

    private SessionTypeEnum sessionType = SessionTypeEnum.Team;
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
        messageListView = (MessageListView) view.findViewById(R.id.list);
        messageListView.requestDisallowInterceptTouchEvent(true);
        messageListView.setMode(AutoRefreshListView.Mode.START);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            messageListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

        adapter = new MessageAdapter(getActivity(), items, this);
        messageListView.setAdapter(adapter);

        messageListView.setOnRefreshListener(new MessageLoader(remote));
        messageListView.setListViewEventListener(new MessageListView.OnListViewEventListener() {
            @Override
            public void onListViewStartScroll() {
                if (chatCallback != null) {
                    chatCallback.shouldCollapseInputPanel();
                }
            }
        });
    }

    public void scrollToBottom() {
        adapter.notifyDataSetChanged();
        ListViewUtil.scrollToBottom(messageListView);
    }

    @Override
    public void resendMessage(IMMessage message) {
        // 重置状态为unsent
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            item.setStatus(MsgStatusEnum.sending);
            deleteItem(item);

            items.add(message);
            adapter.notifyDataSetChanged();
            ListViewUtil.scrollToBottom(messageListView);
        }
        NIMClient.getService(MsgService.class).sendMessage(message, true);
    }

    // 删除消息
    private void deleteItem(IMMessage messageItem) {
        NIMClient.getService(MsgService.class).deleteChattingHistory(messageItem);
        items.remove(messageItem);
        adapter.notifyDataSetChanged();
    }

    private class MessageLoader implements AutoRefreshListView.OnRefreshListener {
        private int LOAD_MESSAGE_COUNT = 20;//聊天加载条数
        // 从服务器拉取消息记录
        private QueryDirectionEnum direction;

        private boolean firstLoad = true;

        public MessageLoader(boolean remote) {
            if (remote) {
                loadFromRemote();
            } else {
                loadFromLocal(QueryDirectionEnum.QUERY_OLD);
            }
        }

        @Override
        public void onRefreshFromStart() {
            if (remote) {
                loadFromRemote();
            } else {
                loadFromLocal(QueryDirectionEnum.QUERY_OLD);
            }
        }

        @Override
        public void onRefreshFromEnd() {
            if (!remote) {
                loadFromLocal(QueryDirectionEnum.QUERY_NEW);
            }
        }

        private void loadFromLocal(QueryDirectionEnum direction) {
            this.direction = direction;
            NIMClient.getService(MsgService.class).queryMessageListEx(anchor(), direction, LOAD_MESSAGE_COUNT, true)
                    .setCallback(callback);
        }

        private void loadFromRemote() {
            this.direction = QueryDirectionEnum.QUERY_OLD;
            NIMClient.getService(MsgService.class).pullMessageHistory(anchor(), LOAD_MESSAGE_COUNT, true).setCallback(callback);
        }

        private RequestCallback<List<IMMessage>> callback = new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                if (messages != null) {
                    onMessageLoaded(messages);
                }
            }
        };


        private IMMessage anchor() {
            if (items.size() == 0) {
                return MessageBuilder.createEmptyMessage(sessionId, sessionType, 0);
            } else {
                int index = (direction == QueryDirectionEnum.QUERY_NEW ? items.size() - 1 : 0);
                return items.get(index);
            }
        }

        /**
         * 历史消息加载处理
         *
         * @param messages
         */
        private void onMessageLoaded(List<IMMessage> messages) {
            int count = messages.size();

            if (firstLoad && items.size() > 0) {
                // 在第一次加载的过程中又收到了新消息，做一下去重
                for (IMMessage message : messages) {
                    for (IMMessage item : items) {
                        if (item.isTheSame(message)) {
                            items.remove(item);
                            break;
                        }
                    }
                }
            }

            List<IMMessage> result = new ArrayList<>();
            for (IMMessage message : messages) {
                result.add(message);
            }
            if (direction == QueryDirectionEnum.QUERY_NEW) {
                items.addAll(result);
            } else {
                items.addAll(0, result);
            }

            // 如果是第一次加载，updateShowTimeItem返回的就是lastShowTimeItem
            if (firstLoad) {
                ListViewUtil.scrollToBottom(messageListView);
            }

//            adapter.updateShowTimeItem(items, true, firstLoad);
//            updateReceipt(items); // 更新已读回执标签

            refreshMessageList();
            messageListView.onRefreshComplete(count, LOAD_MESSAGE_COUNT, true);
            firstLoad = false;
        }
    }// 刷新消息列表

    public void refreshMessageList() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    /**
     * ****************** 观察者 **********************
     */

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(receiveMessageObserver, register);
        service.observeMsgStatus(messageStatusObserver, register);
        service.observeAttachmentProgress(attachmentProgressObserver, register);
    }

    /**
     * 消息状态变化观察者
     */
    Observer<IMMessage> messageStatusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            if (isMyMessage(message)) {
                onMessageStatusChange(message);
            }
        }
    };
    /**
     * 消息附件上传/下载进度观察者
     */
    Observer<AttachmentProgress> attachmentProgressObserver = new Observer<AttachmentProgress>() {
        @Override
        public void onEvent(AttachmentProgress progress) {
            onAttachmentProgressChange(progress);
        }
    };

    private void onMessageStatusChange(IMMessage message) {
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            item.setStatus(message.getStatus());
            item.setAttachStatus(message.getAttachStatus());
            if (item.getAttachment() instanceof AudioAttachment) {
                item.setAttachment(message.getAttachment());
            }
            refreshViewHolderByIndex(index);
        }
    }

    private void onAttachmentProgressChange(AttachmentProgress progress) {
        int index = getItemIndex(progress.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            float value = (float) progress.getTransferred() / (float) progress.getTotal();
            adapter.putProgress(item, value);
            refreshViewHolderByIndex(index);
        }
    }

    /**
     * 刷新单条消息
     *
     * @param index
     */
    private void refreshViewHolderByIndex(final int index) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (index < 0) {
                    return;
                }
                Object tag = ListViewUtil.getViewHolderByIndex(messageListView, index);
                if (tag instanceof MessageAdapter.ImageHolder) {
                    MessageAdapter.ImageHolder viewHolder = (MessageAdapter.ImageHolder) tag;
                    viewHolder.refresh();
                }
            }
        });
    }

    Observer<List<IMMessage>> receiveMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            Logger.e("获取到消息");
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
                if (isMyMessage(message) && (message.getMsgType() == MsgTypeEnum.text || message.getMsgType() == MsgTypeEnum.notification || message.getMsgType() == MsgTypeEnum.image)) {
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
                items.addAll(addedListItems);
                if (chatCallback != null) {
                    chatCallback.back(addedListItems);
                }
                sortMessages(items);
                adapter.notifyDataSetChanged();
            }


            // incoming messages tip
            IMMessage lastMsg = messages.get(messages.size() - 1);
            if (isMyMessage(lastMsg)) {
                ListViewUtil.scrollToBottom(messageListView);
            }

        }
    };

    /**
     * **************************** 排序 ***********************************
     */
    private void sortMessages(List<IMMessage> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<IMMessage> comp = new Comparator<IMMessage>() {

        @Override
        public int compare(IMMessage o1, IMMessage o2) {
            long time = o1.getTime() - o2.getTime();
            return time == 0 ? 0 : (time < 0 ? -1 : 1);
        }
    };

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == sessionType
                && message.getSessionId() != null
                && message.getSessionId().equals(sessionId);
    }

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
                        Toast.makeText(getActivity(), getResourceString(R.string.failed_to_obtain_group_information), Toast.LENGTH_SHORT).show();
                        getActivity().finish();
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
        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
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
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeamMember(TeamMember member) {
        }
    };

    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            adapter.notifyDataSetChanged();
        }
    };

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setChatCallBack(Callback c) {
        this.chatCallback = c;
    }

    public void setOwner(String owner) {
        if (adapter != null) {
            adapter.setOwner(owner);

        }
    }

    public interface Callback {
        void back(List<IMMessage> result);//发送消息

        void shouldCollapseInputPanel();//收起输入法回调

        void updateTeam(Team team);//再输入栏中判断是否在该群组
    }

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            IMMessage message = items.get(i);
            if (TextUtils.equals(message.getUuid(), uuid)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        registerTeamUpdateObserver(false);
    }
}
