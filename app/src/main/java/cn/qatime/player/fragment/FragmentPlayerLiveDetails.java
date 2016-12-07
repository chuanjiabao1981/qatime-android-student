package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.TeacherDataActivity;
import cn.qatime.player.base.BaseFragment;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.bean.SchoolBean;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.view.GridViewForScrollView;

public class FragmentPlayerLiveDetails extends BaseFragment {
    private TextView subject;
    private TextView totalClass;
    private TextView grade;
    private TextView classType;
    private TextView classStartTime;
    private TextView classEndTime;
    private TextView courseDescribe;
    private TextView name;
    private TextView sex;
    private TextView teachingYears;
    private TextView school;
    private TextView teacherDescribe;
    private ImageView image;
    private GridViewForScrollView list;
    private RemedialClassDetailBean.Data data;
    private CommonAdapter<RemedialClassDetailBean.Lessons> adapter;
    private List<RemedialClassDetailBean.Lessons> classList = new ArrayList<>();

    private SimpleDateFormat parse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat parse2 = new SimpleDateFormat("yyyy-MM-dd");
    private Handler hd = new Handler();
    private View viewEmptyGone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_nevideo_player3, null);
        subject = (TextView) view.findViewById(R.id.subject);
        totalClass = (TextView) view.findViewById(R.id.total_class);
        grade = (TextView) view.findViewById(R.id.grade);
        classType = (TextView) view.findViewById(R.id.class_type);
        classStartTime = (TextView) view.findViewById(R.id.class_start_time);
        classEndTime = (TextView) view.findViewById(R.id.class_end_time);
        courseDescribe = (TextView) view.findViewById(R.id.course_describe);
        name = (TextView) view.findViewById(R.id.name);
        sex = (TextView) view.findViewById(R.id.sex);
        teachingYears = (TextView) view.findViewById(R.id.teaching_years);
        school = (TextView) view.findViewById(R.id.school);
        teacherDescribe = (TextView) view.findViewById(R.id.teacher_describe);
        image = (ImageView) view.findViewById(R.id.image);
        list = (GridViewForScrollView) view.findViewById(R.id.list);
        viewEmptyGone = view.findViewById(R.id.view_empty_gone);
        initList();
        return view;
    }

    private void initList() {
        list.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new CommonAdapter<RemedialClassDetailBean.Lessons>(getActivity(), classList, R.layout.item_fragment_nevideo_player33) {

            @Override
            public void convert(ViewHolder holder, RemedialClassDetailBean.Lessons item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.live_time, item.getLive_time());
                if (item.getStatus().equals("missed")) {
                    holder.setText(R.id.status, getResourceString(R.string.class_missed));
                } else if (item.getStatus().equals("init")) {//未开始
                    holder.setText(R.id.status, getResourceString(R.string.class_init));
                } else if (item.getStatus().equals("ready")) {//待开课
                    holder.setText(R.id.status, getResourceString(R.string.class_ready));
                } else if (item.getStatus().equals("teaching")) {//直播中
                    holder.setText(R.id.status, getResourceString(R.string.class_teaching));
                } else if (item.getStatus().equals("paused")) {
                    holder.setText(R.id.status, getResourceString(R.string.class_teaching));
                } else {//closed finished billing completed
                    holder.setText(R.id.status, getResourceString(R.string.class_over));//已结束
                }
                holder.setText(R.id.class_date, item.getClass_date());
                if (item.getStatus().equals("closed") || item.getStatus().equals("finished") || item.getStatus().equals("billing") || item.getStatus().equals("completed")) {
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
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public void setData(RemedialClassDetailBean data) {
        this.data = data.getData();
        setDataClassDetails();
        setDataTeacherDetails();
        setDataListDetails();
    }

    private void setDataListDetails() {
        classList.clear();
        classList.addAll(data.getLessons());
        if (classList.size() == 0) {
            viewEmptyGone.setVisibility(View.GONE);
        } else {
            viewEmptyGone.setVisibility(View.VISIBLE);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
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
                subject.setText((data.getSubject() == null ? "" : data.getSubject()));
                try {
                    classStartTime.setText((data.getLive_start_time() == null ? "" : parse2.format(parse1.parse(data.getLive_start_time()))));
                    classEndTime.setText(parse2.format(parse1.parse(data.getLive_end_time())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                grade.setText((data.getGrade() == null ? "" : data.getGrade()));
                totalClass.setText("共" + data.getPreset_lesson_count() + "课");
                courseDescribe.setText(StringUtils.isNullOrBlanK(data.getDescription()) ? "暂无简介" : data.getDescription());
            }
        }
    };
    private Runnable runnableTeacher = new Runnable() {
        @Override
        public void run() {
            if (getActivity() != null && getActivity().getResources() != null) {
                sex.setText(getSex(data.getTeacher().getGender()));
                sex.setTextColor(getSexColor(data.getTeacher().getGender()));
                name.setText(data.getTeacher().getName());
                if (!StringUtils.isNullOrBlanK(data.getTeacher().getTeaching_years())) {
                    if (data.getTeacher().getTeaching_years().equals("within_three_years")) {
                        teachingYears.setText(getResourceString(R.string.teacher_years) + " " + getResourceString(R.string.within_three_years));
                    } else if (data.getTeacher().getTeaching_years().equals("within_ten_years")) {
                        teachingYears.setText(getResourceString(R.string.teacher_years) + " " + getResourceString(R.string.within_ten_years));
                    } else if (data.getTeacher().getTeaching_years().equals("within_twenty_years")) {
                        teachingYears.setText(getResourceString(R.string.teacher_years) + " " + getResourceString(R.string.within_twenty_years));
                    } else {
                        teachingYears.setText(getResourceString(R.string.teacher_years) + " " + getResourceString(R.string.more_than_ten_years));
                    }
                }
                teacherDescribe.setText(StringUtils.isNullOrBlanK(data.getTeacher().getDesc()) ? "暂无简介" : data.getTeacher().getDesc());

                SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getActivity().getCacheDir() + "/school.txt").toString(), SchoolBean.class);
                if (schoolBean != null && schoolBean.getData() != null) {
                    for (int i = 0; i < schoolBean.getData().size(); i++) {
                        if (data.getTeacher().getSchool() == schoolBean.getData().get(i).getId()) {
                            school.setText(getResourceString(R.string.teacher_school) + " " + schoolBean.getData().get(i).getName());
                            break;
                        }
                    }
                } else {
                    school.setText(getResourceString(R.string.teacher_school) + " 暂无");
                }

                Glide.with(getActivity()).load(data.getTeacher().getAvatar_url()).placeholder(R.mipmap.error_header_rect).crossFade().into(image);
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

    private int getSexColor(String gender) {
        if ("male".equals(gender)) {
            return 0xff00ccff;
        } else if ("female".equals(gender)) {
            return 0xffff9966;
        }
        return 0xffff9966;
    }

    private String getSex(String gender) {
        if ("male".equals(gender)) {
            return "♂";
        } else if ("female".equals(gender)) {
            return "♀";
        }
        return "";
    }
}
