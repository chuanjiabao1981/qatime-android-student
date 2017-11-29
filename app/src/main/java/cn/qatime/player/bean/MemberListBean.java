package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author luntify
 * @date 2017/11/29 17:01
 * @Description:
 */

public class MemberListBean implements Serializable {

    /**
     * status : 1
     * data : {"teachers":[{"name":"姜海全","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/2a48fe0bfa8ba831fc958c7fae2d371e.jpg"}],"members":[{"student_name":"王晓龙","student_avatar":{"url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/318714b08938204dd11a57172da90218.jpg"},"updated_at":"2017-10-26T16:45:15.268+08:00","created_at":"2017-10-26T16:45:15.268+08:00"}]}
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
        private List<TeachersBean> teachers;
        private List<MembersBean> members;

        public List<TeachersBean> getTeachers() {
            return teachers;
        }

        public void setTeachers(List<TeachersBean> teachers) {
            this.teachers = teachers;
        }

        public List<MembersBean> getMembers() {
            return members;
        }

        public void setMembers(List<MembersBean> members) {
            this.members = members;
        }

        public static class TeachersBean {
            /**
             * name : 姜海全
             * avatar_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/2a48fe0bfa8ba831fc958c7fae2d371e.jpg
             */

            private String name;
            private String avatar_url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }
        }

        public static class MembersBean {
            /**
             * student_name : 王晓龙
             * student_avatar : {"url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/318714b08938204dd11a57172da90218.jpg"}
             * updated_at : 2017-10-26T16:45:15.268+08:00
             * created_at : 2017-10-26T16:45:15.268+08:00
             */

            private String student_name;
            private StudentAvatarBean student_avatar;
            private String updated_at;
            private String created_at;
            private boolean owner = false;
            private String firstLetters;

            public String getStudent_name() {
                return student_name;
            }

            public void setStudent_name(String student_name) {
                this.student_name = student_name;
            }

            public StudentAvatarBean getStudent_avatar() {
                return student_avatar;
            }

            public void setStudent_avatar(StudentAvatarBean student_avatar) {
                this.student_avatar = student_avatar;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public boolean isOwner() {
                return owner;
            }

            public void setOwner(boolean owner) {
                this.owner = owner;
            }

            public void setFirstLetters(String firstLetters) {
                this.firstLetters = firstLetters;
            }

            public String getFirstLetters() {
                return firstLetters;
            }

            public static class StudentAvatarBean {
                /**
                 * url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/318714b08938204dd11a57172da90218.jpg
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
