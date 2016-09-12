package cn.qatime.player.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.MessageActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.MessageListBean;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.observer.UserInfoObservable;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.TutorialClassBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author luntify
 * @date 2016/8/15 20:05
 * @Description 辅导班消息
 */
public class FragmentNews1 extends BaseFragment {

    public static final long RECENT_TAG_STICKY = 1; // 联系人置顶tag

    private ArrayList<MessageListBean> items;
    private CommonAdapter<MessageListBean> adapter;
    private PullToRefreshListView listView;
    private boolean msgLoaded = false;
    private List<RecentContact> loadedRecents;
    private UserInfoObservable.UserInfoObserver userInfoObserver;
    private UserInfoObservable userInfoObservable;
    private TutorialClassBean courses;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_news1, null);
        initView(view);
        getCourses();
        return view;
    }

    private void getCourses() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlMyRemedialClass + BaseApplication.getUserId() + "/courses", null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        try {
                            Logger.e(response.toString());
                            courses = JsonUtils.objectFromJson(response.toString(), TutorialClassBean.class);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
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

    private void initView(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.getRefreshableView().setDividerHeight(1);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initMessageList();
        requestMessages(true);
        registerObservers(true);
    }

    /**
     * 初始化消息列表
     */
    private void initMessageList() {
        items = new ArrayList<>();

        adapter = new CommonAdapter<MessageListBean>(getActivity(), items, R.layout.item_fragment_news1) {
            @Override
            public void convert(ViewHolder holder, MessageListBean item, int position) {
                if (item.getSessionType() == SessionTypeEnum.Team) {
                    ((TextView) holder.getView(R.id.name)).setMaxWidth((int) (ScreenUtils.getScreenWidth(getActivity()) * 0.8));
                    holder.setText(R.id.name, item.getContent().replace("讨论组", ""));
                }
                ((ImageView) holder.getView(R.id.notify)).setVisibility(item.isMute() ? View.VISIBLE : View.GONE);
                holder.getView(R.id.count).setVisibility(item.getUnreadCount() == 0 ? View.GONE : View.VISIBLE);
                holder.setText(R.id.count, String.valueOf(item.getUnreadCount()));

            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtra("sessionId", items.get(position - 1).getContactId());
                intent.putExtra("sessionType", items.get(position - 1).getSessionType());
                intent.putExtra("courseId", items.get(position - 1).getCourseId());
                intent.putExtra("pull_address", items.get(position - 1).getPull_address());
                intent.putExtra("name", items.get(position - 1).getContent());
                startActivity(intent);
            }
        });

        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (NIMClient.getStatus() == StatusCode.LOGINED) {
                    final Dialog dialog = new Dialog(getActivity(), R.style.Transparent);
                    final View v = View.inflate(getActivity(), R.layout.team_notify_alert_dialog, null);
                    ((TextView) v.findViewById(R.id.text)).setText(items.get(position - 1).isMute() ? getResourceString(R.string.resume_alert) : getResourceString(R.string.nolongger_alert));
                    v.findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            NIMClient.getService(TeamService.class).muteTeam(items.get(position - 1).getContactId(), !items.get(position - 1).isMute()).setCallback(new RequestCallback<Void>() {
                                @Override
                                public void onSuccess(Void param) {
                                    Team team = TeamDataCache.getInstance().getTeamById(items.get(position - 1).getContactId());
                                    items.get(position - 1).setMute(team.mute());
//                                notificationConfigText.setText(team.mute() ? getString(R.string.close) : getString(R.string.open));
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailed(int code) {
                                    Logger.e("muteTeam failed code:" + code);
                                }

                                @Override
                                public void onException(Throwable exception) {

                                }
                            });
                        }
                    });
                    v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setContentView(v);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
