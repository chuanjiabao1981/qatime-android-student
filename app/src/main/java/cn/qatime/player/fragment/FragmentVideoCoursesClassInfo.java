package cn.qatime.player.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2017/4/10 17:00
 * @Describe
 */

public class FragmentVideoCoursesClassInfo extends BaseFragment {
    private TextView subject;
    private TextView grade;
    private TextView target;
    private TextView suitable;
    private TextView timeLength;
    private WebView webView;
    private WebView notify;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_courses_class_info, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        subject = (TextView) findViewById(R.id.subject);
        grade = (TextView) findViewById(R.id.grade);
        target = (TextView) findViewById(R.id.target);
        suitable = (TextView) findViewById(R.id.suitable);
        timeLength = (TextView) findViewById(R.id.time_length);
        webView = (WebView) findViewById(R.id.describe);
        notify = (WebView) findViewById(R.id.notify);

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setBackgroundColor(0); // 设置背景色
        webView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBlockNetworkImage(false);
        settings.setDefaultFontSize(14);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(settings.MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }
        notify.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        notify.setBackgroundColor(0); // 设置背景色
        notify.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        notify.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        WebSettings notifySettings = notify.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBlockNetworkImage(false);
        settings.setDefaultFontSize(14);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(settings.MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }

        String header = "<style>* {color:#666666;margin:0;padding:0;}p {margin-bottom:3}</style>";//默认color段落间距
        String footer = "<p style='margin-top:20'><font style='font-size:15;color:#333333'>学习须知</font></p> \n" +
                "<p style='margin-top:5;'><font style='font-size:15;color:#333333'>购买</font></p> \n" +
                "<p><font>1.视频课是以学生自主观看视频进行独立学习的课程模式。<br> \n" +
                "2.视频课一经购买后永久有效，不限观看时间和观看次数；方便学生进行重复学习。<br> \n" +
                "3.视频课属一次性销售课程，不能重复购买，亦不支持（暂时）退款。<br> \n" +
                "4.视频课禁止下载或转录！</font></p> \n" +
                "<p style='margin-top:5;'><font style='font-size:15;color:#333333'>观看</font></p> \n" +
                "<p><font >1.视频课观看无需使用客户端，网页即可播放。<br> \n" +
                "2.上课途中如突遇屏幕卡顿，视频缓冲等情况，请检查网络状态后刷新页面。<br> \n" +
                "3.观看过程中请做好笔记，能够帮助学生快速掌握视频中的知识。<br> \n" +
                "4.学习结束后请尽量关闭视频，以免过多占用网络资源影响您浏览其他内容。</font></p> ";
        notify.loadDataWithBaseURL(null, header + footer, "text/html", "UTF-8", null);
    }
}
