package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

import libraryextra.bean.IconsBean;
import libraryextra.bean.TeacherBean;

/**
 * @author lungtify
 * @Time 2017/3/21 14:20
 * @Describe 精选内容
 */

public class EssenceContentBean implements Serializable {


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


        private int index;
        private LiveStudioCourseBean live_studio_course;
        private String logo_url;
        private String tag_one;
        private int target_id;
        private String target_type;
        private String title;
        private String type;
        private LiveStudioVideoCourseBean live_studio_video_course;
        private LiveStudioInteractiveCourseBean live_studio_interactive_course;

        public LiveStudioVideoCourseBean getLive_studio_video_course() {
            return live_studio_video_course;
        }

        public void setLive_studio_video_course(LiveStudioVideoCourseBean live_studio_video_course) {
            this.live_studio_video_course = live_studio_video_course;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public LiveStudioCourseBean getLive_studio_course() {
            return live_studio_course;
        }

        public void setLive_studio_course(LiveStudioCourseBean live_studio_course) {
            this.live_studio_course = live_studio_course;
        }

        public String getLogo_url() {
            return logo_url;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }

        public String getTag_one() {
            return tag_one;
        }

        public void setTag_one(String tag_one) {
            this.tag_one = tag_one;
        }

        public int getTarget_id() {
            return target_id;
        }

        public void setTarget_id(int target_id) {
            this.target_id = target_id;
        }

        public String getTarget_type() {
            return target_type;
        }

        public void setTarget_type(String target_type) {
            this.target_type = target_type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public LiveStudioInteractiveCourseBean getLive_studio_interactive_course() {
            return live_studio_interactive_course;
        }

        public void setLive_studio_interactive_course(LiveStudioInteractiveCourseBean live_studio_interactive_course) {
            this.live_studio_interactive_course = live_studio_interactive_course;
        }

        public static class LiveStudioCourseBean {
            /**
             * buy_tickets_count : 0
             * chat_team_id : 25191774
             * chat_team_owner : 8b1ea01a546dab8c1fbb4de519f4c1ff
             * closed_lessons_count : 0
             * completed_lesson_count : 0
             * completed_lessons_count : 0
             * current_price : 600
             * grade : 六年级
             * icons : {"cheap_moment":false,"coupon_free":true,"free_taste":false,"join_cheap":false,"refund_any_time":true}
             * id : 67
             * live_end_time : 2016-11-21 11:47
             * live_start_time : 2016-11-21 08:47
             * name : asd as das das
             * preset_lesson_count : 1
             * price : 600
             * publicize : http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_204bb7f889f10173b9ec10a03726cc3d.jpg
             * status : rejected
             * subject : 数学
             * taste_count : 0
             * teacher_name : 荣
             */

            private int buy_tickets_count;
            private String chat_team_id;
            private String chat_team_owner;
            private int closed_lessons_count;
            private int completed_lesson_count;
            private int completed_lessons_count;
            private String current_price;
            private String grade;
            private IconsBean icons;
            private int id;
            private String live_end_time;
            private String live_start_time;
            private String name;
            private int preset_lesson_count;
            private String price;
            private String publicize;
            private String status;
            private String subject;
            private int taste_count;
            private String teacher_name;


            public int getBuy_tickets_count() {
                return buy_tickets_count;
            }

            public void setBuy_tickets_count(int buy_tickets_count) {
                this.buy_tickets_count = buy_tickets_count;
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

            public int getClosed_lessons_count() {
                return closed_lessons_count;
            }

            public void setClosed_lessons_count(int closed_lessons_count) {
                this.closed_lessons_count = closed_lessons_count;
            }

            public int getCompleted_lesson_count() {
                return completed_lesson_count;
            }

            public void setCompleted_lesson_count(int completed_lesson_count) {
                this.completed_lesson_count = completed_lesson_count;
            }

            public int getCompleted_lessons_count() {
                return completed_lessons_count;
            }

            public void setCompleted_lessons_count(int completed_lessons_count) {
                this.completed_lessons_count = completed_lessons_count;
            }

            public String getCurrent_price() {
                return current_price;
            }

            public void setCurrent_price(String current_price) {
                this.current_price = current_price;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public IconsBean getIcons() {
                return icons;
            }

            public void setIcons(IconsBean icons) {
                this.icons = icons;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public int getPreset_lesson_count() {
                return preset_lesson_count;
            }

            public void setPreset_lesson_count(int preset_lesson_count) {
                this.preset_lesson_count = preset_lesson_count;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getPublicize() {
                return publicize;
            }

            public void setPublicize(String publicize) {
                this.publicize = publicize;
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

            public int getTaste_count() {
                return taste_count;
            }

            public void setTaste_count(int taste_count) {
                this.taste_count = taste_count;
            }

            public String getTeacher_name() {
                return teacher_name;
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }

        }

        public static class LiveStudioInteractiveCourseBean {
            /**
             * chat_team_id : 28299876
             * closed_lessons_count : 0
             * completed_lessons_count : 0
             * created_at : 1491039171
             * description : <p>一对一学习好</p>
             * grade : 高一
             * icons : {"cheap_moment":false,"coupon_free":true,"refund_any_time":true}
             * id : 4
             * lessons_count : 10
             * live_end_time : 2017-04-24 05:00
             * live_start_time : 2017-04-01 20:10
             * name : 刘刚老师一对一
             * objective : 学好励志
             * price : 900.0
             * publicize_app_url : /assets/interactive_courses/app_info_default-2747aad8d6f3bda42a47964d5030a8f8.png
             * publicize_info_url : /assets/interactive_courses/info_default-2696b4c40a5ea2383bbc733e323ab735.png
             * publicize_list_url : /assets/interactive_courses/list_default-214e337ab4433dba9eb86dc0c4cbbee4.png
             * publicize_url : /interactive_courses/default.png
             * status : init
             * subject : 历史
             * suit_crowd : 大中小人群
             * teachers : [{"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/9b72b8587267d4b7890aa79f9daf6250.jpeg","category":"初中","city":1,"desc":"","email":"944120834@qq.com","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_9b72b8587267d4b7890aa79f9daf6250.jpeg","grade_range":[],"id":2827,"login_mobile":"17854272721","name":"蔡群萍","nick_name":"菜菜0","province":1,"school":16,"school_id":16,"subject":"英语","teaching_years":"within_three_years"},{"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/1b1c3c2f5f8b1b182678ca87b156804f.jpeg","category":"小学","city":2,"desc":"","email":"1040459160@qq.com","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_1b1c3c2f5f8b1b182678ca87b156804f.jpeg","grade_range":[],"id":595,"login_mobile":"13967572323","name":"斯瑜科","nick_name":"小可儿科","province":1,"school":126,"school_id":126,"subject":"语文","teaching_years":"within_three_years"},{"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/993289f3ebc994fbd699f38b9b9f0808.jpeg","category":"初中","city":1,"desc":"","email":"xulinno1@sohu.com","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_993289f3ebc994fbd699f38b9b9f0808.jpeg","grade_range":[],"id":2837,"name":"徐林","nick_name":"徐林no1","province":1,"school":13,"school_id":13,"subject":"数学","teaching_years":"within_three_years"},{"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/f9c2b225a5fd296a010c03666b543e38.jpg","birthday":"2017-03-02","category":"高中","city":606,"desc":"啊撒旦发送的发啊说是道非","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_f9c2b225a5fd296a010c03666b543e38.jpg","gender":"male","grade_range":[],"id":3056,"login_mobile":"13212345678","name":"刘刚老师","nick_name":"昵称刘刚","province":25,"school":140,"school_id":140,"subject":"数学","teaching_years":"within_three_years"},{"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/9f7949d37910ec05ff1ba833036d64ab.jpg","category":"高中","city":1,"desc":"","email":"shibazhong@163.com","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_9f7949d37910ec05ff1ba833036d64ab.jpg","grade_range":[],"id":1625,"name":"HZH4","nick_name":"答疑时间客服老师4","province":1,"school":27,"school_id":27,"subject":"数学","teaching_years":"within_three_years"},{"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ac7b10a5132677c95751935e8e8ffde3.jpg","birthday":"1991-06-11","category":"小学","city":2,"desc":"请注意 ，这是开发人员的测试账号！","email":"wchtest001@163.com","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_ac7b10a5132677c95751935e8e8ffde3.jpg","gender":"male","grade_range":["二年级","三年级","四年级","五年级","六年级",""],"id":2489,"login_mobile":"13121249326","name":"王志成","nick_name":"luke测试","province":1,"school":3,"school_id":3,"subject":"化学","teaching_years":"more_than_twenty_years"},{"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/1075d8573e60bdf48b4d150cb3278838.jpg","category":"初中","city":2,"desc":"","email":"1315708753@qq.com","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_1075d8573e60bdf48b4d150cb3278838.jpg","grade_range":[],"id":2823,"login_mobile":"15868506228","name":"胡柳潇","nick_name":"闹够啦","province":1,"school":108,"school_id":108,"subject":"数学","teaching_years":"within_three_years"},{"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/02688dc06d375f1261df57c2e50e7684.jpg","category":"初中","city":2,"desc":"","email":"837787173@qq.com","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_02688dc06d375f1261df57c2e50e7684.jpg","grade_range":[],"id":2838,"login_mobile":"15925888089","name":"何丽萍","nick_name":"yyeexx","province":1,"school":110,"school_id":110,"subject":"数学","teaching_years":"within_three_years"},{"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ba50fd6aa6a3b3b1f2d9fc47d9949f17.jpg","category":"高中","city":1,"desc":"","email":"shiliuzhong@chusan.cn","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_ba50fd6aa6a3b3b1f2d9fc47d9949f17.jpg","grade_range":[],"id":1225,"name":"HZH3","nick_name":"答疑时间客服老师3","province":1,"school":1,"school_id":1,"subject":"数学","teaching_years":"within_three_years"},{"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/92103b87195f88d509139278e7821294.jpg","category":"初中","city":2,"desc":"主讲数学，也擅长化学物理","email":"2225664831@qq.com","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_92103b87195f88d509139278e7821294.jpg","grade_range":[],"id":2834,"login_mobile":"15957169683","name":"寿浙辉","nick_name":"VampireSH","province":1,"school":77,"school_id":77,"subject":"数学","teaching_years":"within_three_years"}]
             */

            private String chat_team_id;
            private int closed_lessons_count;
            private int completed_lessons_count;
            private int created_at;
            private String description;
            private String grade;
            private IconsBean icons;
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
            private String teacher_name;

            public String getTeacher_name() {
                return teacher_name;
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }

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

            public int getCompleted_lessons_count() {
                return completed_lessons_count;
            }

            public void setCompleted_lessons_count(int completed_lessons_count) {
                this.completed_lessons_count = completed_lessons_count;
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

            public IconsBean getIcons() {
                return icons;
            }

            public void setIcons(IconsBean icons) {
                this.icons = icons;
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

        }
        public static class LiveStudioVideoCourseBean {
            /**
             * id : 24
             * name : 萨哈拉以南的非洲及欧洲西部
             * subject : 地理
             * grade : 初一
             * teacher_name : 郭老师
             * teacher : {"id":3082,"name":"郭老师","nick_name":"","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/25ab2f5d5cfa0a095e85eec598ce84fa.png","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_25ab2f5d5cfa0a095e85eec598ce84fa.png","login_mobile":"15603536985","email":null,"teaching_years":"within_three_years","category":"初中","subject":"地理","grade_range":[],"gender":null,"birthday":null,"province":1,"city":1,"school":45,"school_id":45,"desc":"干练 懂礼貌"}
             * price : 0
             * current_price : 0
             * chat_team_id :
             * chat_team_owner : 38d3781df49162079cc13baceffabbb0
             * buy_tickets_count : 0
             * status : init
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
             * total_duration : 3528
             * icons : {"free_taste":false,"coupon_free":true,"cheap_moment":false}
             */

            private int id;
            private String name;
            private String subject;
            private String grade;
            private String teacher_name;
            private TeacherBean teacher;
            private String price;
            private String current_price;
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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getCurrent_price() {
                return current_price;
            }

            public void setCurrent_price(String current_price) {
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
    }
}
