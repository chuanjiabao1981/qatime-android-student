package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/9/28.
 */

public class AttachmentsBean implements Serializable {
    public String id;
    public String file_url;
    public String file_type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttachmentsBean that = (AttachmentsBean) o;

        return id.equals(that.id)||file_url.equals(that.file_url);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
