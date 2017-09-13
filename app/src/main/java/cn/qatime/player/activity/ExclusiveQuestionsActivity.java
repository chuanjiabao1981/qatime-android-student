package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;

/**
 * Created by lenovo on 2017/8/24.
 */

public class ExclusiveQuestionsActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listview;
    private List<Object> datas = new ArrayList<>();
    private CommonAdapter<Object> adapter;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_lesson_questions);
        setTitles("课程提问");
        setRightImage(R.mipmap.add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExclusiveQuestionsActivity.this, QuestionEditActivity.class);
                startActivity(intent);
            }
        });
        initView();
        getData(0);
    }


    private void initView() {
        findViewById(R.id.all).setOnClickListener(this);
        findViewById(R.id.me).setOnClickListener(this);
        listview = (PullToRefreshListView) findViewById(R.id.listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.getLoadingLayoutProxy( true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listview.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listview.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listview.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));
        listview.setEmptyView(View.inflate(this, R.layout.empty_view, null));
        adapter = new CommonAdapter<Object>(this, datas, R.layout.item_exclusive_lesson_question) {

            @Override
            public void convert(ViewHolder holder, Object item, int position) {

            }
        };
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(ExclusiveQuestionsActivity.this, QuestionDetailsActivity.class);
//                startActivity(intent);
            }
        });
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                getData(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page += 1;
                getData(1);
            }
        });
    }

    private void getData(int type) {
        if(type==0){
            page=1;
        }

        datas.add(null);
        datas.add(null);
        datas.add(null);
        datas.add(null);
        datas.add(null);
        adapter.notifyDataSetChanged();
        listview.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {

    }
}
