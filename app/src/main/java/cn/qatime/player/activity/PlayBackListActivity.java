package cn.qatime.player.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.PlayBackListBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2017/6/13 11:27
 * @Describe
 */

public class PlayBackListActivity extends BaseActivity implements View.OnClickListener {
    private TextView latest;
    private TextView most;
    private PullToRefreshListView listView;
    private List<PlayBackListBean.DataBean> datas = new ArrayList<PlayBackListBean.DataBean>();
    private CommonAdapter<PlayBackListBean.DataBean> adapter;
    private int latestResult = 1;
    private int page = 1;
    private String s = "";


    private void assignViews() {
        try {
            s = URLEncoder.encode("updated_at desc", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        latest = (TextView) findViewById(R.id.latest);
        most = (TextView) findViewById(R.id.most);
        listView = (PullToRefreshListView) findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));
        View empty = View.inflate(PlayBackListActivity.this, R.layout.empty_view, null);
        listView.setEmptyView(empty);
        adapter = new CommonAdapter<PlayBackListBean.DataBean>(this, datas, R.layout.item_play_back_list) {
            @Override
            public void convert(ViewHolder holder, PlayBackListBean.DataBean item, int position) {
                Glide.with(PlayBackListActivity.this).load(item.getLogo_url()).placeholder(R.mipmap.photo).into((ImageView) holder.getView(R.id.image));
                holder.setText(R.id.name, item.getLive_studio_lesson().getName());
                StringBuilder text = new StringBuilder();
                if (!StringUtils.isNullOrBlanK(item.getGrade())) {
                    text.append(item.getGrade());
                }
                if (!StringUtils.isNullOrBlanK(item.getSubject())) {
                    text.append(item.getSubject());
                }
                text.append("|");
                if (!StringUtils.isNullOrBlanK(item.getTeacher_name())) {
                    text.append(item.getTeacher_name());
                }
                holder.setText(R.id.teacher, text.toString());
                holder.setText(R.id.buy_count, item.getReplay_times() + "人");
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
                Intent intent = new Intent(PlayBackListActivity.this, PlayBackActivity.class);
                intent.putExtra("id", datas.get(position - 1).getId());
                startActivity(intent);
            }
        });
        latest.setOnClickListener(this);
        most.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_back_list);
        setTitles("精彩回放");
        assignViews();
        initData(1);
    }

    private void initData(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("s", s);
        map.put("per_page", "10");
        map.put("page", String.valueOf(page));

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlHomeReplays, map), null, new VolleyListener(PlayBackListActivity.this) {
            @Override
            protected void onTokenOut() {

            }

            @Override
            protected void onSuccess(JSONObject response) {
                PlayBackListBean data = JsonUtils.objectFromJson(response.toString(), PlayBackListBean.class);
                if (type == 1) {
                    datas.clear();
                }
                if (data != null) {
                    datas.addAll(data.getData());
                    adapter.notifyDataSetChanged();
                }
                String label = DateUtils.formatDateTime(PlayBackListActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                listView.onRefreshComplete();
            }

            @Override
            protected void onError(JSONObject response) {

            }
        }, new VolleyErrorListener());
        addToRequestQueue(request);
    }

    private void refreshState(TextView view, int result) {//-1未选 0上 1下
        if (result == -1) {
            view.setCompoundDrawables(null, null, null, null);
            view.setTextColor(0xff666666);
        } else if (result == 0) {
            Drawable up = getResources().getDrawable(R.mipmap.arrow_up);
            up.setBounds(0, 0, up.getMinimumWidth(), up.getMinimumHeight());
            view.setCompoundDrawables(null, null, up, null);
            view.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (result == 1) {
            Drawable down = getResources().getDrawable(R.mipmap.arrow_down);
            down.setBounds(0, 0, down.getMinimumWidth(), down.getMinimumHeight());
            view.setCompoundDrawables(null, null, down, null);
            view.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.latest:
                if (latestResult == -1) {
                    latestResult = 1;
                    try {
                        s = URLEncoder.encode("updated_at desc", "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else if (latestResult == 0) {
                    latestResult = 1;
                    try {
                        s = URLEncoder.encode("updated_at desc", "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else if (latestResult == 1) {
                    latestResult = 0;
                    try {
                        s = URLEncoder.encode("updated_at asc", "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                refreshState(latest, latestResult);
                most.setTextColor(0xff666666);
                initData(1);
                break;
            case R.id.most:
                latestResult = -1;
                try {
                    s = URLEncoder.encode("replay_times desc", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                refreshState(latest, latestResult);
                most.setTextColor(getResources().getColor(R.color.colorPrimary));
                initData(1);
                break;
        }
    }
}
