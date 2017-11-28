package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.activity.AboutUsActivity;
import cn.qatime.player.activity.LoginActivity2;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.utils.Constant;

/**
 * @author lungtify
 * @Time 2016/11/25 11:08
 * @Describe
 */

public class FragmentUnLoginHomeUserCenter extends BaseFragment implements View.OnClickListener {

    private void assignViews(View v) {
        LinearLayout information = (LinearLayout) v.findViewById(R.id.information);
        TextView name = (TextView) v.findViewById(R.id.name);
        LinearLayout myWallet = (LinearLayout) v.findViewById(R.id.my_wallet);
        LinearLayout myOrder = (LinearLayout) v.findViewById(R.id.my_order);
        LinearLayout myCourse = (LinearLayout) v.findViewById(R.id.my_course);
        LinearLayout setting = (LinearLayout) v.findViewById(R.id.setting);
        LinearLayout myInteract = (LinearLayout) v.findViewById(R.id.my_interact);
        LinearLayout myVideo = (LinearLayout) v.findViewById(R.id.my_video);
        TextView myTaste = (TextView) v.findViewById(R.id.my_taste);
        LinearLayout myExclusive = (LinearLayout) v.findViewById(R.id.my_exclusive);
        TextView downloadManager = (TextView) v.findViewById(R.id.download_manager);
        findViewById(R.id.my_homework).setOnClickListener(this);
        findViewById(R.id.my_question).setOnClickListener(this);
        myInteract.setOnClickListener(this);
        myVideo.setOnClickListener(this);
        information.setOnClickListener(this);
        name.setOnClickListener(this);
        myTaste.setOnClickListener(this);
        myExclusive.setOnClickListener(this);
        downloadManager.setOnClickListener(this);

        myWallet.setOnClickListener(this);
        myOrder.setOnClickListener(this);
        myCourse.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_unlogin_home_user_center, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignViews(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.information:
            case R.id.name:
            case R.id.my_wallet:
            case R.id.my_order:
            case R.id.my_course:
            case R.id.my_interact:
            case R.id.my_video:
            case R.id.my_taste:
            case R.id.my_exclusive:
            case R.id.my_homework:
            case R.id.my_question:
            case R.id.setting:
            case R.id.download_manager:
                Intent intent = new Intent(getActivity(), LoginActivity2.class);
                intent.putExtra("activity_action", Constant.LoginAction.toPage5);
                startActivity(intent);
                break;

        }
    }
}
