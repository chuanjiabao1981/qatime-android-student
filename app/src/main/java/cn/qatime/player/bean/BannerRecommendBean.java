package cn.qatime.player.bean;

import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2016/11/1 15:49
 * @Description:
 */
public class BannerRecommendBean {

    /**
     * status : 1
     * data : [{"title":"哈哈哈","index":1,"type":"Recommend::BannerItem","logo_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/banners/logo/f633841365af0b2b19fd53346b11f371.jpg","link":"http://www.baidu.com"},{"title":"哈哈哈","index":1,"type":"Recommend::BannerItem","logo_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/banners/logo/2fa19498b7777873289154bed28783a8.jpg","link":"http://www.baidu.com"},{"title":"哈哈哈","index":2,"type":"Recommend::BannerItem","logo_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/banners/logo/e57fdd6bee0969bc7bd84f69dd4590aa.jpg","link":"http://www.baidu.com"},{"title":"哈哈哈","index":3,"type":"Recommend::BannerItem","logo_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/banners/logo/5e714999d157f3a65a1e4d7fc6446767.jpg","link":"http://www.baidu.com"}]
     */

    private int status;
    /**
     * title : 哈哈哈
     * index : 1
     * type : Recommend::BannerItem
     * logo_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/banners/logo/f633841365af0b2b19fd53346b11f371.jpg
     * link : http://www.baidu.com
     */

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
        private String title;
        private int index;
        private String type;
        private String logo_url;
        private String link;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLogo_url() {
            return logo_url;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
