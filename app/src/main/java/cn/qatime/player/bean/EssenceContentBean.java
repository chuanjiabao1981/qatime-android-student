package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/3/21 14:20
 * @Describe 精选内容
 */

public class EssenceContentBean implements Serializable {

    /**
     * status : 1
     * data : [{"title":"精选可伶","index":2,"type":"Recommend::ChoicenessItem","live_studio_course":{"id":40,"name":"高二历史新人历史要这样学","subject":"历史","grade":"高二","teacher_name":"关老师","price":110,"current_price":110,"chat_team_id":"25192346","chat_team_owner":"a751b56731e2d09ee6fc12fce1d2e12c","buy_tickets_count":1,"status":"teaching","preset_lesson_count":2,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2016-09-07 18:30","live_end_time":"2016-09-08 19:30","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_fa104ad170800014568e47681710fb6d.jpg"},"reason":null,"logo_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/info_fa104ad170800014568e47681710fb6d.jpg","tag_one":"star_teacher","tag_two":"join_cheap"},{"title":"干的漂亮哈","index":1,"type":"Recommend::ChoicenessItem","live_studio_course":{"id":36,"name":"短文改错必考点","subject":"英语","grade":"高一","teacher_name":"关倩倩","price":200,"current_price":200,"chat_team_id":"25184263","chat_team_owner":"4ab9c55ffcc2412de0d0b5f8e5afd09f","buy_tickets_count":1,"status":"teaching","preset_lesson_count":2,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2016-09-02 15:00","live_end_time":"2016-09-03 11:00","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_9eebb5640a17812fd06896bc711a0ec3.jpg"},"reason":null,"logo_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/info_9eebb5640a17812fd06896bc711a0ec3.jpg","tag_one":"star_teacher","tag_two":"join_cheap"},{"title":"刘刚做的好","index":2,"type":"Recommend::ChoicenessItem","live_studio_course":{"id":71,"name":"测试辅导班1","subject":"化学","grade":"高三","teacher_name":"王志成","price":100,"current_price":0,"chat_team_id":"25184264","chat_team_owner":"07b7c43a854ed44d36c2941f1fc5ad00","buy_tickets_count":0,"status":"completed","preset_lesson_count":2,"completed_lesson_count":4,"taste_count":1,"completed_lessons_count":4,"live_start_time":"2016-11-29 11:59","live_end_time":"2016-12-01 21:30","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_224c587df0ff7caa71fccf2327a66e91.png"},"reason":null,"logo_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/info_224c587df0ff7caa71fccf2327a66e91.png","tag_one":"best_seller","tag_two":"free_tastes"},{"title":"这是必须的哈哈哈哈哈哈哈","index":3,"type":"Recommend::ChoicenessItem","live_studio_course":{"id":71,"name":"测试辅导班1","subject":"化学","grade":"高三","teacher_name":"王志成","price":100,"current_price":0,"chat_team_id":"25184264","chat_team_owner":"07b7c43a854ed44d36c2941f1fc5ad00","buy_tickets_count":0,"status":"completed","preset_lesson_count":2,"completed_lesson_count":4,"taste_count":1,"completed_lessons_count":4,"live_start_time":"2016-11-29 11:59","live_end_time":"2016-12-01 21:30","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_224c587df0ff7caa71fccf2327a66e91.png"},"reason":null,"logo_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/info_224c587df0ff7caa71fccf2327a66e91.png","tag_one":null,"tag_two":"join_cheap"}]
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
         * title : 精选可伶
         * index : 2
         * type : Recommend::ChoicenessItem
         * live_studio_course : {"id":40,"name":"高二历史新人历史要这样学","subject":"历史","grade":"高二","teacher_name":"关老师","price":110,"current_price":110,"chat_team_id":"25192346","chat_team_owner":"a751b56731e2d09ee6fc12fce1d2e12c","buy_tickets_count":1,"status":"teaching","preset_lesson_count":2,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"live_start_time":"2016-09-07 18:30","live_end_time":"2016-09-08 19:30","publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_fa104ad170800014568e47681710fb6d.jpg"}
         * reason : null
         * logo_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/info_fa104ad170800014568e47681710fb6d.jpg
         * tag_one : star_teacher
         * tag_two : join_cheap
         */

        private String title;
        private int index;
        private String type;
        private LiveStudioCourseBean live_studio_course;
        private String reason;
        private String logo_url;
        private String tag_one;
        private String tag_two;

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

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
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

        public String getTag_two() {
            return tag_two;
        }

        public void setTag_two(String tag_two) {
            this.tag_two = tag_two;
        }

        public static class LiveStudioCourseBean {
            /**
             * id : 40
             * name : 高二历史新人历史要这样学
             * subject : 历史
             * grade : 高二
             * teacher_name : 关老师
             * price : 110
             * current_price : 110
             * chat_team_id : 25192346
             * chat_team_owner : a751b56731e2d09ee6fc12fce1d2e12c
             * buy_tickets_count : 1
             * status : teaching
             * preset_lesson_count : 2
             * completed_lesson_count : 0
             * taste_count : 0
             * completed_lessons_count : 0
             * live_start_time : 2016-09-07 18:30
             * live_end_time : 2016-09-08 19:30
             * publicize : http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_fa104ad170800014568e47681710fb6d.jpg
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
