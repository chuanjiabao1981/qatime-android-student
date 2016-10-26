package cn.qatime.player.activity;

import android.os.Bundle;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * @author Tianhaoranly
 * @date 2016/10/26 13:52
 * @Description: 教师详情页
 */
public class TeacherDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        setTitle("名师简介");
    }
}