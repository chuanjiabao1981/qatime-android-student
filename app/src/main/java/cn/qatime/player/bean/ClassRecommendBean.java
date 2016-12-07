package cn.qatime.player.bean;

import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2016/11/1 16:39
 * @Description:
 */
public class ClassRecommendBean {
    /**
     * status : 1
     * data : [{"title":"初三化学秋季精品高分班推荐","index":1,"type":"Recommend::LiveStudioCourseItem","live_studio_course":{"id":38,"name":"初三化学秋季精品高分班","subject":"化学","grade":"初三","teacher_name":"赵雪琴","price":760,"chat_team_id":"7965148","buy_tickets_count":0,"preset_lesson_count":4,"completed_lesson_count":0,"live_start_time":"2016-09-03 09:30","live_end_time":"2016-09-11 11:00","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_a03ad822a289a4aff651fd583c78b4d1.jpg"}},{"title":"七年级地理提升课推荐","index":2,"type":"Recommend::LiveStudioCourseItem","live_studio_course":{"id":39,"name":"七年级地理提升课","subject":"地理","grade":"初二","teacher_name":"潘馨","price":500,"chat_team_id":"7961745","buy_tickets_count":0,"preset_lesson_count":4,"completed_lesson_count":0,"live_start_time":"2016-09-02 18:00","live_end_time":"2016-09-02 17:30","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_761c7dea6fc919d4ba9f933fcdc3b6a2.jpg"}},{"title":"高二历史新人历史要这样学推荐","index":3,"type":"Recommend::LiveStudioCourseItem","live_studio_course":{"id":40,"name":"高二历史新人历史要这样学","subject":"历史","grade":"高二","teacher_name":"关老师","price":110,"chat_team_id":"7964479","buy_tickets_count":0,"preset_lesson_count":2,"completed_lesson_count":0,"live_start_time":"2016-09-07 18:30","live_end_time":"2016-09-08 19:30","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_1a47b0f400b808b46b668e3fa4d96a61.jpg"}},{"title":"教育经验分享推荐","index":4,"type":"Recommend::LiveStudioCourseItem","live_studio_course":{"id":41,"name":"教育经验分享","subject":"语文","grade":"高二","teacher_name":"韩志慧","price":160,"chat_team_id":"7964480","buy_tickets_count":0,"preset_lesson_count":1,"completed_lesson_count":0,"live_start_time":"2016-09-12 15:03","live_end_time":"2016-09-12 19:00","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_d522762fcc1cc083a1c38b7e838dd6ed.jpg"}},{"title":"测试辅导班1推荐","index":5,"type":"Recommend::LiveStudioCourseItem","live_studio_course":{"id":42,"name":"测试辅导班1","subject":"数学","grade":"高一","teacher_name":"王志成","price":1000,"chat_team_id":"8185574","buy_tickets_count":0,"preset_lesson_count":10,"completed_lesson_count":3,"live_start_time":"2016-10-11 14:13","live_end_time":"2016-10-31 16:23","publicize":"http://testing.qatime.cn/imgs/no_img.png"}},{"title":"又一个测试辅导班推荐","index":6,"type":"Recommend::LiveStudioCourseItem","live_studio_course":{"id":43,"name":"又一个测试辅导班","subject":"物理","grade":"高三","teacher_name":"贾晓利","price":1000,"chat_team_id":"8189541","buy_tickets_count":0,"preset_lesson_count":10,"completed_lesson_count":2,"live_start_time":"2016-10-11 15:46","live_end_time":"2016-10-31 20:00","publicize":"http://testing.qatime.cn/imgs/no_img.png"}}]
     */

    private int status;
    /**
     * title : 初三化学秋季精品高分班推荐
     * index : 1
     * type : Recommend::LiveStudioCourseItem
     * live_studio_course : {"id":38,"name":"初三化学秋季精品高分班","subject":"化学","grade":"初三","teacher_name":"赵雪琴","price":760,"chat_team_id":"7965148","buy_tickets_count":0,"preset_lesson_count":4,"completed_lesson_count":0,"live_start_time":"2016-09-03 09:30","live_end_time":"2016-09-11 11:00","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_a03ad822a289a4aff651fd583c78b4d1.jpg"}
     */

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
        private String title;
        private int index;
        private String type;

        private String reason;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        /**
         * id : 38
         * name : 初三化学秋季精品高分班
         * subject : 化学
         * grade : 初三
         * teacher_name : 赵雪琴
         * price : 760
         * chat_team_id : 7965148
         * buy_tickets_count : 0
         * preset_lesson_count : 4
         * completed_lesson_count : 0
         * live_start_time : 2016-09-03 09:30
         * live_end_time : 2016-09-11 11:00
         * publicize : http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_a03ad822a289a4aff651fd583c78b4d1.jpg
         */

        private LiveStudioCourseBean live_studio_course;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public LiveStudioCourseBean getLive_studio_course() {
            return live_studio_course;
        }

        public void setLive_studio_course(LiveStudioCourseBean live_studio_course) {
            this.live_studio_course = live_studio_course;
        }

        public static class LiveStudioCourseBean {
            private int id;
            private String name;
            private String subject;
            private String grade;
            private String teacher_name;
            public float price;
            public float current_price;
            private String chat_team_id;
            private int buy_tickets_count;
            private int preset_lesson_count;
            private int completed_lesson_count;
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

            public int getBuy_tickets_count() {
                return buy_tickets_count;
            }

            public void setBuy_tickets_count(int buy_tickets_count) {
                this.buy_tickets_count = buy_tickets_count;
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
