package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import javax.security.auth.Subject;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.RemedialClassDetailBean;
import cn.qatime.player.utils.LogUtils;

public class FragmentRemedialClassDetail1 extends BaseFragment {

    TextView describe;
    TextView name;
    TextView classstarttime;
    TextView subject;
    TextView grade;
    TextView timetostart;
    TextView status;
    RemedialClassDetailBean data;


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
    }

    public void setData(RemedialClassDetailBean data) {
        RemedialClassDetailBean.Data bean = data.getData();
        if (bean != null) {
            name.setText("名称：" + bean.getName());
            subject.setText("科目类型：" + bean.getSubject() + "\n" + "授课老师：" + bean.getTeacher().getName() + "\n" + "课程进度：" + bean.getCompleted_lesson_count() + "/" + bean.getPreset_lesson_count());
            classstarttime.setText("开课时间：" + bean.getLive_start_time() + "\n" + "结课时间" + bean.getLive_end_time() + "\n" + "授课方式");
            grade.setText("年级类型：" + bean.getGrade() + "\n" + "课时总数：" + bean.getPreset_lesson_count() + "\n课时" + (bean.getPreset_lesson_count() - bean.getCompleted_lesson_count()) + "剩余课时");
            if (bean.getStatus().equals("preview")) {
                status.setText("当前状态：招生中");
            } else if (bean.getStatus().equals("teaching")) {
                status.setText("当前状态：已开课");
            } else {
                status.setText("当前状态：已结束");
            }
            timetostart.setText("距离开课还有" + "天");
            describe.setText(bean.getDescription());
        }
    }
}
