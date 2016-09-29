package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lungtify on 2016/9/5.
 */
public class Announcements implements Serializable {

    /**
     * status : 1
     * data : {"announcements":[{"announcement":"","edit_at":"2016-08-17T18:16:48.037+08:00"}],"members":[{"accid":"ae7be24ed629ee425362f935d8ff453d","name":"luke","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_8ad1d4c9ee1c4f4c9ebca72be9048988.jpg"},{"accid":"e472bffc63e2465eb84faed4f13ffea4","name":"王","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_cc49397e324d0753e62851b3e13954c9.png"},{"accid":"f9a02a05e1142b600d7faabd6bd3b08e","name":"赵冠晨","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_d347e9ca18587baf871f1bc72962da08.png"},{"accid":"b26a46116264a14bf092933fd03c4a6a","name":"luke","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_427014b55d6b32a836627f3a096c5c08.jpg"},{"accid":"e28e93116af4d8f39c61e0baa3244f5d","name":"app端","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_fcc03c3e74aa137392f28d429aab386d.jpg"}],"current_lesson_status":"ready"}
     */

    private int status;
    /**
     * announcements : [{"announcement":"","edit_at":"2016-08-17T18:16:48.037+08:00"}]
     * members : [{"accid":"ae7be24ed629ee425362f935d8ff453d","name":"luke","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_8ad1d4c9ee1c4f4c9ebca72be9048988.jpg"},{"accid":"e472bffc63e2465eb84faed4f13ffea4","name":"王","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_cc49397e324d0753e62851b3e13954c9.png"},{"accid":"f9a02a05e1142b600d7faabd6bd3b08e","name":"赵冠晨","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_d347e9ca18587baf871f1bc72962da08.png"},{"accid":"b26a46116264a14bf092933fd03c4a6a","name":"luke","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_427014b55d6b32a836627f3a096c5c08.jpg"},{"accid":"e28e93116af4d8f39c61e0baa3244f5d","name":"app端","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_fcc03c3e74aa137392f28d429aab386d.jpg"}]
     * current_lesson_status : ready
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

    public static class DataBean implements Serializable {
        private String current_lesson_status;
        /**
         * announcement :
         * edit_at : 2016-08-17T18:16:48.037+08:00
         */

        private List<AnnouncementsBean> announcements;
        /**
         * accid : ae7be24ed629ee425362f935d8ff453d
         * name : luke
         * icon : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_8ad1d4c9ee1c4f4c9ebca72be9048988.jpg
         */

        private List<MembersBean> members;

        public String getCurrent_lesson_status() {
            return current_lesson_status;
        }

        public void setCurrent_lesson_status(String current_lesson_status) {
            this.current_lesson_status = current_lesson_status;
        }

        public List<AnnouncementsBean> getAnnouncements() {
            return announcements;
        }

        public void setAnnouncements(List<AnnouncementsBean> announcements) {
            this.announcements = announcements;
        }

        public List<MembersBean> getMembers() {
            return members;
        }

        public void setMembers(List<MembersBean> members) {
            this.members = members;
        }

        public static class AnnouncementsBean implements Serializable {
            private String announcement;
            private String edit_at;

            public String getAnnouncement() {
                return announcement;
            }

            public void setAnnouncement(String announcement) {
                this.announcement = announcement;
            }

            public String getEdit_at() {
                return edit_at;
            }

            public void setEdit_at(String edit_at) {
                this.edit_at = edit_at;
            }
        }

        public static class MembersBean implements Serializable {
            private String accid;
            private String name;
            private String icon;
            private String firstLetter;

            public String getAccid() {
                return accid;
            }

            public void setAccid(String accid) {
                this.accid = accid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public void setFirstLetter(String firstLetter) {
                this.firstLetter = firstLetter;
            }

            public String getFirstLetter() {
                return firstLetter;
            }
        }
    }
}
