package cn.qatime.player.im.cache;


/**
 * 聊天室成员资料缓存
 */
public class ChatRoomMemberCache {

    public static ChatRoomMemberCache getInstance() {
        return InstanceHolder.instance;
    }

    private boolean isRTSOpen = false; // 白板是否启启用


    public boolean isRTSOpen() {
        return isRTSOpen;
    }

    public void setRTSOpen(boolean RTSOpen) {
        isRTSOpen = RTSOpen;
    }


    /**
     * ************************************ 单例 ***************************************
     */
    static class InstanceHolder {
        final static ChatRoomMemberCache instance = new ChatRoomMemberCache();
    }
}
