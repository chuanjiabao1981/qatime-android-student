package cn.qatime.player.bean;

import android.app.Activity;

/**
 * @author lungtify
 * @Time 2017/2/10 11:57
 * @Describe
 */

public class Container {
    public String account;
    public Activity activity;
    public ModuleProxy proxy;

    public Container(Activity activity, String account, ModuleProxy proxy) {
        this.activity = activity;
        this.account = account;
        this.proxy = proxy;
    }
}
