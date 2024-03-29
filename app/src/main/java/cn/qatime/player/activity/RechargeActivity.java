package cn.qatime.player.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.bean.RechargeBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.SPUtils;
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
    private ImageView wechatPay;
    private ImageView alipay;
    private Button rechargeNow;
    private AlertDialog alertDialog;
    private static final int DECIMAL_DIGITS = 2;//小数的位数
    private String payType = "weixin";
    private AlertDialog alertDialogPhone;

    private void assignViews() {
        rechargeNum = (EditText) findViewById(R.id.recharge_num);
        View wechatLayout = findViewById(R.id.wechat_layout);
        View alipayLayout = findViewById(R.id.alipay_layout);
        wechatPay = (ImageView) findViewById(R.id.wechat_pay);
        alipay = (ImageView) findViewById(R.id.alipay);
        rechargeNow = (Button) findViewById(R.id.recharge_now);
        TextView phone = (TextView) findViewById(R.id.phone);
        phone.setText(Constant.phoneNumber);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPhone();
            }
        });
        alipayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "alipay";
                alipay.setImageResource(R.drawable.shape_select_circle_select);
                wechatPay.setImageResource(R.drawable.shape_select_circle_normal);
            }
        });
        wechatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "weixin";
                wechatPay.setImageResource(R.drawable.shape_select_circle_select);
                alipay.setImageResource(R.drawable.shape_select_circle_normal);
            }
        });

    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constant.phoneNumber));
        if (ActivityCompat.checkSelfPermission(RechargeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
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

    private void dialogPhone() {
        if (alertDialogPhone == null) {
            View view = View.inflate(RechargeActivity.this, R.layout.dialog_cancel_or_confirm, null);
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(RechargeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(RechargeActivity.this, new String[]{
                                    Manifest.permission.CALL_PHONE}, 1);
                        } else {
                            callPhone();
                        }
                    } else {
                        callPhone();
                    }
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(RechargeActivity.this);
            alertDialogPhone = builder.create();
            alertDialogPhone.show();
            alertDialogPhone.setContentView(view);
        } else {
            alertDialogPhone.show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        setTitles(getResourceString(R.string.recharge_choice));
        EventBus.getDefault().register(this);
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


                if (s.toString().trim().equals(".")) {
                    s = "0" + s;
                    rechargeNum.setText(s);
                    rechargeNum.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        rechargeNum.setText(s.subSequence(0, 1));
                        rechargeNum.setSelection(1);
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
                    Toast.makeText(RechargeActivity.this, R.string.amount_can_not_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Double.valueOf(amount) == 0) {
                    Toast.makeText(RechargeActivity.this, R.string.amount_can_not_zero, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Double.valueOf(amount) > Math.pow(10, 6)) {
                    Toast.makeText(RechargeActivity.this, R.string.amount_not_allow, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (payType.equals("weixin")) {
                    IWXAPI api = WXAPIFactory.createWXAPI(RechargeActivity.this, null);
                    if (!api.isWXAppInstalled()) {
                        Toast.makeText(RechargeActivity.this, R.string.wechat_not_installed, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
//                else if (payType.equals("alipay")) {
//                    return;
//                }

                if (amount.endsWith(".")) amount += "0";
                rechargeNow.setEnabled(false);
                Map<String, String> map = new HashMap<>();
                map.put("amount", amount);
                map.put("pay_type", payType);
                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getInstance().getUserId() + "/recharges", map), null, new VolleyListener(RechargeActivity.this) {

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
                            if (TextUtils.equals(payType, "weixin")) {
                                intent.putExtra("app_pay_params", data.getApp_pay_params());
                            } else if (TextUtils.equals(payType, "alipay")) {
                                intent.putExtra("app_pay_params", data.getApp_pay_str());
                            }
                            startActivity(intent);
                            SPUtils.put(RechargeActivity.this, "RechargeId", data.getId());
                            SPUtils.put(RechargeActivity.this, "amount", data.getAmount());
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
            TextView text = (TextView) view.findViewById(R.id.text);
            text.setText(getResourceString(R.string.recharge_server_basy_please_try_again));
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

    @Subscribe
    public void onEvent(PayResultState state) {
//        if (!StringUtils.isNullOrBlanK(event) && event.equals("pay_success")) {
//
//            finish();
//        }
        finish();
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
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
