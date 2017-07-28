package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/3/20 14:30
 * @Describe
 */

public class FilterExclusiveCourseBean implements Serializable {

    /**
     * status : 1
     * data : [{"id":1,"name":"测试专属课1","publicizes_url":{"app_info":"http://testing.qatime.cn/assets/groups/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png","list":"http://testing.qatime.cn/assets/groups/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png","info":"http://testing.qatime.cn/assets/groups/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png"},"subject":"数学","grade":"高一","status":"published","teacher_name":"王志成","price":200,"current_price":200,"view_tickets_count":0,"events_count":3,"start_at":null,"end_at":null,"objective":"测试专属课1","suit_crowd":"测试专属课1","description":"<p>测试专属课1测试专属课1<br><\/p>"}]
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
         * id : 1
         * name : 测试专属课1
         * publicizes_url : {"app_info":"http://testing.qatime.cn/assets/groups/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png","list":"http://testing.qatime.cn/assets/groups/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png","info":"http://testing.qatime.cn/assets/groups/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png"}
         * subject : 数学
         * grade : 高一
         * status : published
         * teacher_name : 王志成
         * price : 200
         * current_price : 200
         * view_tickets_count : 0
         * events_count : 3
         * start_at : null
         * end_at : null
         * objective : 测试专属课1
         * suit_crowd : 测试专属课1
         * description : <p>测试专属课1测试专属课1<br></p>
         */

        private int id;
        private String name;
        private PublicizesUrlBean publicizes_url;
        private String subject;
        private String grade;
        private String status;
        private String teacher_name;
        private String price;
        private String current_price;
        private int view_tickets_count;
        private String objective;
        private String suit_crowd;
        private String description;
        private String sell_type;

        public String getSell_type() {
            return sell_type;
        }

        public void setSell_type(String sell_type) {
            this.sell_type = sell_type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public PublicizesUrlBean getPublicizes_url() {
            return publicizes_url;
        }

        public void setPublicizes_url(PublicizesUrlBean publicizes_url) {
            this.publicizes_url = publicizes_url;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public int getView_tickets_count() {
            return view_tickets_count;
        }

        public void setView_tickets_count(int view_tickets_count) {
            this.view_tickets_count = view_tickets_count;
        }


        public String getObjective() {
            return objective;
        }

        public void setObjective(String objective) {
            this.objective = objective;
        }

        public String getSuit_crowd() {
            return suit_crowd;
        }

        public void setSuit_crowd(String suit_crowd) {
            this.suit_crowd = suit_crowd;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public static class PublicizesUrlBean {
            /**
             * app_info : http://testing.qatime.cn/assets/groups/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png
             * list : http://testing.qatime.cn/assets/groups/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png
             * info : http://testing.qatime.cn/assets/groups/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png
             */

            private String app_info;
            private String list;
            private String info;

            public String getApp_info() {
                return app_info;
            }

            public void setApp_info(String app_info) {
                this.app_info = app_info;
            }

            public String getList() {
                return list;
            }

            public void setList(String list) {
                this.list = list;
            }

            public String getInfo() {
                return info;
            }

            public void setInfo(String info) {
                this.info = info;
            }
        }
    }
}
