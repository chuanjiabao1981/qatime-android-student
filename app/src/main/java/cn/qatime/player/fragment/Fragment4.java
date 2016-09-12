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

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import cn.qatime.player.R;
import cn.qatime.player.activity.PersonalInformationActivity;
import cn.qatime.player.activity.PersonalMyOrderActivity;
import cn.qatime.player.activity.PersonalMyTutorshipActivity;
import cn.qatime.player.activity.SecurityManagerActivity;
import cn.qatime.player.activity.SystemSettingActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.utils.Constant;
import libraryextra.transformation.GlideCircleTransform;

public class Fragment4 extends BaseFragment implements View.OnClickListener {
    private LinearLayout information;
    private ImageView headSculpture;
    private LinearLayout order;
    private LinearLayout ticket;
    private LinearLayout course;
    private LinearLayout security;
    private LinearLayout setting;
    private TextView newVersion;
    private TextView name;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);
        assignViews(view);
        newVersion.setVisibility(getActivity().getIntent().getBooleanExtra("newVersion", false) ? View.VISIBLE : View.INVISIBLE);
        if (BaseApplication.getProfile().getData() != null && BaseApplication.getProfile().getData().getUser() != null) {
            Glide.with(getActivity()).load(BaseApplication.getProfile().getData().getUser().getSmall_avatar_url()).placeholder(R.mipmap.personal_information_head).crossFade().transform(new GlideCircleTransform(getActivity())).into(headSculpture);
        }
        name.setText(BaseApplication.getProfile().getData().getUser().getName());

        order.setOnClickListener(this);
        ticket.setOnClickListener(this);
        course.setOnClickListener(this);
        information.setOnClickListener(this);

        security.setOnClickListener(this);
        setting.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.information:
                Intent intent = new Intent(getActivity(), PersonalInformationActivity.class);
                startActivityForResult(intent, Constant.REQUEST);
                break;
            case R.id.my_ticket:
                intent = new Intent(getActivity(), PersonalMyOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.my_order:
                intent = new Intent(getActivity(), PersonalMyOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.my_course:
                intent = new Intent(getActivity(), PersonalMyTutorshipActivity.class);
                startActivity(intent);
                break;
            case R.id.security:// 安全管理
                intent = new Intent(getActivity(), SecurityManagerActivity.class);
                getActivity().startActivityForResult(intent, Constant.REQUEST_EXIT_LOGIN);
                break;
            case R.id.setting:// 设置
                intent = new Intent(getActivity(), SystemSettingActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.e("图片返回");
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            Glide.with(getActivity()).load(data.getStringExtra("url")).crossFade().transform(new GlideCircleTransform(getActivity())).into(headSculpture);
            name.setText(BaseApplication.getProfile().getData().getUser().getName());
        }
    }

    private void assignViews(View view) {
        information = (LinearLayout) view.findViewById(R.id.information);
        headSculpture = (ImageView) view.findViewById(R.id.head_sculpture);
        name = (TextView) view.findViewById(R.id.name);
        order = (LinearLayout) view.findViewById(R.id.my_order);
        ticket = (LinearLayout) view.findViewById(R.id.my_ticket);
        course = (LinearLayout) view.findViewById(R.id.my_course);
        security = (LinearLayout) view.findViewById(R.id.security);
        setting = (LinearLayout) view.findViewById(R.id.setting);
        newVersion = (TextView) view.findViewById(R.id.new_version);
    }
}
