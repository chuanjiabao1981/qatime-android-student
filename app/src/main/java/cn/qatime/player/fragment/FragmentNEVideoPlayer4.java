package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.FragmentNEVideoPlayerAdapter4;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.MemberBean;
import libraryextra.utils.StringUtils;
import libraryextra.view.SideBar;

public class FragmentNEVideoPlayer4 extends BaseFragment {
    private ListView listView;
    private List<MemberBean> list = new ArrayList<>();
    private FragmentNEVideoPlayerAdapter4 adapter;
    private SideBar sidebar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_nevideo_player4, null);

        list.add(new MemberBean("八个", StringUtils.getPYIndexStr("八个".substring(0, 1))));
        list.add(new MemberBean("八哥", StringUtils.getPYIndexStr("八哥".substring(0, 1))));
        list.add(new MemberBean("蔡蔡", StringUtils.getPYIndexStr("蔡蔡".substring(0, 1))));
        list.add(new MemberBean("大大", StringUtils.getPYIndexStr("大大".substring(0, 1))));
        list.add(new MemberBean("吴用", StringUtils.getPYIndexStr("吴用".substring(0, 1))));
        list.add(new MemberBean("什么鬼", StringUtils.getPYIndexStr("什么鬼".substring(0, 1))));
        list.add(new MemberBean("鬼脚七", StringUtils.getPYIndexStr("鬼脚七".substring(0, 1))));
        list.add(new MemberBean("王小小", StringUtils.getPYIndexStr("王小小".substring(0, 1))));
        list.add(new MemberBean("李淡淡", StringUtils.getPYIndexStr("李淡淡".substring(0, 1))));
        list.add(new MemberBean("啊大大", StringUtils.getPYIndexStr("啊大大".substring(0, 1))));
        list.add(new MemberBean("李沐阳", StringUtils.getPYIndexStr("李沐阳".substring(0, 1))));
        list.add(new MemberBean("胡春梅", StringUtils.getPYIndexStr("胡春梅".substring(0, 1))));
        list.add(new MemberBean("司马光", StringUtils.getPYIndexStr("司马光".substring(0, 1))));
        list.add(new MemberBean("天君", StringUtils.getPYIndexStr("天君".substring(0, 1))));
        list.add(new MemberBean("江大莎", StringUtils.getPYIndexStr("江大莎".substring(0, 1))));
        list.add(new MemberBean("潘盼盼", StringUtils.getPYIndexStr("潘盼盼".substring(0, 1))));
        list.add(new MemberBean("周周周", StringUtils.getPYIndexStr("周周周".substring(0, 1))));

        Collections.sort(list, new Comparator<MemberBean>() {
            @Override
            public int compare(MemberBean lhs, MemberBean rhs) {
                return lhs.getFirstLetter().compareTo(rhs.getFirstLetter());
            }
        });
        sidebar = (SideBar) view.findViewById(R.id.sidebar);
        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new FragmentNEVideoPlayerAdapter4(getActivity(), list, R.layout.item_fragment_nevideo_player4);
        listView.setAdapter(adapter);
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                if (s.equals("#")) {
                    listView.setSelection(0);
                } else {
                    int position = adapter.getPositionByLetter(s);
                    if (position >= 0) {
                        listView.setSelection(position);
                    }
                }
            }
        });
        return view;
    }
}
