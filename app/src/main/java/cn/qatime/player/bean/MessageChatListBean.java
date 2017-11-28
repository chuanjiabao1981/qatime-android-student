package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author luntify
 * @date 2017/11/28 15:56
 * @Description: 聊天列表页面后台数据
 */

public class MessageChatListBean implements Serializable {

    /**
     * status : 1
     * data : [{"team_id":"85754484","discussable_id":6,"discussable_type":"LiveStudio::Group","discussable_name":"专属课之打秀海1","discussable_publicize":{"url":"https://testing.qatime.cn/groups/biology/default.png","list":{"url":"https://testing.qatime.cn/assets/groups/biology/list_default-9785d813c105d58a591acd28cefb825d.png"}}}]
     */

    private int status;
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
        /**
         * team_id : 85754484
         * discussable_id : 6
         * discussable_type : LiveStudio::Group
         * discussable_name : 专属课之打秀海1
         * discussable_publicize : {"url":"https://testing.qatime.cn/groups/biology/default.png","list":{"url":"https://testing.qatime.cn/assets/groups/biology/list_default-9785d813c105d58a591acd28cefb825d.png"}}
         */

        private String team_id;
        private int discussable_id;
        private String discussable_type;
        private String discussable_name;
        private DiscussablePublicizeBean discussable_publicize;

        public String getTeam_id() {
            return team_id;
        }

        public void setTeam_id(String team_id) {
            this.team_id = team_id;
        }

        public int getDiscussable_id() {
            return discussable_id;
        }

        public void setDiscussable_id(int discussable_id) {
            this.discussable_id = discussable_id;
        }

        public String getDiscussable_type() {
            return discussable_type;
        }

        public void setDiscussable_type(String discussable_type) {
            this.discussable_type = discussable_type;
        }

        public String getDiscussable_name() {
            return discussable_name;
        }

        public void setDiscussable_name(String discussable_name) {
            this.discussable_name = discussable_name;
        }

        public DiscussablePublicizeBean getDiscussable_publicize() {
            return discussable_publicize;
        }

        public void setDiscussable_publicize(DiscussablePublicizeBean discussable_publicize) {
            this.discussable_publicize = discussable_publicize;
        }

        public static class DiscussablePublicizeBean {
            /**
             * url : https://testing.qatime.cn/groups/biology/default.png
             * list : {"url":"https://testing.qatime.cn/assets/groups/biology/list_default-9785d813c105d58a591acd28cefb825d.png"}
             */

            private String url;
            private ListBean list;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public ListBean getList() {
                return list;
            }

            public void setList(ListBean list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * url : https://testing.qatime.cn/assets/groups/biology/list_default-9785d813c105d58a591acd28cefb825d.png
                 */

                private String url;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }
    }
}
