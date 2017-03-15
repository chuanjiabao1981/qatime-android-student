package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;

/**
 * @author lungtify
 * @Time 2017/3/15 14:46
 * @Describe 筛选条件页面
 */

public class ScreeningConditionActivity extends BaseActivity {
    private GridView timeGrid;
    private GridView courseGrid;
    private GridView tasteGrid;
    private TextView start;
    private TextView end;
    private Button next;

    private void assignViews() {
        timeGrid = (GridView) findViewById(R.id.time_grid);
        courseGrid = (GridView) findViewById(R.id.course_grid);
        tasteGrid = (GridView) findViewById(R.id.taste_grid);
        start = (TextView) findViewById(R.id.start);
        end = (TextView) findViewById(R.id.end);
        next = (Button) findViewById(R.id.next);
        List<String> timeData = new ArrayList<>();
        timeData.add("所有");
        timeData.add("近一个月");
        timeData.add("近二个月");
        timeData.add("近三个月");
        timeData.add("近半年");
        timeData.add("近一年");
        CommonAdapter<String> timeAdapter = new CommonAdapter<String>(this, timeData, R.layout.item_screening_condition) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {

            }
        };
        timeGrid.setAdapter(timeAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_condition);

        assignViews();
        setTitles("筛选");
        setRightText("重置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
