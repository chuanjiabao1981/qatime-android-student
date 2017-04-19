package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

import libraryextra.bean.TeacherBean;


public class ProductInteractiveCourseBean implements Serializable {
    /**
     * chat_team_id : 28054274
     * closed_lessons_count : 0
     * completed_lessons_count : 0
     * created_at : 1490940558
     * description : <p>哈哈哈哈哈哈哈&nbsp;</p>
     * grade : 初二
     * id : 2
     * lessons_count : 10
     * live_end_time : 2017-04-10 18:45
     * live_start_time : 2017-04-01 18:00
     * name : 创建10个课程要疯呀
     * objective : 漫无目的的走在大街上，哪里会有目标
     * price : 500.0
     * publicize_app_url : http://testing.qatime.cn/imgs/course_default.png
     * publicize_info_url : http://testing.qatime.cn/imgs/course_default.png
     * publicize_list_url : http://testing.qatime.cn/imgs/course_default.png
     * publicize_url : http://testing.qatime.cn/imgs/course_default.png
     * status : published
     * subject : 化学
     * suit_crowd : 活到老，学到老，学习不分年龄
     * teachers : [{"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ed8858f5ed860b8e94226f37446b89c1.jpg","category":"高中","city":1,"desc":"","email":"qatime@8.cn","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_ed8858f5ed860b8e94226f37446b89c1.jpg","grade_range":[],"id":541,"name":"教师","nick_name":"春意盎然","province":1,"school":1,"subject":"数学","teaching_years":"within_three_years"}]
     */

    private String chat_team_id;
    private int closed_lessons_count;
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


    public String getChat_team_id() {
        return chat_team_id;
    }

    public void setChat_team_id(String chat_team_id) {
        this.chat_team_id = chat_team_id;
    }

    public int getClosed_lessons_count() {
        return closed_lessons_count;
    }

    public void setClosed_lessons_count(int closed_lessons_count) {
        this.closed_lessons_count = closed_lessons_count;
    }

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