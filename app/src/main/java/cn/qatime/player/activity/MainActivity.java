package cn.qatime.player.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.config.UserPreferences;
import cn.qatime.player.fragment.FragmentHomeClassTable;
import cn.qatime.player.fragment.FragmentHomeMainPage;
import cn.qatime.player.fragment.FragmentHomeMessage;
import cn.qatime.player.fragment.FragmentHomeSelectSubject;
import cn.qatime.player.fragment.FragmentHomeUserCenter;
import cn.qatime.player.fragment.FragmentUnLoginHomeClassTable;
import cn.qatime.player.fragment.FragmentUnLoginHomeMessage;
import cn.qatime.player.fragment.FragmentUnLoginHomeUserCenter;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.cache.UserInfoCache;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.bean.SystemNotifyBean;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.SPUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.FragmentLayout;

public class MainActivity extends BaseFragmentActivity {

    FragmentLayout fragmentlayout;
    public ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private int[] tab_img = {R.id.tab_img1, R.id.tab_img2, R.id.tab_img3, R.id.tab_img4, R.id.tab_img5};
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4, R.id.tab_text5};
    private View message_x;
    private int image_list[][] = new int[][]{
            {R.mipmap.tab_home1, R.mipmap.tab_home2},
            {R.mipmap.tab_tutorship1, R.mipmap.tab_tutorship2},
            {R.mipmap.tab_moments1, R.mipmap.tab_moments2},
            {R.mipmap.tab_message1, R.mipmap.tab_message2},
            {R.mipmap.tab_person1, R.mipmap.tab_person2},
    };
    //      创建观察者对象
    Observer<List<RecentContact>> messageObserver =
            new Observer<List<RecentContact>>() {
                @Override
                public void onEvent(List<RecentContact> messages) {
                    if (fragmentlayout.getCurrentPosition() != 3) {
                        refreshUnreadNum();
                    }
                }
            };

    /**
     * 刷新未读
     */
    private void refreshUnreadNum() {
        int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
        Logger.e("unreadNum" + unreadNum);
        message_x.setVisibility(unreadNum == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        EventBus.getDefault().register(this);
        //  注册/注销观察者
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, true);
        refreshUnreadNum();
        refreshMedia();

        File file = new File(Constant.CACHEPATH);
        if (!file.mkdirs()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        parseIntent();
        //        GetGradeslist();
//        GetProvinceslist();
//        GetCitieslist();
        GetSchoolslist();
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, true);
    }

    /**
     * 初始化布局
     */
    private void initView() {
        //重置
        fragBaseFragments.clear();
        if (fragmentlayout != null) {
            fragmentlayout.reset();
        }

        //添加fragment
        fragBaseFragments.add(new FragmentHomeMainPage());
//        fragBaseFragments.add(new FragmentRemedialClassAll());
        fragBaseFragments.add(new FragmentHomeSelectSubject());

        if (BaseApplication.isLogined()) {
            fragBaseFragments.add(new FragmentHomeClassTable());
            fragBaseFragments.add(new FragmentHomeMessage());
            initMessage();
            fragBaseFragments.add(new FragmentHomeUserCenter());
        } else {
            fragBaseFragments.add(new FragmentUnLoginHomeClassTable());
            fragBaseFragments.add(new FragmentUnLoginHomeMessage());
            fragBaseFragments.add(new FragmentUnLoginHomeUserCenter());
        }


        fragmentlayout = (FragmentLayout) findViewById(R.id.fragmentlayout);
        fragmentlayout.setScorllToNext(false);
        fragmentlayout.setScorll(false);
        fragmentlayout.setWhereTab(0);

        fragmentlayout.setOnChangeFragmentListener(new FragmentLayout.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff666666);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xffff5842);
                ((ImageView) lastTabView.findViewById(tab_img[lastPosition])).setImageResource(image_list[lastPosition][1]);
                ((ImageView) currentTabView.findViewById(tab_img[position])).setImageResource(image_list[position][0]);
                if (position == 3) {
                    /**
                     * 设置最近联系人的消息为已读
                     *
                     * @param account,    聊天对象帐号，或者以下两个值：
                     *                    {@link #MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
                     *                    {@link #MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
                     */
                    message_x.setVisibility(View.GONE);
                    NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
                } else {
                    NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
                }
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout, 0x1000);
        fragmentlayout.getViewPager().setOffscreenPageLimit(4);
        message_x = fragmentlayout.getTabLayout().findViewById(R.id.message_x);


    }

    private void initMessage() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(BaseApplication.getUserId()));
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlUser + BaseApplication.getUserId() + "/notifications", map), null,
                new VolleyListener(MainActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        SystemNotifyBean data = JsonUtils.objectFromJson(response.toString(), SystemNotifyBean.class);
                        if (data != null && data.getData() != null) {
                            for (SystemNotifyBean.DataBean bean : data.getData()) {
                                if (!bean.isRead()) {//有未读发送未读event
                                    EventBus.getDefault().postSticky("handleUPushMessage");
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constant.QRCODE_SUCCESS) {//扫描二维码返回数据跳转至辅导班详情页
            Intent intent = new Intent(this, RemedialClassDetailActivity.class);
            intent.putExtra("id", data.getIntExtra("id", 0));
            intent.putExtra("coupon", data.getStringExtra("coupon"));
            startActivity(intent);
        }
    }

    private boolean flag = false;

    @Override
    public void onBackPressed() {
        if (!flag) {
            Toast toast = Toast.makeText(this, getResources().getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            flag = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    flag = false;
                }
            }, 2500);
        } else {
            this.finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (!StringUtils.isNullOrBlanK(intent.getStringExtra("out")) || (!StringUtils.isNullOrBlanK(intent.getStringExtra("sign")))) {
            Intent start = new Intent(this, LoginActivity.class);
            if (!StringUtils.isNullOrBlanK(intent.getStringExtra("sign"))) {
                start.putExtra("sign", intent.getStringExtra("sign"));
            }
            startActivity(start);
            finish();
        } else if (!StringUtils.isNullOrBlanK(intent.getStringExtra("activity_action"))) {
            initView();
            String action = intent.getStringExtra("activity_action");
            if (!StringUtils.isNullOrBlanK(action)) {
                if (action.equals(Constant.LoginAction.toPage3)) {//课程表点击登录返回
                    fragmentlayout.setCurrenItem(2);
                    if (action.equals(Constant.LoginAction.toMessage)) {//消息
                        fragmentlayout.setCurrenItem(3);
                    } else if (action.equals(Constant.LoginAction.toPage5)) {//个人中心
                        fragmentlayout.setCurrenItem(4);
                    } else if (action.equals(Constant.LoginAction.toClassTimeTable)) {//课程表右上角点击返回
                        fragmentlayout.setCurrenItem(2);
                        Intent intentAction = new Intent(MainActivity.this, ClassTimeTableActivity.class);
                        startActivity(intentAction);
                    } else if (action.equals(Constant.LoginAction.toRemedialClassDetail)) {
                        Intent intentAction = new Intent(MainActivity.this, RemedialClassDetailActivity.class);
                        intent.putExtra("id", getIntent().getStringExtra("id"));
                        startActivity(intentAction);
                    }
                }
            }
        }else {
            //云信通知消息
            setIntent(intent);
            parseIntent();
        }
    }

    private void parseIntent() {
        Intent data = getIntent();
        /**     * 解析通知栏发来的云信消息     */
        if (data != null) {
            if (data.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT) ||
                    //转到系统消息页面
                    (data.hasExtra("type") && data.getStringExtra("type").equals("system_message"))) {
                if (fragBaseFragments != null && fragBaseFragments.size() > 0 && fragBaseFragments.get(3) instanceof FragmentHomeMessage) {
                    ((FragmentHomeMessage) fragBaseFragments.get(3)).setMessage(data);
                }
//                Intent intent = new Intent(this, MessageFragmentActivity.class);
//                intent.putExtra("intent", data);
//                startActivity(intent);
            }
        }
    }

    /**
     * 刷新媒体库
     */
    public void refreshMedia() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        } else {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }

    }

