package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;

/**
 * @author lungtify
 * @Time 2017/3/14 15:29
 * @Describe 筛选课程内容页面
 */

public class FilterCourseContentActivity extends BaseActivity {


    private String grade;
    private String subject;
    private PullToRefreshListView listview;
    private CommonAdapter<Object> adapter;
    private List<Object> datas = new ArrayList<>();
    private View screen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_course);
        grade = getIntent().getStringExtra("grade");
        subject = getIntent().getStringExtra("subject");
        setTitles(grade + subject);
        initView();

    }

    private void initView() {
        listview = (PullToRefreshListView) findViewById(R.id.listview);
        datas.add("");
        datas.add("");
        datas.add("");
        datas.add("");
        datas.add("");
        datas.add("");
        datas.add("");
        datas.add("");
        datas.add("");
        datas.add("");
        adapter = new CommonAdapter<Object>(this, datas, R.layout.item_filter_course) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {

            }
        };
        listview.setAdapter(adapter);
        screen = findViewById(R.id.screen);//筛选按钮
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
