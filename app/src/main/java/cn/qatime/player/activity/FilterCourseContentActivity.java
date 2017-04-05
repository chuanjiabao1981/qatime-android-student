package cn.qatime.player.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
import cn.qatime.player.bean.FilterCourseContentBean;
import cn.qatime.player.utils.Constant;
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
 * @Time 2017/3/14 15:29
 * @Describe 筛选课程内容页面
 */

public class FilterCourseContentActivity extends BaseActivity {


    private String grade;
    private String subject;
    private PullToRefreshListView listview;
    private CommonAdapter<FilterCourseContentBean.DataBean> adapter;
    private List<FilterCourseContentBean.DataBean> datas = new ArrayList<>();
    private int latestResult = 1;//0上1下-1未选
    private int popularityResult = -1;
    private int priceResult = -1;
    private int page = 1;
    private String tag = null;//标签
    private String range = null;//查询范围
    private String courseStatus = null;//课程状态
    private String endTime = null;
    private String startTime = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_course);
        grade = getIntent().getStringExtra("grade");
        subject = getIntent().getStringExtra("subject");
        setTitles(grade + subject);
        initView();
        getData(0);
    }

    /**
     * @param type 0下拉1上啦
     */
    private void getData(final int type) {
        Map<String, String> map = new HashMap<>();
        if(type == 0){
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
                map.put("sort_by", "left_price");
            } else {
                map.put("sort_by", "left_price.asc");
            }
        } else if (popularityResult != -1) {
            if (popularityResult == 0) {
                map.put("sort_by", "buy_tickets_count");
            } else {
                map.put("sort_by", "buy_tickets_count.asc");
            }
        }
        try {
            map.put("q[grade_eq]", URLEncoder.encode(grade, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            if (!subject.equals("全部")) {
                map.put("q[subject_eq]", URLEncoder.encode(subject, "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (!StringUtils.isNullOrBlanK(tag)) {
            try {
                map.put("tags", URLEncoder.encode(tag, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isNullOrBlanK(range)) {
            map.put("range", range);
        }
        if (!StringUtils.isNullOrBlanK(courseStatus)) {
            map.put("q[status_eq]", courseStatus);
        }
        if (!StringUtils.isNullOrBlanK(startTime) && !StringUtils.isNullOrBlanK(endTime)) {
            map.put("q[class_date_gteq]", startTime);
            map.put("q[class_date_lt]", endTime);
        }

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlSearch, map), null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {

            }

            @Override
            protected void onSuccess(JSONObject response) {
                if (type == 0) {
                    datas.clear();
                }
                String label = DateUtils.formatDateTime(FilterCourseContentActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                listview.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                listview.onRefreshComplete();
                FilterCourseContentBean data = JsonUtils.objectFromJson(response.toString(), FilterCourseContentBean.class);
                assert data != null;
                datas.addAll(data.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onError(JSONObject response) {

            }
        }, new VolleyErrorListener());
        addToRequestQueue(request);
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
        final TextView label = (TextView) findViewById(R.id.label);
        View screen = findViewById(R.id.screen);

        listview = (PullToRefreshListView) findViewById(R.id.listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listview.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listview.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listview.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));
        adapter = new CommonAdapter<FilterCourseContentBean.DataBean>(this, datas, R.layout.item_filter_course) {
            @Override
            public void convert(ViewHolder holder, FilterCourseContentBean.DataBean item, int position) {
                Glide.with(FilterCourseContentActivity.this).load(item.getPublicize()).crossFade().placeholder(R.mipmap.photo).into((ImageView) holder.getView(R.id.image));
                holder.setText(R.id.name, item.getName())
                        .setText(R.id.price, "￥" + item.getPrice())
                        .setText(R.id.teacher, item.getTeacher_name());
            }
        };
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FilterCourseContentActivity.this, RemedialClassDetailActivity.class);
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
        final List<String> labelData = new ArrayList<>();
        labelData.add("不限");
        labelData.add("高考");
        labelData.add("中考");
        labelData.add("会考");
        labelData.add("小升初考试");
        labelData.add("高考志愿");
        labelData.add("英语考级");
        labelData.add("奥数竞赛");
        labelData.add("历年真题");
        labelData.add("期中期末试卷");
        labelData.add("自编试卷");
        labelData.add("暑假课");
        labelData.add("寒假课");
        labelData.add("周末课");
        labelData.add("国庆假期课");
        labelData.add("基础课");
        labelData.add("巩固课");
        labelData.add("提高课");
        labelData.add("外教");
        labelData.add("冲刺");
        labelData.add("重点难点");
        final CommonAdapter<String> labelAdapter = new CommonAdapter<String>(this, labelData, R.layout.item_screening_condition) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.text, item);
                if (label.getText().toString().equals(labelData.get(position))) {
                    ((TextView) holder.getView(R.id.text)).setBackgroundResource(R.drawable.text_background_red);
                    ((TextView) holder.getView(R.id.text)).setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    ((TextView) holder.getView(R.id.text)).setBackgroundResource(R.drawable.text_background);
                    ((TextView) holder.getView(R.id.text)).setTextColor(0xff999999);
                }
            }
        };
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //标签
                final AlertDialog dialog = new AlertDialog.Builder(FilterCourseContentActivity.this).create();
                View view = LayoutInflater.from(FilterCourseContentActivity.this).inflate(R.layout.dialog_grid, null);
                GridView gridView = (GridView) view.findViewById(R.id.grid);
                gridView.setAdapter(labelAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        label.setText(labelData.get(position));
                        if (labelData.get(position).equals("不限")) {
                            tag = null;
                        } else {
                            tag = labelData.get(position);
                        }
                        getData(0);
                        dialog.dismiss();
                    }
                });
                dialog.setView(view);
                dialog.show();
            }
        });

        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterCourseContentActivity.this, ScreeningConditionActivity.class);
                intent.putExtra("range", range);
                intent.putExtra("courseStatus", courseStatus);
                intent.putExtra("startTime", startTime);
                intent.putExtra("endTime", endTime);
                startActivityForResult(intent, Constant.REQUEST);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            if (data != null) {
                range = data.getStringExtra("range");
                courseStatus = data.getStringExtra("courseStatus");
                startTime = data.getStringExtra("startTime");
                endTime = data.getStringExtra("endTime");
                getData(0);
            }
        }
    }
}
