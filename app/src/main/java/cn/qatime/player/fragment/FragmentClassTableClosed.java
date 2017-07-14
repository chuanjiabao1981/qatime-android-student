package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.InteractCourseDetailActivity;
import cn.qatime.player.activity.InteractiveLiveActivity;
import cn.qatime.player.activity.NEVideoPlayerActivity;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.ClassTimeTableBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.MPermission;
import cn.qatime.player.utils.MPermissionUtil;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.annotation.OnMPermissionDenied;
import cn.qatime.player.utils.annotation.OnMPermissionGranted;
import cn.qatime.player.utils.annotation.OnMPermissionNeverAskAgain;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.NetUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentClassTableClosed extends BaseFragment {
    private PullToRefreshListView listView;
    private CommonAdapter<ClassTimeTableBean.DataBean.LessonsBean> adapter;
    private List<ClassTimeTableBean.DataBean> totalList = new ArrayList<>();
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private String date = parse.format(new Date());
    private List<ClassTimeTableBean.DataBean.LessonsBean> itemList = new ArrayList<>();
    private ClassTimeTableBean.DataBean.LessonsBean item;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_table_closed, container, false);
        initview(view);
        initData();
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));
        View emptyView = View.inflate(getActivity(), R.layout.empty_view, null);
        TextView textEmpty = (TextView) emptyView.findViewById(R.id.text_empty);
        textEmpty.setText("本周暂无数据");
        listView.setEmptyView(emptyView);

        adapter = new CommonAdapter<ClassTimeTableBean.DataBean.LessonsBean>(getActivity(), itemList, R.layout.item_fragment_remedial_class_time_table2) {
            @Override
            public void convert(ViewHolder helper, final ClassTimeTableBean.DataBean.LessonsBean item, int position) {
                      Glide.with(getActivity()).load(item.getCourse_publicize()).placeholder(R.mipmap.error_header_rect).centerCrop().crossFade().dontAnimate().into((ImageView) helper.getView(R.id.image));
////                helper.setText(R.id.course, item.getCourse_name());
                helper.setText(R.id.classname, item.getName());
                //试听状态
                TextView taste = helper.getView(R.id.taste);

                taste.setVisibility(item.isTaste() ? View.VISIBLE : View.GONE);
                try {
                    Date date = parse.parse(item.getClass_date());
                    helper.setText(R.id.class_date, getMonth(date.getMonth()) + "-" + getDay(date.getDate()) + "  ");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                helper.setText(R.id.status, getStatus(item.getStatus()));
                helper.setText(R.id.live_time, item.getLive_time());
                helper.setText(R.id.grade, item.getGrade());
                helper.setText(R.id.subject, item.getSubject());
                helper.setText(R.id.teacher, "/"   + item.getTeacher_name());
                if ("LiveStudio::Lesson".equals(itemList.get(position).getModel_type())) {
                    helper.getView(R.id.modal_type).setBackgroundColor(0xffff4856);
                    helper.setText(R.id.modal_type, "直播课");
                } else if ("LiveStudio::InteractiveLesson".equals(itemList.get(position).getModel_type())) {
                    helper.getView(R.id.modal_type).setBackgroundColor(0xff4856ff);
                    helper.setText(R.id.modal_type, "一对一");
                }


                String status = item.getStatus();

                boolean showEnter = "ready".equals(status) || "paused".equals(status) || "closed".equals(status) || "teaching".equals(status);//是否是待上课、已直播、直播中
                //进入状态
                helper.getView(R.id.enter).setVisibility(showEnter ? View.VISIBLE : View.GONE);//进入播放器按钮显示或隐藏
                helper.getView(R.id.enter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("LiveStudio::Lesson".equals(item.getModel_type())) {
                            Intent intent = new Intent(getActivity(), NEVideoPlayerActivity.class);
                            intent.putExtra("id", Integer.valueOf(item.getProduct_id()));
                            startActivity(intent);
                        } else if ("LiveStudio::InteractiveLesson".equals(item.getModel_type())) {
                            FragmentClassTableClosed.this.item = item;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (NetUtils.checkPermission(getActivity()).size() > 0) {
                                    requestLivePermission();
                                } else {
                                    toNext();
                                }
                            } else {
                                toNext();
                            }
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ("LiveStudio::Lesson".equals(itemList.get(position - 1).getModel_type())) {
                    Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                    intent.putExtra("id", Integer.valueOf(itemList.get(position - 1).getProduct_id()));
                    intent.putExtra("pager", 2);
                    startActivity(intent);
                } else if ("LiveStudio::InteractiveLesson".equals(itemList.get(position - 1).getModel_type())) {
                    Intent intent = new Intent(getActivity(), InteractCourseDetailActivity.class);
                    intent.putExtra("id", Integer.valueOf(itemList.get(position - 1).getProduct_id()));
                    intent.putExtra("pager", 2);
                    startActivity(intent);
                }
            }
        });
    }

    private void toNext() {
        Intent intent = new Intent(getActivity(), InteractiveLiveActivity.class);
        intent.putExtra("id", Integer.valueOf(item.getProduct_id()));
        startActivity(intent);
    }

    private void requestLivePermission() {
        MPermission.with(this)
                .addRequestCode(100)
                .permissions(NetUtils.checkPermission(getActivity()).toArray(new String[NetUtils.checkPermission(getActivity()).size()]))
                .request();
    }

    @OnMPermissionGranted(100)
    public void onLivePermissionGranted() {
//        Toast.makeText(InteractiveLiveActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
        toNext();
    }

    @OnMPermissionDenied(100)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, NetUtils.checkPermission(getActivity()).toArray(new String[NetUtils.checkPermission(getActivity()).size()]));
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法开启直播";
        Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionNeverAskAgain(100)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, NetUtils.checkPermission(getActivity()).toArray(new String[NetUtils.checkPermission(getActivity()).size()]));
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, NetUtils.checkPermission(getActivity()).toArray(new String[NetUtils.checkPermission(getActivity()).size()]));
        StringBuilder sb = new StringBuilder();
        sb.append("无法开启直播，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }

        Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG).show();
    }

    private String getDay(int day) {
        if (day < 10) {
            return "0" + day;
        }
        return String.valueOf(day);
    }

    private String getMonth(int month) {
        month += 1;
        if (month < 10) {
            return "0" + month;
        }
        return String.valueOf(month);
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
        } else if (status.equals("closed")) {//已直播  。。。
            return getResourceString(R.string.class_closed);
        } else if (status.equals("paused")) {//直播中   .....
            return getResourceString(R.string.class_teaching);
        } else {
            return getResourceString(R.string.class_over);//已结束
        }
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("date", date);
        map.put("date_type", "week");
        map.put("state", "closed");
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlMyRemedialClass + BaseApplication.getInstance().getUserId() + "/schedule_data", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        totalList.clear();
                        try {
                            ClassTimeTableBean data = JsonUtils.objectFromJson(response.toString(), ClassTimeTableBean.class);
                            totalList.addAll(data.getData());
                            filterList();
                            listView.onRefreshComplete();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

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

    private void filterList() {
        itemList.clear();
        for (int i = 0; i < totalList.size(); i++) {
            itemList.addAll(totalList.get(i).getLessons());
        }
        adapter.notifyDataSetChanged();
    }
}
