package cn.qatime.player.bean;

import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.netease.neliveplayer.util.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.BaseFetchLoadAdapter;
import cn.qatime.player.adapter.MsgAdapter;
import cn.qatime.player.audio.MessageAudioControl;
import cn.qatime.player.config.UserPreferences;
import cn.qatime.player.im.observer.UserInfoObservable;
import cn.qatime.player.view.listview.ListViewUtil;
import cn.qatime.player.view.loadmore.MsgListFetchLoadMoreView;

/**
 * @author lungtify
 * @Time 2017/2/10 11:52
 * @Describe
 */

public class MessageListPanel {
    private final Container container;
    private final View rootView;
    private RecyclerView messageListView;
    private List<IMMessage> items;
    private MsgAdapter adapter;
    private IMMessage anchor;

    public MessageListPanel(Container container, View rootView) {
        this.container = container;
        this.rootView = rootView;
        init();
    }

    private void init() {
        initListView();
//        this.uiHandler = new Handler();

        registerObservers(true);
    }

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeMsgStatus(messageStatusObserver, register);
        service.observeAttachmentProgress(attachmentProgressObserver, register);
//        service.observeRevokeMessage(revokeMessageObserver, register);
//        if (register) {
//            registerUserInfoObserver();
//        } else {
//            unregisterUserInfoObserver();
//        }

//        MessageListPanelHelper.getInstance().registerObserver(incomingLocalMessageObserver, register);
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

    private void initListView() {
        // RecyclerView
        messageListView = (RecyclerView) rootView.findViewById(R.id.messageListView);
        messageListView.setLayoutManager(new LinearLayoutManager(container.activity));
        messageListView.requestDisallowInterceptTouchEvent(true);
        messageListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    container.proxy.shouldCollapseInputPanel();
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            messageListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        // adapter
        items = new ArrayList<>();
        adapter = new MsgAdapter(messageListView, items);
        adapter.setFetchMoreView(new MsgListFetchLoadMoreView());
        adapter.setLoadMoreView(new MsgListFetchLoadMoreView());
//        adapter.setEventListener(new MsgItemEventListener());
        initFetchLoadListener();
        messageListView.setAdapter(adapter);

    }

    private void initFetchLoadListener() {
        MessageLoader loader = new MessageLoader();
        adapter.setOnFetchMoreListener(loader);
    }

    public void onIncomingMessage(List<IMMessage> messages) {
        boolean needScrollToBottom = isLastMessageVisible();
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
            sortMessages(items);
            adapter.notifyDataSetChanged();
        }

        adapter.updateShowTimeItem(addedListItems, false, true);

