package cn.qatime.player.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.BusEvent;
import cn.qatime.player.utils.Constant;
import libraryextra.bean.CashAccountBean;

/**
 * @author Tianhaoranly
 * @date 2016/9/27 15:52
 * @Description:
 */
public class PersonalMyWalletActivity extends BaseActivity implements View.OnClickListener {
    private TextView balance;
    private TextView yuan;
    private TextView consumption;
    private LinearLayout rechargeRecord;
    private LinearLayout consumptionRecord;
    private LinearLayout withdrawRecord;
    private TextView phone;
    private TextView recharge;
    private TextView withdrawCash;
    private Dialog alertDialog;
    DecimalFormat df = new DecimalFormat("#.00");
    private LinearLayout refundRecord;

    private void assignViews() {
        balance = (TextView) findViewById(R.id.balance);
        yuan = (TextView) findViewById(R.id.yuan);
        consumption = (TextView) findViewById(R.id.consumption);
        rechargeRecord = (LinearLayout) findViewById(R.id.recharge_record);
        consumptionRecord = (LinearLayout) findViewById(R.id.consumption_record);
        withdrawRecord = (LinearLayout) findViewById(R.id.withdraw_record);
        refundRecord = (LinearLayout) findViewById(R.id.refund_record);
        phone = (TextView) findViewById(R.id.phone);
        phone.setText(Constant.phoneNumber);
        recharge = (TextView) findViewById(R.id.recharge);
        withdrawCash = (TextView) findViewById(R.id.withdraw_cash);
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constant.phoneNumber));
        if (ActivityCompat.checkSelfPermission(PersonalMyWalletActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
        //调用拨号面板
//        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constant.phoneNumber));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
            } else {
                callPhone();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_my_wallet);
        setTitles(getResourceString(R.string.my_wallet));
        setRightText("说明", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalMyWalletActivity.this, WalletExplainActivity.class);
                startActivity(intent);
            }
        });
        assignViews();
        EventBus.getDefault().register(this);
        initData();
        recharge.setOnClickListener(this);
        phone.setOnClickListener(this);
        consumptionRecord.setOnClickListener(this);
        rechargeRecord.setOnClickListener(this);
        withdrawRecord.setOnClickListener(this);
        refundRecord.setOnClickListener(this);
        withdrawCash.setOnClickListener(this);
    }

    private void initData() {
        CashAccountBean cashAccount = BaseApplication.getInstance().getCashAccount();
        if (cashAccount != null && cashAccount.getData() != null) {
            String price = cashAccount.getData().getBalance();
            if (price.startsWith(".")) {
                price = "0" + price;
            }
            balance.setText(price);
            String price1 = cashAccount.getData().getTotal_expenditure();
            if (price1.startsWith(".")) {
                price1 = "0" + price1;
            }
            consumption.setText(price1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone:
                if (alertDialog == null) {
                    View view = View.inflate(PersonalMyWalletActivity.this, R.layout.dialog_cancel_or_confirm, null);
                    TextView text = (TextView) view.findViewById(R.id.text);
                    text.setText(getResourceString(R.string.call_customer_service_phone) + Constant.phoneNumber);
                    Button cancel = (Button) view.findViewById(R.id.cancel);
                    Button confirm = (Button) view.findViewById(R.id.confirm);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(PersonalMyWalletActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(PersonalMyWalletActivity.this, new String[]{
                                            Manifest.permission.CALL_PHONE}, 1);
                                } else {
                                    callPhone();
                                }
                            } else {
                                callPhone();
                            }
                        }
                    });
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PersonalMyWalletActivity.this);
                    alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.setContentView(view);
                } else {
                    alertDialog.show();
                }
                break;
            case R.id.recharge:
                Intent intent = new Intent(this, RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.withdraw_cash:
                intent = new Intent(this, WithdrawCashActivity.class);
                startActivity(intent);
                break;
            case R.id.recharge_record:
                intent = new Intent(this, RecordFundActivity.class);
                intent.putExtra("page", 0);
                startActivity(intent);
                break;
            case R.id.withdraw_record:
                intent = new Intent(this, RecordFundActivity.class);
                intent.putExtra("page", 1);
                startActivity(intent);
                break;
            case R.id.consumption_record:
                intent = new Intent(this, RecordFundActivity.class);
                intent.putExtra("page", 2);
                startActivity(intent);
                break;
            case R.id.refund_record:
                intent = new Intent(this, RecordFundActivity.class);
                intent.putExtra("page", 3);
                startActivity(intent);
                break;
        }
    }

    @Subscribe
    public void onEvent(BusEvent event) {
        if (event == BusEvent.ON_REFRESH_CASH_ACCOUNT)
            initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

//    private void refreshCashAccount() {
//        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.urlpayment + BaseApplication.getUserId() + "/cash", null, new VolleyListener(PersonalMyWalletActivity.this) {
//
//            @Override
//            protected void onTokenOut() {
//                tokenOut();
//            }
//
//            @Override
//            protected void onSuccess(JSONObject response) {
//                CashAccountBean cashAccount = JsonUtils.objectFromJson(response.toString(), CashAccountBean.class);
//                BaseApplication.setCashAccount(cashAccount);
//                initData();
//            }
//
//            @Override
//            protected void onError(JSONObject response) {
//                Toast.makeText(PersonalMyWalletActivity.this, getResourceString(R.string.get_wallet_info_error), Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(PersonalMyWalletActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
//            }
//        }));
//    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
