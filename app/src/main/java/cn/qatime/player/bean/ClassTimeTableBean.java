package cn.qatime.player.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author luntify
 * @date 2016/8/26 16:23
 * @Description
 */
public class ClassTimeTableBean implements Serializable {


    /**
     * status : 1
     * data : [{"date":"2016-08-08","lessons":[{"id":15,"name":"第一节","status":"finished","class_date":"2016-08-08","live_time":"6:30~8:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":15,"name":"第一节","status":"finished","class_date":"2016-08-08","live_time":"6:30~8:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":16,"name":"第二节","status":"finished","class_date":"2016-08-08","live_time":"21:00~22:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":16,"name":"第二节","status":"finished","class_date":"2016-08-08","live_time":"21:00~22:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]},{"date":"2016-08-09","lessons":[{"id":17,"name":"第三节","status":"finished","class_date":"2016-08-09","live_time":"10:00~12:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":17,"name":"第三节","status":"finished","class_date":"2016-08-09","live_time":"10:00~12:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":18,"name":"第四节","status":"finished","class_date":"2016-08-09","live_time":"14:00~16:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":18,"name":"第四节","status":"finished","class_date":"2016-08-09","live_time":"14:00~16:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]},{"date":"2016-08-10","lessons":[{"id":19,"name":"第五节","status":"ready","class_date":"2016-08-10","live_time":"10:00~12:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":19,"name":"第五节","status":"ready","class_date":"2016-08-10","live_time":"10:00~12:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":20,"name":"第六节","status":"ready","class_date":"2016-08-10","live_time":"14:00~16:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":20,"name":"第六节","status":"ready","class_date":"2016-08-10","live_time":"14:00~16:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]},{"date":"2016-08-11","lessons":[{"id":21,"name":"第七节","status":"ready","class_date":"2016-08-11","live_time":"8:00~10:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":21,"name":"第七节","status":"ready","class_date":"2016-08-11","live_time":"8:00~10:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":22,"name":"第八节","status":"ready","class_date":"2016-08-11","live_time":"16:00~18:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":22,"name":"第八节","status":"ready","class_date":"2016-08-11","live_time":"16:00~18:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]},{"date":"2016-08-12","lessons":[{"id":23,"name":"第九节","status":"ready","class_date":"2016-08-12","live_time":"10:00~12:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":23,"name":"第九节","status":"ready","class_date":"2016-08-12","live_time":"10:00~12:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":24,"name":"第十节","status":"ready","class_date":"2016-08-12","live_time":"14:00~18:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":24,"name":"第十节","status":"ready","class_date":"2016-08-12","live_time":"14:00~18:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":52,"name":"adfasdfasdfasdfsad","status":"init","class_date":"2016-08-12","live_time":"0:30~2:00","course_name":"高考冲刺班","course_publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_f335bd3eb0a2bec09aaab740668a3248.png","subject":"生物","pull_address":"","teacher_name":"luke"}]},{"date":"2016-08-13","lessons":[{"id":25,"name":"第十一节","status":"ready","class_date":"2016-08-13","live_time":"8:00~12:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":25,"name":"第十一节","status":"ready","class_date":"2016-08-13","live_time":"8:00~12:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":26,"name":"第十二节","status":"ready","class_date":"2016-08-13","live_time":"16:00~20:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":26,"name":"第十二节","status":"ready","class_date":"2016-08-13","live_time":"16:00~20:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":50,"name":"第三课 氕氘氚和卤气","status":"ready","class_date":"2016-08-13","live_time":"1:00~6:30","course_name":"初二化学重点、难点、易错点、考点综合讲解辅导班","course_publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_05db4f303a24d0180d173eb31e2e5051.jpeg","subject":"化学","pull_address":"","teacher_name":"luke"},{"id":50,"name":"第三课 氕氘氚和卤气","status":"ready","class_date":"2016-08-13","live_time":"1:00~6:30","course_name":"初二化学重点、难点、易错点、考点综合讲解辅导班","course_publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_05db4f303a24d0180d173eb31e2e5051.jpeg","subject":"化学","pull_address":"","teacher_name":"luke"},{"id":51,"name":"dadfasdfafsdafssdasdsdf","status":"ready","class_date":"2016-08-13","live_time":"2:00~3:30","course_name":"高考冲刺班","course_publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_bcbaf42e2cd72e59ee8378fd504dbcb5.png","subject":"物理","pull_address":"","teacher_name":"luke"},{"id":53,"name":"adsfdsfasdf","status":"ready","class_date":"2016-08-13","live_time":"1:00~2:30","course_name":"高考冲刺班","course_publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_b949d1f8c297404d2fd4338955df8583.png","subject":"物理","pull_address":"","teacher_name":"luke"}]},{"date":"2016-08-14","lessons":[{"id":27,"name":"第十三节","status":"ready","class_date":"2016-08-14","live_time":"8:00~10:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":27,"name":"第十三节","status":"ready","class_date":"2016-08-14","live_time":"8:00~10:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":28,"name":"第十四节","status":"ready","class_date":"2016-08-14","live_time":"16:00~21:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":28,"name":"第十四节","status":"ready","class_date":"2016-08-14","live_time":"16:00~21:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]},{"date":"2016-08-15","lessons":[{"id":29,"name":"第十五节","status":"ready","class_date":"2016-08-15","live_time":"9:00~12:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":29,"name":"第十五节","status":"ready","class_date":"2016-08-15","live_time":"9:00~12:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":30,"name":"第十六节","status":"ready","class_date":"2016-08-15","live_time":"14:00~18:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":30,"name":"第十六节","status":"ready","class_date":"2016-08-15","live_time":"14:00~18:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]},{"date":"2016-08-16","lessons":[{"id":31,"name":"第十七节","status":"ready","class_date":"2016-08-16","live_time":"8:00~10:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":31,"name":"第十七节","status":"ready","class_date":"2016-08-16","live_time":"8:00~10:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":32,"name":"第十八节","status":"ready","class_date":"2016-08-16","live_time":"14:00~18:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":32,"name":"第十八节","status":"ready","class_date":"2016-08-16","live_time":"14:00~18:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]},{"date":"2016-08-17","lessons":[{"id":33,"name":"第十九节","status":"ready","class_date":"2016-08-17","live_time":"10:00~12:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":33,"name":"第十九节","status":"ready","class_date":"2016-08-17","live_time":"10:00~12:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":34,"name":"第二十节","status":"ready","class_date":"2016-08-17","live_time":"10:00~14:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":34,"name":"第二十节","status":"ready","class_date":"2016-08-17","live_time":"10:00~14:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]},{"date":"2016-08-18","lessons":[{"id":35,"name":"第二十一节","status":"ready","class_date":"2016-08-18","live_time":"9:00~11:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":35,"name":"第二十一节","status":"ready","class_date":"2016-08-18","live_time":"9:00~11:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":36,"name":"第二十二节","status":"ready","class_date":"2016-08-18","live_time":"14:00~18:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":36,"name":"第二十二节","status":"ready","class_date":"2016-08-18","live_time":"14:00~18:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]},{"date":"2016-08-19","lessons":[{"id":37,"name":"第二十三节","status":"ready","class_date":"2016-08-19","live_time":"8:00~10:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":37,"name":"第二十三节","status":"ready","class_date":"2016-08-19","live_time":"8:00~10:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":38,"name":"第二十四节","status":"ready","class_date":"2016-08-19","live_time":"14:00~15:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":38,"name":"第二十四节","status":"ready","class_date":"2016-08-19","live_time":"14:00~15:30","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]},{"date":"2016-08-20","lessons":[{"id":39,"name":"第二十五节","status":"ready","class_date":"2016-08-20","live_time":"9:30~11:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":39,"name":"第二十五节","status":"ready","class_date":"2016-08-20","live_time":"9:30~11:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":40,"name":"第二十六节","status":"ready","class_date":"2016-08-20","live_time":"15:00~17:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":40,"name":"第二十六节","status":"ready","class_date":"2016-08-20","live_time":"15:00~17:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]},{"date":"2016-08-21","lessons":[{"id":41,"name":"第二十七节","status":"ready","class_date":"2016-08-21","live_time":"9:30~11:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":41,"name":"第二十七节","status":"ready","class_date":"2016-08-21","live_time":"9:30~11:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":42,"name":"第二十八节","status":"ready","class_date":"2016-08-21","live_time":"14:30~21:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":42,"name":"第二十八节","status":"ready","class_date":"2016-08-21","live_time":"14:30~21:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]},{"date":"2016-08-22","lessons":[{"id":43,"name":"第二十九节","status":"ready","class_date":"2016-08-22","live_time":"10:00~12:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":43,"name":"第二十九节","status":"ready","class_date":"2016-08-22","live_time":"10:00~12:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":44,"name":"第三十节","status":"ready","class_date":"2016-08-22","live_time":"14:00~19:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":44,"name":"第三十节","status":"ready","class_date":"2016-08-22","live_time":"14:00~19:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]},{"date":"2016-08-01","lessons":[{"id":49,"name":"第二课 铁钙镁铝四元素的主要特性（难点）","status":"init","class_date":"2016-08-01","live_time":"2:00~2:30","course_name":"初二化学重点、难点、易错点、考点综合讲解辅导班","course_publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_05db4f303a24d0180d173eb31e2e5051.jpeg","subject":"化学","pull_address":"","teacher_name":"luke"},{"id":49,"name":"第二课 铁钙镁铝四元素的主要特性（难点）","status":"init","class_date":"2016-08-01","live_time":"2:00~2:30","course_name":"初二化学重点、难点、易错点、考点综合讲解辅导班","course_publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_05db4f303a24d0180d173eb31e2e5051.jpeg","subject":"化学","pull_address":"","teacher_name":"luke"}]},{"date":"2016-08-31","lessons":[{"id":58,"name":"122123","status":"init","class_date":"2016-08-31","live_time":"3:00~3:30","course_name":"王小龙 800*500","course_publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_41364c5b954a58bb2b0379ee672d71ed.png","subject":"数学","pull_address":"","teacher_name":"luke"},{"id":58,"name":"122123","status":"init","class_date":"2016-08-31","live_time":"3:00~3:30","course_name":"王小龙 800*500","course_publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_41364c5b954a58bb2b0379ee672d71ed.png","subject":"数学","pull_address":"","teacher_name":"luke"},{"id":59,"name":"122123","status":"init","class_date":"2016-08-31","live_time":"2:00~6:00","course_name":"王小龙  新图片","course_publicize":"http://qatime-testing.oss-cn-beijing.aliyuncs.com/courses/publicize/list_1fef98d0b40a237f808d931043c9b763.png","subject":"物理","pull_address":"","teacher_name":"luke"}]}]
     */

