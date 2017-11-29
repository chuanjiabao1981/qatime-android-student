package cn.qatime.player.bean;

import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2017/6/9 15:48
 * @Description:
 */
public class SearchResultTeacherBean {


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

        private int id;
        private String name;
        private String nick_name;
        private String avatar_url;
        private String gender;
        private String teaching_years;
        private String category;
        private String subject;
        private String school;
        private int total_entries;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getTotal_entries() {
            return total_entries;
        }

        public void setTotal_entries(int total_entries) {
            this.total_entries = total_entries;
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

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }
    }
}
