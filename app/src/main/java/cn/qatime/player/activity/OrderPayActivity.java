package cn.qatime.player.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.PayResult;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.PayPopView;
import libraryextra.bean.AppPayParamsBean;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;


public class OrderPayActivity extends BaseActivity {
    TextView code;
    TextView time;
    TextView price;
    TextView type;
    TextView phone;
    Button commit;

    private IWXAPI api;


    private AppPayParamsBean weixinData;
    private String payType = "weixin";
    private String aliPayData;
    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SDK_PAY_FLAG) {
                PayResult payResult = new PayResult((String) msg.obj);
                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
//                String resultInfo = payResult.getResult();
//                Logger.e("resultinfo", resultInfo);
                String resultStatus = payResult.getResultStatus();
                Logger.e("resultstatus", resultStatus);
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
//                    Toast.makeText(AliPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                    setResult(Cantent.PAY_SUCCESS);
//                    finish();
                    EventBus.getDefault().post(PayResultState.SUCCESS);
                } else if (TextUtils.equals(resultStatus, "6001")) {
//                    onEvent(PayResultState.CANCEL);
                } else {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
//                            Toast.makeText(AliPayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();                    EventBus.getDefault().post(PayResultState.SUCCESS);
                        EventBus.getDefault().post(PayResultState.SUCCESS);
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            Toast.makeText(AliPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(PayResultState.ERROR);
                    }
                }
            }
        }
    };
    private int SDK_PAY_FLAG = 1;
    private AlertDialog alertDialog;
    private PayPopView payPopView;
    private String amount;
    private String orderName;
    private android.app.AlertDialog alertDialogPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        setTitles(getResourceString(R.string.pay_confirm));
        initView();
        payType = getIntent().getStringExtra("type");
        api = WXAPIFactory.createWXAPI(this, null);

        EventBus.getDefault().register(this);

        if (payType.equals("weixin")) {
            // 将该app注册到微信
            api.registerApp(Constant.APP_ID);
        }
