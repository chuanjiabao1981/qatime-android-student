package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/3/21 10:56
 * @Describe
 */

public class LiveTodayBean implements Serializable {

    /**
     * status : 1
     * data : [{"id":283,"name":"第一节课","status":"closed","class_date":"2017-03-21","live_time":"16:00-17:00","board_pull_stream":"rtmp://va0a19f55.live.126.net/live/2794c854398f4d05934157e05e2fe419","camera_pull_stream":"rtmp://va0a19f55.live.126.net/live/0ca7943afaa340c9a7c1a8baa5afac97","replayable":false,"left_replay_times":0,"course":{"id":100,"name":"测试一下价格","subject":"化学","grade":"高三","teacher_name":"王志成","price":100,"current_price":50,"chat_team_id":"","chat_team_owner":"07b7c43a854ed44d36c2941f1fc5ad00","buy_tickets_count":1,"status":"teaching","preset_lesson_count":2,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2017-03-20 19:11","live_end_time":"2017-03-23 21:00","publicize":"http://testing.qatime.cn/imgs/course_default.png"}},{"id":273,"name":"面积","status":"ready","class_date":"2017-03-21","live_time":"09:00-10:00","board_pull_stream":"rtmp://va0a19f55.live.126.net/live/2794c854398f4d05934157e05e2fe419","camera_pull_stream":"rtmp://va0a19f55.live.126.net/live/0ca7943afaa340c9a7c1a8baa5afac97","replayable":false,"left_replay_times":0,"course":{"id":95,"name":"2017奔跑吧成绩  三年级数学","subject":"数学","grade":"三年级","teacher_name":"张莉","price":100,"current_price":90,"chat_team_id":"25195008","chat_team_owner":"aa3ba21506abfcd2b2866f0b1d5f581a","buy_tickets_count":0,"status":"teaching","preset_lesson_count":10,"completed_lesson_count":1,"taste_count":9,"completed_lessons_count":1,"live_start_time":"2017-03-17 14:45","live_end_time":"2017-03-26 10:00","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_7a78ce6e4f97ed7f351ff7bcdeee0294.jpg"}},{"id":279,"name":"第一节课","status":"ready","class_date":"2017-03-21","live_time":"20:00-21:00","board_pull_stream":"rtmp://va0a19f55.live.126.net/live/2794c854398f4d05934157e05e2fe419","camera_pull_stream":"rtmp://va0a19f55.live.126.net/live/0ca7943afaa340c9a7c1a8baa5afac97","replayable":false,"left_replay_times":0,"course":{"id":96,"name":"测试默认图片","subject":"化学","grade":"二年级","teacher_name":"王志成","price":100,"current_price":100,"chat_team_id":"","chat_team_owner":"07b7c43a854ed44d36c2941f1fc5ad00","buy_tickets_count":0,"status":"teaching","preset_lesson_count":1,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2017-03-21 20:00","live_end_time":"2017-03-21 21:00","publicize":"http://testing.qatime.cn/imgs/course_default.png"}}]
     */

    private int status;
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 283
         * name : 第一节课
         * status : closed
         * class_date : 2017-03-21
         * live_time : 16:00-17:00
         * board_pull_stream : rtmp://va0a19f55.live.126.net/live/2794c854398f4d05934157e05e2fe419
         * camera_pull_stream : rtmp://va0a19f55.live.126.net/live/0ca7943afaa340c9a7c1a8baa5afac97
         * replayable : false
         * left_replay_times : 0
         * course : {"id":100,"name":"测试一下价格","subject":"化学","grade":"高三","teacher_name":"王志成","price":100,"current_price":50,"chat_team_id":"","chat_team_owner":"07b7c43a854ed44d36c2941f1fc5ad00","buy_tickets_count":1,"status":"teaching","preset_lesson_count":2,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2017-03-20 19:11","live_end_time":"2017-03-23 21:00","publicize":"http://testing.qatime.cn/imgs/course_default.png"}
         */

        private int id;
        private String name;
        private String status;
        private String class_date;
        private String live_time;
        private String board_pull_stream;
        private String camera_pull_stream;
        private boolean replayable;
        private int left_replay_times;
        private CourseBean course;

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

        public String getBoard_pull_stream() {
            return board_pull_stream;
        }

        public void setBoard_pull_stream(String board_pull_stream) {
            this.board_pull_stream = board_pull_stream;
        }

        public String getCamera_pull_stream() {
            return camera_pull_stream;
        }

        public void setCamera_pull_stream(String camera_pull_stream) {
            this.camera_pull_stream = camera_pull_stream;
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

        public CourseBean getCourse() {
            return course;
        }

        public void setCourse(CourseBean course) {
            this.course = course;
        }

        public static class CourseBean {
            /**
             * id : 100
             * name : 测试一下价格
             * subject : 化学
             * grade : 高三
             * teacher_name : 王志成
             * price : 100
             * current_price : 50
             * chat_team_id :
             * chat_team_owner : 07b7c43a854ed44d36c2941f1fc5ad00
             * buy_tickets_count : 1
             * status : teaching
             * preset_lesson_count : 2
             * completed_lesson_count : 0
             * taste_count : 0
             * completed_lessons_count : 0
             * live_start_time : 2017-03-20 19:11
             * live_end_time : 2017-03-23 21:00
             * publicize : http://testing.qatime.cn/imgs/course_default.png
             */

            private int id;
            private String name;
            private String subject;
            private String grade;
            private String teacher_name;
            private int price;
            private int current_price;
            private String chat_team_id;
            private String chat_team_owner;
            private int buy_tickets_count;
            private String status;
            private int preset_lesson_count;
            private int completed_lesson_count;
            private int taste_count;
            private int completed_lessons_count;
            private String live_start_time;
            private String live_end_time;
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

            public String getPublicize() {
                return publicize;
            }

            public void setPublicize(String publicize) {
                this.publicize = publicize;
            }
        }
    }
}
