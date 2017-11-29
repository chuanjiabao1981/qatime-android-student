package cn.qatime.player.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.FragmentNEVideoPlayerAdapter;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.MembersBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.PinyinUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class FragmentExclusiveMembers extends BaseFragment {
    private List<MembersBean.DataBean.Members> list = new ArrayList<>();
    private FragmentNEVideoPlayerAdapter adapter;
    private Handler hd = new Handler();
    private boolean hasLoad = false;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (hasLoad) {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                    hd.removeCallbacks(this);
                } else {
                    hd.postDelayed(this, 200);
                }
            } else {
                hd.postDelayed(this, 200);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_player_members, null);

        ListView listView = (ListView) view.findViewById(R.id.listview);
        adapter = new FragmentNEVideoPlayerAdapter(getActivity(), list, R.layout.item_fragment_nevideo_player);
        listView.setAdapter(adapter);
        hasLoad = true;
        return view;
    }

    public void setData(String id) {
        DaYiJsonObjectRequest requestMember = new DaYiJsonObjectRequest(String.format(UrlUtils.urlUsersTeamsMember, BaseApplication.getInstance().getUserId(), id), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        MembersBean bean = JsonUtils.objectFromJson(response.toString(), MembersBean.class);
                        if (bean != null && bean.getData() != null && !StringUtils.isNullOrBlanK(bean.getData().getMembers())) {
                            setDatas(bean.getData().getMembers());
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
        addToRequestQueue(requestMember);

    }

    private void setDatas(List<MembersBean.DataBean.Members> accounts) {
        if (accounts != null) {
            list.clear();
            list.addAll(accounts);
            for (MembersBean.DataBean.Members item : list) {
                if (item == null) continue;
                if (StringUtils.isNullOrBlanK(item.getName())) {
                    item.setFirstLetters("");
                } else {
                    item.setFirstLetters(PinyinUtils.getPinyinFirstLetters(item.getName()));
                }
            }
            Collections.sort(list, new Comparator<MembersBean.DataBean.Members>() {
                @Override
                public int compare(MembersBean.DataBean.Members lhs, MembersBean.DataBean.Members rhs) {
                    int x = 0;
                    if (lhs.isOwner() && !rhs.isOwner()) {
                        x = -3;
                    } else if (!lhs.isOwner() && rhs.isOwner()) {
                        x = 3;
                    } else if (lhs.isOwner() && rhs.isOwner()) {
                        x = -3;
                    }

                    int y = lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
                    if (x == 0) {
                        return y;
                    }
                    return x;
//                    return lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
                }
            });
            hd.postDelayed(runnable, 200);
        }
    }
}
