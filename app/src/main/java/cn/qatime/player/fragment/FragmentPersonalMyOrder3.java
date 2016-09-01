package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import cn.qatime.player.activity.OrderConfirmActivity;
import cn.qatime.player.activity.OrderPayActivity;
import cn.qatime.player.activity.PersonalMyOrderDetailActivity;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.MyOrderBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.OrderDetailBean;
import libraryextra.bean.OrderPayBean;
import libraryextra.bean.TutorialClassBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentPersonalMyOrder3 extends BaseFragment {
    private PullToRefreshListView listView;
    private java.util.List<MyOrderBean.Data> list = new ArrayList<>();
    private CommonAdapter<MyOrderBean.Data> adapter;
    private int page = 1;
    private int id;
    DecimalFormat df = new DecimalFormat("#.00");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_my_order3, container, false);
        initview(view);
        initData(1);
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);

        adapter = new CommonAdapter<MyOrderBean.Data>(getActivity(), list, R.layout.item_fragment_personal_my_order3) {
            @Override
            public void convert(ViewHolder helper, MyOrderBean.Data item, final int position) {
                Glide.with(getActivity()).load(item.getProduct().getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().into((ImageView) helper.getView(R.id.image));
                helper.setText(R.id.classname, item.getProduct().getName());
                helper.setText(R.id.grade, item.getProduct().getGrade());
                helper.setText(R.id.subject, item.getProduct().getSubject());
                helper.setText(R.id.teacher, item.getProduct().getTeacher_name());
                helper.setText(R.id.progress, item.getProduct().getCompleted_lesson_count() + "/" + item.getProduct().getPreset_lesson_count());//进度
                if (item.getStatus().equals("unpaid")) {//待付款
                    helper.setText(R.id.status, getResources().getString(R.string.paying));
                } else if (item.getStatus().equals("paid")) {//已付款
                    helper.setText(R.id.status, getResources().getString(R.string.paid));
                } else {//已取消
                    helper.setText(R.id.status, getResources().getString(R.string.cancelled));
                }
                String price = df.format(item.getProduct().getPrice());
                if (price.startsWith(".")) {
                    price = "0" + price;
                }
                helper.setText(R.id.price, "￥" + price);

                helper.getView(R.id.reorder).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), OrderConfirmActivity.class);
                                intent.putExtra("id", list.get(position).getProduct().getId());
                                OrderPayBean bean = new OrderPayBean();
                                bean.image = list.get(position).getProduct().getPublicize();
                                bean.name = list.get(position).getProduct().getName();
                                bean.subject = list.get(position).getProduct().getSubject();
                                bean.grade = list.get(position).getProduct().getGrade();
                                bean.classnumber = list.get(position).getProduct().getPreset_lesson_count();
                                bean.teacher = list.get(position).getProduct().getTeacher_name();
                                bean.classendtime = list.get(position).getProduct().getLive_end_time();
                                bean.classstarttime = list.get(position).getProduct().getLive_start_time();
                                if (StringUtils.isNullOrBlanK(list.get(position).getProduct().getStatus())) {
                                    bean.status = " ";
                                } else {
                                    bean.status = list.get(position).getProduct().getStatus();
                                }
                                Logger.e(list.get(position).getProduct().getStatus());
                                bean.price = list.get(position).getProduct().getPrice();
                                intent.putExtra("data", bean);

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
                Intent intent = new Intent(getActivity(), PersonalMyOrderDetailActivity.class);
                Logger.e(list.get(position - 1).getId());

                intent.putExtra("id", list.get(position - 1).getId());

                OrderDetailBean bean = new OrderDetailBean();
                bean.image = list.get(position - 1).getProduct().getPublicize();
                bean.name = list.get(position - 1).getProduct().getName();
                bean.subject = list.get(position - 1).getProduct().getSubject();
                bean.grade = list.get(position - 1).getProduct().getGrade();
                bean.teacher = list.get(position - 1).getProduct().getTeacher_name();
                bean.Preset_lesson_count = list.get(position - 1).getProduct().getPreset_lesson_count();
                bean.Completed_lesson_count = list.get(position - 1).getProduct().getCompleted_lesson_count();
                bean.price = list.get(position - 1).getProduct().getPrice();
                intent.putExtra("data", bean);
                startActivity(intent);
            }
        });
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
                            if (data != null) {
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
}
