package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.TeacherDataActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.ExclusiveLessonPlayBean;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.SchoolBean;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.view.ListViewForScrollView;

public class FragmentExclusiveLiveDetails extends BaseFragment {
    private TextView subject;
    private TextView totalClass;
    private TextView grade;
    private TextView classStartTime;
    private TextView classEndTime;
    //    private WebView courseDescribe;
    private TextView name;
    private ImageView sex;
    private TextView teachingYears;
    private TextView school;
    private WebView teacherDescribe;
    private ImageView image;

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private Handler hd = new Handler();
    private View viewEmptyGone;
    private TextView className;
    private TextView target;
    private TextView suitable;
    private ExclusiveLessonPlayBean.DataBean data;
    private ListViewForScrollView scheduleListView;
    private ListViewForScrollView offlineListView;
    private CommonAdapter<ExclusiveLessonPlayBean.DataBean.ScheduledLessonsBean> scheduleAdapter;
    private CommonAdapter<ExclusiveLessonPlayBean.DataBean.OfflineLessonsBean> offlineAdapter;
    private List<ExclusiveLessonPlayBean.DataBean.ScheduledLessonsBean> scheduleList = new ArrayList<>();
    private List<ExclusiveLessonPlayBean.DataBean.OfflineLessonsBean> offlineList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_exclusive_live_details, null);
        className = (TextView) view.findViewById(R.id.class_name);
        subject = (TextView) view.findViewById(R.id.subject);
        totalClass = (TextView) view.findViewById(R.id.total_class);
        grade = (TextView) view.findViewById(R.id.grade);
        classStartTime = (TextView) view.findViewById(R.id.class_start_time);
        classEndTime = (TextView) view.findViewById(R.id.class_end_time);
        name = (TextView) view.findViewById(R.id.name);
        sex = (ImageView) view.findViewById(R.id.sex);
        teachingYears = (TextView) view.findViewById(R.id.teaching_years);
        school = (TextView) view.findViewById(R.id.school);
        image = (ImageView) view.findViewById(R.id.image);
        scheduleListView = (ListViewForScrollView) view.findViewById(R.id.schedule_list);
        offlineListView = (ListViewForScrollView) view.findViewById(R.id.offline_list);
        viewEmptyGone = view.findViewById(R.id.view_empty_gone);

        target = (TextView) view.findViewById(R.id.target);
        suitable = (TextView) view.findViewById(R.id.suitable);

        teacherDescribe = (WebView) view.findViewById(R.id.teacher_describe);

        teacherDescribe.setBackgroundColor(0); // 设置背景色
        teacherDescribe.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        teacherDescribe.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        WebSettings settingsT = teacherDescribe.getSettings();
        settingsT.setDefaultTextEncodingName("UTF-8");
        settingsT.setBlockNetworkImage(false);
        settingsT.setDefaultFontSize(14);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settingsT.setMixedContentMode(settingsT.MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }

        initList();
        return view;
    }

    private void initList() {
        scheduleListView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        offlineListView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        scheduleAdapter = new CommonAdapter<ExclusiveLessonPlayBean.DataBean.ScheduledLessonsBean>(getActivity(), scheduleList, R.layout.item_fragment_exclusive_class_list_schedule) {

            @Override
            public void convert(ViewHolder holder, ExclusiveLessonPlayBean.DataBean.ScheduledLessonsBean item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.live_time, item.getStart_time());
                holder.setText(R.id.status, getStatus(item.getStatus()));
                try {
                    holder.setText(R.id.class_date, format.format(parse.parse(item.getClass_date())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (isFinished(item.getStatus())) {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff999999);
                } else {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff00a0e9);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff666666);
                }

            }
        };
        scheduleListView.setAdapter(scheduleAdapter);
        offlineAdapter = new CommonAdapter<ExclusiveLessonPlayBean.DataBean.OfflineLessonsBean>(getActivity(), offlineList, R.layout.item_fragment_exclusive_class_list_offline) {

            @Override
            public void convert(ViewHolder holder, ExclusiveLessonPlayBean.DataBean.OfflineLessonsBean item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.live_time, item.getStart_time());
                holder.setText(R.id.address, "上课地点:" + item.getClass_address());
                holder.setText(R.id.status, getStatus(item.getStatus()));
                try {
                    holder.setText(R.id.class_date, format.format(parse.parse(item.getClass_date())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (isFinished(item.getStatus())) {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.address)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff999999);
                } else {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff00a0e9);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.address)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff666666);
                }
            }
        };
        offlineListView.setAdapter(offlineAdapter);
    }

    private boolean isFinished(String item) {
        return item.equals("closed") || item.equals("finished") || item.equals("billing") || item.equals("completed");
    }


    public void setData(ExclusiveLessonPlayBean data) {
        this.data = data.getData();
        setDataClassDetails();
        setDataTeacherDetails();
        setDataListDetails();
    }

    private String getStatus(String status) {
        if (status.equals("missed")) {
            return getResourceString(R.string.class_missed);
        } else if (status.equals("init")) {//未开始
            return getResourceString(R.string.class_init);
        } else if (status.equals("ready")) {//待开课
            return getResourceString(R.string.class_ready);
        } else if (status.equals("teaching")) {//直播中
            return getResourceString(R.string.class_teaching);
        } else if (status.equals("closed")) {//已直播
            return getResourceString(R.string.class_closed);
        } else if (status.equals("paused")) {
            return getResourceString(R.string.class_teaching);
        } else {//closed finished billing completed
            return getResourceString(R.string.class_over);//已结束
        }
    }

    private void setDataListDetails() {
        scheduleList.clear();
        offlineList.clear();
        scheduleList.addAll(data.getScheduled_lessons());
        offlineList.addAll(data.getOffline_lessons());
        if (scheduleAdapter != null) {
            scheduleAdapter.notifyDataSetChanged();
        }
        if (offlineAdapter != null) {
            offlineAdapter.notifyDataSetChanged();
        }
        if (scheduleList.size() == 0) {
            viewEmptyGone.setVisibility(View.GONE);
        } else {
            viewEmptyGone.setVisibility(View.VISIBLE);
        }
    }

    private void setDataTeacherDetails() {
        hd.postDelayed(runnableTeacher, 1000);
    }

    private void setDataClassDetails() {
        hd.postDelayed(runnableClass, 1000);
    }

    private Runnable runnableClass = new Runnable() {
        @Override
        public void run() {
            if (getActivity() != null && getActivity().getResources() != null) {
                className.setText(data.getName());
                subject.setText((data.getSubject() == null ? "" : data.getSubject()));
                classStartTime.setText((data.getStart_at() == 0 ? "" : format.format(new Date(data.getStart_at() * 1000))));
                classEndTime.setText(data.getEnd_at() == 0 ? "" : format.format(new Date(data.getEnd_at() * 1000)));
                grade.setText((data.getGrade() == null ? "" : data.getGrade()));
                totalClass.setText(getString(R.string.lesson_count, data.getView_tickets_count()));
                if (!StringUtils.isNullOrBlanK(data.getObjective())) {
                    target.setText(data.getObjective());
                }
                if (!StringUtils.isNullOrBlanK(data.getSuit_crowd())) {
                    suitable.setText(data.getSuit_crowd());
                }
            }
        }
    };
    private Runnable runnableTeacher = new Runnable() {
        @Override
        public void run() {
            if (getActivity() != null && getActivity().getResources() != null) {
                sex.setImageResource("male".equals(data.getTeacher().getGender()) ? R.mipmap.male : R.mipmap.female);
                name.setText(data.getTeacher().getName());
                if (!StringUtils.isNullOrBlanK(data.getTeacher().getTeaching_years())) {
                    if (data.getTeacher().getTeaching_years().equals("within_three_years")) {
                        teachingYears.setText(getResourceString(R.string.within_three_years));
                    } else if (data.getTeacher().getTeaching_years().equals("within_ten_years")) {
                        teachingYears.setText(getResourceString(R.string.within_ten_years));
                    } else if (data.getTeacher().getTeaching_years().equals("within_twenty_years")) {
                        teachingYears.setText(getResourceString(R.string.within_twenty_years));
                    } else {
                        teachingYears.setText(getResourceString(R.string.more_than_ten_years));
                    }
                }
                String body = StringUtils.isNullOrBlanK(data.getTeacher().getDesc()) ? getString(R.string.no_desc) : data.getTeacher().getDesc();
                body = body.replace("\r\n", "<br>");
                String css = "<style>* {color:#666666;margin:0;padding:0;}</style>";//默认color
                teacherDescribe.loadDataWithBaseURL(null, css + body, "text/html", "UTF-8", null);
                SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getActivity().getFilesDir() + "/school.txt").toString(), SchoolBean.class);
                if (schoolBean != null && schoolBean.getData() != null) {
                    for (int i = 0; i < schoolBean.getData().size(); i++) {
                        if (data.getTeacher().getSchool() == schoolBean.getData().get(i).getId()) {
                            school.setText(schoolBean.getData().get(i).getName());
                            break;
                        }
                    }
                } else {
                    school.setText(getString(R.string.not_available));
                }

                Glide.with(getActivity()).load(data.getTeacher().getAvatar_url()).placeholder(R.mipmap.error_header).crossFade().into(image);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), TeacherDataActivity.class);
                        intent.putExtra("teacherId", data.getTeacher().getId());
                        startActivity(intent);
                    }
                });
            }
        }
    };
}
