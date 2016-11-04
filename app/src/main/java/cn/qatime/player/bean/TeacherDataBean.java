package cn.qatime.player.bean;

import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2016/11/1 9:30
 * @Description:
 */
public class TeacherDataBean {
    /**
     * status : 1
     * data : {"name":"张老师","desc":"张老师是测试老师，账号是用来测试的.","teaching_years":"within_three_years","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/0c8769a3968a5979286710a867766f0f.JPG","school":"阳泉市第一中学校","courses":[]}
     */

    private int status;
    /**
     * name : 张老师
     * desc : 张老师是测试老师，账号是用来测试的.
     * teaching_years : within_three_years
     * avatar_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/0c8769a3968a5979286710a867766f0f.JPG
     * school : 阳泉市第一中学校
     * courses : []
     */

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
        private String name;
        private String desc;
        private String teaching_years;
        private String avatar_url;
        private String school;
        private List<Course> courses;
        private String gender;
        /**
         * grade : null
         * subject : 数学
         */

        private String grade;
        private String subject;
        private String province;
        private String city;
        private String town;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getGender() {
            return this.gender;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getTeaching_years() {
            return teaching_years;
        }

        public void setTeaching_years(String teaching_years) {
            this.teaching_years = teaching_years;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public List<Course> getCourses() {
            return courses;
        }

        public void setCourses(List<Course> courses) {
            this.courses = courses;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public static class Course{
            private int id;

            private String name;

            private String subject;
            private int buy_tickets_count;

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

            public int getBuy_tickets_count() {
                return buy_tickets_count;
            }

            public void setBuy_tickets_count(int buy_tickets_count) {
                this.buy_tickets_count = buy_tickets_count;
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
