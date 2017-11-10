package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

import libraryextra.bean.IconsBean;
import libraryextra.bean.TeacherBean;

/**
 * @author lungtify
 * @Time 2017/7/13 17:33
 * @Describe 直播课详情新接口数据
 */

public class LiveLessonDetailBean implements Serializable {

    /**
     * status : 1
     * data : {"course":{"id":140,"name":"王者荣耀物理攻击详解","subject":"物理","grade":"高三","teacher_name":"李老师","publicize":"http://testing.qatime.cn/assets/courses/physics/app_info_default-eec12329ec9ad350d788f1a66a6cd473.png","teacher":{"id":3178,"name":"李老师","nick_name":null,"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/8e3b09a4ebe9da86f6bde2044d4002ad.png","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_8e3b09a4ebe9da86f6bde2044d4002ad.png","login_mobile":"17600209160","email":null,"is_guest":false,"teaching_years":"within_ten_years","category":"高中","subject":"数学","grade_range":[],"gender":null,"birthday":null,"province":1,"city":1,"school":151,"school_name":"阳泉一中","school_id":151,"desc":"学而不厌;诲人不倦;何有于我哉"},"price":0,"current_price":0,"chat_team_id":"67458307","chat_team_owner":"5cc4d282b255ede6a9380528c5a39f28","buy_tickets_count":5,"status":"teaching","description":"<p>晚上不睡觉，一起上王者<\/p>","tag_list":["自编试卷"],"lessons_count":3,"preset_lesson_count":0,"taste_count":0,"completed_lessons_count":1,"closed_lessons_count":3,"started_lessons_count":3,"live_start_time":"2017-07-11 17:19","live_end_time":"2017-07-13 16:46","objective":"理解物理攻击原理","suit_crowd":"小学生","teacher_percentage":0,"lessons":[{"id":487,"name":"第一节","status":"completed","course_id":140,"real_time":540,"pos":0,"class_date":"2017-07-11","live_time":"20:00-21:00","replayable":true,"left_replay_times":0},{"id":488,"name":"第二节","status":"finished","course_id":140,"real_time":8974,"pos":0,"class_date":"2017-07-12","live_time":"18:00-19:00","replayable":false,"left_replay_times":0},{"id":489,"name":"第三节","status":"closed","course_id":140,"real_time":0,"pos":0,"class_date":"2017-07-13","live_time":"18:00-19:00","replayable":true,"left_replay_times":0}],"icons":{"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":true,"free_taste":false},"off_shelve":false,"taste_overflow":true,"sell_type":"free","tastable":false},"ticket":{"id":793,"used_count":0,"buy_count":2,"lesson_price":"0.0","status":"active","type":"LiveStudio::BuyTicket"}}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * course : {"id":140,"name":"王者荣耀物理攻击详解","subject":"物理","grade":"高三","teacher_name":"李老师","publicize":"http://testing.qatime.cn/assets/courses/physics/app_info_default-eec12329ec9ad350d788f1a66a6cd473.png","teacher":{"id":3178,"name":"李老师","nick_name":null,"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/8e3b09a4ebe9da86f6bde2044d4002ad.png","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_8e3b09a4ebe9da86f6bde2044d4002ad.png","login_mobile":"17600209160","email":null,"is_guest":false,"teaching_years":"within_ten_years","category":"高中","subject":"数学","grade_range":[],"gender":null,"birthday":null,"province":1,"city":1,"school":151,"school_name":"阳泉一中","school_id":151,"desc":"学而不厌;诲人不倦;何有于我哉"},"price":0,"current_price":0,"chat_team_id":"67458307","chat_team_owner":"5cc4d282b255ede6a9380528c5a39f28","buy_tickets_count":5,"status":"teaching","description":"<p>晚上不睡觉，一起上王者<\/p>","tag_list":["自编试卷"],"lessons_count":3,"preset_lesson_count":0,"taste_count":0,"completed_lessons_count":1,"closed_lessons_count":3,"started_lessons_count":3,"live_start_time":"2017-07-11 17:19","live_end_time":"2017-07-13 16:46","objective":"理解物理攻击原理","suit_crowd":"小学生","teacher_percentage":0,"lessons":[{"id":487,"name":"第一节","status":"completed","course_id":140,"real_time":540,"pos":0,"class_date":"2017-07-11","live_time":"20:00-21:00","replayable":true,"left_replay_times":0},{"id":488,"name":"第二节","status":"finished","course_id":140,"real_time":8974,"pos":0,"class_date":"2017-07-12","live_time":"18:00-19:00","replayable":false,"left_replay_times":0},{"id":489,"name":"第三节","status":"closed","course_id":140,"real_time":0,"pos":0,"class_date":"2017-07-13","live_time":"18:00-19:00","replayable":true,"left_replay_times":0}],"icons":{"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":true,"free_taste":false},"off_shelve":false,"taste_overflow":true,"sell_type":"free","tastable":false}
         * ticket : {"id":793,"used_count":0,"buy_count":2,"lesson_price":"0.0","status":"active","type":"LiveStudio::BuyTicket"}
         */

