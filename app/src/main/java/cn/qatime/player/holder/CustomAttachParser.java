package cn.qatime.player.holder;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser;

import org.json.JSONException;
import org.json.JSONObject;


public class CustomAttachParser implements MsgAttachmentParser {
    @Override
    public MsgAttachment parse(String s) {
        CustomAttachment attachment = new CustomAttachment();
        try {
            attachment.fromJson(new JSONObject(s));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return attachment;
    }
}
