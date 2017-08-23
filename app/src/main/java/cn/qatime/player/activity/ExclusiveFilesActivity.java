package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.ExclusiveFilesBean;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;

/**
 * Created by lenovo on 2017/8/15.
 */

public class ExclusiveFilesActivity extends BaseActivity {

    private PullToRefreshListView list;
    private CommonAdapter<ExclusiveFilesBean> adapter;
    private List<ExclusiveFilesBean> fileList = new ArrayList<>();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_files);
        setTitles("课件管理");
        id = getIntent().getIntExtra("id", 0);
        initView();
        initData();
    }

    private void initData() {
        ExclusiveFilesBean file = new ExclusiveFilesBean();
        file.setName("aaaaa.apx");
        file.setSize("100kb");
        file.setTime("上传时间1991-11-11 11:11:11");
        file.setUrl("softwares/7/download");
        fileList.add(file);
        fileList.add(file);
        fileList.add(file);
        fileList.add(file);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        list = (PullToRefreshListView) findViewById(R.id.list);
        list.setMode(PullToRefreshBase.Mode.BOTH);
        list.setEmptyView(View.inflate(this, R.layout.empty_view, null));
//        list.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
//        list.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
//        list.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
//        list.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
//        list.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
//        list.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));
        adapter = new CommonAdapter<ExclusiveFilesBean>(this, fileList, R.layout.item_exclusive_files) {
            @Override
            public void convert(ViewHolder helper, ExclusiveFilesBean item, int position) {
                helper.setText(R.id.name, item.getName());
                helper.setText(R.id.size, item.getSize());
                helper.setText(R.id.time, item.getTime());
            }
        };
        list.setAdapter(adapter);

//        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
////                page = 1;
////                initData(1);
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
////                page++;
////                initData(2);
//            }
//
//        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExclusiveFilesActivity.this, ExclusiveFileDetailActivity.class);
                intent.putExtra("file", fileList.get(position - 1));
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }
}