//                    v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                        @Override
//                        public void onGlobalLayout() {
//                            Window window = dialog.getWindow();
//                            WindowManager.LayoutParams lp = window.getAttributes();
//                            lp.gravity = Gravity.CENTER;
//                            lp.width = v.getWidth();
//                            lp.height = v.getHeight();
//                            dialog.getWindow().setAttributes(lp);
//                        }
//                    });
                    return true;
                }
                return false;
            }
        });
    }


    private void requestMessages(boolean delay) {
        if (msgLoaded) {
            return;
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (msgLoaded) {
                    return;
                }
                // 查询最近联系人列表数据
                NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                            return;
                        }
                        loadedRecents = recents;

                        // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                        //
                        msgLoaded = true;
                        if (isAdded()) {
                            onRecentContactsLoaded();
                        }
                    }
                });
            }
        }, delay ? 1000 : 0);
    }

    private void onRecentContactsLoaded() {
        items.clear();
        if (courses != null && courses.getData() != null) {
            for (TutorialClassBean.Data data : courses.getData()) {
                for (RecentContact item : loadedRecents) {
                    if (data.getChat_team_id().equals(item.getContactId())) {
                        Team team = TeamDataCache.getInstance().getTeamById(item.getContactId());
                        MessageListBean bean = new MessageListBean();
                        bean.setMute(team.mute());
                        bean.setContactId(item.getContactId());
                        bean.setSessionType(item.getSessionType());
                        bean.setContent(data.getName());
                        if (StringUtils.isNullOrBlanK(bean.getContent())) {
                            bean.setContent(item.getContent().replace("讨论组", ""));
                        }
                        bean.setCourseId(data.getId());
                        bean.setUnreadCount(item.getUnreadCount());
                        bean.setPull_address(data.getPull_address());
                        bean.setTime(item.getTime());
                        bean.setRecentMessageId(item.getRecentMessageId());
                        items.add(bean);
                    }
                }
            }
        } else {
            getCourses();
            for (RecentContact item : loadedRecents) {
//                    if (data.getChat_team_id().equals(item.getContactId())) {
                Team team = TeamDataCache.getInstance().getTeamById(item.getContactId());
                MessageListBean bean = new MessageListBean();
                bean.setMute(team.mute());
                bean.setContactId(item.getContactId());
                bean.setSessionType(item.getSessionType());
                bean.setContent(item.getContent());
                bean.setUnreadCount(item.getUnreadCount());
                bean.setRecentMessageId(item.getRecentMessageId());
//                        bean.setPull_address(data.getPull_address());
                bean.setTime(item.getTime());
                items.add(bean);
//                    }
            }
        }
        if (loadedRecents != null) {
//            items.addAll(loadedRecents);
            loadedRecents = null;
        }
        refreshMessages(true);
    }

    private void refreshMessages(boolean unreadChanged) {
        sortRecentContacts(items);
        adapter.notifyDataSetChanged();

        if (unreadChanged) {

            // 方式一：累加每个最近联系人的未读（快）
            /*
            int unreadNum = 0;
            for (RecentContact r : items) {
                unreadNum += r.getUnreadCount();
            }
            */

            // 方式二：直接从SDK读取（相对慢）
            int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();

//            if (callback != null) {
//                callback.onUnreadCountChange(unreadNum);
//            }
        }
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortRecentContacts(List<MessageListBean> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<MessageListBean> comp = new Comparator<MessageListBean>() {

        @Override
        public int compare(MessageListBean o1, MessageListBean o2) {
            // 先比较置顶tag
//            long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
//            if (sticky != 0) {
//                return sticky > 0 ? -1 : 1;
//            } else {
            long time = o1.getTime() - o2.getTime();
            return time == 0 ? 0 : (time > 0 ? -1 : 1);
//            }
        }
    };


    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeRecentContact(messageObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);
        registerTeamUpdateObserver(register);
        registerTeamMemberUpdateObserver(register);
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
    }

    private void registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = new UserInfoObservable.UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    refreshMessages(false);
                }
            };
        }

        if (userInfoObservable == null) {
            userInfoObservable = new UserInfoObservable(getContext());
        }
        userInfoObservable.registerObserver(userInfoObserver);
    }

    private void unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            if (userInfoObservable != null) {
                userInfoObservable.unregisterObserver(userInfoObserver);
            }
        }
    }

    /**
     * 监听用户在线状态
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
//                kickOut(code);
                Logger.e("未登录成功");
            } else {
                if (code == StatusCode.NET_BROKEN) {
                    Logger.e("当前网络不可用");
                } else if (code == StatusCode.UNLOGIN) {
                    Logger.e("未登录");
                } else if (code == StatusCode.CONNECTING) {
                    Logger.e("连接中...");
                } else if (code == StatusCode.LOGINING) {
                    Logger.e("登录中...");
                } else {
//                    onRecentContactsLoaded();
                    Logger.e("其他" + code);
                }
            }
        }
    };

    /**
     * 注册群信息&群成员更新监听
     */
    private void registerTeamUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamDataChangedObserver(teamDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamDataChangedObserver(teamDataChangedObserver);
        }
    }

    private void registerTeamMemberUpdateObserver(boolean register) {
        if (register) {
            TeamDataCache.getInstance().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        } else {
            TeamDataCache.getInstance().unregisterTeamMemberDataChangedObserver(teamMemberDataChangedObserver);
        }
    }

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> messages) {
            int index;
            for (RecentContact msg : messages) {
                index = -1;
                for (int i = 0; i < items.size(); i++) {
                    if (msg.getContactId().equals(items.get(i).getContactId())
                            && msg.getSessionType() == (items.get(i).getSessionType())) {
                        index = i;
                        break;
                    }
                }

                MessageListBean bean = new MessageListBean();
                if (index >= 0) {
                    bean = items.get(index);
                    boolean haveData = false;
                    if (courses != null && courses.getData() != null) {
                        for (TutorialClassBean.Data data : courses.getData()) {
                            if (data.getChat_team_id().equals(bean.getContactId())) {
                                haveData = true;
                            }
                        }
                        if (!haveData) {
                            getCourses();
                        }
                    }
                    items.remove(index);
                } else {
                    if (courses != null && courses.getData() != null) {
                        for (TutorialClassBean.Data data : courses.getData()) {
                            if (data.getChat_team_id().equals(msg.getContactId())) {
                                bean.setContent(data.getName());
                                bean.setPull_address(data.getPull_address());
                            }
                        }
                    } else {
                        getCourses();
                    }
                }
                bean.setContactId(msg.getContactId());
                Team team = TeamDataCache.getInstance().getTeamById(bean.getContactId());
                bean.setMute(team.mute());
                bean.setSessionType(msg.getSessionType());
                if (StringUtils.isNullOrBlanK(bean.getContent())) {
                    bean.setContent(msg.getContent().replace("讨论组", ""));
                }
                bean.setUnreadCount(msg.getUnreadCount());
                bean.setTime(msg.getTime());
                bean.setRecentMessageId(msg.getRecentMessageId());
                items.add(bean);
            }

            refreshMessages(true);
        }
    };

    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                MessageListBean item = items.get(index);
                item.setMsgStatus(message.getStatus());
            }
        }
    };

    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
        @Override
        public void onEvent(RecentContact recentContact) {
            if (recentContact != null) {
                for (MessageListBean item : items) {
                    if (TextUtils.equals(item.getContactId(), recentContact.getContactId())
                            && item.getSessionType() == recentContact.getSessionType()) {
                        items.remove(item);
                        refreshMessages(true);
                        break;
                    }
                }
            } else {
                items.clear();
                refreshMessages(true);
            }
        }
    };
    TeamDataCache.TeamDataChangedObserver teamDataChangedObserver = new TeamDataCache.TeamDataChangedObserver() {

        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeam(Team team) {

        }
    };

    TeamDataCache.TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamDataCache.TeamMemberDataChangedObserver() {
        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeamMember(TeamMember member) {

        }
    };


    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            MessageListBean item = items.get(i);
            if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
                return i;
            }
        }

        return -1;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }
}
