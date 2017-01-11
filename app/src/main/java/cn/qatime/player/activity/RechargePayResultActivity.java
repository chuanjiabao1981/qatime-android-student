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
import com.umeng.analytics.MobclickAgent;

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
 * @date 2016/9/29 11:00
 * @Description:
 */
public class RechargePayResultActivity extends BaseActivity implements View.OnClickListener {
    private boolean reLoad = false;
    private boolean rechargeStatus = false;
    private ImageView image;
    private TextView status;
    private TextView orderId;
    private TextView price;
    private TextView look;
    private TextView payDsc;
    private TextView viewOrder;//查看订单
    private Button complete;
    private RelativeLayout loading;
    private View failedLayout;

    private void assignViews() {
        failedLayout = findViewById(R.id.failed_layout);
        image = (ImageView) findViewById(R.id.image);
        status = (TextView) findViewById(R.id.status);
        orderId = (TextView) findViewById(R.id.orderId);
        price = (TextView) findViewById(R.id.price);
        look = (TextView) findViewById(R.id.look);
        payDsc = (TextView) findViewById(R.id.pay_dsc);
        viewOrder = (TextView) findViewById(R.id.view_order);
        complete = (Button) findViewById(R.id.complete);
        loading = (RelativeLayout) findViewById(R.id.loading);

        viewOrder.setOnClickListener(this);
        complete.setOnClickListener(this);


        // TODO: 2016/9/29 支付方式
        PayResultState state = (PayResultState) getIntent().getSerializableExtra("state");
        switch (state) {
            case SUCCESS:
                initData();
                break;
        }
        price.setText(getIntent().getStringExtra("price"));
        orderId.setText(getIntent().getStringExtra("orderId"));
        payDsc.setText(R.string.recharge_amount);
    }

    private void payFailed() {
        viewOrder.setText(R.string.recharge_record);
        loading.setVisibility(View.GONE);
        image.setImageResource(R.mipmap.pay_faild);
        RechargePayResultActivity.this.status.setText(getResources().getString(R.string.recharge_failure));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay_result);
        setTitle("充值结果");
        assignViews();

    }

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
                                    rechargeStatus = true;
                                    loading.setVisibility(View.GONE);
                                    look.setVisibility(View.GONE);
                                    viewOrder.setText(R.string.go_for_buy);
                                    image.setImageResource(R.mipmap.pay_success);
                                    RechargePayResultActivity.this.status.setText(getResources().getString(R.string.recharge_success));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_order:
                if (rechargeStatus) {
                    // 去逛逛
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                    //  充值记录
                    Intent intent = new Intent(this, RecordFundActivity.class);
                    intent.putExtra("page", 0);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.complete:
                finish();
                break;
        }
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
}
