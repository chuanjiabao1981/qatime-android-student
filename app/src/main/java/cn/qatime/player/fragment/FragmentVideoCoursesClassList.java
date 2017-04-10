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
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.NEVideoPlaybackActivity;
import cn.qatime.player.base.BaseFragment;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.RemedialClassDetailBean;

import static cn.qatime.player.R.id.status;

/**
 * @author lungtify
 * @Time 2017/4/10 17:05
 * @Describe
 */

public class FragmentVideoCoursesClassList extends BaseFragment {
    private List<RemedialClassDetailBean.Lessons> list = new ArrayList<>();
    private CommonAdapter<RemedialClassDetailBean.Lessons> adapter;

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

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
        adapter = new CommonAdapter<RemedialClassDetailBean.Lessons>(getActivity(), list, R.layout.item_fragment_remedial_class_detail3) {

            @Override
            public void convert(ViewHolder holder, RemedialClassDetailBean.Lessons item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.live_time, item.getLive_time());
                if (item.getStatus().equals("missed")) {
                    holder.setText(status, getResourceString(R.string.class_missed));
                } else if (item.getStatus().equals("init")) {//未开始
                    holder.setText(status, getResourceString(R.string.class_init));
                } else if (item.getStatus().equals("ready")) {//待开课
                    holder.setText(status, getResourceString(R.string.class_ready));
                } else if (item.getStatus().equals("teaching")) {//直播中
                    holder.setText(status, getResourceString(R.string.class_teaching));
                } else if (item.getStatus().equals("closed")) {//已直播
                    holder.setText(status, getResourceString(R.string.class_closed));
                } else if (item.getStatus().equals("paused")) {
                    holder.setText(status, getResourceString(R.string.class_teaching));
                } else {//closed finished billing completed
                    holder.setText(status, getResourceString(R.string.class_over));//已结束
                }
                try {
                    holder.setText(R.id.class_date, format.format(parse.parse(item.getClass_date())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.setText(R.id.view_playback, getString(R.string.playback_count, item.getLeft_replay_times()));
                if (isFinished(item)) {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff999999);
                    ((TextView) holder.getView(status)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff999999);
//                    holder.getView(R.id.view_playback).setVisibility(data.getIs_bought() && item.isReplayable() ? View.VISIBLE : View.GONE);
                } else {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff00a0e9);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff666666);
                    holder.getView(R.id.view_playback).setVisibility(View.GONE);
                }

            }
        };
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                RemedialClassDetailBean.Lessons item = list.get(position);
//                if (isFinished(item)) {
//                    if (data.getIs_bought()) {
//                        if (!item.isReplayable()) {
////                        Toast.makeText(getActivity(), getResourceString(R.string.no_playback_video), Toast.LENGTH_SHORT).show();
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

    private boolean isFinished(RemedialClassDetailBean.Lessons item) {
        return item.getStatus().equals("closed") || item.getStatus().equals("finished") || item.getStatus().equals("billing") || item.getStatus().equals("completed");
    }
}
