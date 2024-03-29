package cn.qatime.player.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimStrings;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.common.inter.ITagManager;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.message.tag.TagManager;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import cn.qatime.player.R;
import cn.qatime.player.activity.MainActivity;
import cn.qatime.player.bean.BusEvent;
import cn.qatime.player.config.UserPreferences;
import cn.qatime.player.holder.CustomAttachParser;
import cn.qatime.player.im.LoginSyncDataStatusObserver;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.cache.UserInfoCache;
import cn.qatime.player.utils.SPUtils;
import cn.qatime.player.utils.StorageUtil;
import cn.qatime.player.utils.UrlUtils;
import custom.Configure;
import libraryextra.bean.CashAccountBean;
import libraryextra.bean.CityBean;
import libraryextra.bean.Profile;
import libraryextra.rx.HttpManager;
import libraryextra.rx.model.HttpHeaders;
import libraryextra.utils.AppUtils;
import libraryextra.utils.StringUtils;

public class BaseApplication extends MultiDexApplication {
    private Profile profile;
    private UserInfoProvider userInfoProvider;
    private static BaseApplication context;
    private RequestQueue Queue;
    public boolean newVersion;
    private CityBean.Data currentCity;
    private PushAgent mPushAgent;
    private CashAccountBean cashAccount;
    private boolean tokenOut = false;//账号已过期
//    public List<Activity> topActivity = new ArrayList<>();

    public RequestQueue getRequestQueue() {
        if (Queue == null) {
            Queue = Volley.newRequestQueue(context);
        }
        return Queue;
    }

    static {
        PlatformConfig.setWeixin("wxf2dfbeb5f641ce40", "7eb546caee5844cc6893449287be3b1b");
    }

    public CityBean.Data getCurrentCity() {
        if (currentCity == null) {
            currentCity = new CityBean.Data("全国");
            currentCity.setWorkstation_id(-1);
        }
        return currentCity;
    }

    public void setCurrentCity(CityBean.Data currentCity) {
        this.currentCity = currentCity;
        SPUtils.putObject(context, "current_city", currentCity);
    }

    public void setCashAccount(CashAccountBean cashAccount) {
        this.cashAccount = cashAccount;
    }

    public CashAccountBean getCashAccount() {
        return cashAccount;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        UMShareAPI.get(this);
        Logger.init("QTA-TIME")               // default tag : PRETTYLOGGER or use just init()
                .setMethodCount(3)            // default 2
                .hideThreadInfo()             // default it is shown
                .setLogLevel(Configure.isDebug ? LogLevel.FULL : LogLevel.NONE);  // default : LogLevel.FULL
        HttpManager.init(this);
        HttpManager.getInstance().setBaseUrl(UrlUtils.getBaseUrl());

        profile = SPUtils.getObject(this, "profile", Profile.class);
        currentCity = SPUtils.getObject(this, "current_city", CityBean.Data.class);

//        CrashHandler.getInstance().init(this);
        //y友盟统计
        MobclickAgent.setDebugMode(Configure.isDebug);
        initYunxin();
        initUmengPush();

        StorageUtil.init(context, null);
        initRx();
//        initGlobeActivity();
    }

    private void initRx() {
        HttpManager.init(this);

        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        if (!StringUtils.isNullOrBlanK(getProfile().getToken())) {
            headers.put("Remember-Token", getProfile().getToken());
        }
        HttpManager.getInstance()
                .setReadTimeOut(60 * 1000)
                .setWriteTimeOut(60 * 1000)
                .setConnectTimeout(60 * 1000)
                .setBaseUrl(UrlUtils.getBaseUrl())
                //.addConverterFactory(GsonConverterFactory.create(gson))//本框架没有采用Retrofit的Gson转化，所以不用配置
                .addCommonHeaders(headers);//设置全局公共头//设置全局公共参数
    }

