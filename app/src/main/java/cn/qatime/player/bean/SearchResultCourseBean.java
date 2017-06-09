package cn.qatime.player.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2017/6/9 15:48
 * @Description:
 */
public class SearchResultCourseBean {

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
            private int total_entries;

            public int getTotal_entries() {
                return total_entries;
            }

            public void setTotal_entries(int total_entries) {
                this.total_entries = total_entries;
            }

//                private String price;
//                private String current_price;
//                private String chat_team_id;
//                private String chat_team_owner;
//                private int buy_tickets_count;
//                private String status;
//                private int preset_lesson_count;
//                private int completed_lesson_count;
//                private int taste_count;
//                private int completed_lessons_count;
//                private int closed_lessons_count;
//                private int started_lessons_count;
//                private String live_start_time;
//                private String live_end_time;
//                private String objective;
//                private String suit_crowd;
//                private int teacher_percentage;
//                private IconsBean icons;
//                private boolean off_shelve;


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

            public String getPublicize() {
                return publicize;
            }

            public void setPublicize(String publicize) {
                this.publicize = publicize;
            }
        }
    }
}
