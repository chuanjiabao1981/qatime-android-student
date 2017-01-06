package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * @author Tianhaoranly
 * @date 2017/1/5 17:40
 * @Description:
 */
public class OrderRefundBean {

    /**
     * status : 1
     * data : {"id":null,"transaction_no":"201612062024180943","pay_type":"account","status":"init","created_at":"2016-12-06T20:24:18.365+08:00","amount":"0.0","refund_amount":"0.0","reason":null}
     */

    private int status;
    /**
     * id : null
     * transaction_no : 201612062024180943
     * pay_type : account
     * status : init
     * created_at : 2016-12-06T20:24:18.365+08:00
     * amount : 0.0
     * refund_amount : 0.0
     * reason : null
     */

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

    public static class DataBean implements Serializable {
        private String id;
        private String transaction_no;
        private String pay_type;
        private String status;
        private String created_at;
        private String amount;
        private String refund_amount;
        private String reason;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTransaction_no() {
            return transaction_no;
        }

        public void setTransaction_no(String transaction_no) {
            this.transaction_no = transaction_no;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRefund_amount() {
            return refund_amount;
        }

        public void setRefund_amount(String refund_amount) {
            this.refund_amount = refund_amount;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
