package cn.qatime.player.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Tianhaoranly
 * @date 2016/9/28 16:46
 * @Description:
 */
public class RechargeBean{

    /**
     * status : 1
     * data : {"id":"201609281659010468","amount":"1.0","pay_type":"weixin","status":"unpaid","source":"app","created_at":"2016-09-28T16:59:01.349+08:00","updated_at":"2016-09-28T16:59:01.349+08:00","pay_at":null,"prepay_id":"wx201609281659015a22c20cba0147363422","nonce_str":"FGMsCO63cyoKCpH1","app_pay_params":{"appid":"wxf2dfbeb5f641ce40","partnerid":"1379576802","package":"Sign=WXPay","timestamp":"1475053141","prepayid":"wx201609281659015a22c20cba0147363422","noncestr":"FGMsCO63cyoKCpH1","sign":"ECE6B302C06704E0606B061B168B388A"}}
     */

    private int status;
    /**
     * id : 201609281659010468
     * amount : 1.0
     * pay_type : weixin
     * status : unpaid
     * source : app
     * created_at : 2016-09-28T16:59:01.349+08:00
     * updated_at : 2016-09-28T16:59:01.349+08:00
     * pay_at : null
     * prepay_id : wx201609281659015a22c20cba0147363422
     * nonce_str : FGMsCO63cyoKCpH1
     * app_pay_params : {"appid":"wxf2dfbeb5f641ce40","partnerid":"1379576802","package":"Sign=WXPay","timestamp":"1475053141","prepayid":"wx201609281659015a22c20cba0147363422","noncestr":"FGMsCO63cyoKCpH1","sign":"ECE6B302C06704E0606B061B168B388A"}
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

    public static class DataBean{
        private String id;
        private String amount;
        private String pay_type;
        private String status;
        private String source;
        private String created_at;
        private String updated_at;
        private Object pay_at;
        private String prepay_id;
        private String nonce_str;
        /**
         * appid : wxf2dfbeb5f641ce40
         * partnerid : 1379576802
         * package : Sign=WXPay
         * timestamp : 1475053141
         * prepayid : wx201609281659015a22c20cba0147363422
         * noncestr : FGMsCO63cyoKCpH1
         * sign : ECE6B302C06704E0606B061B168B388A
         */

        private AppPayParamsBean app_pay_params;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
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

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public Object getPay_at() {
            return pay_at;
        }

        public void setPay_at(Object pay_at) {
            this.pay_at = pay_at;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public AppPayParamsBean getApp_pay_params() {
            return app_pay_params;
        }

        public void setApp_pay_params(AppPayParamsBean app_pay_params) {
            this.app_pay_params = app_pay_params;
        }

        public static class AppPayParamsBean implements Serializable {
            private String appid;
            private String partnerid;
            @SerializedName("package")
            private String packageX;
            private String timestamp;
            private String prepayid;
            private String noncestr;
            private String sign;

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getPartnerid() {
                return partnerid;
            }

            public void setPartnerid(String partnerid) {
                this.partnerid = partnerid;
            }

            public String getPackageX() {
                return packageX;
            }

            public void setPackageX(String packageX) {
                this.packageX = packageX;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
            }

            public String getPrepayid() {
                return prepayid;
            }

            public void setPrepayid(String prepayid) {
                this.prepayid = prepayid;
            }

            public String getNoncestr() {
                return noncestr;
            }

            public void setNoncestr(String noncestr) {
                this.noncestr = noncestr;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }
        }
    }
}