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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.InteractCourseDetailActivity;
import cn.qatime.player.activity.InteractiveLiveActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.PlayBackBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.MPermission;
import cn.qatime.player.utils.MPermissionUtil;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.annotation.OnMPermissionDenied;
import cn.qatime.player.utils.annotation.OnMPermissionGranted;
import cn.qatime.player.utils.annotation.OnMPermissionNeverAskAgain;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.MyInteractClassBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.NetUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentInteractStudying extends BaseFragment {
    private PullToRefreshListView listView;
    private java.util.List<MyInteractClassBean.DataBean> list = new ArrayList<>();
    private CommonAdapter<MyInteractClassBean.DataBean> adapter;
    private int page = 1;
    private MyInteractClassBean.DataBean item;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_interact_teaching, container, false);
        initview(view);
        initOver = true;
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));

        adapter = new CommonAdapter<MyInteractClassBean.DataBean>(getActivity(), list, R.layout.item_fragment_my_interact_teaching) {
            @Override
            public void convert(ViewHolder helper, final MyInteractClassBean.DataBean item, int position) {
                helper.getView(R.id.enter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentInteractStudying.this.item = item;
                        if (NetUtils.checkPermission(getActivity()).size() > 0) {
                            requestLivePermission();
                        } else {
                            toNext();
                        }
                    }
                });
                Glide.with(getActivity()).load(item.getPublicize_url()).placeholder(R.mipmap.photo).centerCrop().crossFade().into((ImageView) helper.getView(R.id.image));
                helper.setText(R.id.name, item.getName());
                helper.setText(R.id.subject, item.getSubject());
                helper.setText(R.id.teacher, "/" + item.getTeachers().get(0).getName() + (item.getTeachers().size() > 1 ? "..." : ""));
                helper.setText(R.id.progress, getString(R.string.progress, item.getClosed_lessons_count(), item.getLessons_count()));
                helper.setText(R.id.grade, item.getGrade());
            }
        };
        listView.setAdapter(adapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                initData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                initData(2);
            }

        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), InteractCourseDetailActivity.class);
                intent.putExtra("id", list.get(position - 1).getId());
                startActivity(intent);
            }
        });
    }

    private void toNext() {
        Intent intent = new Intent(getActivity(), InteractiveLiveActivity.class);
        intent.putExtra("id", item.getId());
        intent.putExtra("teamId", item.getChat_team_id());
        startActivity(intent);
    }

    private void requestLivePermission() {
        MPermission.with(this)
                .addRequestCode(100)
                .permissions(NetUtils.checkPermission(getActivity()).toArray(new String[NetUtils.checkPermission(getActivity()).size()]))
                .request();
    }

    @OnMPermissionGranted(100)
    public void onLivePermissionGranted() {
//        Toast.makeText(InteractiveLiveActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
        toNext();
    }

    @OnMPermissionDenied(100)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, NetUtils.checkPermission(getActivity()).toArray(new String[NetUtils.checkPermission(getActivity()).size()]));
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法开启直播";
        Toast.makeText(getActivity(), tip, Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionNeverAskAgain(100)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, NetUtils.checkPermission(getActivity()).toArray(new String[NetUtils.checkPermission(getActivity()).size()]));
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, NetUtils.checkPermission(getActivity()).toArray(new String[NetUtils.checkPermission(getActivity()).size()]));
        StringBuilder sb = new StringBuilder();
        sb.append("无法开启直播，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }

        Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG).show();
    }

    public void onShow() {
        if (!isLoad) {
            if (initOver) {
                initData(1);
            } else {
                super.onShow();
            }
        }
    }

    /**
     * @param type 1刷新
     *             2加载更多
     */
    private void initData(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("per_page", "10");

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlMyRemedialClass + BaseApplication.getUserId() + "/interactive_courses", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        isLoad = true;
                        Logger.e(response.toString());
                        if (type == 1) {
                            list.clear();
                        }
                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                        listView.onRefreshComplete();

                        try {
                            MyInteractClassBean data = JsonUtils.objectFromJson(response.toString(), MyInteractClassBean.class);
                            if (data != null) {
                                for (MyInteractClassBean.DataBean item : data.getData()) {
                                    if (!"completed".equals(item.getStatus())) {//只显示未结束
                                        list.add(item);
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
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

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                listView.onRefreshComplete();
            }
        });
        addToRequestQueue(request);
    }
}
