package cn.qatime.player.holder;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

import cn.qatime.player.R;
import cn.qatime.player.adapter.BaseMultiItemFetchLoadAdapter;
import cn.qatime.player.adapter.MsgAdapter;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.im.cache.TeamDataCache;
import libraryextra.utils.DateUtils;
import libraryextra.utils.StringUtils;

/**
 * 会话窗口消息列表项的ViewHolder基类，负责每个消息项的外层框架，包括头像，昵称，发送/接收进度条，重发按钮等。<br>
 * 具体的消息展示项可继承该基类，然后完成具体消息内容展示即可。
 */
public abstract class MsgViewHolderBase extends RecyclerViewHolder<BaseMultiItemFetchLoadAdapter, BaseViewHolder, IMMessage> {

    MsgViewHolderBase(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
        this.adapter = adapter;
    }

    // basic
    protected View view;
    protected Context context;
    protected BaseMultiItemFetchLoadAdapter adapter;

    // data
    protected IMMessage message;

    // view
    View alertButton;
    private TextView timeTextView;
    ProgressBar progressBar;
    private TextView nameTextView;
    private FrameLayout contentContainer;
    private LinearLayout nameContainer;
//    protected TextView readReceiptTextView;

    private ImageView avatarLeft;
    private ImageView avatarRight;

//    private ImageView nameIconView;

    // contentContainerView的默认长按事件。如果子类需要不同的处理，可覆盖onItemLongClick方法
    // 但如果某些子控件会拦截触摸消息，导致contentContainer收不到长按事件，子控件也可在inflate时重新设置
    View.OnLongClickListener longClickListener;

    /// -- 以下接口可由子类覆盖或实现
    // 返回具体消息类型内容展示区域的layout res id
    abstract protected int getContentResId();

    // 在该接口中根据layout对各控件成员变量赋值
    abstract protected void inflateContentView();

    // 将消息数据项与内容的view进行绑定
    abstract protected void bindContentView();

    // 内容区域点击事件响应处理。
    protected void onItemClick() {
    }

    // 内容区域长按事件响应处理。该接口的优先级比adapter中有长按事件的处理监听高，当该接口返回为true时，adapter的长按事件监听不会被调用到。
    private boolean onItemLongClick() {
        return false;
    }

    // 当是接收到的消息时，内容区域背景的drawable id
    protected int leftBackground() {
        return R.drawable.chatfrom_bg_normal;
    }

    // 当是发送出去的消息时，内容区域背景的drawable id
    protected int rightBackground() {
        return R.drawable.chatto_bg_normal;
    }

    // 返回该消息是不是居中显示
    protected boolean isMiddleItem() {
        return false;
    }

    // 是否显示头像，默认为显示
    protected boolean isShowHeadImage() {
        return true;
    }

//    // 是否显示气泡背景，默认为显示
//    private boolean isShowBubble() {
//        return true;
//    }

    /// -- 以下接口可由子类调用
    final MsgAdapter getMsgAdapter() {
        return (MsgAdapter) adapter;
    }

