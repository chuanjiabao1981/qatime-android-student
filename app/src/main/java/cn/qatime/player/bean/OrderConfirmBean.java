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

        private App_pay_params app_pay_params;

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

        public void setApp_pay_params(App_pay_params app_pay_params) {
            this.app_pay_params = app_pay_params;
        }

        public App_pay_params getApp_pay_params() {
            return this.app_pay_params;
        }

    }

    public class App_pay_params implements Serializable {
        private String appid;

        private String partnerid;
        @SerializedName("package")
        private String packages;

        private String timestamp;

        private String prepayid;

        private String noncestr;

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

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return this.sign;
        }

    }
}
