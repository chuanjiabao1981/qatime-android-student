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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.text.DecimalFormat;

import cn.qatime.player.R;
import cn.qatime.player.activity.PersonalInformationActivity;
import cn.qatime.player.activity.PersonalMyOrderActivity;
import cn.qatime.player.activity.PersonalMyTutorshipActivity;
import cn.qatime.player.activity.PersonalMyWalletActivity;
import cn.qatime.player.activity.SecurityManagerActivity;
import cn.qatime.player.activity.SystemSettingActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.CashAccountBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyListener;

public class FragmentHomeUserCenter extends BaseFragment implements View.OnClickListener {
    private LinearLayout information;
    private ImageView banner;
    private ImageView headSculpture;
    private LinearLayout order;
    private LinearLayout wallet;
    private LinearLayout course;
    private LinearLayout security;
    private LinearLayout setting;
    private TextView newVersion;
    private TextView name;
    private TextView balance;
    DecimalFormat df = new DecimalFormat("#.00");

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_user_center, container, false);
        assignViews(view);
        newVersion.setVisibility(BaseApplication.newVersion ? View.VISIBLE : View.INVISIBLE);
        if (BaseApplication.getProfile().getData() != null && BaseApplication.getProfile().getData().getUser() != null) {
            Glide.with(getActivity()).load(BaseApplication.getProfile().getData().getUser().getEx_big_avatar_url()).placeholder(R.mipmap.personal_information_head).crossFade().transform(new GlideCircleTransform(getActivity())).into(headSculpture);
        }
        name.setText(StringUtils.isNullOrBlanK(BaseApplication.getProfile().getData().getUser().getName()) ? "null" : BaseApplication.getProfile().getData().getUser().getName());
        refreshCashAccount();
        order.setOnClickListener(this);
        wallet.setOnClickListener(this);
        course.setOnClickListener(this);
        information.setOnClickListener(this);

        security.setOnClickListener(this);
        setting.setOnClickListener(this);
        return view;
    }

    private void initData() {
        CashAccountBean cashAccount = BaseApplication.getCashAccount();
        if (cashAccount != null && cashAccount.getData() != null) {
            String price = cashAccount.getData().getBalance();
            if (price.startsWith(".")) {
                price = "0" + price;
            }
            balance.setText(price);
        } else {
            refreshCashAccount();
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
                intent = new Intent(getActivity(), PersonalMyWalletActivity.class);
                startActivityForResult(intent, Constant.REQUEST);
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
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            Glide.with(getActivity()).load(BaseApplication.getProfile().getData().getUser().getEx_big_avatar_url()).placeholder(R.mipmap.personal_information_head).crossFade().transform(new GlideCircleTransform(getActivity())).into(headSculpture);
            name.setText(StringUtils.isNullOrBlanK(BaseApplication.getProfile().getData().getUser().getName()) ? "姓名" : BaseApplication.getProfile().getData().getUser().getName());
            initData();
            CashAccountBean cashAccount = BaseApplication.getCashAccount();
            if (cashAccount != null && cashAccount.getData() != null) {
                String price = cashAccount.getData().getBalance();
                if (price.startsWith(".")) {
                    price = "0" + price;
                }
                balance.setText(price);
            } else {
                refreshCashAccount();
            }
        }
    }

    private void refreshCashAccount() {
        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.urlpayment + BaseApplication.getUserId() + "/cash", null, new VolleyListener(getActivity()) {

            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                CashAccountBean cashAccount = JsonUtils.objectFromJson(response.toString(), CashAccountBean.class);
                BaseApplication.setCashAccount(cashAccount);
                initData();
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(getActivity(), getResourceString(R.string.get_wallet_info_error), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void assignViews(View view) {
        information = (LinearLayout) view.findViewById(R.id.information);
        banner = (ImageView) view.findViewById(R.id.banner);
        headSculpture = (ImageView) view.findViewById(R.id.head_sculpture);
        name = (TextView) view.findViewById(R.id.name);
        balance = (TextView) view.findViewById(R.id.balance);
        order = (LinearLayout) view.findViewById(R.id.my_order);
        wallet = (LinearLayout) view.findViewById(R.id.my_wallet);
        course = (LinearLayout) view.findViewById(R.id.my_course);
        security = (LinearLayout) view.findViewById(R.id.security);
        setting = (LinearLayout) view.findViewById(R.id.setting);
        newVersion = (TextView) view.findViewById(R.id.new_version);
    }
}
