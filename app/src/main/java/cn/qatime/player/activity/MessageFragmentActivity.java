package cn.qatime.player.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.fragment.MessageChatNewsF;
import cn.qatime.player.fragment.MessageNotifyNewsF;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * @author Tianhaoranly
 * @date 2016/10/26 14:40
 * @Description:
 */
public class MessageFragmentActivity extends BaseFragmentActivity {
    FragmentLayoutWithLine fragmentlayout;
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_fragment);
        initview();
    }

    private void initview() {
//        Button roll = (Button) findViewById(R.id.roll);
//        roll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), NEVideoPlayerActivity.class);
//                startActivity(intent);
//            }
//        });

        fragBaseFragments.add(new MessageChatNewsF());
        fragBaseFragments.add(new MessageNotifyNewsF());


        fragmentlayout = (FragmentLayoutWithLine)findViewById(R.id.fragmentlayout);

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
