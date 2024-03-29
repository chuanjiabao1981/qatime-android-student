package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2017/5/12 17:56
 * @Description:
 */
public class VideoPlayBean implements Serializable {

    /**
     * status : 1
     * data : {"video_lesson":{"id":5,"name":"第二学时","duration":null,"video":{"id":7768,"token":"1492157167944","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b574edcbab5c0c9ce60035847b1d11f7.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/24e4490546fc753cd51eb9da30d2bb92.mp4"}},"ticket":{"id":662,"used_count":1,"buy_count":2,"lesson_price":"300.0","progress":[{"video_lesson":{"id":5,"name":"第二学时","duration":null,"video":{"id":7768,"token":"1492157167944","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b574edcbab5c0c9ce60035847b1d11f7.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/24e4490546fc753cd51eb9da30d2bb92.mp4"}},"status":"used"},{"video_lesson":{"id":4,"name":"第一学时","duration":null,"video":{"id":7767,"token":"1492157139451","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/96fcf0349390a9ddccd8871c868d070a.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/5f89cfbf304841a1ea8c15b2451e306a.mp4"}},"status":"unused"}],"video_course":{"id":3,"name":"初中考试1","subject":"化学","grade":"初一","teacher_name":"王志成","publicize":"/assets/video_courses/app_info_default-061a95d589b702b146739a0de859454a.png"},"status":"active"}}
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
         * video_lesson : {"id":5,"name":"第二学时","duration":null,"video":{"id":7768,"token":"1492157167944","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b574edcbab5c0c9ce60035847b1d11f7.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/24e4490546fc753cd51eb9da30d2bb92.mp4"}}
         * ticket : {"id":662,"used_count":1,"buy_count":2,"lesson_price":"300.0","progress":[{"video_lesson":{"id":5,"name":"第二学时","duration":null,"video":{"id":7768,"token":"1492157167944","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b574edcbab5c0c9ce60035847b1d11f7.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/24e4490546fc753cd51eb9da30d2bb92.mp4"}},"status":"used"},{"video_lesson":{"id":4,"name":"第一学时","duration":null,"video":{"id":7767,"token":"1492157139451","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/96fcf0349390a9ddccd8871c868d070a.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/5f89cfbf304841a1ea8c15b2451e306a.mp4"}},"status":"unused"}],"video_course":{"id":3,"name":"初中考试1","subject":"化学","grade":"初一","teacher_name":"王志成","publicize":"/assets/video_courses/app_info_default-061a95d589b702b146739a0de859454a.png"},"status":"active"}
         */

        private VideoLessonBean video_lesson;
        private TicketBean ticket;

        public VideoLessonBean getVideo_lesson() {
            return video_lesson;
        }

        public void setVideo_lesson(VideoLessonBean video_lesson) {
            this.video_lesson = video_lesson;
        }

        public TicketBean getTicket() {
            return ticket;
        }

        public void setTicket(TicketBean ticket) {
            this.ticket = ticket;
        }

        public static class VideoLessonBean {
            /**
             * id : 5
             * name : 第二学时
             * duration : null
             * video : {"id":7768,"token":"1492157167944","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b574edcbab5c0c9ce60035847b1d11f7.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/24e4490546fc753cd51eb9da30d2bb92.mp4"}
             */

            private int id;
            private String name;
            private Object duration;
            private VideoBean video;

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

            public Object getDuration() {
                return duration;
            }

            public void setDuration(Object duration) {
                this.duration = duration;
            }

            public VideoBean getVideo() {
                return video;
            }

            public void setVideo(VideoBean video) {
                this.video = video;
            }

            public static class VideoBean {
                /**
                 * id : 7768
                 * token : 1492157167944
                 * video_type : mp4
                 * duration : 2
                 * tmp_duration : 2
                 * format_tmp_duration : 00:00:02
                 * capture : http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b574edcbab5c0c9ce60035847b1d11f7.jpg
                 * name_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/24e4490546fc753cd51eb9da30d2bb92.mp4
                 */

                private int id;
                private String token;
                private String video_type;
                private int duration;
                private int tmp_duration;
                private String format_tmp_duration;
                private String capture;
                private String name_url;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getToken() {
                    return token;
                }

                public void setToken(String token) {
                    this.token = token;
                }

                public String getVideo_type() {
                    return video_type;
                }

                public void setVideo_type(String video_type) {
                    this.video_type = video_type;
                }

                public int getDuration() {
                    return duration;
                }

                public void setDuration(int duration) {
                    this.duration = duration;
                }

                public int getTmp_duration() {
                    return tmp_duration;
                }

                public void setTmp_duration(int tmp_duration) {
                    this.tmp_duration = tmp_duration;
                }

                public String getFormat_tmp_duration() {
                    return format_tmp_duration;
                }

                public void setFormat_tmp_duration(String format_tmp_duration) {
                    this.format_tmp_duration = format_tmp_duration;
                }

