package cn.qatime.player.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.fragment.FragmentTutorshipOver;
import cn.qatime.player.fragment.FragmentTutorshipPreview;
import cn.qatime.player.fragment.FragmentTutorshipTeaching;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * 我的辅导
 */
public class PersonalMyTutorshipActivity extends BaseFragmentActivity {
    private int[] tab_text = {R.id.tab_text2, R.id.tab_text3, R.id.tab_text4};
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_tutorship);
        setTitles("我的直播课");

        initView();
    }


    private void initView() {
        fragBaseFragments.add(new FragmentTutorshipPreview());
        fragBaseFragments.add(new FragmentTutorshipTeaching());
        fragBaseFragments.add(new FragmentTutorshipOver());

        fragmentlayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4, getResources().getColor(R.color.colorPrimary));
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);

                ((BaseFragment) fragBaseFragments.get(position)).onShow();
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tableout_personal_my_tutor, 0x0311);
        fragmentlayout.getViewPager().setOffscreenPageLimit(2);
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
}
