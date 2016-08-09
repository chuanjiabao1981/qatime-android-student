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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.adapter.CommonAdapter;
import cn.qatime.player.adapter.ViewHolder;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.RemedialClassBean;
import cn.qatime.player.bean.RemedialClassDetailBean;

public class FragmentNEVideoPlayer1 extends BaseFragment {
    private int page = 0;
    private PullToRefreshListView listView;
    private CommonAdapter<String> adapter;
    private List<String> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_nevideo_player1, null);
        initview(view);
        //测试数据
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载");
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开刷新");
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");


        adapter = new CommonAdapter<String>(getActivity(), list, R.layout.item_fragment_nevideo_player1) {
            @Override
            public void convert(ViewHolder helper, String item, int position) {
                if (position == 0) {
                    helper.getView(R.id.late).setVisibility(View.VISIBLE);
                    ((TextView) helper.getView(R.id.time)).setTextColor(0xff151515);
                    ((TextView) helper.getView(R.id.describe)).setTextColor(0xff151515);
                } else {
                    helper.getView(R.id.late).setVisibility(View.GONE);
                    ((TextView) helper.getView(R.id.time)).setTextColor(0xff545454);
                    ((TextView) helper.getView(R.id.describe)).setTextColor(0xff545454);
                }
            }
        };
        listView.setAdapter(adapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }
}
