package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.qatime.player.R;
import cn.qatime.player.activity.LoginActivity;
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
        LinearLayout security = (LinearLayout) v.findViewById(R.id.security);
        LinearLayout setting = (LinearLayout) v.findViewById(R.id.setting);
        information.setOnClickListener(this);
        name.setOnClickListener(this);

        myWallet.setOnClickListener(this);
        myOrder.setOnClickListener(this);
        myCourse.setOnClickListener(this);
        security.setOnClickListener(this);
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
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("sign", Constant.VISITORTOLOGIN);
                intent.putExtra("action", Constant.LoginAction.toPage4);
                startActivityForResult(intent, Constant.REQUEST);
                break;
            case R.id.my_wallet:
            case R.id.my_order:
            case R.id.my_course:
            case R.id.security:
            case R.id.setting:
                Toast.makeText(getActivity(), getResourceString(R.string.unlogin_alert), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}