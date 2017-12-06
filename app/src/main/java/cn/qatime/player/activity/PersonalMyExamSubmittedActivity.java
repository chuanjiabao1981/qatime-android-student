package cn.qatime.player.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.fragment.FragmentExamSubmitted;
import cn.qatime.player.fragment.FragmentExamUnSubmit;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * @author luntify
 * @date 2017/12/5 16:51
 * @Description: 已提交的试卷
 */

public class PersonalMyExamSubmittedActivity extends BaseActivity {
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_exam_submitted);
        setTitles("已提判的试卷");
        initView();
    }

    private void initView() {
        fragBaseFragments.add(new FragmentExamUnSubmit());
        fragBaseFragments.add(new FragmentExamSubmitted());

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
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_personal_my_exam_submitted, 0x0359);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ((BaseFragment) fragBaseFragments.get(0)).onShow();
//            }
//        }, 200);
    }
}
