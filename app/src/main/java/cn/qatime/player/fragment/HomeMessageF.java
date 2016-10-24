package cn.qatime.player.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.view.FragmentLayoutWithLine;

public class HomeMessageF extends BaseFragment {
    FragmentLayoutWithLine fragmentlayout;
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);
        initview(view);
        return view;
    }

    private void initview(View view) {
//        Button roll = (Button) view.findViewById(R.id.roll);
//        roll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), NEVideoPlayerActivity.class);
//                startActivity(intent);
//            }
//        });

        fragBaseFragments.add(new MessageChatNewsF());
        fragBaseFragments.add(new MessageNotifyNewsF());


        fragmentlayout = (FragmentLayoutWithLine) view.findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4,0xffff9999);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
                ((BaseFragment)fragBaseFragments.get(position)).onShow();
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_fragment_news, 0x0912);

    }

    /**
     * 云信发来的sessionid
     *
     * @param message 会话
     */
    public void setMessage(IMMessage message) {
        if (fragBaseFragments != null && fragBaseFragments.size() > 0) {
            ((MessageChatNewsF) fragBaseFragments.get(0)).setMessage(message);

        }
    }

    /**
     * 转到系统消息页面
     */
    public void toSystemMessage() {
        if (fragmentlayout != null) {
            fragmentlayout.setCurrenItem(1);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (fragmentlayout != null) {
                        fragmentlayout.setCurrenItem(1);
                    }
                }
            },800);
        }
    }
}
