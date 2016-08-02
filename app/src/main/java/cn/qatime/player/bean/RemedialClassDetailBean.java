package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

public class RemedialClassDetailBean implements Serializable {
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

        private String subject;

        private String status;

        private String description;

        private int lesson_count;

        private int preset_lesson_count;

        private int completed_lesson_count;

        private String push_address;

        private List<Lessons> lessons;

        private Teacher teacher;

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

        public void setLessons(List<Lessons> lessons) {
            this.lessons = lessons;
        }

        public List<Lessons> getLessons() {
            return this.lessons;
        }

        public void setTeacher(Teacher teacher) {
            this.teacher = teacher;
        }

        public Teacher getTeacher() {
            return this.teacher;
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

    public class Teacher implements Serializable {
        private int id;

        private String name;

        private String nick_name;

        private String small_avatar_url;

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

    }
}
