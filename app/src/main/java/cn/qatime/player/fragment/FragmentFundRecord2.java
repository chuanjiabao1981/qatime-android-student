package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2016/9/27 17:17
 * @Description:
 */
public class FragmentFundRecord2 extends BaseFragment {
    private PullToRefreshListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fund_record2, container, false);
        initview(view);
        initData();
        return view;
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("start_date", "0");
        map.put("end_date", new Date().getTime() + "");
        map.put("page", "1");
        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getUserId() + "/consumption_records", map), null, new VolleyListener(getActivity()) {

            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {

            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(getActivity(), getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.getRefreshableView().setDividerHeight(2);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));

//        adapter = new CommonAdapter<RechargeRecordBean.DataBean>(getActivity(), data, R.layout.item_fragment_fund_record2) {
//
//            @Override
//            public void convert(ViewHolder helper, RechargeRecordBean.DataBean item, int position) {
//                helper.setText(R.id.id, item.getId());
//                helper.setText(R.id.classname, item.getAmount());
//                helper.setText(R.id.time, item.getCreated_at());
//                helper.setText(R.id.money_amount, item.getAmount());
//                helper.setText(R.id.status, item.getStatus());
//            }
//        };
//        listView.setAdapter(adapter);
//
//        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        String label = DateUtils.formatDateTime(
//                                getActivity(),
//                                System.currentTimeMillis(),
//                                DateUtils.FORMAT_SHOW_TIME
//                                        | DateUtils.FORMAT_SHOW_DATE
//                                        | DateUtils.FORMAT_ABBREV_ALL);
//                        // Update the LastUpdatedLabel
//                        listView.getLoadingLayoutProxy(false, true)
//                                .setLastUpdatedLabel(label);
//                        listView.onRefreshComplete();
//                    }
//                }, 200);
//                initData();
//            }
//        });
    }
}