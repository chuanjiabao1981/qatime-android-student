package cn.qatime.player.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.MessageAdapter;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.im.SimpleCallback;
import cn.qatime.player.im.cache.FriendDataCache;
import cn.qatime.player.im.cache.TeamDataCache;

public class PlayerMessageF extends BaseFragment {
    private TextView tipText;
    public PullToRefreshListView listView;
    public BaseAdapter adapter;
    public List<IMMessage> items = new ArrayList<>();

    public Team team;

    private boolean firstLoad = true;

    private SimpleDateFormat parse1 = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat parse2 = new SimpleDateFormat("MM-dd HH:mm");

    // 从服务器拉取消息记录
    private boolean remote = false;

    private IMMessage anchor;
    private QueryDirectionEnum direction;
    private int LOAD_MESSAGE_COUNT = 20;//聊天加载条数

    private SessionTypeEnum sessionType = SessionTypeEnum.Team;
    private String sessionId;
    private Callback chatCallback;

    private Handler hd = new Handler();
    private boolean hasLoad = false;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (hasLoad) {
                if (tipText != null) {
                    tipText.setText(team.getType() == TeamTypeEnum.Normal ? "您已退出该群组" : "您已退出该群组");
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
        return view;
    }

    private void initView(View view) {
        tipText = (TextView) view.findViewById(R.id.tip);
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.getRefreshableView().setDividerHeight(0);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));

//        adapter = new CommonAdapter<IMMessage>(getActivity(), items, R.layout.item_message) {
//            @Override
//            public void convert(final ViewHolder holder, IMMessage item, int position) {
//
//                if (item.getFromAccount().equals(BaseApplication.getAccount())) {
//                    holder.getView(R.id.right).setVisibility(View.VISIBLE);
//                    holder.getView(R.id.left).setVisibility(View.GONE);
//                    //.transform(new GlideRoundTransform(MessageActivity.this))
//                    Glide.with(getActivity()).load(BaseApplication.getProfile().getData().getUser().getEx_big_avatar_url()).crossFade().dontAnimate().into((ImageView) holder.getView(R.id.my_head));
//                    holder.setText(R.id.my_time, getTime(item.getTime()));
//                    ((TextView) holder.getView(R.id.my_content)).setText(ExpressionUtil.getExpressionString(
//                            getActivity(), item.getContent(), ExpressionUtil.emoji, new Hashtable<Integer, GifDrawable>(), new GifDrawable.UpdateListener() {
//                                @Override
//                                public void update() {
//                                    ((TextView) holder.getView(R.id.my_content)).postInvalidate();
//                                }
//                            }));
//                } else {
//                    holder.getView(R.id.right).setVisibility(View.GONE);
//                    holder.getView(R.id.left).setVisibility(View.VISIBLE);
//                    Glide.with(getActivity()).load(BaseApplication.getUserInfoProvide().getUserInfo(item.getFromAccount()).getAvatar()).placeholder(R.mipmap.head_32).crossFade().dontAnimate().into((ImageView) holder.getView(R.id.other_head));
//                    holder.setText(R.id.other_name, item.getFromNick());
//                    ((TextView) holder.getView(R.id.other_content)).setText(ExpressionUtil.getExpressionString(
//                            getActivity(), item.getContent(), ExpressionUtil.emoji, new Hashtable<Integer, GifDrawable>(), new GifDrawable.UpdateListener() {
//                                @Override
//                                public void update() {
//                                    ((TextView) holder.getView(R.id.other_content)).postInvalidate();
//                                }
//                            }));
//                    holder.setText(R.id.other_time, getTime(item.getTime()));
//                }
//
//            }
//        };
        adapter = new MessageAdapter(getActivity(), items);
        listView.setAdapter(adapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
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
//        messageListView.onRefreshStart(direction == QueryDirectionEnum.QUERY_NEW ? AutoRefreshListView.Mode.END : AutoRefreshListView.Mode.START);
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
            if (message.getMsgType() == MsgTypeEnum.text || message.getMsgType() == MsgTypeEnum.notification) {
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
//        messageListView.onRefreshComplete(count, LOAD_MESSAGE_COUNT, true);
        firstLoad = false;
    }

    /**
     * ****************** 观察者 **********************
     */

    public void registerObservers(boolean register) {
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
                if (isMyMessage(message) && (message.getMsgType() == MsgTypeEnum.text || message.getMsgType() == MsgTypeEnum.notification)) {
                    items.add(message);
                    addedListItems.add(message);
                    needRefresh = true;
                }
            }
            if (chatCallback != null) {
                chatCallback.back(addedListItems);
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
        public void onEvent(IMMessage imMessage) {
            if (isMyMessage(imMessage)) {
//                onMessageStatusChange(message);
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
//        setTitle(team == null ? sessionId : team.getName() + "(" + team.getMemberCount() + "人)");
//
        hd.postDelayed(runnable, 200);
    }

    public boolean isAllowSendMessage() {
        if (team == null || !team.isMyTeam()) {
            Toast.makeText(getActivity(), R.string.team_send_message_not_allow, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * 注册群信息更新监听
     *
     * @param register
     */
    public void registerTeamUpdateObserver(boolean register) {
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
            if (team.getId().equals(PlayerMessageF.this.team.getId())) {
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

    public interface Callback {
        void back(List<IMMessage> result);
    }
}
