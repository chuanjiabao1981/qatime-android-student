package cn.qatime.player.bean;


import java.io.Serializable;
import java.util.List;

import libraryextra.bean.AppPayParamsBean;
import libraryextra.bean.TeacherBean;


public class MyOrderBean implements Serializable {


    /**
     * status : 1
     * data : [{"id":"201704011845230299","amount":"500.0","pay_type":"weixin","status":"unpaid","source":"app","created_at":"2017-04-01T18:45:23.194+08:00","updated_at":"2017-04-01T18:45:23.194+08:00","pay_at":null,"prepay_id":"wx2017040118452333022a336b0617284207","nonce_str":"MAA0eFMSyt09NDJb","app_pay_params":{"appid":"wxf2dfbeb5f641ce40","partnerid":"1379576802","package":"Sign=WXPay","timestamp":"1491043550","prepayid":"wx2017040118452333022a336b0617284207","noncestr":"MAA0eFMSyt09NDJb","sign":"A0E0625ED0BF6C8940144A35E026CA5A"},"app_pay_str":null,"product_type":"LiveStudio::InteractiveCourse","product":null,"product_interactive_course":{"id":1,"name":"来个一对一","subject":"生物","grade":"初一","price":"500.0","status":"published","description":"<p>大家好呀<\/p>","lessons_count":10,"completed_lessons_count":0,"closed_lessons_count":0,"live_start_time":"2017-03-06 18:00","live_end_time":"2017-04-10 18:45","objective":"漫无目的的走在大街上，哪里会有目标","suit_crowd":"活到老，学到老，学习不分年龄","publicize_url":"http://testing.qatime.cn/imgs/course_default.png","publicize_info_url":"http://testing.qatime.cn/imgs/course_default.png","publicize_list_url":"http://testing.qatime.cn/imgs/course_default.png","publicize_app_url":"http://testing.qatime.cn/imgs/course_default.png","chat_team_id":"28024539","teachers":[{"id":541,"name":"教师","nick_name":"春意盎然","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ed8858f5ed860b8e94226f37446b89c1.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_ed8858f5ed860b8e94226f37446b89c1.jpg","login_mobile":null,"email":"qatime@8.cn","teaching_years":"within_three_years","category":"高中","subject":"数学","grade_range":[],"gender":null,"birthday":null,"province":1,"city":1,"school":1,"desc":""}],"created_at":1490927644},"coupon_code":null},{"id":"201704011845040005","amount":"50.0","pay_type":"weixin","status":"unpaid","source":"app","created_at":"2017-04-01T18:45:04.489+08:00","updated_at":"2017-04-01T18:45:04.489+08:00","pay_at":null,"prepay_id":"wx20170401184505d369a6216c0489525354","nonce_str":"4qULcJsFeEULBuup","app_pay_params":{"appid":"wxf2dfbeb5f641ce40","partnerid":"1379576802","package":"Sign=WXPay","timestamp":"1491043550","prepayid":"wx20170401184505d369a6216c0489525354","noncestr":"4qULcJsFeEULBuup","sign":"B20C76208181E3388092A68D5C96BB15"},"app_pay_str":null,"product_type":"LiveStudio::Course","product":{"id":25,"name":"初中数学","subject":"数学","grade":"初一","teacher_name":"马燕兆","price":50,"current_price":50,"chat_team_id":"25184275","chat_team_owner":"8b8dac47fc743ebc7d163bd360caaafb","buy_tickets_count":4,"status":"teaching","preset_lesson_count":2,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"closed_lessons_count":0,"live_start_time":"2016-09-25 10:00","live_end_time":"2016-09-25 22:00","objective":null,"suit_crowd":null,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_2d71a0cb8c07f529009ce51bb8cd3dbf.jpg"},"product_interactive_course":null,"coupon_code":null}]
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

    public static class DataBean implements Serializable {
        /**
         * id : 201704011845230299
         * amount : 500.0
         * pay_type : weixin
         * status : unpaid
         * source : app
         * created_at : 2017-04-01T18:45:23.194+08:00
         * updated_at : 2017-04-01T18:45:23.194+08:00
         * pay_at : null
         * prepay_id : wx2017040118452333022a336b0617284207
         * nonce_str : MAA0eFMSyt09NDJb
         * app_pay_params : {"appid":"wxf2dfbeb5f641ce40","partnerid":"1379576802","package":"Sign=WXPay","timestamp":"1491043550","prepayid":"wx2017040118452333022a336b0617284207","noncestr":"MAA0eFMSyt09NDJb","sign":"A0E0625ED0BF6C8940144A35E026CA5A"}
         * app_pay_str : null
         * product_type : LiveStudio::InteractiveCourse
         * product : null
         * product_interactive_course : {"id":1,"name":"来个一对一","subject":"生物","grade":"初一","price":"500.0","status":"published","description":"<p>大家好呀<\/p>","lessons_count":10,"completed_lessons_count":0,"closed_lessons_count":0,"live_start_time":"2017-03-06 18:00","live_end_time":"2017-04-10 18:45","objective":"漫无目的的走在大街上，哪里会有目标","suit_crowd":"活到老，学到老，学习不分年龄","publicize_url":"http://testing.qatime.cn/imgs/course_default.png","publicize_info_url":"http://testing.qatime.cn/imgs/course_default.png","publicize_list_url":"http://testing.qatime.cn/imgs/course_default.png","publicize_app_url":"http://testing.qatime.cn/imgs/course_default.png","chat_team_id":"28024539","teachers":[{"id":541,"name":"教师","nick_name":"春意盎然","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ed8858f5ed860b8e94226f37446b89c1.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_ed8858f5ed860b8e94226f37446b89c1.jpg","login_mobile":null,"email":"qatime@8.cn","teaching_years":"within_three_years","category":"高中","subject":"数学","grade_range":[],"gender":null,"birthday":null,"province":1,"city":1,"school":1,"desc":""}],"created_at":1490927644}
         * coupon_code : null
         */

