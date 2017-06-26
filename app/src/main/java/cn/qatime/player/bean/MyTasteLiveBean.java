package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/5/15 11:16
 * @Describe
 */

public class MyTasteLiveBean implements Serializable {

    /**
     * status : 1
     * data : [{"id":652,"used_count":0,"buy_count":9,"lesson_price":"0.0","course":{"id":102,"name":"物理二轮复习学习方案\u2014\u2014提高","subject":"英语","grade":"高三","teacher_name":"关倩倩","publicize":"/assets/courses/app_info_default-fb4200ff7002fcdabc423b8c54573b71.png"},"status":"inactive"},{"id":618,"used_count":0,"buy_count":5,"lesson_price":"0.0","course":{"id":105,"name":"教师新版本","subject":"化学","grade":"高一","teacher_name":"王志成","publicize":"/assets/courses/app_info_default-fb4200ff7002fcdabc423b8c54573b71.png"},"status":"inactive"}]
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
         * id : 652
         * used_count : 0
         * buy_count : 9
         * lesson_price : 0.0
         * course : {"id":102,"name":"物理二轮复习学习方案\u2014\u2014提高","subject":"英语","grade":"高三","teacher_name":"关倩倩","publicize":"/assets/courses/app_info_default-fb4200ff7002fcdabc423b8c54573b71.png"}
         * status : inactive
         */

        private int id;
        private int used_count;
        private int buy_count;
        private String lesson_price;
        private CourseBean course;
        private String status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUsed_count() {
            return used_count;
        }

        public void setUsed_count(int used_count) {
            this.used_count = used_count;
        }

        public int getBuy_count() {
            return buy_count;
        }

        public void setBuy_count(int buy_count) {
            this.buy_count = buy_count;
        }

        public String getLesson_price() {
            return lesson_price;
        }

        public void setLesson_price(String lesson_price) {
            this.lesson_price = lesson_price;
        }

        public CourseBean getCourse() {
            return course;
        }

        public void setCourse(CourseBean course) {
            this.course = course;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public static class CourseBean {
            /**
             * id : 102
             * name : 物理二轮复习学习方案——提高
             * subject : 英语
             * grade : 高三
             * teacher_name : 关倩倩
             * publicize : /assets/courses/app_info_default-fb4200ff7002fcdabc423b8c54573b71.png
             */

            private int id;
            private String name;
            private String subject;
            private String grade;
            private String teacher_name;
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

            public String getPublicize() {
                return publicize;
            }

            public void setPublicize(String publicize) {
                this.publicize = publicize;
            }
        }
    }
}
