package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.adapter.CommonAdapter;
import cn.qatime.player.adapter.ViewHolder;
import cn.qatime.player.base.BaseFragment;

public class FragmentPersonalMyOrder1 extends BaseFragment {
    private PullToRefreshListView listView;
    private java.util.List<String> list = new ArrayList<>();
    private CommonAdapter<String> adapter;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_my_order1, container, false);
        initview(view);
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);

        adapter = new CommonAdapter<String>(getActivity(), list, R.layout.item_fragment_personal_my_order1) {
            @Override
            public void convert(ViewHolder helper, String item, int position) {

            }
        };
        listView.setAdapter(adapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                listView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                listView.onRefreshComplete();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

}
