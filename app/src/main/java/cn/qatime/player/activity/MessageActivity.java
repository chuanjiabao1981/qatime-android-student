package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.MessageAdapter;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.ChatVideoBean;
import cn.qatime.player.im.SimpleCallback;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.view.BiaoQingView;
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
    private QueryDirectionEnum direction;
    private List<IMMessage> items = new ArrayList<>();
    private int LOAD_MESSAGE_COUNT = 20;//聊天加载条数
    //    private CommonAdapter<IMMessage> adapter;
    private Team team;
    private Button send;
    private TextView tipText;
    private ImageView emoji;
    private EditText content;

    private int courseId;
    private boolean isMute = false;//当前用户 是否被禁言
    private MessageAdapter adapter;
    private String board;
    private String camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        EventBus.getDefault().register(this);
        String name = getIntent().getStringExtra("name");
        if (!StringUtils.isNullOrBlanK(name)) {
            setTitle(name);
        } else {
            setTitle(getResources().getString(R.string.team));
        }
        sessionId = getIntent().getStringExtra("sessionId");
        sessionType = (SessionTypeEnum) getIntent().getSerializableExtra("sessionType");
        courseId = getIntent().getIntExtra("courseId", 0);
        camera = getIntent().getStringExtra("camera");
        board = getIntent().getStringExtra("board");
        initView();
        setRightImage(R.mipmap.online_room, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, NEVideoPlayerActivity.class);
                intent.putExtra("camera", camera);
                intent.putExtra("board", board);
                intent.putExtra("id", courseId);
                intent.putExtra("sessionId", sessionId);
                startActivity(intent);
            }
        });
        loadMessage(false);
        registerTeamUpdateObserver(true);
    }

    private void initView() {
        isMute = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount()).isMute();
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

        adapter = new MessageAdapter(this, items);
        adapter.setOwner(getIntent().getStringExtra("owner"));
        listView.setAdapter(adapter);
        content = (EditText) findViewById(R.id.content);
        emoji = (ImageView) findViewById(R.id.emoji);

        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAllowSendMessage()) {
                    Toast.makeText(MessageActivity.this, getResourceString(R.string.team_send_message_not_allow), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isNullOrBlanK(content.getText().toString())) {
                    Toast.makeText(MessageActivity.this, getResourceString(R.string.message_can_not_null), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isMute) {
                    Toast.makeText(MessageActivity.this, getResourceString(R.string.have_muted), Toast.LENGTH_SHORT).show();
                    content.setText("");
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
//        loadMessage(false);
        BiaoQingView bq = (BiaoQingView) findViewById(R.id.biaoQingView);
        bq.init(content, emoji);
        if (isMute) {
            content.setHint(R.string.have_muted);
        } else {
            content.setHint("");
        }
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
            loadFromLocal(QueryDirectionEnum.QUERY_OLD);
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
        firstLoad = false;
    }

    /**
     * ****************** 观察者 **********************
     */

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
//        service.observeMsgStatus(messageStatusObserver, register);
        service.observeReceiveMessage(receiveMessageObserver, register);
    }

    Observer<List<IMMessage>> receiveMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }
            Logger.e("获取到消息");
            boolean needRefresh = false;
            for (IMMessage message : messages) {
//                if (isMyMessage(message) && message.getMsgType() == MsgTypeEnum.text) {
//                    addedListItems.add(message);
//                    needRefresh = true;
//                }
                //做一下去重
                for (IMMessage item : items) {
                    if (item.isTheSame(message)) {
                        items.remove(item);
                        break;
                    }
                }
                if (isMyMessage(message) && (message.getMsgType() == MsgTypeEnum.text || message.getMsgType() == MsgTypeEnum.notification)) {
                    items.add(message);
                    isMute = TeamDataCache.getInstance().getTeamMember(sessionId, BaseApplication.getAccount()).isMute();
                    if (isMute) {
                        content.setHint(R.string.have_muted);
                    } else {
                        content.setHint("");
                    }
                    needRefresh = true;
                }

            }

            if (needRefresh) {
                sortMessages(items);
                adapter.notifyDataSetChanged();
                listView.getRefreshableView().setSelection(adapter.getCount());
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
//    /**
//     * 消息状态变化观察者
//     */
//    Observer<IMMessage> messageStatusObserver = new Observer<IMMessage>() {
//        @Override
//        public void onEvent(IMMessage imMessage) {
//            if (isMyMessage(imMessage)) {
//            }
//        }
//    };

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
        tipText.setText(team.getType() == TeamTypeEnum.Normal ? getResourceString(R.string.you_have_quit_the_group) : getResourceString(R.string.you_have_quit_the_group));
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
//        FriendDataCache.getInstance().registerFriendDataChangedObserver(friendDataChangedObserver, register);
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

//    FriendDataCache.FriendDataChangedObserver friendDataChangedObserver = new FriendDataCache.FriendDataChangedObserver() {
//        @Override
//        public void onAddedOrUpdatedFriends(List<String> accounts) {
//            adapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onDeletedFriends(List<String> accounts) {
//            adapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onAddUserToBlackList(List<String> account) {
//            adapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onRemoveUserFromBlackList(List<String> account) {
//            adapter.notifyDataSetChanged();
//        }
//    };

    @Override
    protected void onResume() {
        super.onResume();
        registerObservers(true);
        requestTeamInfo();
        NIMClient.getService(MsgService.class).setChattingAccount(sessionId, sessionType);
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.viewPager) != null && findViewById(R.id.viewPager).getVisibility() == View.VISIBLE) {
            findViewById(R.id.viewPager).setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerObservers(false);
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
    }

    @Subscribe
    public void onEvent(ChatVideoBean event) {
        if (event != null) {
            this.courseId = event.getCourseId();
            this.camera = event.getCamera();
            this.board = event.getBoard();
            if (adapter != null) {
                adapter.setOwner(event.getChat_team_owner());
            }
            if (!StringUtils.isNullOrBlanK(event.getName())) {
                setTitle(event.getName());
            } else {
                setTitle(getResources().getString(R.string.team));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerTeamUpdateObserver(false);
        EventBus.getDefault().unregister(this);
    }
}
