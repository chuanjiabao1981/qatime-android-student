package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * @author luntify
 * @date 2016/8/11 20:06
 * @Description 个人信息
 */
public class PersonalInformationBean implements Serializable {

    private int status;

    private Data data;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }


    public class Data implements Serializable {
        private int id;

        private String name;

        private String nick_name;

        private String small_avatar_url;

        private String gender;

        private String birthday;

        private String grade;

        private String province;

        private String city;

        private int school;

        private String desc;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public String getNick_name() {
            return this.nick_name;
        }

        public void setSmall_avatar_url(String small_avatar_url) {
            this.small_avatar_url = small_avatar_url;
        }

        public String getSmall_avatar_url() {
            return this.small_avatar_url;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getGender() {
            return this.gender;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getBirthday() {
            return this.birthday;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getGrade() {
            return this.grade;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getProvince() {
            return this.province;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity() {
            return this.city;
        }

        public void setSchool(int school) {
            this.school = school;
        }

        public int getSchool() {
            return this.school;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return this.desc;
        }

    }
}
