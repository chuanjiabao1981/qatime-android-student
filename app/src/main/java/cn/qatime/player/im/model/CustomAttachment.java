package cn.qatime.player.im.model;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class CustomAttachment implements MsgAttachment {

    protected int type;

    CustomAttachment(int type) {
        this.type = type;
    }

    public void fromJson(JSONObject data) {
        if (data != null) {
            try {
                parseData(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toJson(boolean send) {
        try {
            return CustomAttachParser.packData(type, packData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getType() {
        return type;
    }

    protected abstract void parseData(JSONObject data) throws JSONException;

    protected abstract JSONObject packData() throws JSONException;
}
