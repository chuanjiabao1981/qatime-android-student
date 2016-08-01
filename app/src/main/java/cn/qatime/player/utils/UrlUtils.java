package cn.qatime.player.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * url类
 */
public class UrlUtils {
    public static String baseurl ="http://testing.qatime.cn/";

    public static  String url_login = baseurl+"api/v1/sessions";















    public static String getUrl(String function, Map<String, String> params) {
        String url;
        url = function + "?" + Map2String(params);

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
            sb.append(entry.getKey().toString()).append("=").append(null == entry.getValue() ? "" :
                    entry.getValue().toString()).append(iterator.hasNext() ? "&" : "");
        }
        return sb.toString();
    }
}
