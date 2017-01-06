package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.MyOrderBean;
import cn.qatime.player.bean.OrderRefundBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2017/1/3 16:20
 * @Description:
 */
public class ApplyRefundActivity extends BaseActivity {

    private OrderRefundBean orderRefundBean;
    private MyOrderBean.Data order;
    private TextView orderId;
    private TextView productName;
    private TextView progress;
    private TextView price;
    private TextView usedAmount;
    private TextView refundType;
    private TextView refundAmount;
    private TextView confirm;
    private EditText reason;


    private void assignViews() {
        orderId = (TextView) findViewById(R.id.order_id);
        productName = (TextView) findViewById(R.id.product_name);
        progress = (TextView) findViewById(R.id.progress);
        price = (TextView) findViewById(R.id.price);
        usedAmount = (TextView) findViewById(R.id.used_amount);
        refundType = (TextView) findViewById(R.id.refund_type);
        refundAmount = (TextView) findViewById(R.id.refund_amount);
        reason = (EditText) findViewById(R.id.reason);
        confirm = (TextView) findViewById(R.id.confirm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_refund);
        assignViews();
        initView();
    }

    private void initView() {
        setTitle("退款申请");
        String response = getIntent().getStringExtra("response");
        order = (MyOrderBean.Data) getIntent().getSerializableExtra("order");
        orderRefundBean = JsonUtils.objectFromJson(response, OrderRefundBean.class);

        orderId.setText(order.getId());
        productName.setText(order.getProduct().getName());
        progress.setText(order.getProduct().getCompleted_lesson_count() + "/" + order.getProduct().getPreset_lesson_count());
        price.setText("￥" + orderRefundBean.getData().getAmount());
        usedAmount.setText("￥" + (Double.valueOf(orderRefundBean.getData().getAmount()) - Double.valueOf(orderRefundBean.getData().getRefund_amount())));
        String pay_type = orderRefundBean.getData().getPay_type();
        if ("weixin".equals(pay_type)) {
            refundType.setText("退至微信");
        } else if ("alipay".equals(pay_type)) {
            refundType.setText("退至支付宝");
        } else {
            refundType.setText("退至余额");
        }
        refundAmount.setText("￥" + orderRefundBean.getData().getRefund_amount());

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmRefund();
            }
        });
    }

    /**
     * 提交退款申请
     */
    private void confirmRefund() {
        if (StringUtils.isNullOrBlanK(reason.getText().toString())) {
            Toast.makeText(ApplyRefundActivity.this, "请输入退款原因", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("order_id", order.getId());
        map.put("reason", reason.getText().toString());
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST,UrlUtils.getUrl(UrlUtils.urlpayment+ BaseApplication.getUserId() + "/refunds", map), null,
                new VolleyListener(ApplyRefundActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Toast.makeText(ApplyRefundActivity.this, "退款申请成功", Toast.LENGTH_SHORT).show();
                        setResult(Constant.RESPONSE);
                        finish();
                    }

                    @Override
                    protected void onError(JSONObject response) {
//                        Toast.makeText(getActivity(), getResourceString(R.string.order_cancel_failed), Toast.LENGTH_SHORT).show();
                        try {
                            if(response.getJSONObject("error").getInt("code")==3002){
                                Toast.makeText(ApplyRefundActivity.this, "暂无法申请退款", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                Logger.e(volleyError.getMessage());
            }
        });
        addToRequestQueue(request);

    }
}
