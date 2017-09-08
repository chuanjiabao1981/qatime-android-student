package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.ClassTimeTableBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.MPermission;
import cn.qatime.player.utils.MPermissionUtil;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.annotation.OnMPermissionDenied;
import cn.qatime.player.utils.annotation.OnMPermissionGranted;
import cn.qatime.player.utils.annotation.OnMPermissionNeverAskAgain;
import cn.qatime.player.view.MonthDateView;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.NetUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class ClassTimeTableActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listView;
    private List<ClassTimeTableBean.DataBean> totalList = new ArrayList<>();
    private CommonAdapter<ClassTimeTableBean.DataBean.LessonsBean> adapter;
    private List<Integer> alertList = new ArrayList<>();
    private MonthDateView monthDateView;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private String date = parse.format(new Date());
    private List<ClassTimeTableBean.DataBean.LessonsBean> itemList = new ArrayList<>();
    private ClassTimeTableBean.DataBean.LessonsBean item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_time_table);
        setTitles(getResources().getString(R.string.all_course));
        initview();
        initData();
    }

    /**
     * 获取联网数据
     */
    private void initData() {
        Map<String, String> map = new HashMap<>();
        if (!StringUtils.isNullOrBlanK(date)) {
            map.put("date", date);
        }
        map.put("date_type", "month");
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlScheduleData + BaseApplication.getInstance().getUserId() + "/schedule_data", map), null,

                new VolleyListener(ClassTimeTableActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        totalList.clear();
                        try {
                            ClassTimeTableBean data = JsonUtils.objectFromJson(response.toString(), ClassTimeTableBean.class);
                            totalList.addAll(data.getData());
                            alertList.clear();
                            for (int i = 0; i < totalList.size(); i++) {
                                alertList.add(parse.parse(totalList.get(i).getDate()).getDate());
                            }
                            monthDateView.setDaysHasThingList(alertList);
                            filterList();
                            listView.onRefreshComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    protected void onError(JSONObject response) {
                        String label = DateUtils.formatDateTime(ClassTimeTableActivity.this, System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
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
            }
        });
        addToRequestQueue(request);
    }

    private void filterList() {
        itemList.clear();
        for (int i = 0; i < totalList.size(); i++) {
            Logger.e(date + "--------" + totalList.get(i).getDate());
            if (date.equals(totalList.get(i).getDate())) {
                itemList.addAll(totalList.get(i).getLessons());
                break;
            }
        }
        Logger.e(itemList.size() + "******************");
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
        View emptyView = View.inflate(this, R.layout.empty_view, null);
        TextView textEmpty = (TextView) emptyView.findViewById(R.id.text_empty);
        textEmpty.setText("暂无数据");
        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("LiveStudio::Course".equals(itemList.get(position - 1).getProduct_type())) {
                    Intent intent = new Intent(ClassTimeTableActivity.this, RemedialClassDetailActivity.class);
                    intent.putExtra("id", Integer.valueOf(itemList.get(position - 1).getProduct_id()));
                    intent.putExtra("pager", 2);
                    startActivity(intent);
                } else if ("LiveStudio::InteractiveCourse".equals(itemList.get(position - 1).getProduct_type())) {
                    Intent intent = new Intent(ClassTimeTableActivity.this, InteractCourseDetailActivity.class);
                    intent.putExtra("id", Integer.valueOf(itemList.get(position - 1).getProduct_id()));
                    intent.putExtra("pager", 2);
                    startActivity(intent);
                } else if ("LiveStudio::CustomizedGroup".equals(itemList.get(position - 1).getProduct_type())) {
                    Intent intent = new Intent(ClassTimeTableActivity.this, ExclusiveLessonDetailActivity.class);
                    intent.putExtra("id", Integer.valueOf(itemList.get(position - 1).getProduct_id()));
                    intent.putExtra("pager", 2);
                    startActivity(intent);
                }
            }
        });
        adapter = new CommonAdapter<ClassTimeTableBean.DataBean.LessonsBean>(this, itemList, R.layout.item_activity_class_time_table) {
            @Override
            public void convert(final ViewHolder helper, final ClassTimeTableBean.DataBean.LessonsBean item, int position) {
                Glide.with(ClassTimeTableActivity.this).load(item.getCourse_publicize()).placeholder(R.mipmap.error_header_rect).centerCrop().crossFade().dontAnimate().into((ImageView) helper.getView(R.id.image));
//                helper.setText(R.id.course, item.getCourse_name());
                helper.setText(R.id.classname, item.getName());
                //试听状态
                TextView taste = helper.getView(R.id.taste);

                taste.setVisibility(item.isTaste() ? View.VISIBLE : View.GONE);
                try {
                    Date date = parse.parse(item.getClass_date());
                    helper.setText(R.id.class_date, getMonth(date.getMonth()) + "-" + getDay(date.getDate()) + "  ");
                    helper.setText(R.id.status, getStatus(item.getStatus()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                helper.setText(R.id.live_time, item.getLive_time());
                helper.setText(R.id.grade, item.getGrade());
                helper.setText(R.id.subject, item.getSubject());
                helper.setText(R.id.teacher, "/" + item.getTeacher_name());
                if ("LiveStudio::Course".equals(item.getProduct_type())) {
                    helper.getView(R.id.modal_type).setBackgroundColor(0xffb8860b);
                    helper.setText(R.id.modal_type, "直播课");
                } else if ("LiveStudio::InteractiveCourse".equals(item.getProduct_type())) {
                    helper.getView(R.id.modal_type).setBackgroundColor(0xffffb6c1);
                    helper.setText(R.id.modal_type, "一对一");
                } else if ("LiveStudio::CustomizedGroup".equals(item.getProduct_type())) {
                    helper.getView(R.id.modal_type).setBackgroundColor(0xff00ccff);
                    helper.setText(R.id.modal_type, "专属课");
                }

                if("LiveStudio::OfflineLesson".equals(item.getModel_type())){
                    helper.getView(R.id.offline_flag).setVisibility(View.VISIBLE);
                }else{
                    helper.getView(R.id.offline_flag).setVisibility(View.GONE);
                }
                String status = item.getStatus();
                boolean showEnter = (!"LiveStudio::OfflineLesson".equals(item.getModel_type())) && ("ready".equals(status) || "paused".equals(status) || "closed".equals(status) || "teaching".equals(status));//是否是待上课、已直播、直播中
                //进入状态
                helper.getView(R.id.enter).setVisibility(showEnter ? View.VISIBLE : View.GONE);//进入播放器按钮显示或隐藏
                helper.getView(R.id.enter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("LiveStudio::Course".equals(item.getProduct_type())) {
                            Intent intent = new Intent(ClassTimeTableActivity.this, NEVideoPlayerActivity.class);
                            intent.putExtra("id", Integer.valueOf(item.getProduct_id()));
                            startActivity(intent);
                        } else if ("LiveStudio::InteractiveCourse".equals(item.getProduct_type())) {
                            ClassTimeTableActivity.this.item = item;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (NetUtils.checkPermission(ClassTimeTableActivity.this).size() > 0) {
                                    requestLivePermission();
                                } else {
                                    toNext();
                                }
                            } else {
                                toNext();
                            }
                        } else if ("LiveStudio::CustomizedGroup".equals(item.getProduct_type())) {
                            Intent intent = new Intent(ClassTimeTableActivity.this, ExclusiveVideoPlayerActivity.class);
                            intent.putExtra("id", item.getProduct_id());
                            startActivity(intent);
                        }
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();
            }
        });

        ImageView ivLeft = (ImageView) findViewById(R.id.iv_left);
        ImageView ivRight = (ImageView) findViewById(R.id.iv_right);
        monthDateView = (MonthDateView) findViewById(R.id.monthDateView);
        monthDateView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, DensityUtils.dp2px(this, 35) * 6));
        TextView tvDate = (TextView) findViewById(R.id.date_text);
        View tvToday = findViewById(R.id.date_operator_ll);
        monthDateView.setTextView(tvDate, null);
        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        tvToday.setOnClickListener(this);
        monthDateView.setDateClick(new MonthDateView.DateClick() {
            @Override
            public void onClickOnDate() {
                getDate();
                filterList();
            }
        });
        monthDateView.setOnCalendarPageChangeListener(new MonthDateView.OnCalendarPageChangeListener() {
            @Override
            public void onPageChange(int type) {
                cancelAll(this);//旧请求取消
                getDate();
                initData();
            }
        });
    }

    private void toNext() {
        Intent intent = new Intent(ClassTimeTableActivity.this, InteractiveLiveActivity.class);
        intent.putExtra("id", Integer.valueOf(item.getProduct_id()));
        startActivity(intent);
    }

    private void requestLivePermission() {
        MPermission.with(this)
                .addRequestCode(100)
                .permissions(NetUtils.checkPermission(ClassTimeTableActivity.this).toArray(new String[NetUtils.checkPermission(ClassTimeTableActivity.this).size()]))
                .request();
    }

    @OnMPermissionGranted(100)
    public void onLivePermissionGranted() {
//        Toast.makeText(InteractiveLiveActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
        toNext();
    }

    @OnMPermissionDenied(100)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, NetUtils.checkPermission(ClassTimeTableActivity.this).toArray(new String[NetUtils.checkPermission(ClassTimeTableActivity.this).size()]));
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法开启直播";
        Toast.makeText(ClassTimeTableActivity.this, tip, Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionNeverAskAgain(100)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, NetUtils.checkPermission(ClassTimeTableActivity.this).toArray(new String[NetUtils.checkPermission(ClassTimeTableActivity.this).size()]));
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, NetUtils.checkPermission(ClassTimeTableActivity.this).toArray(new String[NetUtils.checkPermission(ClassTimeTableActivity.this).size()]));
        StringBuilder sb = new StringBuilder();
        sb.append("无法开启直播，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }

        Toast.makeText(ClassTimeTableActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
    }

    private String getMonth(int month) {
        month += 1;
        if (month < 10) {
            return "0" + month;
        }
        return String.valueOf(month);
    }

    private String getDay(int day) {
        if (day < 10) {
            return "0" + day;
        }
        return String.valueOf(day);
    }

    private void getDate() {
        date = monthDateView.getmSelYear() + "-" + (monthDateView.getmSelMonth() + 1 < 10 ? "0" + (monthDateView.getmSelMonth() + 1) : monthDateView.getmSelMonth() + 1) + "-" +
                (monthDateView.getmSelDay() < 10 ? "0" + monthDateView.getmSelDay() : monthDateView.getmSelDay());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                monthDateView.onLeftClick();

                break;
            case R.id.iv_right:
                monthDateView.onRightClick();

                break;
            case R.id.date_operator_ll:
                monthDateView.setTodayToView();
                getDate();
                initData();
                break;
        }
    }

    private String getStatus(String status) {
        if (status.equals("missed")) {//待补课
            return getResourceString(R.string.class_wait);
        } else if (status.equals("init")) {//未开始
            return getResourceString(R.string.class_init);
        } else if (status.equals("ready")) {//待开课
            return getResourceString(R.string.class_ready);
        } else if (status.equals("teaching")) {//直播中
            return getResourceString(R.string.class_teaching);
        } else if (status.equals("closed")) {//已直播
            return getResourceString(R.string.class_closed);
        } else if (status.equals("paused")) {//直播中
            return getResourceString(R.string.class_teaching);
        } else {
            return getResourceString(R.string.class_over);//已结束
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
