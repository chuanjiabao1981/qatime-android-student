package cn.qatime.player.bean;


import java.io.Serializable;
import java.util.List;

import libraryextra.bean.AppPayParamsBean;

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
        private String coupon_code;

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


        public static class ProductInteractiveCourseBean implements Serializable{
            /**
             * id : 1
             * name : 来个一对一
             * subject : 生物
             * grade : 初一
             * price : 500.0
             * status : published
             * description : <p>大家好呀</p>
             * lessons_count : 10
             * completed_lessons_count : 0
             * closed_lessons_count : 0
             * live_start_time : 2017-03-06 18:00
             * live_end_time : 2017-04-10 18:45
             * objective : 漫无目的的走在大街上，哪里会有目标
             * suit_crowd : 活到老，学到老，学习不分年龄
             * publicize_url : http://testing.qatime.cn/imgs/course_default.png
             * publicize_info_url : http://testing.qatime.cn/imgs/course_default.png
             * publicize_list_url : http://testing.qatime.cn/imgs/course_default.png
             * publicize_app_url : http://testing.qatime.cn/imgs/course_default.png
             * chat_team_id : 28024539
             * teachers : [{"id":541,"name":"教师","nick_name":"春意盎然","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ed8858f5ed860b8e94226f37446b89c1.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_ed8858f5ed860b8e94226f37446b89c1.jpg","login_mobile":null,"email":"qatime@8.cn","teaching_years":"within_three_years","category":"高中","subject":"数学","grade_range":[],"gender":null,"birthday":null,"province":1,"city":1,"school":1,"desc":""}]
             * created_at : 1490927644
             */

            private int id;
            private String name;
            private String subject;
            private String grade;
            private String price;
            private String status;
            private String description;
            private int lessons_count;
            private int closed_lessons_count;
            private String live_start_time;
            private String live_end_time;
            private String objective;
            private String suit_crowd;
            private String publicize_url;
            private String publicize_info_url;
            private String publicize_list_url;
            private String publicize_app_url;
            private String chat_team_id;
            private int created_at;
            private List<TeachersBean> teachers;

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

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
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

            public int getLessons_count() {
                return lessons_count;
            }

            public void setLessons_count(int lessons_count) {
                this.lessons_count = lessons_count;
            }


            public int getClosed_lessons_count() {
                return closed_lessons_count;
            }

            public void setClosed_lessons_count(int closed_lessons_count) {
                this.closed_lessons_count = closed_lessons_count;
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

            public String getPublicize_url() {
                return publicize_url;
            }

            public void setPublicize_url(String publicize_url) {
                this.publicize_url = publicize_url;
            }

            public String getPublicize_info_url() {
                return publicize_info_url;
            }

            public void setPublicize_info_url(String publicize_info_url) {
                this.publicize_info_url = publicize_info_url;
            }

            public String getPublicize_list_url() {
                return publicize_list_url;
            }

            public void setPublicize_list_url(String publicize_list_url) {
                this.publicize_list_url = publicize_list_url;
            }

            public String getPublicize_app_url() {
                return publicize_app_url;
            }

            public void setPublicize_app_url(String publicize_app_url) {
                this.publicize_app_url = publicize_app_url;
            }

            public String getChat_team_id() {
                return chat_team_id;
            }

            public void setChat_team_id(String chat_team_id) {
                this.chat_team_id = chat_team_id;
            }

            public int getCreated_at() {
                return created_at;
            }

            public void setCreated_at(int created_at) {
                this.created_at = created_at;
            }

            public List<TeachersBean> getTeachers() {
                return teachers;
            }

            public void setTeachers(List<TeachersBean> teachers) {
                this.teachers = teachers;
            }

            public static class TeachersBean implements Serializable {
                /**
                 * id : 541
                 * name : 教师
                 * nick_name : 春意盎然
                 * avatar_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ed8858f5ed860b8e94226f37446b89c1.jpg
                 * ex_big_avatar_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_ed8858f5ed860b8e94226f37446b89c1.jpg
                 * login_mobile : null
                 * email : qatime@8.cn
                 * teaching_years : within_three_years
                 * category : 高中
                 * subject : 数学
                 * grade_range : []
                 * gender : null
                 * birthday : null
                 * province : 1
                 * city : 1
                 * school : 1
                 * desc :
                 */

                private int id;
                private String name;
                private String nick_name;
                private String avatar_url;
                private String ex_big_avatar_url;
                private String login_mobile;
                private String email;
                private String teaching_years;
                private String category;
                private String subject;
                private String gender;
                private String birthday;
                private int province;
                private int city;
                private int school;
                private String desc;
                private List<String> grade_range;

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

                public String getNick_name() {
                    return nick_name;
                }

                public void setNick_name(String nick_name) {
                    this.nick_name = nick_name;
                }

                public String getAvatar_url() {
                    return avatar_url;
                }

                public void setAvatar_url(String avatar_url) {
                    this.avatar_url = avatar_url;
                }

                public String getEx_big_avatar_url() {
                    return ex_big_avatar_url;
                }

                public void setEx_big_avatar_url(String ex_big_avatar_url) {
                    this.ex_big_avatar_url = ex_big_avatar_url;
                }

                public String getLogin_mobile() {
                    return login_mobile;
                }

                public void setLogin_mobile(String login_mobile) {
                    this.login_mobile = login_mobile;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public String getTeaching_years() {
                    return teaching_years;
                }

                public void setTeaching_years(String teaching_years) {
                    this.teaching_years = teaching_years;
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

                public String getBirthday() {
                    return birthday;
                }

                public void setBirthday(String birthday) {
                    this.birthday = birthday;
                }

                public int getProvince() {
                    return province;
                }

                public void setProvince(int province) {
                    this.province = province;
                }

                public int getCity() {
                    return city;
                }

                public void setCity(int city) {
                    this.city = city;
                }

                public int getSchool() {
                    return school;
                }

                public void setSchool(int school) {
                    this.school = school;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public List<String> getGrade_range() {
                    return grade_range;
                }

                public void setGrade_range(List<String> grade_range) {
                    this.grade_range = grade_range;
                }
            }
        }
        public class Product implements Serializable {
            private int id;

            private String name;

            private String subject;

            private String grade;

            private String teacher_name;

            public float price;
            public float current_price;

            private String chat_team_id;

            private int buy_tickets_count;

            private String status;

            private String description;

            private int lesson_count;

            private int preset_lesson_count;

            private int closed_lessons_count;

            private String live_start_time;

            private String live_end_time;

            private String publicize;

            private List<Lessons> lessons;

            private Chat_team chat_team;

            public int getClosed_lessons_count() {
                return closed_lessons_count;
            }

            public void setClosed_lessons_count(int closed_lessons_count) {
                this.closed_lessons_count = closed_lessons_count;
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

            public void setChat_team_id(String chat_team_id) {
                this.chat_team_id = chat_team_id;
            }

            public String getChat_team_id() {
                return this.chat_team_id;
            }

            public void setBuy_tickets_count(int buy_tickets_count) {
                this.buy_tickets_count = buy_tickets_count;
            }

            public int getBuy_tickets_count() {
                return this.buy_tickets_count;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStatus() {
                return this.status;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getDescription() {
                return this.description;
            }

            public void setLesson_count(int lesson_count) {
                this.lesson_count = lesson_count;
            }

            public int getLesson_count() {
                return this.lesson_count;
            }

            public void setPreset_lesson_count(int preset_lesson_count) {
                this.preset_lesson_count = preset_lesson_count;
            }

            public int getPreset_lesson_count() {
                return this.preset_lesson_count;
            }



            public void setLive_start_time(String live_start_time) {
                this.live_start_time = live_start_time;
            }

            public String getLive_start_time() {
                return this.live_start_time;
            }

            public void setLive_end_time(String live_end_time) {
                this.live_end_time = live_end_time;
            }

            public String getLive_end_time() {
                return this.live_end_time;
            }

            public void setPublicize(String publicize) {
                this.publicize = publicize;
            }

            public String getPublicize() {
                return this.publicize;
            }

            public void setLessons(List<Lessons> lessons) {
                this.lessons = lessons;
            }

            public List<Lessons> getLessons() {
                return this.lessons;
            }

            public void setChat_team(Chat_team chat_team) {
                this.chat_team = chat_team;
            }

            public Chat_team getChat_team() {
                return this.chat_team;
            }

        }

        public class Lessons implements Serializable {
            private int id;

            private String name;

            private String status;

            private String class_date;

            private String live_time;

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

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStatus() {
                return this.status;
            }

            public void setClass_date(String class_date) {
                this.class_date = class_date;
            }

            public String getClass_date() {
                return this.class_date;
            }

            public void setLive_time(String live_time) {
                this.live_time = live_time;
            }

            public String getLive_time() {
                return this.live_time;
            }

        }

        public class Chat_team implements Serializable {
            private String announcement;

            private List<Team_announcements> team_announcements;

            private List<Accounts> accounts;

            public void setAnnouncement(String announcement) {
                this.announcement = announcement;
            }

            public String getAnnouncement() {
                return this.announcement;
            }

            public void setTeam_announcements(List<Team_announcements> team_announcements) {
                this.team_announcements = team_announcements;
            }

            public List<Team_announcements> getTeam_announcements() {
                return this.team_announcements;
            }

            public void setAccounts(List<Accounts> accounts) {
                this.accounts = accounts;
            }

            public List<Accounts> getAccounts() {
                return this.accounts;
            }

        }

        public class Accounts implements Serializable {
            private String accid;

            private String name;

            private String icon;

            public void setAccid(String accid) {
                this.accid = accid;
            }

            public String getAccid() {
                return this.accid;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getName() {
                return this.name;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getIcon() {
                return this.icon;
            }

        }

        public class Team_announcements implements Serializable {
            private String announcement;

            private String edit_at;

            public void setAnnouncement(String announcement) {
                this.announcement = announcement;
            }

            public String getAnnouncement() {
                return this.announcement;
            }

            public void setEdit_at(String edit_at) {
                this.edit_at = edit_at;
            }

            public String getEdit_at() {
                return this.edit_at;
            }

        }

    }
}