        private String id;
        private String amount;
        private String pay_type;
        private String status;
        private String source;
        private String created_at;
        private String updated_at;
        private String pay_at;
        private String prepay_id;
        private String nonce_str;
        private AppPayParamsBean app_pay_params;
        private String app_pay_str;
        private String product_type;
        private Product product;
        private ProductInteractiveCourseBean product_interactive_course;
        private ProductVideoCourseBean product_video_course;
        private ProductCustomizedGroupBean product_customized_group;
        private String coupon_code;

        public ProductCustomizedGroupBean getProduct_customized_group() {
            return product_customized_group;
        }

        public void setProduct_customized_group(ProductCustomizedGroupBean product_customized_group) {
            this.product_customized_group = product_customized_group;
        }

        public ProductVideoCourseBean getProduct_video_course() {
            return product_video_course;
        }

        public void setProduct_video_course(ProductVideoCourseBean product_video_course) {
            this.product_video_course = product_video_course;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getPay_at() {
            return pay_at;
        }

        public void setPay_at(String pay_at) {
            this.pay_at = pay_at;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public AppPayParamsBean getApp_pay_params() {
            return app_pay_params;
        }

        public void setApp_pay_params(AppPayParamsBean app_pay_params) {
            this.app_pay_params = app_pay_params;
        }

        public String getApp_pay_str() {
            return app_pay_str;
        }

        public void setApp_pay_str(String app_pay_str) {
            this.app_pay_str = app_pay_str;
        }

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public ProductInteractiveCourseBean getProduct_interactive_course() {
            return product_interactive_course;
        }

        public void setProduct_interactive_course(ProductInteractiveCourseBean product_interactive_course) {
            this.product_interactive_course = product_interactive_course;
        }

        public String getCoupon_code() {
            return coupon_code;
        }

        public void setCoupon_code(String coupon_code) {
            this.coupon_code = coupon_code;
        }


        public static class Product implements Serializable {

            private int id;
            private String name;
            private String subject;
            private String grade;
            private String teacher_name;
            private String status;
            private int preset_lesson_count;
            private int started_lessons_count;
            private boolean off_shelve;

            public boolean isOff_shelve() {
                return off_shelve;
            }

            public void setOff_shelve(boolean off_shelve) {
                this.off_shelve = off_shelve;
            }

            public int getStarted_lessons_count() {
                return started_lessons_count;
            }

            public void setStarted_lessons_count(int started_lessons_count) {
                this.started_lessons_count = started_lessons_count;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getId() {
                return this.id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return this.name;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getSubject() {
                return this.subject;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public String getGrade() {
                return this.grade;
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }

            public String getTeacher_name() {
                return this.teacher_name;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStatus() {
                return this.status;
            }

            public void setPreset_lesson_count(int preset_lesson_count) {
                this.preset_lesson_count = preset_lesson_count;
            }

            public int getPreset_lesson_count() {
                return this.preset_lesson_count;
            }

        }

        public static class ProductInteractiveCourseBean implements Serializable {
            private int id;
            private String name;
            private String subject;
            private String grade;
            private String status;
            private List<TeacherBean> teachers;
            private int lessons_count;
            private int started_lessons_count;
            private boolean off_shelve;

            public boolean isOff_shelve() {
                return off_shelve;
            }

            public void setOff_shelve(boolean off_shelve) {
                this.off_shelve = off_shelve;
            }

            public int getStarted_lessons_count() {
                return started_lessons_count;
            }

            public void setStarted_lessons_count(int started_lessons_count) {
                this.started_lessons_count = started_lessons_count;
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

            public int getLessons_count() {
                return lessons_count;
            }

            public void setLessons_count(int lessons_count) {
                this.lessons_count = lessons_count;
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

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }


            public List<TeacherBean> getTeachers() {
                return teachers;
            }

            public void setTeachers(List<TeacherBean> teachers) {
                this.teachers = teachers;
            }

        }

        public static class ProductVideoCourseBean implements Serializable {
            private int id;
            private String name;
            private String subject;
            private String grade;
            private String teacher_name;
            private TeacherBean teacher;
            private String status;
            private int preset_lesson_count;
            private boolean off_shelve;

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

            public boolean isOff_shelve() {
                return off_shelve;
            }

            public void setOff_shelve(boolean off_shelve) {
                this.off_shelve = off_shelve;
            }
        }

        public static class ProductCustomizedGroupBean implements Serializable {
            private int id;
            private String name;
            private String subject;
            private String grade;
            private String teacher_name;
            private String status;
            private boolean off_shelve;
            private int events_count;
            private int closed_events_count;

            public int getClosed_events_count() {
                return closed_events_count;
            }

            public void setClosed_events_count(int closed_events_count) {
                this.closed_events_count = closed_events_count;
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

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public boolean isOff_shelve() {
                return off_shelve;
            }

            public void setOff_shelve(boolean off_shelve) {
                this.off_shelve = off_shelve;
            }

            public int getEvents_count() {
                return events_count;
            }

            public void setEvents_count(int events_count) {
                this.events_count = events_count;
            }
        }
    }
}