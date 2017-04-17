package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.qatime.player.R;
import cn.qatime.player.activity.TeacherDataActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.VideoCoursesDetailsBean;
import libraryextra.bean.SchoolBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2017/4/10 17:05
 * @Describe
 */

public class FragmentVideoCoursesTeacherInfo extends BaseFragment {
    private TextView name;
    private ImageView image;
    private TextView teachingyears;
    private TextView school;
    private ImageView sex;
    private WebView describe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_courses_teacher_info, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        name = (TextView) findViewById(R.id.name);
        image = (ImageView) findViewById(R.id.image);
        teachingyears = (TextView) findViewById(R.id.teaching_years);
        school = (TextView) findViewById(R.id.school);
        sex = (ImageView) findViewById(R.id.sex);

        describe = (WebView) findViewById(R.id.describe);

        describe.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        describe.setBackgroundColor(0); // 设置背景色
        describe.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        describe.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        WebSettings settings = describe.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBlockNetworkImage(false);
        settings.setDefaultFontSize(14);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(settings.MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }
    }

    public void setData(final VideoCoursesDetailsBean data) {
//        sex.setText(getSex(data.getData().getTeacher().getGender()));
//        sex.setTextColor(getSexColor(data.getData().getTeacher().getGender()));
        sex.setImageResource("male".equals(data.getData().getTeacher().getGender()) ? R.mipmap.male : R.mipmap.female);
        name.setText(data.getData().getTeacher().getName());
        if (!StringUtils.isNullOrBlanK(data.getData().getTeacher().getTeaching_years())) {
            if (data.getData().getTeacher().getTeaching_years().equals("within_three_years")) {
                teachingyears.setText(getResourceString(R.string.within_three_years));
            } else if (data.getData().getTeacher().getTeaching_years().equals("within_ten_years")) {
                teachingyears.setText(getResourceString(R.string.within_ten_years));
            } else if (data.getData().getTeacher().getTeaching_years().equals("within_twenty_years")) {
                teachingyears.setText(getResourceString(R.string.within_twenty_years));
            } else {
                teachingyears.setText(getResourceString(R.string.more_than_ten_years));
            }
        }

        SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getActivity().getCacheDir() + "/school.txt"), SchoolBean.class);
        if (schoolBean != null && schoolBean.getData() != null) {
            for (int i = 0; i < schoolBean.getData().size(); i++) {
                if (data.getData().getTeacher().getSchool() == schoolBean.getData().get(i).getId()) {
                    school.setText(schoolBean.getData().get(i).getName());
                    break;
                }
            }
        } else {
            school.setText(R.string.not_available);
        }

        Glide.with(this).load(data.getData().getTeacher().getAvatar_url()).bitmapTransform(new GlideCircleTransform(getActivity())).placeholder(R.mipmap.error_header).crossFade().into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TeacherDataActivity.class);
                intent.putExtra("teacherId", data.getData().getTeacher().getId());
                startActivity(intent);
            }
        });
        String body = StringUtils.isNullOrBlanK(data.getData().getTeacher().getDesc()) ? getString(R.string.no_desc) : data.getData().getTeacher().getDesc();
        body = body.replace("\r\n", "<br>");
        String css = "<style>* {color:#666666;margin:0;padding:0}</style>";//默认color（android标签下以及所有未设置颜色的标签）
        describe.loadDataWithBaseURL(null, css + body, "text/html", "UTF-8", null);
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
