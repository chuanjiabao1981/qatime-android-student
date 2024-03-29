package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.NEVideoPlaybackActivity;
import cn.qatime.player.activity.TeacherDataActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.view.FlowLayout;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.Lessons;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.bean.SchoolBean;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.view.ListViewForScrollView;

import static android.view.View.GONE;
import static cn.qatime.player.R.id.status;

public class FragmentPlayerLiveDetails extends BaseFragment {
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
    private ListViewForScrollView list;
    private RemedialClassDetailBean.Data data;
    private CommonAdapter<Lessons> adapter;

    private List<Lessons> classList = new ArrayList<>();
    private SimpleDateFormat parse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat parse2 = new SimpleDateFormat("yyyy-MM-dd");
    private Handler hd = new Handler();
    private View viewEmptyGone;
    private TextView className;
    private FlowLayout flow;
    private LinearLayout flowLayout;
    private TextView target;
    private TextView suitable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_nevideo_player3, null);
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
        list = (ListViewForScrollView) view.findViewById(R.id.list);
        viewEmptyGone = view.findViewById(R.id.view_empty_gone);

        flowLayout = (LinearLayout) view.findViewById(R.id.flow_layout);
        flow = (FlowLayout) view.findViewById(R.id.flow);
        target = (TextView) view.findViewById(R.id.target);
        suitable = (TextView) view.findViewById(R.id.suitable);

        teacherDescribe = (WebView) view.findViewById(R.id.teacher_describe);
        teacherDescribe.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

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
        list.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new CommonAdapter<Lessons>(getActivity(), classList, R.layout.item_fragment_nevideo_player33) {

            @Override
            public void convert(ViewHolder holder, Lessons item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.live_time, item.getLive_time());
                if (item.getStatus().equals("missed")) {
                    holder.setText(status, getResourceString(R.string.class_missed));
                } else if (item.getStatus().equals("init")) {//未开始
                    holder.setText(status, getResourceString(R.string.class_init));
                } else if (item.getStatus().equals("ready")) {//待开课
                    holder.setText(status, getResourceString(R.string.class_ready));
                } else if (item.getStatus().equals("teaching")) {//直播中
                    holder.setText(status, getResourceString(R.string.class_teaching));
                } else if (item.getStatus().equals("closed")) {//已直播
                    holder.setText(status, getResourceString(R.string.class_closed));
                } else if (item.getStatus().equals("paused")) {
                    holder.setText(status, getResourceString(R.string.class_teaching));
                } else {//closed finished billing completed
                    holder.setText(status, getResourceString(R.string.class_over));//已结束
                }
                holder.setText(R.id.class_date, item.getClass_date());
                holder.setText(R.id.view_playback, getString(R.string.playback_count, item.getLeft_replay_times()));
                if (isFinished(item)) {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff999999);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff999999);
                    holder.getView(R.id.view_playback).setVisibility(data.getIs_bought() && item.isReplayable() ? View.VISIBLE : View.GONE);
                } else {
                    ((TextView) holder.getView(R.id.status_color)).setTextColor(0xff00a0e9);
                    ((TextView) holder.getView(R.id.name)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.live_time)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.status)).setTextColor(0xff666666);
                    ((TextView) holder.getView(R.id.class_date)).setTextColor(0xff666666);
                    holder.getView(R.id.view_playback).setVisibility(View.GONE);
                }

            }
        };
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Lessons item = classList.get(position);
                if (isFinished(item)) {
                    if (data.getIs_bought()) {
                        if (!item.isReplayable()) {
//                            Toast.makeText(getActivity(), getResourceString(R.string.no_playback_video), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (item.getLeft_replay_times() <= 0) {
                            Toast.makeText(getActivity(), getResourceString(R.string.have_no_left_playback_count), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(getActivity(), NEVideoPlaybackActivity.class);
                        intent.putExtra("id", item.getId());
                        intent.putExtra("name", item.getName());
                        intent.putExtra("type","live");
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean isFinished(Lessons item) {
        return item.getStatus().equals("closed") || item.getStatus().equals("finished") || item.getStatus().equals("billing") || item.getStatus().equals("completed");
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
                className.setText(data.getName());
                subject.setText((data.getSubject() == null ? "" : data.getSubject()));
                try {
                    classStartTime.setText((data.getLive_start_time() == null ? "" : parse2.format(parse1.parse(data.getLive_start_time()))));
                    classEndTime.setText(parse2.format(parse1.parse(data.getLive_end_time())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                grade.setText((data.getGrade() == null ? "" : data.getGrade()));
                totalClass.setText(getString(R.string.lesson_count, data.getPreset_lesson_count()));
                if (!StringUtils.isNullOrBlanK(data.getTag_list())) {
                    for (int va = 0; va < data.getTag_list().size(); va++) {
                        TextView textView = new TextView(getActivity());
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextColor(0xff999999);
                        textView.setTextSize(13);
                        textView.setBackgroundResource(R.drawable.text_background_flowlayout);
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = 2;
                        params.rightMargin = 2;
                        params.topMargin = 2;
                        params.bottomMargin = 2;
                        textView.setLayoutParams(params);
                        textView.setText(data.getTag_list().get(va));
                        flow.addView(textView);
                    }
                } else {
                    flowLayout.setVisibility(GONE);
                }
                if (!StringUtils.isNullOrBlanK(data.getObjective())) {
                    target.setText(data.getObjective());
                }
                if (!StringUtils.isNullOrBlanK(data.getSuit_crowd())) {
                    suitable.setText(data.getSuit_crowd());
                }
//                String body =StringUtils.isNullOrBlanK(data.getDescription()) ? getString(R.string.no_desc) : data.getDescription();
//                body = body.replace("\r\n", "<br>");
//                String css = "<style>* {color:#999999;}</style>";//默认color
//                courseDescribe.loadDataWithBaseURL(null,css+body,"text/html","UTF-8",null);
            }
        }
    };
    private Runnable runnableTeacher = new Runnable() {
        @Override
        public void run() {
            if (getActivity() != null && getActivity().getResources() != null) {
//                sex.setText(getSex(data.getTeacher().getGender()));
//                sex.setTextColor(getSexColor(data.getTeacher().getGender()));
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
