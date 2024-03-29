package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.VideoCoursesPlayActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.VideoCoursesDetailsBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.VideoLessonsBean;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2017/4/10 17:05
 * @Describe
 */

public class FragmentVideoCoursesClassList extends BaseFragment {
    private List<VideoLessonsBean> list = new ArrayList<>();
    private CommonAdapter<VideoLessonsBean> adapter;
    private VideoCoursesDetailsBean data;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_courses_class_list, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        ListView listView = (ListView) findViewById(R.id.id_stickynavlayout_innerscrollview);
        listView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new CommonAdapter<VideoLessonsBean>(getActivity(), list, R.layout.item_fragment_video_courses_class_list) {

            @Override
            public void convert(ViewHolder holder, VideoLessonsBean item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.time, "时长" + item.getVideo().getFormat_tmp_duration());
                if (data.getData().getTicket() != null && data.getData().getTicket().getStatus().equals("active")) {
                    holder.getView(R.id.taste).setVisibility(View.GONE);
                } else {
                    holder.getView(R.id.taste).setVisibility(item.isTastable() ? View.VISIBLE : View.GONE);
                }
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (data.getData().getTicket() != null && data.getData().getTicket().getStatus().equals("active")) {
                    Intent intent = new Intent(getActivity(), VideoCoursesPlayActivity.class);
                    intent.putExtra("id", list.get(position).getVideo_course_id());
                    intent.putExtra("tasting", false);
                    startActivity(intent);
                } else if (list.get(position).isTastable()) {
                    if (data.getData().getTicket() == null) {//免费课程未购买则购买
                        joinAudition();//加入试听
                    } else {
                        Intent intent = new Intent(getActivity(), VideoCoursesPlayActivity.class);
                        intent.putExtra("id", list.get(position).getVideo_course_id());
                        intent.putExtra("tasting", true);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getActivity(), "该课程需要购买", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void joinAudition() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.urlVideoCourses + data.getData().getVideo_course().getId() + "/taste", null,
                new VolleyListener(getActivity()) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        data.getData().setTicket(new VideoCoursesDetailsBean.DataBean.TicketBean());
                        data.getData().getTicket().setStatus("inactive");
                        Intent intent = new Intent(getActivity(), VideoCoursesPlayActivity.class);
                        intent.putExtra("id", data.getData().getVideo_course().getId());
                        intent.putExtra("tasting", true);
                        startActivity(intent);
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(getActivity(), R.string.the_course_not_support_audition, Toast.LENGTH_SHORT).show();
//                                            }
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                }

                , new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    public void setData(VideoCoursesDetailsBean data) {
        this.data = data;
        list.clear();
        list.addAll(data.getData().getVideo_course().getVideo_lessons());
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
