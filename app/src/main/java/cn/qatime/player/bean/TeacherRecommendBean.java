package cn.qatime.player.bean;

import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2016/11/1 15:49
 * @Description:
 */
public class TeacherRecommendBean {
    /**
     * status : 1
     * data : [{"title":"赵鲁兵推荐","index":1,"type":"Recommend::TeacherItem","teacher":{"id":2845,"name":"赵鲁兵","nick_name":"白杨_","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/4833b3f674d318ef2c28c0ba8d17bdbc.jpg"}},{"title":"韩志慧推荐","index":2,"type":"Recommend::TeacherItem","teacher":{"id":2848,"name":"韩志慧","nick_name":"MISS韩","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/e2caa7ec1470cac04d3dd8d431f3fdec.jpg"}},{"title":"推荐","index":3,"type":"Recommend::TeacherItem","teacher":{"id":2859,"name":null,"nick_name":null,"avatar_url":null}},{"title":"赵雪琴推荐","index":4,"type":"Recommend::TeacherItem","teacher":{"id":2861,"name":"赵雪琴","nick_name":"林海雪原","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/034422515356a5f960d3a41ce12662e4.jpg"}},{"title":"潘馨推荐","index":5,"type":"Recommend::TeacherItem","teacher":{"id":2864,"name":"潘馨","nick_name":"","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/8ce47a600c1c8c8e5b2033cc2c91136c.jpg"}}]
     */

    private int status;
    /**
     * title : 赵鲁兵推荐
     * index : 1
     * type : Recommend::TeacherItem
     * teacher : {"id":2845,"name":"赵鲁兵","nick_name":"白杨_","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/4833b3f674d318ef2c28c0ba8d17bdbc.jpg"}
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
        private String title;
        private int index;
        private String type;
        /**
         * id : 2845
         * name : 赵鲁兵
         * nick_name : 白杨_
         * avatar_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/4833b3f674d318ef2c28c0ba8d17bdbc.jpg
         */

        private TeacherBean teacher;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public TeacherBean getTeacher() {
            return teacher;
        }

        public void setTeacher(TeacherBean teacher) {
            this.teacher = teacher;
        }

        public static class TeacherBean {
            private int id;
            private String name;
            private String nick_name;
            private String avatar_url;

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

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }
        }
    }
}
