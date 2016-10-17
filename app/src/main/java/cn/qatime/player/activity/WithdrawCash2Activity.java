package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * @author Tianhaoranly
 * @date 2016/10/17 16:13
 * @Description:
 */
public class WithdrawCash2Activity extends BaseActivity {
    private EditText account;
    private EditText name;
    private Button withdrawCashNow;
    private String amount;
    private String type;

    private void assignViews() {
        account = (EditText) findViewById(R.id.account);
        name = (EditText) findViewById(R.id.name);
        withdrawCashNow = (Button) findViewById(R.id.withdraw_cash_now);

        amount = getIntent().getStringExtra("amount");
        type = getIntent().getStringExtra("type");

        if ("alipay".equals(type)){
            setTitle(getResourceString(R.string.withdraw_cash_to_alipay));
            account.setHint(getResourceString(R.string.hint_input_alipay_account));
        }else {
            account.setHint(getResourceString(R.string.hint_input_bank_card_account));
            setTitle(getResourceString(R.string.withdraw_cash_to_bank));
        }

        withdrawCashNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/10/17 提现接口
                Intent intent = new Intent(WithdrawCash2Activity.this,WithdrawConfirmActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw2_cash);
        assignViews();
    }
}
