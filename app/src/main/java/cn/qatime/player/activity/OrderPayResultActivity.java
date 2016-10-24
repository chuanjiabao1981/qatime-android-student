package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2016/9/29 10:41
 * @Description:
 */
public class OrderPayResultActivity extends BaseActivity implements View.OnClickListener {
    private boolean reLoad = false;
    private ImageView image;
    private TextView status;
    private TextView orderId;
    private TextView price;
    private TextView viewOrder;//查看订单
    private Button complete;
    private RelativeLayout loading;
    private View failedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay_result);
        setTitle(getResources().getString(R.string.payment_result));
        assignViews();

    }

    private void assignViews() {
        failedLayout = findViewById(R.id.failed_layout);
        image = (ImageView) findViewById(R.id.image);
        status = (TextView) findViewById(R.id.status);
        orderId = (TextView) findViewById(R.id.orderId);
        price = (TextView) findViewById(R.id.price);
        viewOrder = (TextView) findViewById(R.id.view_order);
        complete = (Button) findViewById(R.id.complete);
        loading = (RelativeLayout) findViewById(R.id.loading);

        viewOrder.setOnClickListener(this);
        complete.setOnClickListener(this);
        PayResultState state = (PayResultState) getIntent().getSerializableExtra("state");
        switch (state) {
            case SUCCESS:
                initData();
                break;
        }
        orderId.setText(getIntent().getStringExtra("orderId"));
        price.setText(getIntent().getStringExtra("price"));
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
        String id = getIntent().getStringExtra("orderId");
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
                                        payFailed();
                                    }
                                    break;
                                case "paid":
                                case "shipped":
                                case "completed":
                                    //  支付成功
                                    loading.setVisibility(View.GONE);
                                    image.setImageResource(R.mipmap.pay_success);
                                    OrderPayResultActivity.this.status.setText(getResources().getString(R.string.pay_success));
                                    failedLayout.setVisibility(View.GONE);
                                    break;
                                default:
                                    payFailed();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        payFailed();
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                payFailed();
            }
        });
        addToRequestQueue(request);
    }

    private void payFailed() {
        loading.setVisibility(View.GONE);
        image.setImageResource(R.mipmap.pay_faild);
        OrderPayResultActivity.this.status.setText(getResources().getString(R.string.pay_failure));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_order://查看订单
                Intent intent = new Intent(this,PersonalMyOrderActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.complete://完成  关闭
                finish();
                break;
        }
    }
}
