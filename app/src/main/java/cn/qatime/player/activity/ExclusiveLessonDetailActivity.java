package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.ExclusiveLessonDetailBean;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.fragment.FragmentExclusiveLessonClassInfo;
import cn.qatime.player.fragment.FragmentExclusiveLessonClassList;
import cn.qatime.player.fragment.FragmentExclusiveLessonTeacherInfo;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.SimpleViewPagerIndicator;
import libraryextra.bean.OrderPayBean;
import libraryextra.utils.DateUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2017/7/24 11:10
 * @Describe 专属课详情
 */

public class ExclusiveLessonDetailActivity extends BaseActivity implements View.OnClickListener {
    private int id;
    private String[] mTitles;
    private TextView name;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("#.00");
    private TextView refundAnyTime;
    private TextView joinCheap;
    private TextView progress;
    private TextView timeToStart;
    private TextView status;
    private View layoutView;
    private Button startStudy;
    //    private View startStudyView;
    private TextView price;
    private TextView studentNumber;
    private View handleLayout;
    private SimpleViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private ExclusiveLessonDetailBean data;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_lesson_detail);
        id = getIntent().getIntExtra("id", 0);//联网id
        initView();
        if (id == 0) {
            Toast.makeText(this, "id不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        EventBus.getDefault().register(this);
        initData();
    }

    private void initView() {
        name = (TextView) findViewById(R.id.name);

        fragBaseFragments.add(new FragmentExclusiveLessonClassInfo());
        fragBaseFragments.add(new FragmentExclusiveLessonTeacherInfo());
        fragBaseFragments.add(new FragmentExclusiveLessonClassList());

        refundAnyTime = (TextView) findViewById(R.id.refund_any_time);
        joinCheap = (TextView) findViewById(R.id.join_cheap);
        progress = (TextView) findViewById(R.id.progress);
        timeToStart = (TextView) findViewById(R.id.time_to_start);
        status = (TextView) findViewById(R.id.status);
        layoutView = findViewById(R.id.layout_view);

        startStudy = (Button) findViewById(R.id.start_study);
//        startStudyView = findViewById(R.id.start_study_view);
        price = (TextView) findViewById(R.id.price);
        studentNumber = (TextView) findViewById(R.id.student_number);
        handleLayout = findViewById(R.id.handle_layout);
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
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlExclusiveLesson + "/" + id + "/detail", null,
                new VolleyListener(ExclusiveLessonDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), ExclusiveLessonDetailBean.class);
                        setData();
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

    private void setData() {
        if (data != null && data.getData() != null && data.getData().getCustomized_group() != null) {
            handleLayout.setVisibility(View.VISIBLE);
            status.setText(getStatus(data.getData().getCustomized_group().getStatus()));
            name.setText(data.getData().getCustomized_group().getName());
            setTitles(data.getData().getCustomized_group().getName());
            studentNumber.setText(getString(R.string.student_number, data.getData().getCustomized_group().getView_tickets_count()));
            String price = df.format(data.getData().getCustomized_group().getPrice());
            if (price.startsWith(".")) {
                price = "0" + price;
            }
            ExclusiveLessonDetailActivity.this.price.setText("￥" + price);
            if (data.getData().getCustomized_group().getSell_type().equals("charge")) {
                if (Constant.CourseStatus.published.equals(data.getData().getCustomized_group().getStatus())) {
                    int value = 0;
                    try {
                        value = DateUtils.daysBetween(data.getData().getCustomized_group().getStart_at(), System.currentTimeMillis());
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
                    layoutView.setBackgroundColor(0xff00d564);
                } else if (Constant.CourseStatus.teaching.equals(data.getData().getCustomized_group().getStatus())) {
                    progress.setVisibility(View.VISIBLE);
                    timeToStart.setVisibility(View.GONE);
                    layoutView.setBackgroundColor(0xff00a0e9);
                    progress.setText(getString(R.string.progress_live, data.getData().getCustomized_group().getClosed_events_count(), data.getData().getCustomized_group().getEvents_count()));
                } else if (Constant.CourseStatus.completed.equals(data.getData().getCustomized_group().getStatus())) {
                    timeToStart.setVisibility(View.GONE);
                    progress.setVisibility(View.VISIBLE);
                    layoutView.setBackgroundColor(0xff999999);
                    progress.setText(getString(R.string.progress_live, data.getData().getCustomized_group().getClosed_events_count(), data.getData().getCustomized_group().getEvents_count()));
                } else {
                    layoutView.setVisibility(View.GONE);
                }

                if (data.getData().getTicket() != null) {//已试听或已购买
                    if (!Constant.CourseStatus.completed.equals(data.getData().getCustomized_group().getStatus())) {
                        if (!StringUtils.isNullOrBlanK(data.getData().getTicket().getType())) {
                            if (data.getData().getTicket().getType().equals("LiveStudio::BuyTicket")) {//已购买
                                startStudy.setText("开始学习");
//                                startStudyView.setVisibility(View.VISIBLE);//开始学习
//                            } else {//进入试听按钮显示
//                                audition.setVisibility(View.GONE);
//                                auditionStart.setVisibility(View.VISIBLE);
//                                if (data.getData().getTicket().getUsed_count() >= data.getData().getTicket().getBuy_count()) {
//                                    auditionStart.setText("试听结束");
//                                    auditionStart.setEnabled(false);
//                                }
                            }
                        }
                    } else {
                        handleLayout.setVisibility(View.GONE);
                    }
                } else {//需加入试听或购买
//                    startStudyView.setVisibility(View.VISIBLE);
                    startStudy.setText("立即报名");
//                    if (data.getData().getCustomized_group().isTastable()) {//可以加入试听
//                        audition.setVisibility(View.VISIBLE);
//                        auditionStart.setVisibility(View.GONE);
//                    } else {//不可试听  只能购买
//                        auditionLayout.setVisibility(View.GONE);
//                    }
//                    if (data.getData().getCustomized_group().isOff_shelve()) {
//                        startStudyView.setVisibility(View.VISIBLE);
//                        startStudy.setText("已下架");
//                        startStudy.setEnabled(false);
//                    }
                }
            } else if (data.getData().getCustomized_group().getSell_type().equals("free")) {
                layoutView.setVisibility(View.GONE);
                ExclusiveLessonDetailActivity.this.price.setText("免费");
                if (data.getData().getTicket() != null) {//已购买
                    if (!Constant.CourseStatus.completed.equals(data.getData().getCustomized_group().getStatus())) {
                        startStudy.setText("开始学习");
//                        startStudyView.setVisibility(View.VISIBLE);
                    } else {
                        handleLayout.setVisibility(View.GONE);
                    }
                } else {
//                    startStudyView.setVisibility(View.VISIBLE);
                    startStudy.setText("立即报名");
//                    if (data.getData().getCustomized_group().isOff_shelve()) {
//                        startStudy.setText("已下架");
//                        startStudy.setEnabled(false);
//                    }
                }
            }

            if (data.getData().getCustomized_group().getIcons() != null) {
                if (!data.getData().getCustomized_group().getIcons().isRefund_any_time()) {
                    refundAnyTime.setVisibility(View.GONE);
                }
                if (!data.getData().getCustomized_group().getIcons().isJoin_cheap()) {
                    joinCheap.setVisibility(View.GONE);
                }
            }
            ((FragmentExclusiveLessonClassInfo) fragBaseFragments.get(0)).setData(data);
            ((FragmentExclusiveLessonTeacherInfo) fragBaseFragments.get(1)).setData(data);
            ((FragmentExclusiveLessonClassList) fragBaseFragments.get(2)).setData(data);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_study:
                if (BaseApplication.getInstance().isLogined()) {
                    if (data.getData().getTicket() != null) {
                        // TODO: 2017/7/27 转到专属课观看页面
                    } else {
                        if (data.getData().getCustomized_group().getSell_type().equals("charge")) {
                            if (Constant.CourseStatus.teaching.equals(data.getData().getCustomized_group().getStatus())) {
                                if (alertDialog == null) {
                                    View view = View.inflate(ExclusiveLessonDetailActivity.this, R.layout.dialog_cancel_or_confirm, null);
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ExclusiveLessonDetailActivity.this);
                                    alertDialog = builder.create();
                                    alertDialog.show();
                                    alertDialog.setContentView(view);
                                } else {
                                    alertDialog.show();
                                }
                            } else {
                                payRemedial();
                            }
                        } else if (data.getData().getCustomized_group().getSell_type().equals("free")) {
                            free2deliver();
                        }
                    }
                } else {
                    Intent intent = new Intent(ExclusiveLessonDetailActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
        }
    }

    private void free2deliver() {
        Toast.makeText(this, "暂不支持专属课免费", Toast.LENGTH_SHORT).show();
    }

    private void payRemedial() {
        Intent intent = new Intent(ExclusiveLessonDetailActivity.this, OrderConfirmActivity.class);
        intent.putExtra("courseType", "exclusive");
        intent.putExtra("id", id);
        intent.putExtra("coupon", getIntent().getStringExtra("coupon"));
        OrderPayBean bean = new OrderPayBean();
        bean.name = data.getData().getCustomized_group().getName();
        bean.subject = data.getData().getCustomized_group().getSubject();
        bean.grade = data.getData().getCustomized_group().getGrade();
        bean.classnumber = data.getData().getCustomized_group().getView_tickets_count();
        bean.teacher = data.getData().getCustomized_group().getTeacher().getName();
        bean.current_price = data.getData().getCustomized_group().getPrice();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
