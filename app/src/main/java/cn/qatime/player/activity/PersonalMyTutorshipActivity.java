package cn.qatime.player.activity;

import android.os.Bundle;
import android.os.Handler;
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
import cn.qatime.player.fragment.FragmentTutorshipTaste;
import cn.qatime.player.fragment.FragmentTutorshipTeaching;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * 我的辅导
 */
public class PersonalMyTutorshipActivity extends BaseFragmentActivity {
    private int[] tab_text = {R.id.tab_text2, R.id.tab_text3, R.id.tab_text4, R.id.tab_text5};
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_tutorship);
        setTitles(getResources().getString(R.string.my_course));

//        setRightImage(R.mipmap.audition_records, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PersonalMyTutorshipActivity.this, AuditionRecordsActivity.class);
//                startActivity(intent);
//            }
//        });
        initView();
    }


    private void initView() {
        findViewById(R.id.right).setVisibility(View.GONE);

//        fragBaseFragments.add(new FragmentTutorshipToday());
        fragBaseFragments.add(new FragmentTutorshipPreview());
        fragBaseFragments.add(new FragmentTutorshipTeaching());
        fragBaseFragments.add(new FragmentTutorshipOver());
        fragBaseFragments.add(new FragmentTutorshipTaste());

        fragmentlayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4, 0xffbe0b0b);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);

//                if (position == 4) {
//                    findViewById(R.id.right).setVisibility(View.VISIBLE);
//                } else {
//                    findViewById(R.id.right).setVisibility(View.GONE);
//                }
                ((BaseFragment) fragBaseFragments.get(position)).onShow();
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tableout_personal_my_tutor, 0x0311);
        fragmentlayout.getViewPager().setOffscreenPageLimit(4);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentlayout.setCurrenItem(0);
            }
        }, 200);
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