    private int status;
    private List<DataEntity> data;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * date : 2016-08-08
         * lessons : [{"id":15,"name":"第一节","status":"finished","class_date":"2016-08-08","live_time":"6:30~8:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":15,"name":"第一节","status":"finished","class_date":"2016-08-08","live_time":"6:30~8:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":16,"name":"第二节","status":"finished","class_date":"2016-08-08","live_time":"21:00~22:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"},{"id":16,"name":"第二节","status":"finished","class_date":"2016-08-08","live_time":"21:00~22:00","course_name":"测试一下辅导班","course_publicize":"http://testing.qatime.cn/imgs/no_img.png","subject":"数学","pull_address":"","teacher_name":"辛帅锋"}]
         */

        private String date;
        private List<LessonsEntity> lessons;

        public void setDate(String date) {
            this.date = date;
        }

        public void setLessons(List<LessonsEntity> lessons) {
            this.lessons = lessons;
        }

        public String getDate() {
            return date;
        }

        public List<LessonsEntity> getLessons() {
            return lessons;
        }

        public static class LessonsEntity {
            /**
             * id : 15
             * name : 第一节
             * status : finished
             * class_date : 2016-08-08
             * live_time : 6:30~8:00
             * course_name : 测试一下辅导班
             * course_publicize : http://testing.qatime.cn/imgs/no_img.png
             * subject : 数学
             * pull_address :
             * teacher_name : 辛帅锋
             */

