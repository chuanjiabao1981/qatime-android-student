package cn.qatime.player.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewStructure;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.im.SimpleCallback;
import cn.qatime.player.im.cache.FriendDataCache;
import cn.qatime.player.im.cache.TeamDataCache;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2016/8/30 12:25
 * @Description
 */
public class MessageActivity extends BaseActivity {
    private String sessionId;//聊天对象id
    private SessionTypeEnum sessionType;
    private PullToRefreshListView listView;

    private boolean firstLoad = true;

    private SimpleDateFormat parse1 = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat parse2 = new SimpleDateFormat("MM-dd HH:mm");

    // 从服务器拉取消息记录
    private boolean remote = false;
    private IMMessage anchor;
    private QueryDirectionEnum direction;
    private List<IMMessage> items = new ArrayList<>();
    private int LOAD_MESSAGE_COUNT = 20;//聊天加载条数
    private CommonAdapter<IMMessage> adapter;
    private Team team;
    private Button send;
    private TextView tipText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        sessionId = getIntent().getStringExtra("sessionId");
        sessionType = (SessionTypeEnum) getIntent().getSerializableExtra("sessionType");
        initView();

        registerTeamUpdateObserver(true);
    }

    private void initView() {
        tipText = (TextView) findViewById(R.id.tip);
        listView = (PullToRefreshListView) findViewById(R.id.list);
        listView.getRefreshableView().setDividerHeight(0);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));

        adapter = new CommonAdapter<IMMessage>(this, items, R.layout.item_message) {
            @Override
            public void convert(ViewHolder holder, IMMessage item, int position) {

                if (item.getFromAccount().equals(BaseApplication.getAccount())) {
                    holder.getView(R.id.right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.left).setVisibility(View.GONE);
                    Glide.with(MessageActivity.this).load(BaseApplication.getProfile().getData().getUser().getChat_account().getIcon()).crossFade().dontAnimate().transform(new GlideCircleTransform(MessageActivity.this)).into((ImageView) holder.getView(R.id.my_head));
                    holder.setText(R.id.my_time, getTime(item.getTime()));
                    holder.setText(R.id.my_content, item.getContent());
                } else {
                    holder.getView(R.id.right).setVisibility(View.GONE);
                    holder.getView(R.id.left).setVisibility(View.VISIBLE);
                    Glide.with(MessageActivity.this).load(BaseApplication.getUserInfoProvide().getUserInfo(item.getFromAccount()).getAvatar()).placeholder(R.mipmap.head_32).crossFade().dontAnimate().transform(new GlideCircleTransform(MessageActivity.this)).into((ImageView) holder.getView(R.id.other_head));
                    holder.setText(R.id.other_name, item.getFromNick());
                    holder.setText(R.id.other_content, item.getContent());
                    holder.setText(R.id.other_time, getTime(item.getTime()));
                }

            }
        };
        listView.setAdapter(adapter);
        final EditText content = (EditText) findViewById(R.id.content);
        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAllowSendMessage()) {
                    Toast.makeText(MessageActivity.this, "您已不在该群,不能发送消息", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isNullOrBlanK(content.getText().toString())) {
                    Toast.makeText(MessageActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 创建文本消息
                IMMessage message = MessageBuilder.createTextMessage(
                        sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                        sessionType, // 聊天类型，单聊或群组
                        content.getText().toString() // 文本内容
                );
                // 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
                NIMClient.getService(MsgService.class).sendMessage(message, true);

                items.add(message);
                adapter.notifyDataSetChanged();
                listView.getRefreshableView().setSelection(items.size() - 1);
                content.setText("");
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String label = DateUtils.formatDateTime(MessageActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        listView.getLoadingLayoutProxy(false, true).setLastUpdatedLabel(label);
                        listView.onRefreshComplete();
                    }
                }, 200);
                loadFromRemote();
            }
        });
        loadMessage(false);
    }

    /**
     * 将long值转换为时间
     */
    private String getTime(long time) {
        Date result = new Date(time);
        Date current = new Date();
        if (result.getYear() == current.getYear() && result.getMonth() == current.getMonth() && result.getDate() == current.getDate()) {
            return parse1.format(result);
        } else {
            return parse2.format(result);
        }
    }

    public void loadMessage(boolean remote) {
        this.remote = remote;
        if (remote) {
            loadFromRemote();
        } else {
            if (anchor == null) {
                loadFromLocal(QueryDirectionEnum.QUERY_OLD);
            } else {
                // 加载指定anchor的上下文
                loadAnchorContext();
            }
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
            return anchor == null ? MessageBuilder.createEmptyMessage(sessionId, sessionType, 0) : anchor;
        } else {
            int index = (direction == QueryDirectionEnum.QUERY_NEW ? items.size() - 1 : 0);
            return items.get(index);
        }
    }

    private void loadAnchorContext() {
        // query old
        this.direction = QueryDirectionEnum.QUERY_OLD;
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor(), direction, LOAD_MESSAGE_COUNT, true)
                .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                    @Override
                    public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                        if (code != ResponseCode.RES_SUCCESS || exception != null) {
                            return;
                        }
                        onMessageLoaded(messages);

                        // query new
                        direction = QueryDirectionEnum.QUERY_NEW;
                        NIMClient.getService(MsgService.class).queryMessageListEx(anchor(), direction, LOAD_MESSAGE_COUNT, true)
                                .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                                    @Override
                                    public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                                        if (code != ResponseCode.RES_SUCCESS || exception != null) {
                                            return;
                                        }
                                        onMessageLoaded(messages);
                                    }
                                });
                    }
                });
    }


    /**
     * 历史消息加载处理
     *
     * @param messages
     */
    private void onMessageLoaded(List<IMMessage> messages) {
        if (remote) {
            Collections.reverse(messages);
        }
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

        if (firstLoad && anchor != null) {
            items.add(anchor);
        }

        List<IMMessage> result = new ArrayList<>();
        for (IMMessage message : messages) {
            if (message.getMsgType() == MsgTypeEnum.text) {
                result.add(message);
            }
        }
        if (direction == QueryDirectionEnum.QUERY_NEW) {
            items.addAll(result);
        } else {
            items.addAll(0, result);
        }

        adapter.notifyDataSetChanged();
        listView.getRefreshableView().setSelection(result.size());
        firstLoad = false;
    }

    /**
     * ****************** 观察者 **********************
     */

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeMsgStatus(messageStatusObserver, register);
        service.observeReceiveMessage(receiveMessageObserver, register);
    }

    Observer<List<IMMessage>> receiveMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            Logger.e("获取到消息");
            boolean needRefresh = false;
            List<IMMessage> addedListItems = new ArrayList<>(messages.size());
            for (IMMessage message : messages) {
                if (isMyMessage(message) && message.getMsgType() == MsgTypeEnum.text) {
                    items.add(message);
                    addedListItems.add(message);
                    needRefresh = true;
                }
            }
            if (needRefresh) {
                adapter.notifyDataSetChanged();
                listView.getRefreshableView().setSelection(adapter.getCount() - 1);
            }
        }
    };
    /**
     * 消息状态变化观察者
     */
    Observer<IMMessage> messageStatusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            if (isMyMessage(message)) {
            }
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
                        Toast.makeText(MessageActivity.this, "获取群组信息失败!", Toast.LENGTH_SHORT).show();
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
        tipText.setText(team.getType() == TeamTypeEnum.Normal ? "您已退出该群组" : "您已退出该群组");
        tipText.setVisibility(team.isMyTeam() ? View.GONE : View.VISIBLE);
    }

    public boolean isAllowSendMessage() {
        if (team == null || !team.isMyTeam()) {
            Toast.makeText(MessageActivity.this, R.string.team_send_message_not_allow, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

    @Override
    protected void onResume() {
        super.onResume();
        registerObservers(true);
        requestTeamInfo();
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerTeamUpdateObserver(false);
    }
}
