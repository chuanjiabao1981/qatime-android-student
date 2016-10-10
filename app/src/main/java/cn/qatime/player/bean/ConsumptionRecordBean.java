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
     * data : [{"id":13040,"amount":"-7.25","change_type":"account","created_at":"9月13日 16:54","target_type":"家庭作业批改"},{"id":13037,"amount":"-4.86","change_type":"account","created_at":"9月13日 16:54","target_type":"家庭作业批改"},{"id":13034,"amount":"-3.27","change_type":"account","created_at":"9月13日 16:54","target_type":"家庭作业批改"},{"id":13031,"amount":"-4.29","change_type":"account","created_at":"9月13日 16:54","target_type":"家庭作业批改"},{"id":13028,"amount":"-3.85","change_type":"account","created_at":"9月13日 16:54","target_type":"家庭作业批改"},{"id":13010,"amount":"-6.34","change_type":"account","created_at":"9月11日 20:56","target_type":"随堂作业批改"},{"id":13007,"amount":"-3.31","change_type":"account","created_at":"9月11日 20:56","target_type":"随堂作业批改"},{"id":13004,"amount":"-8.07","change_type":"account","created_at":"9月11日 20:56","target_type":"随堂作业批改"},{"id":13001,"amount":"-1.64","change_type":"account","created_at":"9月11日 20:56","target_type":"随堂作业批改"},{"id":12998,"amount":"-30.63","change_type":"account","created_at":"9月11日 20:56","target_type":"课程"},{"id":12995,"amount":"-0.29","change_type":"account","created_at":"9月11日 20:56","target_type":"课程"},{"id":12935,"amount":"-6.51","change_type":"account","created_at":"9月01日 16:09","target_type":"课程"},{"id":12932,"amount":"-1.32","change_type":"account","created_at":"9月01日 15:39","target_type":"家庭作业批改"},{"id":12929,"amount":"-0.26","change_type":"account","created_at":"9月01日 15:39","target_type":"家庭作业批改"},{"id":12926,"amount":"-5.21","change_type":"account","created_at":"9月01日 15:39","target_type":"家庭作业批改"},{"id":12923,"amount":"-4.91","change_type":"account","created_at":"9月01日 15:39","target_type":"家庭作业批改"},{"id":12920,"amount":"-0.99","change_type":"account","created_at":"9月01日 15:39","target_type":"家庭作业批改"},{"id":12917,"amount":"-3.95","change_type":"account","created_at":"9月01日 15:39","target_type":"家庭作业批改"},{"id":12914,"amount":"-12.99","change_type":"account","created_at":"8月31日 15:50","target_type":"课程"},{"id":12905,"amount":"-4.74","change_type":"account","created_at":"8月29日 17:45","target_type":"家庭作业批改"},{"id":12902,"amount":"-2.83","change_type":"account","created_at":"8月29日 17:45","target_type":"家庭作业批改"},{"id":12899,"amount":"-0.82","change_type":"account","created_at":"8月29日 17:45","target_type":"家庭作业批改"},{"id":12896,"amount":"-4.3","change_type":"account","created_at":"8月29日 17:45","target_type":"家庭作业批改"},{"id":12893,"amount":"-3.39","change_type":"account","created_at":"8月29日 17:45","target_type":"家庭作业批改"},{"id":12890,"amount":"-2.01","change_type":"account","created_at":"8月29日 17:45","target_type":"家庭作业批改"},{"id":12887,"amount":"-0.73","change_type":"account","created_at":"8月29日 17:45","target_type":"家庭作业批改"},{"id":12884,"amount":"-0.96","change_type":"account","created_at":"8月29日 17:45","target_type":"家庭作业批改"},{"id":12878,"amount":"-11.31","change_type":"account","created_at":"8月29日 17:38","target_type":"课程"},{"id":12875,"amount":"-13.8","change_type":"account","created_at":"8月29日 17:38","target_type":"课程"},{"id":12872,"amount":"-9.76","change_type":"account","created_at":"8月29日 17:38","target_type":"课程"}]
     */

    private int status;
    /**
     * id : 13040
     * amount : -7.25
     * change_type : account
     * created_at : 9月13日 16:54
     * target_type : 家庭作业批改
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
        private String amount;
        private String change_type;
        private String created_at;
        private String target_type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public void setChange_type(String change_type) {
            this.change_type = change_type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getTarget_type() {
            return target_type;
        }

        public void setTarget_type(String target_type) {
            this.target_type = target_type;
        }
    }
}
