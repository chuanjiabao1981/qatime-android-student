package cn.qatime.player.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import libraryextra.view.FragmentLayoutWithLine;

public class Fragment3 extends BaseFragment {
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

        fragBaseFragments.add(new FragmentNews1());
        fragBaseFragments.add(new FragmentNews2());


        fragmentlayout = (FragmentLayoutWithLine) view.findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4,0xffff9999);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff666666);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
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
            ((FragmentNews1) fragBaseFragments.get(0)).setMessage(message);

        }
    }
}
