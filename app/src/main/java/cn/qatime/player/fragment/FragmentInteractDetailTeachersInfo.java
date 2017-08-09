package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.TeacherDataActivity;
import cn.qatime.player.base.BaseFragment;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.InteractCourseDetailBean;
import libraryextra.bean.SchoolBean;
import libraryextra.bean.TeacherBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;

public class FragmentInteractDetailTeachersInfo extends BaseFragment {

    private ListView listView;
    private CommonAdapter<TeacherBean> adapter;
    private List<TeacherBean> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interact_detail_teachers_info, container, false);
        initview(view);
        return view;
    }


    private void initview(View view) {
        listView = (ListView)view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        listView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        list = new ArrayList<>();
        adapter = new CommonAdapter<TeacherBean>(getActivity(), list, R.layout.item_course_teacher_info) {

            @Override
            public void convert(ViewHolder holder, TeacherBean item, final int position) {
                if (item!=null) {
                    TextView name =holder.getView(R.id.name);
                    ImageView image =holder.getView(R.id.image);
                    TextView teachingyears =holder.getView(R.id.teaching_years);
                    TextView school =holder.getView(R.id.school);
                    ImageView sex =holder.getView(R.id.sex);
                    WebView describe = holder.getView(R.id.describe);

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
                    settings.setDefaultTextEncodingName("UTF-8");
                    settings.setBlockNetworkImage(false);
                    settings.setDefaultFontSize(14);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        settings.setMixedContentMode(settings.MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
                    }


                    sex.setImageResource("male".equals(item.getGender()) ? R.mipmap.male : R.mipmap.female);
//                    sex.setText(getSex(item.getGender()));
//                    sex.setTextColor(getSexColor(item.getGender()));
                    name.setText(item.getName());
                    if (!StringUtils.isNullOrBlanK(item.getTeaching_years())) {
                        if (item.getTeaching_years().equals("within_three_years")) {
                            teachingyears.setText(getResourceString(R.string.within_three_years));
                        } else if (item.getTeaching_years().equals("within_ten_years")) {
                            teachingyears.setText(getResourceString(R.string.within_ten_years));
                        } else if (item.getTeaching_years().equals("within_twenty_years")) {
                            teachingyears.setText(getResourceString(R.string.within_twenty_years));
                        } else {
                            teachingyears.setText(getResourceString(R.string.more_than_ten_years));
                        }
                    }

                    SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getActivity().getFilesDir() + "/school.txt").toString(), SchoolBean.class);
                    if (schoolBean != null && schoolBean.getData() != null) {
                        for (int i = 0; i < schoolBean.getData().size(); i++) {
                            if (item.getSchool() == schoolBean.getData().get(i).getId()) {
                                school.setText(schoolBean.getData().get(i).getName());
                                break;
                            }
                        }
                    } else {
                        school.setText(R.string.not_available);
                    }

                    Glide.with(getActivity()).load(item.getAvatar_url()).bitmapTransform(new GlideCircleTransform(getActivity())).placeholder(R.mipmap.error_header).crossFade().into(image);
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), TeacherDataActivity.class);
                            intent.putExtra("teacherId", list.get(position).getId());
                            startActivity(intent);
                        }
                    });
                    String body = StringUtils.isNullOrBlanK(item.getDesc()) ? getString(R.string.no_desc) : item.getDesc();
                    body = body.replace("\r\n", "<br>");
                    String css = "<style>* {color:#666666;margin:0;padding:0}</style>";//默认color（android标签下以及所有未设置颜色的标签）
                    describe.loadDataWithBaseURL(null, css + body, "text/html", "UTF-8", null);
                }
            }
        };
        listView.setAdapter(adapter);
    }

    public void setData(InteractCourseDetailBean data) {
        if(data.getData()!=null&&data.getData().getInteractive_course().getTeachers()!=null){
            list.addAll(data.getData().getInteractive_course().getTeachers());
            adapter.notifyDataSetChanged();
        }
    }

}
