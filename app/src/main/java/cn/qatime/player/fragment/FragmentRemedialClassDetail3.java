package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.CommonAdapter;
import cn.qatime.player.adapter.ViewHolder;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.RemedialClassBean;
import cn.qatime.player.bean.RemedialClassDetailBean;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.StringUtils;

public class FragmentRemedialClassDetail3 extends BaseFragment {
    private TextView text;
    private CommonAdapter<RemedialClassDetailBean.Lessons> adapter;
    private List<RemedialClassDetailBean.Lessons> list = new ArrayList<>();

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy月MM月dd日");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remedial_class_detail3, container, false);
        initview(view);
        return view;
    }


    private void initview(View view) {
        ListView listView = (ListView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        adapter = new CommonAdapter<RemedialClassDetailBean.Lessons>(getActivity(), list, R.layout.item_fragment_remedial_class_detail3) {

            @Override
            public void convert(ViewHolder holder, RemedialClassDetailBean.Lessons item, int position) {
                holder.setText(R.id.number, StringUtils.Int2String(position + 1));
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.live_time,item.getLive_time());
                if (item.getStatus().equals("finished")) {
                    holder.setText(R.id.status, "当前状态：已结束");
                } else if (item.getStatus().equals("ready")) {
                    holder.setText(R.id.status, "当前状态：待直播");
                } else {
                    holder.setText(R.id.status, "当前状态：直播中");
                }


                try {
                    holder.setText(R.id.class_date,format.format(parse.parse(item.getClass_date())));
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
