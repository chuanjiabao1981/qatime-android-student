package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.MessageReceipt;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
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
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;

/**
 * @author luntify
 * @date 2016/8/30 12:25
 * @Description
 */
public class MessageActivity extends BaseActivity {
    private String sessionId;//聊天对象id
    private SessionTypeEnum sessionType;
    private ListView listView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        sessionId = getIntent().getStringExtra("sessionId");
        sessionType = (SessionTypeEnum) getIntent().getSerializableExtra("sessionType");
        initView();

    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list);
        adapter = new CommonAdapter<IMMessage>(this, items, R.layout.item_message) {
            @Override
            public void convert(ViewHolder holder, IMMessage item, int position) {

                if (item.getFromAccount().equals(BaseApplication.getAccount())) {
                    holder.getView(R.id.right).setVisibility(View.VISIBLE);
                    holder.getView(R.id.left).setVisibility(View.GONE);
                    Glide.with(MessageActivity.this).load(BaseApplication.getUserInfoProvide().getUserInfo(item.getFromAccount()).getAvatar()).placeholder(R.mipmap.head_32).crossFade().dontAnimate().into((ImageView) holder.getView(R.id.other_head));
                } else {
                    holder.getView(R.id.right).setVisibility(View.GONE);
                    holder.getView(R.id.left).setVisibility(View.VISIBLE);
                    Glide.with(MessageActivity.this).load(BaseApplication.getProfile().getData().getUser().getChat_account().getIcon()).crossFade().dontAnimate().into((ImageView) holder.getView(R.id.my_head));
                }

                holder.setText(R.id.other_name, item.getFromNick());
                holder.setText(R.id.other_content, item.getContent());
                holder.setText(R.id.other_time, getTime(item.getTime()));
            }
        };
        listView.setAdapter(adapter);
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
//        messageListView.onRefreshStart(AutoRefreshListView.Mode.START);
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
//                        messageListView.onRefreshStart(AutoRefreshListView.Mode.END);
                        NIMClient.getService(MsgService.class).queryMessageListEx(anchor(), direction, LOAD_MESSAGE_COUNT, true)
                                .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
                                    @Override
                                    public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                                        if (code != ResponseCode.RES_SUCCESS || exception != null) {
                                            return;
                                        }
                                        onMessageLoaded(messages);
                                        // scroll to position
//                                        scrollToAnchor(anchor);
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
        int count = messages.size();

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
            result.add(message);
        }
        if (direction == QueryDirectionEnum.QUERY_NEW) {
            items.addAll(result);
        } else {
            items.addAll(0, result);
        }

        adapter.notifyDataSetChanged();
        listView.setSelection(items.size() - 1);
//        messageListView.onRefreshComplete(count, LOAD_MESSAGE_COUNT, true);
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
                if (isMyMessage(message)) {
                    items.add(message);
                    addedListItems.add(message);
                    needRefresh = true;
                }
            }
            if (needRefresh) {
                adapter.notifyDataSetChanged();
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
//                onMessageStatusChange(message);
            }
        }
    };

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == sessionType
                && message.getSessionId() != null
                && message.getSessionId().equals(sessionId);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerObservers(true);
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
    }
}
