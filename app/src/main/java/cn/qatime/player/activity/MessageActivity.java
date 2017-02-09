package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.MessageAdapter;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.ChatVideoBean;
import cn.qatime.player.bean.InputPanel;
import cn.qatime.player.im.SimpleCallback;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.view.listview.AutoRefreshListView;
import cn.qatime.player.view.listview.ListViewUtil;
import cn.qatime.player.view.listview.MessageListView;
import libraryextra.bean.ImageItem;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2016/8/30 12:25
 * @Description 聊天
 */
public class MessageActivity extends BaseActivity implements InputPanel.InputPanelListener, MessageAdapter.EventListener {
    private String sessionId;//聊天对象id
    private SessionTypeEnum sessionType;
    private MessageListView messageListView;

    private boolean remote = false;
    private List<IMMessage> items = new ArrayList<>();
    private Team team;
    private TextView tipText;

    private int courseId;
    private boolean isMute = false;//当前用户 是否被禁言
    private MessageAdapter adapter;
    private View rootView;
    private InputPanel inputpanel;
    private MessageLoader messageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = View.inflate(this, R.layout.activity_message, null);
        setContentView(rootView);
        EventBus.getDefault().register(this);
        String name = getIntent().getStringExtra("name");
        if (!StringUtils.isNullOrBlanK(name)) {
            setTitles(name);
        } else {
            setTitles(getResources().getString(R.string.team_group));
        }
        sessionId = getIntent().getStringExtra("sessionId");
        sessionType = (SessionTypeEnum) getIntent().getSerializableExtra("sessionType");
        courseId = getIntent().getIntExtra("courseId", 0);
        initView();
        setRightImage(R.mipmap.online_room, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, NEVideoPlayerActivity.class);
//                intent.putExtra("camera", camera);
//                intent.putExtra("board", board);
                intent.putExtra("id", courseId);
                intent.putExtra("sessionId", sessionId);
                startActivityForResult(intent, Constant.REQUEST);
            }
        });
        registerObservers(true);
        registerTeamUpdateObserver(true);
    }

    private void initView() {
        TeamMember teamMember = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount());
        if (teamMember != null) {
            isMute = teamMember.isMute();
        }
        tipText = (TextView) findViewById(R.id.tip);
        messageListView = (MessageListView) findViewById(R.id.list);
        messageListView.requestDisallowInterceptTouchEvent(true);
        messageListView.setMode(AutoRefreshListView.Mode.START);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            messageListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

        adapter = new MessageAdapter(this, items, this);
        adapter.setOwner(getIntent().getStringExtra("owner"));
        messageListView.setAdapter(adapter);

        messageLoader = new MessageLoader(remote);
        messageListView.setOnRefreshListener(messageLoader);

        inputpanel = new InputPanel(this, this, rootView, true, sessionId);
        inputpanel.setMute(isMute);
        messageListView.setListViewEventListener(new MessageListView.OnListViewEventListener() {
            @Override
            public void onListViewStartScroll() {
                shouldCollapseInputPanel();
            }
        });
    }

    /**
     * 收起输入法等
     */
    private void shouldCollapseInputPanel() {
        inputpanel.closeEmojiAndInput();
    }

    @Override
    public void ChatMessage(IMMessage message) {
////         创建文本消息
//        IMMessage message = MessageBuilder.createTextMessage(
//                sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
//                sessionType, // 聊天类型，单聊或群组
//                data // 文本内容
//        );
        sendMessage(message);
    }

    private void sendMessage(IMMessage message) {
        // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
        NIMClient.getService(MsgService.class).sendMessage(message, true);

        items.add(message);
        adapter.notifyDataSetChanged();
        ListViewUtil.scrollToBottom(messageListView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.RESPONSE_CAMERA) {//拍照返回的照片
            if (data != null) {
                String url = data.getStringExtra("url");
                if (url != null && !StringUtils.isNullOrBlanK(url)) {
                    File file = new File(url);
                    if (file.exists()) {
                        sendMessage(MessageBuilder.createImageMessage(sessionId, sessionType, file, file.getName()));
                    }
                }
            }
        } else if (resultCode == Constant.RESPONSE_PICTURE_SELECT) {//选择照片返回的照片
            if (data != null) {
                ImageItem image = (ImageItem) data.getSerializableExtra("data");
                if (image != null && !StringUtils.isNullOrBlanK(image.imagePath)) {
                    if (!inputpanel.isAllowSendMessage()) {
                        return;
                    }
                    if (inputpanel.checkMute()) {
                        return;
                    }
                    File file = new File(image.imagePath);
                    if (file.exists()) {
                        sendMessage(MessageBuilder.createImageMessage(sessionId, sessionType, file, file.getName()));
                    }
                }
            }
        }else{
            messageLoader.onRefreshFromEnd();
        }
    }

    /**
     * 点击重新发送
     *
     * @param message
     */
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

        MessageLoader(boolean remote) {
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

            refreshMessageList();
            messageListView.onRefreshComplete(count, LOAD_MESSAGE_COUNT, true);
            firstLoad = false;


        }
    }

    // 刷新消息列表
    public void refreshMessageList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
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
        runOnUiThread(new Runnable() {

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
//            boolean needScrollToBottom = ListViewUtil.isLastMessageVisible(messageListView);
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
                    items.add(message);
                    needRefresh = true;
                }
            }

            if (needRefresh) {
                TeamMember teamMember = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount());
                if (teamMember != null) {
                    isMute = teamMember.isMute();
                }
                inputpanel.setMute(isMute);
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

    private void requestTeamInfo() {
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
                        Toast.makeText(MessageActivity.this, getResourceString(R.string.failed_to_obtain_group_information), Toast.LENGTH_SHORT).show();
                        finish();
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
        inputpanel.setTeam(team);
        tipText.setText(team.getType() == TeamTypeEnum.Normal ? getResourceString(R.string.you_have_quit_the_group) : getResourceString(R.string.you_have_quit_the_group));
        tipText.setVisibility(team.isMyTeam() ? View.GONE : View.VISIBLE);
    }


    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    private void registerTeamUpdateObserver(boolean register) {
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
            if (team.getId().equals(MessageActivity.this.team.getId())) {
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

    @Override
    protected void onResume() {
        super.onResume();
        requestTeamInfo();
        MobclickAgent.onResume(this);
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
    }

    @Override
    public void onBackPressed() {
        if (inputpanel.isEmojiShow()) {
            inputpanel.closeEmojiAndInput();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
    }

    @Subscribe
    public void onEvent(ChatVideoBean event) {
        if (event != null) {
            this.courseId = event.getCourseId();
            if (adapter != null) {
                adapter.setOwner(event.getChat_team_owner());
            }
            if (!StringUtils.isNullOrBlanK(event.getName())) {
                setTitles(event.getName());
            } else {
                setTitles(getResources().getString(R.string.team_group));
            }
        }
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
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerTeamUpdateObserver(false);
        EventBus.getDefault().unregister(this);
    }

}
