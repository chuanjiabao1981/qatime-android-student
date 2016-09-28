package cn.qatime.player.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;
import libraryextra.bean.OrderConfirmBean;
import libraryextra.utils.StringUtils;

public class OrderPayActivity extends BaseActivity {
    TextView code;
    TextView time;
    TextView price;
    TextView type;
    TextView phone;
    Button commit;

    private IWXAPI api;


    DecimalFormat df = new DecimalFormat("#.00");
    private OrderConfirmBean.App_pay_params data;
    private int payType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        setTitle(getResourceString(R.string.pay_confirm));
        initView();
        payType = getIntent().getIntExtra("type", 0);
        api = WXAPIFactory.createWXAPI(this, null);

        EventBus.getDefault().register(this);

        if (payType == 1) {
            // 将该app注册到微信
            api.registerApp(Constant.APP_ID);
        } else {
        }

        initData();
    }

    private void initData() {
        data = (OrderConfirmBean.App_pay_params) getIntent().getSerializableExtra("data");

        String price = df.format(getIntent().getIntExtra("price", 0));
        if (price.startsWith(".")) {
            price = "0" + price;
        }
        code.setText(getResourceString(R.string.order_number) + "：" + getIntent().getStringExtra("id"));
        time.setText(getResourceString(R.string.time_built) + "：" + getIntent().getStringExtra("time"));
        if (payType == 0) {
            type.setText(getResourceString(R.string.method_payment) + getResourceString(R.string.pay_alipay));
        } else {
            type.setText(getResourceString(R.string.method_payment) + getResourceString(R.string.pay_wexin));
        }
        this.price.setText(getResourceString(R.string.amount_payment) + "：￥" + price);
    }

    public void initView() {

        code = (TextView) findViewById(R.id.code);
        time = (TextView) findViewById(R.id.time);
        type = (TextView) findViewById(R.id.type);
        phone = (TextView) findViewById(R.id.phone);
        price = (TextView) findViewById(R.id.price);
        commit = (Button) findViewById(R.id.commit);
        //拨打电话
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText()));
                startActivity(intent);
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payType == 1) {
                    PayReq request = new PayReq();

                    request.appId = data.getAppid();

                    request.partnerId = data.getPartnerid();

                    request.prepayId = data.getPrepayid();

                    request.packageValue = data.getPackage();

                    request.nonceStr = data.getNoncestr();

                    request.timeStamp = data.getTimestamp();

                    request.sign = data.getSign();
                    api.sendReq(request);
                } else {
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
