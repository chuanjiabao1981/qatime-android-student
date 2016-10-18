package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.NEVideoPlayerActivity;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.ClassTimeTableBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentRemedialClassTimeTable1 extends BaseFragment {
    private PullToRefreshListView listView;
    private CommonAdapter<ClassTimeTableBean.DataEntity.LessonsEntity> adapter;
    private List<ClassTimeTableBean.DataEntity> totalList = new ArrayList<>();
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private String date = parse.format(new Date());
    private List<ClassTimeTableBean.DataEntity.LessonsEntity> itemList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remedial_class_timetable1, container, false);
        initview(view);
        initData();
        return view;
    }


    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("month", date);
        map.put("state", "unclosed");

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlMyRemedialClass + BaseApplication.getUserId() + "/schedule", map), null,
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
        Logger.e(itemList.size() + "");
        adapter.notifyDataSetChanged();
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.getRefreshableView().setDividerHeight(2);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));


        adapter = new CommonAdapter<ClassTimeTableBean.DataEntity.LessonsEntity>(getActivity(), itemList, R.layout.item_fragment_remedial_class_time_table1) {
            @Override
            public void convert(ViewHolder helper, final ClassTimeTableBean.DataEntity.LessonsEntity item, int position) {
                Glide.with(getActivity()).load(item.getCourse_publicize()).centerCrop().crossFade().dontAnimate().into((ImageView) helper.getView(R.id.image));
//                helper.getView(R.id.image).setOnClickListener(
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
//                                intent.putExtra("id",  Integer.valueOf(item.getCourse_id()));
//                                intent.putExtra("pager", 2);
//                                startActivity(intent);
//                            }
//                        });
                helper.setText(R.id.course, item.getCourse_name());
                helper.setText(R.id.classname, item.getName());
                helper.setText(R.id.status, getStatus(item.getStatus()));
                helper.setText(R.id.class_date, item.getClass_date() + " ");
                helper.setText(R.id.live_time, item.getLive_time());
                helper.setText(R.id.subject, getResourceString(R.string.item_subject) + item.getSubject());
                helper.setText(R.id.teacher, getResourceString(R.string.item_teacher) + item.getTeacher_name());
                helper.getView(R.id.enter).setVisibility(StringUtils.isNullOrBlanK(item.getPull_address()) ? View.GONE : View.VISIBLE);
                helper.getView(R.id.enter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NEVideoPlayerActivity.class);
                        intent.putExtra("id", item.getId());
                        intent.putExtra("url", item.getPull_address());
                        startActivity(intent);
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
                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                intent.putExtra("id", Integer.valueOf(itemList.get(position-1).getCourse_id()));
                intent.putExtra("pager", 2);
                startActivity(intent);
            }
        });
    }

    private String getStatus(String status) {
        if (status.equals("teaching")) {//直播中
            return getResourceString(R.string.class_teaching);
        } else if (status.equals("paused")) {
            return getResourceString(R.string.class_teaching);
        } else if (status.equals("init")) {//未开始
            return getResourceString(R.string.class_init);
        } else if (status.equals("ready")) {//待开课
            return getResourceString(R.string.class_ready);
        } else if (status.equals("paused_inner")) {//暂停中
            return getResourceString(R.string.class_paused_inner);
        } else {
            return getResourceString(R.string.class_over);//已结束
        }
    }
}
