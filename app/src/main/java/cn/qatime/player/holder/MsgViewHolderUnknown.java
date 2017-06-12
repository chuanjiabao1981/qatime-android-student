package cn.qatime.player.holder;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import cn.qatime.player.R;
import cn.qatime.player.adapter.BaseMultiItemFetchLoadAdapter;

public class MsgViewHolderUnknown extends MsgViewHolderBase {

    public MsgViewHolderUnknown(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.item_message_unknown;
    }

    @Override
    protected boolean isShowHeadImage() {
        return message.getSessionType() != SessionTypeEnum.ChatRoom;
    }

    @Override
    protected void inflateContentView() {
    }

    @Override
    protected void bindContentView() {
    }
}
