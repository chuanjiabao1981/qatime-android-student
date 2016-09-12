package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.RemedialClassDetailBean;

public class FragmentRemedialClassDetail1 extends BaseFragment {

    TextView describe;
//    TextView name;
    TextView classstarttime;
    TextView subject;
    TextView grade;
    TextView status;
    TextView classendtime;
    TextView teacher;
    TextView totalclass;
    TextView remainclass;
    //    TextView teachway;
    TextView progress;
    RemedialClassDetailBean data;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remedial_class_detail1, container, false);
        initview(view);

        return view;
    }


    private void initview(View view) {
        subject = (TextView) view.findViewById(R.id.subject);
        classstarttime = (TextView) view.findViewById(R.id.class_start_time);
        grade = (TextView) view.findViewById(R.id.grade);
        status = (TextView) view.findViewById(R.id.status);
        describe = (TextView) view.findViewById(R.id.describe);
        classendtime = (TextView) view.findViewById(R.id.class_end_time);
        teacher = (TextView) view.findViewById(R.id.teacher);
        progress = (TextView) view.findViewById(R.id.progress);
        totalclass = (TextView) view.findViewById(R.id.total_class);
        remainclass = (TextView) view.findViewById(R.id.remain_class);
    }

    public void setData(RemedialClassDetailBean data) {
        RemedialClassDetailBean.Data bean = data.getData();
        if (bean != null) {

//            name.setText(getResourceString(R.string.class_name) + bean.getName());
            subject.setText(getResourceString(R.string.subject_type) + bean.getSubject());
            teacher.setText(getResourceString(R.string.teacher) + bean.getTeacher().getName());
            progress.setText(getResourceString(R.string.progress) + bean.getCompleted_lesson_count() + "/" + bean.getPreset_lesson_count());
            classstarttime.setText(getResourceString(R.string.class_start_time) + bean.getLive_start_time());
            classendtime.setText(getResourceString(R.string.class_end_time) + bean.getLive_end_time());

//            teachway.setText(getResourceString(R.string.teach_way));
            grade.setText(getResourceString(R.string.grade_type) + bean.getGrade());
            totalclass.setText(getResourceString(R.string.total_class_hours) + bean.getPreset_lesson_count());
            remainclass.setText(getResourceString(R.string.remain_class) + (bean.getPreset_lesson_count() - bean.getCompleted_lesson_count()));
            if (bean.getStatus().equals("preview")) {
                status.setText(getResourceString(R.string.status_preview));
            } else if (bean.getStatus().equals("teaching")) {
                status.setText(getResourceString(R.string.status_teaching));
            } else {
                status.setText(getResourceString(R.string.status_over));
            }

            describe.setText(bean.getDescription());
        }
    }
}
