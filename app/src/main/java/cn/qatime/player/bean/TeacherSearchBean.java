package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/6/12 10:08
 * @Describe
 */

public class TeacherSearchBean implements Serializable {

    /**
     * status : 1
     * data : [{"id":3163,"name":"荣思杰","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/0b608ed2ae8611b3c97a48d5f8bffcab.png","teaching_years":"within_twenty_years","category":"高中","subject":"生物","gender":null,"province":"广西壮族自治区","city":"宜州市","school":"清华大学"}]
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
         * id : 3163
         * name : 荣思杰
         * avatar_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/0b608ed2ae8611b3c97a48d5f8bffcab.png
         * teaching_years : within_twenty_years
         * category : 高中
         * subject : 生物
         * gender : null
         * province : 广西壮族自治区
         * city : 宜州市
         * school : 清华大学
         */

        private int id;
        private String name;
        private String avatar_url;
        private String ex_big_avatar_url;
        private String teaching_years;
        private String category;
        private String subject;
        private Object gender;
        private String province;
        private String city;
        private String school;

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

        public String getEx_big_avatar_url() {
            return ex_big_avatar_url;
        }

        public void setEx_big_avatar_url(String ex_big_avatar_url) {
            this.ex_big_avatar_url = ex_big_avatar_url;
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

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

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

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }
    }
}
