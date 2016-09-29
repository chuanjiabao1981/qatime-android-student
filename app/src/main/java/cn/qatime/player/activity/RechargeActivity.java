package cn.qatime.player.activity;

import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.RechargeBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2016/9/27 18:33
 * @Description:
 */
public class RechargeActivity extends BaseActivity {
    private EditText rechargeNum;
    private RadioGroup radioGroup;
    private RadioButton wechatPay;
    private RadioButton alipay;
    private Button rechargeNow;
    private AlertDialog alertDialog;
    private static final int DECIMAL_DIGITS = 2;//小数的位数

    private void assignViews() {
        rechargeNum = (EditText) findViewById(R.id.recharge_num);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        wechatPay = (RadioButton) findViewById(R.id.wechat_pay);
        alipay = (RadioButton) findViewById(R.id.alipay);
        rechargeNow = (Button) findViewById(R.id.recharge_now);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        setTitle(getResourceString(R.string.recharge_choice));
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
                if (StringUtils.isNullOrBlanK(amount) || Double.valueOf(amount) == 0) {
                    Toast.makeText(RechargeActivity.this, R.string.amount_can_not_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Double.valueOf(amount) > Math.pow(10, 6)) {
                    Toast.makeText(RechargeActivity.this, "金额不支持", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (amount.endsWith(".")) amount += "0";
                rechargeNow.setEnabled(false);
                String pay_type = radioGroup.getCheckedRadioButtonId() == R.id.wechat_pay ? "weixin" : "alipay";
                Map<String, String> map = new HashMap<>();
                map.put("amount", amount);
                map.put("pay_type", pay_type);
                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getUserId() + "/recharges", map), null, new VolleyListener(RechargeActivity.this) {

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                        rechargeNow.setEnabled(true);
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        if (!response.isNull("data")) {
                            Intent intent = new Intent(RechargeActivity.this, RechargeConfirmActivity.class);
                            RechargeBean.DataBean data = JsonUtils.objectFromJson(response.toString(), RechargeBean.class).getData();
                            intent.putExtra("id", data.getId());
                            intent.putExtra("amount", data.getAmount());
                            intent.putExtra("pay_type", data.getPay_type());
                            intent.putExtra("created_at", data.getCreated_at());
                            intent.putExtra("app_pay_params", data.getApp_pay_params());
                            startActivity(intent);
                        } else {
                            dialog();
                        }
                        rechargeNow.setEnabled(true);
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        dialog();
                        rechargeNow.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        rechargeNow.setEnabled(true);
                    }
                }));
            }
        });
    }

    protected void dialog() {
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            alertDialog = builder.create();
            View view = View.inflate(this, R.layout.dialog_confirm, null);
            Button confirm = (Button) view.findViewById(R.id.confirm);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            alertDialog.setContentView(view);
        } else {
            alertDialog.show();
        }
    }
}
