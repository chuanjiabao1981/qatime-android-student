package cn.qatime.player.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class StringUtils {


    public static String Int2String(int request) {
        return request < 10 ? "0" + request : String.valueOf(request);
    }

    /**
     *
     * @方法描述：（对象为空字符或null或集合长度为0）
     * @返回值：true/false
     * @version
     */
    public static boolean isNullOrBlanK(Object param) {
        if (param == null) {
            return true;
        } else {
            if (java.lang.String.class.isInstance(param)) {
                if (!"".equals(((String) param).trim())) {
                    return false;
                }
            } else if (java.util.List.class.isInstance(param)) {
                if (((ArrayList) param).size() != 0) {
                    return false;
                }
            } else if (java.util.Map.class.isInstance(param)) {
                if (((HashMap) param).size() != 0) {
                    return false;
                }
            } else if (java.lang.String[].class.isInstance(param)) {
                if (((Object[]) param).length != 0) {
                    return false;
                }
            } else {
                return false;
            }
            return true;
        }
    }
}
