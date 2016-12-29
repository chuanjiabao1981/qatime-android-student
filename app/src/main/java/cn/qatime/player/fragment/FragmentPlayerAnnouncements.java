package cn.qatime.player.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.Announcements;

public class FragmentPlayerAnnouncements extends BaseFragment {
    private PullToRefreshListView listView;
    private CommonAdapter<Announcements.DataBean.AnnouncementsBean> adapter;
    private List<Announcements.DataBean.AnnouncementsBean> items = new ArrayList<>();
    private boolean hasLoad = false;
    private Handler hd = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (hasLoad) {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                    hd.removeCallbacks(this);
                } else {
                    hd.postDelayed(this, 200);
                }
            } else {
                hd.postDelayed(this, 200);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_player_announcements, null);
        initview(view);
        hasLoad = true;
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        View empty = View.inflate(getActivity(), R.layout.empty_view, null);
        TextView textEmpty = (TextView) empty.findViewById(R.id.text_empty);
        textEmpty.setText("暂无辅导班公告");
        listView.setEmptyView(empty);
        listView.setMode(PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));


        adapter = new CommonAdapter<Announcements.DataBean.AnnouncementsBean>(getActivity(), items, R.layout.item_fragment_nevideo_player1) {
            @Override
            public void convert(ViewHolder helper, Announcements.DataBean.AnnouncementsBean item, int position) {
                if (position == 0) {
//                    helper.getView(R.id.late).setVisibility(View.VISIBLE);
                    ((TextView) helper.getView(R.id.time)).setTextColor(0xff999999);
                    ((TextView) helper.getView(R.id.describe)).setTextColor(0xff666666);
                    helper.setImageResource(R.id.notify, R.mipmap.announcements_new);
                } else {
//                    helper.getView(R.id.late).setVisibility(View.GONE);
                    ((TextView) helper.getView(R.id.time)).setTextColor(0xff999999);
                    ((TextView) helper.getView(R.id.describe)).setTextColor(0xff999999);
                    helper.setImageResource(R.id.notify, R.mipmap.announcements_normal);
                }
                helper.setText(R.id.time, item.getEdit_at());
                helper.setText(R.id.describe, item.getAnnouncement());
            }
        };
        listView.setAdapter(adapter);
    }


    public void setData(List<Announcements.DataBean.AnnouncementsBean> announcements) {
        items.clear();
        items.addAll(announcements);
        hd.postDelayed(runnable, 200);
    }

}
