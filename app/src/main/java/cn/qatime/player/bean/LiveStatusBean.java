package cn.qatime.player.bean;

import java.util.List;

/**
 * @author lungtify
 * @Time 2017/3/23 17:03
 * @Describe
 */

public class LiveStatusBean {

    /**
     * status : 1
     * data : {"live_info":{"id":79,"name":"第一单元  侵略和反抗","status":"missed","board":0,"camera":0,"t":1490259752},"online_users":["2857"],"timestamp":1490259752}
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
         * live_info : {"id":79,"name":"第一单元  侵略和反抗","status":"missed","board":0,"camera":0,"t":1490259752}
         * online_users : ["2857"]
         * timestamp : 1490259752
         */

        private LiveInfoBean live_info;
        private int timestamp;
        private List<String> online_users;

        public LiveInfoBean getLive_info() {
            return live_info;
        }

        public void setLive_info(LiveInfoBean live_info) {
            this.live_info = live_info;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public List<String> getOnline_users() {
            return online_users;
        }

        public void setOnline_users(List<String> online_users) {
            this.online_users = online_users;
        }

        public static class LiveInfoBean {
            /**
             * id : 79
             * name : 第一单元  侵略和反抗
             * status : missed
             * board : 0
             * camera : 0
             * t : 1490259752
             */

            private int id;
            private String name;
            private String status;
            private int board;
            private int camera;
            private int t;

            public int getId() {
                return id;
            }

            public void setId(int id) {
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

            public int getBoard() {
                return board;
            }

            public void setBoard(int board) {
                this.board = board;
            }

            public int getCamera() {
                return camera;
            }

            public void setCamera(int camera) {
                this.camera = camera;
            }

            public int getT() {
                return t;
            }

            public void setT(int t) {
                this.t = t;
            }
        }
    }
}
