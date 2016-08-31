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

public class FragmentNEVideoPlayer32 extends BaseFragment {
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
        View view = View.inflate(getActivity(), R.layout.fragment_nevideo_player32, null);
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

    public void setData(RemedialClassDetailBean.Data data) {
        if (data != null) {
            name.setText(getResources().getString(R.string.teacher_name) + data.getTeacher().getName());
            subject.setText(getResources().getString(R.string.teacher_subject) + data.getTeacher().getSubject());
            if (!StringUtils.isNullOrBlanK(data.getTeacher().getTeaching_years())) {
                if (data.getTeacher().getTeaching_years().equals("within_three_years")) {
                    teachingyears.setText(getResources().getString(R.string.teacher_years) + getResources().getString(R.string.within_three_years));
                } else if (data.getTeacher().getTeaching_years().equals("within_ten_years")) {
                    teachingyears.setText(getResources().getString(R.string.teacher_years) + getResources().getString(R.string.within_ten_years));
                } else if (data.getTeacher().getTeaching_years().equals("within_twenty_years")) {
                    teachingyears.setText(getResources().getString(R.string.teacher_years) + getResources().getString(R.string.within_twenty_years));
                } else {
                    teachingyears.setText(getResources().getString(R.string.teacher_years) + getResources().getString(R.string.more_than_ten_years));
                }
            }
            gradetype.setText(getResources().getString(R.string.grade_type) + "高中");
            describe.setText(data.getTeacher().getDesc());

            SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getActivity().getCacheDir() + "/school.txt").toString(), SchoolBean.class);

            if (schoolBean != null && schoolBean.getData() != null) {
                for (int i = 0; i < schoolBean.getData().size(); i++) {
                    if (data.getTeacher().getSchool() == schoolBean.getData().get(i).getId()) {
                        school.setText(getResources().getString(R.string.teacher_school) + schoolBean.getData().get(i).getName());
                        break;
                    }
                }
            } else {
                school.setText("");
            }

            Glide.with(this).load(data.getTeacher().getAvatar_url()).placeholder(R.mipmap.ic_launcher).crossFade().into(image);

        }


    }
}
