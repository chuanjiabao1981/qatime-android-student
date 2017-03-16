package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;
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
        TextView latest = (TextView) findViewById(R.id.latest);
        TextView price = (TextView) findViewById(R.id.price);
        TextView popularity = (TextView) findViewById(R.id.popularity);

        latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        popularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final TextView label = (TextView) findViewById(R.id.label);
        screen = findViewById(R.id.screen);//筛选按钮

        listview = (PullToRefreshListView) findViewById(R.id.listview);
//        datas.add("");
//        datas.add("");
//        datas.add("");
//        datas.add("");
//        datas.add("");
//        datas.add("");
//        datas.add("");
//        datas.add("");
//        datas.add("");
//        datas.add("");
        adapter = new CommonAdapter<Object>(this, datas, R.layout.item_filter_course) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {

            }
        };
        listview.setAdapter(adapter);
        final List<String> labelData = new ArrayList<>();
        labelData.add("不限");
        labelData.add("高考");
        labelData.add("中考");
        labelData.add("会考");
        labelData.add("小升初考试");
        labelData.add("高考志愿");
        labelData.add("英语考级");
        labelData.add("奥数竞赛");
        labelData.add("历年真题");
        labelData.add("期中期末试卷");
        labelData.add("自编试卷");
        labelData.add("暑假课");
        labelData.add("寒假课");
        labelData.add("周末课");
        labelData.add("国庆假期课");
        labelData.add("基础课");
        labelData.add("巩固课");
        labelData.add("提高课");
        labelData.add("外教");
        labelData.add("冲刺");
        labelData.add("重点难点");
        final CommonAdapter<String> labelAdapter = new CommonAdapter<String>(this, labelData, R.layout.item_screening_condition) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.text, item);
                if (label.getText().toString().equals(labelData.get(position))) {
                    ((TextView) holder.getView(R.id.text)).setBackgroundColor(0xffcccccc);
                } else {
                    ((TextView) holder.getView(R.id.text)).setBackgroundResource(R.drawable.text_background_red);
                }
            }
        };
        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //标签
                final AlertDialog dialog = new AlertDialog.Builder(FilterCourseContentActivity.this).create();
                View view = LayoutInflater.from(FilterCourseContentActivity.this).inflate(R.layout.dialog_grid, null);
                GridView gridView = (GridView) view.findViewById(R.id.grid);
                gridView.setAdapter(labelAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        label.setText(labelData.get(position));
                        dialog.dismiss();
                    }
                });
                dialog.setView(view);
                dialog.show();
            }
        });

        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterCourseContentActivity.this, ScreeningConditionActivity.class);
                startActivityForResult(intent, Constant.REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
