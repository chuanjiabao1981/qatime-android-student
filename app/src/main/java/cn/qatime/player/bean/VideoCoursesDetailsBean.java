package cn.qatime.player.bean;


import java.io.Serializable;
import java.util.List;

import libraryextra.bean.IconsBean;
import libraryextra.bean.TeacherBean;
import libraryextra.bean.VideoLessonsBean;

/**
 * @author lungtify
 * @Time 2017/4/14 15:02
 * @Describe 视频课详情
 */

public class VideoCoursesDetailsBean implements Serializable {


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

        private VideoCourseBean video_course;
        private TicketBean ticket;

        public VideoCourseBean getVideo_course() {
            return video_course;
        }

        public void setVideo_course(VideoCourseBean video_course) {
            this.video_course = video_course;
        }

        public TicketBean getTicket() {
            return ticket;
        }

        public void setTicket(TicketBean ticket) {
            this.ticket = ticket;
        }

        public static class VideoCourseBean {

            private int id;
            private String name;
            private String subject;
            private String grade;
            private String teacher_name;
            private String publicize;
            private TeacherBean teacher;
            private String price;
            private int buy_tickets_count;
            private String status;
            private String description;
            private int video_lessons_count;
            private int taste_count;
            private String objective;
            private String suit_crowd;
            private String sell_type;
            private int total_duration;
            private IconsBean icons;
            private List<String> tag_list;
            private List<VideoLessonsBean> video_lessons;
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

            public String getPublicize() {
                return publicize;
            }

            public void setPublicize(String publicize) {
                this.publicize = publicize;
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

            public int getVideo_lessons_count() {
                return video_lessons_count;
            }

            public void setVideo_lessons_count(int video_lessons_count) {
                this.video_lessons_count = video_lessons_count;
            }

            public int getTaste_count() {
                return taste_count;
            }

            public void setTaste_count(int taste_count) {
                this.taste_count = taste_count;
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

        public static class TicketBean {
            /**
             * id : 667
             * used_count : 0
             * buy_count : 3
             * lesson_price : 0.0
             * progress : [{"video_lesson":{"id":43,"name":"体验终章","duration":null,"video":{"id":7817,"token":"1493284223769","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/393d4890f5c6c0b84ee1b2853361a6a1.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/acd6a82b7e533e7282cfc410c42403d0.mp4"}},"status":"unused"},{"video_lesson":{"id":42,"name":"体验第二周","duration":null,"video":{"id":7816,"token":"1493284212518","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b2fa3b8a4e5011fe4c07539a5580020d.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/46e2483d5621337ace0be3014ee1d24a.mp4"}},"status":"unused"},{"video_lesson":{"id":41,"name":"体验第一周","duration":null,"video":{"id":7815,"token":"1493284188624","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/c9f5e22bfd97e0330b1be40ea0cb8dfd.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/8fdd927ad57e5a39f567a377edd7b318.mp4"}},"status":"unused"}]
             * video_course : {"id":18,"name":"空手道免费体验课","subject":"数学","grade":"高一","teacher_name":"刘刚老师","publicize":"/assets/video_courses/app_info_default-061a95d589b702b146739a0de859454a.png"}
             * status : active
             */

            private int id;
            private int used_count;
            private int buy_count;
            private String lesson_price;
            private VideoCourseBean video_course;
            private String status;
            private List<ProgressBean> progress;

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

            public VideoCourseBean getVideo_course() {
                return video_course;
            }

            public void setVideo_course(VideoCourseBean video_course) {
                this.video_course = video_course;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public List<ProgressBean> getProgress() {
                return progress;
            }

            public void setProgress(List<ProgressBean> progress) {
                this.progress = progress;
            }


            public static class ProgressBean {
                /**
                 * video_lesson : {"id":43,"name":"体验终章","duration":null,"video":{"id":7817,"token":"1493284223769","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/393d4890f5c6c0b84ee1b2853361a6a1.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/acd6a82b7e533e7282cfc410c42403d0.mp4"}}
                 * status : unused
                 */

                private VideoLessonsBean video_lesson;
                private String status;

                public VideoLessonsBean getVideo_lesson() {
                    return video_lesson;
                }

                public void setVideo_lesson(VideoLessonsBean video_lesson) {
                    this.video_lesson = video_lesson;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }
            }
        }
    }
}
