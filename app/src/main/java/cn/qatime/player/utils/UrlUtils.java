package cn.qatime.player.utils;

import java.util.Iterator;
import java.util.Map;

/**
 * url类
 */
public class UrlUtils {
    //测试地址
    public static String baseUrl = "http://testing.qatime.cn/";
    //正式地址
//    public static String baseUrl ="http://qatime.cn/";
    //登录
    public static String urlLogin = baseUrl + "api/v1/sessions";
    //辅导班
    public static String urlRemedialClass = baseUrl + "api/v1/live_studio/courses";


    public static String getUrl(String function, Map<String, String> params) {
        String url;
        url = function + "?" + Map2String(params);
        LogUtils.e("请求地址------    " + url);
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
            if (entry.getKey().equals("Remember-Token") && StringUtils.isNullOrBlanK(entry.getValue())) {
                LogUtils.e("token ************************************is null");
            }
            if (!StringUtils.isNullOrBlanK(entry.getKey()) && !StringUtils.isNullOrBlanK(entry.getValue())) {
                sb.append(entry.getKey().toString()).append("=").append(entry.getValue().toString()).append(iterator.hasNext() ? "&" : "");
            }
        }
        return sb.toString();
    }
}
