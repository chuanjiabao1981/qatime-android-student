package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2017/9/8.
 */

public class StudentHomeWorksBean implements Serializable {
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


        private int id;
        private String title;
        private String parent_id;
        private String status;
        private int user_id;
        private String user_name;
        private int created_at;
        private String model_name;
        private CorrectionBean correction;
        private MyHomeWorksBean.DataBean homework;
        private List<ItemsBean> items;
        private int course_id;
        private String course_name;
        private String course_model_name;
        private String tasks_count;

        public int getCourse_id() {
            return course_id;
        }

        public void setCourse_id(int course_id) {
            this.course_id = course_id;
        }

        public String getCourse_name() {
            return course_name;
        }

        public void setCourse_name(String course_name) {
            this.course_name = course_name;
        }

        public String getCourse_model_name() {
            return course_model_name;
        }

        public void setCourse_model_name(String course_model_name) {
            this.course_model_name = course_model_name;
        }

        public String getTasks_count() {
            return tasks_count;
        }

        public void setTasks_count(String tasks_count) {
            this.tasks_count = tasks_count;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getCreated_at() {
            return created_at;
        }

        public void setCreated_at(int created_at) {
            this.created_at = created_at;
        }

        public String getModel_name() {
            return model_name;
        }

        public void setModel_name(String model_name) {
            this.model_name = model_name;
        }

        public CorrectionBean getCorrection() {
            return correction;
        }

        public void setCorrection(CorrectionBean correction) {
            this.correction = correction;
        }

        public MyHomeWorksBean.DataBean getHomework() {
            return homework;
        }

        public void setHomework(MyHomeWorksBean.DataBean homework) {
            this.homework = homework;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
        }

        public static class ItemsBean implements Serializable {
            private int id;
            private String body;
            private int parent_id;
            private List<AttachmentsBean> attachments;

            public List<AttachmentsBean> getAttachments() {
                return attachments;
            }

            public void setAttachments(List<AttachmentsBean> attachments) {
                this.attachments = attachments;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getBody() {
                return body;
            }

            public void setBody(String body) {
                this.body = body;
            }

            public int getParent_id() {
                return parent_id;
            }

            public void setParent_id(int parent_id) {
                this.parent_id = parent_id;
            }
        }

        public static class CorrectionBean implements Serializable {
            private int created_at;
            private int id;
            private String model_name;
            private int parent_id;
            private String title;
            private int user_id;
            private String user_name;
            private List<ItemsBean> items;

            public int getCreated_at() {
                return created_at;
            }

            public void setCreated_at(int created_at) {
                this.created_at = created_at;
            }

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

            public int getParent_id() {
                return parent_id;
            }

            public void setParent_id(int parent_id) {
                this.parent_id = parent_id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public List<ItemsBean> getItems() {
                return items;
            }

            public void setItems(List<ItemsBean> items) {
                this.items = items;
            }

            public static class ItemsBean implements Serializable {
                private String body;
                private int id;
                private int parent_id;
                private List<AttachmentsBean> attachments;

                public List<AttachmentsBean> getAttachments() {
                    return attachments;
                }

                public void setAttachments(List<AttachmentsBean> attachments) {
                    this.attachments = attachments;
                }

                public String getBody() {
                    return body;
                }

                public void setBody(String body) {
                    this.body = body;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getParent_id() {
                    return parent_id;
                }

                public void setParent_id(int parent_id) {
                    this.parent_id = parent_id;
                }
            }
        }
    }
}
