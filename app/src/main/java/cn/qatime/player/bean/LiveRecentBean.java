package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/3/21 10:56
 * @Describe
 */

public class LiveRecentBean implements Serializable {
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
         * id : 602
         * model_name : LiveStudio::Lesson
         * name : 8
         * course_id : 182
         * course_model_name : LiveStudio::Course
         * status : init
         * publicizes : {"info":{"url":"http://testing.qatime.cn/assets/courses/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png"},"list":{"url":"http://testing.qatime.cn/assets/courses/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png"},"small":{"url":"http://testing.qatime.cn/assets/courses/mathematics/small_default-812233594daef82c95313ab4bfb99bd6.png"},"app_info":{"url":"http://testing.qatime.cn/assets/courses/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png"}}
         * start_at : 1510251000
         */

        private int id;
        private String model_name;
        private String name;
        private int course_id;
        private String course_model_name;
        private String status;
        private PublicizesBean publicizes;
        private long start_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getModel_name() {
            return model_name;
        }

        public void setModel_name(String model_name) {
            this.model_name = model_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCourse_id() {
            return course_id;
        }

        public void setCourse_id(int course_id) {
            this.course_id = course_id;
        }

        public String getCourse_model_name() {
            return course_model_name;
        }

        public void setCourse_model_name(String course_model_name) {
            this.course_model_name = course_model_name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public PublicizesBean getPublicizes() {
            return publicizes;
        }

        public void setPublicizes(PublicizesBean publicizes) {
            this.publicizes = publicizes;
        }

        public long getStart_at() {
            return start_at;
        }

        public void setStart_at(long start_at) {
            this.start_at = start_at;
        }

        public static class PublicizesBean {
            /**
             * info : {"url":"http://testing.qatime.cn/assets/courses/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png"}
             * list : {"url":"http://testing.qatime.cn/assets/courses/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png"}
             * small : {"url":"http://testing.qatime.cn/assets/courses/mathematics/small_default-812233594daef82c95313ab4bfb99bd6.png"}
             * app_info : {"url":"http://testing.qatime.cn/assets/courses/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png"}
             */

            private InfoBean info;
            private ListBean list;
            private SmallBean small;
            private AppInfoBean app_info;

            public InfoBean getInfo() {
                return info;
            }

            public void setInfo(InfoBean info) {
                this.info = info;
            }

            public ListBean getList() {
                return list;
            }

            public void setList(ListBean list) {
                this.list = list;
            }

            public SmallBean getSmall() {
                return small;
            }

            public void setSmall(SmallBean small) {
                this.small = small;
            }

            public AppInfoBean getApp_info() {
                return app_info;
            }

            public void setApp_info(AppInfoBean app_info) {
                this.app_info = app_info;
            }

            public static class InfoBean {
                /**
                 * url : http://testing.qatime.cn/assets/courses/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png
                 */

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            public static class ListBean {
                /**
                 * url : http://testing.qatime.cn/assets/courses/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png
                 */

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            public static class SmallBean {
                /**
                 * url : http://testing.qatime.cn/assets/courses/mathematics/small_default-812233594daef82c95313ab4bfb99bd6.png
                 */

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            public static class AppInfoBean {
                /**
                 * url : http://testing.qatime.cn/assets/courses/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png
                 */

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }
    }
}
