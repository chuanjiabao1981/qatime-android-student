package cn.qatime.player.activity;

import android.os.Bundle;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * @author Tianhaoranly
 * @date 2016/10/17 17:22
 * @Description:
 */
public class WithdrawResultActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_result);
        setTitle(getResourceString(R.string.withdraw_cash_result));
    }
}
