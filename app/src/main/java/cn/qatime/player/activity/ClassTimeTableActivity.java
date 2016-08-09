package cn.qatime.player.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.adapter.CommonAdapter;
import cn.qatime.player.adapter.ViewHolder;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.RemedialClassBean;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VolleyErrorListener;

public class ClassTimeTableActivity extends BaseActivity {
    private PullToRefreshListView List;
    private java.util.List<RemedialClassBean.Data> list = new ArrayList<>();
    private CommonAdapter<RemedialClassBean.Data> adapter;
    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_time_table);
        setTitle("全部课程");
        initview();
        initData(1);
    }



    private void initview() {
        List = (PullToRefreshListView) findViewById(R.id.list);
        List.setMode(PullToRefreshBase.Mode.BOTH);
        List.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        List.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        List.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        List.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        List.getLoadingLayoutProxy(true, false).setReleaseLabel("松开刷新");
        List.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");


        adapter = new CommonAdapter<RemedialClassBean.Data>(this, list, R.layout.item_activity_class_time_table) {
            @Override
            public void convert(ViewHolder helper, RemedialClassBean.Data item, int position) {

            }
        };
        List.setAdapter(adapter);

        List.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                            list.clear();
                        }
                        String label = DateUtils.formatDateTime(ClassTimeTableActivity.this, System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        List.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                        List.onRefreshComplete();

                        try {
                            Gson gson = new Gson();
                            RemedialClassBean data = gson.fromJson(jsonObject.toString(), RemedialClassBean.class);
                            list.addAll(data.getData());
                            adapter.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }

                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                List.onRefreshComplete();
            }
        });
        addToRequestQueue(request);
    }
}
