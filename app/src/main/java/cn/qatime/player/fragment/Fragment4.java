package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qatime.player.R;
import cn.qatime.player.activity.PersonalMyOrderActivity;
import cn.qatime.player.base.BaseFragment;

public class Fragment4 extends BaseFragment {


    private TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);
        initview(view);
        return view;
    }

    private void initview(View view) {
        tv = (TextView) view.findViewById(R.id.paying);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonalMyOrderActivity.class);
                startActivity(intent);
            }
        });

    }


}
