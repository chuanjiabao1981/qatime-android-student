package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

import libraryextra.bean.IconsBean;
import libraryextra.bean.TeacherBean;


public class ProductInteractiveCourseBean implements Serializable {

    private String chat_team_id;
    //    private int closed_lessons_count;
    private int started_lessons_count;
    private int created_at;
    private String description;
    private String grade;
    private int id;
    private int lessons_count;
    private String live_end_time;
    private String live_start_time;
    private String name;
    private String objective;
    private String price;
    private String publicize_app_url;
    private String publicize_info_url;
    private String publicize_list_url;
    private String publicize_url;
    private String status;
    private String subject;
    private String suit_crowd;
    private List<TeacherBean> teachers;
    private IconsBean icons;
    private boolean off_shelve;

    public boolean isOff_shelve() {
        return off_shelve;
    }

    public void setOff_shelve(boolean off_shelve) {
        this.off_shelve = off_shelve;
    }

    public int getStarted_lessons_count() {
        return started_lessons_count;
    }

    public void setStarted_lessons_count(int started_lessons_count) {
        this.started_lessons_count = started_lessons_count;
    }

    public String getChat_team_id() {
        return chat_team_id;
    }

    public void setChat_team_id(String chat_team_id) {
        this.chat_team_id = chat_team_id;
    }

//    public int getClosed_lessons_count() {
//        return closed_lessons_count;
//    }
//
//    public void setClosed_lessons_count(int closed_lessons_count) {
//        this.closed_lessons_count = closed_lessons_count;
//    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLessons_count() {
        return lessons_count;
    }

    public void setLessons_count(int lessons_count) {
        this.lessons_count = lessons_count;
    }

    public String getLive_end_time() {
        return live_end_time;
    }

    public void setLive_end_time(String live_end_time) {
        this.live_end_time = live_end_time;
    }

    public String getLive_start_time() {
        return live_start_time;
    }

    public void setLive_start_time(String live_start_time) {
        this.live_start_time = live_start_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPublicize_app_url() {
        return publicize_app_url;
    }

    public void setPublicize_app_url(String publicize_app_url) {
        this.publicize_app_url = publicize_app_url;
    }

    public String getPublicize_info_url() {
        return publicize_info_url;
    }

    public void setPublicize_info_url(String publicize_info_url) {
        this.publicize_info_url = publicize_info_url;
    }

    public String getPublicize_list_url() {
        return publicize_list_url;
    }

    public void setPublicize_list_url(String publicize_list_url) {
        this.publicize_list_url = publicize_list_url;
    }

    public String getPublicize_url() {
        return publicize_url;
    }

    public void setPublicize_url(String publicize_url) {
        this.publicize_url = publicize_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSuit_crowd() {
        return suit_crowd;
    }

    public void setSuit_crowd(String suit_crowd) {
        this.suit_crowd = suit_crowd;
    }

    public List<TeacherBean> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<TeacherBean> teachers) {
        this.teachers = teachers;
    }

    public IconsBean getIcons() {
        return icons;
    }

    public void setIcons(IconsBean icons) {
        this.icons = icons;
    }

}