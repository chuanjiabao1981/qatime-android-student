package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.ExclusiveLessonDetailBean;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.StringUtils;

public class FragmentExclusiveLessonClassList extends BaseFragment {
    private CommonAdapter<ExclusiveLessonDetailBean.DataBean.CustomizedGroupBean.ScheduledLessonsBean> scheduleAdapter;
    private List<ExclusiveLessonDetailBean.DataBean.CustomizedGroupBean.ScheduledLessonsBean> scheduleList = new ArrayList<>();
    private CommonAdapter<ExclusiveLessonDetailBean.DataBean.CustomizedGroupBean.OfflineLessonsBean> offlineAdapter;
    private List<ExclusiveLessonDetailBean.DataBean.CustomizedGroupBean.OfflineLessonsBean> offlineList = new ArrayList<>();

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exclusive_lesson_class_list, container, false);
        initview(view);
        return view;
    }


    private void initview(View view) {
        ListView scheduleListView = (ListView) view.findViewById(R.id.schedule_list);
        scheduleListView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        ListView offlineListView = (ListView) view.findViewById(R.id.offline_list);
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
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff999999);
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
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                LiveLessonDetailBean.DataBean.CourseBean.LessonsBean item = list.get(position);
//                if (isFinished(item)) {
//                    if (data != null && data.getTicket() != null && !StringUtils.isNullOrBlanK(data.getTicket().getType()) && data.getTicket().getType().equals("LiveStudio::BuyTicket")) {
//                        if (!item.isReplayable()) {
//                            Toast.makeText(getActivity(), "该课程不可回放", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        if (item.getLeft_replay_times() <= 0) {
//                            Toast.makeText(getActivity(), getResourceString(R.string.have_no_left_playback_count), Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        Intent intent = new Intent(getActivity(), NEVideoPlaybackActivity.class);
//                        intent.putExtra("id", item.getId());
//                        startActivity(intent);
//                    }
//                }
//            }
//        });
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
    public void setData(ExclusiveLessonDetailBean data) {
        if (data.getData().getCustomized_group() != null) {
            scheduleList.clear();
            offlineList.clear();
            scheduleList.addAll(data.getData().getCustomized_group().getScheduled_lessons());
            offlineList.addAll(data.getData().getCustomized_group().getOffline_lessons());
            if (scheduleAdapter != null) {
                scheduleAdapter.notifyDataSetChanged();
            }
            if (offlineAdapter != null) {
                offlineAdapter.notifyDataSetChanged();
            }
        }
    }
}


