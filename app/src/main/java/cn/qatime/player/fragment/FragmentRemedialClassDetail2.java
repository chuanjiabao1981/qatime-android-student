package cn.qatime.player.fragment;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.RemedialClassDetailBean;
import cn.qatime.player.bean.SchoolBean;
import cn.qatime.player.transformation.GlideCircleTransform;
import cn.qatime.player.utils.FileUtil;
import cn.qatime.player.utils.JsonUtils;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.StringUtils;

public class FragmentRemedialClassDetail2 extends BaseFragment {
    private TextView name;
    private ImageView image;
//    private TextView degree;
    private TextView teachingyears;
    private TextView subject;

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
//        degree = (TextView) view.findViewById(R.id.degree);
//        graderange = (TextView) view.findViewById(R.id.grade_range);
        subject = (TextView) view.findViewById(R.id.subject);
        teachingyears = (TextView) view.findViewById(R.id.teaching_years);
        school = (TextView) view.findViewById(R.id.school);
        describe = (TextView) view.findViewById(R.id.describe);
    }

    public void setData(RemedialClassDetailBean data) {
        if (data.getData() != null) {

            name.setText("老师姓名：" + data.getData().getTeacher().getName());
            subject.setText("所授科目：" + data.getData().getTeacher().getSubject());
            if (!StringUtils.isNullOrBlanK(data.getData().getTeacher().getTeaching_years())) {
                if (data.getData().getTeacher().getTeaching_years().equals("within_three_years")) {
                    teachingyears.setText("执教年龄：3年");
                } else if (data.getData().getTeacher().getTeaching_years().equals("within_ten_years")) {
                    teachingyears.setText("执教年龄：10年");
                } else if (data.getData().getTeacher().getTeaching_years().equals("within_twenty_years")) {
                    teachingyears.setText("执教年龄：20年");
                } else {
                    teachingyears.setText("执教年龄：20年以上");
                }
            }
//            graderange.setText("年级范围：" + data.getData().getTeacher().getGrade_range());
            describe.setText(data.getData().getTeacher().getDesc());

            SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getActivity().getCacheDir() + "/school.txt").toString(), SchoolBean.class);

            if (schoolBean != null && schoolBean.getData() != null) {
                for (int i = 0; i < schoolBean.getData().size(); i++) {
                    if (data.getData().getTeacher().getSchool() == schoolBean.getData().get(i).getId()) {
                        school.setText("所在学校："+schoolBean.getData().get(i).getName());
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
