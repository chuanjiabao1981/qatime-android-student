package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.bean.VideoCoursesDetailsBean;
import cn.qatime.player.config.UserPreferences;
import cn.qatime.player.fragment.FragmentVideoCoursesClassInfo;
import cn.qatime.player.fragment.FragmentVideoCoursesClassList;
import cn.qatime.player.fragment.FragmentVideoCoursesTeacherInfo;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.cache.UserInfoCache;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.OrderPayBean;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.SPUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.SimpleViewPagerIndicator;

/**
 * @author lungtify
 * @Time 2017/4/10 16:27
 * @Describe 视频课详情
 */

public class VideoCoursesActivity extends BaseFragmentActivity implements View.OnClickListener {
    private String[] mTitles;
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private ViewPager mViewPager;
    private int id;
    private VideoCoursesDetailsBean data;
    DecimalFormat df = new DecimalFormat("#.00");
    private TextView name;
    private TextView price;
    private TextView transferPrice;
    private TextView studentNumber;
    private RelativeLayout handleLayout;
    private Button auditionStart;
    private LinearLayout startStudyView;
    private Button startStudy;
    private AlertDialog alertDialog;
    private TextView freeTaste;
    private TextView joinCheap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_courses);
        id = getIntent().getIntExtra("id", 0);//联网id
        EventBus.getDefault().register(this);
        initView();
        initData();
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlVideoCourses + id, null,
                new VolleyListener(VideoCoursesActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), VideoCoursesDetailsBean.class);

                        if (data != null && data.getData() != null) {
                            handleLayout.setVisibility(View.VISIBLE);
                            name.setText(data.getData().getName());
                            setTitles(data.getData().getName());
                            studentNumber.setText("报名人数" + data.getData().getBuy_tickets_count());

                            if (data.getData().getSell_type().equals("charge")) {
                                String price;
                                if (Constant.CourseStatus.finished.equals(data.getData().getStatus()) || Constant.CourseStatus.completed.equals(data.getData().getStatus())) {
                                    price = df.format(data.getData().getPrice());
                                } else {
                                    price = df.format(data.getData().getCurrent_price());
                                }
                                if (price.startsWith(".")) {
                                    price = "0" + price;
                                }
                                VideoCoursesActivity.this.price.setText("￥" + price);
                                if (Constant.CourseStatus.teaching.equals(data.getData().getStatus())) {
                                    transferPrice.setVisibility(View.VISIBLE);
                                } else {
                                    transferPrice.setVisibility(View.GONE);
                                }
                                if (data.getData().getIs_tasting() || data.getData().isTasted()) {//显示进入试听按钮
                                    auditionStart.setVisibility(View.VISIBLE);
                                } else {
                                    auditionStart.setVisibility(View.GONE);
                                }

                                if (data.getData().getIs_bought()) {
                                    startStudyView.setVisibility(View.VISIBLE);
                                    if (data.getData().getStatus().equals("completed") || data.getData().getStatus().equals("finished")) {
                                        startStudy.setEnabled(false);
                                    }
                                }

                                if (Constant.CourseStatus.finished.equals(data.getData().getStatus()) || Constant.CourseStatus.completed.equals(data.getData().getStatus())) {
                                    handleLayout.setVisibility(View.GONE);//已结束的课程隐藏操作按钮
                                }

                            } else if (data.getData().getSell_type().equals("free")) {
                                transferPrice.setText("免费");
                                transferPrice.setVisibility(View.VISIBLE);
                                price.setVisibility(View.GONE);

                                startStudyView.setVisibility(View.VISIBLE);
                                if (data.getData().getStatus().equals("completed") || data.getData().getStatus().equals("finished")) {
                                    startStudy.setEnabled(false);
                                }
                            }

                            if (data.getData().getIcons() != null) {
                                if (!data.getData().getIcons().isFree_taste()) {
                                    freeTaste.setVisibility(View.GONE);
                                }
                                if (!data.getData().getIcons().isJoin_cheap()) {
                                    joinCheap.setVisibility(View.GONE);
                                }
                            }
                            ((FragmentVideoCoursesClassInfo) fragBaseFragments.get(0)).setData(data);
                            ((FragmentVideoCoursesTeacherInfo) fragBaseFragments.get(1)).setData(data);
                            ((FragmentVideoCoursesClassList) fragBaseFragments.get(2)).setData(data);

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

    private void initView() {
        freeTaste = (TextView) findViewById(R.id.free_taste);
        joinCheap = (TextView) findViewById(R.id.join_cheap);
        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        transferPrice = (TextView) findViewById(R.id.transfer_price);
        studentNumber = (TextView) findViewById(R.id.student_number);
        handleLayout = (RelativeLayout) findViewById(R.id.handle_layout);
        auditionStart = (Button) findViewById(R.id.audition_start);
        Button pay = (Button) findViewById(R.id.pay);
        startStudyView = (LinearLayout) findViewById(R.id.start_study_view);
        startStudy = (Button) findViewById(R.id.start_study);

        auditionStart.setOnClickListener(this);
        pay.setOnClickListener(this);
        startStudy.setOnClickListener(this);

        fragBaseFragments.add(new FragmentVideoCoursesClassInfo());
        fragBaseFragments.add(new FragmentVideoCoursesTeacherInfo());
        fragBaseFragments.add(new FragmentVideoCoursesClassList());

        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mTitles = new String[]{getString(R.string.remedial_detail), getString(R.string.teacher_detail), "课时安排"};
        mIndicator.setTitles(mTitles);

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return fragBaseFragments.get(position);
            }
        };

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mIndicator.select(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mIndicator.setOnItemClickListener(new SimpleViewPagerIndicator.OnItemClickListener() {
            @Override
            public void OnClick(int position) {
                mViewPager.setCurrentItem(position);
            }
        });
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        if (data == null || data.getData() == null) {
            return;
        }
        Intent intent;
        switch (v.getId()) {
            case R.id.audition_start:
                if (BaseApplication.isLogined()) {
                    if (!(data.getData().getIs_tasting()||data.getData().isTasted())) { //不在试听状态则加入试听
                        joinAudition();
                    }else{
                        intent = new Intent(VideoCoursesActivity.this, VideoCoursesPlayActivity.class);
                        intent.putExtra("id", data.getData().getId());
                        intent.putExtra("tasting",true);
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(VideoCoursesActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
            case R.id.start_study:
                if (BaseApplication.isLogined()) {
                    intent = new Intent(VideoCoursesActivity.this, VideoCoursesPlayActivity.class);
                    intent.putExtra("id", data.getData().getId());
                    intent.putExtra("tasting",false);
                    startActivity(intent);
                } else {
                    intent = new Intent(VideoCoursesActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
            case R.id.pay:
                if (BaseApplication.isLogined()) {
                    if ("teaching".equals(data.getData().getStatus())) {
                        if (alertDialog == null) {
                            View view = View.inflate(VideoCoursesActivity.this, R.layout.dialog_cancel_or_confirm, null);
                            Button cancel = (Button) view.findViewById(R.id.cancel);
                            Button confirm = (Button) view.findViewById(R.id.confirm);
                            TextView text = (TextView) view.findViewById(R.id.text);
                            text.setText(R.string.continue_buy_lasses_lesson);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                            confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    payRemedial();
                                    alertDialog.dismiss();
                                }
                            });
                            AlertDialog.Builder builder = new AlertDialog.Builder(VideoCoursesActivity.this);
                            alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.setContentView(view);
                        } else {
                            alertDialog.show();
                        }
                    } else {
                        payRemedial();
                    }
                } else {
                    intent = new Intent(VideoCoursesActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                break;
        }
    }

    private void payRemedial() {
        Intent intent = new Intent(VideoCoursesActivity.this, OrderConfirmActivity.class);
        intent.putExtra("courseType", "video");
        intent.putExtra("id", id);
        intent.putExtra("coupon", getIntent().getStringExtra("coupon"));
        OrderPayBean bean = new OrderPayBean();
        bean.name = data.getData().getName();
        bean.subject = data.getData().getSubject();
        bean.grade = data.getData().getGrade();
        bean.classnumber = data.getData().getPreset_lesson_count();
        bean.teacher = data.getData().getTeacher().getName();
        bean.current_price = data.getData().getCurrent_price();

        intent.putExtra("data", bean);
        startActivity(intent);
    }

    @Subscribe
    public void onEvent(PayResultState code) {
//        if (!StringUtils.isNullOrBlanK(event) && event.equals("pay_success")) {
//
//            finish();
//        }
        finish();
    }

    private void joinAudition() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST,UrlUtils.urlVideoCourses  + id + "/taste", null,
                new VolleyListener(VideoCoursesActivity.this) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        //已加入试听
                        data.getData().setIs_tasting(true);
                        Intent intent = new Intent(VideoCoursesActivity.this, VideoCoursesPlayActivity.class);
                        intent.putExtra("id", data.getData().getId());
                        intent.putExtra("tasting",true);
                        startActivity(intent);
                        if (StringUtils.isNullOrBlanK(BaseApplication.getAccount()) || StringUtils.isNullOrBlanK(BaseApplication.getAccountToken())) {
                            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", null,
                                    new VolleyListener(VideoCoursesActivity.this) {
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
//                                                            BaseApplication.clearToken();
                                                            Profile profile = BaseApplication.getProfile();
                                                            profile.getData().setRemember_token("");
                                                            SPUtils.putObject(VideoCoursesActivity.this, "profile", profile);
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
                    }

                    @Override
                    protected void onError(JSONObject response) {
//                            if(response.getJSONObject("error").getInt("code")==3004){//CourseTasteLimit
                        Toast.makeText(VideoCoursesActivity.this, R.string.the_course_not_support_audition, Toast.LENGTH_SHORT).show();
//                                            }
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                }

                , new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
