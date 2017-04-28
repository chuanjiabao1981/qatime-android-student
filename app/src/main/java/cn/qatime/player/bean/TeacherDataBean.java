package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tianhaoranly
 * @date 2016/11/1 9:30
 * @Description:
 */
public class TeacherDataBean implements Serializable {

    /**
     * status : 1
     * data : {"name":"王志成","desc":"请注意 ，这是开发人员的测试账号！","teaching_years":"more_than_twenty_years","gender":"male","grade":null,"subject":"化学","category":"小学","province":"山西省","city":"太原市","avatar_url":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ac7b10a5132677c95751935e8e8ffde3.jpg","school":"阳泉市外国语学校","courses":[{"id":61,"name":"再来一个辅导班","grade":"高二","price":"10.0","current_price":10,"subject":"化学","buy_tickets_count":4,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_b876ab65a61aaa1ae47597ef144b9f45.png","status":"published","lessons_count":1,"is_finished":false},{"id":104,"name":"再来一个辅导班","grade":"高二","price":"200.0","current_price":200,"subject":"化学","buy_tickets_count":0,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_6e3dc780684346cebb06b4b523473bcc.jpg","status":"teaching","lessons_count":1,"is_finished":false},{"id":109,"name":"刘刚测试直播客","grade":"高二","price":"999.0","current_price":249.75,"subject":"生物","buy_tickets_count":0,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"teaching","lessons_count":4,"is_finished":false},{"id":96,"name":"测试默认图片","grade":"二年级","price":"100.0","current_price":100,"subject":"化学","buy_tickets_count":0,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"teaching","lessons_count":1,"is_finished":false},{"id":107,"name":"第一个辅导班呀","grade":"高三","price":"200.0","current_price":100,"subject":"化学","buy_tickets_count":0,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"teaching","lessons_count":2,"is_finished":false},{"id":100,"name":"测试一下价格","grade":"高三","price":"100.0","current_price":50,"subject":"化学","buy_tickets_count":1,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"teaching","lessons_count":2,"is_finished":false},{"id":105,"name":"教师新版本","grade":"高一","price":"50.0","current_price":50,"subject":"化学","buy_tickets_count":1,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"teaching","lessons_count":6,"is_finished":false},{"id":27,"name":"钢琴辅导班","grade":"初二","price":"3000.0","current_price":0,"subject":"数学","buy_tickets_count":1,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"completed","lessons_count":10,"is_finished":true},{"id":42,"name":"测试辅导班1","grade":"高一","price":"1000.0","current_price":0,"subject":"数学","buy_tickets_count":0,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"completed","lessons_count":10,"is_finished":true},{"id":87,"name":"测试回放辅导班啦啦","grade":"初一","price":"2000.0","current_price":0,"subject":"化学","buy_tickets_count":1,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_b979f6856d77ad6c2ab025cbb1dc1526.jpg","status":"completed","lessons_count":4,"is_finished":true},{"id":28,"name":"钢琴辅导班","grade":"高二","price":"3000.0","current_price":0,"subject":"数学","buy_tickets_count":0,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"completed","lessons_count":10,"is_finished":true},{"id":72,"name":"再来一个辅导班2","grade":"初二","price":"300.0","current_price":0,"subject":"化学","buy_tickets_count":1,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_a83cd6248143bf0036cc30bfa1d82d35.png","status":"completed","lessons_count":3,"is_finished":true},{"id":88,"name":"test 04辛老师回放功能测试","grade":"高二","price":"200.0","current_price":0,"subject":"化学","buy_tickets_count":2,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_b94be0f3b4cca9b2363024cf5a40fe92.jpg","status":"completed","lessons_count":5,"is_finished":true},{"id":55,"name":"超级英语辅导","grade":"高一","price":"300.0","current_price":0,"subject":"英语","buy_tickets_count":1,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_1c30c5ae7b3051a8f08e612dedcb707f.jpg","status":"completed","lessons_count":3,"is_finished":true},{"id":79,"name":"又一个测试辅导班","grade":"高二","price":"100.0","current_price":0,"subject":"化学","buy_tickets_count":1,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_fcb3c13bce3c11dae8963b6abb19b8a0.png","status":"completed","lessons_count":1,"is_finished":true},{"id":70,"name":"再来一个辅导班","grade":"高二","price":"100.0","current_price":0,"subject":"化学","buy_tickets_count":5,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_5600a2b5061d74da67d5898004f4fa2e.jpg","status":"completed","lessons_count":3,"is_finished":true},{"id":86,"name":"哈哈哈辅导班","grade":"高二","price":"200.0","current_price":0,"subject":"化学","buy_tickets_count":4,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_704629152bd5cb90689a6e0a16aa8bfe.png","status":"completed","lessons_count":5,"is_finished":true},{"id":76,"name":"再来一个辅导班","grade":"高三","price":"100.0","current_price":0,"subject":"化学","buy_tickets_count":0,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_1919e283dcbaa3950c6674875b20c753.png","status":"completed","lessons_count":4,"is_finished":true},{"id":81,"name":"测试退款辅导班","grade":"高二","price":"150.0","current_price":0,"subject":"化学","buy_tickets_count":5,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_2bca2643441b5b6623c6a26aa7b2a727.png","status":"completed","lessons_count":3,"is_finished":true},{"id":93,"name":"再来一个辅导班","grade":"高三","price":"100.0","current_price":0,"subject":"化学","buy_tickets_count":12,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_8a7e08039eade62e5ea947c6a492683f.jpg","status":"completed","lessons_count":1,"is_finished":true},{"id":82,"name":"辛老师的测试专用直播课程 test01","grade":"一年级","price":"200.0","current_price":0,"subject":"化学","buy_tickets_count":2,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_db83575da432f815463b7902d07934de.jpg","status":"completed","lessons_count":6,"is_finished":true},{"id":106,"name":"教师测试新版本","grade":"高一","price":"50.0","current_price":0,"subject":"化学","buy_tickets_count":0,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"completed","lessons_count":3,"is_finished":true},{"id":31,"name":"购买辅导班测试","grade":"高一","price":"50.0","current_price":0,"subject":"物理","buy_tickets_count":2,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_c787fbba9ac792a8314930befd4a5e77.jpeg","status":"completed","lessons_count":2,"is_finished":true},{"id":71,"name":"测试辅导班1","grade":"高三","price":"100.0","current_price":0,"subject":"化学","buy_tickets_count":0,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_224c587df0ff7caa71fccf2327a66e91.png","status":"completed","lessons_count":2,"is_finished":true},{"id":84,"name":"辛老师回放功能测试 test 02","grade":"初二","price":"3000.0","current_price":0,"subject":"化学","buy_tickets_count":0,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_0dfadb975d4ce133039baea8acdcd0bd.png","status":"completed","lessons_count":3,"is_finished":true},{"id":89,"name":"JINGJING BUY BUY BUY","grade":"高二","price":"2997.0","current_price":0,"subject":"化学","buy_tickets_count":4,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_226712be41e242423f878df3ea549519.jpg","status":"completed","lessons_count":2,"is_finished":true},{"id":34,"name":"初中语文辅导班","grade":"初二","price":"123.0","current_price":13.67,"subject":"语文","buy_tickets_count":2,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"completed","lessons_count":9,"is_finished":true},{"id":45,"name":"测试状态","grade":"高一","price":"1111.0","current_price":416.64,"subject":"数学","buy_tickets_count":3,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"completed","lessons_count":16,"is_finished":true},{"id":80,"name":"今天直播一下换个心情","grade":"高二","price":"1000.0","current_price":0,"subject":"化学","buy_tickets_count":0,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_c134b48ba83739dff475857501142b6e.png","status":"completed","lessons_count":5,"is_finished":true}],"interactive_courses":[],"video_courses":[{"id":3,"name":"初中考试1","grade":"初一","price":"300.0","current_price":"300.0","subject":"化学","buy_tickets_count":2,"publicize":"/assets/video_courses/list_default-d30bb0d64e2d9ea1d59d5214f7b4613a.png","status":"published","lessons_count":2},{"id":7,"name":"试听课11","grade":"高一","price":"333.0","current_price":"333.0","subject":"化学","buy_tickets_count":0,"publicize":"/assets/video_courses/list_default-d30bb0d64e2d9ea1d59d5214f7b4613a.png","status":"published","lessons_count":3},{"id":2,"name":"刘刚测试视频课2","grade":"高二","price":"300.0","current_price":"300.0","subject":"化学","buy_tickets_count":1,"publicize":"/assets/video_courses/list_default-d30bb0d64e2d9ea1d59d5214f7b4613a.png","status":"published","lessons_count":2},{"id":5,"name":"高二物理视频课","grade":"高二","price":"300.0","current_price":"300.0","subject":"化学","buy_tickets_count":2,"publicize":"/assets/video_courses/list_default-d30bb0d64e2d9ea1d59d5214f7b4613a.png","status":"published","lessons_count":1}],"icons":{"course_can_refund":true,"info_complete":true,"teach_online":true}}
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
         * name : 王志成
         * desc : 请注意 ，这是开发人员的测试账号！
         * teaching_years : more_than_twenty_years
         * gender : male
         * grade : null
         * subject : 化学
         * category : 小学
         * province : 山西省
         * city : 太原市
         * avatar_url : http://qatime-testing.oss-cn-beijing.aliyuncs.com/avatars/ac7b10a5132677c95751935e8e8ffde3.jpg
         * school : 阳泉市外国语学校
         * courses : [{"id":61,"name":"再来一个辅导班","grade":"高二","price":"10.0","current_price":10,"subject":"化学","buy_tickets_count":4,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_b876ab65a61aaa1ae47597ef144b9f45.png","status":"published","lessons_count":1,"is_finished":false},{"id":104,"name":"再来一个辅导班","grade":"高二","price":"200.0","current_price":200,"subject":"化学","buy_tickets_count":0,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_6e3dc780684346cebb06b4b523473bcc.jpg","status":"teaching","lessons_count":1,"is_finished":false},{"id":109,"name":"刘刚测试直播客","grade":"高二","price":"999.0","current_price":249.75,"subject":"生物","buy_tickets_count":0,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"teaching","lessons_count":4,"is_finished":false},{"id":96,"name":"测试默认图片","grade":"二年级","price":"100.0","current_price":100,"subject":"化学","buy_tickets_count":0,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"teaching","lessons_count":1,"is_finished":false},{"id":107,"name":"第一个辅导班呀","grade":"高三","price":"200.0","current_price":100,"subject":"化学","buy_tickets_count":0,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"teaching","lessons_count":2,"is_finished":false},{"id":100,"name":"测试一下价格","grade":"高三","price":"100.0","current_price":50,"subject":"化学","buy_tickets_count":1,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"teaching","lessons_count":2,"is_finished":false},{"id":105,"name":"教师新版本","grade":"高一","price":"50.0","current_price":50,"subject":"化学","buy_tickets_count":1,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"teaching","lessons_count":6,"is_finished":false},{"id":27,"name":"钢琴辅导班","grade":"初二","price":"3000.0","current_price":0,"subject":"数学","buy_tickets_count":1,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"completed","lessons_count":10,"is_finished":true},{"id":42,"name":"测试辅导班1","grade":"高一","price":"1000.0","current_price":0,"subject":"数学","buy_tickets_count":0,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"completed","lessons_count":10,"is_finished":true},{"id":87,"name":"测试回放辅导班啦啦","grade":"初一","price":"2000.0","current_price":0,"subject":"化学","buy_tickets_count":1,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_b979f6856d77ad6c2ab025cbb1dc1526.jpg","status":"completed","lessons_count":4,"is_finished":true},{"id":28,"name":"钢琴辅导班","grade":"高二","price":"3000.0","current_price":0,"subject":"数学","buy_tickets_count":0,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"completed","lessons_count":10,"is_finished":true},{"id":72,"name":"再来一个辅导班2","grade":"初二","price":"300.0","current_price":0,"subject":"化学","buy_tickets_count":1,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_a83cd6248143bf0036cc30bfa1d82d35.png","status":"completed","lessons_count":3,"is_finished":true},{"id":88,"name":"test 04辛老师回放功能测试","grade":"高二","price":"200.0","current_price":0,"subject":"化学","buy_tickets_count":2,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_b94be0f3b4cca9b2363024cf5a40fe92.jpg","status":"completed","lessons_count":5,"is_finished":true},{"id":55,"name":"超级英语辅导","grade":"高一","price":"300.0","current_price":0,"subject":"英语","buy_tickets_count":1,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_1c30c5ae7b3051a8f08e612dedcb707f.jpg","status":"completed","lessons_count":3,"is_finished":true},{"id":79,"name":"又一个测试辅导班","grade":"高二","price":"100.0","current_price":0,"subject":"化学","buy_tickets_count":1,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_fcb3c13bce3c11dae8963b6abb19b8a0.png","status":"completed","lessons_count":1,"is_finished":true},{"id":70,"name":"再来一个辅导班","grade":"高二","price":"100.0","current_price":0,"subject":"化学","buy_tickets_count":5,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_5600a2b5061d74da67d5898004f4fa2e.jpg","status":"completed","lessons_count":3,"is_finished":true},{"id":86,"name":"哈哈哈辅导班","grade":"高二","price":"200.0","current_price":0,"subject":"化学","buy_tickets_count":4,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_704629152bd5cb90689a6e0a16aa8bfe.png","status":"completed","lessons_count":5,"is_finished":true},{"id":76,"name":"再来一个辅导班","grade":"高三","price":"100.0","current_price":0,"subject":"化学","buy_tickets_count":0,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_1919e283dcbaa3950c6674875b20c753.png","status":"completed","lessons_count":4,"is_finished":true},{"id":81,"name":"测试退款辅导班","grade":"高二","price":"150.0","current_price":0,"subject":"化学","buy_tickets_count":5,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_2bca2643441b5b6623c6a26aa7b2a727.png","status":"completed","lessons_count":3,"is_finished":true},{"id":93,"name":"再来一个辅导班","grade":"高三","price":"100.0","current_price":0,"subject":"化学","buy_tickets_count":12,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_8a7e08039eade62e5ea947c6a492683f.jpg","status":"completed","lessons_count":1,"is_finished":true},{"id":82,"name":"辛老师的测试专用直播课程 test01","grade":"一年级","price":"200.0","current_price":0,"subject":"化学","buy_tickets_count":2,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_db83575da432f815463b7902d07934de.jpg","status":"completed","lessons_count":6,"is_finished":true},{"id":106,"name":"教师测试新版本","grade":"高一","price":"50.0","current_price":0,"subject":"化学","buy_tickets_count":0,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"completed","lessons_count":3,"is_finished":true},{"id":31,"name":"购买辅导班测试","grade":"高一","price":"50.0","current_price":0,"subject":"物理","buy_tickets_count":2,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_c787fbba9ac792a8314930befd4a5e77.jpeg","status":"completed","lessons_count":2,"is_finished":true},{"id":71,"name":"测试辅导班1","grade":"高三","price":"100.0","current_price":0,"subject":"化学","buy_tickets_count":0,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_224c587df0ff7caa71fccf2327a66e91.png","status":"completed","lessons_count":2,"is_finished":true},{"id":84,"name":"辛老师回放功能测试 test 02","grade":"初二","price":"3000.0","current_price":0,"subject":"化学","buy_tickets_count":0,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_0dfadb975d4ce133039baea8acdcd0bd.png","status":"completed","lessons_count":3,"is_finished":true},{"id":89,"name":"JINGJING BUY BUY BUY","grade":"高二","price":"2997.0","current_price":0,"subject":"化学","buy_tickets_count":4,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_226712be41e242423f878df3ea549519.jpg","status":"completed","lessons_count":2,"is_finished":true},{"id":34,"name":"初中语文辅导班","grade":"初二","price":"123.0","current_price":13.67,"subject":"语文","buy_tickets_count":2,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"completed","lessons_count":9,"is_finished":true},{"id":45,"name":"测试状态","grade":"高一","price":"1111.0","current_price":416.64,"subject":"数学","buy_tickets_count":3,"publicize":"/assets/courses/list_default-74744b9444c91d1dae4b3aa1c8f1682b.png","status":"completed","lessons_count":16,"is_finished":true},{"id":80,"name":"今天直播一下换个心情","grade":"高二","price":"1000.0","current_price":0,"subject":"化学","buy_tickets_count":0,"publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_c134b48ba83739dff475857501142b6e.png","status":"completed","lessons_count":5,"is_finished":true}]
         * interactive_courses : []
         * video_courses : [{"id":3,"name":"初中考试1","grade":"初一","price":"300.0","current_price":"300.0","subject":"化学","buy_tickets_count":2,"publicize":"/assets/video_courses/list_default-d30bb0d64e2d9ea1d59d5214f7b4613a.png","status":"published","lessons_count":2},{"id":7,"name":"试听课11","grade":"高一","price":"333.0","current_price":"333.0","subject":"化学","buy_tickets_count":0,"publicize":"/assets/video_courses/list_default-d30bb0d64e2d9ea1d59d5214f7b4613a.png","status":"published","lessons_count":3},{"id":2,"name":"刘刚测试视频课2","grade":"高二","price":"300.0","current_price":"300.0","subject":"化学","buy_tickets_count":1,"publicize":"/assets/video_courses/list_default-d30bb0d64e2d9ea1d59d5214f7b4613a.png","status":"published","lessons_count":2},{"id":5,"name":"高二物理视频课","grade":"高二","price":"300.0","current_price":"300.0","subject":"化学","buy_tickets_count":2,"publicize":"/assets/video_courses/list_default-d30bb0d64e2d9ea1d59d5214f7b4613a.png","status":"published","lessons_count":1}]
         * icons : {"course_can_refund":true,"info_complete":true,"teach_online":true}
         */

