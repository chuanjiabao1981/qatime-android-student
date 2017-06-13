package cn.qatime.player.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;

/**
 * @author lungtify
 * @Time 2017/6/13 11:27
 * @Describe
 */

public class PlayBackListActivity extends BaseActivity implements View.OnClickListener {
    private TextView latest;
    private TextView most;
    private PullToRefreshListView listView;
    private List<String> datas = new ArrayList<>();
    private CommonAdapter<String> adapter;
    private int latestResult = 1;

    private void assignViews() {
        latest = (TextView) findViewById(R.id.latest);
        most = (TextView) findViewById(R.id.most);
        listView = (PullToRefreshListView) findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));
        View empty = View.inflate(PlayBackListActivity.this, R.layout.empty_view, null);
        listView.setEmptyView(empty);
        adapter = new CommonAdapter<String>(this, datas, R.layout.item_play_back_list) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {

            }
        };
        listView.setAdapter(adapter);
        latest.setOnClickListener(this);
        most.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_back_list);
        setTitles("全部精彩回放");
        assignViews();
    }

    private void refreshState(TextView view, int result) {//-1未选 0上 1下
        if (result == -1) {
            view.setCompoundDrawables(null, null, null, null);
            view.setTextColor(0xff666666);
        } else if (result == 0) {
            Drawable up = getResources().getDrawable(R.mipmap.arrow_up);
            up.setBounds(0, 0, up.getMinimumWidth(), up.getMinimumHeight());
            view.setCompoundDrawables(null, null, up, null);
            view.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (result == 1) {
            Drawable down = getResources().getDrawable(R.mipmap.arrow_down);
            down.setBounds(0, 0, down.getMinimumWidth(), down.getMinimumHeight());
            view.setCompoundDrawables(null, null, down, null);
            view.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.latest:
                if (latestResult == -1) {
                    latestResult = 1;
                } else if (latestResult == 0) {
                    latestResult = 1;
                } else if (latestResult == 1) {
                    latestResult = 0;
                }
                refreshState(latest, latestResult);
                most.setTextColor(0xff666666);
                break;
            case R.id.most:
                latestResult = -1;
                refreshState(latest, latestResult);
                most.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
    }
}
