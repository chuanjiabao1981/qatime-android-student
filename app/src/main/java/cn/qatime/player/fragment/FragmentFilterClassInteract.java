package cn.qatime.player.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.InteractCourseDetailActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.FilterInteractCourseBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.TeacherBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2017/4/10 18:16
 * @Description:
 */
public class FragmentFilterClassInteract extends BaseFragment {


    private String grade;
    private String subject;
    private PullToRefreshListView listview;
    private CommonAdapter<FilterInteractCourseBean.DataBean> adapter;
    private List<FilterInteractCourseBean.DataBean> datas = new ArrayList<>();
    private int latestResult = 1;//0上1下-1未选
    private int priceResult = -1;
    private int page = 1;

    public BaseFragment setArguments(String grade, String subject) {
        this.grade = grade;
        this.subject = subject;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_interact_course, container, false);
        initView(view);
        getData(0);
        return view;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_filter_interact_course);
//        grade = getIntent().getStringExtra("grade");
//        subject = getIntent().getStringExtra("subject");
//        setTitles(grade + subject + "一对一");
//        initView();
//        getData(0);
//    }

    /**
     * @param type 0下拉1上啦
     */
    public void getData(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("per_page", "20");
        map.put("page", String.valueOf(page));

        if (type == 0) {
            page = 1;
        }

        if (latestResult != -1) {//0上1下-1未选
            if (latestResult == 0) {
                map.put("sort_by", "published_at");
            } else {
                map.put("sort_by", "published_at.asc");
            }
        } else if (priceResult != -1) {
            if (priceResult == 0) {
                map.put("sort_by", "left_price");
            } else {
                map.put("sort_by", "left_price.asc");
            }
        }
        try {
            map.put("q[grade_eq]", URLEncoder.encode(grade, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            if (!subject.equals("全部")) {
                map.put("q[subject_eq]", URLEncoder.encode(subject, "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlInteractCourses + "search", map), null, new VolleyListener(getActivity()) {
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
                FilterInteractCourseBean data = JsonUtils.objectFromJson(response.toString(), FilterInteractCourseBean.class);
                assert data != null;
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
        final TextView latest = (TextView) view.findViewById(R.id.latest);
        final TextView price = (TextView) view.findViewById(R.id.price);

        latest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latestResult == -1) {
                    latestResult = 1;
                } else if (latestResult == 0) {
                    latestResult = 1;
                } else if (latestResult == 1) {
                    latestResult = 0;
                }
                priceResult = -1;
                refreshState(latest, latestResult);
                refreshState(price, priceResult);
                getData(0);
            }
        });
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (priceResult == -1) {
                    priceResult = 1;
                } else if (priceResult == 0) {
                    priceResult = 1;
                } else if (priceResult == 1) {
                    priceResult = 0;
                }
                latestResult = -1;
                refreshState(latest, latestResult);
                refreshState(price, priceResult);
                getData(0);
            }
        });

        listview = (PullToRefreshListView) view.findViewById(R.id.listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listview.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listview.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listview.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));
        adapter = new CommonAdapter<FilterInteractCourseBean.DataBean>(getActivity(), datas, R.layout.item_filter_course) {
            @Override
            public void convert(ViewHolder holder, FilterInteractCourseBean.DataBean item, int position) {
                Glide.with(getActivity()).load(item.getPublicize_url()).crossFade().placeholder(R.mipmap.photo).into((ImageView) holder.getView(R.id.image));
                List<TeacherBean> teachers = item.getTeachers();
                StringBuilder teacherNames = new StringBuilder();
                for (int i = 0; i < teachers.size(); i++) {
                    teacherNames.append(teachers.get(0).getName());
                    if (i != teachers.size() - 1) {
                        teacherNames.append("/");
                    }
                }
                holder.setText(R.id.name, item.getName())
                        .setText(R.id.price, "￥" + item.getPrice())
                        .setText(R.id.teacher, teacherNames.toString());
            }
        };
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InteractCourseDetailActivity.class);
                intent.putExtra("id", datas.get(position - 1).getId());
                getActivity().startActivityForResult(intent, Constant.REQUEST);
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

    private void refreshState(TextView view, int result) {//-1未选 0上 1下
        if (result == -1) {
            view.setCompoundDrawables(null, null, null, null);
            view.setTextColor(0xff666666);
        } else if (result == 0) {
            Drawable up = getResources().getDrawable(R.mipmap.arrow_up);
            up.setBounds(0, 0, up.getMinimumWidth(), up.getMinimumHeight());
            view.setCompoundDrawables(null, null, up, null);
            view.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (result == 1) {
            Drawable down = getResources().getDrawable(R.mipmap.arrow_down);
            down.setBounds(0, 0, down.getMinimumWidth(), down.getMinimumHeight());
            view.setCompoundDrawables(null, null, down, null);
            view.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }
}
