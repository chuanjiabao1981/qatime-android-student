package cn.qatime.player.bean;


import java.io.Serializable;
import java.util.List;

public class RemedialClassBean implements Serializable {
    private int status;

    private List<Data> data;

    public void setStatu(int status) {
        this.status = status;
    }

    public int getStatu() {
        return this.status;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return this.data;
    }

    public class Data implements Serializable {
        private int id;

        private String name;

        private String subject;

        private String grade;

        private String status;

        private String description;

        private int lesson_count;

        private int preset_lesson_count;

        private int completed_lesson_count;

        private String live_start_time;

        private String live_end_time;

        private String publicize;

        private String pull_address;

        private boolean is_tasting;

        private boolean is_bought;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
        public void setSubject(String subject){
            this.subject = subject;
        }
        public String getSubject(){
            return this.subject;
        }
        public void setGrade(String grade){
            this.grade = grade;
        }
        public String getGrade(){
            return this.grade;
        }
        public void setStatus(String status){
            this.status = status;
        }
        public String getStatus(){
            return this.status;
        }
        public void setDescription(String description){
            this.description = description;
        }
        public String getDescription(){
            return this.description;
        }
        public void setLesson_count(int lesson_count){
            this.lesson_count = lesson_count;
        }
        public int getLesson_count(){
            return this.lesson_count;
        }
        public void setPreset_lesson_count(int preset_lesson_count){
            this.preset_lesson_count = preset_lesson_count;
        }
        public int getPreset_lesson_count(){
            return this.preset_lesson_count;
        }
        public void setCompleted_lesson_count(int completed_lesson_count){
            this.completed_lesson_count = completed_lesson_count;
        }
        public int getCompleted_lesson_count(){
            return this.completed_lesson_count;
        }
        public void setLive_start_time(String live_start_time){
            this.live_start_time = live_start_time;
        }
        public String getLive_start_time(){
            return this.live_start_time;
        }
        public void setLive_end_time(String live_end_time){
            this.live_end_time = live_end_time;
        }
        public String getLive_end_time(){
            return this.live_end_time;
        }
        public void setPublicize(String publicize){
            this.publicize = publicize;
        }
        public String getPublicize(){
            return this.publicize;
        }
        public void setPull_address(String pull_address){
            this.pull_address = pull_address;
        }
        public String getPull_address(){
            return this.pull_address;
        }
        public void setIs_tasting(boolean is_tasting){
            this.is_tasting = is_tasting;
        }
        public boolean getIs_tasting(){
            return this.is_tasting;
        }
        public void setIs_bought(boolean is_bought){
            this.is_bought = is_bought;
        }
        public boolean getIs_bought(){
            return this.is_bought;
        }

    }
}
