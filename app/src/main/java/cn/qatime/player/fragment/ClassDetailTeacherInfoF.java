package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.bean.SchoolBean;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;

public class ClassDetailTeacherInfoF extends BaseFragment {
    private TextView name;
    private ImageView image;
    private TextView teachingyears;
    private TextView subject;
    private TextView gradetype;
    private TextView school;
    private TextView describe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remedial_class_detail2, container, false);
        initview(view);
        return view;
    }


    private void initview(View view) {
        name = (TextView) view.findViewById(R.id.name);
        image = (ImageView) view.findViewById(R.id.image);
        gradetype = (TextView) view.findViewById(R.id.grade_type);
        subject = (TextView) view.findViewById(R.id.subject);
        teachingyears = (TextView) view.findViewById(R.id.teaching_years);
        school = (TextView) view.findViewById(R.id.school);
        describe = (TextView) view.findViewById(R.id.describe);
    }

    public void setData(RemedialClassDetailBean data) {
        if (data.getData() != null) {

            name.setText(getResourceString(R.string.teacher_name) + data.getData().getTeacher().getName());
            subject.setText(getResourceString(R.string.teacher_subject) + data.getData().getTeacher().getSubject());
            if (!StringUtils.isNullOrBlanK(data.getData().getTeacher().getTeaching_years())) {
                if (data.getData().getTeacher().getTeaching_years().equals("within_three_years")) {
                    teachingyears.setText(getResourceString(R.string.teacher_years) + getResourceString(R.string.within_three_years));
                } else if (data.getData().getTeacher().getTeaching_years().equals("within_ten_years")) {
                    teachingyears.setText(getResourceString(R.string.teacher_years) + getResourceString(R.string.within_ten_years));
                } else if (data.getData().getTeacher().getTeaching_years().equals("within_twenty_years")) {
                    teachingyears.setText(getResourceString(R.string.teacher_years) + getResourceString(R.string.within_twenty_years));
                } else {
                    teachingyears.setText(getResourceString(R.string.teacher_years) +getResourceString(R.string.more_than_ten_years));
                }
            }
            gradetype.setText(getResourceString(R.string.grade_type) + "高中");
            describe.setText(data.getData().getTeacher().getDesc());

            SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getActivity().getCacheDir() + "/school.txt").toString(), SchoolBean.class);

            if (schoolBean != null && schoolBean.getData() != null) {
                for (int i = 0; i < schoolBean.getData().size(); i++) {
                    if (data.getData().getTeacher().getSchool() == schoolBean.getData().get(i).getId()) {
                        school.setText(getResourceString(R.string.teacher_school) + schoolBean.getData().get(i).getName());
                        break;
                    }
                }
            } else {
                school.setText("");
            }

            Glide.with(this).load(data.getData().getTeacher().getAvatar_url()).placeholder(R.mipmap.ic_launcher).crossFade().into(image);

        }


    }


}
