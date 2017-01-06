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

import com.android.volley.VolleyError;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.PersonalMyOrderCanceledDetailActivity;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.MyOrderBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.OrderDetailBean;
import libraryextra.bean.OrderPayBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentOrderCanceled extends BaseFragment {
    private PullToRefreshListView listView;
    private java.util.List<MyOrderBean.Data> list = new ArrayList<>();
    private CommonAdapter<MyOrderBean.Data> adapter;
    private int page = 1;
    DecimalFormat df = new DecimalFormat("#.00");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_canceled, container, false);
        initview(view);
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));

        adapter = new CommonAdapter<MyOrderBean.Data>(getActivity(), list, R.layout.item_fragment_personal_my_order3) {
            @Override
            public void convert(ViewHolder helper, final MyOrderBean.Data item, final int position) {
                StringBuilder sp = new StringBuilder();
                sp.append(item.getProduct().getGrade()).append(item.getProduct().getSubject()).append("/共").append(item.getProduct().getPreset_lesson_count())
                        .append("课/").append(item.getProduct().getTeacher_name());
                helper.setText(R.id.classname, item.getProduct().getName())
                        .setText(R.id.describe, sp.toString());
                if (item.getStatus().equals("refunded")) {//交易关闭
                    helper.setText(R.id.status, "已退款");
                } else if (item.getStatus().equals("canceled")) {//交易关闭
                    helper.setText(R.id.status, getResourceString(R.string.deal_closed));
                } else if (item.getStatus().equals("expired")) {//交易关闭
                    helper.setText(R.id.status, getResourceString(R.string.deal_closed));
                } else {//已取消
                    helper.setText(R.id.status, "        ");
                }
                String price = item.getAmount();
                if (price.startsWith(".")) {
                    price = "0" + price;
                }
                helper.setText(R.id.price, "￥" + price);

                helper.getView(R.id.reorder).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                                intent.putExtra("id", item.getProduct().getId());
                                intent.putExtra("page", 0);
                                startActivity(intent);
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
                Intent intent = new Intent(getActivity(), PersonalMyOrderCanceledDetailActivity.class);
                Logger.e(list.get(position - 1).getId());
                intent.putExtra("id", list.get(position - 1).getProduct().getId());
                intent.putExtra("order_id", list.get(position - 1).getId());
                intent.putExtra("created_at", list.get(position - 1).getCreated_at());
                intent.putExtra("payType", list.get(position - 1).getPay_type());
                OrderPayBean payBean = new OrderPayBean();//重新下单数据
                payBean.image = list.get(position - 1).getProduct().getPublicize();
                payBean.name = list.get(position - 1).getProduct().getName();
                payBean.subject = list.get(position - 1).getProduct().getSubject();
                payBean.grade = list.get(position - 1).getProduct().getGrade();
                payBean.classnumber = list.get(position - 1).getProduct().getPreset_lesson_count();
                payBean.teacher = list.get(position - 1).getProduct().getTeacher_name();
                payBean.classendtime = list.get(position - 1).getProduct().getLive_end_time();
                payBean.classstarttime = list.get(position - 1).getProduct().getLive_start_time();
                if (StringUtils.isNullOrBlanK(list.get(position - 1).getProduct().getStatus())) {
                    payBean.status = " ";
                } else {
                    payBean.status = list.get(position - 1).getProduct().getStatus();
                }
                payBean.current_price = list.get(position - 1).getProduct().getCurrent_price();
                intent.putExtra("pay_data", payBean);

                OrderDetailBean bean = new OrderDetailBean();//订单详情数据
                bean.image = list.get(position - 1).getProduct().getPublicize();
                bean.name = list.get(position - 1).getProduct().getName();
                bean.subject = list.get(position - 1).getProduct().getSubject();
                bean.grade = list.get(position - 1).getProduct().getGrade();
                bean.status = list.get(position - 1).getStatus();
                bean.teacher = list.get(position - 1).getProduct().getTeacher_name();
                bean.Preset_lesson_count = list.get(position - 1).getProduct().getPreset_lesson_count();
                bean.Completed_lesson_count = list.get(position - 1).getProduct().getCompleted_lesson_count();
                bean.current_price = list.get(position - 1).getProduct().getCurrent_price();
                bean.amount = list.get(position-1).getAmount();
                intent.putExtra("data", bean);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onShow() {
        if (!isLoad) {
            initData(1);
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
        map.put("cate", "canceled");

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
                            if (data != null && data.getData() != null) {
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
        }

        );

        addToRequestQueue(request);
    }
}
