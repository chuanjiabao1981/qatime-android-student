package cn.qatime.player.holder;

import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * @author lungtify
 * @Time 2017/7/26 17:44
 * @Describe 自定义消息
 */

public class MsgViewHolderCustom extends MsgViewHolderBase {
    private TextView customContentTextView;

    MsgViewHolderCustom(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.item_message_custom;
    }

    @Override
    protected void inflateContentView() {
        customContentTextView = (TextView) view.findViewById(R.id.message_item_custom_content);
    }

    @Override
    protected void bindContentView() {
        customContentTextView.setText(message.getContent());
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}
