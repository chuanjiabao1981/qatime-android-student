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
import cn.qatime.player.bean.ExclusiveLessonDetailBean;
import libraryextra.bean.SchoolBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;

public class FragmentExclusiveLessonTeacherInfo extends BaseFragment {
    private TextView name;
    private ImageView image;
    private TextView teachingyears;
    private TextView school;
    private TextView describe;
    private ImageView sex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exclusive_lesson_teacher_info, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initview();
    }

    private void initview() {
        name = (TextView)findViewById(R.id.name);
        image = (ImageView)findViewById(R.id.image);
        teachingyears = (TextView)findViewById(R.id.teaching_years);
        school = (TextView)findViewById(R.id.school);
        sex = (ImageView)findViewById(R.id.sex);

        describe = (TextView)findViewById(R.id.describe);

        describe.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });


    }

    public void setData(final ExclusiveLessonDetailBean data) {
        if (data.getData() != null && data.getData().getCustomized_group() != null && data.getData().getCustomized_group().getTeacher() != null) {
            sex.setImageResource("male".equals(data.getData().getCustomized_group().getTeacher().getGender()) ? R.mipmap.male : R.mipmap.female);
            name.setText(data.getData().getCustomized_group().getTeacher().getName());
            if (!StringUtils.isNullOrBlanK(data.getData().getCustomized_group().getTeacher().getTeaching_years())) {
                if (data.getData().getCustomized_group().getTeacher().getTeaching_years().equals("within_three_years")) {
                    teachingyears.setText(getResourceString(R.string.within_three_years));
                } else if (data.getData().getCustomized_group().getTeacher().getTeaching_years().equals("within_ten_years")) {
                    teachingyears.setText(getResourceString(R.string.within_ten_years));
                } else if (data.getData().getCustomized_group().getTeacher().getTeaching_years().equals("within_twenty_years")) {
                    teachingyears.setText(getResourceString(R.string.within_twenty_years));
                } else {
                    teachingyears.setText(getResourceString(R.string.more_than_ten_years));
                }
            }

            SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getActivity().getFilesDir() + "/school.txt"), SchoolBean.class);
            if (schoolBean != null && schoolBean.getData() != null) {
                for (int i = 0; i < schoolBean.getData().size(); i++) {
                    if (data.getData().getCustomized_group().getTeacher().getSchool() == schoolBean.getData().get(i).getId()) {
                        school.setText(schoolBean.getData().get(i).getName());
                        break;
                    }
                }
            } else {
                school.setText(R.string.not_available);
            }

            Glide.with(this).load(data.getData().getCustomized_group().getTeacher().getAvatar_url()).bitmapTransform(new GlideCircleTransform(getActivity())).placeholder(R.mipmap.error_header).crossFade().into(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TeacherDataActivity.class);
                    intent.putExtra("teacherId", data.getData().getCustomized_group().getTeacher().getId());
                    startActivity(intent);
                }
            });
            describe.setText(StringUtils.isNullOrBlanK(data.getData().getCustomized_group().getTeacher().getDesc()) ? getString(R.string.no_desc) : data.getData().getCustomized_group().getTeacher().getDesc());
            findViewById(R.id.name_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getActivity(), TeacherDataActivity.class);
                    intent.putExtra("teacherId", data.getData().getCustomized_group().getTeacher().getId());
                    startActivity(intent);
                }
            });
        }
    }
}
