package cn.qatime.player.im.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class GuessAttachment extends CustomAttachment {

    public enum Guess {
        Shitou(1, "石头"),
        Jiandao(2, "剪刀"),
        Bu(3, "布"),
        ;

        private int value;
        private String desc;

        Guess(int value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        static Guess enumOfValue(int value) {
            for (Guess direction : values()){
                if (direction.getValue() == value) {
                    return direction;
                }
            }
            return Shitou;
        }

        public int getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    private Guess value;

    public GuessAttachment() {
        super(CustomAttachmentType.Guess);
        random();
    }

    @Override
    protected void parseData(JSONObject data) throws JSONException {
        value = Guess.enumOfValue(data.getInt("value"));
    }

    @Override
    protected JSONObject packData() throws JSONException {
        JSONObject data = new JSONObject();
        data.put("value", value.getValue());
        return data;
    }

    private void random() {
        int value = new Random().nextInt(3) + 1;
        this.value = Guess.enumOfValue(value);
    }

    public Guess getValue() {
        return value;
    }
}
