package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.OrderConfirmBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.JsonUtils;
import cn.qatime.player.utils.SignUtil;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        initView();
        api = WXAPIFactory.createWXAPI(this, null);

        // 将该app注册到微信
        api.registerApp(Constant.APP_ID);

        int id = getIntent().getIntExtra("id", 0);
        String payType = getIntent().getStringExtra("payType");
        initData(id, payType);
    }

    private void initData(int id, String payType) {
        Map<String, String> map = new HashMap<>();
        map.put("pay_type", payType);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlPayPrepare + id + "/orders", map), null,
                new VolleyListener(OrderPayActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), OrderConfirmBean.class);
                        commit.setClickable(true);
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    private void initView() {
        code = (TextView) findViewById(R.id.code);
        time = (TextView) findViewById(R.id.time);
        type = (TextView) findViewById(R.id.type);
        price = (TextView) findViewById(R.id.price);
        commit = (Button) findViewById(R.id.commit);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PayReq request = new PayReq();

                request.appId = Constant.APP_ID;

                request.partnerId = "1900000109";

                request.prepayId = data.getData().getPrepayid();

                request.packageValue = "Sign=WXPay";

                request.nonceStr = data.getData().getNoncestr();

                request.timeStamp = "1398746574";

                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("appid", request.appId));
                param.add(new BasicNameValuePair("partnerid", request.partnerId));
                param.add(new BasicNameValuePair("prepayid", request.prepayId));
                param.add(new BasicNameValuePair("package", request.packageValue));
                param.add(new BasicNameValuePair("noncestr", request.nonceStr));
                param.add(new BasicNameValuePair("timestamp", request.timeStamp));
                request.sign = SignUtil.genAppSign(param);

                api.sendReq(request);

            }
        });
    }
}
