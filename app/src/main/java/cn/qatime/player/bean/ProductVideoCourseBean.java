package cn.qatime.player.bean;

import java.io.Serializable;

import libraryextra.bean.IconsBean;
import libraryextra.bean.TeacherBean;

/**
 * @author Tianhaoranly
 * @date 2017/4/19 13:20
 * @Description:
 */
public class ProductVideoCourseBean implements Serializable{


    private int id;
    private String name;
    private String subject;
    private String grade;
    private String teacher_name;
    private TeacherBean teacher;
    private int price;
    private int current_price;
    private String chat_team_id;
    private String chat_team_owner;
    private int buy_tickets_count;
    private String status;
    private int lesson_count;
    private int video_lessons_count;
    private int preset_lesson_count;
    private int completed_lesson_count;
    private int taste_count;
    private int completed_lessons_count;
    private int closed_lessons_count;
    private String objective;
    private String suit_crowd;
    private String publicize;
    private String sell_type;
    private int total_duration;
    private IconsBean icons;
    private boolean off_shelve;

    public boolean isOff_shelve() {
        return off_shelve;
    }

    public void setOff_shelve(boolean off_shelve) {
        this.off_shelve = off_shelve;
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

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public TeacherBean getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherBean teacher) {
        this.teacher = teacher;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(int current_price) {
        this.current_price = current_price;
    }

    public String getChat_team_id() {
        return chat_team_id;
    }

    public void setChat_team_id(String chat_team_id) {
        this.chat_team_id = chat_team_id;
    }

    public String getChat_team_owner() {
        return chat_team_owner;
    }

    public void setChat_team_owner(String chat_team_owner) {
        this.chat_team_owner = chat_team_owner;
    }

    public int getBuy_tickets_count() {
        return buy_tickets_count;
    }

    public void setBuy_tickets_count(int buy_tickets_count) {
        this.buy_tickets_count = buy_tickets_count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLesson_count() {
        return lesson_count;
    }

    public void setLesson_count(int lesson_count) {
        this.lesson_count = lesson_count;
    }

    public int getVideo_lessons_count() {
        return video_lessons_count;
    }

    public void setVideo_lessons_count(int video_lessons_count) {
        this.video_lessons_count = video_lessons_count;
    }

    public int getPreset_lesson_count() {
        return preset_lesson_count;
    }

    public void setPreset_lesson_count(int preset_lesson_count) {
        this.preset_lesson_count = preset_lesson_count;
    }

    public int getCompleted_lesson_count() {
        return completed_lesson_count;
    }

    public void setCompleted_lesson_count(int completed_lesson_count) {
        this.completed_lesson_count = completed_lesson_count;
    }

    public int getTaste_count() {
        return taste_count;
    }

    public void setTaste_count(int taste_count) {
        this.taste_count = taste_count;
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

    public String getPublicize() {
        return publicize;
    }

    public void setPublicize(String publicize) {
        this.publicize = publicize;
    }

    public String getSell_type() {
        return sell_type;
    }

    public void setSell_type(String sell_type) {
        this.sell_type = sell_type;
    }

    public int getTotal_duration() {
        return total_duration;
    }

    public void setTotal_duration(int total_duration) {
        this.total_duration = total_duration;
    }

    public IconsBean getIcons() {
        return icons;
    }

    public void setIcons(IconsBean icons) {
        this.icons = icons;
    }

}
