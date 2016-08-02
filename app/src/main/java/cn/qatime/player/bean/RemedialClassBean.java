package cn.qatime.player.bean;

import com.google.gson.annotations.SerializedName;

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

        private String status;

        private String description;

        private int lesson_count;

        private int preset_lesson_count;

        private int completed_lesson_count;

        private String push_address;

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

        public void setPush_address(String push_address) {
            this.push_address = push_address;
        }

        public String getPush_address() {
            return this.push_address;
        }

    }
}
