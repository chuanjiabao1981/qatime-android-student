package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.OrderPayBean;
import cn.qatime.player.bean.RemedialClassDetailBean;
import cn.qatime.player.fragment.FragmentRemedialClassDetail1;
import cn.qatime.player.fragment.FragmentRemedialClassDetail2;
import cn.qatime.player.fragment.FragmentRemedialClassDetail3;
import cn.qatime.player.transformation.GlideCircleTransform;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.JsonUtils;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.ScreenUtils;
import cn.qatime.player.utils.StringUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VolleyErrorListener;
import cn.qatime.player.utils.VolleyListener;
import cn.qatime.player.view.SimpleViewPagerIndicator;

public class RemedialClassDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    ImageView image;
    private int id;
    private String[] mTitles = new String[]{"信息详情", "教师详情", "课程列表"};
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private Button audition;
    private Button pay;
    private RemedialClassDetailBean data;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private int pager = 0;
    TextView price;
    TextView studentnumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedial_class_detail);
        setTitle(getResources().getString(R.string.tutor_information));
        id = getIntent().getIntExtra("id", 0);//联网id
        pager = getIntent().getIntExtra("pager", 0);
        initView();
        initData();

    }


    public void initView() {
        EventBus.getDefault().register(this);
        image = (ImageView) findViewById(R.id.image);
        image.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenWidth(this) * 5 / 8));

        fragBaseFragments.add(new FragmentRemedialClassDetail1());
        fragBaseFragments.add(new FragmentRemedialClassDetail2());
        fragBaseFragments.add(new FragmentRemedialClassDetail3());

        audition = (Button) findViewById(R.id.audition);
        pay = (Button) findViewById(R.id.pay);
        price = (TextView) findViewById(R.id.price);
        studentnumber = (TextView) findViewById(R.id.student_number);
        audition.setOnClickListener(this);
        pay.setOnClickListener(this);

        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
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
                        Glide.with(RemedialClassDetailActivity.this).load(R.mipmap.eight_five).placeholder(R.mipmap.photo).fitCenter().crossFade().into(image);
//                        Glide.with(RemedialClassDetailActivity.this).load(data.getData().getPublicize()).placeholder(R.mipmap.photo).fitCenter().crossFade().into(image);

                        if (data.getData() != null) {
                            price.setText("￥" + data.getData().getPrice());
                            studentnumber.setText("学习人数 " + data.getData().getBuy_tickets_count());
                        }
                        if (data != null) {
                            ((FragmentRemedialClassDetail1) fragBaseFragments.get(0)).setData(data);
                            ((FragmentRemedialClassDetail2) fragBaseFragments.get(1)).setData(data);
                            ((FragmentRemedialClassDetail3) fragBaseFragments.get(2)).setData(data);
                            if (data.getData() != null) {
                                if (data.getData().getIs_tasting()) {
                                    audition.setEnabled(false);
                                    audition.setText(getResources().getString(R.string.Joined_the_audition));
                                } else {
                                    audition.setEnabled(true);
                                    audition.setText(getResources().getString(R.string.Join_the_audition));
                                }

                                if (data.getData().getIs_bought()) {
                                    pay.setEnabled(false);
                                    pay.setText("已购买");
                                    audition.setEnabled(false);
                                } else {
                                    pay.setEnabled(true);
                                    pay.setText(getResources().getString(R.string.purchase_now));
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
        switch (v.getId()) {
            case R.id.audition:
                joinAudition();
                break;
            case R.id.pay:
                Intent intent = new Intent(RemedialClassDetailActivity.this, OrderConfirmActivity.class);
                intent.putExtra("id", id);
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
                bean.price = data.getData().getPrice();

                intent.putExtra("data", bean);
                startActivity(intent);
                break;
        }
    }

    private void joinAudition() {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass + "/" + id + "/taste", map), null,
                new VolleyListener(RemedialClassDetailActivity.this) {


                    @Override
                    protected void onSuccess(JSONObject response) {
                        //已加入试听
                        audition.setEnabled(false);
                        audition.setText(getResources().getString(R.string.Joined_the_audition));
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

    @Subscribe
    public void onEvent(String event) {
        if (!StringUtils.isNullOrBlanK(event) && event.equals("pay_success")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
