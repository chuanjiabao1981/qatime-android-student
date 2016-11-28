package cn.qatime.player.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.StringUtils;

public class FragmentPlayerLiveClassDetails extends BaseFragment {
    TextView describe;
    TextView classStartTime;
    TextView classEndTime;
    TextView subject;
    TextView grade;
    TextView totalclass;
    TextView classType;
    TextView progress;
    RemedialClassDetailBean data;
    private SimpleDateFormat parse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat parse2 = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private Handler hd = new Handler();
    private RemedialClassDetailBean.Data bean;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (getActivity() != null && getActivity().getResources() != null) {
                subject.setText((bean.getSubject() == null ? "" : bean.getSubject()));
                try {
                    classStartTime.setText((bean.getLive_start_time() == null ? "" : parse2.format(parse1.parse(bean.getLive_start_time()))));
                    classEndTime.setText(parse2.format(parse1.parse(bean.getLive_end_time())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                grade.setText((bean.getGrade() == null ? "" : bean.getGrade()));
                totalclass.setText("共" + bean.getPreset_lesson_count() + "课");
                describe.setText(StringUtils.isNullOrBlanK(bean.getDescription())?"暂无简介":bean.getDescription());
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_palyer_live_class_details, null);
        initview(view);
        return view;
    }

    private void initview(View view) {
        subject = (TextView) view.findViewById(R.id.subject);
        grade = (TextView) view.findViewById(R.id.grade);
        classStartTime = (TextView) view.findViewById(R.id.class_start_time);
        classEndTime = (TextView) view.findViewById(R.id.class_end_time);
        totalclass = (TextView) view.findViewById(R.id.total_class);
        describe = (TextView) view.findViewById(R.id.describe);
        classType = (TextView) view.findViewById(R.id.class_type);
    }

    public void setData(RemedialClassDetailBean.Data bean) {
        if (bean != null) {
            this.bean = bean;
            hd.postDelayed(runnable, 1000);
        }
    }
}
