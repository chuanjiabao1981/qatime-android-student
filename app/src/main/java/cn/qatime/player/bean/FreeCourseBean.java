package cn.qatime.player.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import libraryextra.bean.TeacherBean;
import libraryextra.utils.StringUtils;

/**
 * @author Tianhaoranly
 * @date 2017/6/8 18:08
 * @Description:
 */
public class FreeCourseBean {
    /**
     * status : 1
     * data : [{"product_type":"LiveStudio::VideoCourse","product":{"id":18,"name":"空手道免费体验课","subject":"数学","grade":"高一","teacher_name":"刘刚老师","teacher":{"id":3056,"name":"刘刚老师","nick_name":"昵称刘刚","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/f9c2b225a5fd296a010c03666b543e38.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_f9c2b225a5fd296a010c03666b543e38.jpg","login_mobile":"13212345678","email":null,"is_guest":false,"teaching_years":"within_three_years","category":"高中","subject":"数学","grade_range":[],"gender":"male","birthday":"2017-03-02","province":25,"city":606,"school":140,"school_id":140,"desc":"啊撒旦发送的发啊说是道非"},"price":0,"current_price":0,"chat_team_id":null,"chat_team_owner":null,"buy_tickets_count":3,"status":"published","lesson_count":3,"video_lessons_count":3,"preset_lesson_count":3,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"closed_lessons_count":0,"objective":"人人都能空手套白狼","suit_crowd":"小学生","teacher_percentage":0,"publicize":"http://testing.qatime.cn/assets/video_courses/list_default-79f13f78089740c6be5bbba12768f1ae.png","chat_team":{},"sell_type":"free","total_duration":6,"icons":{"free_taste":false,"coupon_free":true,"cheap_moment":false},"off_shelve":false}},{"product_type":"LiveStudio::VideoCourse","product":{"id":12,"name":"测试测试测试","subject":"数学","grade":"一年级","teacher_name":"HZH11","teacher":{"id":2479,"name":"HZH11","nick_name":"答疑时间_客服","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/fbb9b57027c5731b7670a85bd93a9fa6.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_fbb9b57027c5731b7670a85bd93a9fa6.jpg","login_mobile":"18535307198","email":"kuangyizhonggaoyi@163.com","is_guest":false,"teaching_years":"within_three_years","category":"高中","subject":"数学","grade_range":[],"gender":null,"birthday":null,"province":1,"city":1,"school":4,"school_id":4,"desc":""},"price":0,"current_price":0,"chat_team_id":null,"chat_team_owner":null,"buy_tickets_count":2,"status":"published","lesson_count":5,"video_lessons_count":5,"preset_lesson_count":5,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"closed_lessons_count":0,"objective":"赶紧换感觉很怪高科技更健康韩国开个，好个， ","suit_crowd":"交话费vhjjhkh 孤鸿寡鹄","teacher_percentage":0,"publicize":"http://testing.qatime.cn/assets/video_courses/list_default-79f13f78089740c6be5bbba12768f1ae.png","chat_team":{},"sell_type":"free","total_duration":12771,"icons":{"free_taste":false,"coupon_free":true,"cheap_moment":false},"off_shelve":false}}]
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
         * product_type : LiveStudio::VideoCourse
         * product : {"id":18,"name":"空手道免费体验课","subject":"数学","grade":"高一","teacher_name":"刘刚老师","teacher":{"id":3056,"name":"刘刚老师","nick_name":"昵称刘刚","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/f9c2b225a5fd296a010c03666b543e38.jpg","ex_big_avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ex_big_f9c2b225a5fd296a010c03666b543e38.jpg","login_mobile":"13212345678","email":null,"is_guest":false,"teaching_years":"within_three_years","category":"高中","subject":"数学","grade_range":[],"gender":"male","birthday":"2017-03-02","province":25,"city":606,"school":140,"school_id":140,"desc":"啊撒旦发送的发啊说是道非"},"price":0,"current_price":0,"chat_team_id":null,"chat_team_owner":null,"buy_tickets_count":3,"status":"published","lesson_count":3,"video_lessons_count":3,"preset_lesson_count":3,"completed_lesson_count":0,"taste_count":0,"completed_lessons_count":0,"closed_lessons_count":0,"objective":"人人都能空手套白狼","suit_crowd":"小学生","teacher_percentage":0,"publicize":"http://testing.qatime.cn/assets/video_courses/list_default-79f13f78089740c6be5bbba12768f1ae.png","chat_team":{},"sell_type":"free","total_duration":6,"icons":{"free_taste":false,"coupon_free":true,"cheap_moment":false},"off_shelve":false}
         */

        private String product_type;
        private ProductBean product;

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

        public ProductBean getProduct() {
            return product;
        }

        public void setProduct(ProductBean product) {
            this.product = product;
        }

        public static class ProductBean {
            /**
             * id : 131
             * name : 试听课测试
             * subject : 物理
             * grade : 高三
             * teacher_name : 王志成
             * price : 100
             * current_price : 100
             * chat_team_id : 50481553
             * chat_team_owner : 07b7c43a854ed44d36c2941f1fc5ad00
             * buy_tickets_count : 0
             * status : teaching
             * preset_lesson_count : 4
             * completed_lesson_count : 0
             * taste_count : 3
             * completed_lessons_count : 0
             * closed_lessons_count : 0
             * started_lessons_count : 0
             * live_start_time : 2017-06-06 16:00
             * live_end_time : 2017-06-09 08:30
             * objective : 试听课测试
             * suit_crowd : 试听课测试
             * teacher_percentage : 5
             * publicize : http://testing.qatime.cn/assets/courses/list_default-3b713cd7dd73e98c68de8d36bb011fc0.png
             * icons : {"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":false,"free_taste":true}
             * off_shelve : false
             */

            private int id;
            private String name;
            private String subject;
            @SerializedName(value = "publicize", alternate = "publicize_url")
            private String publicize;
            private String grade;
            private String teacher_name;
            private PublicizesUrlBean publicizes_url;
            private List<TeacherBean> teachers;

            public String getTeacher_name() {
                if (!StringUtils.isNullOrBlanK(teacher_name)) {
                    return teacher_name;
                } else {
                    if (teachers != null && teachers.size() > 0) {
                        return teachers.get(0).getName();
                    } else {
                        return "";
                    }
                }
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


            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }

            public String getPublicize() {
                if (!StringUtils.isNullOrBlanK(publicize)) {
                    return publicize;
                } else {
                    return publicizes_url != null ? publicizes_url.getList() : "";
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
