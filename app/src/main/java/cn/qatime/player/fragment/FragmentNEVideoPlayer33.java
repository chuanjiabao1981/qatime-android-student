package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.StringUtils;
import cn.qatime.player.view.VerticalListView;

public class FragmentNEVideoPlayer33 extends BaseFragment {
    private CommonAdapter<RemedialClassDetailBean.Lessons> adapter;
    private List<RemedialClassDetailBean.Lessons> lists = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_nevideo_player33, null);
        VerticalListView list = (VerticalListView) view.findViewById(R.id.list);
        adapter = new CommonAdapter<RemedialClassDetailBean.Lessons>(getActivity(), lists, R.layout.item_fragment_nevideo_player33) {
            @Override
            public void convert(ViewHolder holder, RemedialClassDetailBean.Lessons item, int position) {
                holder.setText(R.id.number, StringUtils.Int2String(position + 1));
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.time, item.getClass_date() + " " + item.getLive_time());
                holder.setText(R.id.status, item.getStatus());
            }
        };
        list.setAdapter(adapter);
        return view;
    }

    public void setData(RemedialClassDetailBean data) {
        if (data != null && data.getData() != null) {
            lists.clear();
            lists.addAll(data.getData().getLessons());
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}
