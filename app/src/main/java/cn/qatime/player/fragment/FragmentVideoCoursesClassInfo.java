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
import cn.qatime.player.bean.VideoCoursesDetailsBean;
import libraryextra.utils.DateUtils;
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
    private TextView totalClass;

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
        totalClass = (TextView) findViewById(R.id.total_class);
        target = (TextView) findViewById(R.id.target);
        suitable = (TextView) findViewById(R.id.suitable);
        timeLength = (TextView) findViewById(R.id.time_length);
        webView = (WebView) findViewById(R.id.describe);

        initWebView(webView);
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

    public void setData(VideoCoursesDetailsBean bean) {
        subject.setText((StringUtils.isNullOrBlanK(bean.getData().getVideo_course().getSubject()) ? "" : bean.getData().getVideo_course().getSubject()));
        grade.setText((bean.getData().getVideo_course().getGrade() == null ? "" : bean.getData().getVideo_course().getGrade()));
        totalClass.setText(getString(R.string.lesson_count, bean.getData().getVideo_course().getVideo_lessons_count()));
        timeLength.setText("总时长"+ DateUtils.stringForTime_s(bean.getData().getVideo_course().getTotal_duration()));
        if (!StringUtils.isNullOrBlanK(bean.getData().getVideo_course().getObjective())) {
            target.setText(bean.getData().getVideo_course().getObjective());
        }
        if (!StringUtils.isNullOrBlanK(bean.getData().getVideo_course().getSuit_crowd())) {
            suitable.setText(bean.getData().getVideo_course().getSuit_crowd());
        }
        String header = "<style>* {color:#666666;margin:0;padding:0;}p {margin-bottom:3}</style>";//默认color段落间距
        String body = StringUtils.isNullOrBlanK(bean.getData().getVideo_course().getDescription()) ? getString(R.string.no_desc) : bean.getData().getVideo_course().getDescription();
        body = body.replace("\r\n", "<br>");
        webView.loadDataWithBaseURL(null, header + body, "text/html", "UTF-8", null);
    }
}
