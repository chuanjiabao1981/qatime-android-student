package cn.qatime.player.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.fragment.FragmentFilterClassInteract;
import cn.qatime.player.fragment.FragmentFilterClassLive;
import cn.qatime.player.fragment.FragmentFilterClassVideo;

/**
 * @author Tianhaoranly
 * @date 2017/4/10 15:55
 * @Description:
 */
public class FilterCourseActivity extends BaseFragmentActivity implements View.OnClickListener {
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private ArrayList<View> views = new ArrayList<>();
    private ViewPager pager;
    private int lastPosition = 0;
    private String grade;
    private String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_filter);
        initView();
    }

    public void backClick(View view) {
        this.finish();
    }

    private void initView() {
        View tab1 = findViewById(R.id.tab_text1);
        View tab2 = findViewById(R.id.tab_text2);
        View tab3 = findViewById(R.id.tab_text3);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        views.add(tab1);
        views.add(tab2);
        views.add(tab3);

        grade = getIntent().getStringExtra("grade");
        subject = getIntent().getStringExtra("subject");
        fragBaseFragments.add(new FragmentFilterClassLive().setArguments(grade, subject));
        fragBaseFragments.add(new FragmentFilterClassInteract().setArguments(grade, subject));
        fragBaseFragments.add(new FragmentFilterClassVideo().setArguments(grade, subject));

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragBaseFragments.get(position);
            }

            @Override
            public int getCount() {
                return views.size();
            }
        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((TextView) views.get(position)).setTextColor(getResources().getColor(R.color.colorPrimary));
                views.get(position).setBackgroundColor(0xffffffff);
                ((TextView) views.get(lastPosition)).setTextColor(0xffffffff);
                views.get(lastPosition).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_text1:
                pager.setCurrentItem(0);
                break;
            case R.id.tab_text2:
                pager.setCurrentItem(1);
                break;
            case R.id.tab_text3:
                pager.setCurrentItem(2);
                break;
        }
    }
}
