package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.FilterCourseContentActivity;
import cn.qatime.player.base.BaseFragment;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;

/**
 * @author lungtify
 * @Time 2017/3/14 18:22
 * @Describe
 */

public class FragmentHomeSelectSubject extends BaseFragment {

    private ListView listview;
    private GridView gridView;
    private List<String> gradeData = new ArrayList<>();
    private List<String> subjectData = new ArrayList<>();
    private int gradeChecked = 0;
//    private int subjectChecked = 0;

    private void assignViews(View view) {
        listview = (ListView) view.findViewById(R.id.listview);
        gridView = (GridView) view.findViewById(R.id.gridView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_grade_and_subject, container, false);
        assignViews(view);
        gradeData.add("高三");
        gradeData.add("高二");
        gradeData.add("高一");
        gradeData.add("初三");
        gradeData.add("初二");
        gradeData.add("初一");
        gradeData.add("六年级");
        gradeData.add("五年级");
        gradeData.add("四年级");
        gradeData.add("三年级");
        gradeData.add("二年级");
        gradeData.add("一年级");
        final CommonAdapter<String> listAdapter = new CommonAdapter<String>(getActivity(), gradeData, R.layout.item_grade) {

            @Override
            public void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.text, gradeData.get(position));
                if (gradeChecked == position) {
                    ((TextView) holder.getView(R.id.text)).setTextColor(0xfffeb0b0);
                    holder.getView(R.id.text).setBackgroundColor(0xffffffff);
                } else {
                    ((TextView) holder.getView(R.id.text)).setTextColor(0xffcccccc);
                    holder.getView(R.id.text).setBackgroundColor(0xfff2f2f2);
                }
            }
        };
        listview.setAdapter(listAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gradeChecked = position;
                listAdapter.notifyDataSetChanged();
            }
        });
        subjectData.add("全部");
        subjectData.add("语文");
        subjectData.add("数学");
        subjectData.add("英语");
        subjectData.add("政治");
        subjectData.add("历史");
        subjectData.add("地理");
        subjectData.add("物理");
        subjectData.add("化学");
        subjectData.add("生物");
        CommonAdapter<String> gridAdapter = new CommonAdapter<String>(getActivity(), subjectData, R.layout.item_subject) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.text, subjectData.get(position));
//                if (subjectChecked == position) {
//                    ((TextView) holder.getView(R.id.text)).setTextColor(0xfffeb0b0);
//                    holder.getView(R.id.text).setBackgroundColor(0xffffffff);
//                } else {
//                    ((TextView) holder.getView(R.id.text)).setTextColor(0xffcccccc);
//                    holder.getView(R.id.text).setBackgroundColor(0xfff2f2f2);
//                }
            }
        };
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), FilterCourseContentActivity.class);
                intent.putExtra("grade", gradeData.get(gradeChecked));
                intent.putExtra("subject", subjectData.get(position));
                startActivity(intent);
            }
        });
        return view;
    }
}
