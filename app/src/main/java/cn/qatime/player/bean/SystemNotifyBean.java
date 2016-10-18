package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author lungtify
 * @Time 2016/10/18 17:05
 * @Describe
 */
public class SystemNotifyBean implements Serializable {

    /**
     * status : 1
     * data : [{"id":32040,"read":false,"action_name":"notice_create","notice_content":"公告: \"\" --  老师","type":"live_studio/course","created_at":"9月30日 09:20"},{"id":32005,"read":false,"action_name":"notice_create","notice_content":"公告: \"\" --  老师","type":"live_studio/course","created_at":"9月22日 13:36"},{"id":32002,"read":false,"action_name":"notice_create","notice_content":"公告: \"\" --  老师","type":"live_studio/course","created_at":"9月22日 13:32"},{"id":31999,"read":false,"action_name":"notice_create","notice_content":"公告: \"\" --  老师","type":"live_studio/course","created_at":"9月22日 13:32"}]
     */

    private int status;
    /**
     * id : 32040
     * read : false
     * action_name : notice_create
     * notice_content : 公告: "" --  老师
     * type : live_studio/course
     * created_at : 9月30日 09:20
     */

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

    public class DataBean implements Serializable {
        private int id;
        private boolean read;
        private String action_name;
        private String notice_content;
        private String type;
        private String created_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isRead() {
            return read;
        }

        public void setRead(boolean read) {
            this.read = read;
        }

        public String getAction_name() {
            return action_name;
        }

        public void setAction_name(String action_name) {
            this.action_name = action_name;
        }

        public String getNotice_content() {
            return notice_content;
        }

        public void setNotice_content(String notice_content) {
            this.notice_content = notice_content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
