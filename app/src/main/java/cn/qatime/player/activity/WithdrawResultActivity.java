package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;

/**
 * @author Tianhaoranly
 * @date 2016/10/17 17:05
 * @Description:
 */
public class WithdrawResultActivity extends BaseActivity implements View.OnClickListener {
    private TextView id;
    private TextView time;
    private TextView mode;
    private TextView amount;
    //    private TextView commission;
    private Button withdrawCashConfirm;
    private TextView phone;
    private AlertDialog alertDialogPhone;

    private void assignViews() {
        id = (TextView) findViewById(R.id.id);
        time = (TextView) findViewById(R.id.time);
        mode = (TextView) findViewById(R.id.mode);
        amount = (TextView) findViewById(R.id.amount);
//        commission = (TextView) findViewById(R.id.commission);
        amount.setText("￥" + getIntent().getStringExtra("amount"));
        mode.setText(getPayType(getIntent().getStringExtra("pay_type")));
        id.setText(getIntent().getStringExtra("id"));
        SimpleDateFormat parseISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            time.setText(parse.format(parseISO.parse(getIntent().getStringExtra("create_at"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        withdrawCashConfirm = (Button) findViewById(R.id.withdraw_cash_confirm);
        phone = (TextView) findViewById(R.id.phone);
        phone.setText(Constant.phoneNumber);
        phone.setOnClickListener(this);
        withdrawCashConfirm.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_result);
        setTitles(getResourceString(R.string.recharge_confirm));
        assignViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone:
                if (alertDialogPhone == null) {
                    View view = View.inflate(WithdrawResultActivity.this, R.layout.dialog_cancel_or_confirm, null);
                    TextView text = (TextView) view.findViewById(R.id.text);
                    text.setText(getResourceString(R.string.call_customer_service_phone) + Constant.phoneNumber);
                    Button cancel = (Button) view.findViewById(R.id.cancel);
                    Button confirm = (Button) view.findViewById(R.id.confirm);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialogPhone.dismiss();
                        }
                    });
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialogPhone.dismiss();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constant.phoneNumber));
                            startActivity(intent);
                        }
                    });
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(WithdrawResultActivity.this);
                    alertDialogPhone = builder.create();
                    alertDialogPhone.show();
                    alertDialogPhone.setContentView(view);
                } else {
                    alertDialogPhone.show();
                }
                break;
            case R.id.withdraw_cash_confirm:
                finish();
                break;
        }
    }

    private String getPayType(String pay_type) {
        switch (pay_type) {
            case "bank":
                return getString(R.string.bank_card);
            case "alipay":
                return getString(R.string.alipay);
            case "weixin":
                return "微信";
        }
        return "微信";
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
