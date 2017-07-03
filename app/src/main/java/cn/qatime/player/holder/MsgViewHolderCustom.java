package cn.qatime.player.holder;

import cn.qatime.player.R;
import cn.qatime.player.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * @author lungtify
 * @Time 2017/6/28 16:23
 * @Describe
 */

public class MsgViewHolderCustom extends MsgViewHolderBase {

    MsgViewHolderCustom(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.item_message_custom;
    }

    @Override
    protected void inflateContentView() {

    }

    @Override
    protected void bindContentView() {

    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}
