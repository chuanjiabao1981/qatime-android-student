package cn.qatime.player.holder;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;

import cn.qatime.player.R;
import cn.qatime.player.adapter.BaseMultiItemFetchLoadAdapter;
import cn.qatime.player.im.helper.TeamNotificationHelper;

public class MsgViewHolderNotification extends MsgViewHolderBase {

    public MsgViewHolderNotification(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    private TextView notificationContentTextView;
    private TextView notificationTextView;

    @Override
    protected int getContentResId() {
        return R.layout.item_message_notification;
    }

    @Override
    protected void inflateContentView() {
        notificationTextView = (TextView) view.findViewById(R.id.message_item_notification_label);
        notificationContentTextView = (TextView) view.findViewById(R.id.message_item_notification_content);
    }

    @Override
    protected void bindContentView() {
        notificationTextView.setText(TeamNotificationHelper.getTeamNotificationText(message, message.getSessionId()));
        notificationTextView.setMovementMethod(LinkMovementMethod.getInstance());
        NotificationAttachment attachment = (NotificationAttachment) message.getAttachment();
        if (attachment instanceof UpdateTeamAttachment) {
            UpdateTeamAttachment updateTeamAttachment = (UpdateTeamAttachment) attachment;
            if (updateTeamAttachment.getUpdatedFields().containsKey(TeamFieldEnum.Announcement)) {
                notificationContentTextView.setVisibility(View.VISIBLE);
                notificationContentTextView.setText(updateTeamAttachment.getUpdatedFields().get(TeamFieldEnum.Announcement).toString());
            } else {
                notificationContentTextView.setVisibility(View.GONE);
            }
        } else {
            notificationContentTextView.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }
}

