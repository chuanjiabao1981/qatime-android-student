package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
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
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.LiveLessonDetailBean;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.config.UserPreferences;
import cn.qatime.player.fragment.FragmentClassDetailClassInfo;
import cn.qatime.player.fragment.FragmentClassDetailClassList;
import cn.qatime.player.fragment.FragmentClassDetailTeacherInfo;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.cache.UserInfoCache;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.ShareUtil;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.SimpleViewPagerIndicator;
import libraryextra.bean.OrderPayBean;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.DateUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class RemedialClassDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    private int id;
    private String[] mTitles;
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private Button audition;
    private TextView name;
    private LiveLessonDetailBean data;
    private ViewPager mViewPager;
    private int pager = 0;
    TextView price;
    TextView studentnumber;
    DecimalFormat df = new DecimalFormat("#.00");
    private AlertDialog alertDialog;
    private Button startStudy;
    private View startStudyView;
    private Button auditionStart;
    private TextView transferPrice;
    private View handleLayout;
//    private TextView refundAnyTime;
    private TextView freeTaste;
    private TextView couponFree;
//    private TextView joinCheap;

    private TextView progress;
    private TextView status;
    private TextView timeToStart;
    private View layoutView;
    private RelativeLayout auditionLayout;
    private PopupWindow pop;
    private RemedialClassDetailBean playInfo;

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
        name = (TextView) findViewById(R.id.name);

        fragBaseFragments.add(new FragmentClassDetailClassInfo());
        fragBaseFragments.add(new FragmentClassDetailTeacherInfo());
        fragBaseFragments.add(new FragmentClassDetailClassList());

//        refundAnyTime = (TextView) findViewById(R.id.refund_any_time);
        freeTaste = (TextView) findViewById(R.id.free_taste);
        couponFree = (TextView) findViewById(R.id.coupon_free);
