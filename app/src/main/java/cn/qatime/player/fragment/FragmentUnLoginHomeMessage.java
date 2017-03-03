package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qatime.player.R;
import cn.qatime.player.activity.LoginActivity2;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.utils.Constant;

/**
 * @author lungtify
 * @Time 2016/11/25 10:12
 * @Describe
 */

public class FragmentUnLoginHomeMessage extends BaseFragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unlogin_home_message, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.login).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), LoginActivity2.class);
        switch (v.getId()) {
            case R.id.login:
                intent.putExtra("activity_action", Constant.LoginAction.toMessage);
                break;
        }
        startActivity(intent);
    }
}
