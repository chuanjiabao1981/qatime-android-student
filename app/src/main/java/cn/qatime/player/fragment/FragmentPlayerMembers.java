package cn.qatime.player.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.FragmentNEVideoPlayerAdapter4;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.Announcements;
import libraryextra.utils.StringUtils;

public class FragmentPlayerMembers extends BaseFragment {
    private ListView listView;
    private List<Announcements.DataBean.MembersBean> list = new ArrayList<>();
    private FragmentNEVideoPlayerAdapter4 adapter;
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

        listView = (ListView) view.findViewById(R.id.listview);
        adapter = new FragmentNEVideoPlayerAdapter4(getActivity(), list, R.layout.item_fragment_nevideo_player4);
        listView.setAdapter(adapter);
        hasLoad = true;
        return view;
    }

    /**
     * @param accounts
     */
    public void setData(Announcements.DataBean accounts) {
        if (accounts != null) {
            list.clear();
            list.addAll(accounts.getMembers());
            for (Announcements.DataBean.MembersBean item : list) {
                if (!StringUtils.isNullOrBlanK(accounts.getOwner())) {
                    if (accounts.getOwner().equals(item.getAccid())) {
                        item.setOwner(true);
                    } else {
                        item.setOwner(false);
                    }
                }
                if (StringUtils.isNullOrBlanK(item.getName())) {
                    item.setFirstLetters("");
                } else {
                    item.setFirstLetters(StringUtils.getPYIndexStr(item.getName().substring(0, 1)));
                }
            }
            Collections.sort(list, new Comparator<Announcements.DataBean.MembersBean>() {
                @Override
                public int compare(Announcements.DataBean.MembersBean lhs, Announcements.DataBean.MembersBean rhs) {
                    return rhs.isOwner() ? 1 : lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
                }
            });
            hd.postDelayed(runnable, 200);
        }
    }
}
