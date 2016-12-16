package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.CustomKeyboard;
import cn.qatime.player.view.PayEditText;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2016/12/14 16:31
 * @Description:
 */
public class PayPSWVerifyActivity extends BaseActivity {
    private PayEditText payEditText;
    private CustomKeyboard customKeyboard;
    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "", "0", "<<"
    };
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_password);
        initView();
        setSubView();
        initEvent();
    }

    private void initView() {
        setTitle("验证支付密码");
        payEditText = (PayEditText) findViewById(R.id.PayEditText_pay);
        customKeyboard = (CustomKeyboard) findViewById(R.id.KeyboardView_pay);
        payEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customKeyboard.setVisibility(customKeyboard.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
//                KeyBoardUtils.openKeybord(editText,PayPSWVerifyActivity.this);
            }
        });
    }

    private void setSubView() {
        //设置键盘
        customKeyboard.setKeyboardKeys(KEY);
    }

    private void initEvent() {

        customKeyboard.setOnClickKeyboardListener(new CustomKeyboard.OnClickKeyboardListener() {
            @Override
            public void onKeyClick(int position, String value) {
                if (position < 11 && position != 9) {
                    payEditText.add(value);
                } else if (position == 11) {
                    payEditText.remove();
                }
            }
        });

        /**
         * 当密码输入完成时的回调
         */
        payEditText.setOnInputChangeListener(new PayEditText.OnInputChangeListener() {
            @Override
            public void onInputFinished(String password) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("current_pament_password", password);
                DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.cashAccounts + BaseApplication.getUserId() + "/password/ticket_token", map), null,
                        new VolleyListener(PayPSWVerifyActivity.this) {
                            @Override
                            protected void onSuccess(JSONObject response) {
                                Logger.e("密码正确");
                                Intent intent = new Intent(PayPSWVerifyActivity.this, PayPSWChangeActivity.class);
                                try {
                                    intent.putExtra("ticket_token",response.getString("data"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(intent);
                                finish();
                            }

                            protected void onError(JSONObject response) {
                                payEditText.clear();
                                try {
                                    int errorCode = response.getJSONObject("error").getInt("code");
                                    if (errorCode == 2005) {
                                        Toast.makeText(PayPSWVerifyActivity.this, "密码验证失败", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PayPSWVerifyActivity.this, "请先设置支付密码", Toast.LENGTH_SHORT).show();
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

                    }
                });
                addToRequestQueue(request);
            }

            @Override
            public void onInputAdd(String value) {

            }

            @Override
            public void onInputRemove() {

            }
        });
    }
}
