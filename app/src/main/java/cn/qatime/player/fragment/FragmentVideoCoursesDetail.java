package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;

/**
 * @author lungtify
 * @Time 2017/4/14 16:01
 * @Describe
 */

public class FragmentVideoCoursesDetail extends BaseFragment {
    private TextView className;
    private TextView subject;
    private TextView totalClass;
    private TextView grade;
    private TextView totalTime;
    private TextView target;
    private TextView suitable;
    private ImageView image;
    private TextView name;
    private TextView sex;
    private TextView school;
    private TextView teachingYears;
    private WebView teacherDescribe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_courses_detail, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    private void initView(View view) {
        className = (TextView) view.findViewById(R.id.class_name);
        subject = (TextView) view.findViewById(R.id.subject);
        totalClass = (TextView) view.findViewById(R.id.total_class);
        grade = (TextView) view.findViewById(R.id.grade);
        totalTime = (TextView) view.findViewById(R.id.total_time);
        target = (TextView) view.findViewById(R.id.target);
        suitable = (TextView) view.findViewById(R.id.suitable);
        image = (ImageView) view.findViewById(R.id.image);
        name = (TextView) view.findViewById(R.id.name);
        sex = (TextView) view.findViewById(R.id.sex);
        school = (TextView) view.findViewById(R.id.school);
        teachingYears = (TextView) view.findViewById(R.id.teaching_years);
        teacherDescribe = (WebView) view.findViewById(R.id.teacher_describe);
    }
}
