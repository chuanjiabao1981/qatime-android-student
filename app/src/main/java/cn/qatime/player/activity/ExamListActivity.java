package cn.qatime.player.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.ExamListBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author luntify
 * @date 2017/12/13 16:19
 * @Description: 考试列表
 */

public class ExamListActivity extends BaseActivity {
    private int latestResult = 1;//0上1下-1未选
    private int popularityResult = -1;
    private int priceResult = -1;
    private int page = 1;
    private PullToRefreshListView listview;
    private CommonAdapter<ExamListBean.DataBean> adapter;
    private List<ExamListBean.DataBean> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list);
        setTitles("在线模考");
        initView();
        getData(0);
    }

    private void initView() {
        final TextView latest = (TextView) findViewById(R.id.latest);
        final TextView price = (TextView) findViewById(R.id.price);
        final TextView popularity = (TextView) findViewById(R.id.popularity);
        latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latestResult == -1) {
                    latestResult = 1;
                } else if (latestResult == 0) {
                    latestResult = 1;
                } else if (latestResult == 1) {
                    latestResult = 0;
                }
                priceResult = -1;
                popularityResult = -1;
                refreshState(latest, latestResult);
                refreshState(price, priceResult);
                refreshState(popularity, popularityResult);
                getData(0);
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priceResult == -1) {
                    priceResult = 1;
                } else if (priceResult == 0) {
                    priceResult = 1;
                } else if (priceResult == 1) {
                    priceResult = 0;
                }
                latestResult = -1;
                popularityResult = -1;
                refreshState(latest, latestResult);
                refreshState(price, priceResult);
                refreshState(popularity, popularityResult);
                getData(0);
            }
        });
        popularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popularityResult == -1) {
                    popularityResult = 1;
                } else if (popularityResult == 0) {
                    popularityResult = 1;
                } else if (popularityResult == 1) {
                    popularityResult = 0;
                }
                latestResult = -1;
                priceResult = -1;
                refreshState(latest, latestResult);
                refreshState(price, priceResult);
                refreshState(popularity, popularityResult);
                getData(0);
            }
        });
        listview = (PullToRefreshListView) findViewById(R.id.listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listview.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listview.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listview.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));
        listview.setEmptyView(View.inflate(ExamListActivity.this, R.layout.empty_view, null));
        adapter = new CommonAdapter<ExamListBean.DataBean>(ExamListActivity.this, datas, R.layout.item_exam_list) {
            @Override
            public void convert(ViewHolder holder, ExamListBean.DataBean item, int position) {
                holder.setText(R.id.name, item.getName())
                        .setText(R.id.price, "￥" + item.getPrice())
                        .setText(R.id.message, (item.getDuration() == 0 ? 0 : item.getDuration() / 60) + "分钟 | 共" + item.getTopics_count() + "小题 | " + item.getGrade_category() + item.getSubject())
                        .setText(R.id.count, "已考人数 " + item.getUsers_count());
            }
        };
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExamListActivity.this, ExaminationActivity.class);
                intent.putExtra("id", datas.get(position - 1).getId());
                startActivity(intent);
            }
        });
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                getData(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page += 1;
                getData(1);
            }
        });
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

    /**
     * @param type 0下拉1上啦
     */
    private void getData(final int type) {
        Map<String, String> map = new HashMap<>();
        if (type == 0) {
            page = 1;
        }

        map.put("per_page", "20");
        map.put("page", String.valueOf(page));

        if (latestResult != -1) {//0上1下-1未选
            if (latestResult == 0) {
                map.put("sort_by", "published_at");
            } else {
                map.put("sort_by", "published_at.asc");
            }
        } else if (priceResult != -1) {
            if (priceResult == 0) {
                map.put("sort_by", "price");
            } else {
                map.put("sort_by", "price.asc");
            }
        } else if (popularityResult != -1) {
            if (popularityResult == 0) {
                map.put("sort_by", "users_count");
            } else {
                map.put("sort_by", "users_count.asc");
            }
        }

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlExamSearch, map), null, new VolleyListener(ExamListActivity.this) {
            @Override
            protected void onTokenOut() {
                listview.onRefreshComplete();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                if (type == 0) {
                    datas.clear();
                }
                String label = DateUtils.formatDateTime(ExamListActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                listview.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                listview.onRefreshComplete();
                ExamListBean data = JsonUtils.objectFromJson(response.toString(), ExamListBean.class);
                assert data != null;
                datas.addAll(data.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onError(JSONObject response) {
                listview.onRefreshComplete();
            }
        }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                listview.onRefreshComplete();
            }
        });
        addToRequestQueue(request);
    }
}
