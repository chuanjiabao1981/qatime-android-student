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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.bean.VideoCoursesDetailsBean;
import cn.qatime.player.fragment.FragmentVideoCoursesClassInfo;
import cn.qatime.player.fragment.FragmentVideoCoursesClassList;
import cn.qatime.player.fragment.FragmentVideoCoursesTeacherInfo;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.OrderPayBean;
import libraryextra.utils.JsonUtils;
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
    private ImageView image;
    private TextView name;
    private TextView price;
    private TextView transferPrice;
    private TextView studentNumber;
    private RelativeLayout handleLayout;
    private Button audition;
    private Button auditionStart;
    private Button pay;
    private LinearLayout startStudyView;
    private Button startStudy;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_courses);
        id = getIntent().getIntExtra("id", 0);//联网id
        initView();
        initData();
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlVideoCourses + "/" + id, null,
                new VolleyListener(VideoCoursesActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), VideoCoursesDetailsBean.class);

                        if (data != null && data.getData() != null) {
                            handleLayout.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(data.getData().getPublicize()).placeholder(R.mipmap.photo).fitCenter().crossFade().into(image);
                            name.setText(data.getData().getName());
                            setTitles(data.getData().getName());
                            studentNumber.setText(getString(R.string.student_number, data.getData().getBuy_tickets_count()));

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
                                    audition.setVisibility(View.GONE);
                                    if (data.getData().isTasted()) {
                                        auditionStart.setText(getResourceString(R.string.audition_over));
                                        auditionStart.setEnabled(false);
                                    }
                                } else {//显示加入试听按钮
                                    audition.setText(getResources().getString(R.string.Join_the_audition));
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
        image = (ImageView) findViewById(R.id.image);
        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        transferPrice = (TextView) findViewById(R.id.transfer_price);
        studentNumber = (TextView) findViewById(R.id.student_number);
        handleLayout = (RelativeLayout) findViewById(R.id.handle_layout);
        audition = (Button) findViewById(R.id.audition);
        auditionStart = (Button) findViewById(R.id.audition_start);
        pay = (Button) findViewById(R.id.pay);
        startStudyView = (LinearLayout) findViewById(R.id.start_study_view);
        startStudy = (Button) findViewById(R.id.start_study);

        audition.setOnClickListener(this);
        auditionStart.setOnClickListener(this);
        pay.setOnClickListener(this);
        startStudy.setOnClickListener(this);

        fragBaseFragments.add(new FragmentVideoCoursesClassInfo());
        fragBaseFragments.add(new FragmentVideoCoursesTeacherInfo());
        fragBaseFragments.add(new FragmentVideoCoursesClassList());

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
                        intent = new Intent(VideoCoursesActivity.this, NEVideoPlayerActivity.class);
//                    intent.putExtra("camera", data.getData().getCamera());
//                    intent.putExtra("board", data.getData().getBoard());
                        intent.putExtra("id", data.getData().getId());
                        intent.putExtra("sessionId", data.getData().getChat_team_id());
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(VideoCoursesActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
            case R.id.audition:
                if (BaseApplication.isLogined()) {
                    joinAudition();
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

    }
}
