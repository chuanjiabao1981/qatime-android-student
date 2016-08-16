package cn.qatime.player.bean;

import com.google.gson.annotations.SerializedName;

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

    public class Data implements Serializable {
        private String id;

        private String status;

        private String prepay_id;

        private String nonce_str;

        private Generate_app_pay_params generate_app_pay_params;

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

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }

        public String getPrepay_id() {
            return this.prepay_id;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getNonce_str() {
            return this.nonce_str;
        }

        public void setGenerate_app_pay_params(Generate_app_pay_params generate_app_pay_params) {
            this.generate_app_pay_params = generate_app_pay_params;
        }

        public Generate_app_pay_params getGenerate_app_pay_params() {
            return this.generate_app_pay_params;
        }

    }

    public class Generate_app_pay_params {
        private String appid;

        private String partnerid;
        @SerializedName("packages")
        private String packages;

        private String timestamp;

        private String body;

        private String out_trade_no;

        private int total_fee;

        private String spbill_create_ip;

        private String notify_url;

        private String trade_type;

        private String fee_type;

        private String sign;

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getAppid() {
            return this.appid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPartnerid() {
            return this.partnerid;
        }

        public void setPackage(String packages) {
            this.packages = packages;
        }

        public String getPackage() {
            return this.packages;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getTimestamp() {
            return this.timestamp;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getBody() {
            return this.body;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getOut_trade_no() {
            return this.out_trade_no;
        }

        public void setTotal_fee(int total_fee) {
            this.total_fee = total_fee;
        }

        public int getTotal_fee() {
            return this.total_fee;
        }

        public void setSpbill_create_ip(String spbill_create_ip) {
            this.spbill_create_ip = spbill_create_ip;
        }

        public String getSpbill_create_ip() {
            return this.spbill_create_ip;
        }

        public void setNotify_url(String notify_url) {
            this.notify_url = notify_url;
        }

        public String getNotify_url() {
            return this.notify_url;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getTrade_type() {
            return this.trade_type;
        }

        public void setFee_type(String fee_type) {
            this.fee_type = fee_type;
        }

        public String getFee_type() {
            return this.fee_type;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return this.sign;
        }

    }
}
