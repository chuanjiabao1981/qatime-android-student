package cn.qatime.player.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;

public class Fragment3 extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);
//        initview(view);
//        initData();
        return view;
    }
}
