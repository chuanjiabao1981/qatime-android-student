package cn.qatime.player.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.im.SimpleCallback;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.helper.AnnouncementHelper;
import cn.qatime.player.im.model.Announcement;
import cn.qatime.player.utils.DateUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import cn.qatime.player.base.BaseFragment;
import libraryextra.utils.StringUtils;

public class FragmentNEVideoPlayer1 extends BaseFragment {
    private int page = 0;
    private PullToRefreshListView listView;
    private CommonAdapter<Announcement> adapter;
    private List<Announcement> items = new ArrayList<>();
    private String announce;
    private String teamId;

    private boolean isMember = false;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private boolean hasLoad = false;
    private Handler hd = new Handler();
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
        View view = View.inflate(getActivity(), R.layout.fragment_nevideo_player1, null);
        initview(view);
        hasLoad = true;
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));


        adapter = new CommonAdapter<Announcement>(getActivity(), items, R.layout.item_fragment_nevideo_player1) {
            @Override
            public void convert(ViewHolder helper, Announcement item, int position) {
                if (position == 0) {
                    helper.getView(R.id.late).setVisibility(View.VISIBLE);
                    ((TextView) helper.getView(R.id.time)).setTextColor(0xff151515);
                    ((TextView) helper.getView(R.id.describe)).setTextColor(0xff151515);
                } else {
                    helper.getView(R.id.late).setVisibility(View.GONE);
                    ((TextView) helper.getView(R.id.time)).setTextColor(0xff545454);
                    ((TextView) helper.getView(R.id.describe)).setTextColor(0xff545454);
                }
                helper.setText(R.id.time, DateUtils.getTimeShowString(item.getTime() * 1000, false));
                helper.setText(R.id.describe, item.getContent());
            }
        };
        listView.setAdapter(adapter);

    }

    /**
     * 请求群信息
     */
    public void setTeamId(String teamId) {
        this.teamId = teamId;
        Team t = TeamDataCache.getInstance().getTeamById(teamId);
        if (t != null) {
            updateAnnounceInfo(t);
        } else {
            TeamDataCache.getInstance().fetchTeamById(teamId, new SimpleCallback<Team>() {
                @Override
                public void onResult(boolean success, Team result) {
                    if (success && result != null) {
                        updateAnnounceInfo(result);
                    }
                }
            });
        }
        requestMemberData();

    }

    private void requestMemberData() {
        TeamMember teamMember = TeamDataCache.getInstance().getTeamMember(teamId, BaseApplication.getAccount());
        if (teamMember != null) {
            updateTeamMember(teamMember);
        } else {
            // 请求群成员
            TeamDataCache.getInstance().fetchTeamMember(teamId, BaseApplication.getAccount(), new SimpleCallback<TeamMember>() {
                @Override
                public void onResult(boolean success, TeamMember member) {
                    if (success && member != null) {
                        updateTeamMember(member);
                    }
                }
            });
        }
    }

    /**
     * 判断是否是普通成员
     *
     * @param teamMember 群成员
     */
    private void updateTeamMember(TeamMember teamMember) {
        if (teamMember.getType() == TeamMemberType.Normal) {
            isMember = true;
        }
    }

    /**
     * 更新公告信息
     *
     * @param team 群
     */
    private void updateAnnounceInfo(Team team) {
        if (team == null) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.team_not_exist), Toast.LENGTH_SHORT).show();
        } else {
            announce = team.getAnnouncement();
            setAnnounceItem();
        }
    }

    private void setAnnounceItem() {
        if (StringUtils.isNullOrBlanK(announce)) {
            //TODO 空数据提示
            return;
        }
        Logger.e(announce);
        List<Announcement> list = AnnouncementHelper.getAnnouncements(teamId, announce, isMember ? 5 : Integer.MAX_VALUE);
        if (list == null || list.isEmpty()) {
            return;
        }
        Logger.e("size" + items.size());
        items.clear();
        items.addAll(list);
        hd.postDelayed(runnable, 200);
    }

}
