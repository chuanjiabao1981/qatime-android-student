package cn.qatime.player.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;


import java.text.DecimalFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import libraryextra.utils.SPUtils;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author luntify
 * @date 2016/8/15 11:11
 * @Description
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
    private IWXAPI api;
    private boolean reLoad = false;
    private ImageView image;
    private TextView status;
    private TextView orderId;
    private TextView price;
    private TextView viewOrder;//查看订单
    private TextView myOrder;//我的订单
    private Button complete;
    private RelativeLayout loading;
    private View successLayout;
    private View faildLayout;

    DecimalFormat df = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        setTitle(getResources().getString(R.string.payment_result));
        assignViews();

        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
        api.handleIntent(getIntent(), this);
    }


    private void assignViews() {
        successLayout = findViewById(R.id.success_layout);
        faildLayout = findViewById(R.id.faild_layout);
        image = (ImageView) findViewById(R.id.image);
        status = (TextView) findViewById(R.id.status);
        orderId = (TextView) findViewById(R.id.orderId);
        price = (TextView) findViewById(R.id.price);
        viewOrder = (TextView) findViewById(R.id.view_order);
        complete = (Button) findViewById(R.id.complete);
        loading = (RelativeLayout) findViewById(R.id.loading);
//        viewOrder.setOnClickListener(this);
        complete.setOnClickListener(this::onClick);
        orderId.setText((String) SPUtils.get(WXPayEntryActivity.this, "orderId", ""));
        String price = df.format(SPUtils.get(WXPayEntryActivity.this, "price", 0));
        if (price.startsWith(".")) {
            price = "0" + price;
        }

        WXPayEntryActivity.this.price.setText("￥" + price);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.errCode == 0) {
            EventBus.getDefault().post("pay_success");
            initData();
        } else if (baseResp.errCode == -2) {//用户取消
            finish();
        } else {
            finish();
        }

    }

//    unpaid: 0, # 未支付
//    paid: 1, # 已支付
//    shipped: 2, # 已发货
//    completed: 3, # 已完成
//    expired: 96, # 过期订单
//    failed: 97, # 下单失败
//    refunded: 98, # 已退款
//    waste: 99 # 无效订单

    private void initData() {
        String id = (String) SPUtils.get(this, "orderId", "");
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPayResult + id + "/result", null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        try {
                            String status = response.getString("data");
                            switch (status) {
                                case "unpaid":
                                    if (!reLoad) {
                                        reLoad = true;
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                initData();
                                            }
                                        }, 5000);
                                    } else {
                                        loading.setVisibility(View.GONE);
                                        image.setImageResource(R.mipmap.pay_faild);
                                        WXPayEntryActivity.this.status.setText(getResources().getString(R.string.pay_failure));
                                        faildLayout.setVisibility(View.VISIBLE);
                                        successLayout.setVisibility(View.GONE);
                                    }
                                    break;
                                case "paid":
                                case "shipped":
                                case "completed":
                                    //  支付成功
                                    loading.setVisibility(View.GONE);
                                    image.setImageResource(R.mipmap.pay_success);
                                    WXPayEntryActivity.this.status.setText(getResources().getString(R.string.pay_success));
                                    faildLayout.setVisibility(View.GONE);
                                    successLayout.setVisibility(View.VISIBLE);
                                    break;
                                default:
                                    loading.setVisibility(View.GONE);
                                    image.setImageResource(R.mipmap.pay_faild);
                                    WXPayEntryActivity.this.status.setText(getResources().getString(R.string.pay_failure));
                                    faildLayout.setVisibility(View.VISIBLE);
                                    successLayout.setVisibility(View.GONE);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        loading.setVisibility(View.GONE);
                        image.setImageResource(R.mipmap.pay_faild);
                        WXPayEntryActivity.this.status.setText(getResources().getString(R.string.pay_failure));
                        faildLayout.setVisibility(View.VISIBLE);
                        successLayout.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                loading.setVisibility(View.GONE);
                image.setImageResource(R.mipmap.pay_faild);
                WXPayEntryActivity.this.status.setText(getResources().getString(R.string.pay_failure));
                faildLayout.setVisibility(View.VISIBLE);
                successLayout.setVisibility(View.GONE);
            }
        });
        addToRequestQueue(request);
    }

    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.view_order://查看订单
//                break;
            case R.id.complete://完成  关闭
                finish();
                break;
        }
    }
}
