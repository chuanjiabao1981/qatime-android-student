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

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.qatime.player.R;
import cn.qatime.player.activity.DownloadManagerActivity;
import cn.qatime.player.activity.PersonalInformationActivity;
import cn.qatime.player.activity.PersonalMyExclusiveActivity;
import cn.qatime.player.activity.PersonalMyHomeworkActivity;
import cn.qatime.player.activity.PersonalMyInteractActivity;
import cn.qatime.player.activity.PersonalMyOrderActivity;
import cn.qatime.player.activity.PersonalMyQuestionActivity;
import cn.qatime.player.activity.PersonalMyTasteActivity;
import cn.qatime.player.activity.PersonalMyTutorshipActivity;
import cn.qatime.player.activity.PersonalMyVideoActivity;
import cn.qatime.player.activity.PersonalMyWalletActivity;
import cn.qatime.player.activity.SystemSettingActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.BusEvent;
import cn.qatime.player.utils.Constant;
import libraryextra.bean.CashAccountBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.StringUtils;

public class FragmentHomeUserCenter extends BaseFragment implements View.OnClickListener {
    private LinearLayout information;
    private ImageView headSculpture;
    private LinearLayout order;
    private LinearLayout wallet;
    private LinearLayout course;
    private LinearLayout myInteract;
    private LinearLayout myVideo;
    private TextView myTaste;
    private LinearLayout setting;
    private TextView newVersion;
    private TextView name;
    private TextView balance;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_home_user_center, container, false);
        assignViews(view);
        initData();
        newVersion.setVisibility(BaseApplication.getInstance().newVersion ? View.VISIBLE : View.INVISIBLE);
        if (BaseApplication.getInstance().getProfile().getData() != null && BaseApplication.getInstance().getProfile().getData().getUser() != null) {
            Glide.with(getActivity()).load(BaseApplication.getInstance().getProfile().getData().getUser().getEx_big_avatar_url()).placeholder(R.mipmap.personal_information_head).crossFade().transform(new GlideCircleTransform(getActivity())).into(headSculpture);
        }
        name.setText(StringUtils.isNullOrBlanK(BaseApplication.getInstance().getProfile().getData().getUser().getName()) ? "null" : BaseApplication.getInstance().getProfile().getData().getUser().getName());
        order.setOnClickListener(this);
        wallet.setOnClickListener(this);
        course.setOnClickListener(this);
        myInteract.setOnClickListener(this);
        myVideo.setOnClickListener(this);
        myTaste.setOnClickListener(this);
        information.setOnClickListener(this);
        setting.setOnClickListener(this);
        return view;
    }

    private void initData() {
        CashAccountBean cashAccount = BaseApplication.getInstance().getCashAccount();
        if (cashAccount != null && cashAccount.getData() != null) {
            String price = cashAccount.getData().getBalance();
            if (price.startsWith(".")) {
                price = "0" + price;
            }
            balance.setText(price);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.information:
                Intent intent = new Intent(getActivity(), PersonalInformationActivity.class);
                startActivityForResult(intent, Constant.REQUEST);
                break;
            case R.id.my_wallet:
                if (BaseApplication.getInstance().getCashAccount() != null && BaseApplication.getInstance().getCashAccount().getData() != null) {
                    intent = new Intent(getActivity(), PersonalMyWalletActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), R.string.get_wallet_info_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.my_order:
                intent = new Intent(getActivity(), PersonalMyOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.my_course:
                intent = new Intent(getActivity(), PersonalMyTutorshipActivity.class);
                startActivity(intent);
                break;
            case R.id.my_interact:
                intent = new Intent(getActivity(), PersonalMyInteractActivity.class);
                startActivity(intent);
                break;
            case R.id.my_video:
                intent = new Intent(getActivity(), PersonalMyVideoActivity.class);
                startActivity(intent);
                break;
            case R.id.my_taste:
                intent = new Intent(getActivity(), PersonalMyTasteActivity.class);
                startActivity(intent);
                break;
            case R.id.my_exclusive:
                intent = new Intent(getActivity(), PersonalMyExclusiveActivity.class);
                startActivity(intent);
                break;
            case R.id.my_homework:
                intent = new Intent(getActivity(), PersonalMyHomeworkActivity.class);
                startActivity(intent);
                break;
            case R.id.my_question:
                intent = new Intent(getActivity(), PersonalMyQuestionActivity.class);
                startActivity(intent);
                break;
            case R.id.download_manager:
                intent = new Intent(getActivity(), DownloadManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:// 设置
                intent = new Intent(getActivity(), SystemSettingActivity.class);
                getActivity().startActivity(intent);
                break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            Glide.with(getActivity()).load(BaseApplication.getInstance().getProfile().getData().getUser().getEx_big_avatar_url()).placeholder(R.mipmap.personal_information_head).crossFade().transform(new GlideCircleTransform(getActivity())).into(headSculpture);
            name.setText(StringUtils.isNullOrBlanK(BaseApplication.getInstance().getProfile().getData().getUser().getName()) ? " " : BaseApplication.getInstance().getProfile().getData().getUser().getName());
        }
    }

    @Subscribe
    public void onEvent(BusEvent event) {
        if (BusEvent.ON_REFRESH_CASH_ACCOUNT == event)
            initData();
    }

    private void assignViews(View view) {
        information = (LinearLayout) view.findViewById(R.id.information);
        headSculpture = (ImageView) view.findViewById(R.id.head_sculpture);
        name = (TextView) view.findViewById(R.id.name);
        balance = (TextView) view.findViewById(R.id.balance);
        order = (LinearLayout) view.findViewById(R.id.my_order);
        wallet = (LinearLayout) view.findViewById(R.id.my_wallet);
        course = (LinearLayout) view.findViewById(R.id.my_course);
        myInteract = (LinearLayout) view.findViewById(R.id.my_interact);
        myVideo = (LinearLayout) view.findViewById(R.id.my_video);
        view.findViewById(R.id.my_exclusive).setOnClickListener(this);
        view.findViewById(R.id.download_manager).setOnClickListener(this);
        view.findViewById(R.id.my_homework).setOnClickListener(this);
        view.findViewById(R.id.my_question).setOnClickListener(this);
        myTaste = (TextView) view.findViewById(R.id.my_taste);
        setting = (LinearLayout) view.findViewById(R.id.setting);
        newVersion = (TextView) view.findViewById(R.id.new_version);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
