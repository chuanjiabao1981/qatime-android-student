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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.NEVideoPlaybackActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.view.FlowLayout;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.view.ListViewForScrollView;

import static cn.qatime.player.R.id.status;

/**
 * @author lungtify
 * @Time 2017/3/28 11:24
 * @Describe 互动直播详情
 */
public class FragmentInteractiveDetails extends BaseFragment {
    private TextView subject;
    private TextView totalClass;
    private TextView grade;
    private TextView classStartTime;
    private TextView classEndTime;
    //    private WebView courseDescribe;
    private TextView name;
    private TextView sex;
    private TextView teachingYears;
    private TextView school;
    private WebView teacherDescribe;
    private ImageView image;
    private ListViewForScrollView list;
    private RemedialClassDetailBean.Data data;
    private CommonAdapter<RemedialClassDetailBean.Lessons> adapter;

    private List<RemedialClassDetailBean.Lessons> classList = new ArrayList<>();
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
        return View.inflate(getActivity(), R.layout.fragment_interactive_details, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        className = (TextView) findViewById(R.id.class_name);
        subject = (TextView) findViewById(R.id.subject);
        totalClass = (TextView) findViewById(R.id.total_class);
        grade = (TextView) findViewById(R.id.grade);
        classStartTime = (TextView) findViewById(R.id.class_start_time);
        classEndTime = (TextView) findViewById(R.id.class_end_time);
        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        teachingYears = (TextView) findViewById(R.id.teaching_years);
        school = (TextView) findViewById(R.id.school);
        image = (ImageView) findViewById(R.id.image);
        list = (ListViewForScrollView) findViewById(R.id.list);
        viewEmptyGone = findViewById(R.id.view_empty_gone);

        flowLayout = (LinearLayout) findViewById(R.id.flow_layout);
        flow = (FlowLayout) findViewById(R.id.flow);
        target = (TextView) findViewById(R.id.target);
        suitable = (TextView) findViewById(R.id.suitable);

        teacherDescribe = (WebView) findViewById(R.id.teacher_describe);
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
        settingsT.setDefaultFontSize(13);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settingsT.setMixedContentMode(settingsT.MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }


        initList();
    }

    private void initList() {
        list.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new CommonAdapter<RemedialClassDetailBean.Lessons>(getActivity(), classList, R.layout.item_fragment_nevideo_player33) {

            @Override
            public void convert(ViewHolder holder, RemedialClassDetailBean.Lessons item, int position) {
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
                RemedialClassDetailBean.Lessons item = classList.get(position);
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
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean isFinished(RemedialClassDetailBean.Lessons item) {
        return item.getStatus().equals("closed") || item.getStatus().equals("finished") || item.getStatus().equals("billing") || item.getStatus().equals("completed");
    }
}
