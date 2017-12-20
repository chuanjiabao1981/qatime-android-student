package cn.qatime.player.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.flow.BasicDispatcher;
import cn.qatime.player.flow.BasicKeyParceler;
import cn.qatime.player.flow.screen.FlowExplainScreen;
import flow.Flow;

/**
 * @author luntify
 * @date 2017/12/1 10:43
 * @Description 考试考题页面
 */

public class ExaminationIngActivity extends BaseFragmentActivity {
//    private ViewPager viewPager;
//    private ExamFragmentStatePagerAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = Flow.configure(newBase, this)
                .dispatcher(new BasicDispatcher(this))
                .defaultKey(new FlowExplainScreen(0))
                .keyParceler(new BasicKeyParceler())
                .install();
        super.attachBaseContext(newBase);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_ing);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
