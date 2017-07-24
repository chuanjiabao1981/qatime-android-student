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
import cn.qatime.player.activity.ExclusiveScreeningConditionActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.FilterLiveCourseBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2017/4/10 18:15
 * @Description:
 */
public class FragmentFilterClassExclusive extends BaseFragment {

    private String grade;
    private String subject;
    private PullToRefreshListView listview;
    private CommonAdapter<FilterLiveCourseBean.DataBean> adapter;
    private List<FilterLiveCourseBean.DataBean> datas = new ArrayList<>();
    private int latestResult = 1;//0上1下-1未选
    private int popularityResult = -1;
    private int priceResult = -1;
    private int page = 1;
    private String range = null;//查询范围
    private String courseStatus = null;//课程状态
    private String sellType = null;//课程类型
    private String endTime = null;
    private String startTime = null;

    public BaseFragment setArguments(String grade, String subject) {
        this.grade = grade;
        this.subject = subject;
        return this;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_exclusive_course, container, false);
        initView(view);
        getData(0);
        return view;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_filter_course);
//        grade = getIntent().getStringExtra("grade");
//        subject = getIntent().getStringExtra("subject");
//        setTitles(grade + subject+"直播课");
//        initView();
//    }

    /**
     * @param type 0下拉1上啦
     */
    private void getData(final int type) {
        Map<String, String> map = new HashMap<>();
        if (type == 0) {
            page = 1;
        }

        map.put("per_page", "20");
        map.put("page", String.valueOf(page));

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
        } else if (popularityResult != -1) {
            if (popularityResult == 0) {
                map.put("sort_by", "buy_tickets_count");
            } else {
                map.put("sort_by", "buy_tickets_count.asc");
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

        if (!StringUtils.isNullOrBlanK(range)) {
            map.put("range", range);
        }
        if (!StringUtils.isNullOrBlanK(courseStatus)) {
            map.put("q[status_eq]", courseStatus);
        }
        if (!StringUtils.isNullOrBlanK(startTime) && !StringUtils.isNullOrBlanK(endTime)) {
            map.put("q[class_date_gteq]", startTime);
            map.put("q[class_date_lt]", endTime);
        }
        map.put("q[sell_type_eq]", sellType);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlSearch, map), null, new VolleyListener(getActivity()) {
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
                FilterLiveCourseBean data = JsonUtils.objectFromJson(response.toString(), FilterLiveCourseBean.class);
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
        final TextView popularity = (TextView) view.findViewById(R.id.popularity);

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
                popularityResult = -1;
                refreshState(latest, latestResult);
                refreshState(price, priceResult);
                refreshState(popularity, popularityResult);
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
                popularityResult = -1;
                refreshState(latest, latestResult);
                refreshState(price, priceResult);
                refreshState(popularity, popularityResult);
                getData(0);
            }
        });
        popularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popularityResult == -1) {
                    popularityResult = 1;
                } else if (popularityResult == 0) {
                    popularityResult = 1;
                } else if (popularityResult == 1) {
                    popularityResult = 0;
                }
                latestResult = -1;
                priceResult = -1;
                refreshState(latest, latestResult);
                refreshState(price, priceResult);
                refreshState(popularity, popularityResult);
                getData(0);
            }
        });
        View screen = view.findViewById(R.id.screen);

        listview = (PullToRefreshListView) view.findViewById(R.id.listview);
        listview.setMode(PullToRefreshBase.Mode.BOTH);
        listview.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listview.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listview.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listview.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listview.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));
        listview.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        adapter = new CommonAdapter<FilterLiveCourseBean.DataBean>(getActivity(), datas, R.layout.item_filter_course_exclusive) {
            @Override
            public void convert(ViewHolder holder, FilterLiveCourseBean.DataBean item, int position) {
                Glide.with(getActivity()).load(item.getPublicize()).crossFade().placeholder(R.mipmap.photo).into((ImageView) holder.getView(R.id.image));
                holder.setText(R.id.name, item.getName())
                        .setText(R.id.price, "free".equals(item.getSell_type()) ? "免费" : ("￥" + item.getPrice()))
                        .setText(R.id.teacher, item.getTeacher_name())
                        .setText(R.id.buy_count, item.getBuy_tickets_count() + "");
            }
        };
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
//                intent.putExtra("id", datas.get(position - 1).getId());
//                startActivity(intent);
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

        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExclusiveScreeningConditionActivity.class);
                intent.putExtra("courseStatus", courseStatus);
                intent.putExtra("startTime", startTime);
                intent.putExtra("sellType", sellType);
                intent.putExtra("endTime", endTime);
                startActivityForResult(intent, Constant.REQUEST);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            if (data != null) {
                courseStatus = data.getStringExtra("courseStatus");
                sellType = data.getStringExtra("sellType");
                startTime = data.getStringExtra("startTime");
                endTime = data.getStringExtra("endTime");
                getData(0);
            }
        }
    }
}
