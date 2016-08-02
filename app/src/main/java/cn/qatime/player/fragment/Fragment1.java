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
import cn.qatime.player.view.FragmentLayout;

public class Fragment1 extends BaseFragment {

    private FragmentLayout root;
    public ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        initView(view);
        return view;
    }
    /**
     * 初始化布局
     * @param view
     */
    private void initView(View view) {
        //重置
        fragBaseFragments.clear();
        if (root != null) {
            root.reset();
        }

        //添加fragment
        fragBaseFragments.add(new Fragment11());
        fragBaseFragments.add(new Fragment12());

        root = (FragmentLayout) view.findViewById(R.id.root);
        root.setScorllToNext(true);
        root.setScorll(true);
        root.setWhereTab(1);
        root.setOnChangeFragmentListener(new FragmentLayout.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0x99000000);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff000000);
            }
        });
        root.setAdapter(fragBaseFragments, R.layout.tablayout_fragment1, 0x1001);
        root.getViewPager().setOffscreenPageLimit(1);
    }
}
