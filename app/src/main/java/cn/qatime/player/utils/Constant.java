package cn.qatime.player.utils;

import android.os.Environment;

/**
 * @author luntify
 * @date 2016/8/10 10:28
 * @Description 常量类
 */
public class Constant {
    public static String phoneNumber = "400-838-8010";
    public static String APP_ID = "wxf2dfbeb5f641ce40";//微信appid
    public static final String CACHEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/qatime/images";

    public static int REQUEST = 0;

    public static int RESPONSE = 1;

    public static int REQUEST_EXIT_LOGIN = 0x1001;
    public static int RESPONSE_EXIT_LOGIN = 0x1002;
    public static int REQUEST_CAMERA = 0x1003;
    public static int RESPONSE_CAMERA = 0x1004;
    public static int PHOTO_CROP = 0x1005;
    public static int REQUEST_PICTURE_SELECT = 0x1006;
    public static int RESPONSE_PICTURE_SELECT = 0x1007;
    public static int RESPONSE_HEAD_SCULPTURE = 0x1008;
    public static int REGIST_1 = 0x1009;//LoginActivity
    public static int REGIST_2 = 0x1010;//LoginActivity2
    public static int RESPONSE_CITY_SELECT = 0x1011;
    public static int CHANGE_PAY_PSW = 0x1012;
    public static final int QRCODE_SUCCESS = 0x1013;
    public static int REQUEST_REGION_SELECT = 0x1014;
    public static int RESPONSE_REGION_SELECT = 0x1015;
    public static int REQUEST_SCHOOL_SELECT = 0x1016;
    public static int RESPONSE_SCHOOL_SELECT = 0x1017;

    public static class CourseStatus {
        //<--teacher
        public static String rejected = "rejected";//审核被拒绝
        public static String init = "init";//审核被拒绝
        //-->
        public static String published = "published";//招生中
        public static String teaching = "teaching";//已开课
        public static String completed = "completed";//已完成
    }

    /**
     * 系统通知状态
     */
    public static class NotificationStatus {
        public static String LiveStudioCourseNotification = "live_studio_course_notification";
        public static String CustomizedCourseActionNotification = "customized_course_action_notification";
        public static String LiveStudioLessonNotification = "live_studio_lesson_notification";
    }

    /**
     * 游客状态到登录页返回时需要做的动作
     */
    public static class LoginAction {
        public static String toPage3 = "toPage3";
        //        public static String toPage4 = "toPage4";
        public static String toPage5 = "toPage5";
        public static String toClassTimeTable = "toClassTimeTable";
        public static String toMessage = "toMessage";
        public static String toRemedialClassDetail = "toRemedialClassDetail";
    }
}
