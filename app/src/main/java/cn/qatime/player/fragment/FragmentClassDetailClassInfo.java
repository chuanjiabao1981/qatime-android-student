package cn.qatime.player.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.LiveLessonDetailBean;
import cn.qatime.player.view.FlowLayout;
import libraryextra.utils.StringUtils;

import static android.view.ViewGroup.GONE;
import static android.view.ViewGroup.LayoutParams;
import static android.view.ViewGroup.MarginLayoutParams;

public class FragmentClassDetailClassInfo extends BaseFragment {

    WebView describe;
    TextView classStartTime;
    TextView classEndTime;
    TextView subject;
    TextView grade;
    TextView totalclass;
    private SimpleDateFormat parse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat parse2 = new SimpleDateFormat("yyyy-MM-dd");
    private LinearLayout flowLayout;
    private FlowLayout flow;
    private TextView suitable;
    private TextView target;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_detail_class_info, container, false);
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
        flowLayout = (LinearLayout) view.findViewById(R.id.flow_layout);
        flow = (FlowLayout) view.findViewById(R.id.flow);

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

    public void setData(LiveLessonDetailBean bean) {
        if (bean != null && bean.getData() != null && bean.getData().getCourse() != null) {
            subject.setText((StringUtils.isNullOrBlanK(bean.getData().getCourse().getSubject()) ? "" : bean.getData().getCourse().getSubject()));
            try {
                classStartTime.setText((bean.getData().getCourse().getLive_start_time() == null ? "" : parse2.format(parse1.parse(bean.getData().getCourse().getLive_start_time()))));
                classEndTime.setText(parse2.format(parse1.parse(bean.getData().getCourse().getLive_end_time())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            grade.setText((bean.getData().getCourse().getGrade() == null ? "" : bean.getData().getCourse().getGrade()));
            totalclass.setText(getString(R.string.lesson_count, bean.getData().getCourse().getLessons_count()));
            if (!StringUtils.isNullOrBlanK(bean.getData().getCourse().getTag_list())) {
                if (flow.getChildCount() > 0) {
                    flow.removeAllViews();
                }
                for (int va = 0; va < bean.getData().getCourse().getTag_list().size(); va++) {
                    TextView textView = new TextView(getActivity());
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(0xff999999);
                    textView.setTextSize(13);
                    textView.setBackgroundResource(R.drawable.text_background_flowlayout);
                    MarginLayoutParams params = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    params.leftMargin = 2;
                    params.rightMargin = 2;
                    params.topMargin = 2;
                    params.bottomMargin = 2;
                    textView.setLayoutParams(params);
                    textView.setText(bean.getData().getCourse().getTag_list().get(va));
                    flow.addView(textView);
                }
            } else {
                flowLayout.setVisibility(GONE);
            }
            if (!StringUtils.isNullOrBlanK(bean.getData().getCourse().getObjective())) {
                target.setText(bean.getData().getCourse().getObjective());
            }
            if (!StringUtils.isNullOrBlanK(bean.getData().getCourse().getSuit_crowd())) {
                suitable.setText(bean.getData().getCourse().getSuit_crowd());
            }
            String header = "<style>* {color:#666666;margin:0;padding:0;}.one {float: left;width:50%;height:auto;position: relative;text-align: center;}.two {width:100%;height:100%;top: 0;left:0;position: absolute;text-align: center;}</style>";//默认color段落间距
            String body = StringUtils.isNullOrBlanK(bean.getData().getCourse().getDescription()) ? getString(R.string.no_desc) : bean.getData().getCourse().getDescription();
            body = body.replace("\r\n", "<br>");
            //......

            describe.loadDataWithBaseURL(null, header + body, "text/html", "UTF-8", null);
        }
    }
}
