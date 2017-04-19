package cn.qatime.player.bean;

import libraryextra.bean.TeacherBean;

/**
 * @author Tianhaoranly
 * @date 2017/4/19 13:20
 * @Description:
 */
public class ProductVideoCourseBean {

    /**
     * id : 3
     * name : 初中考试1
     * subject : 化学
     * grade : 初一
     * teacher_name : 王志成
     * teacher : {"id":2489,"name":"王志成","nick_name":"luke测试","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/8dba956b321d278c40f6b4eb84f05543.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_8dba956b321d278c40f6b4eb84f05543.jpg","login_mobile":"13121249326","email":"wchtest001@163.com","teaching_years":"more_than_twenty_years","category":"小学","subject":"化学","grade_range":["二年级","三年级","四年级","五年级","六年级",""],"gender":"male","birthday":"1991-06-11","province":1,"city":1,"school":3,"desc":"请注意 ，这是开发人员的测试账号！"}
     * price : 300
     * current_price : 300
     * chat_team_id : 31975075
     * chat_team_owner : 07b7c43a854ed44d36c2941f1fc5ad00
     * buy_tickets_count : 2
     * status : published
     * lesson_count : 2
     * video_lessons_count : 2
     * preset_lesson_count : 2
     * completed_lesson_count : 0
     * taste_count : 0
     * completed_lessons_count : 0
     * closed_lessons_count : 0
     * objective : null
     * suit_crowd : null
     * publicize : /assets/video_courses/list_default-d30bb0d64e2d9ea1d59d5214f7b4613a.png
     * sell_type : charge
     * total_duration : 0
     * icons : {"free_taste":false,"coupon_free":true,"cheap_moment":false}
     */

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
    private Object objective;
    private Object suit_crowd;
    private String publicize;
    private String sell_type;
    private int total_duration;
    private IconsBean icons;

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

    public Object getObjective() {
        return objective;
    }

    public void setObjective(Object objective) {
        this.objective = objective;
    }

    public Object getSuit_crowd() {
        return suit_crowd;
    }

    public void setSuit_crowd(Object suit_crowd) {
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
