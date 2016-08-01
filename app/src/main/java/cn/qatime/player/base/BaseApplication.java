package cn.qatime.player.base;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class BaseApplication extends Application{
//    public static RequestQueue Queue= Volley.newRequestQueue(this);;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
