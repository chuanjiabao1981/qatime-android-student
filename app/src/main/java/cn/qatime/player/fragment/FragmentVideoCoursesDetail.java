package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.qatime.player.R;
import cn.qatime.player.activity.TeacherDataActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.VideoCoursesDetailsBean;
import libraryextra.bean.SchoolBean;
import libraryextra.utils.DateUtils;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;

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
    private ImageView sex;
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
        sex = (ImageView) view.findViewById(R.id.sex);
        school = (TextView) view.findViewById(R.id.school);
        teachingYears = (TextView) view.findViewById(R.id.teaching_years);
        teacherDescribe = (WebView) view.findViewById(R.id.teacher_describe);
    }


    public void setData(final VideoCoursesDetailsBean data) {
        className.setText(data.getData().getVideo_course().getName());
        subject.setText((data.getData().getVideo_course().getSubject() == null ? "" : data.getData().getVideo_course().getSubject()));
        grade.setText((data.getData().getVideo_course().getGrade() == null ? "" : data.getData().getVideo_course().getGrade()));
        totalClass.setText(getString(R.string.lesson_count, data.getData().getVideo_course().getVideo_lessons_count()));
        if (!StringUtils.isNullOrBlanK(data.getData().getVideo_course().getObjective())) {
            target.setText(data.getData().getVideo_course().getObjective());
        }
        if (!StringUtils.isNullOrBlanK(data.getData().getVideo_course().getSuit_crowd())) {
            suitable.setText(data.getData().getVideo_course().getSuit_crowd());
        }
        totalTime.setText("总时长" + DateUtils.stringForTime(data.getData().getVideo_course().getTotal_duration()));
        sex.setImageResource("male".equals(data.getData().getVideo_course().getTeacher().getGender()) ? R.mipmap.male : R.mipmap.female);
        name.setText(data.getData().getVideo_course().getTeacher().getName());
        if (!StringUtils.isNullOrBlanK(data.getData().getVideo_course().getTeacher().getTeaching_years())) {
            if (data.getData().getVideo_course().getTeacher().getTeaching_years().equals("within_three_years")) {
                teachingYears.setText(getResourceString(R.string.within_three_years));
            } else if (data.getData().getVideo_course().getTeacher().getTeaching_years().equals("within_ten_years")) {
                teachingYears.setText(getResourceString(R.string.within_ten_years));
            } else if (data.getData().getVideo_course().getTeacher().getTeaching_years().equals("within_twenty_years")) {
                teachingYears.setText(getResourceString(R.string.within_twenty_years));
            } else {
                teachingYears.setText(getResourceString(R.string.more_than_ten_years));
            }
        }
        String body = StringUtils.isNullOrBlanK(data.getData().getVideo_course().getTeacher().getDesc()) ? getString(R.string.no_desc) : data.getData().getVideo_course().getTeacher().getDesc();
        body = body.replace("\r\n", "<br>");
        String css = "<style>* {color:#666666;margin:0;padding:0;}</style>";//默认color
        teacherDescribe.loadDataWithBaseURL(null, css + body, "text/html", "UTF-8", null);
        SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getActivity().getFilesDir() + "/school.txt"), SchoolBean.class);
        if (schoolBean != null && schoolBean.getData() != null) {
            for (int i = 0; i < schoolBean.getData().size(); i++) {
                if (data.getData().getVideo_course().getTeacher().getSchool() == schoolBean.getData().get(i).getId()) {
                    school.setText(schoolBean.getData().get(i).getName());
                    break;
                }
            }
        } else {
            school.setText(getString(R.string.not_available));
        }

        Glide.with(getActivity()).load(data.getData().getVideo_course().getTeacher().getAvatar_url()).placeholder(R.mipmap.error_header).crossFade().into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TeacherDataActivity.class);
                intent.putExtra("teacherId", data.getData().getVideo_course().getTeacher().getId());
                startActivity(intent);
            }
        });
    }
}
