package cn.qatime.player.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.view.GridViewForScrollView;

/**
 * @author lungtify
 * @Time 2016/10/25 16:23
 * @Describe
 */
public class TeacherDataActivity extends BaseActivity {
    private ImageView banner;
    private ImageView headSculpture;
    private TextView sex;
    private TextView name;
    private TextView describe;
    private GridViewForScrollView grid;
    private List<String> data = new ArrayList<>();
    private TextView teachAge;
    private TextView school;

    private void assignViews() {
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        PullToRefreshScrollView scroll = (PullToRefreshScrollView) findViewById(R.id.scroll);
        scroll.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        banner = (ImageView) findViewById(R.id.banner);
        headSculpture = (ImageView) findViewById(R.id.head_sculpture);
        sex = (TextView) findViewById(R.id.sex);
        name = (TextView) findViewById(R.id.name);
        teachAge = (TextView) findViewById(R.id.teach_age);
        school = (TextView) findViewById(R.id.school);
        describe = (TextView) findViewById(R.id.describe);
        grid = (GridViewForScrollView) findViewById(R.id.grid);
        scroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_data);
        setTitle("高玉明");
        assignViews();
        CommonAdapter<String> adapter = new CommonAdapter<String>(this, data, R.layout.item_teacher_data) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {

            }
        };
        grid.setAdapter(adapter);
    }
}