        // incoming messages tip
        IMMessage lastMsg = messages.get(messages.size() - 1);
        if (isMyMessage(lastMsg)) {
            if (needScrollToBottom) {
                doScrollToBottom();
            }
//            else if (incomingMsgPrompt != null && lastMsg.getSessionType() != SessionTypeEnum.ChatRoom) {
//                incomingMsgPrompt.show(lastMsg);
//            }
        }
    }

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

    private boolean isLastMessageVisible() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) messageListView.getLayoutManager();
        int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
        return lastVisiblePosition >= adapter.getBottomDataPosition();
    }

    public void setOwner(String owner) {

    }

    public void onResume() {
        setEarPhoneMode(UserPreferences.isEarPhoneModeEnable());
    }

    private void setEarPhoneMode(boolean earPhoneMode) {
        UserPreferences.setEarPhoneModeEnable(earPhoneMode);
        MessageAudioControl.getInstance(container.activity).setEarPhoneModeEnable(earPhoneMode);
    }

    public boolean onBackPressed() {
        return false;
    }

    private class MessageLoader implements BaseFetchLoadAdapter.RequestFetchMoreListener {

        private int LOAD_MESSAGE_COUNT = 20;//聊天加载条数
        // 从服务器拉取消息记录
        private QueryDirectionEnum direction;

        private boolean firstLoad = true;

        MessageLoader() {
            loadFromLocal(QueryDirectionEnum.QUERY_OLD);
        }

        @Override
        public void onFetchMoreRequested() {
            loadFromLocal(QueryDirectionEnum.QUERY_OLD);
        }

        private void loadFromLocal(QueryDirectionEnum direction) {
            this.direction = direction;
            NIMClient.getService(MsgService.class).queryMessageListEx(anchor(), direction, LOAD_MESSAGE_COUNT, true)
                    .setCallback(callback);
        }

        private IMMessage anchor() {
            if (items.size() == 0) {
                return anchor == null ? MessageBuilder.createEmptyMessage(container.account, SessionTypeEnum.Team, 0) : anchor;
            } else {
                int index = (direction == QueryDirectionEnum.QUERY_NEW ? items.size() - 1 : 0);
                return items.get(index);
            }
        }

        private RequestCallback<List<IMMessage>> callback = new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int code, List<IMMessage> messages, Throwable exception) {
                if (code != ResponseCode.RES_SUCCESS || exception != null) {
                    if (direction == QueryDirectionEnum.QUERY_OLD) {
                        adapter.fetchMoreFailed();
                    } else if (direction == QueryDirectionEnum.QUERY_NEW) {
                        adapter.loadMoreFail();
                    }

                    return;
                }
                if (messages != null) {
                    onMessageLoaded(messages);
                }
            }
        };

        /**
         * 历史消息加载处理
         *
         * @param messages
         */
        private void onMessageLoaded(List<IMMessage> messages) {
            if (messages == null) return;

            if (firstLoad && items.size() > 0) {
                // 在第一次加载的过程中又收到了新消息，做一下去重
                for (IMMessage message : messages) {
                    int removeIndex = 0;
                    for (IMMessage item : items) {
                        if (item.isTheSame(message)) {
                            adapter.remove(removeIndex);
                            break;
                        }
                        removeIndex++;
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
            adapter.updateShowTimeItem(items, true, firstLoad); // 更新要显示时间的消息

            // 顶部加载
            int count = messages.size();
            if (count <= 0) {
                adapter.fetchMoreEnd(true);
            } else {
                adapter.fetchMoreComplete(messageListView, messages);
            }

            // 如果是第一次加载，updateShowTimeItem返回的就是lastShowTimeItem
            if (firstLoad) {
                doScrollToBottom();
            }
//            refreshMessageList();
            firstLoad = false;
        }

    }

    private void onMessageStatusChange(IMMessage message) {
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            item.setStatus(message.getStatus());
            item.setAttachStatus(message.getAttachStatus());
            if (
//                    item.getAttachment() instanceof AVChatAttachment ||
                    item.getAttachment() instanceof AudioAttachment) {
                item.setAttachment(message.getAttachment());
            }

            // resend的的情况，可能时间已经变化了，这里要重新检查是否要显示时间
            List<IMMessage> msgList = new ArrayList<>(1);
            msgList.add(message);
            adapter.updateShowTimeItem(msgList, false, true);

            refreshViewHolderByIndex(index);
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

    /**
     * 刷新单条消息
     */
    private void refreshViewHolderByIndex(final int index) {
        container.activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (index < 0) {
                    return;
                }

                adapter.notifyDataItemChanged(index);
            }
        });
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

    private void doScrollToBottom() {
        messageListView.scrollToPosition(adapter.getBottomDataPosition());
    }

    // 刷新消息列表
    public void refreshMessageList() {
        container.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }


    public void onPause() {
        MessageAudioControl.getInstance(container.activity).stopAudio();
    }

    public void onDestroy() {
        registerObservers(false);
    }

    private boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == SessionTypeEnum.Team && message.getSessionId() != null && message.getSessionId().equals(container.account);
    }

    public void scrollToBottom() {
//        uiHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                doScrollToBottom();
//            }
//        }, 200);
    }

}
