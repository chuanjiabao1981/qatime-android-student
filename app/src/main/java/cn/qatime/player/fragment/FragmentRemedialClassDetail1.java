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
import cn.qatime.player.bean.RemedialClassDetailBean;

public class FragmentRemedialClassDetail1 extends BaseFragment {

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
    TextView teachway;
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
        teachway = (TextView) view.findViewById(R.id.teach_way);
        totalclass = (TextView) view.findViewById(R.id.total_class);
        remainclass = (TextView) view.findViewById(R.id.remain_class);
    }

    public void setData(RemedialClassDetailBean data){
        RemedialClassDetailBean.Data bean = data.getData();
        if (bean != null) {

            name.setText("名称：" + bean.getName());
            subject.setText("科目类型：" + bean.getSubject());
                teacher.setText("授课老师：" + bean.getTeacher().getName());
            progress.setText("课程进度：" + bean.getCompleted_lesson_count() + "/" + bean.getPreset_lesson_count());
            try {
                classstarttime.setText("开课时间：" + format.format(parse.parse(bean.getLive_start_time())));
                classendtime.setText("结课时间：" + format.format(parse.parse(bean.getLive_end_time())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            teachway.setText("授课方式：未知");
            grade.setText("年级类型：" + bean.getGrade());
            totalclass.setText("课时总数：" + bean.getPreset_lesson_count() + "课时");
            remainclass.setText("剩余课时："+ (bean.getPreset_lesson_count() - bean.getCompleted_lesson_count()));
            if (bean.getStatus().equals("preview")) {
                status.setText("当前状态：招生中");
            } else if (bean.getStatus().equals("teaching")) {
                status.setText("当前状态：已开课");
            } else {
                status.setText("当前状态：已结束");
            }
            timetostart.setText("距离开课还有   " + "天");

            describe.setText(bean.getDescription());
        }
    }
}
