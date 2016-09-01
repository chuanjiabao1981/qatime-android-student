package cn.qatime.player.bean;


import java.io.Serializable;
import java.util.List;

import libraryextra.bean.OrderConfirmBean;

public class MyOrderBean implements Serializable {
    private int status;

    private List<Data> data;
    private int pay_type;
    private String created_at;

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

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
        private String id;

        private String status;

        private String prepay_id;

        private String nonce_str;

        private OrderConfirmBean.App_pay_params app_pay_params;

        private Product product;

        private int pay_type;
        private String created_at;

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }

        public String getPrepay_id() {
            return this.prepay_id;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getNonce_str() {
            return this.nonce_str;
        }

        public void setApp_pay_params(OrderConfirmBean.App_pay_params app_pay_params) {
            this.app_pay_params = app_pay_params;
        }

        public OrderConfirmBean.App_pay_params getApp_pay_params() {
            return this.app_pay_params;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public Product getProduct() {
            return this.product;
        }

    }

    public class Product implements Serializable {
        private int id;

        private String name;

        private String subject;

        private String grade;

        private String teacher_name;

        private int price;

        private String chat_team_id;

        private int buy_tickets_count;

        private String status;

        private String description;

        private int lesson_count;

        private int preset_lesson_count;

        private int completed_lesson_count;

        private String live_start_time;

        private String live_end_time;

        private String publicize;

        private List<Lessons> lessons;

        private Chat_team chat_team;

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

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getSubject() {
            return this.subject;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getGrade() {
            return this.grade;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getTeacher_name() {
            return this.teacher_name;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getPrice() {
            return this.price;
        }

        public void setChat_team_id(String chat_team_id) {
            this.chat_team_id = chat_team_id;
        }

        public String getChat_team_id() {
            return this.chat_team_id;
        }

        public void setBuy_tickets_count(int buy_tickets_count) {
            this.buy_tickets_count = buy_tickets_count;
        }

        public int getBuy_tickets_count() {
            return this.buy_tickets_count;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }

        public void setLesson_count(int lesson_count) {
            this.lesson_count = lesson_count;
        }

        public int getLesson_count() {
            return this.lesson_count;
        }

        public void setPreset_lesson_count(int preset_lesson_count) {
            this.preset_lesson_count = preset_lesson_count;
        }

        public int getPreset_lesson_count() {
            return this.preset_lesson_count;
        }

        public void setCompleted_lesson_count(int completed_lesson_count) {
            this.completed_lesson_count = completed_lesson_count;
        }

        public int getCompleted_lesson_count() {
            return this.completed_lesson_count;
        }

        public void setLive_start_time(String live_start_time) {
            this.live_start_time = live_start_time;
        }

        public String getLive_start_time() {
            return this.live_start_time;
        }

        public void setLive_end_time(String live_end_time) {
            this.live_end_time = live_end_time;
        }

        public String getLive_end_time() {
            return this.live_end_time;
        }

        public void setPublicize(String publicize) {
            this.publicize = publicize;
        }

        public String getPublicize() {
            return this.publicize;
        }

        public void setLessons(List<Lessons> lessons) {
            this.lessons = lessons;
        }

        public List<Lessons> getLessons() {
            return this.lessons;
        }

        public void setChat_team(Chat_team chat_team) {
            this.chat_team = chat_team;
        }

        public Chat_team getChat_team() {
            return this.chat_team;
        }

    }

    public class Lessons implements Serializable {
        private int id;

        private String name;

        private String status;

        private String class_date;

        private String live_time;

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

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }

        public void setClass_date(String class_date) {
            this.class_date = class_date;
        }

        public String getClass_date() {
            return this.class_date;
        }

        public void setLive_time(String live_time) {
            this.live_time = live_time;
        }

        public String getLive_time() {
            return this.live_time;
        }

    }

    public class Chat_team implements Serializable {
        private String announcement;

        private List<Team_announcements> team_announcements;

        private List<Accounts> accounts;

        public void setAnnouncement(String announcement) {
            this.announcement = announcement;
        }

        public String getAnnouncement() {
            return this.announcement;
        }

        public void setTeam_announcements(List<Team_announcements> team_announcements) {
            this.team_announcements = team_announcements;
        }

        public List<Team_announcements> getTeam_announcements() {
            return this.team_announcements;
        }

        public void setAccounts(List<Accounts> accounts) {
            this.accounts = accounts;
        }

        public List<Accounts> getAccounts() {
            return this.accounts;
        }

    }

    public class Accounts implements Serializable {
        private String accid;

        private String name;

        private String icon;

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getAccid() {
            return this.accid;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getIcon() {
            return this.icon;
        }

    }

    public class Team_announcements implements Serializable {
        private String announcement;

        private String edit_at;

        public void setAnnouncement(String announcement) {
            this.announcement = announcement;
        }

        public String getAnnouncement() {
            return this.announcement;
        }

        public void setEdit_at(String edit_at) {
            this.edit_at = edit_at;
        }

        public String getEdit_at() {
            return this.edit_at;
        }

    }

}
