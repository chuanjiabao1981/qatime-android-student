package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.adapter.CommonAdapter;
import cn.qatime.player.adapter.ViewHolder;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.RemedialClassBean;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.ScreenUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VolleyErrorListener;

public class Fragment21 extends BaseFragment {
    private PullToRefreshListView list;
    private PullToRefreshGridView grid;
    private List<RemedialClassBean.Data> list1 = new ArrayList<>();
    private CommonAdapter<RemedialClassBean.Data> adapter;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment11, container, false);
        initview(view);
        initData(1);
        return view;
    }

    private void initview(View view) {
        list = (PullToRefreshListView) view.findViewById(R.id.list);
        list.setMode(PullToRefreshBase.Mode.BOTH);
        list.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        list.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        list.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        list.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        list.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        list.getLoadingLayoutProxy(true, false).setReleaseLabel("松开刷新");
        list.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");


        adapter = new CommonAdapter<RemedialClassBean.Data>(getActivity(), list1, R.layout.item_fragment11) {
            @Override
            public void convert(ViewHolder helper, RemedialClassBean.Data item, int position) {
                ((ImageView) helper.getView(R.id.image)).setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(getActivity()) / 2, ScreenUtils.getScreenWidth(getActivity()) / 2));
//                Glide.with(getActivity()).load(item.getPush_address()).into(((ImageView) helper.getView(R.id.image)));
                helper.setText(R.id.name, item.getName());
                helper.setText(R.id.subject, item.getSubject());
                helper.setText(R.id.grade, item.getGrade());

            }
        };
        grid.setAdapter(adapter);

        grid.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                page = 1;
                initData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page++;
                initData(2);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                intent.putExtra("id", list1.get(position).getId());
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
        map.put("Remember-Token", BaseApplication.getProfile().getToken());
        map.put("page", String.valueOf(page));
        map.put("per_page", "10");
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass, map), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        LogUtils.e(jsonObject.toString());
                        if (type == 1) {
                            list1.clear();
                        }
                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        grid.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                        grid.onRefreshComplete();

                        try {
                            Gson gson = new Gson();
                            RemedialClassBean data = gson.fromJson(jsonObject.toString(), RemedialClassBean.class);
                            list1.addAll(data.getData());
                            adapter.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }

                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
             list.onRefreshComplete();
            }
        });
        addToRequestQueue(request);
    }
}
