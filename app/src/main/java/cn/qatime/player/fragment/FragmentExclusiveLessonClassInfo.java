package cn.qatime.player.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.ExclusiveLessonDetailBean;
import libraryextra.utils.StringUtils;

public class FragmentExclusiveLessonClassInfo extends BaseFragment {

    WebView describe;
    TextView classStartTime;
    TextView classEndTime;
    TextView subject;
    TextView grade;
    TextView totalclass;
    private SimpleDateFormat parse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat parse2 = new SimpleDateFormat("yyyy-MM-dd");
    private TextView suitable;
    private TextView target;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exclusive_lesson_class_info, container, false);
        initview(view);

        return view;
    }


    private void initview(View view) {
        subject = (TextView) view.findViewById(R.id.subject);
        grade = (TextView) view.findViewById(R.id.grade);
        target = (TextView) view.findViewById(R.id.target);
        suitable = (TextView) view.findViewById(R.id.suitable);
        classStartTime = (TextView) view.findViewById(R.id.class_start_time);
        classEndTime = (TextView) view.findViewById(R.id.class_end_time);
        totalclass = (TextView) view.findViewById(R.id.total_class);
        describe = (WebView) view.findViewById(R.id.describe);

        initWebView(describe);
    }

    private void initWebView(WebView webView) {
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setBackgroundColor(0); // 设置背景色
        webView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        webView.setFocusable(false);//防止加载之后webview滚动
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setBlockNetworkImage(false);
        settings.setDefaultFontSize(14);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(settings.MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }
    }

    public void setData(ExclusiveLessonDetailBean bean) {
        if (bean != null && bean.getData() != null && bean.getData().getCustomized_group() != null) {
            subject.setText((StringUtils.isNullOrBlanK(bean.getData().getCustomized_group().getSubject()) ? "" : bean.getData().getCustomized_group().getSubject()));
            Date start = new Date(bean.getData().getCustomized_group().getStart_at() * 1000);
            classStartTime.setText(parse2.format(start));
            Date end = new Date(bean.getData().getCustomized_group().getEnd_at() * 1000);
            classEndTime.setText(parse2.format(end));
            grade.setText((bean.getData().getCustomized_group().getGrade() == null ? "" : bean.getData().getCustomized_group().getGrade()));
            totalclass.setText(getString(R.string.lesson_count, bean.getData().getCustomized_group().getEvents_count()));

            if (!StringUtils.isNullOrBlanK(bean.getData().getCustomized_group().getObjective())) {
                target.setText(bean.getData().getCustomized_group().getObjective());
            }
            if (!StringUtils.isNullOrBlanK(bean.getData().getCustomized_group().getSuit_crowd())) {
                suitable.setText(bean.getData().getCustomized_group().getSuit_crowd());
            }
            String header = "<style>* {color:#666666;margin:0;padding:0;}.one {float: left;width:50%;height:auto;position: relative;text-align: center;}.two {width:100%;height:100%;top: 0;left:0;position: absolute;text-align: center;}</style>";//默认color段落间距
            String body = StringUtils.isNullOrBlanK(bean.getData().getCustomized_group().getDescription()) ? getString(R.string.no_desc) : bean.getData().getCustomized_group().getDescription();
            body = body.replace("\r\n", "<br>");
            describe.loadDataWithBaseURL(null, header + body, "text/html", "UTF-8", null);
        }
    }
}
