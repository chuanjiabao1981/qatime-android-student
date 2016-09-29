package cn.qatime.player.bean;

import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2016/9/28 13:22
 * @Description:
 */
public class ConsumptionRecordBean {
    /**
     * status : 1
     * data : [{"id":"201609281318520642","amount":"888888.0","pay_type":"alipay","status":"unpaid","source":"app","created_at":"2016-09-28T13:18:52.008+08:00","updated_at":"2016-09-28T13:18:52.008+08:00","pay_at":null},{"id":"201609281024590559","amount":"1000.0","pay_type":"alipay","status":"unpaid","source":"app","created_at":"2016-09-28T10:24:59.786+08:00","updated_at":"2016-09-28T10:24:59.786+08:00","pay_at":null},{"id":"201609281024520096","amount":"1000.0","pay_type":"weixin","status":"unpaid","source":"app","created_at":"2016-09-28T10:24:52.321+08:00","updated_at":"2016-09-28T10:24:52.321+08:00","pay_at":null},{"id":"201609281022080412","amount":"1000.0","pay_type":"alipay","status":"unpaid","source":"app","created_at":"2016-09-28T10:22:08.505+08:00","updated_at":"2016-09-28T10:22:08.505+08:00","pay_at":null},{"id":"201609271830520288","amount":"1000.0","pay_type":"alipay","status":"unpaid","source":"app","created_at":"2016-09-27T18:30:52.735+08:00","updated_at":"2016-09-27T18:30:52.735+08:00","pay_at":null}]
     */
    /**
     * id : 201609281318520642
     * amount : 888888.0
     * pay_type : alipay
     * status : unpaid
     * source : app
     * created_at : 2016-09-28T13:18:52.008+08:00
     * updated_at : 2016-09-28T13:18:52.008+08:00
     * pay_at : null
     */

    private List<DataBean> data;


    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String amount;
        private String change_type;
        private String target_type;
        private String created_at;

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

        public String getChange_type() {
            return change_type;
        }

        public void setTarget_type(String target_type) {
            this.target_type = target_type;
        }

        public String getTarget_type() {
            return target_type;
        }

        public void setChange_type(String change_type) {
            this.change_type = change_type;
        }
        public void a(Object o){}
        public void a(String o){}

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }


    }
}
