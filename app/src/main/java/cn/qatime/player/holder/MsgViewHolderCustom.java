package cn.qatime.player.holder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.activity.ExclusiveFileDetailActivity;
import cn.qatime.player.activity.HomeWorkDetailActivity;
import cn.qatime.player.activity.QuestionDetailsActivity;
import cn.qatime.player.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * @author lungtify
 * @Time 2017/7/26 17:44
 * @Describe 自定义消息
 */

public class MsgViewHolderCustom extends MsgViewHolderBase {
    private TextView customContentTextView;
    private TextView customTitle;
    private TextView customEvent;
    private View customBackground;
    private TextView customType;
    private CustomAttachment attachment;

    MsgViewHolderCustom(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.item_message_custom;
    }

    @Override
    protected void inflateContentView() {
        attachment = (CustomAttachment) message.getAttachment();
        View layout1 = view.findViewById(R.id.custom_layout1);
        View layout2 = view.findViewById(R.id.custom_layout2);
        setCustomMatchParent();
        String type = attachment.getType();
        if (type.equals("LiveStudio::Homework") || type.equals("LiveStudio::Question") || type.equals("LiveStudio::Answer") || type.equals("Resource::File")) {
            customTitle = (TextView) view.findViewById(R.id.custom_title);
            customEvent = (TextView) view.findViewById(R.id.custom_event);
            customType = (TextView) view.findViewById(R.id.custom_type);
            customBackground = view.findViewById(R.id.custom_background);
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        } else {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
            customContentTextView = (TextView) view.findViewById(R.id.message_item_custom_content);
        }
    }

    @Override
    protected void bindContentView() {
        String type = attachment.getType();
        if (type.equals("LiveStudio::Homework") || type.equals("LiveStudio::Question") || type.equals("LiveStudio::Answer") || type.equals("Resource::File")) {
            if (type.equals("LiveStudio::Homework")) {
                customType.setText("作业");
                customBackground.setBackgroundColor(0xffff5842);
                customTitle.setText(attachment.getTitle());
                customEvent.setText("发布作业");
            } else if (type.equals("LiveStudio::Question")) {
                customType.setText("问题");
                customBackground.setBackgroundColor(0xff069dd5);
                customTitle.setText(attachment.getTitle());
                customEvent.setText("创建提问");
            } else if (type.equals("LiveStudio::Answer")) {
                customType.setText("问题");
                customBackground.setBackgroundColor(0xff069dd5);
                customTitle.setText(attachment.getTitle());
                customEvent.setText("回复提问");
            } else if (type.equals("Resource::File")) {
                customType.setText("课件");
                customBackground.setBackgroundColor(0xffeda719);
                customTitle.setText(attachment.getTitle());
                customEvent.setText("上传课件");
            }

        } else {
            String result = "";
            String event = attachment.getEvent();
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

    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    @Override
    protected void onItemClick() {
        String type = attachment.getType();
        if (type.equals("LiveStudio::Homework")) {
            Intent intent = new Intent(context, HomeWorkDetailActivity.class);
            intent.putExtra("id", attachment.getId());
            context.startActivity(intent);
        } else if (type.equals("LiveStudio::Question")) {
            Intent intent = new Intent(context, QuestionDetailsActivity.class);
            intent.putExtra("id", attachment.getId());
            context.startActivity(intent);
        } else if (type.equals("LiveStudio::Answer")) {
            Intent intent = new Intent(context, QuestionDetailsActivity.class);
            intent.putExtra("id", attachment.getId());
            context.startActivity(intent);
        } else if (type.equals("Resource::File")) {
            Intent intent = new Intent(context, ExclusiveFileDetailActivity.class);
            intent.putExtra("id", attachment.getId());
            intent.putExtra("courseId", attachment.getTaskable_id());
            context.startActivity(intent);
        } else {
        }
    }

    @Override
    protected boolean isMiddleItem() {
        String type = attachment.getType();
        if (type.equals("LiveStudio::Homework") || type.equals("LiveStudio::Question") || type.equals("LiveStudio::Answer") || type.equals("Resource::File")) {
            return false;
        } else {
            return true;
        }
    }
}
