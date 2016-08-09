package cn.qatime.player.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.adapter.CommonAdapter;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.RemedialClassBean;
import cn.qatime.player.fragment.FragmentPersonalMyOrder1;
import cn.qatime.player.fragment.FragmentPersonalMyOrder2;
import cn.qatime.player.fragment.FragmentPersonalMyOrder3;
import cn.qatime.player.view.FragmentLayoutWithLine;

public class PersonalMyOrderActivity extends BaseFragmentActivity {
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3};
    private CommonAdapter<RemedialClassBean.Data> adapter;
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_order);
        setTitle("");
//        setRight(R.mipmap.back_arrow, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        initView();
    }


    private void initView() {


        fragBaseFragments.add(new FragmentPersonalMyOrder1());
        fragBaseFragments.add(new FragmentPersonalMyOrder2());
        fragBaseFragments.add(new FragmentPersonalMyOrder3());

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
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_personal_my_order, 0x0311);
        fragmentlayout.getViewPager().setOffscreenPageLimit(2);
    }
}