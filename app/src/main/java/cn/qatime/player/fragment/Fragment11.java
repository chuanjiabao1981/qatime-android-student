package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.RemedialClassBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.ScreenUtils;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class Fragment11 extends BaseFragment {
    private PullToRefreshGridView grid;
    private List<RemedialClassBean.Data> list = new ArrayList<>();
    private CommonAdapter<RemedialClassBean.Data> adapter;
    private int page = 1;

    DecimalFormat df = new DecimalFormat("#.00");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment11, container, false);
        initview(view);
        initData(1);
        return view;
    }

    private void initview(View view) {
        grid = (PullToRefreshGridView) view.findViewById(R.id.grid);
//        View empty = LayoutInflater.from(getActivity()).inflate(R.layout.empty_view, null);
//        grid.getRefreshableView().setEmptyView(empty);
        grid.setMode(PullToRefreshBase.Mode.BOTH);
        grid.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        grid.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        grid.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        grid.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        grid.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        grid.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));


        adapter = new CommonAdapter<RemedialClassBean.Data>(getActivity(), list, R.layout.item_fragment11) {
            @Override
            public void convert(ViewHolder helper, RemedialClassBean.Data item, int position) {
                ((ImageView) helper.getView(R.id.image)).setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(getActivity()) / 2, ScreenUtils.getScreenWidth(getActivity()) / 2 * 5 / 8));
//                Glide.with(getActivity()).load(R.mipmap.four).placeholder(R.mipmap.photo).centerCrop().crossFade().dontAnimate().into(((ImageView) helper.getView(R.id.image)));
                Glide.with(getActivity()).load(item.getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().dontAnimate().into(((ImageView) helper.getView(R.id.image)));
                helper.setText(R.id.name, item.getName());
                helper.setText(R.id.subject, item.getSubject());
                helper.setText(R.id.grade, item.getGrade());
                helper.setText(R.id.teacher, item.getTeacher_name());
                String price = df.format(item.getPrice());
                if (price.startsWith(".")) {
                    price = "0" + price;
                }
                helper.setText(R.id.price, "￥" + price);
                helper.setText(R.id.student_number, String.valueOf(item.getBuy_tickets_count()));

            }
        };
        grid.setAdapter(adapter);

        grid.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                page = 1;
                initData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page++;
                initData(2);
            }
        });
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }
        });
    }

    /**
     * @param type 1刷新
     *             2加载更多
     */
    private void initData(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("per_page", "10");
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass, map), null,
                new VolleyListener(getActivity()) {


                    @Override
                    protected void onSuccess(JSONObject response) {
                        if (type == 1) {
                            list.clear();
                        }
                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        grid.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                        grid.onRefreshComplete();

                        try {
                            RemedialClassBean data = JsonUtils.objectFromJson(response.toString(), RemedialClassBean.class);
                            list.addAll(data.getData());
                            adapter.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        grid.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                        grid.onRefreshComplete();
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                grid.onRefreshComplete();
            }
        });
        addToRequestQueue(request);
    }
}
