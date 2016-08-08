package cn.qatime.player.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.ClassTimeTableActivity;
import cn.qatime.player.activity.PersonalMyOrderActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.adapter.MyPagerAdapter;

public class Fragment4 extends BaseFragment {


    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);

        return view;
    }

    private void initview(View view) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), PersonalMyOrderActivity.class);
                startActivity(intent);

            }
        });

    }


}