//        else if (payType.equals("alipay")) {
//        }

        initData();
    }

    private void initData() {
        if (payType.equals("weixin")) {
            weixinData = (AppPayParamsBean) getIntent().getSerializableExtra("data");
        } else if (payType.equals("alipay")) {
            aliPayData = getIntent().getStringExtra("data");
        } else if (payType.equals("account")) {
            orderName = getIntent().getStringExtra("data");
        }

        code.setText(getIntent().getStringExtra("id"));
        SimpleDateFormat parseISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            time.setText(parse.format(parseISO.parse(getIntent().getStringExtra("time"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (payType.equals("alipay")) {
            type.setText(getResourceString(R.string.pay_alipay));
        } else if (payType.equals("weixin")) {
            type.setText(getResourceString(R.string.pay_wexin));
        } else {
            type.setText(getResourceString(R.string.pay_account));
        }
        amount = getIntent().getStringExtra("price");
        this.price.setText("￥" + amount);
    }

    public void initView() {

        code = (TextView) findViewById(R.id.code);
        time = (TextView) findViewById(R.id.time);
        type = (TextView) findViewById(R.id.type);
        phone = (TextView) findViewById(R.id.phone);
        phone.setText(Constant.phoneNumber);
        price = (TextView) findViewById(R.id.price);
        commit = (Button) findViewById(R.id.commit);
        //拨打电话
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialogPhone == null) {
                    View view = View.inflate(OrderPayActivity.this, R.layout.dialog_cancel_or_confirm, null);
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
                                if (ContextCompat.checkSelfPermission(OrderPayActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(OrderPayActivity.this, new String[]{
                                            Manifest.permission.CALL_PHONE}, 1);
                                } else {
                                    callPhone();
                                }
                            } else {
                                callPhone();
                            }
                        }
                    });
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OrderPayActivity.this);
                    alertDialogPhone = builder.create();
                    alertDialogPhone.show();
                    alertDialogPhone.setContentView(view);
                } else {
                    alertDialogPhone.show();
                }
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payType.equals("weixin")) {
//                    if (!api.isWXAppInstalled()) {
//                        Toast.makeText(OrderPayActivity.this, R.string.wechat_not_installed, Toast.LENGTH_SHORT).show();
//                    } else if (!api.isWXAppSupportAPI()) {
//                        Toast.makeText(OrderPayActivity.this, R.string.wechat_not_support, Toast.LENGTH_SHORT).show();
//                    } else {
                    PayReq request = new PayReq();
                    request.appId = weixinData.getAppid();


                    request.partnerId = weixinData.getPartnerid();

                    request.prepayId = weixinData.getPrepayid();

                    request.packageValue = weixinData.getPackage();

                    request.nonceStr = weixinData.getNoncestr();

                    request.timeStamp = weixinData.getTimestamp();

                    request.sign = weixinData.getSign();
                    api.sendReq(request);
//                    }

                } else if (payType.equals("alipay")) {
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            // 构造PayTask 对象
                            PayTask alipay = new PayTask(OrderPayActivity.this);
                            // 调用支付接口，获取支付结果
                            String result = alipay.pay(aliPayData, true);
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            hd.sendMessage(msg);
                        }
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } else if (payType.equals("account")) {
                    Logger.e("钱包支付");
                    if (BaseApplication.getInstance().getCashAccount().getData().isHas_password()) {
                        long changeAt = BaseApplication.getInstance().getCashAccount().getData().getPassword_set_at();

                        int diff = 2 - (int) ((System.currentTimeMillis() / 1000 - changeAt) / 3600);
                        if (diff <= 2 && diff > 0) {
                            dialogServerError(getString(R.string.pay_password_not_enough_time));//未满24小时
                        } else {
                            showPSWPop();
                        }
                    } else {
                        Toast.makeText(OrderPayActivity.this, R.string.pay_password_not_set, Toast.LENGTH_SHORT).show();
                    }
//                    Intent intent = new Intent(OrderPayActivity.this,OrderPayResultActivity.class);
//                    intent.putExtra("state",PayResultState.SUCCESS);
//                    startActivity(intent);
//                    finish();
                }
            }
        });
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constant.phoneNumber));
        if (ActivityCompat.checkSelfPermission(OrderPayActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    private void showPSWPop() {
//        if (BaseApplication.getCashAccount().getData().isHas_password()) {
        payPopView = new PayPopView(getIntent().getStringExtra("id"), orderName, "￥" + amount, OrderPayActivity.this);
        payPopView.showPop();
        payPopView.setOnPayPSWVerifyListener(new PayPopView.OnPayPSWVerifyListener() {
            @Override
            public void onSuccess(String ticket_token) {
                payPopView.dismiss();
                accountPayOrder(ticket_token);
            }

            @Override
            public void onError(int errorCode) {
                payPopView.dismiss();
                if (errorCode == 2005) {
                    dialogPSWError();
                } else if (errorCode == 2006) {
                    Toast.makeText(OrderPayActivity.this, R.string.pay_password_not_set, Toast.LENGTH_SHORT).show();
                } else if (errorCode == 2008) {
                    dialogServerError(getString(R.string.pay_password_not_enough_time));//未满24小时
                } else if (errorCode == 2009) {
                    dialogServerError(getString(R.string.pay_password_too_many_mistake));//错误次数太多
                } else if (errorCode == 0) {
                    Toast.makeText(OrderPayActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                } else {
                    dialogServerError(getString(R.string.pay_server_error));//支付系统繁忙
                }
            }
        });
//        } else {
//            dialogNotify();
//            Toast.makeText(OrderPayActivity.this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
//        }
    }

    private void accountPayOrder(String ticket_token) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("ticket_token", ticket_token);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlPayResult + getIntent().getStringExtra("id") + "/pay", map), null,
                new VolleyListener(OrderPayActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        EventBus.getDefault().post(PayResultState.SUCCESS);
                    }

                    protected void onError(JSONObject response) {
//                        2007 tocken error;
                        try {
                            if (response.getJSONObject("error").getInt("code") == 2007) {
                                Toast.makeText(OrderPayActivity.this, R.string.token_error, Toast.LENGTH_SHORT).show();
                            } else if (response.getJSONObject("error").getInt("code") == 9999) {
                                Toast.makeText(OrderPayActivity.this, response.getJSONObject("error").getString("msg"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OrderPayActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(OrderPayActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                Toast.makeText(OrderPayActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
        addToRequestQueue(request);
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

    @Subscribe
    public void onEvent(PayResultState state) {
        Intent intent = new Intent(this, OrderPayResultActivity.class);
        intent.putExtra("state", state);
        intent.putExtra("orderId", code.getText().toString().replace(getResourceString(R.string.order_number) + "：", ""));
        intent.putExtra("price", price.getText().toString().replace(getResourceString(R.string.amount_payment) + "：", ""));
        startActivity(intent);
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
