package cn.qatime.player.bean;

import java.util.List;

import libraryextra.bean.ChatTeamBean;
import libraryextra.bean.TeacherBean;
import libraryextra.bean.VideoLessonsBean;


/**
 * @author Tianhaoranly
 * @date 2017/4/14 17:19
 * @Description:
 */
public class FilterVideoCourseBean {

    /**
     * status : 1
     * data : [{"id":3,"name":"初中考试1","subject":"化学","grade":"初一","teacher_name":"王志成","teacher":{"id":2489,"name":"王志成","nick_name":"luke测试","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/8dba956b321d278c40f6b4eb84f05543.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_8dba956b321d278c40f6b4eb84f05543.jpg","login_mobile":"13121249326","email":"wchtest001@163.com","teaching_years":"more_than_twenty_years","category":"小学","subject":"化学","grade_range":["二年级","三年级","四年级","五年级","六年级",""],"gender":"male","birthday":"1991-06-11","province":1,"city":1,"school":3,"desc":"请注意 ，这是开发人员的测试账号！"},"price":300,"current_price":300,"chat_team_id":"31975075","chat_team_owner":"07b7c43a854ed44d36c2941f1fc5ad00","buy_tickets_count":0,"status":"published","description":"<p>初中考试1初中考试1初中考试1初中考试1初中考试1<br><\/p>","tag_list":[],"lesson_count":2,"video_lessons_count":2,"preset_lesson_count":2,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"closed_lessons_count":0,"objective":null,"suit_crowd":null,"publicize":"/assets/video_courses/list_default-3968830f4ea85ff7da98ab57295ececc.png","video_lessons":[{"id":4,"name":"第一学时","status":"init","video_course_id":3,"pos":1,"video":{"id":7767,"token":"1492157139451","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/96fcf0349390a9ddccd8871c868d070a.jpg"}},{"id":5,"name":"第二学时","status":"init","video_course_id":3,"pos":2,"video":{"id":7768,"token":"1492157167944","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b574edcbab5c0c9ce60035847b1d11f7.jpg"}}],"chat_team":{"announcement":null,"team_id":"31975075","team_announcements":[],"accounts":[]},"sell_type":"charge"}]
     */

    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
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
         * buy_tickets_count : 0
         * status : published
         * description : <p>初中考试1初中考试1初中考试1初中考试1初中考试1<br></p>
         * tag_list : []
         * lesson_count : 2
         * video_lessons_count : 2
         * preset_lesson_count : 2
         * completed_lesson_count : 0
         * taste_count : 0
         * completed_lessons_count : 0
         * closed_lessons_count : 0
         * objective : null
         * suit_crowd : null
         * publicize : /assets/video_courses/list_default-3968830f4ea85ff7da98ab57295ececc.png
         * video_lessons : [{"id":4,"name":"第一学时","status":"init","video_course_id":3,"pos":1,"video":{"id":7767,"token":"1492157139451","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/96fcf0349390a9ddccd8871c868d070a.jpg"}},{"id":5,"name":"第二学时","status":"init","video_course_id":3,"pos":2,"video":{"id":7768,"token":"1492157167944","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b574edcbab5c0c9ce60035847b1d11f7.jpg"}}]
         * chat_team : {"announcement":null,"team_id":"31975075","team_announcements":[],"accounts":[]}
         * sell_type : charge
         */

        private int id;
        private String name;
        private String subject;
        private String grade;
        private String teacher_name;
        private TeacherBean teacher;
        private float price;
        private float current_price;
        private String chat_team_id;
        private String chat_team_owner;
        private int buy_tickets_count;
        private String status;
        private String description;
        private int lesson_count;
        private int video_lessons_count;
        private int preset_lesson_count;
        private int taste_count;
        private int closed_lessons_count;
        private String objective;
        private String suit_crowd;
        private String publicize;
        private ChatTeamBean chat_team;
        private String sell_type;
        private List<String> tag_list;
        private List<VideoLessonsBean> video_lessons;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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



        public int getTaste_count() {
            return taste_count;
        }

        public void setTaste_count(int taste_count) {
            this.taste_count = taste_count;
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

        public ChatTeamBean getChat_team() {
            return chat_team;
        }

        public void setChat_team(ChatTeamBean chat_team) {
            this.chat_team = chat_team;
        }

        public String getSell_type() {
            return sell_type;
        }

        public void setSell_type(String sell_type) {
            this.sell_type = sell_type;
        }

        public List<String> getTag_list() {
            return tag_list;
        }

        public void setTag_list(List<String> tag_list) {
            this.tag_list = tag_list;
        }

        public List<VideoLessonsBean> getVideo_lessons() {
            return video_lessons;
        }

        public void setVideo_lessons(List<VideoLessonsBean> video_lessons) {
            this.video_lessons = video_lessons;
        }
    }
}
