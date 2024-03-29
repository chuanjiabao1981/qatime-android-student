package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.NEVideoPlaybackActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.ExclusiveLessonDetailBean;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.StringUtils;
import libraryextra.view.ListViewForScrollView;

public class FragmentExclusiveLessonClassList extends BaseFragment {
    private CommonAdapter<ExclusiveLessonDetailBean.DataBean.CustomizedGroupBean.ScheduledLessonsBean> scheduleAdapter;
    private List<ExclusiveLessonDetailBean.DataBean.CustomizedGroupBean.ScheduledLessonsBean> scheduleList = new ArrayList<>();
    private CommonAdapter<ExclusiveLessonDetailBean.DataBean.CustomizedGroupBean.OfflineLessonsBean> offlineAdapter;
    private List<ExclusiveLessonDetailBean.DataBean.CustomizedGroupBean.OfflineLessonsBean> offlineList = new ArrayList<>();

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private ExclusiveLessonDetailBean.DataBean data;
    private View scheduleText;
    private View offlineText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exclusive_lesson_class_list, container, false);
        initview(view);
        return view;
    }


    private void initview(View view) {
        scheduleText = view.findViewById(R.id.schedule_text);
        offlineText = view.findViewById(R.id.offline_text);
        ListViewForScrollView scheduleListView = (ListViewForScrollView) view.findViewById(R.id.schedule_list);
        scheduleListView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        ListViewForScrollView offlineListView = (ListViewForScrollView) view.findViewById(R.id.offline_list);
        offlineListView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        scheduleAdapter = new CommonAdapter<ExclusiveLessonDetailBean.DataBean.CustomizedGroupBean.ScheduledLessonsBean>(getActivity(), scheduleList, R.layout.item_fragment_exclusive_class_list_schedule) {

            @Override
            public void convert(ViewHolder holder, ExclusiveLessonDetailBean.DataBean.CustomizedGroupBean.ScheduledLessonsBean item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.live_time, item.getStart_time());
                holder.setText(R.id.status, getStatus(item.getStatus()));
                try {
                    holder.setText(R.id.class_date, format.format(parse.parse(item.getClass_date())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (isFinished(item.getStatus())) {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff999999);
                    if (data != null && data.getTicket() != null && !StringUtils.isNullOrBlanK(data.getTicket().getType()) && data.getTicket().getType().equals("LiveStudio::BuyTicket") && item.isReplayable()) {
                        ((TextView) holder.getView(R.id.status)).setTextColor(0xffC4483C);
                        holder.setText(R.id.status, "观看回放");
                    } else {
                        ((TextView) holder.getView(R.id.status)).setTextColor(0xff999999);
                    }
                } else {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff00a0e9);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff666666);
                }

            }
        };
        scheduleListView.setAdapter(scheduleAdapter);
        offlineAdapter = new CommonAdapter<ExclusiveLessonDetailBean.DataBean.CustomizedGroupBean.OfflineLessonsBean>(getActivity(), offlineList, R.layout.item_fragment_exclusive_class_list_offline) {

            @Override
            public void convert(ViewHolder holder, ExclusiveLessonDetailBean.DataBean.CustomizedGroupBean.OfflineLessonsBean item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.live_time, item.getStart_time());
                holder.setText(R.id.address, "上课地点:" + item.getClass_address());
                holder.setText(R.id.status, getStatus2(item.getStatus()));
                try {
                    holder.setText(R.id.class_date, format.format(parse.parse(item.getClass_date())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (isFinished(item.getStatus())) {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.address)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff999999);
                } else {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff00a0e9);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.address)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff666666);
                }
            }
        };
        offlineListView.setAdapter(offlineAdapter);
        scheduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isFinished(scheduleList.get(position).getStatus())) {
                    if (data != null && data.getTicket() != null && !StringUtils.isNullOrBlanK(data.getTicket().getType()) && data.getTicket().getType().equals("LiveStudio::BuyTicket")) {
                        if (!scheduleList.get(position).isReplayable()) {
                            Toast.makeText(getActivity(), "该课程不可回放", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(getActivity(), NEVideoPlaybackActivity.class);
                        intent.putExtra("id", scheduleList.get(position).getId());
                        intent.putExtra("name", scheduleList.get(position).getName());
                        intent.putExtra("type", "exclusive");
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private String getStatus2(String status) {
       if (status.equals("init")) {//未开始
            return getResourceString(R.string.class_init);
        } else if (status.equals("ready")) {//待上课
            return getResourceString(R.string.class_ready);
        } else if (status.equals("teaching")) {//直播中
            return "上课中";
        } else {//closed finished billing completed
            return getResourceString(R.string.class_over);//已结束
        }
    }

    private String getStatus(String status) {
        if (status.equals("missed")) {
            return getResourceString(R.string.class_missed);
        } else if (status.equals("init")) {//未开始
            return getResourceString(R.string.class_init);
        } else if (status.equals("ready")) {//待开课
            return getResourceString(R.string.class_ready);
        } else if (status.equals("teaching")) {//直播中
            return getResourceString(R.string.class_teaching);
        } else if (status.equals("closed")) {//已直播
            return getResourceString(R.string.class_closed);
        } else if (status.equals("paused")) {
            return getResourceString(R.string.class_teaching);
        } else {//closed finished billing completed
            return getResourceString(R.string.class_over);//已结束
        }
    }

    private boolean isFinished(String item) {
        return item.equals("closed") || item.equals("finished") || item.equals("billing") || item.equals("completed");
    }

    //
    public void setData(ExclusiveLessonDetailBean.DataBean data) {
        this.data = data;
        scheduleList.clear();
        offlineList.clear();
        scheduleList.addAll(data.getCustomized_group().getScheduled_lessons());
        offlineList.addAll(data.getCustomized_group().getOffline_lessons());
        if(scheduleList.size()>0){
            scheduleText.setVisibility(View.VISIBLE);
        }
        if(offlineList.size()>0){
            offlineText.setVisibility(View.VISIBLE);
        }
        if (scheduleAdapter != null) {
            scheduleAdapter.notifyDataSetChanged();
        }
        if (offlineAdapter != null) {
            offlineAdapter.notifyDataSetChanged();
        }
    }
}


