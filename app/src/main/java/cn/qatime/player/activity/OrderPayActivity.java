package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.OrderConfirmBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.JsonUtils;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.SPUtils;
import cn.qatime.player.utils.SignUtil;
import cn.qatime.player.utils.StringUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VolleyErrorListener;
import cn.qatime.player.utils.VolleyListener;

public class OrderPayActivity extends BaseActivity {
    TextView code;
    TextView time;
    TextView type;
    TextView price;
    Button commit;
    private OrderConfirmBean data;
    private IWXAPI api;
    private boolean canPay = false;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private int priceNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        setTitle(getResources().getString(R.string.pay_confirm));
        initView();
        api = WXAPIFactory.createWXAPI(this, null);

        EventBus.getDefault().register(this);

        // 将该app注册到微信
        api.registerApp(Constant.APP_ID);

        int id = getIntent().getIntExtra("id", 0);
        String payType = getIntent().getStringExtra("payType");
        priceNumber = getIntent().getIntExtra("price", 0);
        initData(id, payType);
    }

    private void initData(int id, final String payType) {
        Map<String, String> map = new HashMap<>();
        map.put("pay_type", payType);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlPayPrepare + id + "/orders", map), null,
                new VolleyListener(OrderPayActivity.this) {


                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), OrderConfirmBean.class);

                        if (data != null) {
                            canPay = true;
                            code.setText("订单编号：" + data.getData().getId());
                            time.setText("创建时间：" + format.format(new Date()));
                            if (payType.equals("1")) {
                                type.setText("支付方式：微信支付");
                            } else {
                                type.setText("支付方式：支付宝支付");
                            }
                            price.setText("支付金额：￥" + priceNumber);
                            commit.setEnabled(true);
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        canPay = false;

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    public void initView() {

        code = (TextView) findViewById(R.id.code);
        time = (TextView) findViewById(R.id.time);
        type = (TextView) findViewById(R.id.type);
        price = (TextView) findViewById(R.id.price);
        commit = (Button) findViewById(R.id.commit);
        commit.setEnabled(false);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (canPay) {
                    PayReq request = new PayReq();

                    request.appId = data.getData().getApp_pay_params().getAppid();

                    request.partnerId = data.getData().getApp_pay_params().getPartnerid();

                    request.prepayId = data.getData().getApp_pay_params().getPrepayid();

                    request.packageValue = data.getData().getApp_pay_params().getPackage();

                    request.nonceStr = data.getData().getNonce_str();

                    request.timeStamp = data.getData().getApp_pay_params().getTimestamp();


                    request.sign = data.getData().getApp_pay_params().getSign();
                    api.sendReq(request);
                    SPUtils.put(OrderPayActivity.this, "orderId", data.getData().getId());
                    SPUtils.put(OrderPayActivity.this, "price", priceNumber);
                } else {
                    Toast.makeText(OrderPayActivity.this, "不能支付", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Subscribe
    public void onEvent(String event) {
        if (!StringUtils.isNullOrBlanK(event) && event.equals("pay_success")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
