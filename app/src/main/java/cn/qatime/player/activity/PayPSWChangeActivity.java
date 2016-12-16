package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
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
import cn.qatime.player.view.CustomKeyboard;
import cn.qatime.player.view.PayEditText;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2016/12/16 9:53
 * @Description:
 */
public class PayPSWChangeActivity extends BaseActivity implements View.OnClickListener {
    private PayEditText payEditText;
    private CustomKeyboard customKeyboard;
    private Button over;
    private String tempPassword;

    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "", "0", "<<"
    };
    private EditText editText;

    private void assignViews() {
        payEditText = (PayEditText) findViewById(R.id.PayEditText_pay);
        over = (Button) findViewById(R.id.over);
        customKeyboard = (CustomKeyboard) findViewById(R.id.KeyboardView_pay);


        setTitle("设置新支付密码");
        payEditText.setOnClickListener(this);
        over.setOnClickListener(this);
        over.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pay_psw);
        assignViews();
        setSubView();
        initEvent();
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
                if (over.getVisibility() == View.INVISIBLE) {
                    //下一步
                    tempPassword = password;
                    payEditText.clear();
                    over.setVisibility(View.VISIBLE);
                    over.setEnabled(false);
                    Toast.makeText(PayPSWChangeActivity.this, "请确认您的支付密码", Toast.LENGTH_SHORT).show();
                    setTitle("确认新支付密码");
                }else{
                    over.setEnabled(true);
                }
            }

            @Override
            public void onInputAdd(String value) {

            }

            @Override
            public void onInputRemove() {
                over.setEnabled(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.over:
                if (!tempPassword.equals(payEditText.getText())) {
                    //重置
                    over.setVisibility(View.INVISIBLE);
                    tempPassword = "";
                    payEditText.clear();
                    Toast.makeText(PayPSWChangeActivity.this, "两次密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    setTitle("设置新支付密码");
                }
                Map<String, String> map = new HashMap<>();
                map.put("pament_password", tempPassword);
                map.put("ticket_token", getIntent().getStringExtra("ticket_token"));
                DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.cashAccounts + BaseApplication.getUserId() + "/password", map), null,
                        new VolleyListener(PayPSWChangeActivity.this) {
                            @Override
                            protected void onSuccess(JSONObject response) {
                                Toast.makeText(PayPSWChangeActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            protected void onError(JSONObject response) {
                                try {
                                    int errorCode = response.getJSONObject("error").getInt("code");
                                    if (errorCode == 2005) {
                                        Toast.makeText(PayPSWChangeActivity.this, "密码验证失败", Toast.LENGTH_SHORT).show();
                                    } else if (errorCode == 2003) {
                                        Toast.makeText(PayPSWChangeActivity.this, "无效的验证码", Toast.LENGTH_SHORT).show();
                                    } else if (errorCode == 2007) {
                                        Toast.makeText(PayPSWChangeActivity.this, "授权token无效", Toast.LENGTH_SHORT).show();
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
                break;
            case R.id.PayEditText_pay:
                customKeyboard.setVisibility(customKeyboard.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
                break;
        }
    }
}
