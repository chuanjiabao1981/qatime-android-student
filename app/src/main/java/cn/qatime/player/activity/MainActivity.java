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
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.fragment.Fragment1;
import cn.qatime.player.fragment.Fragment2;
import cn.qatime.player.fragment.Fragment3;
import cn.qatime.player.fragment.Fragment4;
import cn.qatime.player.im.manager.ReminderManager;
import cn.qatime.player.im.model.ReminderItem;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.FileUtil;
import libraryextra.utils.SPUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.FragmentLayout;

public class MainActivity extends BaseFragmentActivity {

    FragmentLayout fragmentlayout;
    public ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private int[] tab_img = {R.id.tab_img1, R.id.tab_img2, R.id.tab_img3, R.id.tab_img4};
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4};
    private int tabImages[][] = {
            {R.mipmap.tab_home_1, R.mipmap.tab_home_2},
            {R.mipmap.tab_moments_1, R.mipmap.tab_moments_2},
            {R.mipmap.tab_message_1, R.mipmap.tab_message_2},
            {R.mipmap.tab_person_1, R.mipmap.tab_person_2}};
    private int currentPosition = 0;

    /**
     * 当前用户信息
     */
//    public Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        refreshMedia();
        File file = new File(Constant.CACHEPATH);
        if (!file.mkdirs()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        GetGradeslist();
//        GetProvinceslist();
//        GetCitieslist();
//        GetSchoolslist();


//        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
//        registerMsgUnreadInfoObserver(true);
//        registerSystemMessageObservers(true);
//        requestSystemMessageUnreadCount();
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
        fragBaseFragments.add(new Fragment1());
        fragBaseFragments.add(new Fragment2());
        fragBaseFragments.add(new Fragment3());
        fragBaseFragments.add(new Fragment4());

        fragmentlayout = (FragmentLayout) findViewById(R.id.fragmentlayout);
        fragmentlayout.setScorllToNext(false);
        fragmentlayout.setScorll(false);
        fragmentlayout.setWhereTab(0);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayout.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                currentPosition = position;
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff858786);
                ((ImageView) lastTabView.findViewById(tab_img[lastPosition])).setImageResource(tabImages[lastPosition][1]);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xffeb6a4b);
                ((ImageView) currentTabView.findViewById(tab_img[position])).setImageResource(tabImages[position][0]);
                enableMsgNotification(false);
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout, 0x1000);
        fragmentlayout.getViewPager().setOffscreenPageLimit(3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_EXIT_LOGIN && resultCode == Constant.RESPONSE_EXIT_LOGIN) {
            finish();
        }
    }


    boolean flag = false;

    @Override
    public void onBackPressed() {
        if (!flag) {
            setResult(Constant.REGIST);
            Toast toast = Toast.makeText(this, getResources().getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            flag = true;
            new Handler().postDelayed(new Runnable() {
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
        Intent start = new Intent(this, LoginActivity.class);
        if (!StringUtils.isNullOrBlanK(intent.getStringExtra("sign"))) {
            start.putExtra("sign", intent.getStringExtra("sign"));
        }
        startActivity(start);
        finish();
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


    //省份列表
    public void GetProvinceslist() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlAppconstantInformation + "/provinces", null,
                new VolleyListener(MainActivity.this) {
                    @Override


                    protected void onSuccess(JSONObject response) {

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
        //TODO
//        addToRequestQueue(request);
    }

    //城市列表
    public void GetCitieslist() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlAppconstantInformation + "/cities", null,
                new VolleyListener(MainActivity.this) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        boolean value = FileUtil.writeFile(new ByteArrayInputStream(response.toString().getBytes()), getCacheDir().getAbsolutePath() + "/city.txt", true);
                        SPUtils.put(MainActivity.this, "city", value);
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
        //TODO
//        addToRequestQueue(request);
    }

    //学校列表
    public void GetSchoolslist() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlAppconstantInformation + "/schools", null,
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
        enableMsgNotification(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        enableMsgNotification(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        registerMsgUnreadInfoObserver(false);
//        registerSystemMessageObservers(false);
    }

    /**********************************************
     * 云信
     *******************************************************/

    private void enableMsgNotification(boolean enable) {
        boolean msg = (currentPosition != 2);
        if (enable | msg) {
            /**
             * 设置最近联系人的消息为已读
             *
             * @param account,    聊天对象帐号，或者以下两个值：
             *                    {@link #MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
             *                    {@link #MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
             */
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        } else {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        }
    }

    /**
     * 注册未读消息数量观察者
     */
//    private void registerMsgUnreadInfoObserver(boolean register) {
//        if (register) {
//            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
//        } else {
//            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
//        }
//    }

    /**
     * 注册/注销系统消息未读数变化
     *
     * @param register
     */
//    private void registerSystemMessageObservers(boolean register) {
//        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver, register);
//    }
//
//    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
//        @Override
//        public void onEvent(Integer unreadCount) {
////            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
//            ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
//        }
//    };
//
//    /**
//     * 查询系统消息未读数
//     */
//    private void requestSystemMessageUnreadCount() {
//        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
////        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
//        ReminderManager.getInstance().updateContactUnreadNum(unread);
//
//    }
//
//    /**
//     * 未读消息实现
//     *
//     * @param item
//     */
//    @Override
//    public void onUnreadNumChanged(ReminderItem item) {
//        Logger.e(item.getUnread() + "");
//    }
}
