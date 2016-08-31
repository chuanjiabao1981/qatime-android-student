package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.RemedialClassDetailBean;

public class FragmentNEVideoPlayer31 extends BaseFragment {

    TextView describe;
    TextView name;
    TextView classstarttime;
    TextView subject;
    TextView grade;
    TextView timetostart;
    TextView status;
    TextView classendtime;
    TextView teacher;
    TextView totalclass;
    TextView remainclass;
    //    TextView teachway;
    TextView progress;
    RemedialClassDetailBean data;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_nevideo_player31, null);
        initview(view);
        return view;
    }

    private void initview(View view) {
        name = (TextView) view.findViewById(R.id.name);
        subject = (TextView) view.findViewById(R.id.subject);
        classstarttime = (TextView) view.findViewById(R.id.class_start_time);
        timetostart = (TextView) view.findViewById(R.id.time_to_start);
        grade = (TextView) view.findViewById(R.id.grade);
        status = (TextView) view.findViewById(R.id.status);
        describe = (TextView) view.findViewById(R.id.describe);
        classendtime = (TextView) view.findViewById(R.id.class_end_time);
        teacher = (TextView) view.findViewById(R.id.teacher);
        progress = (TextView) view.findViewById(R.id.progress);
        totalclass = (TextView) view.findViewById(R.id.total_class);
        remainclass = (TextView) view.findViewById(R.id.remain_class);
    }

    public void setData(RemedialClassDetailBean.Data bean) {
        if (bean != null) {
            name.setText(getResources().getString(R.string.class_name) + bean.getName());
            subject.setText(getResources().getString(R.string.subject_type) + bean.getSubject());
            teacher.setText(getResources().getString(R.string.teacher) + bean.getTeacher().getName());
            progress.setText(getResources().getString(R.string.progress) + bean.getCompleted_lesson_count() + "/" + bean.getPreset_lesson_count());
            classstarttime.setText(getResources().getString(R.string.class_start_time) + bean.getLive_start_time());
            classendtime.setText(getResources().getString(R.string.class_end_time) + bean.getLive_end_time());
            grade.setText(getResources().getString(R.string.grade_type) + bean.getGrade());
            remainclass.setText(getResources().getString(R.string.remain_class) + (bean.getPreset_lesson_count() - bean.getCompleted_lesson_count()));
            describe.setText(bean.getDescription());
        }
    }
}
