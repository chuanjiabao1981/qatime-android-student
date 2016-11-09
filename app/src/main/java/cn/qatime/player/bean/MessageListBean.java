package cn.qatime.player.bean;


import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.io.Serializable;

/**
 * @author lungtify
 * @date 2016/9/6 19:36
 * @Description: 消息列表数据item
 */
public class MessageListBean implements Serializable {
    private String contactId;
    private SessionTypeEnum sessionType;
    private String recentMessageId;
    private MsgStatusEnum msgStatus;
    private long time;
    private int unreadCount;
    private String camera;
    private String board;
    private String name;
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

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
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
