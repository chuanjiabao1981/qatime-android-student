package cn.qatime.player.utils;

public class StringUtils {


    public static String Int2String(int request) {
        return request < 10 ? "0" + request : String.valueOf(request);
    }
}
