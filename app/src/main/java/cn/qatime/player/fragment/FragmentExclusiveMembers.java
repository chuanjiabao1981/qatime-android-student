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
import cn.qatime.player.adapter.FragmentNEVideoPlayerAdapter;
import cn.qatime.player.base.BaseFragment;
import libraryextra.bean.ChatTeamBean;
import libraryextra.utils.PinyinUtils;
import libraryextra.utils.StringUtils;

public class FragmentExclusiveMembers extends BaseFragment {
    private List<ChatTeamBean.Accounts> list = new ArrayList<>();
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

    /**
     * @param accounts
     */
    public void setData(List<ChatTeamBean.Accounts> accounts) {
        if (accounts != null) {
            list.clear();
            list.addAll(accounts);
            for (ChatTeamBean.Accounts item : list) {
                if (item == null) continue;
//                if (!StringUtils.isNullOrBlanK(accounts.getOwner())) {
//                    if (accounts.getOwner().equals(item.getAccid())) {
//                        item.setOwner(true);
//                    } else {
//                        item.setOwner(false);
//                    }
//                }
                if (StringUtils.isNullOrBlanK(item.getName())) {
                    item.setFirstLetters("");
                } else {
                    item.setFirstLetters(PinyinUtils.getPinyinFirstLetters(item.getName()));
                }
            }
            Collections.sort(list, new Comparator<ChatTeamBean.Accounts>() {
                @Override
                public int compare(ChatTeamBean.Accounts lhs, ChatTeamBean.Accounts rhs) {
//                    int x = 0;
//                    if (lhs.isOwner() && !rhs.isOwner()) {
//                        x = -3;
//                    } else if (!lhs.isOwner() && rhs.isOwner()) {
//                        x = 3;
//                    } else if (lhs.isOwner() && rhs.isOwner()) {
//                        x = -3;
//                    }

//                    int y = lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
//                    if (x == 0) {
//                        return y;
//                    }
//                    return x;
                    return lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
                }
            });
            hd.postDelayed(runnable, 200);
        }
    }

    public void setOnlineInfo(List<String> online_users) {

    }
}
