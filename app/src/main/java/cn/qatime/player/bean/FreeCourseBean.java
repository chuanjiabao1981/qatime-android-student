package cn.qatime.player.bean;

import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2017/6/8 18:08
 * @Description:
 */
public class FreeCourseBean {
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
        private int id;
        private String name;
        private String grade;
        private String subject;
        private String status;
        private PublicizesBean publicizes;
        private String published_at;
        private String model_name;
        private String teacher_name;

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

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
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

        public String getPublished_at() {
            return published_at;
        }

        public void setPublished_at(String published_at) {
            this.published_at = published_at;
        }

        public String getModel_name() {
            return model_name;
        }

        public void setModel_name(String model_name) {
            this.model_name = model_name;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public static class PublicizesBean {

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
