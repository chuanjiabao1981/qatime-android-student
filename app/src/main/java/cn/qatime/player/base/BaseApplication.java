package cn.qatime.player.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.qatime.player.bean.Profile;

public class BaseApplication extends Application {
    //    public static RequestQueue Queue= Volley.newRequestQueue(this);;
    private static Profile profile;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Profile getProfile() {
        return profile == null ? new Profile() : profile;
    }

    public static int getUserId() {
        return profile != null && profile.getData() != null && profile.getData().getUser() != null ? profile.getData().getUser().getId() : 0;
    }

    public static void setProfile(Profile profile) {
        BaseApplication.profile = profile;
    }

    public static void clearToken() {
        if (profile != null && profile.getData() != null) {
            profile.getData().setRemember_token("");
        }
    }


//    public static boolean inMainProcess(Context context) {
//        String packageName = context.getPackageName();
//        String processName = SystemUtil.getProcessName(context);
//        return packageName.equals(processName);
//    }

    /**
     * 获取当前进程名
     * @param context
     * @return 进程名
     */
    public static final String getProcessName(Context context) {
        String processName = null;

        // ActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }

            // go home
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
