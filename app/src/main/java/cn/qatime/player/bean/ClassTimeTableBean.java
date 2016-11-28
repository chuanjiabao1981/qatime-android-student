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
    private List<DataEntity> data;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public class DataEntity implements Serializable {

        private String date;
        private List<LessonsEntity> lessons;

        public void setDate(String date) {
            this.date = date;
        }

        public void setLessons(List<LessonsEntity> lessons) {
            this.lessons = lessons;
        }

        public String getDate() {
            return date;
        }

        public List<LessonsEntity> getLessons() {
            return lessons;
        }

        public class LessonsEntity implements Serializable {
            /**
             * id : 15
             * name : 第一节
             * status : finished
             * class_date : 2016-08-08
             * live_time : 6:30~8:00
             * course_name : 测试一下辅导班
             * course_publicize : http://testing.qatime.cn/imgs/no_img.png
             * subject : 数学
             * teacher_name : 辛帅锋
             */

            private int id;
            private String name;
            private String status;
            private String class_date;
            private String live_time;
            private String course_name;
            private String course_publicize;
            private String subject;
            private String chat_team_id;
            private String board_pull_stream;
            private String camera_pull_stream;
            private String grade;
            private String teacher_name;
            private String course_id;

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public String getCourse_id() {
                return course_id;
            }

            public void setCourse_id(String course_id) {
                this.course_id = course_id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public void setClass_date(String class_date) {
                this.class_date = class_date;
            }

            public void setLive_time(String live_time) {
                this.live_time = live_time;
            }

            public void setCourse_name(String course_name) {
                this.course_name = course_name;
            }

            public void setCourse_publicize(String course_publicize) {
                this.course_publicize = course_publicize;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getChat_team_id() {
                return chat_team_id;
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getStatus() {
                return status;
            }

            public String getClass_date() {
                return class_date;
            }

            public String getLive_time() {
                return live_time;
            }

            public String getCourse_name() {
                return course_name;
            }

            public String getCourse_publicize() {
                return course_publicize;
            }

            public String getSubject() {
                return subject;
            }

            public String getBoard() {
                return board_pull_stream;
            }

            public String getCamera() {
                return camera_pull_stream;
            }

            public String getTeacher_name() {
                return teacher_name;
            }
        }
    }
}
