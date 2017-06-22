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
import org.json.JSONException;
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
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentOrderPaid extends BaseFragment {
    private PullToRefreshListView listView;
    private java.util.List<MyOrderBean.DataBean> list = new ArrayList<>();
    private CommonAdapter<MyOrderBean.DataBean> adapter;
    private int page = 1;
    DecimalFormat df = new DecimalFormat("#.00");


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_paid, container, false);
        initview(view);
        initOver = true;
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        View empty = View.inflate(getActivity(), R.layout.empty_view, null);
        listView.setEmptyView(empty);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));

        adapter = new CommonAdapter<MyOrderBean.DataBean>(getActivity(), list, R.layout.item_fragment_personal_my_order2) {
            @Override
            public void convert(ViewHolder helper, final MyOrderBean.DataBean item, int position) {

                StringBuilder sp = new StringBuilder();
                if ("LiveStudio::Course".equals(item.getProduct_type())) {
                    sp.append("直播课/");
                    sp.append(item.getProduct().getGrade()).append(item.getProduct().getSubject()).append("/共").append(item.getProduct().getPreset_lesson_count())
                            .append("课/").append(item.getProduct().getTeacher_name());
                    helper.setText(R.id.classname, item.getProduct().getName())
                            .setText(R.id.describe, sp.toString());
                } else if ("LiveStudio::InteractiveCourse".equals(item.getProduct_type())) {
                    sp.append("一对一/");
                    sp.append(item.getProduct_interactive_course().getGrade()).append(item.getProduct_interactive_course().getSubject()).append("/共").append(item.getProduct_interactive_course().getLessons_count())
                            .append("课/").append(item.getProduct_interactive_course().getTeachers().get(0).getName());
                    if (item.getProduct_interactive_course().getTeachers().size() > 1) {
                        sp.append("...");
                    }
                    helper.setText(R.id.classname, item.getProduct_interactive_course().getName())
                            .setText(R.id.describe, sp.toString());
                } else if ("LiveStudio::VideoCourse".equals(item.getProduct_type())) {
                    sp.append("视频课/");
                    sp.append(item.getProduct_video_course().getGrade())
                            .append(item.getProduct_video_course().getSubject())
                            .append("/共").append(item.getProduct_video_course().getPreset_lesson_count()).append("课")
                            .append("/").append(item.getProduct_video_course().getTeacher().getName());
                    helper.setText(R.id.classname, item.getProduct_video_course().getName())
                            .setText(R.id.describe, sp.toString());
                }


                TextView refund = helper.getView(R.id.apply_refund);
                if (item.getStatus().equals("refunding")) {//正在交易
                    helper.setText(R.id.status, getString(R.string.refunding));
                    refund.setText(R.string.cancel_refund);
                    refund.setTextColor(0xffaaaaaa);
                    refund.setBackgroundResource(R.drawable.button_background_light);
                    //                    android:background="@drawable/button_background"
                } else {
                    refund.setText(R.string.apply_refund);
                    refund.setTextColor(0xffff5842);
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
                intent.putExtra("data", list.get(position - 1));
                startActivityForResult(intent, Constant.REQUEST);
            }
        });

    }

    protected void dialog(final MyOrderBean.DataBean item) {
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

    private void cancelRefund(MyOrderBean.DataBean item) {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.urlpayment + BaseApplication.getInstance().getUserId() + "/refunds/" + item.getId() + "/cancel", null,
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

    private void applyRefund(final MyOrderBean.DataBean item) {
        if ("LiveStudio::VideoCourse".equals(item.getProduct_type())) {
            Toast.makeText(getActivity(), "此订单类型暂不支持退款", Toast.LENGTH_SHORT).show();
            return;
        } else if ("LiveStudio::InteractiveCourse".equals(item.getProduct_type()) && item.getProduct_interactive_course().getStatus().equals(Constant.CourseStatus.completed)) {
            Toast.makeText(getActivity(), "已结束的课程不能申请退款", Toast.LENGTH_SHORT).show();
            return;
        } else if ("LiveStudio::Course".equals(item.getProduct_type()) && item.getProduct().getStatus().equals(Constant.CourseStatus.completed)) {
            Toast.makeText(getActivity(), "已结束的课程不能申请退款", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("order_id", item.getId());
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getInstance().getUserId() + "/refunds/info", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        try {
                            if (response.getJSONObject("data").getDouble("refund_amount") == 0) {
                                Toast.makeText(getActivity(), R.string.not_enough_amount_of_refund, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Intent intent = new Intent(getActivity(), ApplyRefundActivity.class);
                        intent.putExtra("response", response.toString());
                        intent.putExtra("order_id", item.getId());
                        if ("LiveStudio::Course".equals(item.getProduct_type())) {
                            intent.putExtra("name", item.getProduct().getName());
                            intent.putExtra("preset_lesson_count", item.getProduct().getPreset_lesson_count());
                            intent.putExtra("closed_lessons_count", item.getProduct().getStarted_lessons_count());
                        } else if ("LiveStudio::InteractiveCourse".equals(item.getProduct_type())) {
                            intent.putExtra("name", item.getProduct_interactive_course().getName());
                            intent.putExtra("preset_lesson_count", item.getProduct_interactive_course().getLessons_count());
                            intent.putExtra("closed_lessons_count", item.getProduct_interactive_course().getStarted_lessons_count());
                        } else if ("LiveStudio::VideoCourse".equals(item.getProduct_type())) {
//                            intent.putExtra("name",item.getProduct_video_course().getName());
//                            intent.putExtra("preset_lesson_count",item.getProduct_video_course().getPreset_lesson_count());
//                            intent.putExtra("closed_lessons_count",item.getProduct_video_course().getClosed_lessons_count());
                            Logger.e("error");
                            return;
                        }

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
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
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
            } else {
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
