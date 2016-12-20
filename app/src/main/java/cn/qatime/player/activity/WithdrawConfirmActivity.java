package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.WithdrawCashBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2016/10/17 16:13
 * @Description:
 */
public class WithdrawConfirmActivity extends BaseActivity implements View.OnClickListener {
    private EditText account;
    private EditText name;
    private Button withdrawCashNow;
    private String amount;
    private String payType;
    private EditText code;
    private TextView textGetcode;
    private TimeCount time;
    private AlertDialog alertDialog;

    private void assignViews() {
        account = (EditText) findViewById(R.id.account);
        name = (EditText) findViewById(R.id.name);
        withdrawCashNow = (Button) findViewById(R.id.withdraw_cash_now);
        amount = getIntent().getStringExtra("amount");
        code = (EditText) findViewById(R.id.code);
        textGetcode = (TextView) findViewById(R.id.text_getcode);
        time = new TimeCount(60000, 1000);
        code.setHint(getResourceString(R.string.hint_input_code));
        payType = getIntent().getStringExtra("pay_type");

        if ("alipay".equals(payType)) {
            setTitle(getResourceString(R.string.withdraw_cash_to_alipay));
            account.setHint(getResourceString(R.string.hint_input_alipay_account));
        } else if ("bank".equals(payType)) {
            account.setHint(getResourceString(R.string.hint_input_bank_card_account));
            setTitle(getResourceString(R.string.withdraw_cash_to_bank));
        }

        withdrawCashNow.setOnClickListener(this);
        textGetcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_getcode:
                Map<String, String> map = new HashMap<>();
                map.put("send_to", BaseApplication.getProfile().getData().getUser().getLogin_mobile());
                map.put("key", "withdraw_cash");

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGetCode, map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        withdrawCashNow.setEnabled(true);
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.code_send_success), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.code_send_failed), Toast.LENGTH_LONG).show();
                    }
                }, new VolleyErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }));


                time.start();
                break;
            case R.id.withdraw_cash_now:
                if (StringUtils.isNullOrBlanK(account.getText().toString())) { //账号
                    if ("alipay".equals(payType)) {
                        Toast.makeText(this, getResources().getString(R.string.hint_input_alipay_account), Toast.LENGTH_SHORT).show();
                    } else if ("bank".equals(payType)) {
                        Toast.makeText(this, getResources().getString(R.string.hint_input_bank_card_account), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (StringUtils.isNullOrBlanK(name.getText().toString())) { //姓名
                    Toast.makeText(this, getResources().getString(R.string.input_real_name), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isNullOrBlanK(code.getText().toString())) { //验证码
                    Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
                    return;
                }

                map = new HashMap<>();
                map.put("send_to", BaseApplication.getProfile().getData().getUser().getLogin_mobile());
                map.put("amount", amount);
                map.put("pay_type", payType);
                map.put("account", account.getText().toString().trim());
                map.put("name", name.getText().toString().trim());
                map.put("verify", code.getText().toString().trim());
                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getUserId() + "/withdraws", map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        if (!response.isNull("data")) {
                            WithdrawCashBean bean = JsonUtils.objectFromJson(response.toString(), WithdrawCashBean.class);
                            Intent intent = new Intent(WithdrawConfirmActivity.this, WithdrawResultActivity.class);
                            intent.putExtra("amount", bean.getData().getAmount());
                            intent.putExtra("pay_type", bean.getData().getPay_type());
                            intent.putExtra("id", bean.getData().getTransaction_no());
                            intent.putExtra("create_at", bean.getData().getCreated_at());
                            startActivityForResult(intent, Constant.REQUEST);
                        } else {
                            onError(response);
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        try {
                            JSONObject error = response.getJSONObject("error");
                            int code = error.getInt("code");
                            if (code == 2003) {
                                Toast.makeText(WithdrawConfirmActivity.this, getResources().getString(R.string.code_error), Toast.LENGTH_SHORT).show();
                            } else if (code == 3002) {//  "msg": "验证失败: Value 账户资金不足，无法提取!"
                                Toast.makeText(WithdrawConfirmActivity.this, getResources().getString(R.string.amount_not_enough), Toast.LENGTH_SHORT).show();
                            } else if (code == 3003) {//  "msg": "APIErrors::WithdrawExisted"
                                Toast.makeText(WithdrawConfirmActivity.this, getResources().getString(R.string.withdraw_existed), Toast.LENGTH_SHORT).show();
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
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_confirm, null);
        TextView tv = (TextView) view.findViewById(R.id.text);
        tv.setText("提现系统繁忙，请稍候再试。");
        Button confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
//        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
//        attributes.width= ScreenUtils.getScreenWidth(getApplicationContext())- DensityUtils.dp2px(getApplicationContext(),20)*2;
//        alertDialog.getWindow().setAttributes(attributes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_confirm);
        assignViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REGIST) {
            setResult(resultCode);
            finish();
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            textGetcode.setText(getResourceString(R.string.getcode));
            textGetcode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            textGetcode.setEnabled(false);//防止重复点击
            textGetcode.setText(millisUntilFinished / 1000 + getResourceString(R.string.time_after_acquisition));
        }
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
