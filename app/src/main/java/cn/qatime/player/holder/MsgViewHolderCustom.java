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
        CustomAttachment attachment = (CustomAttachment) message.getAttachment();
        String result = "";
        String event = attachment.getEvent();
        String type = attachment.getType();
        if (event.equals("close") && type.equals("LiveStudio::ScheduledLesson")) {
            result = "直播关闭";
        } else if (event.equals("start") && type.equals("LiveStudio::ScheduledLesson")) {
            result = "直播开启";
        } else if (event.equals("close") && type.equals("LiveStudio::InstantLesson")) {
            result = "老师关闭了互动答疑";
        } else if (event.equals("start") && type.equals("LiveStudio::InstantLesson")) {
            result = "老师开启了互动答疑";
        }
        customContentTextView.setText(result);
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}
