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
import android.widget.AdapterView;
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
        ListView listView = (ListView) view.findViewById(R.id.id_stickynavlayout_innerscrollview);
        listView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        list = new ArrayList<>();
        adapter = new CommonAdapter<TeacherBean>(getActivity(), list, R.layout.item_course_teacher_info) {

            @Override
            public void convert(ViewHolder holder, TeacherBean item, final int position) {
                if (item != null) {
                    TextView name = holder.getView(R.id.name);
                    ImageView image = holder.getView(R.id.image);
                    TextView teachingyears = holder.getView(R.id.teaching_years);
                    TextView school = holder.getView(R.id.school);
                    ImageView sex = holder.getView(R.id.sex);
                    TextView describe = holder.getView(R.id.describe);

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

                    SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getActivity().getFilesDir() + "/school.txt"), SchoolBean.class);
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
                    describe.setText(StringUtils.isNullOrBlanK(item.getDesc()) ? getString(R.string.no_desc) : item.getDesc());
                }
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), TeacherDataActivity.class);
                intent.putExtra("teacherId", list.get(i).getId());
                startActivity(intent);
            }
        });
    }

    public void setData(InteractCourseDetailBean data) {
        if (data.getData() != null && data.getData().getInteractive_course().getTeachers() != null) {
            list.addAll(data.getData().getInteractive_course().getTeachers());
            adapter.notifyDataSetChanged();
        }
    }
}
