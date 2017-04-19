package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

import libraryextra.bean.TeacherBean;


/**
 * @author tianhaoranly
 * @Time 2017/3/31 14:30
 * @Describe
 */

public class FilterInteractCourseBean implements Serializable {

    /**
     * status : 1
     * data : [{"id":2,"name":"创建10个课程要疯呀","subject":"化学","grade":"初二","price":"500.0","status":"published","description":"<p>哈哈哈哈哈哈哈&nbsp;<\/p>","lessons_count":10,"completed_lessons_count":0,"closed_lessons_count":0,"live_start_time":"2017-04-01 18:00","live_end_time":"2017-04-10 18:45","objective":"漫无目的的走在大街上，哪里会有目标","suit_crowd":"活到老，学到老，学习不分年龄","publicize_url":"http://testing.qatime.cn/imgs/course_default.png","publicize_info_url":"http://testing.qatime.cn/imgs/course_default.png","publicize_list_url":"http://testing.qatime.cn/imgs/course_default.png","publicize_app_url":"http://testing.qatime.cn/imgs/course_default.png","created_at":1490940558}]
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
         * id : 2
         * name : 创建10个课程要疯呀
         * subject : 化学
         * grade : 初二
         * price : 500.0
         * status : published
         * description : <p>哈哈哈哈哈哈哈&nbsp;</p>
         * lessons_count : 10
         * completed_lessons_count : 0
         * closed_lessons_count : 0
         * live_start_time : 2017-04-01 18:00
         * live_end_time : 2017-04-10 18:45
         * objective : 漫无目的的走在大街上，哪里会有目标
         * suit_crowd : 活到老，学到老，学习不分年龄
         * publicize_url : http://testing.qatime.cn/imgs/course_default.png
         * publicize_info_url : http://testing.qatime.cn/imgs/course_default.png
         * publicize_list_url : http://testing.qatime.cn/imgs/course_default.png
         * publicize_app_url : http://testing.qatime.cn/imgs/course_default.png
         * created_at : 1490940558
         */

        private int id;
        private String name;
        private String subject;
        private String grade;
        private String price;
        private String status;
        private String description;
        private int lessons_count;
        private int closed_lessons_count;
        private String live_start_time;
        private String live_end_time;
        private String objective;
        private String suit_crowd;
        private String publicize_url;
        private String publicize_info_url;
        private String publicize_list_url;
        private String publicize_app_url;
        private int created_at;
        private List<TeacherBean> teachers;

        public List<TeacherBean> getTeachers() {
            return teachers;
        }

        public void setTeachers(List<TeacherBean> teachers) {
            this.teachers = teachers;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getLessons_count() {
            return lessons_count;
        }

        public void setLessons_count(int lessons_count) {
            this.lessons_count = lessons_count;
        }


        public int getClosed_lessons_count() {
            return closed_lessons_count;
        }

        public void setClosed_lessons_count(int closed_lessons_count) {
            this.closed_lessons_count = closed_lessons_count;
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

        public String getPublicize_url() {
            return publicize_url;
        }

        public void setPublicize_url(String publicize_url) {
            this.publicize_url = publicize_url;
        }

        public String getPublicize_info_url() {
            return publicize_info_url;
        }

        public void setPublicize_info_url(String publicize_info_url) {
            this.publicize_info_url = publicize_info_url;
        }

        public String getPublicize_list_url() {
            return publicize_list_url;
        }

        public void setPublicize_list_url(String publicize_list_url) {
            this.publicize_list_url = publicize_list_url;
        }

        public String getPublicize_app_url() {
            return publicize_app_url;
        }

        public void setPublicize_app_url(String publicize_app_url) {
            this.publicize_app_url = publicize_app_url;
        }

        public int getCreated_at() {
            return created_at;
        }

        public void setCreated_at(int created_at) {
            this.created_at = created_at;
        }

    }
}
