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
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.OrderPayActivity;
import cn.qatime.player.activity.PersonalMyOrderUnpaidDetailActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.MyOrderBean;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentOrderUnpaid extends BaseFragment {
    private PullToRefreshListView listView;
    private java.util.List<MyOrderBean.DataBean> list = new ArrayList<>();
    private CommonAdapter<MyOrderBean.DataBean> adapter;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_unpaid, container, false);
        EventBus.getDefault().register(this);
        initview(view);
        initOver=true;
        return view;

    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        View empty = View.inflate(getActivity(),R.layout.empty_view,null);
        listView.setEmptyView(empty);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));

        adapter = new CommonAdapter<MyOrderBean.DataBean>(getActivity(), list, R.layout.item_fragment_personal_my_order1) {
            @Override
            public void convert(ViewHolder helper, final MyOrderBean.DataBean item, final int position) {
                StringBuilder sp = new StringBuilder();
                if("LiveStudio::Course".equals(item.getProduct_type())){
                    sp.append("直播课/");
                    sp.append(item.getProduct().getGrade())
                            .append(item.getProduct().getSubject())
                            .append("/共").append(item.getProduct().getPreset_lesson_count()).append("课")
                            .append("/").append(item.getProduct().getTeacher_name());
                    helper.setText(R.id.classname, item.getProduct().getName())
                            .setText(R.id.describe, sp.toString());
                }else if("LiveStudio::InteractiveCourse".equals(item.getProduct_type())){
                    sp.append("一对一/");
                    sp.append(item.getProduct_interactive_course().getGrade())
                            .append(item.getProduct_interactive_course().getSubject())
                            .append("/共").append(item.getProduct_interactive_course().getLessons_count()).append("课")
                            .append("/").append(item.getProduct_interactive_course().getTeachers().get(0).getName());
                    if(item.getProduct_interactive_course().getTeachers().size()>1){
                        sp.append("...");
                    }
                    helper.setText(R.id.classname, item.getProduct_interactive_course().getName())
                            .setText(R.id.describe, sp.toString());
                }else if("LiveStudio::VideoCourse".equals(item.getProduct_type())){
                    sp.append("视频课/");
                    sp.append(item.getProduct_video_course().getGrade())
                            .append(item.getProduct_video_course().getSubject())
                            .append("/共").append(item.getProduct_video_course().getPreset_lesson_count()).append("课")
                            .append("/").append(item.getProduct_video_course().getTeacher().getName());
                    helper.setText(R.id.classname, item.getProduct_video_course().getName())
                            .setText(R.id.describe, sp.toString());
                }



                if (item.getStatus().equals("unpaid")) {//待付款
                    helper.setText(R.id.status, getResourceString(R.string.waiting_for_payment));
                } else if (item.getStatus().equals("paid")) {//已付款
                    helper.setText(R.id.status, getResourceString(R.string.deal_done));
                } else {//已取消
                    helper.setText(R.id.status, getResourceString(R.string.deal_closed));
                }
                String price = item.getAmount();
                if (price.startsWith(".")) {
                    price = "0" + price;
                }
                helper.setText(R.id.price, "￥" + price);

                helper.getView(R.id.pay).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (item.getPay_type().equals("weixin")) {
                                    IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), null);
                                    if (!api.isWXAppInstalled()) {
                                        Toast.makeText(getActivity(), R.string.wechat_not_installed, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } else if (item.getPay_type().equals("alipay")) {
                                    return;
                                } else if (item.getPay_type().equals("account")) {
                                    if (Double.valueOf(item.getAmount()) > Double.valueOf(BaseApplication.getCashAccount().getData().getBalance())) {
                                        Toast.makeText(getActivity(), R.string.amount_not_enough, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                Intent intent = new Intent(getActivity(), OrderPayActivity.class);
                                if (item.getPay_type().equals("weixin")) {
                                    intent.putExtra("data", item.getApp_pay_params());
                                } else if (item.getPay_type().equals("alipay")) {
                                    intent.putExtra("data", item.getApp_pay_str());
                                }
                                intent.putExtra("id", item.getId());
                                intent.putExtra("time", item.getCreated_at());
                                intent.putExtra("price",item.getAmount());
                                intent.putExtra("type", item.getPay_type());
                                startActivity(intent);
                            }
                        });
                helper.getView(R.id.cancel_order).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id = list.get(position).getId();
                                dialog(id);
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
                Intent intent = new Intent(getActivity(), PersonalMyOrderUnpaidDetailActivity.class);
                intent.putExtra("data", list.get(position - 1));
                startActivityForResult(intent, Constant.REQUEST);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            Logger.e("订单取消返回");
            initData(1);
        }
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
                        Toast.makeText(getActivity(), R.string.get_order_info_error, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
        addToRequestQueue(request);
    }

    protected void dialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(getActivity(), R.layout.dialog_cancel_or_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText("是否确认取消此订单");
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
                CancelOrder(id);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
//        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
//        attributes.width= ScreenUtils.getScreenWidth(getActivity())- DensityUtils.dp2px(getActivity(),20)*2;
//        alertDialog.getWindow().setAttributes(attributes);
    }

    private void CancelOrder(String id) {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.urlPaylist + "/" + id + "/cancel", null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                       initData(1);
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(getActivity(), getResourceString(R.string.order_cancel_failed), Toast.LENGTH_SHORT).show();
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
            initData(1);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
