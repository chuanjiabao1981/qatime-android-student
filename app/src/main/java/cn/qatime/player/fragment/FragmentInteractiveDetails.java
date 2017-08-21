package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.NEVideoPlaybackActivity;
import cn.qatime.player.activity.TeacherDataActivity;
import cn.qatime.player.base.BaseFragment;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.InteractCourseDetailBean;
import libraryextra.bean.TeacherBean;
import libraryextra.transformation.GlideRoundTransform;
import libraryextra.utils.StringUtils;
import libraryextra.view.ListViewForScrollView;

/**
 * @author lungtify
 * @Time 2017/3/28 11:24
 * @Describe 互动直播详情
 */
public class FragmentInteractiveDetails extends BaseFragment {
    private TextView subject;
    private TextView grade;
    private ListViewForScrollView list;
    private InteractCourseDetailBean.DataBean data;
    private CommonAdapter<InteractCourseDetailBean.DataBean.InteractiveCourseBean.InteractiveLessonsBean> adapter;

    private List<InteractCourseDetailBean.DataBean.InteractiveCourseBean.InteractiveLessonsBean> classList = new ArrayList<>();
    //    private SimpleDateFormat parse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//    private SimpleDateFormat parse2 = new SimpleDateFormat("yyyy-MM-dd");
    private Handler hd = new Handler();
    private View viewEmptyGone;
    private TextView className;
    private TextView target;
    private TextView suitable;
    private CommonAdapter<TeacherBean> teacherAdapter;
    private List<TeacherBean> teacherTotalList = new ArrayList<>();
    private List<TeacherBean> teacherList = new ArrayList<>();
    private TextView showAll;
    private ListViewForScrollView teacherListView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_interactive_details, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        className = (TextView) findViewById(R.id.class_name);
        subject = (TextView) findViewById(R.id.subject);
        grade = (TextView) findViewById(R.id.grade);
        list = (ListViewForScrollView) findViewById(R.id.list);
        viewEmptyGone = findViewById(R.id.view_empty_gone);

        target = (TextView) findViewById(R.id.target);
        suitable = (TextView) findViewById(R.id.suitable);

        teacherListView = findViewById(R.id.teacherList);

        showAll = findViewById(R.id.show_all);

