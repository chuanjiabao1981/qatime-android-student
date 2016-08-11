package cn.qatime.player.activity;

import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.base.BaseFragmentActivity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.fragment.FragmentPersonalMyTutorship1;
import cn.qatime.player.fragment.FragmentPersonalMyTutorship2;
import cn.qatime.player.fragment.FragmentPersonalMyTutorship3;
import cn.qatime.player.fragment.FragmentPersonalMyTutorship4;
import cn.qatime.player.fragment.FragmentPersonalMyTutorship5;
import cn.qatime.player.view.FragmentLayoutWithLine;

/**
 * 我的辅导
 */
public class PersonalMyTutorshipActivity extends BaseFragmentActivity {
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4, R.id.tab_text5};
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private int pager = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_tutorship);
        setTitle(getResources().getString(R.string.my_course));

        pager = getIntent().getIntExtra("pager", 0);
        setRightImage(R.mipmap.audition_records, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalMyTutorshipActivity.this, AuditionRecordsActivity.class);
                startActivity(intent);
            }
        });
        initView();
    }


    private void initView() {
        findViewById(R.id.right).setVisibility(View.GONE);

        fragBaseFragments.add(new FragmentPersonalMyTutorship1());
        fragBaseFragments.add(new FragmentPersonalMyTutorship2());
        fragBaseFragments.add(new FragmentPersonalMyTutorship3());
        fragBaseFragments.add(new FragmentPersonalMyTutorship4());
        fragBaseFragments.add(new FragmentPersonalMyTutorship5());

        fragmentlayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(6, 0xff000000);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int positon, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff858585);
                ((TextView) currentTabView.findViewById(tab_text[positon])).setTextColor(0xff222222);

                if (positon == 4) {
                    findViewById(R.id.right).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.right).setVisibility(View.GONE);
                }
                ((BaseFragment) fragBaseFragments.get(positon)).onShow();
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tableout_personal_my_tutor, 0x0311);
        fragmentlayout.getViewPager().setOffscreenPageLimit(4);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentlayout.setCurrenItem(pager);
            }
        }, 500);
    }
}
