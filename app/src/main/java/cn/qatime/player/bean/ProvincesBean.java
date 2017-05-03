package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2017/2/28 19:28
 * @Description:
 */
public class ProvincesBean {


    /**
     * status : 1
     * data : [{"id":1,"name":"山西省"},{"id":2,"name":"浙江省"},{"id":3,"name":"北京市"},{"id":4,"name":"天津市"},{"id":5,"name":"山东省"},{"id":6,"name":"江苏省"},{"id":7,"name":"福建省"},{"id":8,"name":"广东省"},{"id":9,"name":"重庆市"},{"id":10,"name":"澳门特别行政区"},{"id":11,"name":"河北省"},{"id":12,"name":"甘肃省"},{"id":13,"name":"四川省"},{"id":14,"name":"宁夏回族自治区"},{"id":15,"name":"内蒙古自治区"},{"id":16,"name":"广西壮族自治区"},{"id":17,"name":"西藏自治区"},{"id":18,"name":"陕西省"},{"id":19,"name":"贵州省"},{"id":20,"name":"云南省"},{"id":21,"name":"安徽省"},{"id":22,"name":"湖南省"},{"id":23,"name":"新疆维吾尔族自治区"},{"id":24,"name":"河南省"},{"id":25,"name":"江西省"},{"id":26,"name":"青海省"},{"id":27,"name":"黑龙江省"},{"id":28,"name":"吉林省"},{"id":29,"name":"辽宁省"},{"id":30,"name":"海南省"},{"id":31,"name":"湖北省"},{"id":32,"name":"台湾市"},{"id":33,"name":"上海市"},{"id":34,"name":"香港特别行政区"}]
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

    public static class DataBean implements Serializable{
        /**
         * id : 1
         * name : 山西省
         */

        private String id;
        private String name;
        private String firstLetter;
        private String firstLetters;

        public String getFirstLetter() {
            return firstLetter;
        }

        public void setFirstLetter(String firstLetter) {
            this.firstLetter = firstLetter;
        }

        public String getFirstLetters() {
            return firstLetters;
        }

        public void setFirstLetters(String firstLetters) {
            this.firstLetters = firstLetters;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