        private CourseBean course;
        private TicketBean ticket;

        public CourseBean getCourse() {
            return course;
        }

        public void setCourse(CourseBean course) {
            this.course = course;
        }

        public TicketBean getTicket() {
            return ticket;
        }

        public void setTicket(TicketBean ticket) {
            this.ticket = ticket;
        }

        public static class CourseBean {
            /**
             * id : 140
             * name : 王者荣耀物理攻击详解
             * subject : 物理
             * grade : 高三
             * teacher_name : 李老师
             * publicize : http://testing.qatime.cn/assets/courses/physics/app_info_default-eec12329ec9ad350d788f1a66a6cd473.png
             * teacher : {"id":3178,"name":"李老师","nick_name":null,"avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/8e3b09a4ebe9da86f6bde2044d4002ad.png","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_8e3b09a4ebe9da86f6bde2044d4002ad.png","login_mobile":"17600209160","email":null,"is_guest":false,"teaching_years":"within_ten_years","category":"高中","subject":"数学","grade_range":[],"gender":null,"birthday":null,"province":1,"city":1,"school":151,"school_name":"阳泉一中","school_id":151,"desc":"学而不厌;诲人不倦;何有于我哉"}
             * price : 0
             * current_price : 0
             * chat_team_id : 67458307
             * chat_team_owner : 5cc4d282b255ede6a9380528c5a39f28
             * buy_tickets_count : 5
             * status : teaching
             * description : <p>晚上不睡觉，一起上王者</p>
             * tag_list : ["自编试卷"]
             * lessons_count : 3
             * preset_lesson_count : 0
             * taste_count : 0
             * completed_lessons_count : 1
             * closed_lessons_count : 3
             * started_lessons_count : 3
             * live_start_time : 2017-07-11 17:19
             * live_end_time : 2017-07-13 16:46
             * objective : 理解物理攻击原理
             * suit_crowd : 小学生
             * teacher_percentage : 0
             * lessons : [{"id":487,"name":"第一节","status":"completed","course_id":140,"real_time":540,"pos":0,"class_date":"2017-07-11","live_time":"20:00-21:00","replayable":true,"left_replay_times":0},{"id":488,"name":"第二节","status":"finished","course_id":140,"real_time":8974,"pos":0,"class_date":"2017-07-12","live_time":"18:00-19:00","replayable":false,"left_replay_times":0},{"id":489,"name":"第三节","status":"closed","course_id":140,"real_time":0,"pos":0,"class_date":"2017-07-13","live_time":"18:00-19:00","replayable":true,"left_replay_times":0}]
             * icons : {"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":true,"free_taste":false}
             * off_shelve : false
             * taste_overflow : true
             * sell_type : free
             * tastable : false
             */

            private int id;
            private String name;
            private String subject;
            private String grade;
            private String teacher_name;
            private TeacherBean teacher;
            private float price;
            private float current_price;
            private int buy_tickets_count;
            private String status;
            private String description;
            private int lessons_count;
            private int preset_lesson_count;
            private int taste_count;
            private int completed_lessons_count;
            private int closed_lessons_count;
            private int started_lessons_count;
            private String live_start_time;
            private String live_end_time;
            private String objective;
            private String suit_crowd;
            private int teacher_percentage;
            private IconsBean icons;
            private boolean off_shelve;
            private boolean taste_overflow;
            private String sell_type;
            private boolean tastable;
            private List<String> tag_list;
            private List<LessonsBean> lessons;

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

