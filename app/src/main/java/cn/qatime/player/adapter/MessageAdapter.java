package cn.qatime.player.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum;
import com.netease.nimlib.sdk.team.model.MemberChangeAttachment;
import com.netease.nimlib.sdk.team.model.MuteMemberAttachment;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.utils.ExpressionUtil;
import cn.qatime.player.view.GifDrawable;
import libraryextra.utils.DateUtils;

/**
 * @author lungtify
 * @Time 2016/9/22 11:18
 * @Describe
 */
public class MessageAdapter extends BaseAdapter {
    private static ThreadLocal<String> teamId = new ThreadLocal<>();
    private final List<IMMessage> items;
    private final Context context;
    private int NOTIFICATION = 0;
    private int TEXT = 1;
    Hashtable<Integer, GifDrawable> cache = new Hashtable<Integer, GifDrawable>();

    public MessageAdapter(Context context, List<IMMessage> items) {
        this.context = context;
        this.items = items;
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
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getMsgType() == MsgTypeEnum.notification) {
            return NOTIFICATION;
        } else {
            return TEXT;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IMMessage item = getItem(position);
        if (getItemViewType(position) == NOTIFICATION) {
            NotifyHolder notifyHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_notify_message, null);
                notifyHolder = new NotifyHolder(convertView);
                convertView.setTag(notifyHolder);
            } else {
                notifyHolder = (NotifyHolder) convertView.getTag();
            }
            teamId.set(item.getSessionId());
            notifyHolder.notify.setText(buildNotification(item.getSessionId(), item.getFromAccount(), (NotificationAttachment) item.getAttachment()));
            teamId.set(null);
        } else {
            final TextHolder textHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_message, null);
                textHolder = new TextHolder(convertView);
                convertView.setTag(textHolder);
            } else {
                textHolder = (TextHolder) convertView.getTag();
            }

            if (item.getFromAccount().equals(BaseApplication.getAccount())) {
                textHolder.right.setVisibility(View.VISIBLE);
                textHolder.left.setVisibility(View.GONE);
                Glide.with(context).load(BaseApplication.getProfile().getData().getUser().getEx_big_avatar_url()).crossFade().dontAnimate().into((ImageView) textHolder.myhead);
                textHolder.mytime.setText(DateUtils.getTimeShowString(item.getTime(), true));
                textHolder.mycontent.setText(ExpressionUtil.getExpressionString(
                        context, item.getContent(), ExpressionUtil.emoji, cache, new GifDrawable.UpdateListener() {
                            @Override
                            public void update() {
                                textHolder.mycontent.postInvalidateDelayed(100);
                            }
                        }));
            } else {
                textHolder.right.setVisibility(View.GONE);
                textHolder.left.setVisibility(View.VISIBLE);
                Glide.with(context).load(BaseApplication.getUserInfoProvide().getUserInfo(item.getFromAccount()).getAvatar()).placeholder(R.mipmap.head_32).crossFade().dontAnimate().into((ImageView) textHolder.otherhead);

                textHolder.othername.setText(item.getFromNick());
                textHolder.othercontent.setText(ExpressionUtil.getExpressionString(
                        context, item.getContent(), ExpressionUtil.emoji, cache, new GifDrawable.UpdateListener() {
                            @Override
                            public void update() {
                                textHolder.othercontent.postInvalidateDelayed(100);
                            }
                        }));
                textHolder.othertime.setText(DateUtils.getTimeShowString(item.getTime(), false));
            }
        }
        return convertView;
    }

    private class NotifyHolder {
        public final TextView notify;
        public final View root;

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
        final TextView mytime;
        final TextView mycontent;
        final ImageView myhead;
        public final LinearLayout right;
        public final View root;

        TextHolder(View root) {
            otherhead = (ImageView) root.findViewById(R.id.other_head);
            othername = (TextView) root.findViewById(R.id.other_name);
            othertime = (TextView) root.findViewById(R.id.other_time);
            othercontent = (TextView) root.findViewById(R.id.other_content);
            left = (LinearLayout) root.findViewById(R.id.left);
            mytime = (TextView) root.findViewById(R.id.my_time);
            mycontent = (TextView) root.findViewById(R.id.my_content);
            myhead = (ImageView) root.findViewById(R.id.my_head);
            right = (LinearLayout) root.findViewById(R.id.right);
            this.root = root;
        }
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
        //        String selfName = getTeamMemberDisplayName(fromAccount);
//
//        sb.append(selfName);
//        sb.append("邀请 ");
        //        Team team = TeamDataCache.getInstance().getTeamById(teamId.get());

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
}
