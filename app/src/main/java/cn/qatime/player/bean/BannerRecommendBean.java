package cn.qatime.player.bean;

import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2016/11/1 15:49
 * @Description:
 */
public class BannerRecommendBean {
    private int status;

    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

    }
}
