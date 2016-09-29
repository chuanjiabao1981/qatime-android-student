package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.SPUtils;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2016/9/29 11:00
 * @Description:
 */
public class RechargePayResultActivity extends BaseActivity implements View.OnClickListener {
    private TextView payResult;
    private ImageView payResultImg;
    private TextView id;
    private TextView amount;
    private LinearLayout action1;
    private TextView balance;
    private LinearLayout action2;
    private Button over;
    private TextView phone;
    DecimalFormat df = new DecimalFormat("#.00");
    private AlertDialog alertDialogPhone;

    private void assignViews() {
        payResult = (TextView) findViewById(R.id.pay_result);
        payResultImg = (ImageView) findViewById(R.id.pay_result_img);
        id = (TextView) findViewById(R.id.id);
        amount = (TextView) findViewById(R.id.amount);
        action1 = (LinearLayout) findViewById(R.id.action1);
        balance = (TextView) findViewById(R.id.balance);
        action2 = (LinearLayout) findViewById(R.id.action2);
        over = (Button) findViewById(R.id.button_over);
        phone = (TextView) findViewById(R.id.phone);
        // TODO: 2016/9/29 支付方式
        int errCode = getIntent().getIntExtra("errCode", 0);
        if (errCode == 0) {//支付成功
            action1.setVisibility(View.VISIBLE);
            action2.setVisibility(View.GONE);
            // TODO: 2016/9/29 应访问网络获取
            payResult.setText("充值成功");
            payResultImg.setImageResource(R.mipmap.pay_success);
            initData();
        } else {
            action2.setVisibility(View.VISIBLE);
            action1.setVisibility(View.GONE);
            payResult.setText("充值结果未找到");
            payResultImg.setImageResource(R.mipmap.pay_faild);
        }
        String rechargeId = (String) SPUtils.get(RechargePayResultActivity.this, "RechargeId", "");
        String price = df.format(Double.valueOf((String) SPUtils.get(RechargePayResultActivity.this, "amount", "0")));
        if (price.startsWith(".")) {
            price = "0" + price;
        }
        price = "￥" + price;
        amount.setText(price);
        id.setText(rechargeId);

        action1.setOnClickListener(this);
        action2.setOnClickListener(this);
        phone.setOnClickListener(this);
        over.setOnClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_pay_result);
        setTitle("充值结果");
        assignViews();
        // TODO: 2016/9/29 获取充值结果 初始化内容

        initData();
    }

    private void initData() {


        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.urlpayment + BaseApplication.getUserId() + "/cash", null, new VolleyListener(this) {

            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    String price = df.format(Double.valueOf(response.getJSONObject("data").getString("balance")));
                    if (price.startsWith(".")) {
                        price = "0" + price;
                    }
                    price = "￥" + price;
                    balance.setText(price);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(RechargePayResultActivity.this, getResourceString(R.string.get_wallet_info_error), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(RechargePayResultActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone:
                if (alertDialogPhone == null) {
                    View view = View.inflate(RechargePayResultActivity.this, R.layout.dialog_cancel_or_confirm, null);
                    TextView text = (TextView) view.findViewById(R.id.text);
                    text.setText(getResourceString(R.string.call_customer_service_phone) + phone.getText() + "?");
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
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText()));
                            startActivity(intent);
                            alertDialogPhone.dismiss();
                        }
                    });
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RechargePayResultActivity.this);
                    alertDialogPhone = builder.create();
                    alertDialogPhone.show();
                    alertDialogPhone.setContentView(view);
                } else {
                    alertDialogPhone.show();
                }
                break;
            case R.id.action1:
                // TODO: 2016/9/27  充值
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.action2:
                // TODO: 2016/9/27  充值记录
                intent = new Intent(this, RecordFundActivity.class);
                intent.putExtra("page",0);
                startActivity(intent);
                finish();
                break;
            case R.id.button_over:
                // TODO: 2016/9/27  充值
//                intent = new Intent(this, PersonalMyWalletActivity.class);
//                startActivity(intent);
                finish();
                break;
        }
    }
}
