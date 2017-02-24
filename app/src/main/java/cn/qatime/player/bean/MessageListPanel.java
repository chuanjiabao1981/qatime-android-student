package cn.qatime.player.bean;

import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
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
import cn.qatime.player.utils.VoiceTrans;
import cn.qatime.player.view.loadmore.MsgListFetchLoadMoreView;
import libraryextra.view.CustomAlertDialog;

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
    private Handler uiHandler;
    private VoiceTrans voiceTrans;

    public MessageListPanel(Container container, View rootView) {
        this.container = container;
        this.rootView = rootView;
        init();
    }

    private void init() {
        initListView();
        this.uiHandler = new Handler();

        registerObservers(true);
    }

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeMsgStatus(messageStatusObserver, register);
        service.observeAttachmentProgress(attachmentProgressObserver, register);
    }

    /**
     * 消息状态变化观察者
     */
    private Observer<IMMessage> messageStatusObserver = new Observer<IMMessage>() {
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
    private Observer<AttachmentProgress> attachmentProgressObserver = new Observer<AttachmentProgress>() {
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
        adapter.setEventListener(new MsgItemEventListener());
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
        adapter.setOwner(owner);
    }

    public void onResume() {
        setEarPhoneMode(UserPreferences.isEarPhoneModeEnable());
    }

    private void setEarPhoneMode(boolean earPhoneMode) {
        UserPreferences.setEarPhoneModeEnable(earPhoneMode);
        MessageAudioControl.getInstance(container.activity).setEarPhoneModeEnable(earPhoneMode);
    }

    public boolean onBackPressed() {
        uiHandler.removeCallbacks(null);
        MessageAudioControl.getInstance(container.activity).stopAudio(); // 界面返回，停止语音播放
        if (voiceTrans != null && voiceTrans.isShow()) {
            voiceTrans.hide();
            return true;
        }
        return false;
    }

    public void onMsgSend(IMMessage message) {
        List<IMMessage> addedListItems = new ArrayList<>(1);
        addedListItems.add(message);
        adapter.updateShowTimeItem(addedListItems, false, true);

        adapter.appendData(message);

        doScrollToBottom();
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
                return MessageBuilder.createEmptyMessage(container.account, SessionTypeEnum.Team, 0);
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

            // 在更新前，先确定一些标记
            List<IMMessage> total = new ArrayList<>();
            total.addAll(items);
            boolean isBottomLoad = direction == QueryDirectionEnum.QUERY_NEW;
            if (isBottomLoad) {
                total.addAll(messages);
            } else {
                total.addAll(0, messages);
            }
            adapter.updateShowTimeItem(total, true, firstLoad); // 更新要显示时间的消息

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

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == SessionTypeEnum.Team && message.getSessionId() != null && message.getSessionId().equals(container.account);
    }

    public void scrollToBottom() {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doScrollToBottom();
            }
        }, 200);
    }

    private class MsgItemEventListener implements MsgAdapter.ViewHolderEventListener {
        @Override
        public boolean onViewHolderLongClick(View clickView, View viewHolderView, IMMessage item) {
            if (container.proxy.isLongClickEnabled()) {
                showLongClickAction(item);
            }
            return false;
        }

        @Override
        public void onFailedBtnClick(IMMessage message) {
            if (message.getDirect() == MsgDirectionEnum.Out) {
                // 发出的消息，如果是发送失败，直接重发，否则有可能是漫游到的多媒体消息，但文件下载
                if (message.getStatus() == MsgStatusEnum.fail) {
                    resendMessage(message); // 重发
                } else {
                    if (message.getAttachment() instanceof FileAttachment) {
                        FileAttachment attachment = (FileAttachment) message.getAttachment();
                        if (TextUtils.isEmpty(attachment.getPath())
                                && TextUtils.isEmpty(attachment.getThumbPath())) {
                            showReDownloadConfirmDlg(message);
                        }
                    } else {
                        resendMessage(message);
                    }
                }
            } else {
                showReDownloadConfirmDlg(message);
            }
        }
    }

    private void showLongClickAction(IMMessage message) {
        CustomAlertDialog alertDialog = new CustomAlertDialog(container.activity);
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);

        prepareDialogItems(message, alertDialog);
        alertDialog.show();
    }

    private void prepareDialogItems(IMMessage selectedItem, CustomAlertDialog alertDialog) {
        MsgTypeEnum msgType = selectedItem.getMsgType();

        MessageAudioControl.getInstance(container.activity).stopAudio();

//        longClickItemVoidToText(selectedItem, alertDialog, msgType);

        longClickItemEarPhoneMode(alertDialog, msgType);

    }

    /**
     * 语音转文字
     */
    private void longClickItemVoidToText(final IMMessage item, CustomAlertDialog alertDialog, MsgTypeEnum msgType) {
        if (msgType != MsgTypeEnum.audio) return;

        if (item.getDirect() == MsgDirectionEnum.In && item.getAttachStatus() != AttachStatusEnum.transferred)
            return;
        if (item.getDirect() == MsgDirectionEnum.Out && item.getAttachStatus() != AttachStatusEnum.transferred)
            return;
        alertDialog.addItem(container.activity.getString(R.string.voice_to_text), new CustomAlertDialog.onSeparateItemClickListener() {

            @Override
            public void onClick() {
                onVoiceToText(item);
            }
        });
    }

    private void onVoiceToText(IMMessage item) {
        if (voiceTrans == null)
            voiceTrans = new VoiceTrans(container.activity);
        voiceTrans.voiceToText(item);
        if (item.getDirect() == MsgDirectionEnum.In && item.getStatus() != MsgStatusEnum.read) {
            item.setStatus(MsgStatusEnum.read);
            NIMClient.getService(MsgService.class).updateIMMessageStatus(item);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 耳机扬声器切换
     */
    private void longClickItemEarPhoneMode(CustomAlertDialog alertDialog, MsgTypeEnum msgType) {
        if (msgType != MsgTypeEnum.audio) return;

        String content = UserPreferences.isEarPhoneModeEnable() ? "切换成扬声器播放" : "切换成听筒播放";
        final String finalContent = content;
        alertDialog.addItem(content, new CustomAlertDialog.onSeparateItemClickListener() {

            @Override
            public void onClick() {
                Toast.makeText(container.activity, finalContent, Toast.LENGTH_SHORT).show();
                setEarPhoneMode(!UserPreferences.isEarPhoneModeEnable());
            }
        });
    }

    // 重新下载(对话框提示)
    private void showReDownloadConfirmDlg(final IMMessage message) {
        if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment)
            NIMClient.getService(MsgService.class).downloadAttachment(message, true);
    }

    // 重发消息到服务器
    private void resendMessage(IMMessage message) {
        // 重置状态为unsent
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            item.setStatus(MsgStatusEnum.sending);
            deleteItem(item, true);
            onMsgSend(item);
        }

        NIMClient.getService(MsgService.class).sendMessage(message, true);
    }

    // 删除消息
    private void deleteItem(IMMessage messageItem, boolean isRelocateTime) {
        NIMClient.getService(MsgService.class).deleteChattingHistory(messageItem);
        adapter.deleteItem(messageItem, isRelocateTime);
    }
}
