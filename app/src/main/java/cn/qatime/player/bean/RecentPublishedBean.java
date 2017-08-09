package cn.qatime.player.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import libraryextra.bean.TeacherBean;
import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2017/3/20 19:15
 * @Describe
 */

public class RecentPublishedBean implements Serializable {

    /**
     * status : 1
     * data : {"all_published_rank":[{"id":131,"name":"试听课测试","product_type":"LiveStudio::Course","product":{"id":131,"name":"试听课测试","subject":"物理","grade":"高三","teacher_name":"王志成","price":100,"current_price":100,"chat_team_id":"50481553","chat_team_owner":"07b7c43a854ed44d36c2941f1fc5ad00","buy_tickets_count":0,"status":"teaching","preset_lesson_count":4,"completed_lesson_count":0,"taste_count":3,"completed_lessons_count":0,"closed_lessons_count":0,"started_lessons_count":0,"live_start_time":"2017-06-06 16:00","live_end_time":"2017-06-09 08:30","objective":"试听课测试","suit_crowd":"试听课测试","teacher_percentage":5,"publicize":"http://testing.qatime.cn/assets/courses/list_default-3b713cd7dd73e98c68de8d36bb011fc0.png","icons":{"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":false,"free_taste":true},"off_shelve":false}},{"id":130,"name":"测试个","product_type":"LiveStudio::Course","product":{"id":130,"name":"测试个","subject":"政治","grade":"四年级","teacher_name":"关倩倩","price":32269,"current_price":32269,"chat_team_id":"45551081","chat_team_owner":"4ab9c55ffcc2412de0d0b5f8e5afd09f","buy_tickets_count":0,"status":"teaching","preset_lesson_count":7,"completed_lesson_count":0,"taste_count":6,"completed_lessons_count":0,"closed_lessons_count":0,"started_lessons_count":0,"live_start_time":"2017-05-24 10:50","live_end_time":"2017-06-03 14:00","objective":"阿尔高VB物色个","suit_crowd":" 是v的士速递的","teacher_percentage":50,"publicize":"http://testing.qatime.cn/assets/courses/list_default-3b713cd7dd73e98c68de8d36bb011fc0.png","icons":{"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":false,"free_taste":true},"off_shelve":false}},{"id":129,"name":"测试个","product_type":"LiveStudio::Course","product":{"id":129,"name":"测试个","subject":"政治","grade":"四年级","teacher_name":"关倩倩","price":32269,"current_price":32269,"chat_team_id":"45548585","chat_team_owner":"4ab9c55ffcc2412de0d0b5f8e5afd09f","buy_tickets_count":0,"status":"teaching","preset_lesson_count":7,"completed_lesson_count":0,"taste_count":6,"completed_lessons_count":0,"closed_lessons_count":0,"started_lessons_count":0,"live_start_time":"2017-05-22 13:50","live_end_time":"2017-05-26 11:35","objective":"阿尔高VB物色个","suit_crowd":" 是v的士速递的","teacher_percentage":50,"publicize":"http://testing.qatime.cn/assets/courses/list_default-3b713cd7dd73e98c68de8d36bb011fc0.png","icons":{"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":false,"free_taste":true},"off_shelve":false}},{"id":128,"name":"测试个","product_type":"LiveStudio::Course","product":{"id":128,"name":"测试个","subject":"政治","grade":"四年级","teacher_name":"关倩倩","price":32269,"current_price":32269,"chat_team_id":"45545265","chat_team_owner":"4ab9c55ffcc2412de0d0b5f8e5afd09f","buy_tickets_count":1,"status":"teaching","preset_lesson_count":7,"completed_lesson_count":0,"taste_count":6,"completed_lessons_count":0,"closed_lessons_count":0,"started_lessons_count":0,"live_start_time":"2017-05-22 13:50","live_end_time":"2017-05-26 11:35","objective":"阿尔高VB物色个","suit_crowd":" 是v的士速递的","teacher_percentage":50,"publicize":"http://testing.qatime.cn/assets/courses/list_default-3b713cd7dd73e98c68de8d36bb011fc0.png","icons":{"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":false,"free_taste":true},"off_shelve":false}}]}
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
        private List<AllPublishedRankBean> all_published_rank;

        public List<AllPublishedRankBean> getAll_published_rank() {
            return all_published_rank;
        }

        public void setAll_published_rank(List<AllPublishedRankBean> all_published_rank) {
            this.all_published_rank = all_published_rank;
        }

        public static class AllPublishedRankBean {
            /**
             * id : 131
             * name : 试听课测试
             * product_type : LiveStudio::Course
             * product : {"id":131,"name":"试听课测试","subject":"物理","grade":"高三","teacher_name":"王志成","price":100,"current_price":100,"chat_team_id":"50481553","chat_team_owner":"07b7c43a854ed44d36c2941f1fc5ad00","buy_tickets_count":0,"status":"teaching","preset_lesson_count":4,"completed_lesson_count":0,"taste_count":3,"completed_lessons_count":0,"closed_lessons_count":0,"started_lessons_count":0,"live_start_time":"2017-06-06 16:00","live_end_time":"2017-06-09 08:30","objective":"试听课测试","suit_crowd":"试听课测试","teacher_percentage":5,"publicize":"http://testing.qatime.cn/assets/courses/list_default-3b713cd7dd73e98c68de8d36bb011fc0.png","icons":{"refund_any_time":true,"coupon_free":true,"cheap_moment":false,"join_cheap":false,"free_taste":true},"off_shelve":false}
             */

            private int id;
            private String name;
            private String product_type;
            private ProductBean product;

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
}
