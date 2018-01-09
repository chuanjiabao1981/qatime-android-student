package cn.qatime.player.bean.exam;

import java.io.Serializable;

/**
 * @author luntify
 * @date 2017/12/14 17:12
 * @Description:
 */

public class Examination implements Serializable {

    /**
     * status : 1
     * data : {"ticket":null,"paper":{"id":1,"name":"20171208","duration":1800,"topics_count":20,"grade_category":"初中","subject":"英语","price":"10.0","users_count":0,"status":"published","score":40,"type":"Exam::JuniorPaper","created_at":"2017-12-13T16:25:22.394+08:00","updated_at":"2017-12-13T18:05:28.079+08:00","published_at":null,"categories":[]}}
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
         * ticket : null
         * paper : {"id":1,"name":"20171208","duration":1800,"topics_count":20,"grade_category":"初中","subject":"英语","price":"10.0","users_count":0,"status":"published","score":40,"type":"Exam::JuniorPaper","created_at":"2017-12-13T16:25:22.394+08:00","updated_at":"2017-12-13T18:05:28.079+08:00","published_at":null,"categories":[]}
         */

        private Object ticket;
        private Paper paper;

        public Object getTicket() {
            return ticket;
        }

        public void setTicket(Object ticket) {
            this.ticket = ticket;
        }

        public Paper getPaper() {
            return paper;
        }

        public void setPaper(Paper paper) {
            this.paper = paper;
        }


    }
}
