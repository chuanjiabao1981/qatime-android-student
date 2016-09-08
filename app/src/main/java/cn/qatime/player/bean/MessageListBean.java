package cn.qatime.player.bean;


import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.io.Serializable;

/**
 * @author lungtify
 * @date 2016/9/6 19:36
 * @Description: 消息列表数据item
 */
public class MessageListBean implements Serializable{
    private String contactId;
    private SessionTypeEnum sessionType;
    private String recentMessageId;
    private MsgStatusEnum msgStatus;
    private long time;
    private int unreadCount;
    private String pull_address;
    private String content;
    private int courseId;
    private boolean mute;

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setSessionType(SessionTypeEnum sessionType) {
        this.sessionType = sessionType;
    }

    public void setRecentMessageId(String recentMessageId) {
        this.recentMessageId = recentMessageId;
    }

    public MsgStatusEnum getMsgStatus() {
        return msgStatus;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getPull_address() {
        return pull_address;
    }

    public void setPull_address(String pull_address) {
        this.pull_address = pull_address;
    }

    public String getContactId() {
        return contactId;
    }

    public SessionTypeEnum getSessionType() {
        return sessionType;
    }

    public CharSequence getRecentMessageId() {
        return recentMessageId;
    }

    public void setMsgStatus(MsgStatusEnum msgStatus) {
        this.msgStatus = msgStatus;
    }

    public long getTime() {
        return time;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public boolean isMute() {
        return mute;
    }
}
