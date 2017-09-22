package cn.qatime.player.holder;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

import org.json.JSONException;
import org.json.JSONObject;


public class CustomAttachment implements MsgAttachment {
    private String event;
    private String type;
    private String title;
    private String body;
    private String id;
    private String taskable_id;
    private String taskable_type;

    @Override
    public String toJson(boolean b) {
        return null;
    }

    public void fromJson(JSONObject result) throws JSONException {
        event = result.getString("event");
        type = result.getString("type");
        title = result.getString("title");
        body = result.getString("body");
        taskable_id = result.getString("taskable_id");
        id = result.getString("id");
        taskable_type = result.getString("taskable_type");
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getTaskable_id() {
        return taskable_id;
    }

    public String getTaskable_type() {
        return taskable_type;
    }

    public String getEvent() {
        return event;
    }

    public String getType() {
        return type;
    }
    public String getId() {
        return id;
    }
}
