package cn.qatime.player.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.BusEvent;
import cn.qatime.player.bean.PayResultState;
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
import cn.qatime.player.qrcore.core.CaptureActivity;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.LogCatHelper;
import cn.qatime.player.utils.UrlUtils;
import io.vov.vitamio.utils.FileUtils;
import libraryextra.bean.CashAccountBean;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.bean.SystemNotifyBean;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
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
        if (BaseApplication.getInstance().isLogined()) {
            int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
            Logger.e("unreadNum" + unreadNum);
            message_x.setVisibility(unreadNum == 0 ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
        } else if (requestCode == 2) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "未取得相机权限", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, Constant.REQUEST);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkUserInfo();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        initView();
        EventBus.getDefault().register(this);
        //  注册/注销观察者
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, true);
        refreshUnreadNum();
        refreshMedia();

        File file = new File(Constant.CACHEPATH);
        FileUtils.deleteDir(file);

        parseIntent();
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, true);
//        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(clientsObserver, true);
    }


    /**
     * 检查用户信息是否完整
     */
    private void checkUserInfo() {
        if (BaseApplication.getInstance().isLogined()) {
            String name = BaseApplication.getInstance().getUserName();
            if (StringUtils.isNullOrBlanK(name)) {
                Toast.makeText(MainActivity.this, getResourceString(R.string.please_set_information), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, RegisterPerfectActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
                finish();
            }
        }
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

        if (BaseApplication.getInstance().isLogined()) {
            fragBaseFragments.add(new FragmentHomeClassTable());
            fragBaseFragments.add(new FragmentHomeMessage());
            fragBaseFragments.add(new FragmentHomeUserCenter());
            initMessage();
            refreshCashAccount();
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
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(getResources().getColor(R.color.colorPrimary));
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
                    NIMClient.getService(MsgService.class).setChattingAccount(BaseApplication.getInstance().isChatMessageNotifyStatus() ? MsgService.MSG_CHATTING_ACCOUNT_NONE : MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (position == 4) {
                        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout, 0x1000);
        fragmentlayout.getViewPager().setOffscreenPageLimit(4);
        message_x = fragmentlayout.getTabLayout().findViewById(R.id.message_x);


    }


    private void initMessage() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(BaseApplication.getInstance().getUserId()));
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlUser + BaseApplication.getInstance().getUserId() + "/notifications", map), null,
                new VolleyListener(MainActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        SystemNotifyBean data = JsonUtils.objectFromJson(response.toString(), SystemNotifyBean.class);
                        if (data != null && data.getData() != null) {
                            for (SystemNotifyBean.DataBean bean : data.getData()) {
                                if (!bean.isRead()) {//有未读发送未读event
                                    EventBus.getDefault().postSticky(BusEvent.HANDLE_U_PUSH_MESSAGE);
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
            // TODO: 2017/4/17 判断优惠码跳转?
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
        checkUserInfo();
        if (!StringUtils.isNullOrBlanK(intent.getStringExtra("out")) || (!StringUtils.isNullOrBlanK(intent.getStringExtra("sign")))) {
            Intent start = new Intent(this, LoginActivity.class);
            if (!StringUtils.isNullOrBlanK(intent.getStringExtra("sign"))) {
                start.putExtra("sign", intent.getStringExtra("sign"));
            }
            BaseApplication.getInstance().clearToken();
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
        } else if (!StringUtils.isNullOrBlanK(getIntent().getStringExtra("kickOut"))) {
            BaseApplication.getInstance().clearToken();
            View view = View.inflate(this, R.layout.dialog_confirm, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            alertDialog = builder.create();
            TextView text = (TextView) view.findViewById(R.id.text);
            text.setText(getResourceString(R.string.login_has_expired));
            Button confirm = (Button) view.findViewById(R.id.confirm);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    Intent start = new Intent(MainActivity.this, LoginActivity.class);
                    BaseApplication.getInstance().clearToken();
                    startActivity(start);
                    finish();
                }
            });
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Intent start = new Intent(MainActivity.this, LoginActivity.class);
                    BaseApplication.getInstance().clearToken();
                    startActivity(start);
                    finish();
                }
            });
            alertDialog.show();
            alertDialog.setContentView(view);
        } else {
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
        NIMClient.getService(MsgService.class).setChattingAccount(BaseApplication.getInstance().isChatMessageNotifyStatus() ? MsgService.MSG_CHATTING_ACCOUNT_NONE : MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
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
//        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(clientsObserver, false);
    }

    /**
     * 监听用户在线状态
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
//                Intent intent = new Intent(BaseApplication.getInstance().getTopActivity(), MainActivity.class);
//                intent.putExtra("kickOut", "kickOut");
//                startActivity(intent);
//                Toast.makeText(MainActivity.this, "userStatus未登录成功", Toast.LENGTH_SHORT).show();
                Logger.e("userStatus未登录成功");
            } else {
                if (code == StatusCode.NET_BROKEN) {
                    Logger.e("userStatus当前网络不可用");
                } else if (code == StatusCode.UNLOGIN) {
                    Logger.e("userStatus未登录");
                } else if (code == StatusCode.CONNECTING) {
                    Logger.e("userStatus连接中...");
                } else if (code == StatusCode.LOGINING) {
                    Logger.e("userStatus登录中...");
                } else {
//                    onRecentContactsLoaded();
                    Logger.e("userStatus。。" + code);
                }
            }
        }
    };
//    Observer<List<OnlineClient>> clientsObserver = new Observer<List<OnlineClient>>() {
//        @Override
//        public void onEvent(List<OnlineClient> onlineClients) {
//            if (onlineClients != null && onlineClients.size() > 0) {
//                OnlineClient client = onlineClients.get(0);
//                switch (client.getClientType()) {
//                    case ClientType.Windows:
//                        break;
//                    case ClientType.Web:
//                        break;
//                    case ClientType.iOS:
//                    case ClientType.Android:
////                        NIMClient.getService(AuthService.class).kickOtherClient(client);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }
//    };

    @Subscribe
    public void onEvent(BusEvent event) {
        if (event == BusEvent.PAY_SUCCESS) {
            if (StringUtils.isNullOrBlanK(BaseApplication.getInstance().getAccount()) || StringUtils.isNullOrBlanK(BaseApplication.getInstance().getAccountToken())) {
                getAccount();
            }
        } else if (event == BusEvent.HANDLE_U_PUSH_MESSAGE) {
            if (fragmentlayout.getCurrentPosition() != 3) {
                message_x.setVisibility(View.VISIBLE);
            }
        } else if (event == BusEvent.REFRESH_CASH_ACCOUNT) {
            refreshCashAccount();
        }

    }

    /**
     * 当用户没有云信账号,第一次直接购买后,需从后台获取一次云信账号
     */
    private void getAccount() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getInstance().getUserId() + "/info", null,
                new VolleyListener(MainActivity.this) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        PersonalInformationBean bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);
                        if (bean != null && bean.getData() != null && bean.getData().getChat_account() != null) {
                            Profile profile = BaseApplication.getInstance().getProfile();
                            profile.getData().getUser().setChat_account(bean.getData().getChat_account());
                            BaseApplication.getInstance().setProfile(profile);

                            String account = BaseApplication.getInstance().getAccount();
                            String token = BaseApplication.getInstance().getAccountToken();

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
                                        BaseApplication.getInstance().clearToken();
                                    }

                                    @Override
                                    public void onException(Throwable throwable) {
                                        Logger.e(throwable.getMessage());
                                        BaseApplication.getInstance().clearToken();
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


    public void setCurrentPosition(int currentPosition, int position) {
        fragmentlayout.setCurrenItem(currentPosition);
        FragmentHomeSelectSubject fragmentHomeSelectSubject = (FragmentHomeSelectSubject) fragBaseFragments.get(1);
        fragmentHomeSelectSubject.setGrade(position);
    }

    @Subscribe
    public void onEvent(PayResultState state) {
        refreshCashAccount();
    }

    private void refreshCashAccount() {
        if (BaseApplication.getInstance().isLogined()) {
            addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.urlpayment + BaseApplication.getInstance().getUserId() + "/cash", null, new VolleyListener(MainActivity.this) {

                @Override
                protected void onTokenOut() {

                }

                @Override
                protected void onSuccess(JSONObject response) {
                    CashAccountBean cashAccount = JsonUtils.objectFromJson(response.toString(), CashAccountBean.class);
                    BaseApplication.getInstance().setCashAccount(cashAccount);
                    EventBus.getDefault().post(BusEvent.ON_REFRESH_CASH_ACCOUNT);
                }

                @Override
                protected void onError(JSONObject response) {
                    Toast.makeText(MainActivity.this, getResourceString(R.string.get_wallet_info_error), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MainActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }));
        }
    }

}
