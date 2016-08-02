package cn.qatime.player.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.fragment.Fragment1;
import cn.qatime.player.fragment.Fragment2;
import cn.qatime.player.fragment.Fragment3;
import cn.qatime.player.fragment.Fragment4;
import cn.qatime.player.view.FragmentLayout;

public class MainActivity extends BaseFragmentActivity {

    FragmentLayout fragmentlayout;
    public ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    private int[] tab_img = {R.id.tab_img1, R.id.tab_img2, R.id.tab_img3, R.id.tab_img4};
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3, R.id.tab_text4};
    private int tabImages[][] = {
            {R.mipmap.tab_home_1, R.mipmap.tab_home_2},
            {R.mipmap.tab_moments_1, R.mipmap.tab_moments_2},
            {R.mipmap.tab_video_1, R.mipmap.tab_video_2},
            {R.mipmap.tab_find_1, R.mipmap.tab_find_2}};
    /**
     * 当前用户信息
     */
//    public Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        //重置
        fragBaseFragments.clear();
        if (fragmentlayout != null) {
            fragmentlayout.reset();
        }

        //添加fragment
        fragBaseFragments.add(new Fragment1());
        fragBaseFragments.add(new Fragment2());
        fragBaseFragments.add(new Fragment3());
        fragBaseFragments.add(new Fragment4());

        fragmentlayout = (FragmentLayout) findViewById(R.id.fragmentlayout);
        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(false);
        fragmentlayout.setWhereTab(0);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayout.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff858786);
                ((ImageView) lastTabView.findViewById(tab_img[lastPosition])).setImageResource(tabImages[lastPosition][1]);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xffeb6a4b);
                ((ImageView) currentTabView.findViewById(tab_img[position])).setImageResource(tabImages[position][0]);
//                fragBaseFragments.get(position).onShow(lastPosition);
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout, 0x1000);
        fragmentlayout.getViewPager().setOffscreenPageLimit(3);
//        ((BaseFragment) fragBaseFragments.get(0)).onShow(0);
    }
}
