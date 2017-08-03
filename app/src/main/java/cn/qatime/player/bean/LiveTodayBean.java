package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

import libraryextra.utils.StringUtils;

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
        private String live_time;
        private String start_time;
        private String end_time;
        private String lesson_type;
        private String modal_type;
        private CourseBean course;
        private CourseBean customized_group;

        public String getModal_type() {
            return modal_type;
        }

        public void setModal_type(String modal_type) {
            this.modal_type = modal_type;
        }

        public String getLesson_type() {
            return lesson_type;
        }

        public void setLesson_type(String lesson_type) {
            this.lesson_type = lesson_type;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


        public String getLive_time() {
            if (!StringUtils.isNullOrBlanK(live_time)) {
                return live_time;
            } else {
                return start_time + "-" + end_time;
            }
        }

        public void setLive_time(String live_time) {
            this.live_time = live_time;
        }


        public CourseBean getCourse() {
            if (course != null) {
                return course;
            } else {
                return customized_group;
            }
        }

        public void setCourse(CourseBean course) {
            this.course = course;
        }

        public static class CourseBean {
            private int id;
            private String publicize;
            private PublicizesUrlBean publicizes_url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPublicize() {
                if (!StringUtils.isNullOrBlanK(publicize)) {
                    return publicize;
                } else {
                    return publicizes_url.getList();
                }
            }

            public void setPublicize(String publicize) {
                this.publicize = publicize;
            }

            private class PublicizesUrlBean {
                private String app_info;
                private String list;
                private String info;

                public String getApp_info() {
                    return app_info;
                }

                public void setApp_info(String app_info) {
                    this.app_info = app_info;
                }

                public String getList() {
                    return list;
                }

                public void setList(String list) {
                    this.list = list;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }
            }
        }
    }
}
