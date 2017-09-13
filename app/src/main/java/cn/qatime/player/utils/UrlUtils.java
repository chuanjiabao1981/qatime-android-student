package cn.qatime.player.utils;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import custom.Configure;
import libraryextra.utils.StringUtils;

/**
 * url类
 */
public class UrlUtils {

    private static String baseUrl = Configure.isDebug ? "http://testing.qatime.cn/" : "https://qatime.cn/";
    //登录
    public static String urlLogin = baseUrl + "api/v1/sessions";
    //搜索
    public static String urlHomeSearch = baseUrl + "api/v1/home/search";
    //回放
    public static String urlHomeReplays = baseUrl + "api/v1/home/replays";
    //搜索老师
    public static String teachers = baseUrl + "api/v1/home/teachers";
    //课程base
    public static String urlLiveStudio = baseUrl + "api/v1/live_studio/";
    //专属课
    public static String urlExclusiveLesson = baseUrl + "/api/v1/live_studio/customized_groups";
    //课程
    public static String urlCourses = baseUrl + "api/v1/live_studio/courses/";
    //一对一
    public static String urlInteractCourses = baseUrl + "api/v1/live_studio/interactive_courses/";
    //视频课
    public static String urlVideoCourses = baseUrl + "api/v1/live_studio/video_courses/";
    //视频课
    public static String urlVideoLessons = baseUrl + "api/v1/live_studio/video_lessons/";
    //我的
    public static String urlStudent = baseUrl + "api/v1/live_studio/students/";
    //专属课资源
    public static String urlGroups = baseUrl + "api/v1/live_studio/groups/";
    //
    public static String lessons = baseUrl + "api/v1/live_studio/lessons/";
    //文件资源
    public static String urlFiles = baseUrl + "api/v1/resource/";
    //个人信息
    public static String urlPersonalInformation = baseUrl + "api/v1/students/";
    //教师信息
    public static String urlTeacherInformation = baseUrl + "api/v1/teachers/";
    //用户信息
    public static String urlUser = baseUrl + "api/v1/users/";
    //注册
    public static String urlRegister = baseUrl + "api/v1/user/register";
    //微信注册
    public static String urlWeChatRegister = baseUrl + "api/v1/user/wechat_register";
    //直播课搜索
    public static String urlSearch = baseUrl + "api/v1/live_studio/courses/search";
    //最新课程
    public static String urlLiveStudioLatest = baseUrl + "api/v2/live_studio/courses/latest";
    //免费课程
    public static String urlLiveStudioFree = baseUrl + "api/v2/live_studio/free_courses";
    //今日直播
    public static String urlToady = baseUrl + "api/v2/live_studio/lessons/today";
    //獲取支付结果
    public static String urlPayResult = baseUrl + "api/v1/payment/orders/";
    //订单列表
    public static String urlPaylist = baseUrl + "api/v1/payment/orders";
    //优惠券
    public static String urlCoupon = baseUrl + "api/v1/payment/coupons/";
    //账户充值
    public static String urlpayment = baseUrl + "api/v1/payment/users/";
    //资产账户
    public static String cashAccounts = baseUrl + "/api/v1/payment/cash_accounts/";
    //检查更新
    public static String urlcheckUpdate = baseUrl + "api/v1/system/check_update";
    //上传用户设备信息
    public static String urlDeviceInfo = baseUrl + "api/v1/system/device_info";
    //找回密码
    public static String urlfindPassword = baseUrl + "api/v1/password";
    //标记通知已读
    public static String urlNotifications = baseUrl + "api/v1/notifications/";
    //首页推荐
    public static String urlRecommend = baseUrl + "api/v1/recommend/positions/";
    //获取验证码
    public static String urlGetCode = baseUrl + "api/v1/captcha";
    //基础信息
    public static String urlAppconstantInformation = baseUrl + "api/v1/app_constant";
    //课程表
    public static String urlScheduleData = baseUrl + "api/v2/live_studio/students/";

    public static String getUrl(String function, Map<String, String> params) {
        String url = function + "?" + Map2String(params);
        if (url.endsWith("&")) {
            url = url.substring(0, url.length() - 1);
        }
        Logger.e("request*url****" + url);
        return url;
    }

    /***
     * 对内使用
     *
     * @param map
     * @return
     */
    private static String Map2String(Map<String, String> map) {
        Map.Entry entry;
        StringBuilder sb = new StringBuilder();
        for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); ) {
            entry = (Map.Entry) iterator.next();
            if (!StringUtils.isNullOrBlanK(entry.getKey()) && !StringUtils.isNullOrBlanK(entry.getValue())) {
                sb.append(entry.getKey().toString()).append("=").append(entry.getValue().toString()).append(iterator.hasNext() ? "&" : "");
            }
        }
        return sb.toString();
    }

    public static Map<String, Object> getUrlParams(String param) {
        Map<String, Object> map = new HashMap<String, Object>();
        if ("".equals(param) || null == param) {
            return map;
        }
        String[] params = param.split("&");
        for (String param1 : params) {
            String[] p = param1.split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }
}
