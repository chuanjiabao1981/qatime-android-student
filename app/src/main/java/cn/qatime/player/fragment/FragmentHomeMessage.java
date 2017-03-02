package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.utils.StringUtils;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * @author lungtify
 * @Time 2017/3/1 11:03
 * @Describe
 */

public class FragmentHomeMessage extends BaseFragment {
    FragmentLayoutWithLine fragmentlayout;
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private int currentPosition = 0;
    Observer<List<RecentContact>> messageObserver =
            new Observer<List<RecentContact>>() {
                @Override
                public void onEvent(List<RecentContact> messages) {
                    if (currentPosition == 1) {
                        fragmentlayout.getTabLayout().findViewById(R.id.flag1).setVisibility(View.VISIBLE);
                    }
                }
            };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_message, container, false);
        assignViews(view);
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, true);
        EventBus.getDefault().register(this);
        return view;
    }

    private void assignViews(View view) {
        fragBaseFragments.add(new FragmentMessageChatNews());
        fragBaseFragments.add(new FragmentMessageNotifyNews());


        fragmentlayout = (FragmentLayoutWithLine) view.findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4, 0xffbe0b0b);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
                ((BaseFragment) fragBaseFragments.get(position)).onShow();
                currentPosition = position;
                if (position == 0) {
                    currentTabView.findViewById(R.id.flag1).setVisibility(View.INVISIBLE);
                } else if (position == 1) {
                    currentTabView.findViewById(R.id.flag2).setVisibility(View.INVISIBLE);
                }
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_fragment_news, 0x0912);
        fragmentlayout.getViewPager().setOffscreenPageLimit(2);
    }

    @Subscribe(sticky = true)
    public void onEvent(String msg) {
        if (!StringUtils.isNullOrBlanK(msg) && "handleUPushMessage".equals(msg)) {
            EventBus.getDefault().removeStickyEvent(msg);
            fragmentlayout.getTabLayout().findViewById(R.id.flag2).setVisibility(View.VISIBLE);
        }else if(!StringUtils.isNullOrBlanK(msg) && "refreshNotifications".equals(msg)){
            //系统消息刷新event flag2消失
            fragmentlayout.getTabLayout().findViewById(R.id.flag2).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  注册/注销观察者
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, false);
        EventBus.getDefault().unregister(this);
    }

    public void setMessage(Intent intent) {
        /**     * 解析通知栏发来的云信消息     */
        if (intent != null && intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            ArrayList<IMMessage> messages = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            if (messages != null && messages.size() == 1) {
                final IMMessage message = messages.get(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (message != null) {
                            if (fragmentlayout != null) {
                                fragmentlayout.setCurrenItem(0);
                            }
                            if (((FragmentMessageChatNews) fragBaseFragments.get(0)) != null) {
                                ((FragmentMessageChatNews) fragBaseFragments.get(0)).setMessage(message);
                            }
                        }
                    }
                }, 500);
            }
        } else if (intent != null && intent.hasExtra("type") && intent.getStringExtra("type").equals("system_message")) {//转到系统消息页面
            if (fragmentlayout != null) {
                fragmentlayout.setCurrenItem(1);
            }
        }
    }
}
