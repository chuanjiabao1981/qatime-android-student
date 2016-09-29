package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.bean.RechargeBean;
import cn.qatime.player.utils.Constant;

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
    DecimalFormat df = new DecimalFormat("#.00");
    private RechargeBean.DataBean.AppPayParamsBean data;
    private IWXAPI api;

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

        api = WXAPIFactory.createWXAPI(this, null);

        EventBus.getDefault().register(this);

        // 将该app注册到微信
        api.registerApp(Constant.APP_ID);


        Intent intent = getIntent();
        id.setText(intent.getStringExtra("id"));
        time.setText(intent.getStringExtra("created_at"));
        mode.setText(getPayType(intent.getStringExtra("pay_type")));
        String price = df.format(Double.valueOf(intent.getStringExtra("amount")));
        if (price.startsWith(".")) {
            price = "0" + price;
        }
        price = "￥"+price;
        amount.setText(price);
        data = (RechargeBean.DataBean.AppPayParamsBean) intent.getSerializableExtra("app_pay_params");

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
            case R.id.recharge_confirm:
                // TODO: 2016/9/27  调用api充值
                if ("weixin".equals(getIntent().getStringExtra("pay_type"))) {//微信支付
                    Logger.e("微信支付");
                    PayReq request = new PayReq();

                    request.appId = data.getAppid();

                    request.partnerId = data.getPartnerid();

                    request.prepayId = data.getPrepayid();

                    request.packageValue = data.getPackageX();

                    request.nonceStr = data.getNoncestr();

                    request.timeStamp = data.getTimestamp();

                    request.sign = data.getSign();

                    api.sendReq(request);
                } else {//支付宝支付
                    // TODO: 2016/9/28 支付宝
                    Logger.e("支付宝支付");
                }
                break;

        }
    }

    private String getPayType(String pay_type) {
        switch (pay_type) {
            case "weixin":
                return "微信支付";
            case "alipay":
                return "支付宝";
            case "offline":
                return "线下支付";
        }
        return "未支付";
    }

    @Subscribe
    public void onEvent(PayResultState state) {
        Intent intent = new Intent(this,RechargePayResultActivity.class);
        intent.putExtra("state",state);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
