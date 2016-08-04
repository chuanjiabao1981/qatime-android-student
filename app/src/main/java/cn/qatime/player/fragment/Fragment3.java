package cn.qatime.player.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.qatime.player.R;
import cn.qatime.player.activity.NEVideoPlayerActivity;
import cn.qatime.player.base.BaseFragment;

public class Fragment3 extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);
        initview(view);
//        initData();
        return view;
    }

    private void initview(View view) {
        Button roll = (Button) view.findViewById(R.id.roll);
        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NEVideoPlayerActivity.class);
                startActivity(intent);
            }
        });
    }
}
