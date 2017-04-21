package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/3/20 14:30
 * @Describe
 */

public class FilterLiveCourseBean implements Serializable {

    /**
     * status : 1
     * data : [{"id":93,"name":"再来一个辅导班","subject":"化学","grade":"高三","teacher_name":"王志成","price":100,"current_price":0,"chat_team_id":"25195018","chat_team_owner":"07b7c43a854ed44d36c2941f1fc5ad00","buy_tickets_count":12,"status":"teaching","preset_lesson_count":1,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2017-03-17 14:29","live_end_time":"2017-03-17 15:21","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_8a7e08039eade62e5ea947c6a492683f.jpg","preview_time":"2017-03-30 02:10","is_tasting":false,"is_bought":true}]
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
         * id : 93
         * name : 再来一个辅导班
         * subject : 化学
         * grade : 高三
         * teacher_name : 王志成
         * price : 100
         * current_price : 0
         * chat_team_id : 25195018
         * chat_team_owner : 07b7c43a854ed44d36c2941f1fc5ad00
         * buy_tickets_count : 12
         * status : teaching
         * preset_lesson_count : 1
         * completed_lesson_count : 0
         * taste_count : 0
         * completed_lessons_count : 0
         * live_start_time : 2017-03-17 14:29
         * live_end_time : 2017-03-17 15:21
         * publicize : http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_8a7e08039eade62e5ea947c6a492683f.jpg
         * preview_time : 2017-03-30 02:10
         * is_tasting : false
         * is_bought : true
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
        private int taste_count;
        private String live_start_time;
        private String live_end_time;
        private String publicize;
        private String preview_time;
        private boolean is_tasting;
        private boolean is_bought;

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


        public int getTaste_count() {
            return taste_count;
        }

        public void setTaste_count(int taste_count) {
            this.taste_count = taste_count;
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

        public String getPreview_time() {
            return preview_time;
        }

        public void setPreview_time(String preview_time) {
            this.preview_time = preview_time;
        }

        public boolean isIs_tasting() {
            return is_tasting;
        }

        public void setIs_tasting(boolean is_tasting) {
            this.is_tasting = is_tasting;
        }

        public boolean isIs_bought() {
            return is_bought;
        }

        public void setIs_bought(boolean is_bought) {
            this.is_bought = is_bought;
        }
    }
}
