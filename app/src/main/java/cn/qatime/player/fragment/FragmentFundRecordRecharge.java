package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.RechargeConfirmActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.bean.RechargeRecordBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.SPUtils;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2016/9/27 17:17
 * @Description:
 */
public class FragmentFundRecordRecharge extends BaseFragment {
    private PullToRefreshListView listView;
    private List<RechargeRecordBean.DataBean> data = new ArrayList<>();
    private CommonAdapter<RechargeRecordBean.DataBean> adapter;
    DecimalFormat df = new DecimalFormat("#.00");
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fund_record_recharge, container, false);
        EventBus.getDefault().register(this);
        initview(view);
        return view;
    }

    @Override
    public void onShow() {
        if (!isLoad) {
            initData(1);
        }
    }

    private void initData(final int loadType) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getUserId() + "/recharges", map), null, new VolleyListener(getActivity()) {

            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                RechargeRecordBean bean = JsonUtils.objectFromJson(response.toString(), RechargeRecordBean.class);
                isLoad = true;
                if (loadType == 1) {
                    data.clear();
                }
                data.addAll(bean.getData());
                adapter.notifyDataSetChanged();
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                listView.onRefreshComplete();
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(getActivity(), getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
                String label = DateUtils.formatDateTime(
                        getActivity(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                // Update the LastUpdatedLabel
                listView.getLoadingLayoutProxy(false, true)
                        .setLastUpdatedLabel(label);
                listView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
                listView.onRefreshComplete();
            }
        }));
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.getRefreshableView().setDividerHeight(2);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));

        adapter = new CommonAdapter<RechargeRecordBean.DataBean>(getActivity(), data, R.layout.item_fragment_fund_record1) {

            @Override
            public void convert(ViewHolder helper, RechargeRecordBean.DataBean item, int position) {
                helper.setText(R.id.id, item.getId());
                String price = df.format(Double.valueOf(item.getAmount()));
                if (price.startsWith(".")) {
                    price = "0" + price;
                }
                helper.setText(R.id.money_amount, "+￥" + price);
                helper.setText(R.id.time, item.getCreated_at());
                helper.setText(R.id.mode, getPayType(item.getPay_type()));
                helper.setText(R.id.status, getStatus(item.getStatus()));
            }
        };
        listView.setAdapter(adapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                initData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                initData(2);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RechargeRecordBean.DataBean dataBean = data.get(position - 1);
                String status = dataBean.getStatus();
                if ("unpaid".equals(status)) {//如果是未支付进行跳转
                    Intent intent = new Intent(getActivity(), RechargeConfirmActivity.class);
                    intent.putExtra("id", dataBean.getId());
                    intent.putExtra("amount", dataBean.getAmount());
                    intent.putExtra("pay_type", dataBean.getPay_type());
                    intent.putExtra("created_at", dataBean.getCreated_at());
                    // TODO: 2016/10/9  判断是微信还是支付宝
                    intent.putExtra("app_pay_params", dataBean.getApp_pay_params());
                    startActivity(intent);
                    SPUtils.put(getActivity(), "RechargeId", dataBean.getId());
                    SPUtils.put(getActivity(), "amount", dataBean.getAmount());
                }
            }
        });

    }

    @Subscribe
    public void onEvent(PayResultState code) {
        //充值成功刷新订单
        if (!isLoad) {
            initData(1);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private String getPayType(String pay_type) {
        switch (pay_type) {
            case "weixin":
                return "微信支付";
            case "alipay":
                return "支付宝";
            case "offline":
                return "线下支付";
        }
        return "微信支付";
    }

    private String getStatus(String status) {
        switch (status) {
            case "unpaid":
                return "未支付";
            case "received":
                return "充值成功";
            default:
                return "交易关闭";
        }
    }

}
