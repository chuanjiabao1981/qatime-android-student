package cn.qatime.player.bean.exam;

import java.io.Serializable;
import java.util.List;

/**
 * @author luntify
 * @date 2017/12/14 17:12
 * @Description:
 */

public class Examination implements Serializable {

    /**
     * status : 1
     * data : {"ticket":null,"paper":{"id":1,"name":"20171208","duration":1800,"topics_count":20,"grade_category":"初中","subject":"英语","price":"10.0","users_count":0,"status":"published","score":40,"type":"Exam::JuniorPaper","created_at":"2017-12-13T16:25:22.394+08:00","updated_at":"2017-12-13T18:05:28.079+08:00","published_at":null,"categories":[]}}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * ticket : null
         * paper : {"id":1,"name":"20171208","duration":1800,"topics_count":20,"grade_category":"初中","subject":"英语","price":"10.0","users_count":0,"status":"published","score":40,"type":"Exam::JuniorPaper","created_at":"2017-12-13T16:25:22.394+08:00","updated_at":"2017-12-13T18:05:28.079+08:00","published_at":null,"categories":[]}
         */

        private Object ticket;
        private PaperBean paper;

        public Object getTicket() {
            return ticket;
        }

        public void setTicket(Object ticket) {
            this.ticket = ticket;
        }

        public PaperBean getPaper() {
            return paper;
        }

        public void setPaper(PaperBean paper) {
            this.paper = paper;
        }

        public static class PaperBean {
            /**
             * id : 1
             * name : 20171208
             * duration : 1800
             * topics_count : 20
             * grade_category : 初中
             * subject : 英语
             * price : 10.0
             * users_count : 0
             * status : published
             * score : 40
             * type : Exam::JuniorPaper
             * created_at : 2017-12-13T16:25:22.394+08:00
             * updated_at : 2017-12-13T18:05:28.079+08:00
             * published_at : null
             * categories : []
             */

            private int id;
            private String name;
            private int duration;
            private int topics_count;
            private String grade_category;
            private String subject;
            private float price;
            private int users_count;
            private String status;
            private int score;
            private String type;
            private List<Categories> categories;

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

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public int getUsers_count() {
                return users_count;
            }

            public void setUsers_count(int users_count) {
                this.users_count = users_count;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<Categories> getCategories() {
                return categories;
            }

            public void setCategories(List<Categories> categories) {
                this.categories = categories;
            }
        }
    }
}
