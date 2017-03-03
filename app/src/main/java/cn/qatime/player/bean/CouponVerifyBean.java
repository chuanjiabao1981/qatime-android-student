package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * @author lungtify
 * @Time 2017/3/3 16:19
 * @Describe 优惠码验证
 */

public class CouponVerifyBean implements Serializable {
    private int status;

    private Data data;


    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        private int id;
        private String code;
        private String price;

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
    }
}