        initList();
    }

    private void initList() {
        list.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new CommonAdapter<InteractCourseDetailBean.DataBean.InteractiveCourseBean.InteractiveLessonsBean>(getActivity(), classList, R.layout.item_fragment_interactive_details_courses) {

            @Override
            public void convert(ViewHolder holder, InteractCourseDetailBean.DataBean.InteractiveCourseBean.InteractiveLessonsBean item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.teacher_name, "教师：" + item.getTeacher().getName());
                holder.setText(R.id.class_date, item.getClass_date());
                holder.setText(R.id.live_time, item.getStart_time() + "-" + item.getEnd_time());
                if (item.getStatus().equals("missed")) {
                    holder.setText(R.id.status, getResourceString(R.string.class_missed));
                } else if (item.getStatus().equals("init")) {//未开始
                    holder.setText(R.id.status, getResourceString(R.string.class_init));
                } else if (item.getStatus().equals("ready")) {//待开课
                    holder.setText(R.id.status, getResourceString(R.string.class_ready));
                } else if (item.getStatus().equals("teaching")) {//直播中
                    holder.setText(R.id.status, getResourceString(R.string.class_teaching));
                } else if (item.getStatus().equals("closed")) {//已直播
                    holder.setText(R.id.status, getResourceString(R.string.class_closed));
                } else if (item.getStatus().equals("paused")) {
                    holder.setText(R.id.status, getResourceString(R.string.class_teaching));
                } else {//closed finished billing completed
                    holder.setText(R.id.status, getResourceString(R.string.class_over));//已结束
                }
                if (isFinished(item)) {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.teacher_name)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff999999);

                    if (data != null && data.getTicket() != null && !StringUtils.isNullOrBlanK(data.getTicket().getStatus()) && data.getTicket().getStatus().equals("active") && item.isReplayable()) {
                        ((TextView) holder.getView(R.id.status)).setTextColor(0xffff5842);
                        holder.setText(R.id.status, "观看回放");
                    } else {
                        ((TextView) holder.getView(R.id.status)).setTextColor(0xff999999);
                    }
                } else {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff00a0e9);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.teacher_name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff666666);
                }


            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isFinished(classList.get(position))) {
                    if (data != null && data.getTicket() != null && !StringUtils.isNullOrBlanK(data.getTicket().getStatus()) && data.getTicket().getStatus().equals("active") &&classList.get(position).isReplayable()) {
                        if (!classList.get(position).isReplayable()) {
                            Toast.makeText(getActivity(), "该课程不可回放", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(getActivity(), NEVideoPlaybackActivity.class);
                        intent.putExtra("id", classList.get(position).getId());
                        intent.putExtra("name", classList.get(position).getName());
                        intent.putExtra("type", "interact");
                        startActivity(intent);
                    }
                }
            }
        });
        adapter.notifyDataSetChanged();
        teacherAdapter = new CommonAdapter<TeacherBean>(getActivity(), teacherList, R.layout.item_interactive_details) {
            @Override
            public void convert(ViewHolder holder, TeacherBean item, int position) {
                Glide.with(getActivity()).load(item.getAvatar_url()).placeholder(R.mipmap.error_header).crossFade().transform(new GlideRoundTransform(getActivity())).into((ImageView) holder.getView(R.id.image));

                holder.setText(R.id.name, item.getName())
                        .setText(R.id.sex, getSex(item.getGender()))
                        .setText(R.id.school, item.getSchool_name())
                        .setText(R.id.teaching_years, getTeacheringYears(item.getTeaching_years()));
                ((TextView) holder.getView(R.id.sex)).setTextColor(getSexColor(item.getGender()));
            }
        };
        teacherListView.setAdapter(teacherAdapter);
        teacherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TeacherDataActivity.class);
                intent.putExtra("teacherId", teacherList.get(position).getId());
                startActivity(intent);
            }
        });
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAll.setVisibility(View.GONE);
                teacherList.clear();
                teacherList.addAll(teacherTotalList);
                teacherAdapter.notifyDataSetChanged();
            }
        });
    }

    private boolean isFinished(InteractCourseDetailBean.DataBean.InteractiveCourseBean.InteractiveLessonsBean item) {
        return item.getStatus().equals("closed") || item.getStatus().equals("finished") || item.getStatus().equals("billing") || item.getStatus().equals("completed");
    }

    public void setData(InteractCourseDetailBean.DataBean data) {
        this.data = data;
        setDataClassDetails();
        setDataTeacherDetails();
        setDataListDetails();
    }

    private void setDataTeacherDetails() {
        teacherTotalList.addAll(data.getInteractive_course().getTeachers());
        if (teacherTotalList.size() > 2) {
            teacherList.add(teacherTotalList.get(0));
            teacherList.add(teacherTotalList.get(1));
            showAll.setVisibility(View.VISIBLE);
        } else {
            showAll.setVisibility(View.GONE);
            teacherList.addAll(teacherTotalList);
        }
        teacherAdapter.notifyDataSetChanged();
    }

    private void setDataListDetails() {
        classList.clear();
        classList.addAll(data.getInteractive_course().getInteractive_lessons());
        if (classList.size() == 0) {
            viewEmptyGone.setVisibility(View.GONE);
        } else {
            viewEmptyGone.setVisibility(View.VISIBLE);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


    private void setDataClassDetails() {
        hd.postDelayed(runnableClass, 1000);
    }

    private Runnable runnableClass = new Runnable() {
        @Override
        public void run() {
            if (getActivity() != null && getActivity().getResources() != null) {
                className.setText(data.getInteractive_course().getName());
                subject.setText((data.getInteractive_course().getSubject() == null ? "" : data.getInteractive_course().getSubject()));
                grade.setText((data.getInteractive_course().getGrade() == null ? "" : data.getInteractive_course().getGrade()));
                if (!StringUtils.isNullOrBlanK(data.getInteractive_course().getObjective())) {
                    target.setText(data.getInteractive_course().getObjective());
                }
                if (!StringUtils.isNullOrBlanK(data.getInteractive_course().getSuit_crowd())) {
                    suitable.setText(data.getInteractive_course().getSuit_crowd());
                }
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

    private String getTeacheringYears(String value) {
        if (!StringUtils.isNullOrBlanK(value)) {
            switch (value) {
                case "within_three_years":
                    return getResourceString(R.string.within_three_years);
                case "within_ten_years":
                    return getResourceString(R.string.within_ten_years);
                case "within_twenty_years":
                    return getResourceString(R.string.within_twenty_years);
                default:
                    return getResourceString(R.string.more_than_ten_years);
            }
        }
        return "";
    }
}
