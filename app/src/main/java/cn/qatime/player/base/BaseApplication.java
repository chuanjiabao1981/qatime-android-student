package cn.qatime.player.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimStrings;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import cn.qatime.player.R;
import cn.qatime.player.activity.MainActivity;
import cn.qatime.player.config.UserPreferences;
import cn.qatime.player.im.LoginSyncDataStatusObserver;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.cache.UserInfoCache;
import cn.qatime.player.utils.AppUtils;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.Profile;
import libraryextra.utils.SPUtils;
import libraryextra.utils.StringUtils;

public class BaseApplication extends Application {
    private static Profile profile;
    public static UserInfoProvider userInfoProvider;
    private static BaseApplication context;
    private static RequestQueue Queue;

    public static RequestQueue getRequestQueue() {
        return Queue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Logger.init("QTA-TIME")               // default tag : PRETTYLOGGER or use just init()
                .setMethodCount(3)            // default 2
                .hideThreadInfo()             // default it is shown
                .setLogLevel(LogLevel.FULL);  // default : LogLevel.FULL

        Queue = Volley.newRequestQueue(getApplicationContext());

        profile = SPUtils.getObject(this, "profile", Profile.class);
        /** 云信集成start*/
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        SPUtils.setContext(this);
        NIMClient.init(this, loginInfo(), options());

        if (AppUtils.inMainProcess(this)) {
//             注意：以下操作必须在主进程中进行
//             1、UI相关初始化操作
//             2、相关Service调用
            // 初始化消息提醒
            if (!StringUtils.isNullOrBlanK(getAccount())) {
                NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
                // init data cache
                LoginSyncDataStatusObserver.getInstance().registerLoginSyncDataStatus(true);  // 监听登录同步数据完成通知

                UserInfoCache.getInstance().clear();
                TeamDataCache.getInstance().clear();
//                FriendDataCache.getInstance().clear();

                UserInfoCache.getInstance().buildCache();
                TeamDataCache.getInstance().buildCache();
                //好友维护,目前不需要
//                FriendDataCache.getInstance().buildCache();

                UserInfoCache.getInstance().registerObservers(true);
                TeamDataCache.getInstance().registerObservers(true);
//                FriendDataCache.getInstance().registerObservers(true);
            }
            // 注册语言变化监听
            registerLocaleReceiver(true);
        }

        /** 云信集成end*/
    }

    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();
        options.appKey = UrlUtils.appKey;
        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        //TODO 通知要跳的页面
        config.notificationEntrance = MainActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.ic_launcher;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://cn.qatime.player/raw/msg";
        options.statusBarNotificationConfig = config;
        config.vibrate = true;

