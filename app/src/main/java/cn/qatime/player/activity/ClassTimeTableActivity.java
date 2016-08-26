package cn.qatime.player.activity;


import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orhanobut.logger.Logger;


import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.ClassTimeTableBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.MonthDateView;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import cn.qatime.player.base.BaseActivity;
import libraryextra.bean.RemedialClassBean;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class ClassTimeTableActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listView;
    private List<ClassTimeTableBean.DataEntity> totalList = new ArrayList<>();
    private CommonAdapter<ClassTimeTableBean.DataEntity.LessonsEntity> adapter;
    private List<Integer> alertList = new ArrayList<>();
    private int page = 1;
    private MonthDateView monthDateView;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");

    private String date = parse.format(new Date());
    private List<ClassTimeTableBean.DataEntity.LessonsEntity> itemList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_time_table);
        setTitle(getResources().getString(R.string.all_course));
        initview();
        initData();
    }

    /**
     * 获取联网数据
     */
    private void initData() {
        Map<String, String> map = new HashMap<>();
        if (!StringUtils.isNullOrBlanK(date)) {
            map.put("month", date);
        }
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/schedule", map), null,
                new VolleyListener(ClassTimeTableActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        totalList.clear();
                        try {
                            ClassTimeTableBean data = JsonUtils.objectFromJson(response.toString(), ClassTimeTableBean.class);
                            totalList.addAll(data.getData());
                            alertList.clear();
                            for (int i = 0; i < totalList.size(); i++) {
                                alertList.add(parse.parse("2016-10-10").getDate());
                                Logger.e(String.valueOf(parse.parse("2016-10-10").getDate()));
                            }
                            monthDateView.setDaysHasThingList(alertList);
                            filterList();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

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
            }
        });
        addToRequestQueue(request);
    }

    private void filterList() {
        itemList.clear();
        for (int i = 0; i < totalList.size(); i++) {
            if (date.equals(totalList.get(i).getDate())) {
                itemList.addAll(totalList.get(i).getLessons());
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }


    private void initview() {
        listView = (PullToRefreshListView) findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));

        adapter = new CommonAdapter<ClassTimeTableBean.DataEntity.LessonsEntity>(this, itemList, R.layout.item_activity_class_time_table) {
            @Override
            public void convert(ViewHolder helper, ClassTimeTableBean.DataEntity.LessonsEntity item, int position) {

            }
        };
        listView.setAdapter(adapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String label = DateUtils.formatDateTime(ClassTimeTableActivity.this, System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                        listView.onRefreshComplete();
                    }
                }, 300);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        ImageView ivLeft = (ImageView) findViewById(R.id.iv_left);
        ImageView ivRight = (ImageView) findViewById(R.id.iv_right);
        monthDateView = (MonthDateView) findViewById(R.id.monthDateView);
        monthDateView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DensityUtils.dp2px(this, 25) * 7));
        TextView tvDate = (TextView) findViewById(R.id.date_text);
        TextView tvToday = (TextView) findViewById(R.id.tv_today);
        monthDateView.setTextView(tvDate, null);
        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        tvToday.setOnClickListener(this);
        monthDateView.setDateClick(new MonthDateView.DateClick() {
            @Override
            public void onClickOnDate() {
                date = monthDateView.getmSelYear() + "-" + (monthDateView.getmSelMonth() + 1) + "-" + monthDateView.getmSelDay();
                filterList();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                monthDateView.onLeftClick();
                date = monthDateView.getmSelYear() + "-" + (monthDateView.getmSelMonth() + 1) + "-" + monthDateView.getmSelDay();
//                Logger.e(date);
                initData();
                break;
            case R.id.iv_right:
                monthDateView.onRightClick();
                Logger.e(String.valueOf(monthDateView.getmSelMonth()));
                date = monthDateView.getmSelYear() + "-" + (monthDateView.getmSelMonth() + 1) + "-" + monthDateView.getmSelDay();
                initData();
                break;
            case R.id.tv_today:
                monthDateView.setTodayToView();
                break;
        }
    }
}
