package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.InteractCourseDetailBean;

public class FragmentInteractDetailClassList extends BaseFragment {
    private CommonAdapter<InteractCourseDetailBean.DataBean.InteractiveLessonsBean> adapter;
    private List<InteractCourseDetailBean.DataBean.InteractiveLessonsBean> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interact_detail_class_list, container, false);
        initview(view);
        return view;
    }


    private void initview(View view) {
        ListView listView = (ListView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        listView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new CommonAdapter<InteractCourseDetailBean.DataBean.InteractiveLessonsBean>(getActivity(), list, R.layout.item_interact_course_detail3) {

            @Override
            public void convert(ViewHolder holder, InteractCourseDetailBean.DataBean.InteractiveLessonsBean item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.teacher_name, "教师：" + item.getTeacher().getName());
                holder.setText(R.id.class_date, item.getClass_date());
                holder.setText(R.id.live_time, item.getStart_time() + "-" + item.getEnd_time());
                if (item.getStatus().equals("missed")) {
                    holder.setText(R.id.status, getResourceString(R.string.class_missed));
                } else if (item.getStatus().equals("init")) {//未开始
                    holder.setText(R.id.status, getResourceString(R.string.class_init));
                } else if (item.getStatus().equals("ready")) {//待开课
                    holder.setText(R.id.status, getResourceString(R.string.class_ready));
                } else if (item.getStatus().equals("teaching")) {//直播中
                    holder.setText(R.id.status, getResourceString(R.string.class_teaching));
                } else if (item.getStatus().equals("closed")) {//已直播
                    holder.setText(R.id.status, getResourceString(R.string.class_closed));
                } else if (item.getStatus().equals("paused")) {
                    holder.setText(R.id.status, getResourceString(R.string.class_teaching));
                } else {//closed finished billing completed
                    holder.setText(R.id.status, getResourceString(R.string.class_over));//已结束
                }
                if (isFinished(item)) {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.teacher_name)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff999999);
                } else {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff00a0e9);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.teacher_name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff666666);
                }

            }
        };
        listView.setAdapter(adapter);
    }
    private boolean isFinished(InteractCourseDetailBean.DataBean.InteractiveLessonsBean item) {
        return item.getStatus().equals("closed") || item.getStatus().equals("finished") || item.getStatus().equals("billing") || item.getStatus().equals("completed");
    }
    public void setData(InteractCourseDetailBean data) {
        if (data != null && data.getData() != null) {
            list.clear();
            list.addAll(data.getData().getInteractive_lessons());
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}


