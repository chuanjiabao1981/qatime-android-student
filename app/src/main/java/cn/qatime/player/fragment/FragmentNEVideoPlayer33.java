package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import cn.qatime.player.base.BaseFragment;
import libraryextra.utils.StringUtils;
import cn.qatime.player.view.VerticalListView;

public class FragmentNEVideoPlayer33 extends BaseFragment {
    private List<String> lists = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_nevideo_player33, null);
        VerticalListView list = (VerticalListView) view.findViewById(R.id.list);
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        list.setAdapter(new CommonAdapter<String>(getActivity(), lists, R.layout.item_fragment_nevideo_player33) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {
                View root = holder.getView(R.id.root);
                TextView number = holder.getView(R.id.number);
                TextView time = holder.getView(R.id.time);
                TextView status = holder.getView(R.id.status);
                TextView name = holder.getView(R.id.name);
                number.setText(StringUtils.Int2String(position + 1));
                if (position < 3) {
                    number.setTextColor(0xff747474);
                    time.setTextColor(0xff747474);
                    status.setTextColor(0xff747474);
                    name.setTextColor(0xff747474);
                    status.setText("已结束");
                    root.setBackgroundColor(0xffffffff);
                } else if (position == 3) {
                    number.setTextColor(0xff151515);
                    time.setTextColor(0xff151515);
                    status.setTextColor(0xffed0000);
                    name.setTextColor(0xff151515);
                    status.setText("直播中");
                    root.setBackgroundColor(0xffececec);
                } else if (position > 3 && position < 5) {
                    number.setTextColor(0xff747474);
                    time.setTextColor(0xff747474);
                    status.setTextColor(0xff747474);
                    name.setTextColor(0xff747474);
                    status.setText("待上课");
                    root.setBackgroundColor(0xffececec);
                }else {
                    number.setTextColor(0xff747474);
                    time.setTextColor(0xff747474);
                    status.setTextColor(0xff747474);
                    name.setTextColor(0xff747474);
                    status.setText("未开始");
                    root.setBackgroundColor(0xffffffff);
                }
            }
        });
        return view;
    }
}
