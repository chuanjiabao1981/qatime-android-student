package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.adapter.MyPagerAdapter;

public class Fragment4 extends BaseFragment {

    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private View view1, view2, view3, view4, view5;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);
        initview(view);
//        initData();
        return view;
    }

    private void initview(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        //TabLayout关联viewpager
        relation();

    }

    private void relation() {
        mInflater = LayoutInflater.from(getActivity());
        view1 = mInflater.inflate(R.layout.today, null);//今日
        view2 = mInflater.inflate(R.layout.to_classes, null);//待开课
        view3 = mInflater.inflate(R.layout.have_classes, null);//已开课
        view4 = mInflater.inflate(R.layout.has_ended, null);//已结束
        view5 = mInflater.inflate(R.layout.audition, null);//试听

        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        mViewList.add(view4);
        mViewList.add(view5);

        //添加页卡标题
        mTitleList.add("今日");
        mTitleList.add("待开课");
        mTitleList.add("已开课");
        mTitleList.add("已结束");
        mTitleList.add("试听");

        tabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(3)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(4)));

        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList,mTitleList);
        viewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        tabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        tabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
    }

}
