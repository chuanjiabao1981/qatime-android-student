package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author tianhaoranly
 * @Time 2017/3/31 14:30
 * @Describe
 */

public class InteractCourseContentFilterBean implements Serializable {

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
        private int completed_lessons_count;
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
        private List<TeachersBean> teachers;

        public List<TeachersBean> getTeachers() {
            return teachers;
        }

        public void setTeachers(List<TeachersBean> teachers) {
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

        public int getCompleted_lessons_count() {
            return completed_lessons_count;
        }

        public void setCompleted_lessons_count(int completed_lessons_count) {
            this.completed_lessons_count = completed_lessons_count;
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

        public static class TeachersBean {
            /**
             * id : 541
             * name : 教师
             * nick_name : 春意盎然
             * avatar_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ed8858f5ed860b8e94226f37446b89c1.jpg
             * ex_big_avatar_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_ed8858f5ed860b8e94226f37446b89c1.jpg
             * login_mobile : null
             * email : qatime@8.cn
             * teaching_years : within_three_years
             * category : 高中
             * subject : 数学
             * grade_range : []
             * gender : null
             * birthday : null
             * province : 1
             * city : 1
             * school : 1
             * desc :
             */

            private int id;
            private String name;
            private String nick_name;
            private String avatar_url;
            private String ex_big_avatar_url;
            private String login_mobile;
            private String email;
            private String teaching_years;
            private String category;
            private String subject;
            private String gender;
            private String birthday;
            private int province;
            private int city;
            private int school;
            private String desc;
            private List<String> grade_range;

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

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getEx_big_avatar_url() {
                return ex_big_avatar_url;
            }

            public void setEx_big_avatar_url(String ex_big_avatar_url) {
                this.ex_big_avatar_url = ex_big_avatar_url;
            }

            public String getLogin_mobile() {
                return login_mobile;
            }

            public void setLogin_mobile(String login_mobile) {
                this.login_mobile = login_mobile;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getTeaching_years() {
                return teaching_years;
            }

            public void setTeaching_years(String teaching_years) {
                this.teaching_years = teaching_years;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public int getProvince() {
                return province;
            }

            public void setProvince(int province) {
                this.province = province;
            }

            public int getCity() {
                return city;
            }

            public void setCity(int city) {
                this.city = city;
            }

            public int getSchool() {
                return school;
            }

            public void setSchool(int school) {
                this.school = school;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public List<String> getGrade_range() {
                return grade_range;
            }

            public void setGrade_range(List<String> grade_range) {
                this.grade_range = grade_range;
            }
        }
    }
}
