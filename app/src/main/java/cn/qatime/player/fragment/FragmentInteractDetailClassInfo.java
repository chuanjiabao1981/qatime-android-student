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

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.InteractCourseDetailBean;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.StringUtils;

public class FragmentInteractDetailClassInfo extends BaseFragment {

    WebView describe;
    TextView subject;
    TextView grade;
    TextView totalTime;
    TextView classType;
    RemedialClassDetailBean data;
    private TextView suitable;
    private TextView target;
    private TextView totalCount;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interact_detail_class_info, container, false);
        initview(view);

        return view;
    }


    private void initview(View view) {
        subject = (TextView) view.findViewById(R.id.subject);
        grade = (TextView) view.findViewById(R.id.grade);
        totalTime = (TextView) view.findViewById(R.id.total_time);
        classType = (TextView) view.findViewById(R.id.class_type);
        totalCount = (TextView) view.findViewById(R.id.total_count);
        suitable = (TextView) view.findViewById(R.id.suitable);
        target = (TextView) view.findViewById(R.id.target);
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


    public void setData(InteractCourseDetailBean bean) {
        if (bean != null && bean.getData() != null) {
            subject.setText((StringUtils.isNullOrBlanK(bean.getData().getInteractive_course().getSubject()) ? "" : bean.getData().getInteractive_course().getSubject()));
            grade.setText((bean.getData().getInteractive_course().getGrade() == null ? "" : bean.getData().getInteractive_course().getGrade()));
            totalCount.setText(getString(R.string.lesson_count, bean.getData().getInteractive_course().getLessons_count()));
            //时长，平均时长
            totalTime.setText(bean.getData().getInteractive_course().getLessons_count() * 45 + "分钟");
//            averageTime.setText();
            if (!StringUtils.isNullOrBlanK(bean.getData().getInteractive_course().getObjective())) {
                target.setText(bean.getData().getInteractive_course().getObjective());
            }
            if (!StringUtils.isNullOrBlanK(bean.getData().getInteractive_course().getSuit_crowd())) {
                suitable.setText(bean.getData().getInteractive_course().getSuit_crowd());
            }
            String header = "<style>* {color:#666666;margin:0;padding:0;}.one {float: left;width:50%;height:auto;position: relative;text-align: center;}.two {width:100%;height:100%;top: 0;left:0;position: absolute;text-align: center;}</style>";//默认color段落间距
            String body = StringUtils.isNullOrBlanK(bean.getData().getInteractive_course().getDescription()) ? getString(R.string.no_desc) : bean.getData().getInteractive_course().getDescription();
            body = body.replace("\r\n", "<br>");
            //......
            describe.loadDataWithBaseURL(null, header + body, "text/html", "UTF-8", null);
        }
    }
}
