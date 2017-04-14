package cn.qatime.player.activity;

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

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.VideoCoursesDetailsBean;
import cn.qatime.player.fragment.FragmentVideoCoursesClassInfo;
import cn.qatime.player.fragment.FragmentVideoCoursesClassList;
import cn.qatime.player.fragment.FragmentVideoCoursesTeacherInfo;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.SimpleViewPagerIndicator;

/**
 * @author lungtify
 * @Time 2017/4/10 16:27
 * @Describe 视频课详情
 */

public class VideoCoursesActivity extends BaseFragmentActivity {
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
                }

                , new VolleyErrorListener() {
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

}
