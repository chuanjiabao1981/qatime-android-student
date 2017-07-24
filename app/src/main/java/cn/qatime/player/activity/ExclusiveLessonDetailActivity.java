package cn.qatime.player.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.fragment.FragmentExclusiveLessonClassInfo;
import cn.qatime.player.fragment.FragmentExclusiveLessonClassList;
import cn.qatime.player.fragment.FragmentExclusiveLessonTeacherInfo;
import cn.qatime.player.view.SimpleViewPagerIndicator;

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
    private TextView refundAnyTime;
    private TextView joinCheap;
    private TextView progress;
    private TextView timeToStart;
    private TextView status;
    private View layoutView;
    private Button startStudy;
    private View startStudyView;
    private TextView price;
    private TextView studentNumber;
    private View handleLayout;
    private SimpleViewPagerIndicator mIndicator;
    private ViewPager mViewPager;

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
        startStudyView = findViewById(R.id.start_study_view);
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

    }

    @Override
    public void onClick(View v) {

    }
}