        private String name;
        private String desc;
        private String teaching_years;
        private String gender;
        private Object grade;
        private String subject;
        private String category;
        private String province;
        private String city;
        private String avatar_url;
        private String school;
        private IconsBean icons;
        private List<Course> courses;
        private List<InteractiveCourses> interactive_courses;
        private List<VideoCoursesBean> video_courses;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getTeaching_years() {
            return teaching_years;
        }

        public void setTeaching_years(String teaching_years) {
            this.teaching_years = teaching_years;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Object getGrade() {
            return grade;
        }

        public void setGrade(Object grade) {
            this.grade = grade;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public IconsBean getIcons() {
            return icons;
        }

        public void setIcons(IconsBean icons) {
            this.icons = icons;
        }

        public List<Course> getCourses() {
            return courses;
        }

        public void setCourses(List<Course> courses) {
            this.courses = courses;
        }

        public List<InteractiveCourses> getInteractive_courses() {
            return interactive_courses;
        }

        public void setInteractive_courses(List<InteractiveCourses> interactive_courses) {
            this.interactive_courses = interactive_courses;
        }

        public List<VideoCoursesBean> getVideo_courses() {
            return video_courses;
        }

        public void setVideo_courses(List<VideoCoursesBean> video_courses) {
            this.video_courses = video_courses;
        }

        public static class IconsBean {
            /**
             * course_can_refund : true
             * info_complete : true
             * teach_online : true
             */

            private boolean course_can_refund;
            private boolean info_complete;
            private boolean teach_online;

            public boolean isCourse_can_refund() {
                return course_can_refund;
            }

            public void setCourse_can_refund(boolean course_can_refund) {
                this.course_can_refund = course_can_refund;
            }

            public boolean isInfo_complete() {
                return info_complete;
            }

            public void setInfo_complete(boolean info_complete) {
                this.info_complete = info_complete;
            }

            public boolean isTeach_online() {
                return teach_online;
            }

            public void setTeach_online(boolean teach_online) {
                this.teach_online = teach_online;
            }
        }

        public static class Course {
            /**
             * id : 61
             * name : 再来一个辅导班
             * grade : 高二
             * price : 10.0
             * current_price : 10
             * subject : 化学
             * buy_tickets_count : 4
             * publicize : http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_b876ab65a61aaa1ae47597ef144b9f45.png
             * status : published
             * lessons_count : 1
             * is_finished : false
             */

            private int id;
            private String name;
            private String grade;
            private String price;
            private double current_price;
            private String subject;
            private int buy_tickets_count;
            private String publicize;
            private String status;
            private int lessons_count;
            private boolean is_finished;

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

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public double getCurrent_price() {
                return current_price;
            }

            public void setCurrent_price(double current_price) {
                this.current_price = current_price;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public int getBuy_tickets_count() {
                return buy_tickets_count;
            }

            public void setBuy_tickets_count(int buy_tickets_count) {
                this.buy_tickets_count = buy_tickets_count;
            }

            public String getPublicize() {
                return publicize;
            }

            public void setPublicize(String publicize) {
                this.publicize = publicize;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getLessons_count() {
                return lessons_count;
            }

            public void setLessons_count(int lessons_count) {
                this.lessons_count = lessons_count;
            }

            public boolean isIs_finished() {
                return is_finished;
            }

            public void setIs_finished(boolean is_finished) {
                this.is_finished = is_finished;
            }
        }

        public static class VideoCoursesBean {
            /**
             * id : 3
             * name : 初中考试1
             * grade : 初一
             * price : 300.0
             * current_price : 300.0
             * subject : 化学
             * buy_tickets_count : 2
             * publicize : /assets/video_courses/list_default-d30bb0d64e2d9ea1d59d5214f7b4613a.png
             * status : published
             * lessons_count : 2
             */

            private int id;
            private String name;
            private String grade;
            private String price;
            private String current_price;
            private String subject;
            private int buy_tickets_count;
            private String publicize;
            private String status;
            private int lessons_count;

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

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getCurrent_price() {
                return current_price;
            }

            public void setCurrent_price(String current_price) {
                this.current_price = current_price;
            }

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public int getBuy_tickets_count() {
                return buy_tickets_count;
            }

            public void setBuy_tickets_count(int buy_tickets_count) {
                this.buy_tickets_count = buy_tickets_count;
            }

            public String getPublicize() {
                return publicize;
            }

            public void setPublicize(String publicize) {
                this.publicize = publicize;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getLessons_count() {
                return lessons_count;
            }

            public void setLessons_count(int lessons_count) {
                this.lessons_count = lessons_count;
            }
        }

        public static class InteractiveCourses {

            /**
             * id : 5
             * name : 购买以下
             * subject : 化学
             * grade : 初二
             * price : 200.0
             * current_price : 200
             * publicize : /assets/interactive_courses/list_default-214e337ab4433dba9eb86dc0c4cbbee4.png
             * publicize_info_url : /assets/interactive_courses/info_default-2696b4c40a5ea2383bbc733e323ab735.png
             * status : teaching
             * lessons_count : 10
             * is_finished : false
             */

            private int id;
            private String name;
            private String subject;
            private String grade;
            private String price;
            private int current_price;
            private String publicize;
            private String publicize_info_url;
            private String status;
            private int lessons_count;
            private boolean is_finished;

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

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public int getCurrent_price() {
                return current_price;
            }

            public void setCurrent_price(int current_price) {
                this.current_price = current_price;
            }

            public String getPublicize() {
                return publicize;
            }

            public void setPublicize(String publicize) {
                this.publicize = publicize;
            }

            public String getPublicize_info_url() {
                return publicize_info_url;
            }

            public void setPublicize_info_url(String publicize_info_url) {
                this.publicize_info_url = publicize_info_url;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getLessons_count() {
                return lessons_count;
            }

            public void setLessons_count(int lessons_count) {
                this.lessons_count = lessons_count;
            }

            public boolean isIs_finished() {
                return is_finished;
            }

            public void setIs_finished(boolean is_finished) {
                this.is_finished = is_finished;
            }
        }
    }
}
