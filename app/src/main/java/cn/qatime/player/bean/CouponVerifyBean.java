package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * @author lungtify
 * @Time 2017/3/3 16:19
 * @Describe 优惠码验证
 */

public class CouponVerifyBean implements Serializable {

    /**
     * status : 1
     * data : {"id":2,"code":"4321","price":"10.0","total_amount":100,"coupon_amount":"5.0"}
     */

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
        /**
         * id : 2
         * code : 4321
         * price : 10.0
         * total_amount : 100
         * coupon_amount : 5.0
         */

        private int id;
        private String code;
        private String price;
        private double total_amount;
        private String coupon_amount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public double getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }

        public String getCoupon_amount() {
            return coupon_amount;
        }

        public void setCoupon_amount(String coupon_amount) {
            this.coupon_amount = coupon_amount;
        }
    }
}