//        joinCheap = (TextView) findViewById(R.id.join_cheap);
        progress = (TextView) findViewById(R.id.progress);
        timeToStart = (TextView) findViewById(R.id.time_to_start);
        status = (TextView) findViewById(R.id.status);
        layoutView = findViewById(R.id.layout_view);

        audition = (Button) findViewById(R.id.audition);
        auditionStart = (Button) findViewById(R.id.audition_start);
        auditionLayout = (RelativeLayout) findViewById(R.id.audition_layout);
        Button pay = (Button) findViewById(R.id.pay);
        startStudy = (Button) findViewById(R.id.start_study);
        startStudyView = findViewById(R.id.start_study_view);
        price = (TextView) findViewById(R.id.price);
        transferPrice = (TextView) findViewById(R.id.transfer_price);
        studentnumber = (TextView) findViewById(R.id.student_number);
        handleLayout = findViewById(R.id.handle_layout);
        audition.setOnClickListener(this);
        auditionStart.setOnClickListener(this);
        pay.setOnClickListener(this);
        startStudy.setOnClickListener(this);

        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mTitles = new String[]{getString(R.string.remedial_detail), getString(R.string.teacher_detail), getString(R.string.course_arrangement)};
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
        mViewPager.setCurrentItem(pager);
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == 0) {
                    Toast.makeText(RemedialClassDetailActivity.this, "id为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ShareUtil.getInstance(RemedialClassDetailActivity.this, UrlUtils.getBaseUrl() + "live_studio/courses/" + id, name.getText().toString(), "直播课课程", new ShareUtil.ShareListener() {
                    @Override
                    public void onSuccess(SHARE_MEDIA platform) {

                    }

                }).open();
            }
        });
    }

    /**
     * @param tasteOrBought 已购买或已试听
     * @param status        课程状态
     */
    private void initMenu(boolean tasteOrBought, String status) {
        if (pop == null) {
            setRightImage(R.mipmap.exclusive_menu, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop.showAsDropDown(v);
                    backgroundAlpha(0.9f);
                }
            });
            View popView = View.inflate(this, R.layout.course_detail_pop_menu, null);
            View menu1 = popView.findViewById(R.id.menu_1);
            View menu2 = popView.findViewById(R.id.menu_2);
            View menu3 = popView.findViewById(R.id.menu_3);
            View menu4 = popView.findViewById(R.id.menu_4);
            View menu5 = popView.findViewById(R.id.menu_5);
            if (!tasteOrBought) {
                menu1.setVisibility(View.GONE);
            }
            if (Constant.CourseStatus.completed.equals(status)) {
                menu1.setVisibility(View.GONE);
            }
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);
            menu1.setOnClickListener(this);
            menu5.setOnClickListener(this);
            pop = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1);
                }
            });
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlCourses + id + "/detail", null,
                new VolleyListener(RemedialClassDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), LiveLessonDetailBean.class);
                        setData();
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
        DaYiJsonObjectRequest requestPlay = new DaYiJsonObjectRequest(UrlUtils.urlCourses + id + "/play_info", null,
                new VolleyListener(RemedialClassDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        playInfo = JsonUtils.objectFromJson(response.toString(), RemedialClassDetailBean.class);

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
        addToRequestQueue(requestPlay);
    }

    private void setData() {
        if (data != null && data.getData() != null && data.getData().getCourse() != null) {
            handleLayout.setVisibility(View.VISIBLE);
            status.setText(getStatus(data.getData().getCourse().getStatus()));
            name.setText(data.getData().getCourse().getName());
            setTitles(data.getData().getCourse().getName());
            studentnumber.setText(getString(R.string.student_number, data.getData().getCourse().getBuy_tickets_count()));

            if (Constant.CourseStatus.published.equals(data.getData().getCourse().getStatus())) {
                layoutView.setBackgroundColor(0xff00d564);
                int value = 0;
                try {
                    value = DateUtils.daysBetween(data.getData().getCourse().getLive_start_time(), System.currentTimeMillis());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                progress.setVisibility(View.GONE);
                if (value > 0) {
                    timeToStart.setVisibility(View.VISIBLE);
                    timeToStart.setText("[" + getResources().getString(R.string.item_to_start_main) + value + getResources().getString(R.string.item_day) + "]");
                } else {
                    timeToStart.setVisibility(View.GONE);
                }
            } else if (Constant.CourseStatus.teaching.equals(data.getData().getCourse().getStatus())) {
                layoutView.setBackgroundColor(0xff00a0e9);
                timeToStart.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                progress.setText(getString(R.string.progress_live, data.getData().getCourse().getClosed_lessons_count(), data.getData().getCourse().getLessons_count()));
            } else if (Constant.CourseStatus.completed.equals(data.getData().getCourse().getStatus())) {
                layoutView.setBackgroundColor(0xff999999);
                timeToStart.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                progress.setText(getString(R.string.progress_live, data.getData().getCourse().getClosed_lessons_count(), data.getData().getCourse().getLessons_count()));
            } else {
                layoutView.setVisibility(View.GONE);
            }

            if (data.getData().getCourse().getSell_type().equals("charge")) {
                String priceStr;
                if (Constant.CourseStatus.completed.equals(data.getData().getCourse().getStatus())) {
                    priceStr = df.format(data.getData().getCourse().getPrice());
                } else {
                    priceStr = df.format(data.getData().getCourse().getCurrent_price());
                }
                if (priceStr.startsWith(".")) {
                    priceStr = "0" + priceStr;
                }
                price.setText("￥" + priceStr);
                if (Constant.CourseStatus.teaching.equals(data.getData().getCourse().getStatus())) {
                    transferPrice.setVisibility(View.VISIBLE);
                } else {
                    transferPrice.setVisibility(View.GONE);
                }

                if (data.getData().getTicket() != null && "LiveStudio::BuyTicket".equals(data.getData().getTicket().getType())) {//已购买
                    initMenu(true, data.getData().getCourse().getStatus());
                    if (Constant.CourseStatus.completed.equals(data.getData().getCourse().getStatus())) {
                        handleLayout.setVisibility(View.GONE);
                    } else {
                        startStudyView.setVisibility(View.VISIBLE);//开始学习
                        startStudy.setText("开始学习");
                    }
                } else {//未购买
                    if (data.getData().getCourse().isOff_shelve() || Constant.CourseStatus.completed.equals(data.getData().getCourse().getStatus())) {//已下架或未购买已结束显示已下架
                        initMenu(false, data.getData().getCourse().getStatus());
                        handleLayout.setVisibility(View.GONE);
                        transferPrice.setVisibility(View.GONE);
                        price.setText("已下架");
                    } else {
                        if (data.getData().getTicket() == null || StringUtils.isNullOrBlanK(data.getData().getTicket().getType())) {//未试听
                            initMenu(false, data.getData().getCourse().getStatus());
                            if (data.getData().getCourse().isTastable()) {//可以加入试听
                                audition.setVisibility(View.VISIBLE);
                                auditionStart.setVisibility(View.GONE);
                            } else {//不可试听  只能购买
                                auditionLayout.setVisibility(View.GONE);
                            }
                        } else {//已加入试听
                            audition.setVisibility(View.GONE);
                            auditionStart.setVisibility(View.VISIBLE);
                            if (data.getData().getTicket().getUsed_count() >= data.getData().getTicket().getBuy_count()) {
                                initMenu(false, data.getData().getCourse().getStatus());
                                auditionStart.setText("试听结束");
                                auditionStart.setEnabled(false);
                            } else {
                                initMenu(true, data.getData().getCourse().getStatus());
                            }
                        }
                    }
                }
            } else if (data.getData().getCourse().getSell_type().equals("free")) {
                price.setText("免费");
                if (data.getData().getTicket() != null) {//已购买
                    initMenu(true, data.getData().getCourse().getStatus());
                    if (Constant.CourseStatus.completed.equals(data.getData().getCourse().getStatus())) {
                        handleLayout.setVisibility(View.GONE);
                    } else {
                        startStudy.setText("开始学习");
                        startStudyView.setVisibility(View.VISIBLE);
                    }
                } else {
                    initMenu(false, data.getData().getCourse().getStatus());
                    if (data.getData().getCourse().isOff_shelve() || Constant.CourseStatus.completed.equals(data.getData().getCourse().getStatus())) {
                        price.setText("已下架");
                        handleLayout.setVisibility(View.GONE);
                    } else {
                        startStudyView.setVisibility(View.VISIBLE);
                        startStudy.setBackgroundResource(R.drawable.button_bg_selector_red_with_stork);
                        startStudy.setTextColor(getResources().getColor(R.color.colorPrimary));
                        startStudy.setText("立即报名");
                    }
                }
            }

            if (data.getData().getCourse().getIcons() != null) {
                if (!data.getData().getCourse().getIcons().isCoupon_free()) {
                    couponFree.setVisibility(View.GONE);
                }
//                if (!data.getData().getCourse().getIcons().isRefund_any_time()) {
//                    refundAnyTime.setVisibility(View.GONE);
//                }
                if (!data.getData().getCourse().getIcons().isFree_taste()) {
                    freeTaste.setVisibility(View.GONE);
                }
//                if (!data.getData().getCourse().getIcons().isJoin_cheap()) {
//                    joinCheap.setVisibility(View.GONE);
//                }
            }
            ((FragmentClassDetailClassInfo) fragBaseFragments.get(0)).setData(data);
            ((FragmentClassDetailTeacherInfo) fragBaseFragments.get(1)).setData(data);
            ((FragmentClassDetailClassList) fragBaseFragments.get(2)).setData(data);

        }
    }

    @Override
    public void onClick(View v) {
        if (data == null || data.getData() == null) {
            return;
        }
        Intent intent;
        switch (v.getId()) {
            case R.id.audition_start://进入试听
                if (BaseApplication.getInstance().isLogined()) {
                    if (Constant.CourseStatus.published.equals(data.getData().getCourse().getStatus())) {
                        Toast.makeText(this, getString(R.string.published_course_unable_enter) + getString(R.string.audition), Toast.LENGTH_SHORT).show();
                    } else {
                        intent = new Intent(RemedialClassDetailActivity.this, NEVideoPlayerActivity.class);
                        intent.putExtra("id", data.getData().getCourse().getId());
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(RemedialClassDetailActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
            case R.id.audition://加入试听
                if (BaseApplication.getInstance().isLogined()) {
                    if (data.getData().getCourse().isTaste_overflow()) {
                        Toast.makeText(this, "该试听已失效,请直接购买", Toast.LENGTH_SHORT).show();
                    } else {
                        joinAudition();
                    }
                } else {
                    intent = new Intent(RemedialClassDetailActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
            case R.id.start_study:
                if (BaseApplication.getInstance().isLogined()) {
                    if (data.getData().getCourse() != null) {
                        if (data.getData().getCourse().getSell_type().equals("charge") //收费已购买
                                || (data.getData().getCourse().getSell_type().equals("free") && data.getData().getTicket() != null)) {//免费已加入
                            if (Constant.CourseStatus.published.equals(data.getData().getCourse().getStatus())) {
                                Toast.makeText(this, getString(R.string.published_course_unable_enter) + getString(R.string.study), Toast.LENGTH_SHORT).show();
                            } else {
                                intent = new Intent(RemedialClassDetailActivity.this, NEVideoPlayerActivity.class);
                                intent.putExtra("id", data.getData().getCourse().getId());
                                startActivity(intent);
                            }
                        } else {//免费,加入我的直播课
                            free2deliver();
                        }
                    }
                } else {
                    intent = new Intent(RemedialClassDetailActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
            case R.id.pay:
                if (BaseApplication.getInstance().isLogined()) {
                    if (Constant.CourseStatus.teaching.equals(data.getData().getCourse().getStatus())) {
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
            case R.id.menu_1:

                if (playInfo == null || playInfo.getData() == null || StringUtils.isNullOrBlanK(playInfo.getData().getChat_team_id())) {
                    Toast.makeText(this, "id为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(RemedialClassDetailActivity.this, MessageActivity.class);
                intent.putExtra("sessionId", playInfo.getData().getChat_team_id());
                intent.putExtra("sessionType", SessionTypeEnum.None);
                intent.putExtra("courseId", id);
                intent.putExtra("name", data.getData().getCourse().getName());
                intent.putExtra("type", "custom");
                startActivity(intent);
                pop.dismiss();
                break;
            case R.id.menu_5:
                intent = new Intent(RemedialClassDetailActivity.this, MembersActivity.class);
                intent.putExtra("members", playInfo.getData().getChat_team());
                startActivity(intent);
                pop.dismiss();
                break;

        }
    }

    //免费,加入
    private void free2deliver() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.urlCourses + id + "/deliver_free", null,
                new VolleyListener(RemedialClassDetailActivity.this) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        startStudy.setBackgroundResource(R.drawable.button_bg_selector_red);
                        startStudy.setTextColor(Color.WHITE);
                        Toast.makeText(RemedialClassDetailActivity.this, "已成功添加至我的直播课", Toast.LENGTH_SHORT).show();
//                        data.getData().setTicket(new LiveLessonDetailBean.DataBean.TicketBean("LiveStudio::BuyTicket"));
//                        startStudy.setText("开始学习");
                        initData();
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

    private void payRemedial() {
        Intent intent = new Intent(RemedialClassDetailActivity.this, OrderConfirmActivity.class);
        intent.putExtra("courseType", "live");
        intent.putExtra("id", id);
        intent.putExtra("coupon", getIntent().getStringExtra("coupon"));
        OrderPayBean bean = new OrderPayBean();
        bean.name = data.getData().getCourse().getName();
        bean.subject = data.getData().getCourse().getSubject();
        bean.grade = data.getData().getCourse().getGrade();
        bean.classnumber = data.getData().getCourse().getPreset_lesson_count();
        bean.teacher = data.getData().getCourse().getTeacher().getName();
        bean.current_price = data.getData().getCourse().getCurrent_price();

        intent.putExtra("data", bean);
        startActivity(intent);
    }

    private void joinAudition() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlCourses + id + "/taste", null,
                new VolleyListener(RemedialClassDetailActivity.this) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        //已加入试听
//                        data.getData().getEssenceCourse().setTastable(true);
                        auditionStart.setVisibility(View.VISIBLE);
                        loginYunXin();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(RemedialClassDetailActivity.this, "该试听已失效,请直接购买", Toast.LENGTH_SHORT).show();
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

    private void loginYunXin() {
        if (StringUtils.isNullOrBlanK(BaseApplication.getInstance().getAccount()) || StringUtils.isNullOrBlanK(BaseApplication.getInstance().getAccountToken())) {
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getInstance().getUserId() + "/info", null,
                    new VolleyListener(RemedialClassDetailActivity.this) {
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
//                                                            BaseApplication.clearToken();
                                            Profile profile = BaseApplication.getInstance().getProfile();
                                            profile.getData().setRemember_token("");
                                            BaseApplication.getInstance().setProfile(profile);
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
    }

    private String getStatus(String status) {

        if (Constant.CourseStatus.published.equals(status)) {
            return getString(R.string.recruiting);
        } else if (Constant.CourseStatus.teaching.equals(status)) {
            return getString(R.string.teaching);
        } else if (Constant.CourseStatus.completed.equals(status)) {
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
