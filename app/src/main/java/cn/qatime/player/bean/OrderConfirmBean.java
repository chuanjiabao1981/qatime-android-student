package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * @author luntify
 * @date 2016/8/15 11:06
 * @Description
 */
public class OrderConfirmBean implements Serializable {
    private int status;

    private Data data;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }

    public class Data {
        private String id;

        private String status;

        private String prepayid;

        private String noncestr;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPrepayid() {
            return this.prepayid;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getNoncestr() {
            return this.noncestr;
        }

    }
}
