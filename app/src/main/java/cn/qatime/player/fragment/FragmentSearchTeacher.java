package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.SearchResultActivity;
import cn.qatime.player.activity.TeacherDataActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.SearchResultTeacherBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2017/6/9 15:20
 * @Description:
 */
public class FragmentSearchTeacher extends BaseFragment {


    private PullToRefreshListView listview;
    private CommonAdapter<SearchResultTeacherBean.DataBean> adapter;
    private List<SearchResultTeacherBean.DataBean> datas = new ArrayList<>();
    private int page = 1;
    private String search_key;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_teacher, container, false);
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
        map.put("search_cate", "teacher");
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
                SearchResultTeacherBean data = JsonUtils.objectFromJson(response.toString(), SearchResultTeacherBean.class);
                assert data != null;
                SearchResultActivity activity = (SearchResultActivity) getActivity();
                activity.setTeacherCount(data.getData().get(0).getTotal_entries());
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
        adapter = new CommonAdapter<SearchResultTeacherBean.DataBean>(getActivity(), datas, R.layout.item_search_teacher) {
            @Override
            public void convert(ViewHolder holder, SearchResultTeacherBean.DataBean item, int position) {
                Glide.with(getActivity()).load(item.getAvatar_url()).bitmapTransform(new GlideCircleTransform(getActivity())).placeholder(R.mipmap.error_header).crossFade().into((ImageView) holder.getView(R.id.image));
                holder.setText(R.id.name, item.getNick_name())
                        .setText(R.id.teaching_years, getTeachingYear(item.getTeaching_years()))
                        .setText(R.id.info, item.getCategory() + item.getSubject() + "|" + item.getSchool());
            }
        };
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TeacherDataActivity.class);
                intent.putExtra("teacherId", datas.get(position-1).getId());
                startActivityForResult(intent, Constant.REQUEST);
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

    private String getTeachingYear(String teaching_years) {
        switch (teaching_years) {
            case "within_three_years":
                return getResourceString(R.string.within_three_years) + getString(R.string.teach_age);
            case "within_ten_years":
                return getResourceString(R.string.within_ten_years) + getString(R.string.teach_age);
            case "within_twenty_years":
                return getResourceString(R.string.within_twenty_years) + getString(R.string.teach_age);
        }
        return "三年以内教龄";
    }

    public void search(String search_key) {
        this.search_key = search_key;
        getData(0);
    }
}
