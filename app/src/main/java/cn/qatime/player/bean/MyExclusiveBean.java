package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2017/8/1 17:33
 * @Describe
 */

public class MyExclusiveBean implements Serializable {

    /**
     * status : 1
     * data : [{"id":829,"used_count":0,"buy_count":2,"customized_group":{"id":5,"name":"高三数学小班课","publicizes_url":{"app_info":"http://testing.qatime.cn/assets/groups/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png","list":"http://testing.qatime.cn/assets/groups/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png","info":"http://testing.qatime.cn/assets/groups/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png"},"subject":"数学","grade":"高三","view_tickets_count":0,"events_count":2,"closed_events_count":0,"start_at":1501257600,"end_at":1501257600}}]
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
         * id : 829
         * used_count : 0
         * buy_count : 2
         * customized_group : {"id":5,"name":"高三数学小班课","publicizes_url":{"app_info":"http://testing.qatime.cn/assets/groups/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png","list":"http://testing.qatime.cn/assets/groups/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png","info":"http://testing.qatime.cn/assets/groups/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png"},"subject":"数学","grade":"高三","view_tickets_count":0,"events_count":2,"closed_events_count":0,"start_at":1501257600,"end_at":1501257600}
         */

        private int id;
        private int used_count;
        private int buy_count;
        private CustomizedGroupBean customized_group;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUsed_count() {
            return used_count;
        }

        public void setUsed_count(int used_count) {
            this.used_count = used_count;
        }

        public int getBuy_count() {
            return buy_count;
        }

        public void setBuy_count(int buy_count) {
            this.buy_count = buy_count;
        }

        public CustomizedGroupBean getCustomized_group() {
            return customized_group;
        }

        public void setCustomized_group(CustomizedGroupBean customized_group) {
            this.customized_group = customized_group;
        }

        public static class CustomizedGroupBean {
            /**
             * id : 5
             * name : 高三数学小班课
             * publicizes_url : {"app_info":"http://testing.qatime.cn/assets/groups/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png","list":"http://testing.qatime.cn/assets/groups/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png","info":"http://testing.qatime.cn/assets/groups/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png"}
             * subject : 数学
             * grade : 高三
             * view_tickets_count : 0
             * events_count : 2
             * closed_events_count : 0
             * start_at : 1501257600
             * end_at : 1501257600
             */

            private int id;
            private String name;
            private PublicizesUrlBean publicizes_url;
            private String subject;
            private String grade;
            private int view_tickets_count;
            private int events_count;
            private int closed_events_count;
            private long start_at;
            private long end_at;
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

            public PublicizesUrlBean getPublicizes_url() {
                return publicizes_url;
            }

            public void setPublicizes_url(PublicizesUrlBean publicizes_url) {
                this.publicizes_url = publicizes_url;
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

            public int getView_tickets_count() {
                return view_tickets_count;
            }

            public void setView_tickets_count(int view_tickets_count) {
                this.view_tickets_count = view_tickets_count;
            }

            public int getEvents_count() {
                return events_count;
            }

            public void setEvents_count(int events_count) {
                this.events_count = events_count;
            }

            public int getClosed_events_count() {
                return closed_events_count;
            }

            public void setClosed_events_count(int closed_events_count) {
                this.closed_events_count = closed_events_count;
            }

            public long getStart_at() {
                return start_at;
            }

            public void setStart_at(long start_at) {
                this.start_at = start_at;
            }

            public long getEnd_at() {
                return end_at;
            }

            public void setEnd_at(long end_at) {
                this.end_at = end_at;
            }

            public String getTeacher_name() {
                return teacher_name;
            }

            public static class PublicizesUrlBean {
                /**
                 * app_info : http://testing.qatime.cn/assets/groups/mathematics/app_info_default-af5e8db92e98e136a1a7b724b7dedd49.png
                 * list : http://testing.qatime.cn/assets/groups/mathematics/list_default-1b3bf1a3d82979605caea89854b60a44.png
                 * info : http://testing.qatime.cn/assets/groups/mathematics/info_default-3c9e978bbb618ce34ebd31defd6e2c61.png
                 */

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
