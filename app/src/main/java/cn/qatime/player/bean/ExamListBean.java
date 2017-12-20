package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author luntify
 * @date 2017/12/13 18:06
 * @Description:
 */

public class ExamListBean implements Serializable {

    /**
     * status : 1
     * data : [{"id":1,"name":"20171208","duration":1800,"topics_count":20,"grade_category":"初中","subject":"英语","price":"10.0","users_count":0}]
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
         * name : 20171208
         * duration : 1800
         * topics_count : 20
         * grade_category : 初中
         * subject : 英语
         * price : 10.0
         * users_count : 0
         */

        private int id;
        private String name;
        private int duration;
        private int topics_count;
        private String grade_category;
        private String subject;
        private String price;
        private int users_count;

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

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getTopics_count() {
            return topics_count;
        }

        public void setTopics_count(int topics_count) {
            this.topics_count = topics_count;
        }

        public String getGrade_category() {
            return grade_category;
        }

        public void setGrade_category(String grade_category) {
            this.grade_category = grade_category;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getUsers_count() {
            return users_count;
        }

        public void setUsers_count(int users_count) {
            this.users_count = users_count;
        }
    }
}
