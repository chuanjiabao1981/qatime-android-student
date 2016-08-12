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

        private String teacher_name;

        private int price;

        private int buy_tickets_count;

        private String publicize;

        private boolean is_tasting;

        private boolean is_bought;

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

        public void setBuy_tickets_count(int buy_tickets_count) {
            this.buy_tickets_count = buy_tickets_count;
        }

        public int getBuy_tickets_count() {
            return this.buy_tickets_count;
        }

        public void setPublicize(String publicize) {
            this.publicize = publicize;
        }

        public String getPublicize() {
            return this.publicize;
        }

        public void setIs_tasting(boolean is_tasting) {
            this.is_tasting = is_tasting;
        }

        public boolean getIs_tasting() {
            return this.is_tasting;
        }

        public void setIs_bought(boolean is_bought) {
            this.is_bought = is_bought;
        }

        public boolean getIs_bought() {
            return this.is_bought;
        }

    }
}
