package cn.qatime.player.fragment;

import android.content.Context;
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
import cn.qatime.player.bean.VideoPlayBean;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.VideoLessonsBean;
import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2017/4/14 15:43
 * @Describe
 */

public class FragmentVideoCoursesList extends BaseFragment {
    private List<VideoLessonsBean> list = new ArrayList<>();
    private CommonAdapter<VideoLessonsBean> adapter;
    private VideoCoursesPlayActivity context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_courses_list, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (VideoCoursesPlayActivity) context;
    }

    private void initView(View view) {
        ListView listView = (ListView) view.findViewById(R.id.listView);
        adapter = new CommonAdapter<VideoLessonsBean>(getActivity(), list, R.layout.item_video_courses_list) {
            @Override
            public void convert(ViewHolder holder, VideoLessonsBean item, int position) {
                holder.setText(R.id.number, getPosition(position))
                        .setText(R.id.time_length, "时长 " + item.getVideo().getFormat_tmp_duration());

                TextView name = holder.getView(R.id.name);
                name.setText(item.getName());
                if (context.playingData.getId() == item.getId()) {
                    name.setTextColor(getResources().getColor(R.color.colorPrimary));
                    ((TextView) holder.getView(R.id.status)).setText("正在观看");
                } else {
                    name.setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.status)).setText("已看");
                }
                holder.getView(R.id.status).setVisibility((item.isVisited() || context.playingData.getId() == item.getId()) ? View.VISIBLE : View.GONE);
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (context.playingData != null && context.playingData.getVideo().getId() == list.get(position).getVideo().getId()) {
                    return;
                }
                context.playingData = list.get(position);
                context.playCourses(context.playingData);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private String getPosition(int position) {
        position += 1;
        if (position < 10) return "0" + position;
        return String.valueOf(position);
    }


    public void setData(List<VideoLessonsBean> video_lessons) {
//        video_lessons.get(0)
        list.clear();
        if (context.isTasting()) {
            for (VideoLessonsBean videoLessonsBean : video_lessons) {
                if (videoLessonsBean.isTastable()) {//只显示可试听的课
                    list.add(videoLessonsBean);
                }
            }
        } else {
            list.addAll(video_lessons);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void setProgress(List<VideoPlayBean.DataBean.TicketBean.ProgressBean> progress) {
        if (progress != null && progress.size() > 0) {
            for (VideoPlayBean.DataBean.TicketBean.ProgressBean pro : progress) {
                for (VideoLessonsBean videoLessonsBean : list) {
                    if (pro.getVideo_lesson().getId() == videoLessonsBean.getId() && !StringUtils.isNullOrBlanK(pro.getStatus()) &&
                            pro.getStatus().equals("used")) {
                        videoLessonsBean.setVisited(true);
                    }
                }
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}
