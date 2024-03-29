package cn.qatime.player.fragment;


import android.content.Intent;
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
import cn.qatime.player.activity.ClassTimeTableActivity;
import cn.qatime.player.base.BaseFragment;
import libraryextra.view.FragmentLayoutWithLine;

public class FragmentHomeClassTable extends BaseFragment {

    ImageView image;
    FragmentLayoutWithLine fragmentlayout;

    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_class_table, container, false);
        initView(view);
        return view;
    }



    private void initView(View view) {

        view.findViewById(R.id.calendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClassTimeTableActivity.class);
                startActivity(intent);
            }
        });

        fragBaseFragments.add(new FragmentClassTableUnclosed());
        fragBaseFragments.add(new FragmentClassTableClosed());


        fragmentlayout = (FragmentLayoutWithLine) view.findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4, getResources().getColor(R.color.colorPrimary));
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_remedial_class_timetable, 0x0911);
    }

}
