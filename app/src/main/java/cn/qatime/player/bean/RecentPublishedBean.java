package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/3/20 19:15
 * @Describe
 */

public class RecentPublishedBean implements Serializable {

    /**
     * status : 1
     * data : {"published_rank":[{"id":25,"name":"初中数学","subject":"数学","grade":"初一","teacher_name":"马燕兆","price":50,"current_price":50,"chat_team_id":"25184275","chat_team_owner":"8b8dac47fc743ebc7d163bd360caaafb","buy_tickets_count":4,"status":"teaching","preset_lesson_count":2,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2016-09-25 10:00","live_end_time":"2016-09-25 22:00","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_2d71a0cb8c07f529009ce51bb8cd3dbf.jpg"},{"id":32,"name":"思想品德","subject":"政治","grade":"初一","teacher_name":"石锋","price":50,"current_price":50,"chat_team_id":"25195017","chat_team_owner":"e194a95b2cd0bd0452233bf64cfa91c1","buy_tickets_count":8,"status":"teaching","preset_lesson_count":1,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2016-08-19 8:00","live_end_time":"2016-08-19 8:30","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_d3f2f4cea543e30e41a60606887835e6.png"},{"id":24,"name":"高中化学离子方程式的书写","subject":"化学","grade":"高二","teacher_name":"乔保国","price":50,"current_price":50,"chat_team_id":"25184274","chat_team_owner":"2d10b094407fcc0982080f61f75b7b57","buy_tickets_count":5,"status":"teaching","preset_lesson_count":2,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2016-08-14 20:00","live_end_time":"2016-08-18 10:30","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_f356c4fb710b204de341009c8471e165.jpg"},{"id":13,"name":"初中数学辅导班","subject":"数学","grade":"初二","teacher_name":"张震","price":50,"current_price":50,"chat_team_id":"25190624","chat_team_owner":"5f265cb6d4fb411de8ebd8f42bc6271f","buy_tickets_count":2,"status":"teaching","preset_lesson_count":3,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2016-08-12 10:30","live_end_time":"2016-08-13 15:00","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_2668ca981533e5d6a3d7a083e54bb1d3.jpg"},{"id":8,"name":"初中物理","subject":"物理","grade":"初二","teacher_name":"张贤","price":50,"current_price":50,"chat_team_id":"25196239","chat_team_owner":"2926fcf2dd5f864615cbe6ea2d531a1c","buy_tickets_count":2,"status":"teaching","preset_lesson_count":1,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2016-08-19 20:00","live_end_time":"2016-08-19 21:00","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_ebb085653b5d094635b2a78be1330a9b.jpg"}],"start_rank":[{"id":96,"name":"测试默认图片","subject":"化学","grade":"二年级","teacher_name":"王志成","price":100,"current_price":100,"chat_team_id":"","chat_team_owner":"07b7c43a854ed44d36c2941f1fc5ad00","buy_tickets_count":0,"status":"published","preset_lesson_count":1,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2017-03-21 20:00","live_end_time":"2017-03-21 21:00","publicize":"http://testing.qatime.cn/imgs/course_default.png"},{"id":61,"name":"再来一个辅导班","subject":"化学","grade":"高二","teacher_name":"王志成","price":10,"current_price":10,"chat_team_id":"25194018","chat_team_owner":"07b7c43a854ed44d36c2941f1fc5ad00","buy_tickets_count":4,"status":"published","preset_lesson_count":1,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2016-11-22 09:00","live_end_time":"2016-11-22 10:00","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_b876ab65a61aaa1ae47597ef144b9f45.png"}]}
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
        private List<PublishedRankBean> published_rank;
        private List<StartRankBean> start_rank;

        public List<PublishedRankBean> getPublished_rank() {
            return published_rank;
        }

        public void setPublished_rank(List<PublishedRankBean> published_rank) {
            this.published_rank = published_rank;
        }

        public List<StartRankBean> getStart_rank() {
            return start_rank;
        }

        public void setStart_rank(List<StartRankBean> start_rank) {
            this.start_rank = start_rank;
        }

        public static class PublishedRankBean {
            /**
             * id : 25
             * name : 初中数学
             * subject : 数学
             * grade : 初一
             * teacher_name : 马燕兆
             * price : 50
             * current_price : 50
             * chat_team_id : 25184275
             * chat_team_owner : 8b8dac47fc743ebc7d163bd360caaafb
             * buy_tickets_count : 4
             * status : teaching
             * preset_lesson_count : 2
             * completed_lesson_count : 0
             * taste_count : 0
             * completed_lessons_count : 0
             * live_start_time : 2016-09-25 10:00
             * live_end_time : 2016-09-25 22:00
             * publicize : http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_2d71a0cb8c07f529009ce51bb8cd3dbf.jpg
             */

            private int id;
            private String name;
            private String subject;
            private String grade;
            private String teacher_name;
            private float price;
            private float current_price;
            private String chat_team_id;
            private String chat_team_owner;
            private int buy_tickets_count;
            private String status;
            private int preset_lesson_count;
            private int completed_lesson_count;
            private int taste_count;
            private int completed_lessons_count;
            private String live_start_time;
            private String live_end_time;
            private String publicize;

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

