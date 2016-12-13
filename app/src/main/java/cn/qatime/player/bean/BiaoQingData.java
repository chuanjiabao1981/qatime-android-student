package cn.qatime.player.bean;

/**
 * @author lungtify
 * @Time 2016/12/12 14:56
 * @Describe 封装表情id与文件名
 */

public class BiaoQingData {
    private String tag;
    private int id;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getResourceId() {
        return id;
    }

    public void setResourceId(int id) {
        this.id = id;
    }

    public String getCode() {
        return "[" + tag + "]";
    }
}
