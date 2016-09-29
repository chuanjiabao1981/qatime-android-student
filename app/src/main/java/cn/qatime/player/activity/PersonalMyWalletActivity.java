package cn.qatime.player.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2016/9/27 15:52
 * @Description:
 */
public class PersonalMyWalletActivity extends BaseActivity implements View.OnClickListener {
    private TextView balance;
    private TextView yuan;
    private TextView consumption;
    private LinearLayout rechargeRecord;
    private LinearLayout consumptionRecord;
    private TextView phone;
    private TextView recharge;
    private Dialog alertDialog;
    DecimalFormat df = new DecimalFormat("#.00");

    private void assignViews() {
        balance = (TextView) findViewById(R.id.balance);
        yuan = (TextView) findViewById(R.id.yuan);
        consumption = (TextView) findViewById(R.id.consumption);
        rechargeRecord = (LinearLayout) findViewById(R.id.recharge_record);
        consumptionRecord = (LinearLayout) findViewById(R.id.consumption_record);
        phone = (TextView) findViewById(R.id.phone);
        recharge = (TextView) findViewById(R.id.recharge);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_my_wallet);
        setTitle(getResourceString(R.string.my_wallet));
        setRightText("说明", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalMyWalletActivity.this, RechargeProcessActivity.class);
                startActivity(intent);
            }
        });
        assignViews();
        recharge.setOnClickListener(this);
        phone.setOnClickListener(this);
        consumptionRecord.setOnClickListener(this);
        rechargeRecord.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                    balance.setText(price);
                    String price1 = df.format(Double.valueOf(response.getJSONObject("data").getString("total_expenditure")));
                    if (price1.startsWith(".")) {
                        price1 = "0" + price1;
                    }
                    consumption.setText(price1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(PersonalMyWalletActivity.this, getResourceString(R.string.get_wallet_info_error), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PersonalMyWalletActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        }));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone:
                if (alertDialog == null) {
                    View view = View.inflate(PersonalMyWalletActivity.this, R.layout.dialog_cancel_or_confirm, null);
                    TextView text = (TextView) view.findViewById(R.id.text);
                    text.setText(getResourceString(R.string.call_customer_service_phone) + phone.getText() + "?");
                    Button cancel = (Button) view.findViewById(R.id.cancel);
                    Button confirm = (Button) view.findViewById(R.id.confirm);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText()));
                            startActivity(intent);
                            alertDialog.dismiss();
                        }
                    });
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PersonalMyWalletActivity.this);
                    alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.setContentView(view);
                } else {
                    alertDialog.show();
                }
                break;
            case R.id.recharge:
                // TODO: 2016/9/27  充值
                Intent intent = new Intent(this, RechargeActivity.class);
                intent.putExtra("page", 0);
                startActivity(intent);
                break;
            case R.id.recharge_record:
                intent = new Intent(this, RecordFundActivity.class);
                intent.putExtra("page", 0);
                startActivity(intent);
                break;
            case R.id.consumption_record:
                intent = new Intent(this, RecordFundActivity.class);
                intent.putExtra("page", 1);
                startActivity(intent);
                break;
        }
    }
}