//    /**
//     * GET /api/v1/app_constant 获取基础信息
//     */
//    public void getBaseInformation() {
//
//        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlAppconstantInformation, null,
//                new VolleyListener(MainActivity.this) {
//                    @Override
//                    protected void onSuccess(JSONObject response) {
//
//                    }
//
//                    @Override
//                    protected void onError(JSONObject response) {
//
//                    }
//                }, new VolleyErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                super.onErrorResponse(volleyError);
//            }
//        });
//        addToRequestQueue(request);
//    }


//    //省份列表
//    public void GetProvinceslist() {
//
//        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlAppconstantInformation + "/provinces", null,
//                new VolleyListener(MainActivity.this) {
//                    @Override
//
//
//                    protected void onSuccess(JSONObject response) {
//
//                    }
//
//                    @Override
//                    protected void onError(JSONObject response) {
//
//                    }
//
//                    @Override
//                    protected void onTokenOut() {
//                        tokenOut();
//                    }
//                }, new VolleyErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                super.onErrorResponse(volleyError);
//            }
//        });
//        //TODO
////        addToRequestQueue(request);
//    }

    //城市列表
//    public void GetCitieslist() {
//
//        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlAppconstantInformation + "/cities", null,
//                new VolleyListener(MainActivity.this) {
//
//                    @Override
//                    protected void onSuccess(JSONObject response) {
//                        boolean value = FileUtil.writeFile(new ByteArrayInputStream(response.toString().getBytes()), getCacheDir().getAbsolutePath() + "/city.txt", true);
//                        SPUtils.put(MainActivity.this, "city", value);
//                    }
//
//                    @Override
//                    protected void onError(JSONObject response) {
//
//                    }
//
//                    @Override
//                    protected void onTokenOut() {
//                        tokenOut();
//                    }
//
//                }, new VolleyErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                super.onErrorResponse(volleyError);
//            }
//        });
//        //TODO
////        addToRequestQueue(request);
//    }

    //学校列表
    public void GetSchoolslist() {

        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.urlAppconstantInformation + "/schools", null,
                new VolleyListener(MainActivity.this) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        boolean value = FileUtil.writeFile(new ByteArrayInputStream(response.toString().getBytes()), getCacheDir().getAbsolutePath() + "/school.txt", true);
                        SPUtils.put(MainActivity.this, "school", value);
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 设置最近联系人的消息为已读
         *
         * @param account,    聊天对象帐号，或者以下两个值：
         *                    {@link #MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
         *                    {@link #MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
         */
        NIMClient.getService(MsgService.class).setChattingAccount(BaseApplication.isChatMessageNotifyStatus() ? MsgService.MSG_CHATTING_ACCOUNT_NONE : MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, false);
    }

    /**
     * 监听用户在线状态
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
//                kickOut(code);
                Logger.e("未登录成功");
            } else {
                if (code == StatusCode.NET_BROKEN) {
                    Logger.e("当前网络不可用");
                } else if (code == StatusCode.UNLOGIN) {
                    Logger.e("未登录");
                } else if (code == StatusCode.CONNECTING) {
                    Logger.e("连接中...");
                } else if (code == StatusCode.LOGINING) {
                    Logger.e("登录中...");
                } else {
//                    onRecentContactsLoaded();
                    Logger.e("其他" + code);
                }
            }
        }
    };

    @Subscribe
    public void onEvent(String event) {
        if (!StringUtils.isNullOrBlanK(event) && event.equals("pay_success")) {
            if (StringUtils.isNullOrBlanK(BaseApplication.getAccount()) || StringUtils.isNullOrBlanK(BaseApplication.getAccountToken())) {
                getAccount();
            }
        } else if (!StringUtils.isNullOrBlanK(event) && "handleUPushMessage".equals(event)) {
            if (fragmentlayout.getCurrentPosition() != 3) {
                message_x.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 当用户没有云信账号,第一次直接购买后,需从后台获取一次云信账号
     */
    private void getAccount() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", null,
                new VolleyListener(MainActivity.this) {


                    @Override
                    protected void onSuccess(JSONObject response) {
                        PersonalInformationBean bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);
                        if (bean != null && bean.getData() != null && bean.getData().getChat_account() != null) {
                            Profile profile = BaseApplication.getProfile();
                            profile.getData().getUser().setChat_account(bean.getData().getChat_account());
                            BaseApplication.setProfile(profile);

                            String account = BaseApplication.getAccount();
                            String token = BaseApplication.getAccountToken();

                            if (!StringUtils.isNullOrBlanK(account) && !StringUtils.isNullOrBlanK(token)) {
                                AbortableFuture<LoginInfo> loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
                                loginRequest.setCallback(new RequestCallback<LoginInfo>() {
                                    @Override
                                    public void onSuccess(LoginInfo o) {
                                        Logger.e("云信登录成功" + o.getAccount());
                                        // 初始化消息提醒
                                        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

                                        NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());
                                        //缓存
                                        UserInfoCache.getInstance().clear();
                                        TeamDataCache.getInstance().clear();

                                        UserInfoCache.getInstance().buildCache();
                                        TeamDataCache.getInstance().buildCache();

                                        UserInfoCache.getInstance().registerObservers(true);
                                        TeamDataCache.getInstance().registerObservers(true);
                                    }

                                    @Override
                                    public void onFailed(int code) {
                                        BaseApplication.clearToken();
                                    }

                                    @Override
                                    public void onException(Throwable throwable) {
                                        Logger.e(throwable.getMessage());
                                        BaseApplication.clearToken();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    public void more(View v) {
        setCurrentPosition(1, 0);
    }

    public void setCurrentPosition(int currentPosition, int position) {
        fragmentlayout.setCurrenItem(currentPosition);
        FragmentHomeSelectSubject fragmentHomeSelectSubject = (FragmentHomeSelectSubject) fragBaseFragments.get(1);
        fragmentHomeSelectSubject.setGrade(position);
    }

}
