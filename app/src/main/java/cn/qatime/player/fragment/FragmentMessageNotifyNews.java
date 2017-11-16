package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.PersonalMyOrderActivity;
import cn.qatime.player.activity.PersonalMyWalletActivity;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.BusEvent;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.SystemNotifyBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author luntify
 * @date 2016/8/15 20:05
 * @Description
 */
public class FragmentMessageNotifyNews extends BaseFragment {
    private PullToRefreshListView listView;
    private CommonAdapter<SystemNotifyBean.DataBean> adapter;
    private List<SystemNotifyBean.DataBean> list = new ArrayList<>();
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_message_notify_news, null);
        initview(view);
        initOver = true;
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));


        adapter = new CommonAdapter<SystemNotifyBean.DataBean>(getActivity(), list, R.layout.item_fragment_news2) {
            @Override
            public void convert(ViewHolder helper, SystemNotifyBean.DataBean item, int position) {
//                &#8195;
//                String blank = "\u3000\u3000  ";
//                if (item != null && !StringUtils.isNullOrBlanK(item.getNotificationable_type())) {
//                    switch (item.getNotificationable_type()) {
//                        case "live_studio/course":
//                            if ("start".equals(item.getAction_name())) {
//                                helper.setText(R.id.type, " 开课 ", 0xff00a0e9);
//                                helper.getView(R.id.type).setBackgroundResource(R.drawable.notify_text_background_teaching);
//                            } else {
//                                helper.setText(R.id.type, " 公告 ", 0xffffafaf);
//                                helper.getView(R.id.type).setBackgroundResource(R.drawable.notify_text_background_notice);
//                            }
//                            break;
//                        case "live_studio/lesson":
//                            if ("start_for_student".equals(item.getAction_name())) {
//                                helper.setText(R.id.type, " 上课 ", 0xff669966);
//                                helper.getView(R.id.type).setBackgroundResource(R.drawable.notify_text_background_start);
//                            } else if ("change_time".equals(item.getAction_name())) {
//                                helper.setText(R.id.type, " 调课 ", 0xffff9900);
//                                helper.getView(R.id.type).setBackgroundResource(R.drawable.notify_text_background_change);
//                            } else {
//                                helper.setText(R.id.type, " 系统 ", 0xffC4483C);
//                                helper.getView(R.id.type).setBackgroundResource(R.drawable.notify_text_background_system);
//                            }
//                            break;
//                        case "payment/order":
//                            helper.setText(R.id.type, " 退款 ", 0xff66cccc);
//                            helper.getView(R.id.type).setBackgroundResource(R.drawable.notify_text_background_refund);
//                            break;
//                        case "action_record":
//                            helper.setText(R.id.type, " 小班课程 ", 0xff780078);
//                            blank = "\u3000\u3000\u3000\u3000  ";
//                            helper.getView(R.id.type).setBackgroundResource(R.drawable.notify_text_background_record);
//                            break;
//                        default:
//                            helper.setText(R.id.type, " 系统 ", 0xffC4483C);
//                            helper.getView(R.id.type).setBackgroundResource(R.drawable.notify_text_background_system);
//                    }
//                }
//                SpannableStringBuilder span = new SpannableStringBuilder(blank + item.getNotice_content());
//                span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, blank.length(),
//                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                TextView details = helper.getView(R.id.details);
//                details.setText(span);
//                details.setTextColor(item.isRead() ? 0xff999999 : 0xff666666);
                helper.setText(R.id.date_time, item.getCreated_at()).setText(R.id.details, item.getNotice_content(), item.isRead() ? 0xff999999 : 0xff666666);
            }
        };
        listView.setAdapter(adapter);
        View inflate = View.inflate(getActivity(), R.layout.empty_view, null);
        listView.setEmptyView(inflate);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String label = DateUtils.formatDateTime(
                                getActivity(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        // Update the LastUpdatedLabel
                        listView.getLoadingLayoutProxy(false, true)
                                .setLastUpdatedLabel(label);
                        listView.onRefreshComplete();
                    }
                }, 200);
                EventBus.getDefault().post(BusEvent.REFRESH_NOTIFICATIONS);
                initData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String label = DateUtils.formatDateTime(
                                getActivity(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        // Update the LastUpdatedLabel
                        listView.getLoadingLayoutProxy(false, true)
                                .setLastUpdatedLabel(label);
                        listView.onRefreshComplete();
                    }
                }, 200);
                initData(2);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SystemNotifyBean.DataBean item = list.get(position - 1);
                String courseId = "";
                if (item.getLink() != null) {
                    courseId = item.getLink().replace("live_studio/course:", "");
                }
                Intent intent;
                switch (item.getNotificationable_type()) {
                    case "live_studio/course":
                        if ("start".equals(item.getAction_name())) {
                            intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                            intent.putExtra("id", Integer.valueOf(courseId));
                            intent.putExtra("pager", 2);
                            startActivity(intent);
                        } else {
                            intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                            intent.putExtra("id", Integer.valueOf(courseId));
                            startActivity(intent);
                        }
                        break;
                    case "live_studio/lesson":
                        if ("start_for_student".equals(item.getAction_name())) {
                            intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                            intent.putExtra("id", Integer.valueOf(courseId));
                            startActivity(intent);
                        } else if ("change_time".equals(item.getAction_name())) {
                            intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                            intent.putExtra("id", Integer.valueOf(courseId));
                            intent.putExtra("pager", 2);
                            startActivity(intent);
                        } else {
                            intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                            intent.putExtra("id", Integer.valueOf(courseId));
                            startActivity(intent);
                        }
                        break;
                    case "payment/order":
                        intent = new Intent(getActivity(), PersonalMyOrderActivity.class);
                        startActivity(intent);
                        break;
                    case "payment/recharge":
                    case "payment/withdraw":
                        intent = new Intent(getActivity(), PersonalMyWalletActivity.class);
                        startActivity(intent);
                        break;
                    case "action_record":
                    default:
                        Toast.makeText(getActivity(), "暂无跳转内容", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onShow() {
        if (initOver) {
            page = 1;
            initData(1);
        } else {
            super.onShow();
        }
    }

    private void initData(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(BaseApplication.getInstance().getUserId()));
        map.put("page", String.valueOf(page));
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlUser + BaseApplication.getInstance().getUserId() + "/notifications", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        if (type == 1) {
                            list.clear();
                        }
                        isLoad = true;
                        SystemNotifyBean data = JsonUtils.objectFromJson(response.toString(), SystemNotifyBean.class);
                        if (data != null && data.getData() != null) {
                            list.addAll(data.getData());
                            adapter.notifyDataSetChanged();
                            StringBuilder unRead = new StringBuilder();
                            for (SystemNotifyBean.DataBean bean : data.getData()) {
                                if (!bean.isRead()) {
                                    unRead.append(bean.getId()).append(" ");
                                }
                            }
                               markNotifiesRead(unRead.toString());
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }

        });
        addToRequestQueue(request);
    }

    public void markNotifiesRead(String unRead) {
        if (unRead.length() > 0) {
            Map<String, String> map = new HashMap<>();
            map.put("ids", unRead);
            JSONObject jsonObject = new JSONObject(map);
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.urlUser + BaseApplication.getInstance().getUserId() + "/notifications/batch_read", jsonObject,
                    new VolleyListener(getActivity(  )) {
                        @Override
                        protected void onSuccess(JSONObject response) {
                        }

                        @Override
                        protected void onError(JSONObject response) {
                        }

                        @Override
                        protected void onTokenOut() {
                            tokenOut();
                        }

                    }, new VolleyErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    super.onErrorResponse(volleyError);
                }
            });
            addToRequestQueue(request);
        }
    }
}
