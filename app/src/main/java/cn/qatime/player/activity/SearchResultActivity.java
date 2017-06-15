package cn.qatime.player.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.fragment.FragmentSearchCourse;
import cn.qatime.player.fragment.FragmentSearchTeacher;
import libraryextra.utils.KeyBoardUtils;
import libraryextra.utils.StringUtils;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * @author Tianhaoranly
 * @date 2017/6/9 13:59
 * @Description:
 */
public class SearchResultActivity extends BaseActivity {

    private EditText edit;
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2};
    private TextView text1;
    private TextView text2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity);
        initView();
        String search_key = getIntent().getStringExtra("search_key");
        edit = (EditText) findViewById(R.id.edit_search);
        edit.setText(search_key);
        findViewById(R.id.right_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.clearFocus();
                String s = edit.getText().toString();
                if (!StringUtils.isNullOrBlanK(s)) {
                    search(s);
                }
            }
        });
        search(search_key);
    }

    private void initView() {
        fragBaseFragments.add(new FragmentSearchCourse());
        fragBaseFragments.add(new FragmentSearchTeacher());

        fragmentlayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);
        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4, 0xffff5842);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
                ((BaseFragment) fragBaseFragments.get(position)).onShow();
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_home_search, 0x0311);
        fragmentlayout.getViewPager().setOffscreenPageLimit(2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentlayout.setCurrenItem(0);
            }
        }, 200);

        text1 = (TextView) fragmentlayout.getTabLayout().findViewById(R.id.tab_text1);
        text2 = (TextView) fragmentlayout.getTabLayout().findViewById(R.id.tab_text2);
    }

    private void search(String search_key) {
        KeyBoardUtils.closeKeybord(this);
        ((FragmentSearchCourse) fragBaseFragments.get(0)).search(search_key);
        ((FragmentSearchTeacher) fragBaseFragments.get(1)).search(search_key);
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


    public void setCourseCount(int courseCount) {
        text1.setText("课程(" + courseCount + ")");
    }

    public void setTeacherCount(int teacherCount) {
        text2.setText("教师(" + teacherCount + ")");
    }
}
