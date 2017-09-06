package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * @author lungtify
 * @Time 2017/6/20 10:27
 * @Describe used to PlayBackActivity
 */

public class PlayBackInfoBean implements Serializable {

    /**
     * status : 1
     * data : {"replay_times":3,"live_studio_lesson":{"name":" test 04辛老师回放功能测试 BB"},"teacher":{"id":2489,"name":"王志成","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_2e519536bc228c4e4f54973ac3ef37d0.jpg","category":"初中","subject":"数学","gender":"male"},"video_duration":888,"video_url":"http://vodb98fi13b.vod.126.net/vodb98fi13b/19184ef1-9ad1-430d-95d3-5d992e064d8c.mp4"}
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
         * replay_times : 3
         * live_studio_lesson : {"name":" test 04辛老师回放功能测试 BB"}
         * teacher : {"id":2489,"name":"王志成","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_2e519536bc228c4e4f54973ac3ef37d0.jpg","category":"初中","subject":"数学","gender":"male"}
         * video_duration : 888
         * video_url : http://vodb98fi13b.vod.126.net/vodb98fi13b/19184ef1-9ad1-430d-95d3-5d992e064d8c.mp4
         */

        private int replay_times;
        private LessonBean live_studio_lesson;
        private LessonBean live_studio_interactive_lesson;
        private LessonBean live_studio_scheduled_lesson;
        private TeacherBean teacher;
        private int video_duration;
        private String video_url;
        private String target_type;

        public int getReplay_times() {
            return replay_times;
        }

        public void setReplay_times(int replay_times) {
            this.replay_times = replay_times;
        }

        public LessonBean getLive_studio_lesson() {
            return live_studio_lesson;
        }

        public void setLive_studio_lesson(LessonBean live_studio_lesson) {
            this.live_studio_lesson = live_studio_lesson;
        }

        public LessonBean getLive_studio_interactive_lesson() {
            return live_studio_interactive_lesson;
        }

        public void setLive_studio_interactive_lesson(LessonBean live_studio_interactive_lesson) {
            this.live_studio_interactive_lesson = live_studio_interactive_lesson;
        }

        public LessonBean getLive_studio_scheduled_lesson() {
            return live_studio_scheduled_lesson;
        }

        public void setLive_studio_scheduled_lesson(LessonBean live_studio_scheduled_lesson) {
            this.live_studio_scheduled_lesson = live_studio_scheduled_lesson;
        }

        public String getTarget_type() {
            return target_type;
        }

        public void setTarget_type(String target_type) {
            this.target_type = target_type;
        }

        public TeacherBean getTeacher() {
            return teacher;
        }

        public void setTeacher(TeacherBean teacher) {
            this.teacher = teacher;
        }

        public int getVideo_duration() {
            return video_duration;
        }

        public void setVideo_duration(int video_duration) {
            this.video_duration = video_duration;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public static class LessonBean {
            /**
             * name :  test 04辛老师回放功能测试 BB
             */

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class TeacherBean {
            /**
             * id : 2489
             * name : 王志成
             * ex_big_avatar_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_2e519536bc228c4e4f54973ac3ef37d0.jpg
             * category : 初中
             * subject : 数学
             * gender : male
             */

            private int id;
            private String name;
            private String ex_big_avatar_url;
            private String category;
            private String subject;
            private String gender;

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

            public String getEx_big_avatar_url() {
                return ex_big_avatar_url;
            }

            public void setEx_big_avatar_url(String ex_big_avatar_url) {
                this.ex_big_avatar_url = ex_big_avatar_url;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }
        }
    }
}
