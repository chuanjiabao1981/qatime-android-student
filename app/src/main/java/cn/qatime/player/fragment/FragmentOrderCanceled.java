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
import android.widget.TextView;

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
import cn.qatime.player.activity.InteractCourseDetailActivity;
import cn.qatime.player.activity.PersonalMyOrderCanceledDetailActivity;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.activity.VideoCoursesActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.MyOrderBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentOrderCanceled extends BaseFragment {
    private PullToRefreshListView listView;
    private java.util.List<MyOrderBean.DataBean> list = new ArrayList<>();
    private CommonAdapter<MyOrderBean.DataBean> adapter;
    private int page = 1;
    DecimalFormat df = new DecimalFormat("#.00");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_canceled, container, false);
        initview(view);
        initOver=true;
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        View empty = View.inflate(getActivity(),R.layout.empty_view,null);
        TextView textEmpty = (TextView) empty.findViewById(R.id.text_empty);
        textEmpty.setText(R.string.not_found_related_order);
        listView.setEmptyView(empty);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));
        adapter = new CommonAdapter<MyOrderBean.DataBean>(getActivity(), list, R.layout.item_fragment_personal_my_order3) {
            @Override
            public void convert(ViewHolder helper, final MyOrderBean.DataBean item, final int position) {
                StringBuilder sp = new StringBuilder();
                if("LiveStudio::Course".equals(item.getProduct_type())){
                    sp.append("直播课/");
                    sp.append(item.getProduct().getGrade()).append(item.getProduct().getSubject()).append("/共").append(item.getProduct().getPreset_lesson_count())
                            .append("课/").append(item.getProduct().getTeacher_name());
                    helper.setText(R.id.classname, item.getProduct().getName())
                            .setText(R.id.describe, sp.toString());
                }else if("LiveStudio::InteractiveCourse".equals(item.getProduct_type())){
                    sp.append("一对一/");
                    sp.append(item.getProduct_interactive_course().getGrade()).append(item.getProduct_interactive_course().getSubject()).append("/共").append(item.getProduct_interactive_course().getLessons_count())
                            .append("课/").append(item.getProduct_interactive_course().getTeachers().get(0).getName());
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


                if (item.getStatus().equals("refunded")) {//交易关闭
                    helper.setText(R.id.status, getString(R.string.refunded));
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
                                if("LiveStudio::Course".equals(item.getProduct_type())){
                                    Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                                    intent.putExtra("id", item.getProduct().getId());
                                    intent.putExtra("page", 0);
                                    startActivity(intent);
                                }else if("LiveStudio::InteractiveCourse".equals(item.getProduct_type())){
                                    Intent intent = new Intent(getActivity(), InteractCourseDetailActivity.class);
                                    intent.putExtra("id", item.getProduct_interactive_course().getId());
                                    intent.putExtra("page", 0);
                                    startActivity(intent);
                                }else if("LiveStudio::VideoCourse".equals(item.getProduct_type())){
                                    Intent intent = new Intent(getActivity(), VideoCoursesActivity.class);
                                    intent.putExtra("id", item.getProduct_video_course().getId());
                                    intent.putExtra("page", 0);
                                    startActivity(intent);
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
                Intent intent = new Intent(getActivity(), PersonalMyOrderCanceledDetailActivity.class);
                intent.putExtra("data", list.get(position - 1));
                startActivity(intent);
            }
        });
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
