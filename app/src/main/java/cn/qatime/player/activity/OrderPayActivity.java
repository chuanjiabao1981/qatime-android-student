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

        initData();
    }

    private void initData() {
        //1
        data = (OrderConfirmBean.App_pay_params) getIntent().getSerializableExtra("data");

        String price = df.format(getIntent().getIntExtra("price", 0));
        if (price.startsWith(".")) {
            price = "0" + price;
        }
        code.setText(getResources().getString(R.string.order_number) + "：" + getIntent().getStringExtra("id"));
        time.setText(getResources().getString(R.string.time_built) + "：" + getIntent().getStringExtra("time"));
        type.setText(getIntent().getStringExtra("type"));
        this.price.setText(getResources().getString(R.string.amount_payment) + "：￥" + price);
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

                PayReq request = new PayReq();

                request.appId = data.getAppid();

                request.partnerId = data.getPartnerid();

                request.prepayId = data.getPrepayid();

                request.packageValue = data.getPackage();

                request.nonceStr = data.getNoncestr();

                request.timeStamp = data.getTimestamp();

                request.sign = data.getSign();
                api.sendReq(request);
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
