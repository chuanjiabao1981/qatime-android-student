package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author luntify
 * @date 2016/8/26 16:23
 * @Description
 */
public class ClassTimeTableBean implements Serializable {


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

        private String date;
        private List<LessonsBean> lessons;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<LessonsBean> getLessons() {
            return lessons;
        }

        public void setLessons(List<LessonsBean> lessons) {
            this.lessons = lessons;
        }

        public static class LessonsBean {
            private boolean taste;
            private int id;
            private String name;
            private String status;
            private String class_date;
            private boolean replayable;
            private int left_replay_times;
            private String live_time;
            private String model_type;
            private String course_name;
            private String teacher_name;
            private String course_publicize;
            private String subject;
            private String grade;
            private int buy_tickets_count;
            private int product_id;
            private String product_type;
            private int course_id;
            private String model_name;

            public boolean isTaste() {
                return taste;
            }

            public void setTaste(boolean taste) {
                this.taste = taste;
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

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getClass_date() {
                return class_date;
            }

            public void setClass_date(String class_date) {
                this.class_date = class_date;
            }

            public boolean isReplayable() {
                return replayable;
            }

            public void setReplayable(boolean replayable) {
                this.replayable = replayable;
            }

            public int getLeft_replay_times() {
                return left_replay_times;
            }

            public void setLeft_replay_times(int left_replay_times) {
                this.left_replay_times = left_replay_times;
            }

            public String getLive_time() {
                return live_time;
            }

            public void setLive_time(String live_time) {
                this.live_time = live_time;
            }

            public String getModel_type() {
                return model_type;
            }

            public void setModel_type(String model_type) {
                this.model_type = model_type;
            }

            public String getCourse_name() {
                return course_name;
            }

            public void setCourse_name(String course_name) {
                this.course_name = course_name;
            }

            public String getTeacher_name() {
                return teacher_name;
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }

            public String getCourse_publicize() {
                return course_publicize;
            }

            public void setCourse_publicize(String course_publicize) {
                this.course_publicize = course_publicize;
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

            public int getBuy_tickets_count() {
                return buy_tickets_count;
            }

            public void setBuy_tickets_count(int buy_tickets_count) {
                this.buy_tickets_count = buy_tickets_count;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public String getProduct_type() {
                return product_type;
            }

            public void setProduct_type(String product_type) {
                this.product_type = product_type;
            }

            public int getCourse_id() {
                return course_id;
            }

            public void setCourse_id(int course_id) {
                this.course_id = course_id;
            }

            public String getModel_name() {
                return model_name;
            }

            public void setModel_name(String model_name) {
                this.model_name = model_name;
            }
        }
    }
}
