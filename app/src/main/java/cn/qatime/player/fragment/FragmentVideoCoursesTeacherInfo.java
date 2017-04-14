package cn.qatime.player.fragment;

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

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.VideoCoursesDetailsBean;

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
    private TextView sex;
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
        sex = (TextView) findViewById(R.id.sex);

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

    public void setData(VideoCoursesDetailsBean data) {

    }
}
