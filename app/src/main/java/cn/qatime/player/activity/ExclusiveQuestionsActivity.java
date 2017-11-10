package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.QuestionsBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2017/8/24.
 */

public class ExclusiveQuestionsActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listview;
    private List<QuestionsBean.DataBean> list = new ArrayList<>();
    private CommonAdapter<QuestionsBean.DataBean> adapter;
    private int page = 1;
    private int courseId;
    private boolean mine;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private TextView all;
    private TextView me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_lesson_questions);
        setTitles("课程提问");
        courseId = getIntent().getIntExtra("courseId", 0);
        setRightImage(R.mipmap.add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExclusiveQuestionsActivity.this, QuestionEditActivity.class);
                intent.putExtra("courseId", courseId);
                startActivityForResult(intent, Constant.REQUEST);
            }
        });
        initView();
        getData(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            getData(0);
        }
    }

    private void initView() {
        all = (TextView) findViewById(R.id.all);
        me = (TextView) findViewById(R.id.me);
        all.setOnClickListener(this);
        me.setOnClickListener(this);
        listview = (PullToRefreshListView) findViewById(R.id.listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listview.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listview.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listview.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));
        listview.setEmptyView(View.inflate(this, R.layout.empty_view, null));
        adapter = new CommonAdapter<QuestionsBean.DataBean>(this, list, R.layout.item_exclusive_lesson_question) {

            @Override
            public void convert(ViewHolder holder, QuestionsBean.DataBean item, int position) {
                if ("pending".equals(item.getStatus())) {
                    long time = item.getCreated_at() * 1000L;
                    holder.setText(R.id.create_time, "创建时间" + parse.format(new Date(time)));
                } else {
                    long time = item.getAnswer().getCreated_at()* 1000L;
                    holder.setText(R.id.create_time, "回复时间" + parse.format(new Date(time)));
                }
                holder.setText(R.id.question_name, item.getTitle())
                        .setText(R.id.author, item.getUser_name())
                        .setText(R.id.status, getStatus(item.getStatus()));
            }
        };
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExclusiveQuestionsActivity.this, QuestionDetailsActivity.class);
                intent.putExtra("id", list.get(position - 1).getId());
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

    private String getStatus(String status) {
        if ("pending".equals(status)) return "待回复";
        else return "已回复";
    }

    private void getData(int type) {
        if (type == 0) {
            page = 1;
        }
        Map<String, String> map = new HashMap<>();
        map.put("page", "" + page);
        map.put("per_page", "10");
        if (mine) {
            map.put("user_id", "" + BaseApplication.getInstance().getUserId());
        }
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlGroups + courseId + "/questions", map), null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        listview.onRefreshComplete();
                        list.clear();
                        try {
                            QuestionsBean data = JsonUtils.objectFromJson(response.toString(), QuestionsBean.class);
                            if (data != null) {
                                list.addAll(data.getData());
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        listview.onRefreshComplete();
                    }

                    @Override
                    protected void onTokenOut() {
                        listview.onRefreshComplete();
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                listview.onRefreshComplete();
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all:
                mine = false;
                me.setTextColor(0xff666666);
                all.setTextColor(getResources().getColor(R.color.colorPrimary));
                getData(0);
                break;
            case R.id.me:
                mine = true;
                getData(0);
                all.setTextColor(0xff666666);
                me.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }

    }
}
