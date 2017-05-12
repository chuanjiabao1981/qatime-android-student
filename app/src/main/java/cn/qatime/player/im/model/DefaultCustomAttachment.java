package cn.qatime.player.im.model;

import org.json.JSONException;
import org.json.JSONObject;

public class DefaultCustomAttachment extends CustomAttachment {

    private String content;

    public DefaultCustomAttachment() {
        super(0);
    }

    @Override
    protected void parseData(JSONObject data) {
        content = data.toString();
    }

    @Override
    protected JSONObject packData() throws JSONException {
        return new JSONObject(content);
    }

    public String getContent() {
        return content;
    }
}