    /**
     * 下载附件/缩略图
     */
    void downloadAttachment() {
        if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment)
            NIMClient.getService(MsgService.class).downloadAttachment(message, true);
    }

    // 设置FrameLayout子控件的gravity参数
    final void setGravity(View view, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = gravity;
    }

    // 设置控件的长宽
    protected void setLayoutParams(int width, int height, View... views) {
        for (View view : views) {
            ViewGroup.LayoutParams maskParams = view.getLayoutParams();
            maskParams.width = width;
            maskParams.height = height;
            view.setLayoutParams(maskParams);
        }
    }

    // 根据layout id查找对应的控件
    protected <T extends View> T findViewById(int id) {
        return (T) view.findViewById(id);
    }

    // 判断消息方向，是否是接收到的消息
    boolean isReceivedMessage() {
        return message.getDirect() == MsgDirectionEnum.In;
    }

    /// -- 以下是基类实现代码
    @Override
    public void convert(BaseViewHolder holder, IMMessage data, int position, boolean isScrolling) {
        view = holder.getConvertView();
        context = holder.getContext();
        message = data;

        inflate();
        refresh();
    }

    protected final void inflate() {
        timeTextView = findViewById(R.id.message_item_time);
        avatarLeft = findViewById(R.id.message_item_portrait_left);
        avatarRight = findViewById(R.id.message_item_portrait_right);
        alertButton = findViewById(R.id.message_item_alert);
        progressBar = findViewById(R.id.message_item_progress);
        nameTextView = findViewById(R.id.message_item_nickname);
        contentContainer = findViewById(R.id.message_item_content);
//        nameIconView = findViewById(R.id.message_item_name_icon);
        nameContainer = findViewById(R.id.message_item_name_layout);
//        readReceiptTextView = findViewById(R.id.textViewAlreadyRead);

        // 这里只要inflate出来后加入一次即可
        if (contentContainer.getChildCount() == 0) {
            View.inflate(view.getContext(), getContentResId(), contentContainer);
        }
        inflateContentView();
    }

    private void refresh() {
        setHeadImageView();
        setNameTextView();
        setTimeTextView();
        setStatus();
        setOnClickListener();
        setLongClickListener();
        setContent();
//        setReadReceipt();

        bindContentView();
    }

    /**
     * 设置时间显示
     */
    private void setTimeTextView() {
        if (isMiddleItem()) {
            nameContainer.setVisibility(View.GONE);
        } else {
            nameContainer.setVisibility(View.VISIBLE);
            if (isReceivedMessage()) {
                nameContainer.setGravity(Gravity.LEFT);
            } else {
                nameContainer.setGravity(Gravity.RIGHT);
            }
            int index = isReceivedMessage() ? 0 : 1;
            if (nameContainer.getChildAt(index) != nameTextView) {
                nameContainer.removeView(nameTextView);
                nameContainer.addView(nameTextView, index);
            }
            String text = DateUtils.getMMddHHmmss(message.getTime());
            timeTextView.setText(text);
        }
    }

    /**
     * 设置消息发送状态
     */
    private void setStatus() {
        MsgStatusEnum status = message.getStatus();
        switch (status) {
            case fail:
                progressBar.setVisibility(View.GONE);
                alertButton.setVisibility(View.VISIBLE);
                break;
            case sending:
                progressBar.setVisibility(View.VISIBLE);
                alertButton.setVisibility(View.GONE);
                break;
            default:
                progressBar.setVisibility(View.GONE);
                alertButton.setVisibility(View.GONE);
                break;
        }
    }


    private void setOnClickListener() {
        // 重发/重收按钮响应事件
        if (getMsgAdapter().getEventListener() != null) {
            alertButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getMsgAdapter().getEventListener().onFailedBtnClick(message);
                }
            });
        }

        // 内容区域点击事件响应， 相当于点击了整项
        contentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });

    }

    /**
     * item长按事件监听
     */
    private void setLongClickListener() {
        longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 优先派发给自己处理，
                if (!onItemLongClick()) {
                    if (getMsgAdapter().getEventListener() != null) {
                        getMsgAdapter().getEventListener().onViewHolderLongClick(contentContainer, view, message);
                        return true;
                    }
                }
                return false;
            }
        };
        // 消息长按事件响应处理
        contentContainer.setOnLongClickListener(longClickListener);
    }

    private void setNameTextView() {
        if (message.getSessionType() == SessionTypeEnum.Team && !isMiddleItem()) {
            if (isReceivedMessage()) {
                nameTextView.setVisibility(View.VISIBLE);
                String name = TeamDataCache.getInstance().getTeamMemberDisplayName(message.getSessionId(), message.getFromAccount());
                String owner = getMsgAdapter().getOwner();
                nameTextView.setTextColor(0xff333333);
                if (!StringUtils.isNullOrBlanK(owner)) {
                    if (owner.equals(message.getFromAccount())) {
                        name = name + "(" + context.getString(R.string.teacher_translate) + ")";
                        nameTextView.setTextColor(0xffff5842);
                    }
                }
                nameTextView.setText(name);
            } else {
                nameTextView.setVisibility(View.GONE);
            }
        }
    }

    private void setContent() {
        LinearLayout bodyContainer = (LinearLayout) view.findViewById(R.id.message_item_body);

        // 调整container的位置
        int index = isReceivedMessage() ? 0 : 2;
        if (bodyContainer.getChildAt(index) != contentContainer) {
            bodyContainer.removeView(contentContainer);
            bodyContainer.addView(contentContainer, index);
        }

        if (isMiddleItem()) {
            setGravity(bodyContainer, Gravity.CENTER);
        } else {
            if (isReceivedMessage()) {
                setGravity(bodyContainer, Gravity.LEFT);
                contentContainer.setBackgroundResource(leftBackground());
            } else {
                setGravity(bodyContainer, Gravity.RIGHT);
                contentContainer.setBackgroundResource(rightBackground());
            }
        }
    }

    protected void setCustomMatchParent() {
        LinearLayout bodyContainer = (LinearLayout) view.findViewById(R.id.message_item_body);
        ViewGroup.LayoutParams layoutParams1 = contentContainer.getLayoutParams();
        layoutParams1.width = ViewGroup.LayoutParams.MATCH_PARENT;
        ViewGroup.LayoutParams layoutParams2 = bodyContainer.getLayoutParams();
        layoutParams2.width = ViewGroup.LayoutParams.MATCH_PARENT;
    }

    private void setHeadImageView() {
        ImageView show = isReceivedMessage() ? avatarLeft : avatarRight;
        ImageView hide = isReceivedMessage() ? avatarRight : avatarLeft;
        hide.setVisibility(View.INVISIBLE);
        if (!isShowHeadImage()) {
            show.setVisibility(View.INVISIBLE);
            return;
        }
        if (isMiddleItem()) {
            show.setVisibility(View.INVISIBLE);
        } else {
            show.setVisibility(View.VISIBLE);
            UserInfoProvider.UserInfo userinfo = BaseApplication.getInstance().getUserInfoProvide().getUserInfo(message.getFromAccount());
            if (userinfo != null)
                Glide.with(context).load(userinfo.getAvatar()).placeholder(R.mipmap.head_default).crossFade().dontAnimate().into(show);
        }

    }
}
