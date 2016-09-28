package cn.qatime.player.activity;

import android.os.Bundle;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * @author Tianhaoranly
 * @date 2016/9/28 10:02
 * @Description:
 */
public class RechargeProcessActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_process);
        setTitle(getResourceString(R.string.recharge_process));
    }
}
