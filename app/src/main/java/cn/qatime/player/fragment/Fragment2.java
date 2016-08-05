package cn.qatime.player.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.view.FragmentLayoutWithLine;

public class Fragment2 extends BaseFragment {

    ImageView image;
    FragmentLayoutWithLine fragmentlayout;
    private int id;

    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);

initView(view);
    return view;}

    private void initView(View view) {


        fragBaseFragments.add(new FragmentRemedialClassTimeTable1());
        fragBaseFragments.add(new FragmentRemedialClassTimeTable2());


        fragmentlayout = (FragmentLayoutWithLine)view. findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(6, 0xff000000);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int positon, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff858585);
                ((TextView) currentTabView.findViewById(tab_text[positon])).setTextColor(0xff222222);
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_remedial_class_timetable, 0x0911);
    }

}
