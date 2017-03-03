package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.config.UserPreferences;
import cn.qatime.player.fragment.FragmentClassDetailClassInfo;
import cn.qatime.player.fragment.FragmentClassDetailClassList;
import cn.qatime.player.fragment.FragmentClassDetailTeacherInfo;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.cache.UserInfoCache;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.OrderPayBean;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.SPUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.SimpleViewPagerIndicator;

public class RemedialClassDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    ImageView image;
    private int id;
    private String[] mTitles;
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private Button audition;
    private TextView name;
    private TextView title;
    private RemedialClassDetailBean data;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private int pager = 0;
    TextView price;
    TextView studentnumber;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    DecimalFormat df = new DecimalFormat("#.00");
    private AlertDialog alertDialog;
    private Button startStudy;
    private View startStudyView;
    private View rlImage;
    private TextView progress;
    private TextView status;
    private TextView timeToStart;
    private View layoutView;
    private Button auditionStart;
    private TextView transferPrice;
    private View handleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedial_class_detail);

        id = getIntent().getIntExtra("id", 0);//联网id
        pager = getIntent().getIntExtra("pager", 0);
        initView();
        if (id == 0) {
            Toast.makeText(this, getResources().getString(R.string.no_tutorial_classes_ID), Toast.LENGTH_SHORT).show();
            return;
        }
        initData();

    }


    public void initView() {
        EventBus.getDefault().register(this);
        image = (ImageView) findViewById(R.id.image);
        rlImage = findViewById(R.id.rl_image);
        name = (TextView) findViewById(R.id.name);
        rlImage.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenWidth(this) * 5 / 8));

        fragBaseFragments.add(new FragmentClassDetailClassInfo());
        fragBaseFragments.add(new FragmentClassDetailTeacherInfo());
        fragBaseFragments.add(new FragmentClassDetailClassList());

        audition = (Button) findViewById(R.id.audition);
        auditionStart = (Button) findViewById(R.id.audition_start);
        Button pay = (Button) findViewById(R.id.pay);
        startStudy = (Button) findViewById(R.id.start_study);
        startStudyView = findViewById(R.id.start_study_view);
        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        transferPrice = (TextView) findViewById(R.id.transfer_price);
        studentnumber = (TextView) findViewById(R.id.student_number);
        progress = (TextView) findViewById(R.id.progress);
        timeToStart = (TextView) findViewById(R.id.time_to_start);
        status = (TextView) findViewById(R.id.status);
        layoutView = findViewById(R.id.layout_view);
        handleLayout = findViewById(R.id.handle_layout);
        audition.setOnClickListener(this);
        auditionStart.setOnClickListener(this);
        pay.setOnClickListener(this);
        startStudy.setOnClickListener(this);

        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mTitles = new String[]{getString(R.string.remedial_detail), getString(R.string.teacher_info), getString(R.string.course_arrangement)};
        mIndicator.setTitles(mTitles);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return fragBaseFragments.get(position);
            }
        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(pager);
        mViewPager.setOffscreenPageLimit(2);
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
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlRemedialClass + "/" + id, null,
                new VolleyListener(RemedialClassDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), RemedialClassDetailBean.class);

                        if (data.getData() != null && data.getData().getLive_start_time() != null) {

                            Glide.with(getApplicationContext()).load(data.getData().getPublicize()).placeholder(R.mipmap.photo).fitCenter().crossFade().into(image);
                            status.setText(getStatus(data.getData().getStatus()));
                            name.setText(data.getData().getName());
                            title.setText(data.getData().getName());
                            studentnumber.setText(getString(R.string.student_number, data.getData().getBuy_tickets_count()));
                            String price;
                            if (Constant.CourseStatus.finished.equals(data.getData().getStatus()) || Constant.CourseStatus.completed.equals(data.getData().getStatus())) {
                                price = df.format(data.getData().getPrice());
                            } else {
                                price = df.format(data.getData().getCurrent_price());
                            }
                            if (price.startsWith(".")) {
                                price = "0" + price;
                            }
                            RemedialClassDetailActivity.this.price.setText("￥" + price);
                            if (Constant.CourseStatus.teaching.equals(data.getData().getStatus())) {
                                transferPrice.setVisibility(View.VISIBLE);
                            } else {
                                transferPrice.setVisibility(View.GONE);
                            }


                            try {
                                if ("init".equals(data.getData().getStatus()) || "published".equals(data.getData().getStatus())) {
                                    long time = parse.parse(data.getData().getLive_start_time()).getTime() - System.currentTimeMillis();
                                    int value = 0;
                                    if (time > 0) {
                                        value = (int) (time / (1000 * 3600 * 24));
                                    }
                                    timeToStart.setVisibility(View.VISIBLE);
                                    progress.setVisibility(View.GONE);
                                    if (value != 0) {
                                        timeToStart.setText("[" + getResources().getString(R.string.item_to_start_main) + value + getResources().getString(R.string.item_day) + "]");
                                    } else {
                                        timeToStart.setText(R.string.ready_to_start);
                                    }
                                    layoutView.setBackgroundColor(0xff00d564);
                                } else if ("teaching".equals(data.getData().getStatus())) {
                                    progress.setVisibility(View.VISIBLE);
                                    timeToStart.setVisibility(View.GONE);
                                    layoutView.setBackgroundColor(0xff00a0e9);
                                    progress.setText(getString(R.string.progress, data.getData().getCompleted_lesson_count(), data.getData().getPreset_lesson_count()));
                                } else if (Constant.CourseStatus.finished.equals(data.getData().getStatus()) || Constant.CourseStatus.completed.equals(data.getData().getStatus())) {
                                    handleLayout.setVisibility(View.GONE);//已结束的课程隐藏操作按钮
                                    progress.setVisibility(View.VISIBLE);
                                    timeToStart.setVisibility(View.GONE);
                                    layoutView.setBackgroundColor(0xff999999);
                                    progress.setText(getString(R.string.progress, data.getData().getCompleted_lesson_count(), data.getData().getPreset_lesson_count()));
                                } else {
                                    layoutView.setVisibility(View.GONE);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            ((FragmentClassDetailClassInfo) fragBaseFragments.get(0)).setData(data);
                            ((FragmentClassDetailTeacherInfo) fragBaseFragments.get(1)).setData(data);
                            ((FragmentClassDetailClassList) fragBaseFragments.get(2)).setData(data);

                            if (data.getData().getIs_tasting() || data.getData().isTasted()) {//显示进入试听按钮
                                auditionStart.setVisibility(View.VISIBLE);
                                audition.setVisibility(View.GONE);
                                if (data.getData().isTasted()) {
                                    auditionStart.setText(getResourceString(R.string.audition_over));
                                    auditionStart.setEnabled(false);
                                }
                            } else {//显示加入试听按钮
                                audition.setText(getResources().getString(R.string.Join_the_audition));
                                auditionStart.setVisibility(View.GONE);
//                                if(data.getData().getTaste_count() == 0){//试听数目为0则该课不支持试听
//                                    audition.setEnabled(false);
//                                }
                            }

                            if (data.getData().getIs_bought()) {
                                startStudyView.setVisibility(View.VISIBLE);
                                if (data.getData().getStatus().equals("completed") || data.getData().getStatus().equals("finished")) {
                                    startStudy.setEnabled(false);
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
    public void onClick(View v) {
        if (data == null || data.getData() == null) {
            return;
        }
        Intent intent;
        switch (v.getId()) {
            case R.id.audition_start:
                if (BaseApplication.isLogined()) {
                    if ("init".equals(data.getData().getStatus()) || "published".equals(data.getData().getStatus())) {
                        Toast.makeText(this, getString(R.string.published_course_unable_enter) + getString(R.string.audition), Toast.LENGTH_SHORT).show();
                    } else {
                        intent = new Intent(RemedialClassDetailActivity.this, NEVideoPlayerActivity.class);
//                    intent.putExtra("camera", data.getData().getCamera());
//                    intent.putExtra("board", data.getData().getBoard());
                        intent.putExtra("id", data.getData().getId());
                        intent.putExtra("sessionId", data.getData().getChat_team_id());
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(RemedialClassDetailActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
            case R.id.audition:
                if (BaseApplication.isLogined()) {
                    joinAudition();
                } else {
                    intent = new Intent(RemedialClassDetailActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
            case R.id.start_study:
                if (BaseApplication.isLogined()) {
                    if ("init".equals(data.getData().getStatus()) || "published".equals(data.getData().getStatus())) {
                        Toast.makeText(this, getString(R.string.published_course_unable_enter) + getString(R.string.study), Toast.LENGTH_SHORT).show();
                    } else {
                        intent = new Intent(RemedialClassDetailActivity.this, NEVideoPlayerActivity.class);
//                    intent.putExtra("camera", data.getData().getCamera());
//                    intent.putExtra("board", data.getData().getBoard());
                        intent.putExtra("id", data.getData().getId());
                        intent.putExtra("sessionId", data.getData().getChat_team_id());
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(RemedialClassDetailActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
            case R.id.pay:
                if (BaseApplication.isLogined()) {
                    if ("teaching".equals(data.getData().getStatus())) {
                        if (alertDialog == null) {
                            View view = View.inflate(RemedialClassDetailActivity.this, R.layout.dialog_cancel_or_confirm, null);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(RemedialClassDetailActivity.this);
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
                    intent = new Intent(RemedialClassDetailActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                break;
        }
    }

    private void payRemedial() {
        Intent intent = new Intent(RemedialClassDetailActivity.this, OrderConfirmActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("coupon", getIntent().getStringExtra("coupon"));
        OrderPayBean bean = new OrderPayBean();
        bean.image = data.getData().getPublicize();
        bean.name = data.getData().getName();
        bean.subject = data.getData().getSubject();
        bean.grade = data.getData().getGrade();
        bean.classnumber = data.getData().getPreset_lesson_count();
        bean.teacher = data.getData().getTeacher().getName();
        bean.classendtime = data.getData().getLive_end_time();
        bean.status = data.getData().getStatus();
        bean.classstarttime = data.getData().getLive_start_time();
        bean.current_price = data.getData().getCurrent_price();

        intent.putExtra("data", bean);
        startActivity(intent);
    }

    private void joinAudition() {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass + "/" + id + "/taste", map), null,
                new VolleyListener(RemedialClassDetailActivity.this) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        //已加入试听
                        data.getData().setIs_tasting(true);
                        auditionStart.setVisibility(View.VISIBLE);
                        if (StringUtils.isNullOrBlanK(BaseApplication.getAccount()) || StringUtils.isNullOrBlanK(BaseApplication.getAccountToken())) {
                            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", null,
                                    new VolleyListener(RemedialClassDetailActivity.this) {
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
                                                            SPUtils.putObject(RemedialClassDetailActivity.this, "profile", profile);
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
                        Toast.makeText(RemedialClassDetailActivity.this, R.string.the_course_not_support_audition, Toast.LENGTH_SHORT).show();
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

    private String getStatus(String status) {
        if (status == null) {
            return getString(R.string.recruiting);
        }
        if (status.equals("published")) {
            return getString(R.string.recruiting);
        } else if (status.equals("init")) {
            return getString(R.string.recruiting);
        } else if (status.equals("teaching")) {
            return getString(R.string.teaching);
        } else if (status.equals(Constant.CourseStatus.completed) || status.equals(Constant.CourseStatus.finished)) {//未开始
            return getString(R.string.completed);
        }
        return getString(R.string.recruiting);
    }

    @Subscribe
    public void onEvent(PayResultState code) {
//        if (!StringUtils.isNullOrBlanK(event) && event.equals("pay_success")) {
//
//            finish();
//        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    }
}
