package cn.qatime.player.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.BusEvent;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.PayPopView;
import libraryextra.bean.CashAccountBean;
import libraryextra.bean.WithdrawCashBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.KeyBoardUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2016/10/17 9:38
 * @Description:
 */
public class WithdrawCashActivity extends BaseActivity implements View.OnClickListener {
    private EditText rechargeNum;
    private Button rechargeNow;
    private String payType = "weixin";
    private static final int DECIMAL_DIGITS = 2;//小数的位数
    private String amount;
    private PayPopView payPopView;
    private AlertDialog alertDialog;
    private TextView phone;
    private android.app.AlertDialog alertDialogPhone;
    private IWXAPI api;
    public String ticket_tocken;

    private void assignViews() {
        rechargeNum = (EditText) findViewById(R.id.recharge_num);
        rechargeNow = (Button) findViewById(R.id.recharge_now);
        CashAccountBean cashAccount = BaseApplication.getInstance().getCashAccount();
        String price = "0";
        if (cashAccount != null && cashAccount.getData() != null) {
            price = cashAccount.getData().getBalance();
            if (price.startsWith(".")) {
                price = "0" + price;
            }
        }
        rechargeNum.setHint(getString(R.string.withdraw_num_hint, price));

        phone = (TextView) findViewById(R.id.phone);
        phone.setText(Constant.phoneNumber);
        phone.setOnClickListener(this);
    }
    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constant.phoneNumber));
        if (ActivityCompat.checkSelfPermission(WithdrawCashActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
            }else{
                callPhone();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constant.APP_ID);
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
        rechargeNum.setClickable(false);
        rechargeNum.setLongClickable(false);
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
        if (Double.valueOf(amount) > Double.valueOf(BaseApplication.getInstance().getCashAccount().getData().getBalance())) {
            Toast.makeText(WithdrawCashActivity.this, getResourceString(R.string.amount_not_enough), Toast.LENGTH_SHORT).show();
            return;
        }

        payPopView = new PayPopView(getString(R.string.user_withdraw), "￥" + amount, WithdrawCashActivity.this);
        payPopView.showPop();
        payPopView.setOnPayPSWVerifyListener(new PayPopView.OnPayPSWVerifyListener() {


            @Override
            public void onSuccess(String ticket_token) {
                payPopView.dismiss();
                WithdrawCashActivity.this.ticket_tocken = ticket_token;
                //绑定
                if (!api.isWXAppInstalled()) {
                    Toast.makeText(WithdrawCashActivity.this, R.string.wechat_not_installed, Toast.LENGTH_SHORT).show();
                } else if (!api.isWXAppSupportAPI()) {
                    Toast.makeText(WithdrawCashActivity.this, R.string.wechat_not_support, Toast.LENGTH_SHORT).show();
                } else {
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_info";
                    api.sendReq(req);
                }
            }

            @Override
            public void onError(int errorCode) {
                payPopView.dismiss();
                if (errorCode == 2005) {
                    dialogPSWError();
                } else if (errorCode == 2006) {


                    Toast.makeText(WithdrawCashActivity.this, R.string.pay_password_not_set, Toast.LENGTH_SHORT).show();
                } else if (errorCode == 2008) {
                    dialogServerError(getString(R.string.pay_password_not_enough_time));//未满24小时
                } else if (errorCode == 2009) {
                    dialogServerError(getString(R.string.pay_password_too_many_mistake));//错误太多
                } else if (errorCode == 0) {
                    Toast.makeText(WithdrawCashActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                } else {
                    dialogServerError(getString(R.string.withdraw_server_error));//提现系统繁忙
                }
            }
        });
    }

    @Subscribe
    public void onEvent(String code) {
        HashMap<String, String> map = new HashMap<>();
        map.put("amount", amount);
        map.put("pay_type", payType);
        map.put("ticket_token", ticket_tocken);
        map.put("app_type", "student_app");
        map.put("access_code", code);
        addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getInstance().getUserId() + "/withdraws", map), null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                if (!response.isNull("data")) {
                    WithdrawCashBean bean = JsonUtils.objectFromJson(response.toString(), WithdrawCashBean.class);
                    Intent intent = new Intent(WithdrawCashActivity.this, WithdrawResultActivity.class);
                    intent.putExtra("amount", bean.getData().getAmount());
                    intent.putExtra("pay_type", bean.getData().getPay_type());
                    intent.putExtra("id", bean.getData().getTransaction_no());
                    intent.putExtra("create_at", bean.getData().getCreated_at());
                    startActivity(intent);
                    EventBus.getDefault().post(BusEvent.REFRESH_CASH_ACCOUNT);
                    finish();
                } else {
                    onError(response);
                }
            }

            @Override
            protected void onError(JSONObject response) {
                try {
                    JSONObject error = response.getJSONObject("error");
                    int code = error.getInt("code");
                    if (code == 2007) {
                        Toast.makeText(WithdrawCashActivity.this, R.string.token_error, Toast.LENGTH_SHORT).show();
                    } else if (code == 3002) {//  "msg": "验证失败: Value 账户资金不足，无法提取!"
                        Toast.makeText(WithdrawCashActivity.this, getResources().getString(R.string.amount_not_enough), Toast.LENGTH_SHORT).show();
                    } else if (code == 3003) {//  "msg": "APIErrors::WithdrawExisted"
                        Toast.makeText(WithdrawCashActivity.this, getResources().getString(R.string.withdraw_existed), Toast.LENGTH_SHORT).show();
                    } else {
                        dialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                dialog();
                Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        }));
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_confirm, null);
        TextView tv = (TextView) view.findViewById(R.id.text);
        tv.setText(R.string.apply_withdraw_error);
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

    private void  dialogPSWError() {
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
        startActivity(new Intent(this, PayPSWForgetActivity.class));
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
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(WithdrawCashActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(WithdrawCashActivity.this, new String[]{
                                            Manifest.permission.CALL_PHONE}, 1);
                                } else {
                                    callPhone();
                                }
                            } else {
                                callPhone();
                            }
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
            case R.id.recharge_now:
                long changeAt = BaseApplication.getInstance().getCashAccount().getData().getPassword_set_at();

                int diff = 2 - (int) ((System.currentTimeMillis() / 1000 - changeAt) / 3600);
                if (diff <= 2 && diff > 0) {
                    dialogServerError(getString(R.string.pay_password_not_enough_time));//未满24小时
                } else {
                    showPSWPop();
                }
                break;
        }
    }
}