    private void initUmengPush() {
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(Configure.isDebug);

        mPushAgent.setNotificationPlaySound(UserPreferences.getRingToggle() ? MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE : MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);
        mPushAgent.setNotificationPlayVibrate(UserPreferences.getVibrateToggle() ? MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE : MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Logger.e("收到custom");
                if (msg != null && msg.custom != null && !StringUtils.isNullOrBlanK(msg.custom)) {
                    try {
                        JSONObject response = new JSONObject(msg.custom);
                        if (response.has("type") && response.get("type").toString().equals("0")) {
                            Intent intent = new Intent(BaseApplication.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("type", "system_message");
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        mPushAgent.setMessageHandler(new UmengMessageHandler() {
            @Override
            public void handleMessage(Context context, UMessage uMessage) {
                EventBus.getDefault().postSticky(BusEvent.HANDLE_U_PUSH_MESSAGE);
                super.handleMessage(context, uMessage);
            }
        });
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                if (getUserId() != 0) {
                    mPushAgent.addAlias(String.valueOf(getUserId()), "student", new UTrack.ICallBack() {
                        @Override
                        public void onMessage(boolean b, String s) {
                        }
                    });
                }
                mPushAgent.getTagManager().add(new TagManager.TCallBack() {
                    @Override
                    public void onMessage(boolean b, ITagManager.Result result) {
                    }
                }, "student");
//                Logger.e("device" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
//        mPushAgent.setNotificaitonOnForeground(false);

    }

    public static void setOptions(boolean voiceStatus, boolean shakeStatus) {
        PushAgent.getInstance(context).setNotificationPlayVibrate(voiceStatus ? MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE : MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        PushAgent.getInstance(context).setNotificationPlaySound(shakeStatus ? MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE : MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.ring = voiceStatus;
        config.vibrate = shakeStatus;
        NIMClient.updateStatusBarNotificationConfig(config);
    }


    private void initYunxin() {
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
            NIMClient.getService(MsgService.class).registerCustomAttachmentParser(new CustomAttachParser());
            // 注册语言变化监听
            registerLocaleReceiver(true);
        }

        /** 云信集成end*/
    }

    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();
//        options.appKey = UrlUtils.appKey;
        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        // 通知要跳的页面
        config.notificationEntrance = MainActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.ic_launcher;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串

        config.notificationSound = "android.resource://cn.qatime.player/raw/msg";
        options.statusBarNotificationConfig = config;
        config.ring = UserPreferences.getRingToggle();
        config.vibrate = UserPreferences.getVibrateToggle();

        UserPreferences.setStatusConfig(config);
        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
//        options.sdkStorageRootPath = getCacheDir() + "/nim";

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
            return R.mipmap.head_default;
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
//                UserInfo user = getUserInfo(account);
//                return (user != null) ? ImageLoaderKit.getNotificationBitmapFromCache(user) : null;
            return null;
        }

        @Override
        public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
            String nick = null;
            if (sessionType == SessionTypeEnum.P2P) {
                nick = UserInfoCache.getInstance().getUserName(account);
            } else if (sessionType == SessionTypeEnum.Team) {
                nick = TeamDataCache.getInstance().getTeamNick(sessionId, account);
                if (TextUtils.isEmpty(nick)) {
                    nick = UserInfoCache.getInstance().getUserName(account);
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

    public Profile getProfile() {
        return profile == null ? new Profile() : profile;
    }

    public int getUserId() {
        return profile != null && profile.getData() != null && profile.getData().getUser() != null ? profile.getData().getUser().getId() : 0;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
        HttpHeaders header = new HttpHeaders();
        header.put("Remember-Token", profile.getToken());
        HttpManager.getInstance().addCommonHeaders(header);
        SPUtils.putObject(context, "profile", profile);
    }

    public void clearToken() {
        if (profile != null && profile.getData() != null) {
            profile.getData().setRemember_token("");
            if (profile.getData().getUser() != null && profile.getData().getUser().getChat_account() != null) {
                profile.getData().getUser().getChat_account().setAccid("");
                profile.getData().getUser().getChat_account().setToken("");
            }
            cashAccount = null;
            SPUtils.putObject(context, "profile", profile);
            LoginSyncDataStatusObserver.getInstance().reset();
            NIMClient.getService(AuthService.class).logout();
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
    public String getAccount() {
        if (getProfile().getData() != null && getProfile().getData().getUser() != null && getProfile().getData().getUser().getChat_account() != null) {
            return getProfile().getData().getUser().getChat_account().getAccid();
        } else {
            return "";
        }
    }

    public boolean isLogined() {
        return !StringUtils.isNullOrBlanK(getProfile().getToken());
    }

    /**
     * 登录云信的token
     */
    public String getAccountToken() {
        if (getProfile().getData() != null && getProfile().getData().getUser() != null && getProfile().getData().getUser().getChat_account() != null) {
            return getProfile().getData().getUser().getChat_account().getToken();
        } else {
            return "";
        }
    }

    public UserInfoProvider getUserInfoProvide() {
        return userInfoProvider;
    }

    public String getUserName() {
        return profile != null && profile.getData() != null && profile.getData().getUser() != null ? profile.getData().getUser().getName() : "";
    }

//    private void initGlobeActivity() {
//        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//            @Override
//            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                if (!topActivity.contains(activity)) {
//                    topActivity.add(0, activity);
////                    Logger.e("top" + topActivity.get(0).getLocalClassName());
//                }
//            }
//
//            @Override
//            public void onActivityDestroyed(Activity activity) {
//                if (topActivity.contains(activity)) {
//                    topActivity.remove(activity);
////                    if (topActivity.size() > 0) {
////                        Logger.e("top" + topActivity.get(0).getLocalClassName());
////                    }
//                }
//            }
//
//            /** Unused implementation **/
//            @Override
//            public void onActivityStarted(Activity activity) {
//            }
//
//            @Override
//            public void onActivityResumed(Activity activity) {
//            }
//
//            @Override
//            public void onActivityPaused(Activity activity) {
//            }
//
//            @Override
//            public void onActivityStopped(Activity activity) {
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//            }
//        });
//    }
//
//    public Activity getTopActivity() {
//        return topActivity.get(0);
//    }

    public static BaseApplication getInstance() {
        return context;
    }

    public void setTokenOut(boolean tokenOut) {
        this.tokenOut = tokenOut;
    }

    public boolean isTokenOut() {
        return tokenOut;
    }
}
