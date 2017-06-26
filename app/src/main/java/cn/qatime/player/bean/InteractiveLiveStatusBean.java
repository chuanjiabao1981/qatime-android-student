package cn.qatime.player.bean;

import java.io.Serializable;

/**
 * @author lungtify
 * @Time 2017/5/12 15:14
 * @Describe
 */

public class InteractiveLiveStatusBean implements Serializable {

    /**
     * status : 1
     * data : {"live_info":{"id":"72","name":"一对一互动课程-第二课时","status":"init","room_id":""}}
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
         * live_info : {"id":"72","name":"一对一互动课程-第二课时","status":"init","room_id":""}
         */

        private LiveInfoBean live_info;

        public LiveInfoBean getLive_info() {
            return live_info;
        }

        public void setLive_info(LiveInfoBean live_info) {
            this.live_info = live_info;
        }

        public static class LiveInfoBean {
            /**
             * id : 72
             * name : 一对一互动课程-第二课时
             * status : init
             * room_id :
             */

            private String id;
            private String name;
            private String status;
            private String room_id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getRoom_id() {
                return room_id;
            }

            public void setRoom_id(String room_id) {
                this.room_id = room_id;
            }
        }
    }
}
