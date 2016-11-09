package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author luntify
 * @date 2016/8/15 10:03
 * @Description
 */
public class TutorialClassBean implements Serializable {
    private int status;

    private List<Data> data;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return this.data;
    }

    public class Data implements Serializable {

        /**
         * id : 5
         * name : 高中数学一轮复习第一章集合第二课时
         * subject : 数学
         * grade : 高三
         * teacher_name : 姜海全
         * price : 0
         * chat_team_id : 7964470
         * buy_tickets_count : 1
         * preset_lesson_count : 1
         * completed_lesson_count : 0
         * live_start_time : 2016-08-10 4:00
         * live_end_time : 2016-08-10 18:30
         * publicize : http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_929d05eabae8e06550620885ecc115f7.jpg
         * board : rtmp://va0a19f55.live.126.net/live/2794c854398f4d05934157e05e2fe419
         * camera :
         * preview_time : 2016-08-10 18:00
         * is_tasting : true
         * is_bought : false
         */

        private int id;
        private String name;
        private String subject;
        private String grade;
        private String teacher_name;
        private int price;
        private String chat_team_id;
        private int buy_tickets_count;
        private int preset_lesson_count;
        private int completed_lesson_count;
        private String live_start_time;
        private String live_end_time;
        private String publicize;
        private String board;
        private String camera;
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

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getChat_team_id() {
            return chat_team_id;
        }

        public void setChat_team_id(String chat_team_id) {
            this.chat_team_id = chat_team_id;
        }

        public int getBuy_tickets_count() {
            return buy_tickets_count;
        }

        public void setBuy_tickets_count(int buy_tickets_count) {
            this.buy_tickets_count = buy_tickets_count;
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

        public String getBoard() {
            return board;
        }

        public void setBoard(String board) {
            this.board = board;
        }

        public String getCamera() {
            return camera;
        }

        public void setCamera(String camera) {
            this.camera = camera;
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
