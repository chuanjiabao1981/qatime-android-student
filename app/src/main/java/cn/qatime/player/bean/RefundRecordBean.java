package cn.qatime.player.bean;

import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2017/1/6 15:20
 * @Description:
 */
public class RefundRecordBean {

    /**
     * status : 1
     * data : [{"id":1421,"transaction_no":"201701061419540871","pay_type":"weixin","status":"cancel","created_at":"2017-01-06T14:19:54.846+08:00","amount":"50.0","refund_amount":"50.0","reason":"hhhh"},{"id":1420,"transaction_no":"201701061419540871","pay_type":"weixin","status":"cancel","created_at":"2017-01-06T14:19:54.846+08:00","amount":"50.0","refund_amount":"50.0","reason":"bhhgw"},{"id":1419,"transaction_no":"201701061419540871","pay_type":"weixin","status":"cancel","created_at":"2017-01-06T14:19:54.846+08:00","amount":"50.0","refund_amount":"50.0","reason":"hjhx"},{"id":1418,"transaction_no":"201701061419540871","pay_type":"weixin","status":"cancel","created_at":"2017-01-06T14:19:54.846+08:00","amount":"50.0","refund_amount":"50.0","reason":"444"},{"id":1417,"transaction_no":"201701061419540871","pay_type":"weixin","status":"cancel","created_at":"2017-01-06T14:19:54.846+08:00","amount":"50.0","refund_amount":"50.0","reason":"你猜啊"},{"id":1414,"transaction_no":"201701061403020086","pay_type":"weixin","status":"refunded","created_at":"2017-01-06T14:03:02.475+08:00","amount":"50.0","refund_amount":"50.0","reason":"啦啦啦"},{"id":1413,"transaction_no":"201701061403020086","pay_type":"weixin","status":"cancel","created_at":"2017-01-06T14:03:02.475+08:00","amount":"50.0","refund_amount":"50.0","reason":"退款"},{"id":1412,"transaction_no":"201701061403020086","pay_type":"weixin","status":"cancel","created_at":"2017-01-06T14:03:02.475+08:00","amount":"50.0","refund_amount":"50.0","reason":"想退就退"},{"id":1411,"transaction_no":"201701061403020086","pay_type":"weixin","status":"cancel","created_at":"2017-01-06T14:03:02.475+08:00","amount":"50.0","refund_amount":"50.0","reason":"你说呢"},{"id":1410,"transaction_no":"201701061403020086","pay_type":"weixin","status":"cancel","created_at":"2017-01-06T14:03:02.475+08:00","amount":"50.0","refund_amount":"50.0","reason":"111"}]
     */

    private int status;
    /**
     * id : 1421
     * transaction_no : 201701061419540871
     * pay_type : weixin
     * status : cancel
     * created_at : 2017-01-06T14:19:54.846+08:00
     * amount : 50.0
     * refund_amount : 50.0
     * reason : hhhh
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

    public static class DataBean {
        private int id;
        private String transaction_no;
        private String pay_type;
        private String status;
        private String created_at;
        private String amount;
        private String refund_amount;
        private String reason;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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