        UserPreferences.setStatusConfig(config);
        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = getCacheDir() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
//        options.thumbnailSize = $ {            Screen.width        }/2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = infoProvider;
        this.userInfoProvider = infoProvider;
        return options;
    }

    private UserInfoProvider infoProvider = new UserInfoProvider() {
        @Override
        public UserInfoProvider.UserInfo getUserInfo(String account) {
            UserInfoProvider.UserInfo user = UserInfoCache.getInstance().getUserInfo(account);
            if (user == null) {
                UserInfoCache.getInstance().getUserInfoFromRemote(account, null);
            }
            return user;
        }

        @Override
        public int getDefaultIconResId() {
            return R.mipmap.head_sculpture;
        }

        @Override
        public Bitmap getTeamIcon(String tid) {
            Drawable drawable = getResources().getDrawable(R.mipmap.nim_avatar_group);
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }
            return null;
        }

        @Override
        public Bitmap getAvatarForMessageNotifier(String account) {
            /**
             * 注意：这里最好从缓存里拿，如果读取本地头像可能导致UI进程阻塞，导致通知栏提醒延时弹出。
             */
            //TODO
//                UserInfo user = getUserInfo(account);
//                return (user != null) ? ImageLoaderKit.getNotificationBitmapFromCache(user) : null;
            return null;
        }

        @Override
        public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
            String nick = null;
            if (sessionType == SessionTypeEnum.P2P) {
                nick = UserInfoCache.getInstance().getAlias(account);
            } else if (sessionType == SessionTypeEnum.Team) {
                nick = TeamDataCache.getInstance().getTeamNick(sessionId, account);
                if (TextUtils.isEmpty(nick)) {
                    nick = UserInfoCache.getInstance().getAlias(account);
                }
            }
            // 返回null，交给sdk处理。如果对方有设置nick，sdk会显示nick
            if (TextUtils.isEmpty(nick)) {
                return null;
            }

            return nick;
        }
    };

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        String account = getAccount();
        String token = getAccountToken();

        if (!StringUtils.isNullOrBlanK(account) && !StringUtils.isNullOrBlanK(token)) {
            Logger.e("云信初始化有账号****************************" + account + "--------" + token);
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    public static Profile getProfile() {
        return profile == null ? new Profile() : profile;
    }

    public static int getUserId() {
        return profile != null && profile.getData() != null && profile.getData().getUser() != null ? profile.getData().getUser().getId() : 0;
    }

    public static void setProfile(Profile profile) {
        BaseApplication.profile = profile;
        SPUtils.putObject(context, "profile", profile);
    }

    public static void clearToken() {
        if (profile != null && profile.getData() != null) {
            profile.getData().setRemember_token("");
            if (profile.getData().getUser() != null && profile.getData().getUser().getChat_account() != null) {
                profile.getData().getUser().getChat_account().setAccid("");
                profile.getData().getUser().getChat_account().setToken("");
            }
            SPUtils.putObject(context, "profile", profile);
            LoginSyncDataStatusObserver.getInstance().reset();
        }
    }

    /**
     * 语言监听
     *
     * @param register
     */
    private void registerLocaleReceiver(boolean register) {
        if (register) {
            updateLocale();
            IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
            registerReceiver(localeReceiver, filter);
        } else {
            unregisterReceiver(localeReceiver);
        }
    }

    private BroadcastReceiver localeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
                updateLocale();
            }
        }
    };


    private void updateLocale() {
        NimStrings strings = new NimStrings();
        strings.status_bar_multi_messages_incoming = getString(R.string.nim_status_bar_multi_messages_incoming);
        strings.status_bar_image_message = getString(R.string.nim_status_bar_image_message);
        strings.status_bar_audio_message = getString(R.string.nim_status_bar_audio_message);
        strings.status_bar_custom_message = getString(R.string.nim_status_bar_custom_message);
        strings.status_bar_file_message = getString(R.string.nim_status_bar_file_message);
        strings.status_bar_location_message = getString(R.string.nim_status_bar_location_message);
        strings.status_bar_notification_message = getString(R.string.nim_status_bar_notification_message);
        strings.status_bar_ticker_text = getString(R.string.nim_status_bar_ticker_text);
        strings.status_bar_unsupported_message = getString(R.string.nim_status_bar_unsupported_message);
        strings.status_bar_video_message = getString(R.string.nim_status_bar_video_message);
        strings.status_bar_hidden_message_content = getString(R.string.nim_status_bar_hidden_msg_content);
        NIMClient.updateStrings(strings);
    }

    /**
     * 登录云信的账号
     */
    public static String getAccount() {
        if (getProfile().getData() != null && getProfile().getData().getUser() != null && getProfile().getData().getUser().getChat_account() != null) {
            return getProfile().getData().getUser().getChat_account().getAccid();
        } else {
            return "";
        }
    }

    /**
     * 登录云信的token
     */
    public static String getAccountToken() {
        if (getProfile().getData() != null && getProfile().getData().getUser() != null && getProfile().getData().getUser().getChat_account() != null) {
            return getProfile().getData().getUser().getChat_account().getToken();
        } else {
            return "";
        }
    }

    public static UserInfoProvider getUserInfoProvide() {
        return userInfoProvider;
    }
}
