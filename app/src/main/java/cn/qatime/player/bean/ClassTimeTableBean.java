package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author luntify
 * @date 2016/8/26 16:23
 * @Description
 */
public class ClassTimeTableBean implements Serializable {


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

        private String date;
        private List<LessonsBean> lessons;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<LessonsBean> getLessons() {
            return lessons;
        }

        public void setLessons(List<LessonsBean> lessons) {
            this.lessons = lessons;
        }

        public static class LessonsBean {
            private String board;
            private String board_pull_stream;
            private String camera;
            private String camera_pull_stream;
            private String chat_team_id;
            private String class_date;
            private String course_name;
            private String course_publicize;
            private String grade;
            private int id;
            private int left_replay_times;
            private String lesson_name;
            private String live_time;
            private String modal_type;
            private String name;
            private ProductCourseBean product_course;
            private int product_id;
            private String pull_address;
            private boolean replayable;
            private String status;
            private String subject;
            private String teacher_name;
            private ProductInteractiveCourseBean product_interactive_course;

            public String getBoard() {
                return board;
            }

            public void setBoard(String board) {
                this.board = board;
            }

            public String getBoard_pull_stream() {
                return board_pull_stream;
            }

            public void setBoard_pull_stream(String board_pull_stream) {
                this.board_pull_stream = board_pull_stream;
            }

            public String getCamera() {
                return camera;
            }

            public void setCamera(String camera) {
                this.camera = camera;
            }

            public String getCamera_pull_stream() {
                return camera_pull_stream;
            }

            public void setCamera_pull_stream(String camera_pull_stream) {
                this.camera_pull_stream = camera_pull_stream;
            }

            public String getChat_team_id() {
                return chat_team_id;
            }

            public void setChat_team_id(String chat_team_id) {
                this.chat_team_id = chat_team_id;
            }

            public String getClass_date() {
                return class_date;
            }

            public void setClass_date(String class_date) {
                this.class_date = class_date;
            }

            public String getCourse_name() {
                return course_name;
            }

            public void setCourse_name(String course_name) {
                this.course_name = course_name;
            }

            public String getCourse_publicize() {
                return course_publicize;
            }

            public void setCourse_publicize(String course_publicize) {
                this.course_publicize = course_publicize;
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

            public int getLeft_replay_times() {
                return left_replay_times;
            }

            public void setLeft_replay_times(int left_replay_times) {
                this.left_replay_times = left_replay_times;
            }

            public String getLesson_name() {
                return lesson_name;
            }

            public void setLesson_name(String lesson_name) {
                this.lesson_name = lesson_name;
            }

            public String getLive_time() {
                return live_time;
            }

            public void setLive_time(String live_time) {
                this.live_time = live_time;
            }

            public String getModal_type() {
                return modal_type;
            }

            public void setModal_type(String modal_type) {
                this.modal_type = modal_type;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public ProductCourseBean getProduct_course() {
                return product_course;
            }

            public void setProduct_course(ProductCourseBean product_course) {
                this.product_course = product_course;
            }

            public int getProduct_id() {
                return product_id;
            }

            public void setProduct_id(int product_id) {
                this.product_id = product_id;
            }

            public String getPull_address() {
                return pull_address;
            }

            public void setPull_address(String pull_address) {
                this.pull_address = pull_address;
            }

            public boolean isReplayable() {
                return replayable;
            }

            public void setReplayable(boolean replayable) {
                this.replayable = replayable;
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

            public String getTeacher_name() {
                return teacher_name;
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }

            public ProductInteractiveCourseBean getProduct_interactive_course() {
                return product_interactive_course;
            }

            public void setProduct_interactive_course(ProductInteractiveCourseBean product_interactive_course) {
                this.product_interactive_course = product_interactive_course;
            }

            public static class ProductCourseBean {
                /**
                 * buy_tickets_count : 1
                 * chat_team_id : 27598161
                 * chat_team_owner : ec889fc585f60ba94e65e8c178df616c
                 * closed_lessons_count : 1
                 * completed_lesson_count : 1
                 * completed_lessons_count : 1
                 * current_price : 81
                 * grade : 六年级
                 * id : 103
                 * live_end_time : 2017-04-08 14:15
                 * live_start_time : 2017-03-29 10:31
                 * name : 测试问题
                 * objective : 提高写作能力
                 * preset_lesson_count : 10
                 * price : 90
                 * publicize : http://testing.qatime.cn/imgs/course_default.png
                 * status : teaching
                 * subject : 语文
                 * suit_crowd : 小学六年级
                 * taste_count : 9
                 * teacher_name : 肖晰月
                 */

                private int buy_tickets_count;
                private String chat_team_id;
                private String chat_team_owner;
                private int closed_lessons_count;
                private float current_price;
                private String grade;
                private int id;
                private String live_end_time;
                private String live_start_time;
                private String name;
                private String objective;
                private int preset_lesson_count;
                private float price;
                private String publicize;
                private String status;
                private String subject;
                private String suit_crowd;
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

                public float getCurrent_price() {
                    return current_price;
                }

                public void setCurrent_price(float current_price) {
                    this.current_price = current_price;
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

                public int getPreset_lesson_count() {
                    return preset_lesson_count;
                }

                public void setPreset_lesson_count(int preset_lesson_count) {
                    this.preset_lesson_count = preset_lesson_count;
                }

                public float getPrice() {
                    return price;
                }

                public void setPrice(float price) {
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

                public String getSuit_crowd() {
                    return suit_crowd;
                }

                public void setSuit_crowd(String suit_crowd) {
                    this.suit_crowd = suit_crowd;
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
        }
    }
}
