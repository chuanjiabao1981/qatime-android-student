package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

public class FragmentRemedialClassDetail3 extends BaseFragment {
    private CommonAdapter<RemedialClassDetailBean.Lessons> adapter;
    private List<RemedialClassDetailBean.Lessons> list = new ArrayList<>();

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remedial_class_detail3, container, false);
        initview(view);
        return view;
    }


    private void initview(View view) {
        ListView listView = (ListView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        listView.setDividerHeight(0);
        adapter = new CommonAdapter<RemedialClassDetailBean.Lessons>(getActivity(), list, R.layout.item_fragment_remedial_class_detail3) {

            @Override
            public void convert(ViewHolder holder, RemedialClassDetailBean.Lessons item, int position) {
                holder.setText(R.id.number, StringUtils.Int2String(position + 1));
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.live_time, item.getLive_time());
                if (item.getStatus().equals("teaching")) {//直播中
                    holder.setText(R.id.status, getResourceString(R.string.class_teaching));
                } else if (item.getStatus().equals("paused")) {
                    holder.setText(R.id.status, getResourceString(R.string.class_teaching));
                } else if (item.getStatus().equals("init")) {//未开始
                    holder.setText(R.id.status, getResourceString(R.string.class_init));
                } else if (item.getStatus().equals("ready")) {//待开课
                    holder.setText(R.id.status, getResourceString(R.string.class_ready));
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

    public void setData(RemedialClassDetailBean data) {
        if (data != null && data.getData() != null) {
            list.clear();
            list.addAll(data.getData().getLessons());
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}
