package cn.qatime.player.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nimlib.p.b;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum;
import com.netease.nimlib.sdk.team.model.MemberChangeAttachment;
import com.netease.nimlib.sdk.team.model.MuteMemberAttachment;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.WatchMessagePictureActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.utils.ExpressionUtil;
import cn.qatime.player.view.GifDrawable;
import libraryextra.utils.DateUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2016/9/22 11:18
 * @Describe
 */
public class MessageAdapter extends BaseAdapter {
    private static ThreadLocal<String> teamId = new ThreadLocal<>();
    private final List<IMMessage> items;
    private final Context context;
    private final HashMap<String, Float> progresses;
    private final EventListener listener;
    private int UNKNOWN = -1;
    private int NOTIFICATION = 0;
    private int TEXT = 1;
    private int IMAGE = 2;
    private Hashtable<Integer, GifDrawable> cache = new Hashtable<>();
    private String owner;

    public interface EventListener {
        void resendMessage(IMMessage message);
    }

    public MessageAdapter(Context context, List<IMMessage> items, EventListener listener) {
        this.context = context;
        this.items = items;
        progresses = new HashMap<>();
        this.listener = listener;
    }


    public void setOwner(String owner) {
        this.owner = owner;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public IMMessage getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getMsgType() == MsgTypeEnum.notification) {
            return NOTIFICATION;
        } else if (getItem(position).getMsgType() == MsgTypeEnum.text) {
            return TEXT;
        } else if (getItem(position).getMsgType() == MsgTypeEnum.image) {
            return IMAGE;
        } else {
            return UNKNOWN;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IMMessage item = getItem(position);
        if (getItemViewType(position) == NOTIFICATION) {//tip
            return tipItem(convertView, item);
        } else if (getItemViewType(position) == TEXT) {//文本
            return textItem(convertView, item);
        } else if (getItemViewType(position) == IMAGE) {//图片
            return imageItem(convertView, item);
        } else if (getItemViewType(position) == UNKNOWN) {//未知类型  显示不支持的类型
            return tipItem(convertView, null);
        }
        return null;
    }

