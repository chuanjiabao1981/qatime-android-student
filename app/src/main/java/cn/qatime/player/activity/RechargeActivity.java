package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2016/9/27 18:33
 * @Description:
 */
public class RechargeActivity extends BaseActivity {
    private EditText rechargeNum;
    private RadioGroup radioGroup;
    private RadioButton wechatPay;
    private RadioButton alipay;
    private Button rechargeNow;
    private AlertDialog alertDialog;

    private void assignViews() {
        rechargeNum = (EditText) findViewById(R.id.recharge_num);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        wechatPay = (RadioButton) findViewById(R.id.wechat_pay);
        alipay = (RadioButton) findViewById(R.id.alipay);
        rechargeNow = (Button) findViewById(R.id.recharge_now);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        setTitle(getResourceString(R.string.recharge_choice));
        assignViews();
        initListener();
    }

    private void initListener() {
        rechargeNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.isNullOrBlanK(rechargeNum.getText().toString())){
                    Toast.makeText(RechargeActivity.this, R.string.amount_can_not_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                rechargeNow.setEnabled(false);
                String amount = rechargeNum.getText().toString();
                String pay_type = radioGroup.getCheckedRadioButtonId() == R.id.wechat_pay ? "weixin" : "alipay";
                Map<String, String> map = new HashMap<>();
                map.put("amount", amount);
                map.put("pay_type", pay_type);
                addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getUserId() + "/recharges", map), null, new VolleyListener(RechargeActivity.this) {

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                        rechargeNow.setEnabled(true);
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        if (!response.isNull("data")) {
                            try {
                                Intent intent = new Intent(RechargeActivity.this, RechargeConfirmActivity.class);
                                JSONObject data = response.getJSONObject("data");
                                intent.putExtra("id", data.getString("id"));
                                intent.putExtra("amount", data.getString("amount"));
                                intent.putExtra("pay_type", data.getString("pay_type"));
                                intent.putExtra("created_at", data.getString("created_at"));
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            dialog();
                        }
                        rechargeNow.setEnabled(true);
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        dialog();
                        rechargeNow.setEnabled(true);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        rechargeNow.setEnabled(true);
                    }
                }));
            }
        });
    }

    protected void dialog() {
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            alertDialog = builder.create();
            View view = View.inflate(this, R.layout.dialog_confirm, null);
            Button confirm = (Button) view.findViewById(R.id.confirm);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            alertDialog.setContentView(view);
        } else {
            alertDialog.show();
        }
    }
}
