package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.RemedialClassDetailBean;
import cn.qatime.player.fragment.FragmentRemedialClassDetail1;
import cn.qatime.player.fragment.FragmentRemedialClassDetail2;
import cn.qatime.player.fragment.FragmentRemedialClassDetail3;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.JsonUtils;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VolleyErrorListener;
import cn.qatime.player.utils.VolleyListener;
import cn.qatime.player.view.SimpleViewPagerIndicator;

public class RemedialClassDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    ImageView image;
    private int id;
    private String[] mTitles = new String[]{"信息详情", "教师详情", "课堂列表"};
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private Button audition;
    private Button pay;
    private RemedialClassDetailBean data;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private int pager = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedial_class_detail);

        id = getIntent().getIntExtra("id", 0);//联网id
        pager = getIntent().getIntExtra("pager", 0);
        initView();
        initData();
    }


    private void initView() {
        image = (ImageView) findViewById(R.id.id_stickynavlayout_topview);
        Glide.with(this).load(R.mipmap.photo).placeholder(R.mipmap.photo).fitCenter().crossFade().into(image);
        fragBaseFragments.add(new FragmentRemedialClassDetail1());
        fragBaseFragments.add(new FragmentRemedialClassDetail2());
        fragBaseFragments.add(new FragmentRemedialClassDetail3());

        audition = (Button) findViewById(R.id.audition);
        pay = (Button) findViewById(R.id.pay);

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
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass + "/" + id, map), null,
                new VolleyListener(RemedialClassDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LogUtils.e(response.toString());
                        data = JsonUtils.objectFromJson(response.toString(), RemedialClassDetailBean.class);
                        ((FragmentRemedialClassDetail1) fragBaseFragments.get(0)).setData(data);
                        ((FragmentRemedialClassDetail2) fragBaseFragments.get(1)).setData(data);
                        //TODO
//                        ((FragmentRemedialClassDetail3) fragBaseFragments.get(2)).setData(data);
                    }

                    @Override
                    protected void onError(JSONObject response) {

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
                break;
            case R.id.pay:
                Intent intent = new Intent(RemedialClassDetailActivity.this, OrderConfirmActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
                break;
        }
    }
}
