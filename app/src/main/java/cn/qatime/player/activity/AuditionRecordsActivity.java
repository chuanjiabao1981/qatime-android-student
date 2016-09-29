package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import cn.qatime.player.R;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import cn.qatime.player.base.BaseActivity;

/**
 * @author luntify
 * @date 2016/8/10 15:36
 * @Description 试听记录
 */
public class AuditionRecordsActivity extends BaseActivity {

    private PullToRefreshListView listView;
    private java.util.List<String> list = new ArrayList<>();
    private CommonAdapter<String> adapter;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audition_records);
        setTitle(getResourceString(R.string.audition_records));
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        initView();
    }

    private void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.list);

        adapter = new CommonAdapter<String>(this, list, R.layout.item_audition_records) {
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
