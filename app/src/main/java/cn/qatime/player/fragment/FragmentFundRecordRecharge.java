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
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import cn.qatime.player.utils.SPUtils;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
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
    SimpleDateFormat parseISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fund_record_recharge, container, false);
        EventBus.getDefault().register(this);
        initview(view);
        initOver=true;
        return view;
    }

    @Override
    public void onShow() {
        if (!isLoad) {
            if(initOver){
                initData(1);
            }else{
                super.onShow();
            }
        }
    }

    private void initData(final int loadType) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getInstance().getUserId() + "/recharges", map), null, new VolleyListener(getActivity()) {

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
                if (bean != null) {
                    data.addAll(bean.getData());
                    adapter.notifyDataSetChanged();
                }
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
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));
        View empty = View.inflate(getActivity(),R.layout.empty_view,null);
        listView.setEmptyView(empty);

        adapter = new CommonAdapter<RechargeRecordBean.DataBean>(getActivity(), data, R.layout.item_fragment_fund_record1) {

            @Override
            public void convert(ViewHolder helper, RechargeRecordBean.DataBean item, int position) {
                helper.setText(R.id.id, item.getId());
                String price = df.format(Double.valueOf(item.getAmount()));
                if (price.startsWith(".")) {
                    price = "0" + price;
                }
                helper.setText(R.id.money_amount, "+￥" + price);
                try {
                    helper.setText(R.id.time, parse.format(parseISO.parse(item.getCreated_at())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
                    if (dataBean.getPay_type().equals("weixin")) {
                        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), null);
                        if (!api.isWXAppInstalled()) {
                            Toast.makeText(getActivity(), R.string.wechat_not_installed, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else if (dataBean.getPay_type().equals("alipay")) {
                        return;
                    }
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
            initData(1);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private String getPayType(String pay_type) {
        switch (pay_type) {
            case "weixin":
                return getResourceString(R.string.pay_wexin);
            case "alipay":
                return getResourceString(R.string.alipay);
            case "offline":
            default:
                return getResourceString(R.string.pay_offline);
        }
    }

    private String getStatus(String status) {
        switch (status) {
            case "unpaid":
                return getString(R.string.unpaid);
            case "received":
                return getString(R.string.recharge_success);
            default:
                return getString(R.string.deal_closed);
        }
    }

}
