package cn.qatime.player.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.fragment.FragmentMyTasteLive;
import cn.qatime.player.fragment.FragmentMyTasteVideo;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * 我的一对一
 */
public class PersonalMyTasteActivity extends BaseFragmentActivity {
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_taste);
        setTitles(getResources().getString(R.string.my_taste));

//        setRightImage(R.mipmap.my_taste_record, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PersonalMyTasteActivity.this, AuditionRecordsActivity.class);
//                startActivity(intent);
//            }
//        });
        initView();
    }


    private void initView() {

        fragBaseFragments.add(new FragmentMyTasteLive());
        fragBaseFragments.add(new FragmentMyTasteVideo());

        fragmentlayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);
        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4, getResources().getColor(R.color.colorPrimary));
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);

//                if (position == 4) {
//                    findViewById(R.id.right).setVisibility(View.VISIBLE);
//                } else {
//                    findViewById(R.id.right).setVisibility(View.GONE);
//                }
                ((BaseFragment) fragBaseFragments.get(position)).onShow();
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_personal_my_taste, 0x0311);
        fragmentlayout.getViewPager().setOffscreenPageLimit(2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((BaseFragment) fragBaseFragments.get(0)).onShow();
            }
        }, 200);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