                public String getCapture() {
                    return capture;
                }

                public void setCapture(String capture) {
                    this.capture = capture;
                }

                public String getName_url() {
                    return name_url;
                }

                public void setName_url(String name_url) {
                    this.name_url = name_url;
                }
            }
        }

        public static class TicketBean {
            /**
             * id : 662
             * used_count : 1
             * buy_count : 2
             * lesson_price : 300.0
             * progress : [{"video_lesson":{"id":5,"name":"第二学时","duration":null,"video":{"id":7768,"token":"1492157167944","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b574edcbab5c0c9ce60035847b1d11f7.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/24e4490546fc753cd51eb9da30d2bb92.mp4"}},"status":"used"},{"video_lesson":{"id":4,"name":"第一学时","duration":null,"video":{"id":7767,"token":"1492157139451","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/96fcf0349390a9ddccd8871c868d070a.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/5f89cfbf304841a1ea8c15b2451e306a.mp4"}},"status":"unused"}]
             * video_course : {"id":3,"name":"初中考试1","subject":"化学","grade":"初一","teacher_name":"王志成","publicize":"/assets/video_courses/app_info_default-061a95d589b702b146739a0de859454a.png"}
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

            public static class VideoCourseBean {
                /**
                 * id : 3
                 * name : 初中考试1
                 * subject : 化学
                 * grade : 初一
                 * teacher_name : 王志成
                 * publicize : /assets/video_courses/app_info_default-061a95d589b702b146739a0de859454a.png
                 */

                private int id;
                private String name;
                private String subject;
                private String grade;
                private String teacher_name;
                private String publicize;

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
            }

            public static class ProgressBean {
                /**
                 * video_lesson : {"id":5,"name":"第二学时","duration":null,"video":{"id":7768,"token":"1492157167944","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b574edcbab5c0c9ce60035847b1d11f7.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/24e4490546fc753cd51eb9da30d2bb92.mp4"}}
                 * status : used
                 */

                private VideoLessonBeanX video_lesson;
                private String status;

                public VideoLessonBeanX getVideo_lesson() {
                    return video_lesson;
                }

                public void setVideo_lesson(VideoLessonBeanX video_lesson) {
                    this.video_lesson = video_lesson;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public static class VideoLessonBeanX {
                    /**
                     * id : 5
                     * name : 第二学时
                     * duration : null
                     * video : {"id":7768,"token":"1492157167944","video_type":"mp4","duration":2,"tmp_duration":2,"format_tmp_duration":"00:00:02","capture":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b574edcbab5c0c9ce60035847b1d11f7.jpg","name_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/24e4490546fc753cd51eb9da30d2bb92.mp4"}
                     */

                    private int id;
                    private String name;
                    private Object duration;
                    private VideoBeanX video;

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

                    public Object getDuration() {
                        return duration;
                    }

                    public void setDuration(Object duration) {
                        this.duration = duration;
                    }

                    public VideoBeanX getVideo() {
                        return video;
                    }

                    public void setVideo(VideoBeanX video) {
                        this.video = video;
                    }

                    public static class VideoBeanX {
                        /**
                         * id : 7768
                         * token : 1492157167944
                         * video_type : mp4
                         * duration : 2
                         * tmp_duration : 2
                         * format_tmp_duration : 00:00:02
                         * capture : http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/capture/b574edcbab5c0c9ce60035847b1d11f7.jpg
                         * name_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/videos/24e4490546fc753cd51eb9da30d2bb92.mp4
                         */

                        private int id;
                        private String token;
                        private String video_type;
                        private int duration;
                        private int tmp_duration;
                        private String format_tmp_duration;
                        private String capture;
                        private String name_url;

                        public int getId() {
                            return id;
                        }

                        public void setId(int id) {
                            this.id = id;
                        }

                        public String getToken() {
                            return token;
                        }

                        public void setToken(String token) {
                            this.token = token;
                        }

                        public String getVideo_type() {
                            return video_type;
                        }

                        public void setVideo_type(String video_type) {
                            this.video_type = video_type;
                        }

                        public int getDuration() {
                            return duration;
                        }

                        public void setDuration(int duration) {
                            this.duration = duration;
                        }

                        public int getTmp_duration() {
                            return tmp_duration;
                        }

                        public void setTmp_duration(int tmp_duration) {
                            this.tmp_duration = tmp_duration;
                        }

                        public String getFormat_tmp_duration() {
                            return format_tmp_duration;
                        }

                        public void setFormat_tmp_duration(String format_tmp_duration) {
                            this.format_tmp_duration = format_tmp_duration;
                        }

                        public String getCapture() {
                            return capture;
                        }

                        public void setCapture(String capture) {
                            this.capture = capture;
                        }

                        public String getName_url() {
                            return name_url;
                        }

                        public void setName_url(String name_url) {
                            this.name_url = name_url;
                        }
                    }
                }
            }
        }
    }
}
