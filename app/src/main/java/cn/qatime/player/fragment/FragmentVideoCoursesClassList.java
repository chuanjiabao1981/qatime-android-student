package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.VideoCoursesPlayActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.VideoCoursesDetailsBean;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.VideoLessonsBean;

/**
 * @author lungtify
 * @Time 2017/4/10 17:05
 * @Describe
 */

public class FragmentVideoCoursesClassList extends BaseFragment {
    private List<VideoLessonsBean> list = new ArrayList<>();
    private CommonAdapter<VideoLessonsBean> adapter;
    private VideoCoursesDetailsBean data;

//    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
//    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_courses_class_list, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        ListView listView = (ListView) findViewById(R.id.id_stickynavlayout_innerscrollview);
        listView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new CommonAdapter<VideoLessonsBean>(getActivity(), list, R.layout.item_fragment_video_courses_class_list) {

            @Override
            public void convert(ViewHolder holder, VideoLessonsBean item, int position) {
                holder.setText(R.id.name, item.getName());
//                if (item.getStatus().equals("missed")) {
//                    holder.setText(status, getResourceString(R.string.class_missed));
//                } else if (item.getStatus().equals("init")) {//未开始
//                    holder.setText(status, getResourceString(R.string.class_init));
//                } else if (item.getStatus().equals("ready")) {//待开课
//                    holder.setText(status, getResourceString(R.string.class_ready));
//                } else if (item.getStatus().equals("teaching")) {//直播中
//                    holder.setText(status, getResourceString(R.string.class_teaching));
//                } else if (item.getStatus().equals("closed")) {//已直播
//                    holder.setText(status, getResourceString(R.string.class_closed));
//                } else if (item.getStatus().equals("paused")) {
//                    holder.setText(status, getResourceString(R.string.class_teaching));
//                } else {//closed finished billing completed
//                    holder.setText(status, getResourceString(R.string.class_over));//已结束
//                }
                holder.setText(R.id.time, "时长" + item.getVideo().getFormat_tmp_duration());
                if (isFinished(item)) {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff999999);
                } else {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff00a0e9);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
                }

                holder.getView(R.id.taste).setVisibility((!data.getData().getIs_bought() && item.isTastable()) ? View.VISIBLE : View.GONE);
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (data.getData().getIs_bought() || list.get(position).isTastable()) {
                    Intent intent = new Intent(getActivity(), VideoCoursesPlayActivity.class);
                    intent.putExtra("id", list.get(position).getId());
                    startActivity(intent);
                }
            }
        });
    }

    private boolean isFinished(VideoLessonsBean item) {
        return item.getStatus().equals("closed") || item.getStatus().equals("finished") || item.getStatus().equals("billing") || item.getStatus().equals("completed");
    }

    public void setData(VideoCoursesDetailsBean data) {
        this.data = data;
        list.clear();
        list.addAll(data.getData().getVideo_lessons());
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
