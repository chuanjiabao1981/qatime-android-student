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
    private ImageView banner;
    private ImageView headSculpture;
    private ImageView modify;
    private LinearLayout paying;
    private LinearLayout paid;
    private LinearLayout canceled;
    private LinearLayout today;
    private TextView waitting;
    private TextView calssed;
    private TextView overed;
    private TextView trying;
    private LinearLayout security;
    private LinearLayout setting;
    private TextView newVersion;
    private TextView name;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);
        assignViews(view);

        if (BaseApplication.getProfile().getData() != null && BaseApplication.getProfile().getData().getUser() != null) {
            Glide.with(getActivity()).load(BaseApplication.getProfile().getData().getUser().getSmall_avatar_url()).placeholder(R.mipmap.personal_information_head).crossFade().transform(new GlideCircleTransform(getActivity())).into(headSculpture);
        }
        name.setText(BaseApplication.getProfile().getData().getUser().getName());
        modify.setOnClickListener(this);

        paying.setOnClickListener(this);
        paid.setOnClickListener(this);
        canceled.setOnClickListener(this);

        today.setOnClickListener(this);
        waitting.setOnClickListener(this);
        calssed.setOnClickListener(this);
        overed.setOnClickListener(this);
        trying.setOnClickListener(this);

        security.setOnClickListener(this);
        setting.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify:
                Intent intent = new Intent(getActivity(), PersonalInformationActivity.class);
                startActivityForResult(intent, Constant.REQUEST);
                break;
            case R.id.paying:
                intent = new Intent(getActivity(), PersonalMyOrderActivity.class);
                intent.putExtra("pager", 0);
                startActivity(intent);
                break;
            case R.id.paid:
                intent = new Intent(getActivity(), PersonalMyOrderActivity.class);
                intent.putExtra("pager", 1);
                startActivity(intent);
                break;
            case R.id.canceled:
                intent = new Intent(getActivity(), PersonalMyOrderActivity.class);
                intent.putExtra("pager", 2);
                startActivity(intent);
                break;
            case R.id.today:
                intent = new Intent(getActivity(), PersonalMyTutorshipActivity.class);
                intent.putExtra("pager", 0);
                startActivity(intent);
                break;
            case R.id.waitting:
                intent = new Intent(getActivity(), PersonalMyTutorshipActivity.class);
                intent.putExtra("pager", 1);
                startActivity(intent);
                break;
            case R.id.calssed:
                intent = new Intent(getActivity(), PersonalMyTutorshipActivity.class);
                intent.putExtra("pager", 2);
                startActivity(intent);
                break;
            case R.id.overed:
                intent = new Intent(getActivity(), PersonalMyTutorshipActivity.class);
                intent.putExtra("pager", 3);
                startActivity(intent);
                break;
            case R.id.trying:
                intent = new Intent(getActivity(), PersonalMyTutorshipActivity.class);
                intent.putExtra("pager", 4);
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
        banner = (ImageView) view.findViewById(R.id.head_sculpture);
        headSculpture = (ImageView) view.findViewById(R.id.head_sculpture);
        name = (TextView) view.findViewById(R.id.name);
        modify = (ImageView) view.findViewById(R.id.modify);
        paying = (LinearLayout) view.findViewById(R.id.paying);
        paid = (LinearLayout) view.findViewById(R.id.paid);
        canceled = (LinearLayout) view.findViewById(R.id.canceled);
        today = (LinearLayout) view.findViewById(R.id.today);
        waitting = (TextView) view.findViewById(R.id.waitting);
        calssed = (TextView) view.findViewById(R.id.calssed);
        overed = (TextView) view.findViewById(R.id.overed);
        trying = (TextView) view.findViewById(R.id.trying);
        security = (LinearLayout) view.findViewById(R.id.security);
        setting = (LinearLayout) view.findViewById(R.id.setting);
        newVersion = (TextView) view.findViewById(R.id.new_version);
    }
}
