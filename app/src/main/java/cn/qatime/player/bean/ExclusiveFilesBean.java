package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/8/15.
 */

public class ExclusiveFilesBean implements Serializable {
    private String name;
    private String icon;
    private String size;
    private String time;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
