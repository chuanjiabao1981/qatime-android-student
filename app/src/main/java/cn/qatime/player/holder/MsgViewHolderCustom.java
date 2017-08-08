package cn.qatime.player.holder;

import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

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
        if (!message.getContent().contains("/")) return;
        String[] content = message.getContent().split("/");
        String result = "";
        String action = content[1];
        String type = content[0];
        if (action.equals("close") && type.equals("Scheduled")) {
            result = "直播关闭";
        } else if (action.equals("start") && type.equals("Scheduled")) {
            result = "直播开启";
        } else if (action.equals("close") && type.equals("Instant")) {
            result = "老师关闭了互动答疑";
        } else if (action.equals("start") && type.equals("Instant")) {
            result = "老师开启了互动答疑";
        }
        customContentTextView.setText(result);
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}
