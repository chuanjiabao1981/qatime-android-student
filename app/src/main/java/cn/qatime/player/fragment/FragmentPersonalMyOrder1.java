package cn.qatime.player.fragment;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
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
import cn.qatime.player.activity.OrderPayActivity;
import cn.qatime.player.activity.PersonalMyOrderUnpaidDetailActivity;
import cn.qatime.player.bean.MyOrderBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.OrderDetailBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentPersonalMyOrder1 extends BaseFragment {
    private PullToRefreshListView listView;
    private java.util.List<MyOrderBean.Data> list = new ArrayList<>();
    private CommonAdapter<MyOrderBean.Data> adapter;
    private int page = 1;
    DecimalFormat df = new DecimalFormat("#.00");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_my_order1, container, false);
        initview(view);
        initData(1);
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

        adapter = new CommonAdapter<MyOrderBean.Data>(getActivity(), list, R.layout.item_fragment_personal_my_order1) {
            @Override
            public void convert(ViewHolder helper, MyOrderBean.Data item, final int position) {
                Glide.with(getActivity()).load(item.getProduct().getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().into((ImageView) helper.getView(R.id.image));
                helper.setText(R.id.classname, item.getProduct().getName());
                if (StringUtils.isNullOrBlanK(item.getProduct().getGrade())) {
                    helper.setText(R.id.grade, "年级");
                } else {
                    helper.setText(R.id.grade, item.getProduct().getGrade());
                }

                if (StringUtils.isNullOrBlanK(item.getProduct().getSubject())) {
                    helper.setText(R.id.subject, "科目");
                } else {
                    helper.setText(R.id.subject, item.getProduct().getSubject());
                }
                Logger.e(item.getProduct().getSubject());

                helper.setText(R.id.teacher, item.getProduct().getTeacher_name());

                helper.setText(R.id.progress, item.getProduct().getCompleted_lesson_count() + "/" + item.getProduct().getPreset_lesson_count());//进度

                if (item.getStatus().equals("unpaid")) {//待付款
                    helper.setText(R.id.status, getActivity().getResources().getString(R.string.waiting_for_payment));
                } else if (item.getStatus().equals("paid")) {//已付款
                    helper.setText(R.id.status, getActivity().getResources().getString(R.string.deal_done));
                } else {//已取消
                    helper.setText(R.id.status, getActivity().getResources().getString(R.string.deal_closed));
                }
                String price = df.format(item.getProduct().getPrice());
                if (price.startsWith(".")) {
                    price = "0" + price;
                }
                helper.setText(R.id.price, "￥" + price);


                helper.getView(R.id.pay).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), OrderPayActivity.class);
                                intent.putExtra("id", list.get(position).getProduct().getId());
                                intent.putExtra("price", list.get(position).getProduct().getPrice());
                                intent.putExtra("payType", "1");
                                startActivity(intent);
                            }
                        });
                helper.getView(R.id.cancel_order).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id = list.get(position).getId();
                                dialog(position, id);
                            }
                        });

            }

        };
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
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
                }, 200);
                initData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
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
                }, 200);
                initData(2);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PersonalMyOrderUnpaidDetailActivity.class);
                intent.putExtra("id", list.get(position - 1).getId());
                OrderDetailBean bean = new OrderDetailBean();
                bean.id = list.get(position - 1).getProduct().getId();
                bean.image = list.get(position - 1).getProduct().getPublicize();
                bean.name = list.get(position - 1).getProduct().getName();
                bean.subject = list.get(position - 1).getProduct().getSubject();
                bean.grade = list.get(position - 1).getProduct().getGrade();
                bean.status = list.get(position - 1).getStatus();
                bean.teacher = list.get(position - 1).getProduct().getTeacher_name();
                bean.Preset_lesson_count = list.get(position - 1).getProduct().getPreset_lesson_count();
                bean.Completed_lesson_count = list.get(position - 1).getProduct().getCompleted_lesson_count();
                bean.price = list.get(position - 1).getProduct().getPrice();
                intent.putExtra("data", bean);
                intent.putExtra("payType", list.get(position - 1).getPay_type());
                intent.putExtra("created_at", list.get(position - 1).getCreated_at());
                startActivity(intent);
            }
        });
    }

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
        map.put("cate", "unpaid");

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

    protected void dialog(final int position, final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认取消订单吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                CancelOrder(position, id);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void CancelOrder(final int position, String id) {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.urlPaylist + "/" + id + "/cancel", null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        list.remove(position);
                        Toast.makeText(getActivity(), "订单取消成功", Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(getActivity(), "取消订单失败，请稍后再试", Toast.LENGTH_SHORT).show();
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


}
