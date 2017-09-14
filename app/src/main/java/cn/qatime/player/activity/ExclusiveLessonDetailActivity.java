package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.ExclusiveLessonDetailBean;
import cn.qatime.player.bean.ExclusiveLessonPlayInfoBean;
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
    private TextView couponFree;
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
    private PopupWindow pop;
    private ExclusiveLessonPlayInfoBean playInfo;

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

    private void initMenu(String status) {
        if (pop == null) {
            setRightImage(R.mipmap.exclusive_menu, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop.showAsDropDown(v);
                    backgroundAlpha(0.9f);
                }
            });
            View popView = View.inflate(this, R.layout.exclusive_pop_menu, null);
            View menu1 = popView.findViewById(R.id.menu_1);
            View menu2 = popView.findViewById(R.id.menu_2);
            View menu3 = popView.findViewById(R.id.menu_3);
            View menu4 = popView.findViewById(R.id.menu_4);
            View menu5 = popView.findViewById(R.id.menu_5);
            if (Constant.CourseStatus.completed.equals(status)) {
                menu1.setVisibility(View.GONE);
            }
            menu1.setOnClickListener(this);
            menu2.setOnClickListener(this);
            menu3.setOnClickListener(this);
            menu4.setOnClickListener(this);
            menu5.setOnClickListener(this);
            pop = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1f;
                    getWindow().setAttributes(lp);
                }
            });
        }
    }


    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void initView() {
        name = (TextView) findViewById(R.id.name);

        fragBaseFragments.add(new FragmentExclusiveLessonClassInfo());
        fragBaseFragments.add(new FragmentExclusiveLessonTeacherInfo());
        fragBaseFragments.add(new FragmentExclusiveLessonClassList());

        refundAnyTime = (TextView) findViewById(R.id.refund_any_time);
        couponFree = (TextView) findViewById(R.id.coupon_free);
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
        DaYiJsonObjectRequest requestMember = new DaYiJsonObjectRequest(UrlUtils.urlExclusiveLesson + "/" + id + "/play", null,
                new VolleyListener(ExclusiveLessonDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        playInfo = JsonUtils.objectFromJson(response.toString(), ExclusiveLessonPlayInfoBean.class);
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
        addToRequestQueue(requestMember);
    }

    private void setData() {
        if (data != null && data.getData() != null && data.getData().getCustomized_group() != null) {
            handleLayout.setVisibility(View.VISIBLE);
            status.setText(getStatus(data.getData().getCustomized_group().getStatus()));
            name.setText(data.getData().getCustomized_group().getName());
            setTitles(data.getData().getCustomized_group().getName());
            studentNumber.setText(getString(R.string.student_number, data.getData().getCustomized_group().getView_tickets_count()));

            if (Constant.CourseStatus.published.equals(data.getData().getCustomized_group().getStatus())) {
                int value = DateUtils.daysBetween(Long.valueOf(data.getData().getCustomized_group().getStart_at()) * 1000, System.currentTimeMillis());
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


            if (data.getData().getCustomized_group().getSell_type().equals("charge")) {

                String priceStr = df.format(data.getData().getCustomized_group().getPrice());
                if (priceStr.startsWith(".")) {
                    priceStr = "0" + priceStr;
                }
                price.setText("￥" + priceStr);
                if (data.getData().getTicket() != null && "LiveStudio::BuyTicket".equals(data.getData().getTicket().getType())) {//已购买
                    initMenu(data.getData().getCustomized_group().getStatus());
                    if (Constant.CourseStatus.completed.equals(data.getData().getCustomized_group().getStatus())) {
                        handleLayout.setVisibility(View.GONE);
                    } else {
                        startStudy.setText("开始学习");
                    }
                } else {//需购买
                    if (data.getData().getCustomized_group().isOff_shelve() || Constant.CourseStatus.completed.equals(data.getData().getCustomized_group().getStatus())) {//已下架或未购买已结束显示已下架
                        handleLayout.setVisibility(View.GONE);
                        price.setText("已下架");
                    } else {
                        startStudy.setBackgroundResource(R.drawable.button_bg_selector_red_with_stork);
                        startStudy.setTextColor(getResources().getColor(R.color.colorPrimary));
                        startStudy.setText("立即报名");
                    }
                }
            } else if (data.getData().getCustomized_group().getSell_type().equals("free")) {
                price.setText("免费");
                if (data.getData().getTicket() != null) {//已购买
                    initMenu(data.getData().getCustomized_group().getStatus());
                    if (Constant.CourseStatus.completed.equals(data.getData().getCustomized_group().getStatus())) {
                        handleLayout.setVisibility(View.GONE);
                    } else {
                        startStudy.setText("开始学习");
                    }
                } else {
                    if (data.getData().getCustomized_group().isOff_shelve() || Constant.CourseStatus.completed.equals(data.getData().getCustomized_group().getStatus())) {
                        price.setText("已下架");
                        handleLayout.setVisibility(View.GONE);
                    } else {
                        startStudy.setBackgroundResource(R.drawable.button_bg_selector_red_with_stork);
                        startStudy.setTextColor(getResources().getColor(R.color.colorPrimary));
                        startStudy.setText("立即报名");
                    }
                }
            }

            if (data.getData().getCustomized_group().getIcons() != null) {
                if (!data.getData().getCustomized_group().getIcons().isRefund_any_time()) {
                    refundAnyTime.setVisibility(View.GONE);
                }
                if (!data.getData().getCustomized_group().getIcons().isCoupon_free()) {
                    couponFree.setVisibility(View.GONE);
                }
            }
            ((FragmentExclusiveLessonClassInfo) fragBaseFragments.get(0)).setData(data);
            ((FragmentExclusiveLessonTeacherInfo) fragBaseFragments.get(1)).setData(data);
            ((FragmentExclusiveLessonClassList) fragBaseFragments.get(2)).setData(data.getData());
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.menu_1:
                if(playInfo==null){
                    Toast.makeText(this, "未获取到聊天群组", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(this, MessageActivity.class);
                intent.putExtra("sessionId", playInfo.getData().getCustomized_group().getChat_team().getTeam_id());
                intent.putExtra("sessionType", SessionTypeEnum.None);
                intent.putExtra("courseId", id);
                intent.putExtra("name", data.getData().getCustomized_group().getName());
                intent.putExtra("type", "exclusive");
                intent.putExtra("owner", 0);
                startActivity(intent);
                pop.dismiss();
                break;
            case R.id.menu_2:
                intent = new Intent(this, ExclusiveFilesActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                pop.dismiss();
                break;
            case R.id.menu_3:
                intent = new Intent(this,ExclusiveQuestionsActivity.class);
                intent.putExtra("courseId",id);
                startActivity(intent);
                pop.dismiss();
                break;
            case R.id.menu_4:
                intent = new Intent(this, ExclusiveStudentHomeWorksActivity.class);
                intent.putExtra("courseId", id);
                startActivity(intent);
                pop.dismiss();
                break;
            case R.id.menu_5:
                if(playInfo==null){
                    Toast.makeText(this, "未获取到聊天群组", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(this,MembersActivity.class);
                intent.putExtra("courseId", id);
                startActivity(intent);
                pop.dismiss();
                break;
            case R.id.start_study:
                if (BaseApplication.getInstance().isLogined()) {
                    if (data.getData().getTicket() != null) {
                        intent = new Intent(this, ExclusiveVideoPlayerActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
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
                    intent = new Intent(ExclusiveLessonDetailActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
        }
    }

    private void free2deliver() {
        // TODO: 2017/8/8 记得改按钮样式 ↓
        //startStudy.setBackgroundResource(R.drawable.button_bg_selector_red);
        //startStudy.setTextColor(Color.WHITE);
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
