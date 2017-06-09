package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.InteractCourseDetailActivity;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.activity.SearchResultActivity;
import cn.qatime.player.activity.VideoCoursesActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.SearchResultCourseBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2017/6/9 15:20
 * @Description:
 */
public class FragmentSearchCourse extends BaseFragment {

    private PullToRefreshListView listview;
    private CommonAdapter<SearchResultCourseBean.DataBean> adapter;
    private List<SearchResultCourseBean.DataBean> datas = new ArrayList<>();
    private int page = 1;
    private String search_key;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_course, container, false);
        initView(view);
        return view;
    }


    /**
     * @param type 0下拉1上啦
     */
    private void getData(final int type) {
        Map<String, String> map = new HashMap<>();
        if (type == 0) {
            page = 1;
        }
        map.put("search_key", search_key);
        map.put("search_cate", "course");
        map.put("per_page", "20");
        map.put("page", String.valueOf(page));

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlHomeSearch, map), null, new VolleyListener(getActivity()) {
            @Override
            protected void onTokenOut() {

            }

            @Override
            protected void onSuccess(JSONObject response) {
                if (type == 0) {
                    datas.clear();
                }
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                listview.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                listview.onRefreshComplete();
                SearchResultCourseBean data = JsonUtils.objectFromJson(response.toString(), SearchResultCourseBean.class);
                assert data != null;
                SearchResultActivity activity = (SearchResultActivity) getActivity();
                activity.setTeacherCount(data.getData().get(0).getProduct().getTotal_entries());
                datas.addAll(data.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onError(JSONObject response) {

            }
        }, new VolleyErrorListener());
        addToRequestQueue(request);
    }

    private void initView(View view) {
        listview = (PullToRefreshListView) view.findViewById(R.id.listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listview.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listview.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listview.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));
        listview.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new CommonAdapter<SearchResultCourseBean.DataBean>(getActivity(), datas, R.layout.item_search_course) {
            @Override
            public void convert(ViewHolder holder, SearchResultCourseBean.DataBean item, int position) {
                holder.setImageByUrl(R.id.image, item.getProduct().getPublicize(), R.mipmap.photo)
                        .setText(R.id.title, item.getProduct().getName())
                        .setText(R.id.teacher, item.getProduct().getTeacher_name())
                        .setText(R.id.grade_subject, item.getProduct().getGrade() + item.getProduct().getSubject());
            }
        };
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int courseId = datas.get(position - 1).getProduct().getId();
                Intent intent = null;
                if ("LiveStudio::Course".equals(datas.get(position).getProduct_type())) {
                    intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                } else if ("LiveStudio::InteractiveCourse".equals(datas.get(position).getProduct_type())) {
                    intent = new Intent(getActivity(), InteractCourseDetailActivity.class);
                } else if ("LiveStudio::VideoCourse".equals(datas.get(position).getProduct_type())) {
                    intent = new Intent(getActivity(), VideoCoursesActivity.class);
                }
                intent.putExtra("id", courseId);
                startActivity(intent);
            }
        });
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                getData(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page += 1;
                getData(1);
            }
        });
    }

    public void search(String search_key) {
        this.search_key = search_key;
        getData(0);
    }
}
