package cn.qatime.player.activity;

import android.os.Bundle;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * Created by lenovo on 2016/8/22.
 */
public class LearningProcessActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

    }


    private void initView() {
        setContentView(R.layout.activity_learning_process);
        setTitle(getResourceString(R.string.learning_process));
    }
}