            public String getTeacher_name() {
                return teacher_name;
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public float getCurrent_price() {
                return current_price;
            }

            public void setCurrent_price(float current_price) {
                this.current_price = current_price;
            }

            public String getChat_team_id() {
                return chat_team_id;
            }

            public void setChat_team_id(String chat_team_id) {
                this.chat_team_id = chat_team_id;
            }

            public String getChat_team_owner() {
                return chat_team_owner;
            }

            public void setChat_team_owner(String chat_team_owner) {
                this.chat_team_owner = chat_team_owner;
            }

            public int getBuy_tickets_count() {
                return buy_tickets_count;
            }

            public void setBuy_tickets_count(int buy_tickets_count) {
                this.buy_tickets_count = buy_tickets_count;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getPreset_lesson_count() {
                return preset_lesson_count;
            }

            public void setPreset_lesson_count(int preset_lesson_count) {
                this.preset_lesson_count = preset_lesson_count;
            }

            public int getCompleted_lesson_count() {
                return completed_lesson_count;
            }

            public void setCompleted_lesson_count(int completed_lesson_count) {
                this.completed_lesson_count = completed_lesson_count;
            }

            public int getTaste_count() {
                return taste_count;
            }

            public void setTaste_count(int taste_count) {
                this.taste_count = taste_count;
            }

            public int getCompleted_lessons_count() {
                return completed_lessons_count;
            }

            public void setCompleted_lessons_count(int completed_lessons_count) {
                this.completed_lessons_count = completed_lessons_count;
            }

            public String getLive_start_time() {
                return live_start_time;
            }

            public void setLive_start_time(String live_start_time) {
                this.live_start_time = live_start_time;
            }

            public String getLive_end_time() {
                return live_end_time;
            }

            public void setLive_end_time(String live_end_time) {
                this.live_end_time = live_end_time;
            }

            public String getPublicize() {
                return publicize;
            }

            public void setPublicize(String publicize) {
                this.publicize = publicize;
            }
        }

        public static class StartRankBean {
            /**
             * id : 96
             * name : 测试默认图片
             * subject : 化学
             * grade : 二年级
             * teacher_name : 王志成
             * price : 100
             * current_price : 100
             * chat_team_id :
             * chat_team_owner : 07b7c43a854ed44d36c2941f1fc5ad00
             * buy_tickets_count : 0
             * status : published
             * preset_lesson_count : 1
             * completed_lesson_count : 0
             * taste_count : 0
             * completed_lessons_count : 0
             * live_start_time : 2017-03-21 20:00
             * live_end_time : 2017-03-21 21:00
             * publicize : http://testing.qatime.cn/imgs/course_default.png
             */

            private int id;
            private String name;
            private String subject;
            private String grade;
            private String teacher_name;
            private float price;
            private float current_price;
            private String chat_team_id;
            private String chat_team_owner;
            private int buy_tickets_count;
            private String status;
            private int preset_lesson_count;
            private int completed_lesson_count;
            private int taste_count;
            private int completed_lessons_count;
            private String live_start_time;
            private String live_end_time;
            private String publicize;

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

            public String getTeacher_name() {
                return teacher_name;
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public float getCurrent_price() {
                return current_price;
            }

            public void setCurrent_price(float current_price) {
                this.current_price = current_price;
            }

            public String getChat_team_id() {
                return chat_team_id;
            }

            public void setChat_team_id(String chat_team_id) {
                this.chat_team_id = chat_team_id;
            }

            public String getChat_team_owner() {
                return chat_team_owner;
            }

            public void setChat_team_owner(String chat_team_owner) {
                this.chat_team_owner = chat_team_owner;
            }

            public int getBuy_tickets_count() {
                return buy_tickets_count;
            }

            public void setBuy_tickets_count(int buy_tickets_count) {
                this.buy_tickets_count = buy_tickets_count;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getPreset_lesson_count() {
                return preset_lesson_count;
            }

            public void setPreset_lesson_count(int preset_lesson_count) {
                this.preset_lesson_count = preset_lesson_count;
            }

            public int getCompleted_lesson_count() {
                return completed_lesson_count;
            }

            public void setCompleted_lesson_count(int completed_lesson_count) {
                this.completed_lesson_count = completed_lesson_count;
            }

            public int getTaste_count() {
                return taste_count;
            }

            public void setTaste_count(int taste_count) {
                this.taste_count = taste_count;
            }

            public int getCompleted_lessons_count() {
                return completed_lessons_count;
            }

            public void setCompleted_lessons_count(int completed_lessons_count) {
                this.completed_lessons_count = completed_lessons_count;
            }

            public String getLive_start_time() {
                return live_start_time;
            }

            public void setLive_start_time(String live_start_time) {
                this.live_start_time = live_start_time;
            }

            public String getLive_end_time() {
                return live_end_time;
            }

            public void setLive_end_time(String live_end_time) {
                this.live_end_time = live_end_time;
            }

            public String getPublicize() {
                return publicize;
            }

            public void setPublicize(String publicize) {
                this.publicize = publicize;
            }
        }
    }
}
