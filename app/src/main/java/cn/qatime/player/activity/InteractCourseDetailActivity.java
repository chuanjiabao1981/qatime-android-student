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
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.fragment.FragmentInteractDetailClassInfo;
import cn.qatime.player.fragment.FragmentInteractDetailClassList;
import cn.qatime.player.fragment.FragmentInteractDetailTeachersInfo;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.InteractCourseDetailBean;
import libraryextra.bean.OrderPayBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.SimpleViewPagerIndicator;

public class InteractCourseDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    ImageView image;
    private int id;
    private String[] mTitles;
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private TextView name;
    private TextView title;
    private InteractCourseDetailBean data;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private int pager = 0;
    TextView price;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    DecimalFormat df = new DecimalFormat("#.00");
    private AlertDialog alertDialog;
    private Button startStudy;
    private View startStudyView;
    private View rlImage;
    private View handleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact_course_detail);

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

        fragBaseFragments.add(new FragmentInteractDetailClassInfo());
        fragBaseFragments.add(new FragmentInteractDetailTeachersInfo());
        fragBaseFragments.add(new FragmentInteractDetailClassList());

        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        handleLayout = findViewById(R.id.handle_layout);
        Button pay = (Button) findViewById(R.id.pay);
        startStudy = (Button) findViewById(R.id.start_study);
        startStudyView = findViewById(R.id.start_study_view);


        pay.setOnClickListener(this);
        startStudy.setOnClickListener(this);

        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mTitles = new String[]{getString(R.string.remedial_detail), getString(R.string.teachers_detail), getString(R.string.course_arrangement)};
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
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlInteractCourses + "/" + id, null,
                new VolleyListener(InteractCourseDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), InteractCourseDetailBean.class);

                        if (data != null && data.getData() != null && data.getData().getLive_start_time() != null) {
                            handleLayout.setVisibility(View.VISIBLE);
                            Glide.with(getApplicationContext()).load(data.getData().getPublicize_url()).placeholder(R.mipmap.photo).fitCenter().crossFade().into(image);
                            name.setText(data.getData().getName());
                            title.setText(data.getData().getName());
                            String price;
                            price = df.format(Double.valueOf(data.getData().getPrice()));
                            if (price.startsWith(".")) {
                                price = "0" + price;
                            }
                            InteractCourseDetailActivity.this.price.setText("￥" + price);

                            ((FragmentInteractDetailClassInfo) fragBaseFragments.get(0)).setData(data);
                            ((FragmentInteractDetailTeachersInfo) fragBaseFragments.get(1)).setData(data);
                            ((FragmentInteractDetailClassList) fragBaseFragments.get(2)).setData(data);


                            if (data.getData().isIs_bought()) {
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
            case R.id.start_study:
                if (BaseApplication.isLogined()) {
                    if ("init".equals(data.getData().getStatus()) || "published".equals(data.getData().getStatus())) {
                        Toast.makeText(this, getString(R.string.published_course_unable_enter) + getString(R.string.study), Toast.LENGTH_SHORT).show();
                    } else {
//                        intent = new Intent(InteractCourseDetailActivity.this, NEVideoPlayerActivity.class);
////                    intent.putExtra("camera", data.getData().getCamera());
////                    intent.putExtra("board", data.getData().getBoard());
//                        intent.putExtra("id", data.getData().getId());
//                        intent.putExtra("sessionId", data.getData().getChat_team_id());
//                        startActivity(intent);
                        // TODO: 2017/3/31  跳转播放器
                    }
                } else {
                    intent = new Intent(InteractCourseDetailActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
            case R.id.pay:
                if (BaseApplication.isLogined()) {
                    if ("teaching".equals(data.getData().getStatus())) {
                        if (alertDialog == null) {
                            View view = View.inflate(InteractCourseDetailActivity.this, R.layout.dialog_cancel_or_confirm, null);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(InteractCourseDetailActivity.this);
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
                    intent = new Intent(InteractCourseDetailActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                break;
        }
    }

    private void payRemedial() {
        Intent intent = new Intent(InteractCourseDetailActivity.this, OrderConfirmActivity.class);
        intent.putExtra("courseType", "interact");
        intent.putExtra("id", id);
        intent.putExtra("coupon", getIntent().getStringExtra("coupon"));
        OrderPayBean bean = new OrderPayBean();
        bean.name = data.getData().getName();
        bean.subject = data.getData().getSubject();
        bean.grade = data.getData().getGrade();
        bean.classnumber = data.getData().getLessons_count();
        List<InteractCourseDetailBean.DataBean.TeachersBean> teachers = data.getData().getTeachers();
//        StringBuffer teacherNames = new StringBuffer();
//        for (int i = 0; i < teachers.size(); i++) {
//            teacherNames.append(teachers.get(0).getName());
//            if (i != teachers.size() - 1) {
//                teacherNames.append("/");
//            }
//        }
//        bean.teacher = teacherNames.toString();
        bean.teacher = teachers.get(0).getName() + "...";
        bean.current_price = Float.valueOf(data.getData().getPrice());

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
