package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

import libraryextra.bean.ChatTeamBean;
import libraryextra.bean.IconsBean;
import libraryextra.bean.Lessons;


public class Product implements Serializable {
    private int id;

    private String name;

    private String subject;

    private String grade;

    private String teacher_name;

    public float price;
    public float current_price;

    private String chat_team_id;

    private int buy_tickets_count;

    private String status;

    private String description;

    private int lesson_count;

    private int preset_lesson_count;

    private int closed_lessons_count;

    private String live_start_time;

    private String live_end_time;

    private String publicize;

    private List<Lessons> lessons;

    private ChatTeamBean chat_team;

    private IconsBean icons;

    private boolean off_shelve;

    public boolean isOff_shelve() {
        return off_shelve;
    }

    public void setOff_shelve(boolean off_shelve) {
        this.off_shelve = off_shelve;
    }

    public IconsBean getIcons() {
        return icons;
    }

    public void setIcons(IconsBean icons) {
        this.icons = icons;
    }

    public int getClosed_lessons_count() {
        return closed_lessons_count;
    }

    public void setClosed_lessons_count(int closed_lessons_count) {
        this.closed_lessons_count = closed_lessons_count;
    }

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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(float current_price) {
        this.current_price = current_price;
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

    public void setChat_team(ChatTeamBean chat_team) {
        this.chat_team = chat_team;
    }

    public ChatTeamBean getChat_team() {
        return this.chat_team;
    }

}