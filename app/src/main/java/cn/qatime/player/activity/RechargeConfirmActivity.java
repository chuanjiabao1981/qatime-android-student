package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * @author Tianhaoranly
 * @date 2016/9/28 10:24
 * @Description:
 */
public class RechargeConfirmActivity extends BaseActivity implements View.OnClickListener {
    private TextView id;
    private TextView time;
    private TextView mode;
    private TextView amount;
    private Button rechargeConfirm;
    private TextView phone;
    private AlertDialog alertDialogPhone;

    private void assignViews() {
        id = (TextView) findViewById(R.id.id);
        time = (TextView) findViewById(R.id.time);
        mode = (TextView) findViewById(R.id.mode);
        amount = (TextView) findViewById(R.id.amount);
        rechargeConfirm = (Button) findViewById(R.id.recharge_confirm);
        phone = (TextView) findViewById(R.id.phone);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_confirm);
        setTitle(getResourceString(R.string.recharge_confirm));
        assignViews();

        Intent intent = getIntent();
        id.setText(intent.getStringExtra("id"));
        time.setText(intent.getStringExtra("time"));
        mode.setText(intent.getStringExtra("pay_type"));
        amount.setText(intent.getStringExtra("amount"));



        phone.setOnClickListener(this);
        rechargeConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone:
                if (alertDialogPhone == null) {
                    View view = View.inflate(RechargeConfirmActivity.this, R.layout.dialog_cancel_or_confirm, null);
                    TextView text = (TextView) view.findViewById(R.id.text);
                    text.setText(getResourceString(R.string.call_customer_service_phone) + phone.getText() + "?");
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
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText()));
                            startActivity(intent);
                            alertDialogPhone.dismiss();
                        }
                    });
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RechargeConfirmActivity.this);
                    alertDialogPhone = builder.create();
                    alertDialogPhone.show();
                    alertDialogPhone.setContentView(view);
                } else {
                    alertDialogPhone.show();
                }
                break;
            case R.id.confirm:
                // TODO: 2016/9/27  调用api充值
                break;

        }
    }
}
