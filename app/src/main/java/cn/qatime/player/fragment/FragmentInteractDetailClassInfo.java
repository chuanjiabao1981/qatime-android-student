package cn.qatime.player.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.InteractCourseDetailBean;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.StringUtils;

public class FragmentInteractDetailClassInfo extends BaseFragment {

    WebView webView;
    TextView subject;
    TextView grade;
    TextView totalTime;
    TextView classType;
    RemedialClassDetailBean data;
    private SimpleDateFormat parse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat parse2 = new SimpleDateFormat("yyyy-MM-dd");
    private TextView suitable;
    private TextView target;
    private TextView averageTime;
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
        averageTime = (TextView) view.findViewById(R.id.average_time);
        totalCount = (TextView) view.findViewById(R.id.total_count);
        suitable = (TextView) view.findViewById(R.id.suitable);
        target = (TextView) view.findViewById(R.id.target);
        webView = (WebView) view.findViewById(R.id.describe);

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

    }

    public void setData(InteractCourseDetailBean bean) {
        if (bean != null && bean.getData() != null) {
            subject.setText((StringUtils.isNullOrBlanK(bean.getData().getSubject()) ? "" : bean.getData().getSubject()));
            grade.setText((bean.getData().getGrade() == null ? "" : bean.getData().getGrade()));
            totalCount.setText(getString(R.string.lesson_count, bean.getData().getLessons_count()));
            //时长，平均时长
            totalTime.setText(bean.getData().getLessons_count() * 45 + "分钟");
//            averageTime.setText();
            if (!StringUtils.isNullOrBlanK(bean.getData().getObjective())) {
                target.setText(bean.getData().getObjective());
            }
            if (!StringUtils.isNullOrBlanK(bean.getData().getSuit_crowd())) {
                suitable.setText(bean.getData().getSuit_crowd());
            }
            String header = "<style>* {color:#666666;margin:0;padding:0;}p {margin-bottom:3}</style>";//默认color段落间距
            String body = StringUtils.isNullOrBlanK(bean.getData().getDescription()) ? getString(R.string.no_desc) : bean.getData().getDescription();
            body = body.replace("\r\n", "<br>");
            //......
            String footer = "<p></p><p style='margin-top:20'><font style='font-size:15;color:#333333'}>上课流程</font></p>" +
                    "<p style='margin-top:5;'><img style='width:100%;height:auto' src='file:///android_res/mipmap/image_default.png' /><p/>" +
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
            webView.loadDataWithBaseURL(null, header + body + footer, "text/html", "UTF-8", null);
        }
    }
}
