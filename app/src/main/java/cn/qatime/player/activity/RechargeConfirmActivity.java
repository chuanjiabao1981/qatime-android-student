package cn.qatime.player.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.PayResult;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.utils.Constant;
import libraryextra.bean.AppPayParamsBean;

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
    private AppPayParamsBean data;
    private IWXAPI api;
    SimpleDateFormat parseISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String alipayData;
    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SDK_PAY_FLAG) {
                PayResult payResult = new PayResult((String) msg.obj);
                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                String resultStatus = payResult.getResultStatus();
                Logger.e("resultstatus", resultStatus);
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
                    EventBus.getDefault().post(PayResultState.SUCCESS);
                } else if (TextUtils.equals(resultStatus, "6001")) {
//                    onEvent(PayResultState.CANCEL);
                } else {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        EventBus.getDefault().post(PayResultState.SUCCESS);
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        EventBus.getDefault().post(PayResultState.ERROR);
                    }
                }
            }
        }
    };
    private int SDK_PAY_FLAG = 1;

    private void assignViews() {
        id = (TextView) findViewById(R.id.id);
        time = (TextView) findViewById(R.id.time);
        mode = (TextView) findViewById(R.id.mode);
        amount = (TextView) findViewById(R.id.amount);
        rechargeConfirm = (Button) findViewById(R.id.recharge_confirm);
        phone = (TextView) findViewById(R.id.phone);
        phone.setText(Constant.phoneNumber);
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constant.phoneNumber));
        if (ActivityCompat.checkSelfPermission(RechargeConfirmActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_confirm);
        setTitles(getResourceString(R.string.recharge_confirm));
        assignViews();

        api = WXAPIFactory.createWXAPI(this, null);
        EventBus.getDefault().register(this);

        // 将该app注册到微信
        api.registerApp(Constant.APP_ID);

        Intent intent = getIntent();
        id.setText(intent.getStringExtra("id"));
        try {
            time.setText(parse.format(parseISO.parse(intent.getStringExtra("created_at"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mode.setText(getPayType(intent.getStringExtra("pay_type")));
        String price = df.format(Double.valueOf(intent.getStringExtra("amount")));
        if (price.startsWith(".")) {
            price = "0" + price;
        }
        price = "￥" + price;
        amount.setText(price);
        if (TextUtils.equals(intent.getStringExtra("pay_type"), "weixin")) {
            data = (AppPayParamsBean) intent.getSerializableExtra("app_pay_params");
        } else {
            //支付宝
            alipayData = intent.getStringExtra("app_pay_params");
        }

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
                                if (ContextCompat.checkSelfPermission(RechargeConfirmActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(RechargeConfirmActivity.this, new String[]{
                                            Manifest.permission.CALL_PHONE}, 1);
                                } else {
                                    callPhone();
                                }
                            } else {
                                callPhone();
                            }
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
                if ("weixin".equals(getIntent().getStringExtra("pay_type"))) {//微信支付
                    Logger.e("微信支付");
//                    if (!api.isWXAppInstalled()) {
//                        Toast.makeText(this, R.string.wechat_not_installed, Toast.LENGTH_SHORT).show();
//                    } else if (!api.isWXAppSupportAPI()) {
//                        Toast.makeText(this, R.string.wechat_not_support, Toast.LENGTH_SHORT).show();
//                    } else {
                    PayReq request = new PayReq();

                    request.appId = data.getAppid();

                    request.partnerId = data.getPartnerid();

                    request.prepayId = data.getPrepayid();

                    request.packageValue = data.getPackage();

                    request.nonceStr = data.getNoncestr();

                    request.timeStamp = data.getTimestamp();

                    request.sign = data.getSign();

                    api.sendReq(request);
//                    }
                } else {//支付宝支付
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            // 构造PayTask 对象
                            PayTask alipay = new PayTask(RechargeConfirmActivity.this);
                            // 调用支付接口，获取支付结果
                            String result = alipay.pay(alipayData, true);
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            hd.sendMessage(msg);
                        }
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
                break;

        }
    }

    private String getPayType(String pay_type) {
        switch (pay_type) {
            case "weixin":
                return getString(R.string.wexin_payment);
            case "alipay":
                return getString(R.string.alipay_payment);
            case "offline":
                return getString(R.string.offline_payment);
            default:
                return getString(R.string.non_payment);
        }
    }

    @Subscribe
    public void onEvent(PayResultState state) {
        Intent intent = new Intent(this, RechargePayResultActivity.class);
        intent.putExtra("orderId", id.getText().toString());
        intent.putExtra("price", amount.getText().toString());
        intent.putExtra("state", state);
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
