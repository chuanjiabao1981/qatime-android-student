package cn.qatime.player.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import libraryextra.bean.CashAccountBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.view.PayPopView;
import libraryextra.utils.KeyBoardUtils;
import libraryextra.utils.StringUtils;

/**
 * @author Tianhaoranly
 * @date 2016/10/17 9:38
 * @Description:
 */
public class WithdrawCashActivity extends BaseActivity implements View.OnClickListener {
    private EditText rechargeNum;
    private ImageView toBank;
    private ImageView toAlipay;
    private Button rechargeNow;
    private String payType = "bank";
    private static final int DECIMAL_DIGITS = 2;//小数的位数
    private String amount;
    private PayPopView payPopView;
    private AlertDialog alertDialog;
    private TextView phone;
    private android.app.AlertDialog alertDialogPhone;

    private void assignViews() {
        rechargeNum = (EditText) findViewById(R.id.recharge_num);
        LinearLayout toBankLayout = (LinearLayout) findViewById(R.id.to_bank_layout);
        toBank = (ImageView) findViewById(R.id.to_bank);
        LinearLayout toAlipayLayout = (LinearLayout) findViewById(R.id.to_alipay_layout);
        toAlipay = (ImageView) findViewById(R.id.to_alipay);
        rechargeNow = (Button) findViewById(R.id.recharge_now);
        CashAccountBean cashAccount = BaseApplication.getCashAccount();
        String price = "0";
        if (cashAccount != null && cashAccount.getData() != null) {
            price = cashAccount.getData().getBalance();
            if (price.startsWith(".")) {
                price = "0" + price;
            }
        }
        price = "￥" + price;
        rechargeNum.setHint(getString(R.string.withdraw_num_hint,price));

        phone = (TextView) findViewById(R.id.phone);
        phone.setText(Constant.phoneNumber);
        phone.setOnClickListener(this);

        toAlipayLayout.setOnClickListener(this);
        toBankLayout.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_cash);
        setTitles(getResourceString(R.string.withdraw_cash));
        assignViews();
        initListener();
    }

    private void initListener() {
        rechargeNum.setCustomSelectionActionModeCallback(new ActionMode.Callback() {//禁止复制粘贴
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
        rechargeNum.addTextChangedListener(new TextWatcher() {//输入框输入限制
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > DECIMAL_DIGITS) {//小数点后2位
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + DECIMAL_DIGITS + 1);
                        rechargeNum.setText(s);
                        rechargeNum.setSelection(s.length());
                    }
                    if (s.toString().indexOf(".") > 5) {//小数点前5位
                        s = s.toString().substring(0, start) + s.toString().substring(start + count, s.length());
                        rechargeNum.setText(s);
                        rechargeNum.setSelection(5);
                    }
                } else if (s.length() > 5) {//整数不超过5位
                    s = s.toString().subSequence(0,
                            5);
                    rechargeNum.setText(s);
                    rechargeNum.setSelection(5);
                }


                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    rechargeNum.setText(s);
                    rechargeNum.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        rechargeNum.setText(s.subSequence(0, 1));
                        rechargeNum.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rechargeNow.setOnClickListener(this);
    }

    private void showPSWPop() {
        KeyBoardUtils.closeKeybord(WithdrawCashActivity.this);
//        if (BaseApplication.getCashAccount().getData().isHas_password()) {
        amount = rechargeNum.getText().toString();
        if (StringUtils.isNullOrBlanK(amount)) {
            Toast.makeText(WithdrawCashActivity.this, R.string.amount_can_not_null, Toast.LENGTH_SHORT).show();
            return;
        }
        if (Double.valueOf(amount) == 0) {
            Toast.makeText(WithdrawCashActivity.this, R.string.amount_can_not_zero, Toast.LENGTH_SHORT).show();
            return;
        }
        if (Double.valueOf(amount) > Math.pow(10, 6)) {
            Toast.makeText(WithdrawCashActivity.this, R.string.amount_not_allow, Toast.LENGTH_SHORT).show();
            return;
        }
        if (Double.valueOf(amount) > Double.valueOf(BaseApplication.getCashAccount().getData().getBalance())) {
            Toast.makeText(WithdrawCashActivity.this, getResourceString(R.string.amount_not_enough), Toast.LENGTH_SHORT).show();
            return;
        }

        payPopView = new PayPopView(getString(R.string.user_withdraw), "￥" + amount, WithdrawCashActivity.this);
        payPopView.showPop();
        payPopView.setOnPayPSWVerifyListener(new PayPopView.OnPayPSWVerifyListener() {
            @Override
            public void onSuccess(String ticket_token) {
                payPopView.dismiss();
                Intent intent = new Intent(WithdrawCashActivity.this, WithdrawConfirmActivity.class);
                intent.putExtra("pay_type", payType);
                intent.putExtra("amount", amount);
                intent.putExtra("ticket_token",ticket_token);
                startActivity(intent);
            }

            @Override
            public void onError(int errorCode) {
                payPopView.dismiss();
                if (errorCode == 2005) {
                    dialogPSWError();
                } else if (errorCode == 2006) {
                    Toast.makeText(WithdrawCashActivity.this,  R.string.pay_password_not_set, Toast.LENGTH_SHORT).show();
                } else if (errorCode == 2008) {
                    dialogServerError(getString(R.string.pay_password_not_enough_time));//未满24小时
                }else if (errorCode == 2009) {
                    dialogServerError(getString(R.string.pay_password_too_many_mistake));//错误太多
                } else if (errorCode == 0) {
                    Toast.makeText(WithdrawCashActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                } else {
                    dialogServerError(getString(R.string.withdraw_server_error));//提现系统繁忙
                }
            }
        });
//        } else {
//            dialogNotify();
//            Toast.makeText(WithdrawCashActivity.this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
//        }
    }

    private void dialogNotify() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(this, R.layout.dialog_cancel_or_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(R.string.change_pay_password_notify);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setText(R.string.continue_anyway);
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
                changePayPSW();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
    }

    private void dialogServerError(String desc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(this, R.layout.dialog_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(desc);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
    }

    private void dialogPSWError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(this, R.layout.dialog_cancel_or_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(R.string.pay_password_error);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        cancel.setText(R.string.try_again);
        confirm.setText(R.string.forget_password);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showPSWPop();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                dialogNotify();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
    }

    private void changePayPSW() {
//        if (BaseApplication.getCashAccount().getData().isHas_password()) {
//            startActivity(new Intent(this, PayPSWVerifyActivity.class));
//        } else {
            startActivity(new Intent(this, PayPSWForgetActivity.class));
//        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone:
                if (alertDialogPhone == null) {
                    View view = View.inflate(WithdrawCashActivity.this, R.layout.dialog_cancel_or_confirm, null);
                    TextView text = (TextView) view.findViewById(R.id.text);
                    text.setText(getResourceString(R.string.call_customer_service_phone) + phone.getText());
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
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText()));
                            startActivity(intent);
                        }
                    });
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(WithdrawCashActivity.this);
                    alertDialogPhone = builder.create();
                    alertDialogPhone.show();
                    alertDialogPhone.setContentView(view);
                } else {
                    alertDialogPhone.show();
                }
                break;
            case R.id.to_bank_layout:
                payType = "bank";
                toBank.setImageResource(R.drawable.shape_select_circle_select);
                toAlipay.setImageResource(R.drawable.shape_select_circle_normal);
                break;
            case R.id.to_alipay_layout:
                payType = "alipay";
                toAlipay.setImageResource(R.drawable.shape_select_circle_select);
                toBank.setImageResource(R.drawable.shape_select_circle_normal);
                break;
            case R.id.recharge_now:
                if(BaseApplication.getCashAccount().getData().isHas_password()){
                    long changeAt = BaseApplication.getCashAccount().getData().getPassword_set_at();

                    int diff = 2 - (int) ((System.currentTimeMillis()/1000  - changeAt) / 3600);
                    if (diff <= 2&&diff > 0) {
                        dialogServerError(getString(R.string.pay_password_not_enough_time));//未满24小时
                    } else {
                        showPSWPop();
                    }
                }else{
                    Toast.makeText(WithdrawCashActivity.this, R.string.pay_password_not_set, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
