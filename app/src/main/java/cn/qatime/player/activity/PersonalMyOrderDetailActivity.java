package cn.qatime.player.activity;

import android.os.Bundle;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;


public class PersonalMyOrderDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_order_detail);
        setTitle(getResources().getString(R.string.detail_of_order));
    }
}

