package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/6/19 19:52
 * @Describe
 */

public class PlayBackListBean implements Serializable {

    /**
     * status : 1
     * data : [{"title":null,"index":null,"type":"Recommend::ReplayItem","logo_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_b94be0f3b4cca9b2363024cf5a40fe92.jpg","top":true,"replay_times":1,"updated_at":1497853996,"target_type":"LiveStudio::Lesson","target_id":247,"live_studio_lesson":{"id":247,"name":" test 04辛老师回放功能测试 BB","status":"completed","class_date":"2017-01-18","live_time":"18:00-18:45","board_pull_stream":"http://va0a19f55.live.126.net/live/2794c854398f4d05934157e05e2fe419.flv?netease=va0a19f55.live.126.net","camera_pull_stream":"http://va0a19f55.live.126.net/live/0ca7943afaa340c9a7c1a8baa5afac97.flv?netease=va0a19f55.live.126.net","replayable":true,"left_replay_times":0,"modal_type":"LiveStudio::Lesson"},"grade":"高二","subject":"化学","teacher_name":"王志成","video_duration":888,"video_url":"http://vodb98fi13b.vod.126.net/vodb98fi13b/19184ef1-9ad1-430d-95d3-5d992e064d8c.mp4"}]
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
         * title : null
         * index : null
         * type : Recommend::ReplayItem
         * logo_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_b94be0f3b4cca9b2363024cf5a40fe92.jpg
         * top : true
         * replay_times : 1
         * updated_at : 1497853996
         * target_type : LiveStudio::Lesson
         * target_id : 247
         * live_studio_lesson : {"id":247,"name":" test 04辛老师回放功能测试 BB","status":"completed","class_date":"2017-01-18","live_time":"18:00-18:45","board_pull_stream":"http://va0a19f55.live.126.net/live/2794c854398f4d05934157e05e2fe419.flv?netease=va0a19f55.live.126.net","camera_pull_stream":"http://va0a19f55.live.126.net/live/0ca7943afaa340c9a7c1a8baa5afac97.flv?netease=va0a19f55.live.126.net","replayable":true,"left_replay_times":0,"modal_type":"LiveStudio::Lesson"}
         * grade : 高二
         * subject : 化学
         * teacher_name : 王志成
         * video_duration : 888
         * video_url : http://vodb98fi13b.vod.126.net/vodb98fi13b/19184ef1-9ad1-430d-95d3-5d992e064d8c.mp4
         */

        private int id;
        private String logo_url;
        private int replay_times;
        private LessonBean live_studio_lesson;
        private String target_type;
        private LessonBean live_studio_interactive_lesson;
        private LessonBean live_studio_scheduled_lesson;
        private String grade;
        private String subject;
        private String teacher_name;

        public String getTarget_type() {
            return target_type;
        }

        public void setTarget_type(String target_type) {
            this.target_type = target_type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLogo_url() {
            return logo_url;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }


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

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
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

        public static class LessonBean {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
