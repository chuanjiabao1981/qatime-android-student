package cn.qatime.player.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class OrderPayActivity extends BaseActivity {
    TextView code;
    TextView time;
    TextView price;
    TextView type;
    TextView phone;
    Button commit;

    private IWXAPI api;


    DecimalFormat df = new DecimalFormat("#.00");
    private AppPayParamsBean weixinData;
    private String payType = "weixin";
    private String aliPayData;
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SDK_PAY_FLAG) {
                PayResult payResult = new PayResult((String) msg.obj);
                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                String resultInfo = payResult.getResult();
                Logger.e("resultinfo", resultInfo);
                String resultStatus = payResult.getResultStatus();
                Logger.e("resultstatus", resultStatus);
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
//                    Toast.makeText(AliPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                    setResult(Cantent.PAY_SUCCESS);
                    finish();
                } else {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
//                            Toast.makeText(AliPayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            Toast.makeText(AliPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };
    private int SDK_PAY_FLAG = 1;

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
        } else if (payType.equals("alipay")) {
        }

        initData();
    }

    private void initData() {
        if (payType.equals("weixin")) {
            weixinData = (AppPayParamsBean) getIntent().getSerializableExtra("data");
        } else if (payType.equals("alipay")) {
            aliPayData = getIntent().getStringExtra("data");
        }


        code.setText(getResourceString(R.string.order_number) + "：" + getIntent().getStringExtra("id"));
        SimpleDateFormat parseISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            time.setText(getResourceString(R.string.time_built) + "：" + parse.format(parseISO.parse(getIntent().getStringExtra("time"))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (payType.equals("alipay")) {
            type.setText(getResourceString(R.string.method_payment) + getResourceString(R.string.pay_alipay));
        } else if (payType.equals("weixin")) {
            type.setText(getResourceString(R.string.method_payment) + getResourceString(R.string.pay_wexin));
        } else {
            type.setText(getResourceString(R.string.method_payment) + getResourceString(R.string.pay_account));
        }
        String price = getIntent().getStringExtra("price");
        this.price.setText(getResourceString(R.string.amount_payment) + "：￥" + price);
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
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constant.phoneNumber));
                startActivity(intent);
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payType.equals("weixin")) {
                    PayReq request = new PayReq();

                    request.appId = weixinData.getAppid();

                    request.partnerId = weixinData.getPartnerid();

                    request.prepayId = weixinData.getPrepayid();

                    request.packageValue = weixinData.getPackage();

                    request.nonceStr = weixinData.getNoncestr();

                    request.timeStamp = weixinData.getTimestamp();

                    request.sign = weixinData.getSign();
                    api.sendReq(request);
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
                    // TODO: 2016/10/8  钱包支付
//                    Intent intent = new Intent(OrderPayActivity.this,OrderPayResultActivity.class);
//                    intent.putExtra("state",PayResultState.SUCCESS);
//                    startActivity(intent);
//                    finish();
                }
            }
        });
    }

    @Subscribe
    public void onEvent(PayResultState state) {
        //// TODO: 2017/1/6 QTA-151 支付密码支付 UI有修改，参数传递需重新确认！！！
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
