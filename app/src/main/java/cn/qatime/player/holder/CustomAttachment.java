package cn.qatime.player.holder;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

import org.json.JSONException;
import org.json.JSONObject;


public class CustomAttachment implements MsgAttachment {
    private String event;
    private String type;

    @Override
    public String toJson(boolean b) {
        return null;
    }

    public void fromJson(JSONObject result) throws JSONException {
        event = result.getString("event");
        type = result.getString("type");
    }

    public String getEvent() {
        return event;
    }

    public String getType() {
        return type;
    }
}
