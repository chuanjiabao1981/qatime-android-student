package cn.qatime.player.im.doodle;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.qatime.player.im.cache.ChatRoomMemberCache;
import cn.qatime.player.im.model.MeetingOptCommand;
import cn.qatime.player.im.model.PermissionAttachment;

public class MsgHelper {

    public static MsgHelper getInstance() {
        return InstanceHolder.instance;
    }

    // 发送点对点不推送不支持离线的自定义系统通知
    public void sendP2PCustomNotification(String roomId, int command, String account, List<String> accountList) {
        CustomNotification notification = new CustomNotification();
        notification.setSessionId(account); // 指定接收者
        notification.setSessionType(SessionTypeEnum.P2P);
        CustomNotificationConfig config = new CustomNotificationConfig();
        config.enablePush = false; // 不推送
        notification.setConfig(config);
        notification.setSendToOnlineUserOnly(true); // 不支持离线

        JSONObject data = new JSONObject();
        JSONObject json = null;
        try {
            data.put("command", command);
            data.put("room_id", roomId);
            data.put("uids", accountList);
            json = new JSONObject();
            json.put("data", data);
            json.put("type", 10);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assert json != null;
        notification.setContent(json.toString());

        // 发送自定义通知
        NIMClient.getService(MsgService.class).sendCustomNotification(notification);
    }

    /**
     * ************************************ 单例 ***************************************
     */
    private static class InstanceHolder {
        final static MsgHelper instance = new MsgHelper();
    }
}
