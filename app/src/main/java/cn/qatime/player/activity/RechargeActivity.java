package cn.qatime.player.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * @author Tianhaoranly
 * @date 2016/9/27 18:33
 * @Description:
 */
public class RechargeActivity extends BaseActivity{
    private RadioGroup radioGroup;
    private RadioButton wechatPay;
    private RadioButton alipay;
    private EditText rechargeNum;

    private void assignViews() {
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        wechatPay = (RadioButton) findViewById(R.id.wechat_pay);
        alipay = (RadioButton) findViewById(R.id.alipay);
        rechargeNum = (EditText) findViewById(R.id.recharge_num);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_activity);
        setTitle(getResourceString(R.string.recharge_choice));
        assignViews();
    }
}
