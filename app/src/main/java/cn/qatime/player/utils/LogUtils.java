package cn.qatime.player.utils;

import android.util.Log;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class LogUtils {
    private static boolean isDebug = true;

    public static void e(Object object) {
        if (LogUtils.isDebug) {
            if (!StringUtils.isNullOrBlanK(object)) {
                Log.e("dayitime", object.toString());
            } else {
            LogUtils.e("参数为空");
            }

        }
    }

    public static void e(String tag, Object object) {
        if (LogUtils.isDebug) {
            Log.e("dayitime" + tag, object.toString());
        }
    }

    public static void e(String tag, Object object, Exception io) {
        if (LogUtils.isDebug) {
            Log.e("dayitime" + tag, object.toString(), io);
        }
    }

    public static void w(String tag, Object object) {
        if (LogUtils.isDebug) {
            Log.w("dayitime" + tag, object.toString());
        }
    }
}
