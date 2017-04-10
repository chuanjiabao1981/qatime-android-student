package cn.qatime.player.bean;

import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2017/4/6 18:36
 * @Description:
 */
public class LabelBean {
    /**
     * status : 1
     * data : [{"name":"高考","taggings_count":4},{"name":"高考志愿","taggings_count":0},{"name":"历年真题","taggings_count":2},{"name":"期中期末试卷","taggings_count":2},{"name":"自编试卷","taggings_count":2},{"name":"暑假课","taggings_count":1},{"name":"寒假课","taggings_count":0},{"name":"周末课","taggings_count":2},{"name":"国庆假期课","taggings_count":1},{"name":"基础课","taggings_count":3},{"name":"巩固课","taggings_count":1},{"name":"外教","taggings_count":1},{"name":"冲刺","taggings_count":1},{"name":"重点难点","taggings_count":3}]
     */

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
        /**
         * name : 高考
         * taggings_count : 4
         */

        private String name;
        private int taggings_count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTaggings_count() {
            return taggings_count;
        }

        public void setTaggings_count(int taggings_count) {
            this.taggings_count = taggings_count;
        }
    }
}
