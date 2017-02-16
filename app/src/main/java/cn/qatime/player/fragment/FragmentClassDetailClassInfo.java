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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.StringUtils;

public class FragmentClassDetailClassInfo extends BaseFragment {

    WebView describe;
    TextView classStartTime;
    TextView classEndTime;
    TextView subject;
    TextView grade;
    TextView totalclass;
    TextView classType;
    RemedialClassDetailBean data;
    private SimpleDateFormat parse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat parse2 = new SimpleDateFormat("yyyy-MM-dd");


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
        classStartTime = (TextView) view.findViewById(R.id.class_start_time);
        classEndTime = (TextView) view.findViewById(R.id.class_end_time);
        totalclass = (TextView) view.findViewById(R.id.total_class);
        classType = (TextView) view.findViewById(R.id.class_type);
        describe = (WebView) view.findViewById(R.id.describe);

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
        settings.setDefaultTextEncodingName("UTF-8") ;
        settings.setBlockNetworkImage(false);
        settings.setDefaultFontSize(13);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
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
            String body =StringUtils.isNullOrBlanK(bean.getData().getDescription()) ? getString(R.string.no_desc) : bean.getData().getDescription();
            body = body.replace("\r\n", "<br />");
            body="<android>"+body+"</android>";
            String css = "<style>android {color:#999999;}</style>";//默认color
            describe.loadDataWithBaseURL(null,css+body,"text/html","UTF-8",null);
        }
    }
}