            private int id;
            private String name;
            private String status;
            private String class_date;
            private String live_time;
            private String course_name;
            private String course_publicize;
            private String subject;
            private String grade;
            private String pull_address;
            private String teacher_name;
            private String course_id;

            public String getGrade() {
                return grade;
            }

            public void setGrade(String grade) {
                this.grade = grade;
            }

            public String getCourse_id() {
                return course_id;
            }

            public void setCourse_id(String course_id) {
                this.course_id = course_id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public void setClass_date(String class_date) {
                this.class_date = class_date;
            }

            public void setLive_time(String live_time) {
                this.live_time = live_time;
            }

            public void setCourse_name(String course_name) {
                this.course_name = course_name;
            }

            public void setCourse_publicize(String course_publicize) {
                this.course_publicize = course_publicize;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public void setPull_address(String pull_address) {
                this.pull_address = pull_address;
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getStatus() {
                return status;
            }

            public String getClass_date() {
                return class_date;
            }

            public String getLive_time() {
                return live_time;
            }

            public String getCourse_name() {
                return course_name;
            }

            public String getCourse_publicize() {
                return course_publicize;
            }

            public String getSubject() {
                return subject;
            }

            public String getPull_address() {
                return pull_address;
            }

            public String getTeacher_name() {
                return teacher_name;
            }
        }
    }
}