            public int getLessons_count() {
                return lessons_count;
            }

            public void setLessons_count(int lessons_count) {
                this.lessons_count = lessons_count;
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

            public int getStarted_lessons_count() {
                return started_lessons_count;
            }

            public void setStarted_lessons_count(int started_lessons_count) {
                this.started_lessons_count = started_lessons_count;
            }

            public String getLive_start_time() {
                return live_start_time;
            }

            public void setLive_start_time(String live_start_time) {
                this.live_start_time = live_start_time;
            }

            public String getLive_end_time() {
                return live_end_time;
            }

            public void setLive_end_time(String live_end_time) {
                this.live_end_time = live_end_time;
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

            public int getTeacher_percentage() {
                return teacher_percentage;
            }

            public void setTeacher_percentage(int teacher_percentage) {
                this.teacher_percentage = teacher_percentage;
            }

            public IconsBean getIcons() {
                return icons;
            }

            public void setIcons(IconsBean icons) {
                this.icons = icons;
            }

            public boolean isOff_shelve() {
                return off_shelve;
            }

            public void setOff_shelve(boolean off_shelve) {
                this.off_shelve = off_shelve;
            }

            public boolean isTaste_overflow() {
                return taste_overflow;
            }

            public void setTaste_overflow(boolean taste_overflow) {
                this.taste_overflow = taste_overflow;
            }

            public String getSell_type() {
                return sell_type;
            }

            public void setSell_type(String sell_type) {
                this.sell_type = sell_type;
            }

            public boolean isTastable() {
                return tastable;
            }

            public void setTastable(boolean tastable) {
                this.tastable = tastable;
            }

            public List<String> getTag_list() {
                return tag_list;
            }

            public void setTag_list(List<String> tag_list) {
                this.tag_list = tag_list;
            }

            public List<LessonsBean> getLessons() {
                return lessons;
            }

            public void setLessons(List<LessonsBean> lessons) {
                this.lessons = lessons;
            }

            public static class LessonsBean {
                /**
                 * id : 487
                 * name : 第一节
                 * status : completed
                 * course_id : 140
                 * real_time : 540
                 * pos : 0
                 * class_date : 2017-07-11
                 * live_time : 20:00-21:00
                 * replayable : true
                 * left_replay_times : 0
                 */

                private int id;
                private String name;
                private String status;
                private int course_id;
                private int real_time;
                private int pos;
                private String class_date;
                private String live_time;
                private boolean replayable;
                private int left_replay_times;

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

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public int getCourse_id() {
                    return course_id;
                }

                public void setCourse_id(int course_id) {
                    this.course_id = course_id;
                }

                public int getReal_time() {
                    return real_time;
                }

                public void setReal_time(int real_time) {
                    this.real_time = real_time;
                }

                public int getPos() {
                    return pos;
                }

                public void setPos(int pos) {
                    this.pos = pos;
                }

                public String getClass_date() {
                    return class_date;
                }

                public void setClass_date(String class_date) {
                    this.class_date = class_date;
                }

                public String getLive_time() {
                    return live_time;
                }

                public void setLive_time(String live_time) {
                    this.live_time = live_time;
                }

                public boolean isReplayable() {
                    return replayable;
                }

                public void setReplayable(boolean replayable) {
                    this.replayable = replayable;
                }

                public int getLeft_replay_times() {
                    return left_replay_times;
                }

                public void setLeft_replay_times(int left_replay_times) {
                    this.left_replay_times = left_replay_times;
                }
            }
        }

        public static class TicketBean {
            /**
             * id : 793
             * used_count : 0
             * buy_count : 2
             * lesson_price : 0.0
             * status : active
             * type : LiveStudio::BuyTicket
             */

            private int id;
            private int used_count;
            private int buy_count;
            private String lesson_price;
            private String status;
            private String type;

            public TicketBean(String type) {
                this.type = type;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUsed_count() {
                return used_count;
            }

            public void setUsed_count(int used_count) {
                this.used_count = used_count;
            }

            public int getBuy_count() {
                return buy_count;
            }

            public void setBuy_count(int buy_count) {
                this.buy_count = buy_count;
            }

            public String getLesson_price() {
                return lesson_price;
            }

            public void setLesson_price(String lesson_price) {
                this.lesson_price = lesson_price;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
