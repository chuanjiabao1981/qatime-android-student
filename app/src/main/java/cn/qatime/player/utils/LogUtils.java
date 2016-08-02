package cn.qatime.player.utils;

import android.util.Log;

import java.util.Objects;
import java.util.logging.Logger;

public class LogUtils {
    private static boolean isDebug = true;

    public static void e(Object object) {
        if (LogUtils.isDebug) {
            Log.e("dayitime", object.toString());
        }
    }
}
