package cn.qatime.player.fragment;

import android.content.Intent;
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
import com.android.volley.VolleyError;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.ApplyRefundActivity;
import cn.qatime.player.activity.PersonalMyOrderPaidDetailActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.MyOrderBean;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.OrderDetailBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentOrderPaid extends BaseFragment {
    private PullToRefreshListView listView;
    private java.util.List<MyOrderBean.Data> list = new ArrayList<>();
    private CommonAdapter<MyOrderBean.Data> adapter;
    private int page = 1;
    DecimalFormat df = new DecimalFormat("#.00");


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_paid, container, false);
        initview(view);
        initOver=true;
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        View empty = View.inflate(getActivity(),R.layout.empty_view,null);
        TextView textEmpty = (TextView) empty.findViewById(R.id.text_empty);
        textEmpty.setText("未找到相关订单");
        listView.setEmptyView(empty);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));

        adapter = new CommonAdapter<MyOrderBean.Data>(getActivity(), list, R.layout.item_fragment_personal_my_order2) {
            @Override
            public void convert(ViewHolder helper, final MyOrderBean.Data item, int position) {
                StringBuilder sp = new StringBuilder();
                sp.append(item.getProduct().getGrade()).append(item.getProduct().getSubject()).append("/共").append(item.getProduct().getPreset_lesson_count())
                        .append("课/").append(item.getProduct().getTeacher_name());
                helper.setText(R.id.classname, item.getProduct().getName())
                        .setText(R.id.describe, sp.toString());

                TextView refund = helper.getView(R.id.apply_refund);
                if (item.getStatus().equals("refunding")) {//正在交易
                    helper.setText(R.id.status, "退款中");
                    refund.setText(R.string.cancel_refund);
                    refund.setTextColor(0xffaaaaaa);
                    refund.setBackgroundResource(R.drawable.button_background_light);
                    //                    android:background="@drawable/button_background"
                } else {
                    refund.setText(R.string.apply_refund);
                    refund.setTextColor(0xffbe0b0b);
                    refund.setBackgroundResource(R.drawable.button_background_normal);
                    if (item.getStatus().equals("shipped")) {//正在交易
                        helper.setText(R.id.status, getResourceString(R.string.dealing));
                    } else if (item.getStatus().equals("paid")) {//正在交易
                        helper.setText(R.id.status, getResourceString(R.string.dealing));
                    } else if (item.getStatus().equals("completed")) {//交易完成
                        helper.setText(R.id.status, getResourceString(R.string.deal_done));
                    } else {//
                        helper.setText(R.id.status, "  ");
                    }
                }
                String price = item.getAmount();
                if (price.startsWith(".")) {
                    price = "0" + price;
                }
                helper.setText(R.id.price, "￥" + price);

                refund.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ("refunding".equals(item.getStatus())) {
                                    //取消退款
                                    dialog(item);
                                } else {
                                    //获取退款信息
                                    applyRefund(item);
                                }
                            }

                        });
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
                Intent intent = new Intent(getActivity(), PersonalMyOrderPaidDetailActivity.class);
                intent.putExtra("id", list.get(position - 1).getId());
                OrderDetailBean bean = new OrderDetailBean();
                bean.image = list.get(position - 1).getProduct().getPublicize();
                bean.id = list.get(position - 1).getProduct().getId();
                bean.name = list.get(position - 1).getProduct().getName();
                bean.subject = list.get(position - 1).getProduct().getSubject();
                bean.status = list.get(position - 1).getStatus();
                bean.grade = list.get(position - 1).getProduct().getGrade();
                bean.teacher = list.get(position - 1).getProduct().getTeacher_name();
                bean.Preset_lesson_count = list.get(position - 1).getProduct().getPreset_lesson_count();
                bean.Completed_lesson_count = list.get(position - 1).getProduct().getCompleted_lesson_count();
                bean.current_price = list.get(position - 1).getProduct().getCurrent_price();
                bean.amount = list.get(position - 1).getAmount();
                intent.putExtra("data", bean);
                intent.putExtra("payType", list.get(position - 1).getPay_type());
                intent.putExtra("created_at", list.get(position - 1).getCreated_at());
                intent.putExtra("pay_at", list.get(position - 1).getPay_at());
                startActivityForResult(intent,Constant.REQUEST);
            }
        });

    }

    protected void dialog(final MyOrderBean.Data item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(getActivity(), R.layout.dialog_cancel_or_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText("是否确认取消退款申请");
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

    private void cancelRefund(MyOrderBean.Data item) {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.urlpayment + BaseApplication.getUserId() + "/refunds/" + item.getId() + "/cancel", null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Toast.makeText(getActivity(), "取消退款申请成功", Toast.LENGTH_SHORT).show();
                        initData(1);
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(getActivity(), "取消退款申请失败", Toast.LENGTH_SHORT).show();
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

    private void applyRefund(final MyOrderBean.Data item) {
        Map<String, String> map = new HashMap<>();
        map.put("order_id", item.getId());
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getUserId() + "/refunds/info", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Intent intent = new Intent(getActivity(), ApplyRefundActivity.class);
                        intent.putExtra("response", response.toString());
                        intent.putExtra("order_id", item.getId());
                        intent.putExtra("name",item.getProduct().getName());
                        intent.putExtra("preset_lesson_count",item.getProduct().getPreset_lesson_count());
                        intent.putExtra("completed_lesson_count",item.getProduct().getCompleted_lesson_count());
                        startActivityForResult(intent, Constant.REQUEST);
                    }

                    @Override
                    protected void onError(JSONObject response) {
//                        Toast.makeText(getActivity(), getResourceString(R.string.order_cancel_failed), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //需要刷新数据
        if(requestCode==Constant.REQUEST&&resultCode==Constant.RESPONSE){
            initData(1);
        }
    }
    @Subscribe
    public void onEvent(PayResultState code) {
        initData(1);
    }
    @Override
    public void onShow() {
        if (!isLoad) {
            if (initOver) {
                initData(1);
            }else{
                super.onShow();
            }
        }
    }

    /**
     * @param type 1刷新
     *             2加载更多
     */
    private void initData(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("per_page", "10");
        map.put("cate", "paid");

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlPaylist, map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        isLoad = true;
                        Logger.e(response.toString());
                        if (type == 1) {
                            list.clear();
                        }
                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                        listView.onRefreshComplete();

                        try {
                            MyOrderBean data = JsonUtils.objectFromJson(response.toString(), MyOrderBean.class);
                            if (data != null & data.getData() != null) {
                                list.addAll(data.getData());
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
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

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                listView.onRefreshComplete();
            }
        });
        addToRequestQueue(request);
    }
}
