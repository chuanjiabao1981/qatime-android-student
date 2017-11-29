package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2017/11/28 17:56
 * @Description:
 */

public class MembersBean implements Serializable {

    /**
     * status : 1
     * data : {"members":[{"accid":"f443180ab1b20afa3f3cc510b78d0776","name":"王晓龙","icon":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_318714b08938204dd11a57172da90218.jpg","role":"normal"}]}
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
        private List<Members> members;

        public List<Members> getMembers() {
            return members;
        }

        public void setMembers(List<Members> members) {
            this.members = members;
        }

        public static class Members {
            /**
             * accid : f443180ab1b20afa3f3cc510b78d0776
             * name : 王晓龙
             * icon : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/small_318714b08938204dd11a57172da90218.jpg
             * role : normal
             */

            private String accid;
            private String name;
            private String icon;
            private String role;
            private String firstLetters;

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

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
            }

            public void setFirstLetters(String firstLetters) {
                this.firstLetters = firstLetters;
            }

            public String getFirstLetters() {
                return firstLetters;
            }

            public boolean isOwner() {
                return !StringUtils.isNullOrBlanK(role) && !role.equals("normal");
            }
        }
    }
}
