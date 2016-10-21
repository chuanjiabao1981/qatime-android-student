package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;
import libraryextra.utils.StringUtils;

/**
 * @author Tianhaoranly
 * @date 2016/10/17 9:38
 * @Description:
 */
public class WithdrawCash1Activity extends BaseActivity {
    private EditText rechargeNum;
    private LinearLayout toBankLayout;
    private ImageView toBank;
    private LinearLayout toAlipayLayout;
    private ImageView toAlipay;
    private Button rechargeNow;
    private AlertDialog alertDialog;
    private String payType = "bank";
    private static final int DECIMAL_DIGITS = 2;//小数的位数

    private void assignViews() {
        rechargeNum = (EditText) findViewById(R.id.recharge_num);
        toBankLayout = (LinearLayout) findViewById(R.id.to_bank_layout);
        toBank = (ImageView) findViewById(R.id.to_bank);
        toAlipayLayout = (LinearLayout) findViewById(R.id.to_alipay_layout);
        toAlipay = (ImageView) findViewById(R.id.to_alipay);
        rechargeNow = (Button) findViewById(R.id.recharge_now);

        toAlipayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "alipay";
                toAlipay.setImageResource(R.drawable.shape_select_circle_select);
                toBank.setImageResource(R.drawable.shape_select_circle_normal);
            }
        });
        toBankLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "bank";
                toBank.setImageResource(R.drawable.shape_select_circle_select);
                toAlipay.setImageResource(R.drawable.shape_select_circle_normal);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw1_cash);
        setTitle(getResourceString(R.string.withdraw_cash));
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

        rechargeNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String amount = rechargeNum.getText().toString();
                if (StringUtils.isNullOrBlanK(amount)) {
                    Toast.makeText(WithdrawCash1Activity.this, R.string.amount_can_not_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Double.valueOf(amount) == 0) {
                    Toast.makeText(WithdrawCash1Activity.this, R.string.amount_can_not_zero, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Double.valueOf(amount) > Math.pow(10, 6)) {
                    Toast.makeText(WithdrawCash1Activity.this, "金额不支持", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Double.valueOf(amount) > Double.valueOf(getIntent().getStringExtra("balance"))) {
                    Toast.makeText(WithdrawCash1Activity.this, getResourceString(R.string.amount_not_enough), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(WithdrawCash1Activity.this, WithdrawConfirmActivity.class);
                intent.putExtra("pay_type", payType);
                intent.putExtra("amount", amount);
                startActivityForResult(intent, Constant.REQUEST);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REGIST) {
            setResult(resultCode);
            finish();
        }
    }
}
