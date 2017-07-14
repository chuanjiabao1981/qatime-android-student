package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orhanobut.logger.Logger;

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
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.bean.RefundRecordBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2017/1/4 13:59
 * @Description:
 */
public class FragmentFundRecordRefund extends BaseFragment {
    private PullToRefreshListView listView;
    private List<RefundRecordBean.DataBean> data = new ArrayList<>();
    private CommonAdapter<RefundRecordBean.DataBean> adapter;
    DecimalFormat df = new DecimalFormat("#.00");
    private int page = 1;
    SimpleDateFormat parseISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fund_record_refund, container, false);
        EventBus.getDefault().register(this);
        initview(view);
        initOver = true;
        return view;
    }

    @Override
    public void onShow() {
        if (!isLoad) {
            if (initOver) {
                initData(1);
            } else {
                super.onShow();
            }
        }
    }

    private void initData(final int loadType) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getInstance().getUserId() + "/refunds", map), null, new VolleyListener(getActivity()) {

            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                RefundRecordBean bean = JsonUtils.objectFromJson(response.toString(), RefundRecordBean.class);
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
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));
        View empty = View.inflate(getActivity(), R.layout.empty_view, null);
        listView.setEmptyView(empty);

        adapter = new CommonAdapter<RefundRecordBean.DataBean>(getActivity(), data, R.layout.item_fragment_fund_record4) {

            @Override
            public void convert(ViewHolder helper, RefundRecordBean.DataBean item, int position) {
                helper.setText(R.id.id, item.getTransaction_no());
                String price = df.format(Double.valueOf(item.getAmount()));
                if (price.startsWith(".")) {
                    price = "0" + price;
                }
                helper.setText(R.id.money_amount, "￥" + price);
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
                RefundRecordBean.DataBean dataBean = data.get(position - 1);
                String status = dataBean.getStatus();
                if ("init".equals(status)) {//如果是退款中可取消退款
                    dialog(dataBean);
                }
            }
        });

    }

    protected void dialog(final RefundRecordBean.DataBean item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(getActivity(), R.layout.dialog_cancel_or_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(R.string.confirm_cancel_refund);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRefund(item);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
//        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
//        attributes.width= ScreenUtils.getScreenWidth(getActivity())- DensityUtils.dp2px(getActivity(),20)*2;
//        alertDialog.getWindow().setAttributes(attributes);
    }

    private void cancelRefund(RefundRecordBean.DataBean item) {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.urlpayment + BaseApplication.getInstance().getUserId() + "/refunds/" + item.getTransaction_no() + "/cancel", null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Toast.makeText(getActivity(), R.string.cancel_refund_success, Toast.LENGTH_SHORT).show();
                        initData(1);
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(getActivity(), R.string.cancel_refund_error, Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                Logger.e(volleyError.getMessage());
            }
        });
        addToRequestQueue(request);
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
                return getString(R.string.refund_to_weixin);
            case "alipay":
                return getString(R.string.refund_to_alipay);
            case "offline":
            default:
                return getString(R.string.refund_to_account);
        }
    }

    private String getStatus(String status) {
        switch (status) {
            case "init":
                return getString(R.string.refunding);
            case "cancel":
                return getString(R.string.cancelled);
            case "ignored":
                return "审核失败";
            case "refunded":
                return getString(R.string.refunded);
        }
        return "";
    }

}
