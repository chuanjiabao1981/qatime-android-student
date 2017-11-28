package cn.qatime.player.bean;


import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.io.Serializable;

import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @date 2016/9/6 19:36
 * @Description: 消息列表数据item
 */
public class MessageListBean implements Serializable {
    private String contactId;
    private String recentMessageId;
    private long time;
    private int unreadCount;
    private String defaultIcon;
    private String icon;
    private String courseType;
    private String name;
    private int courseId;
    private boolean mute;
//    private String owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageListBean that = (MessageListBean) o;

        return contactId != null ? contactId.equals(that.contactId) : that.contactId == null;

    }

    public void setDefaultIcon(String defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

    @Override
    public int hashCode() {
        return contactId != null ? contactId.hashCode() : 0;
    }

    public String getIcon() {
        return !StringUtils.isNullOrBlanK(icon) ? icon : (!StringUtils.isNullOrBlanK(defaultIcon) ? defaultIcon : "");
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

//    public String getOwner() {
//        return owner;
//    }

//    public void setOwner(String owner) {
//        this.owner = owner;
//    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setRecentMessageId(String recentMessageId) {
        this.recentMessageId = recentMessageId;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getContactId() {
        return contactId;
    }

    public CharSequence getRecentMessageId() {
        return recentMessageId;
    }

//    public void setMsgStatus(MsgStatusEnum msgStatus) {
//        this.msgStatus = msgStatus;
//    }

    public long getTime() {
        return time;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
