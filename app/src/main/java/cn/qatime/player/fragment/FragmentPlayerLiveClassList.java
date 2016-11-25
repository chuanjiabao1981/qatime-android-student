package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.view.VerticalListView;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.RemedialClassDetailBean;

public class FragmentPlayerLiveClassList extends BaseFragment {
    private CommonAdapter<RemedialClassDetailBean.Lessons> adapter;
    private List<RemedialClassDetailBean.Lessons> list = new ArrayList<>();

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_palyer_live_class_list, container, false);
        initview(view);
        return view;
    }


    private void initview(View view) {
        VerticalListView listView = (VerticalListView) view.findViewById(R.id.list);
        listView.setDividerHeight(0);
        listView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new CommonAdapter<RemedialClassDetailBean.Lessons>(getActivity(), list, R.layout.item_fragment_nevideo_player33) {

            @Override
            public void convert(ViewHolder holder, RemedialClassDetailBean.Lessons item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.live_time, item.getLive_time());
                if (item.getStatus().equals("missed")) {
                    holder.setText(R.id.status, getResourceString(R.string.class_missed));
                } else if (item.getStatus().equals("init")) {//未开始
                    holder.setText(R.id.status, getResourceString(R.string.class_init));
                } else if (item.getStatus().equals("ready")) {//待开课
                    holder.setText(R.id.status, getResourceString(R.string.class_ready));
                } else if (item.getStatus().equals("teaching")) {//直播中
                    holder.setText(R.id.status, getResourceString(R.string.class_teaching));
                } else if (item.getStatus().equals("closed")) {
                    holder.setText(R.id.status, getResourceString(R.string.class_teaching));
                } else if (item.getStatus().equals("paused")) {
                    holder.setText(R.id.status, getResourceString(R.string.class_teaching));
                } else if (item.getStatus().equals("paused_inner")) {//暂停中
                    holder.setText(R.id.status, getResourceString(R.string.class_teaching));
                } else {
                    holder.setText(R.id.status, getResourceString(R.string.class_over));//已结束
                }
                try {
                    holder.setText(R.id.class_date, format.format(parse.parse(item.getClass_date())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        };
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setData(RemedialClassDetailBean.Data data) {
        if (data != null && data.getLessons() != null) {
            list.clear();
            list.addAll(data.getLessons());
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}
