package cn.qatime.player.bean;

/**
 * @author Tianhaoranly
 * @date 2017/1/9 14:27
 * @Description:
 */
public class NotifyStatusBean {


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
        private Object owner_id;
        private boolean notice;
        private boolean email;
        private boolean message;
        private int before_hours;
        private int before_minutes;

        public Object getOwner_id() {
            return owner_id;
        }

        public void setOwner_id(Object owner_id) {
            this.owner_id = owner_id;
        }

        public boolean isNotice() {
            return notice;
        }

        public void setNotice(boolean notice) {
            this.notice = notice;
        }

        public boolean isEmail() {
            return email;
        }

        public void setEmail(boolean email) {
            this.email = email;
        }

        public boolean isMessage() {
            return message;
        }

        public void setMessage(boolean message) {
            this.message = message;
        }

        public int getBefore_hours() {
            return before_hours;
        }

        public void setBefore_hours(int before_hours) {
            this.before_hours = before_hours;
        }

        public int getBefore_minutes() {
            return before_minutes;
        }

        public void setBefore_minutes(int before_minutes) {
            this.before_minutes = before_minutes;
        }
    }
}
