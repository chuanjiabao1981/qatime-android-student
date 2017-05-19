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
import cn.qatime.player.view.FlowLayout;
import libraryextra.bean.RemedialClassDetailBean;
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
    RemedialClassDetailBean data;
    private SimpleDateFormat parse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat parse2 = new SimpleDateFormat("yyyy-MM-dd");
    private LinearLayout flowLayout;
    private FlowLayout flow;
    private TextView suitable;
    private TextView target;
    private WebView learningTips;


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
        learningTips = (WebView) view.findViewById(R.id.learning_tips);
        flowLayout = (LinearLayout) view.findViewById(R.id.flow_layout);
        flow = (FlowLayout) view.findViewById(R.id.flow);
//        String[] value = new String[]{"高考", "小學考試", "期中考試", "期末", "模擬考", "高高考", "小學考試", "期中考試", "期末", "模擬考"};

        initWebView(describe);
        initWebView(learningTips);

//        ((TextView) view.findViewById(R.id.notice)).setText(Html.fromHtml());
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

    public void setData(RemedialClassDetailBean bean) {
        if (bean != null && bean.getData() != null) {
            subject.setText((StringUtils.isNullOrBlanK(bean.getData().getSubject()) ? "" : bean.getData().getSubject()));
            try {
                classStartTime.setText((bean.getData().getLive_start_time() == null ? "" : parse2.format(parse1.parse(bean.getData().getLive_start_time()))));
                classEndTime.setText(parse2.format(parse1.parse(bean.getData().getLive_end_time())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            grade.setText((bean.getData().getGrade() == null ? "" : bean.getData().getGrade()));
            totalclass.setText(getString(R.string.lesson_count, bean.getData().getPreset_lesson_count()));
            if (!StringUtils.isNullOrBlanK(bean.getData().getTag_list())) {
                for (int va = 0; va < bean.getData().getTag_list().size(); va++) {
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
                    textView.setText(bean.getData().getTag_list().get(va));
                    flow.addView(textView);
                }
            } else {
                flowLayout.setVisibility(GONE);
            }
            if (!StringUtils.isNullOrBlanK(bean.getData().getObjective())) {
                target.setText(bean.getData().getObjective());
            }
            if (!StringUtils.isNullOrBlanK(bean.getData().getSuit_crowd())) {
                suitable.setText(bean.getData().getSuit_crowd());
            }
            String header = "<style>* {color:#666666;margin:0;padding:0;}.one {float: left;width:50%;height:auto;position: relative;text-align: center;}.two {width:100%;height:100%;top: 0;left:0;position: absolute;text-align: center;}</style>";//默认color段落间距
            String body = StringUtils.isNullOrBlanK(bean.getData().getDescription()) ? getString(R.string.no_desc) : bean.getData().getDescription();
            body = body.replace("\r\n", "<br>");
            //......
            String footer =
//                    "<p></p><p style='margin-top:20;margin-bottom:10'><font style='font-size:15;color:#333333'}>上课流程</font></p>" +
//                    "<div class='one'><img style='width:98%;height:auto;margin-right:2%' src='file:///android_res/mipmap/process_111.png' />" +
//                    "<div class='two'>" +
//                    "<p style='margin-top:5;margin-left:27%;margin-right:2%;color:#fff;font-size:14;'>购买课程</p>" +
//                    "<p style='margin-top:5;margin-left:27%;margin-right:2%;color:#fff;font-size:12;'>支持购买,放心退款</p>" +
//                    "</div>" +
//                    "</div>" +
//                    "<div class='one'><img style='width:98%;height:auto;margin-left:2%' src='file:///android_res/mipmap/process_22.png' />" +
//                    "<div class='two'>" +
//                    "<p style='margin-top:5;margin-left:29%;color:#fff;font-size:14;'>准时上课</p>" +
//                    "<p style='margin-top:5;margin-left:29%;color:#fff;font-size:12;'>提前预习,准时上课</p>" +
//                    "</div>" +
//                    "</div>" +
//                    "<div class='one' style='margin-top:10;margin-bottom:20;'><img style='width:98%;height:auto;margin-right:2%' src='file:///android_res/mipmap/process_33.png' />" +
//                    "<div class='two'>" +
//                    "<p style='margin-top:5;margin-left:27%;margin-right:2%;color:#fff;font-size:14;'>在线授课</p>" +
//                    "<p style='margin-top:5;margin-left:27%;margin-right:2%;color:#fff;font-size:12;'>多人交流,生动直播</p>" +
//                    "</div>" +
//                    "</div>" +
//                    "<div class='one' style='margin-top:10;margin-bottom:20;'><img style='width:98%;height:auto;margin-left:2%' src='file:///android_res/mipmap/process_44.png' />" +
//                    "<div class='two'>" +
//                    "<p style='margin-top:5;margin-left:29%;color:#fff;font-size:14;'>上课结束</p>" +
//                    "<p style='margin-top:5;margin-left:29%;color:#fff;font-size:12;'>视频回放,想看就看</p>" +
//                    "</div>" +
//                    "</div>" +
                    "<p style='margin-top:20'><font style='font-size:15;color:#333333'}>学习须知</font></p>" +
                            "<p style='margin-top:5;'><font style='font-size:15;color:#333333'}>上课前</font></p>" +
                            "<p><font>1.做好课程预习，预先了解本课所讲内容，更好的吸收课程精华；<br>" +
                            "2.准备好相关的学习工具（如：纸、笔等）并在上课前调试好电脑，使用手机请保持电量充足。<br>" +
                            "3.选择安静的学习环境，并将与学习无关的事物置于远处；选择安静的环境避免影响听课。 <br>" +
                            "4.三年级以下的同学请在家长帮助下学习。<br>" +
                            "5.遇到网页不能打开或者不能登陆等情况请及时联系客服。</font></p>" +
                            "<p style='margin-top:5;'><font style='font-size:15;color:#333333'>上课中</font></p>" +
                            "<p><font >1.时刻保持注意力集中，认真听讲才能更好的提升学习；<br>" +
                            "2.课程中遇到听不懂的问题及时通过聊天或互动申请向老师提问，老师收到后会给予解答；<br>" +
                            "3.积极响应老师的授课，完成老师布置的课上任务；<br>" +
                            "4.禁止在上课中闲聊或发送一切与本课无关的内容，如有发现，一律禁言；<br>" +
                            "5.上课途中如突遇屏幕卡顿，直播中断等特殊情况，请刷新后等待直播恢复；超过15分钟未恢复去请致电客服；<br>" +
                            "</font></p>" +
                            "<p style='margin-top:5;'><font style='font-size:15;color:#333333'>上课后</font></p>" +
                            "<p><font>1.直播结束后请大家仍可以在直播教室内进行聊天和讨论，老师也会适时解答；<br>" +
                            "2.请同学按时完成老师布置的作业任务。</font></p>";
            describe.loadDataWithBaseURL(null, header + body, "text/html", "UTF-8", null);
            learningTips.loadDataWithBaseURL(null, header + footer, "text/html", "UTF-8", null);
        }
    }
}
