package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.qatime.player.R;
import cn.qatime.player.activity.TeacherDataActivity;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.bean.SchoolBean;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;

public class FragmentClassDetailTeacherInfo extends BaseFragment {
    private TextView name;
    private ImageView image;
    private TextView teachingyears;
    private TextView school;
    private TextView describe;
    private TextView sex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_detail_teacher_info, container, false);
        initview(view);
        return view;
    }


    private void initview(View view) {
        name = (TextView) view.findViewById(R.id.name);
        image = (ImageView) view.findViewById(R.id.image);
        teachingyears = (TextView) view.findViewById(R.id.teaching_years);
        school = (TextView) view.findViewById(R.id.school);
        describe = (TextView) view.findViewById(R.id.describe);
        sex = (TextView) view.findViewById(R.id.sex);
    }

    public void setData(final RemedialClassDetailBean data) {
        if (data.getData() != null && data.getData().getTeacher() != null) {
            sex.setText(getSex(data.getData().getTeacher().getGender()));
            sex.setTextColor(getSexColor(data.getData().getTeacher().getGender()));
            name.setText(data.getData().getTeacher().getName());
            if (!StringUtils.isNullOrBlanK(data.getData().getTeacher().getTeaching_years())) {
                if (data.getData().getTeacher().getTeaching_years().equals("within_three_years")) {
                    teachingyears.setText(getResourceString(R.string.teacher_years) + " " + getResourceString(R.string.within_three_years));
                } else if (data.getData().getTeacher().getTeaching_years().equals("within_ten_years")) {
                    teachingyears.setText(getResourceString(R.string.teacher_years) + " " + getResourceString(R.string.within_ten_years));
                } else if (data.getData().getTeacher().getTeaching_years().equals("within_twenty_years")) {
                    teachingyears.setText(getResourceString(R.string.teacher_years) + " " + getResourceString(R.string.within_twenty_years));
                } else {
                    teachingyears.setText(getResourceString(R.string.teacher_years) + " " + getResourceString(R.string.more_than_ten_years));
                }
            }
            describe.setText(StringUtils.isNullOrBlanK(data.getData().getTeacher().getDesc()) ? "暂无简介" : data.getData().getTeacher().getDesc());

            SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getActivity().getCacheDir() + "/school.txt").toString(), SchoolBean.class);
            if (schoolBean != null && schoolBean.getData() != null) {
                for (int i = 0; i < schoolBean.getData().size(); i++) {
                    if (data.getData().getTeacher().getSchool() == schoolBean.getData().get(i).getId()) {
                        school.setText(getResourceString(R.string.teacher_school) + " " + schoolBean.getData().get(i).getName());
                        break;
                    }
                }
            } else {
                school.setText(getResourceString(R.string.teacher_school) + " 暂无");
            }

            Glide.with(this).load(data.getData().getTeacher().getAvatar_url()).placeholder(R.mipmap.error_header_rect).crossFade().into(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TeacherDataActivity.class);
                    intent.putExtra("teacherId",data.getData().getTeacher().getId());
                    startActivity(intent);
                }
            });
        }

    }

    private int getSexColor(String gender) {
        if ("male".equals(gender)) {
            return 0xff00ccff;
        } else if ("female".equals(gender)) {
            return 0xffff9966;
        }
        return 0xffff9966;
    }

    private String getSex(String gender) {
        if ("male".equals(gender)) {
            return "♂";
        } else if ("female".equals(gender)) {
            return "♀";
        }
        return "";
    }


}