    private View tipItem(View convertView, IMMessage item) {
        NotifyHolder notifyHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_notify_message, null);
            notifyHolder = new NotifyHolder(convertView);
            convertView.setTag(notifyHolder);
        } else {
            notifyHolder = (NotifyHolder) convertView.getTag();
        }
        if (item != null) {
            teamId.set(item.getSessionId());
            notifyHolder.notify.setText(buildNotification(item.getSessionId(), item.getFromAccount(), (NotificationAttachment) item.getAttachment()));
            teamId.set(null);
        } else {
            notifyHolder.notify.setText("不支持的类型");
        }
        return convertView;
    }

    private View imageItem(View convertView, IMMessage item) {
        ImageHolder imageHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_image_message, null);
            imageHolder = new ImageHolder(convertView);
            convertView.setTag(imageHolder);
        } else {
            imageHolder = (ImageHolder) convertView.getTag();
        }
        imageHolder.setItem(item);
        return convertView;
    }

    private View textItem(View convertView, IMMessage item) {
        final TextHolder textHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_text_message, null);
            textHolder = new TextHolder(convertView);
            convertView.setTag(textHolder);
        } else {
            textHolder = (TextHolder) convertView.getTag();
        }

        if (isReceivedMessage(item)) {
            if (!StringUtils.isNullOrBlanK(owner)) {
                if (owner.equals(item.getFromAccount())) {
                    textHolder.othername.setText(item.getFromNick() + "(老师)");
                    textHolder.othername.setTextColor(0xffbe0b0b);
                } else {
                    textHolder.othername.setText(item.getFromNick());
                    textHolder.othername.setTextColor(0xff333333);
                }
            } else {
                textHolder.othername.setText(item.getFromNick());
            }
            textHolder.right.setVisibility(View.GONE);
            textHolder.left.setVisibility(View.VISIBLE);
            if (BaseApplication.getUserInfoProvide() != null) {
                UserInfoProvider.UserInfo userinfo = BaseApplication.getUserInfoProvide().getUserInfo(item.getFromAccount());
                if (userinfo != null)
                    Glide.with(context).load(userinfo.getAvatar()).placeholder(R.mipmap.head_default).crossFade().dontAnimate().into((ImageView) textHolder.otherhead);
            }
            textHolder.othercontent.setText(ExpressionUtil.getExpressionString(
                    context, item.getContent(), ExpressionUtil.emoji, cache, new GifDrawable.UpdateListener() {
                        @Override
                        public void update() {
                            textHolder.othercontent.postInvalidateDelayed(100);
                        }
                    }));
            textHolder.othertime.setText(DateUtils.getTimeShowString(item.getTime(), false));
        } else {
            textHolder.right.setVisibility(View.VISIBLE);
            textHolder.left.setVisibility(View.GONE);
            if (BaseApplication.getUserInfoProvide() != null) {
                UserInfoProvider.UserInfo userinfo = BaseApplication.getUserInfoProvide().getUserInfo(item.getFromAccount());
                if (userinfo != null)
                    Glide.with(context).load(userinfo.getAvatar()).placeholder(R.mipmap.head_default).crossFade().dontAnimate().into((ImageView) textHolder.headmine);
            }
            textHolder.timemine.setText(DateUtils.getTimeShowString(item.getTime(), false));
            textHolder.contentmine.setText(ExpressionUtil.getExpressionString(
                    context, item.getContent(), ExpressionUtil.emoji, cache, new GifDrawable.UpdateListener() {
                        @Override
                        public void update() {
                            textHolder.contentmine.postInvalidateDelayed(100);
                        }
                    }));
        }
        return convertView;
    }

    private class NotifyHolder {
        public final TextView notify;
        final View root;

        NotifyHolder(View root) {
            notify = (TextView) root.findViewById(R.id.notify);
            this.root = root;
        }
    }

    private class TextHolder {
        final ImageView otherhead;
        final TextView othername;
        final TextView othertime;
        final TextView othercontent;
        public final LinearLayout left;
        final TextView timemine;
        final TextView contentmine;
        final ImageView headmine;
        public final LinearLayout right;
        final View root;

        TextHolder(View root) {
            otherhead = (ImageView) root.findViewById(R.id.other_head);
            othername = (TextView) root.findViewById(R.id.other_name);
            othertime = (TextView) root.findViewById(R.id.other_time);
            othercontent = (TextView) root.findViewById(R.id.other_content);
            left = (LinearLayout) root.findViewById(R.id.left);
            timemine = (TextView) root.findViewById(R.id.time_mine);
            contentmine = (TextView) root.findViewById(R.id.content_mine);
            headmine = (ImageView) root.findViewById(R.id.head_mine);
            right = (LinearLayout) root.findViewById(R.id.right);
            this.root = root;
        }
    }

    public class ImageHolder {
        final ImageView otherhead;
        final TextView othername;
        final TextView othertime;
        final ImageView otherimage;
        final RelativeLayout othercontent;
        public final LinearLayout left;
        final TextView timemine;
        final ImageView imagemine;
        private ImageView otheralert;
        private ImageView alertmine;
        ProgressBar progressmine;
        ProgressBar otherprogress;
        final RelativeLayout contentmine;
        final ImageView headmine;
        public final LinearLayout right;
        final View root;
        private IMMessage message;

        ImageHolder(View root) {
            otherhead = (ImageView) root.findViewById(R.id.other_head);
            othername = (TextView) root.findViewById(R.id.other_name);
            othertime = (TextView) root.findViewById(R.id.other_time);
            otherimage = (ImageView) root.findViewById(R.id.other_image);
            othercontent = (RelativeLayout) root.findViewById(R.id.other_content);
            left = (LinearLayout) root.findViewById(R.id.left);
            timemine = (TextView) root.findViewById(R.id.time_mine);
            imagemine = (ImageView) root.findViewById(R.id.image_mine);
            contentmine = (RelativeLayout) root.findViewById(R.id.content_mine);
            headmine = (ImageView) root.findViewById(R.id.head_mine);
            right = (LinearLayout) root.findViewById(R.id.right);
            progressmine = (ProgressBar) root.findViewById(R.id.progress_mine);
            otherprogress = (ProgressBar) root.findViewById(R.id.progress_other);
            otheralert = (ImageView) root.findViewById(R.id.other_alert);
            alertmine = (ImageView) root.findViewById(R.id.alert_mine);
            this.root = root;

            int width = ScreenUtils.getScreenWidth(context) / 3;
            ViewGroup.LayoutParams otherImageParams = otherimage.getLayoutParams();
            otherImageParams.height = width;
            otherImageParams.width = width;
            otherimage.setLayoutParams(otherImageParams);
            otheralert.setLayoutParams(otherImageParams);
            ViewGroup.LayoutParams otherProgressParams = otherprogress.getLayoutParams();
            otherImageParams.width = width;
            otherprogress.setLayoutParams(otherProgressParams);

            ViewGroup.LayoutParams imagemineParams = imagemine.getLayoutParams();
            imagemineParams.height = width;
            imagemineParams.width = width;
            imagemine.setLayoutParams(imagemineParams);
            alertmine.setLayoutParams(imagemineParams);

            ViewGroup.LayoutParams progressmineParams = progressmine.getLayoutParams();
            progressmineParams.width = width;
            progressmine.setLayoutParams(progressmineParams);

            otherimage.setOnClickListener(new ItemClick());
            imagemine.setOnClickListener(new ItemClick());

            otheralert.setOnClickListener(new AlertClick());
            alertmine.setOnClickListener(new AlertClick());
        }

        public void refresh() {
            if (message == null) {
                return;
            }
            FileAttachment msgAttachment = (FileAttachment) message.getAttachment();
            String path = msgAttachment.getPath();
            String thumbPath = msgAttachment.getThumbPath();

            if (isReceivedMessage(message)) {
                right.setVisibility(View.GONE);
                left.setVisibility(View.VISIBLE);
                if (!StringUtils.isNullOrBlanK(owner)) {
                    if (owner.equals(message.getFromAccount())) {
                        othername.setText(message.getFromNick() + "(老师)");
                        othername.setTextColor(0xffbe0b0b);
                    } else {
                        othername.setText(message.getFromNick());
                        othername.setTextColor(0xff333333);
                    }
                } else {
                    othername.setText(message.getFromNick());
                }
                if (BaseApplication.getUserInfoProvide() != null) {
                    UserInfoProvider.UserInfo userinfo = BaseApplication.getUserInfoProvide().getUserInfo(message.getFromAccount());
                    if (userinfo != null)
                        Glide.with(context).load(userinfo.getAvatar()).placeholder(R.mipmap.head_default).crossFade().dontAnimate().into((ImageView) otherhead);
                }
                othertime.setText(DateUtils.getTimeShowString(message.getTime(), false));

                if (!TextUtils.isEmpty(thumbPath)) {
                    loadThumbnailImage(thumbPath, otherimage);
                } else if (!TextUtils.isEmpty(path)) {
                    loadThumbnailImage(path, otherimage);
                } else {
                    loadThumbnailImage(null, otherimage);
                    if (message.getAttachStatus() == AttachStatusEnum.transferred || message.getAttachStatus() == AttachStatusEnum.def) {
                        downloadAttachment();
                    }
                }
            } else {
                right.setVisibility(View.VISIBLE);
                left.setVisibility(View.GONE);

                if (BaseApplication.getUserInfoProvide() != null) {
                    UserInfoProvider.UserInfo userinfo = BaseApplication.getUserInfoProvide().getUserInfo(message.getFromAccount());
                    if (userinfo != null)
                        Glide.with(context).load(userinfo.getAvatar()).placeholder(R.mipmap.head_default).crossFade().dontAnimate().into((ImageView) headmine);
                }
                timemine.setText(DateUtils.getTimeShowString(message.getTime(), false));

                if (!TextUtils.isEmpty(thumbPath)) {
                    loadThumbnailImage(thumbPath, imagemine);
                } else if (!TextUtils.isEmpty(path)) {
                    loadThumbnailImage(path, imagemine);
                } else {
                    loadThumbnailImage(null, imagemine);
                    if (message.getAttachStatus() == AttachStatusEnum.transferred || message.getAttachStatus() == AttachStatusEnum.def) {
                        downloadAttachment();
                    }
                }
            }
            ProgressBar progressCover;
            View alertButton;
            if (isReceivedMessage(message)) {
                progressCover = otherprogress;
                alertButton = otheralert;
            } else {
                progressCover = progressmine;
                alertButton = alertmine;
            }
            setStatus(progressCover, alertButton);
            refreshStatus(progressCover, alertButton);
        }


        /**
         * 设置消息发送状态
         */
        private void setStatus(ProgressBar progressCover, View alertButton) {
            MsgStatusEnum status = message.getStatus();
            switch (status) {
                case fail:
                    progressCover.setVisibility(View.GONE);
                    alertButton.setVisibility(View.VISIBLE);
                    break;
                case sending:
                    progressCover.setVisibility(View.VISIBLE);
                    alertButton.setVisibility(View.GONE);
                    break;
                default:
                    progressCover.setVisibility(View.GONE);
                    alertButton.setVisibility(View.GONE);
                    break;
            }
        }

        private void refreshStatus(ProgressBar progressCover, View alertButton) {//上传进度

            FileAttachment attachment = (FileAttachment) message.getAttachment();
            if (TextUtils.isEmpty(attachment.getPath()) && TextUtils.isEmpty(attachment.getThumbPath())) {
                if (message.getAttachStatus() == AttachStatusEnum.fail || message.getStatus() == MsgStatusEnum.fail) {
                    alertButton.setVisibility(View.VISIBLE);
                } else {
                    alertButton.setVisibility(View.GONE);
                }
            }
            if (message.getStatus() == MsgStatusEnum.sending || (isReceivedMessage(message) && message.getAttachStatus() == AttachStatusEnum.transferring)) {
                progressCover.setVisibility(View.VISIBLE);
                progressCover.setMax(100);
                progressCover.setProgress((int) (getProgress(message) * 100));
            } else {
                progressCover.setVisibility(View.GONE);
            }
        }

        /**
         * 下载附件/缩略图
         */
        private void downloadAttachment() {
            if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment)
                NIMClient.getService(MsgService.class).downloadAttachment(message, true);
        }

        /**
         * 加载图片
         *
         * @param thumbPath
         * @param image
         */
        private void loadThumbnailImage(String thumbPath, ImageView image) {
            Glide.with(context).load(thumbPath).crossFade().placeholder(R.mipmap.message_error_image).centerCrop().into(image);
        }

        public void setItem(IMMessage item) {
            this.message = item;
            refresh();
        }

        /**
         * 图片点击放大
         */
        private class ItemClick implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WatchMessagePictureActivity.class);
                intent.putExtra("message", message);
                context.startActivity(intent);
            }
        }

        /**
         * 失败重试
         */
        private class AlertClick implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                if (message.getDirect() == MsgDirectionEnum.Out) {
                    // 发出的消息，如果是发送失败，直接重发，否则有可能是漫游到的多媒体消息，但文件下载
                    if (message.getStatus() == MsgStatusEnum.fail) {
                        resendMessage(message); // 重发
                    } else {
                        if (message.getAttachment() instanceof FileAttachment) {
                            FileAttachment attachment = (FileAttachment) message.getAttachment();
                            if (TextUtils.isEmpty(attachment.getPath())
                                    && TextUtils.isEmpty(attachment.getThumbPath())) {
                                showReDownload(message);
                            }
                        } else {
                            resendMessage(message);
                        }
                    }
                } else {
                    showReDownload(message);
                }
            }
        }
    }

    /**
     * 重新下载
     *
     * @param message
     */
    private void showReDownload(IMMessage message) {
        if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment)
            NIMClient.getService(MsgService.class).downloadAttachment(message, true);
    }

    /**
     * 重发消息
     *
     * @param message
     */
    private void resendMessage(IMMessage message) {
        if (listener != null) {
            listener.resendMessage(message);
        }
    }


    private float getProgress(IMMessage message) {
        Float progress = progresses.get(message.getUuid());
        return progress == null ? 0 : progress;
    }

    public void putProgress(IMMessage message, float progress) {
        progresses.put(message.getUuid(), progress);
    }

    private String buildNotification(String tid, String fromAccount, NotificationAttachment attachment) {
        String text;
        switch (attachment.getType()) {
            case InviteMember:
                text = buildInviteMemberNotification(((MemberChangeAttachment) attachment), fromAccount);
                break;
            case KickMember:
                text = buildKickMemberNotification(((MemberChangeAttachment) attachment));
                break;
            case LeaveTeam:
                text = buildLeaveTeamNotification(fromAccount);
                break;
            case DismissTeam:
                text = buildDismissTeamNotification(fromAccount);
                break;
            case UpdateTeam:
                text = buildUpdateTeamNotification(tid, fromAccount, (UpdateTeamAttachment) attachment);
                break;
            case PassTeamApply:
                text = buildManagerPassTeamApplyNotification((MemberChangeAttachment) attachment);
                break;
            case TransferOwner:
                text = buildTransferOwnerNotification(fromAccount, (MemberChangeAttachment) attachment);
                break;
            case AddTeamManager:
                text = buildAddTeamManagerNotification((MemberChangeAttachment) attachment);
                break;
            case RemoveTeamManager:
                text = buildRemoveTeamManagerNotification((MemberChangeAttachment) attachment);
                break;
            case AcceptInvite:
                text = buildAcceptInviteNotification(fromAccount, (MemberChangeAttachment) attachment);
                break;
            case MuteTeamMember:
                text = buildMuteTeamNotification((MuteMemberAttachment) attachment);
                break;
            default:
                text = getTeamMemberDisplayName(fromAccount) + ": unknown message";
                break;
        }

        return text;
    }

    private String getTeamMemberDisplayName(String account) {
        return TeamDataCache.getInstance().getTeamMemberDisplayNameYou(teamId.get(), account);
    }

    private String buildMemberListString(List<String> members, String fromAccount) {
        StringBuilder sb = new StringBuilder();
        for (String account : members) {
            if (!TextUtils.isEmpty(fromAccount) && fromAccount.equals(account)) {
                continue;
            }
            sb.append(getTeamMemberDisplayName(account));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private String buildInviteMemberNotification(MemberChangeAttachment a, String fromAccount) {
        return buildMemberListString(a.getTargets(), fromAccount) + " 加入了本班";
    }

    private String buildKickMemberNotification(MemberChangeAttachment a) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildMemberListString(a.getTargets(), null));
        Team team = TeamDataCache.getInstance().getTeamById(teamId.get());
        if (team.getType() == TeamTypeEnum.Advanced) {
            sb.append(" 已被移出群");
        } else {
            sb.append(" 已被移出讨论组");
        }
        return sb.toString();
    }

    private String buildLeaveTeamNotification(String fromAccount) {
        String tip;
        Team team = TeamDataCache.getInstance().getTeamById(teamId.get());
        if (team.getType() == TeamTypeEnum.Advanced) {
            tip = " 离开了群";
        } else {
            tip = " 离开了讨论组";
        }
        return getTeamMemberDisplayName(fromAccount) + tip;
    }

    private String buildDismissTeamNotification(String fromAccount) {
        return getTeamMemberDisplayName(fromAccount) + " 解散了群";
    }

    private String buildUpdateTeamNotification(String tid, String account, UpdateTeamAttachment a) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<TeamFieldEnum, Object> field : a.getUpdatedFields().entrySet()) {
            if (field.getKey() == TeamFieldEnum.Name) {
                sb.append("名称被更新为 ").append(field.getValue());
            } else if (field.getKey() == TeamFieldEnum.Introduce) {
                sb.append("群介绍被更新为 ").append(field.getValue());
            } else if (field.getKey() == TeamFieldEnum.Announcement) {
                sb.append(TeamDataCache.getInstance().getTeamMemberDisplayNameYou(tid, account)).append(" 修改了群公告");
            } else if (field.getKey() == TeamFieldEnum.VerifyType) {
                VerifyTypeEnum type = (VerifyTypeEnum) field.getValue();
                String authen = "群身份验证权限更新为";
                if (type == VerifyTypeEnum.Free) {
                    sb.append(authen).append(context.getString(R.string.team_allow_anyone_join));
                } else if (type == VerifyTypeEnum.Apply) {
                    sb.append(authen).append(context.getString(R.string.team_need_authentication));
                } else {
                    sb.append(authen).append(context.getString(R.string.team_not_allow_anyone_join));
                }
            } else if (field.getKey() == TeamFieldEnum.Extension) {
                sb.append("群扩展字段被更新为 ").append(field.getValue());
            } else if (field.getKey() == TeamFieldEnum.Ext_Server) {
                sb.append("群扩展字段(服务器)被更新为 ").append(field.getValue());
            } else if (field.getKey() == TeamFieldEnum.ICON) {
                sb.append("群头像已更新");
            } else if (field.getKey() == TeamFieldEnum.InviteMode) {
                sb.append("群邀请他人权限被更新为 ").append(field.getValue());
            } else if (field.getKey() == TeamFieldEnum.TeamUpdateMode) {
                sb.append("群资料修改权限被更新为 ").append(field.getValue());
            } else if (field.getKey() == TeamFieldEnum.BeInviteMode) {
                sb.append("群被邀请人身份验证权限被更新为 ").append(field.getValue());
            } else if (field.getKey() == TeamFieldEnum.TeamExtensionUpdateMode) {
                sb.append("群扩展字段修改权限被更新为 ").append(field.getValue());
            } else {
                sb.append("群").append(field.getKey()).append("被更新为 ").append(field.getValue());
            }
            sb.append("\r\n");
        }
        if (sb.length() < 2) {
            return "未知通知";
        }
        return sb.delete(sb.length() - 2, sb.length()).toString();
    }

    private String buildManagerPassTeamApplyNotification(MemberChangeAttachment a) {
        return "管理员通过用户 " + buildMemberListString(a.getTargets(), null) + " 的入群申请";
    }

    private String buildTransferOwnerNotification(String from, MemberChangeAttachment a) {
        return getTeamMemberDisplayName(from) + " 将群转移给 " + buildMemberListString(a.getTargets(), null);
    }

    private String buildAddTeamManagerNotification(MemberChangeAttachment a) {
        return buildMemberListString(a.getTargets(), null) + " 被任命为管理员";
    }

    private String buildRemoveTeamManagerNotification(MemberChangeAttachment a) {
        return buildMemberListString(a.getTargets(), null) + " 被撤销管理员身份";
    }

    private String buildAcceptInviteNotification(String from, MemberChangeAttachment a) {
        return getTeamMemberDisplayName(from) + " 接受了 " + buildMemberListString(a.getTargets(), null) + " 的入群邀请";
    }

    private String buildMuteTeamNotification(MuteMemberAttachment a) {
        return buildMemberListString(a.getTargets(), null) + "被" + (a.isMute() ? "禁言" : "解除禁言");
    }

    // 判断消息方向，是否是接收到的消息
    private boolean isReceivedMessage(IMMessage message) {
        return message.getDirect() == MsgDirectionEnum.In;
    }

}
