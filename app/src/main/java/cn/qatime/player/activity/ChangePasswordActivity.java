package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.SPUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2016/8/17.
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextView forgetPassword;
    private EditText password;
    private EditText newPassword;
    private EditText confirmNewPassword;
    private ImageView matchPwd1;
    private ImageView matchPwd2;
    private Button buttonOver;
    private String password1;
    private String password2;
    private String password3;


    private void assignViews() {
        password = (EditText) findViewById(R.id.password);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        matchPwd1 = (ImageView) findViewById(R.id.match_pwd1);
        confirmNewPassword = (EditText) findViewById(R.id.confirm_new_password);
        matchPwd2 = (ImageView) findViewById(R.id.match_pwd2);
        buttonOver = (Button) findViewById(R.id.button_over);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
    }

    private void initView() {
        setTitle(getResources().getString(R.string.change_password));
        assignViews();

        password.setHint(StringUtils.getSpannedString(this, R.string.hint_input_current_password));
        newPassword.setHint(StringUtils.getSpannedString(this, R.string.hint_6_16_password));
        confirmNewPassword.setHint(StringUtils.getSpannedString(this, R.string.hint_input_again));


        matchPwd1 = (ImageView) findViewById(R.id.match_pwd1);
        matchPwd2 = (ImageView) findViewById(R.id.match_pwd2);
        forgetPassword.setOnClickListener(this);
        buttonOver.setOnClickListener(this);
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password2 = newPassword.getText().toString().trim();
                matchPwd1.setVisibility(View.VISIBLE);
                matchPwd1.setImageResource(StringUtils.isGoodPWD(password2) ? R.mipmap.pay_success : R.mipmap.pay_faild);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password3 = confirmNewPassword.getText().toString().trim();
                matchPwd2.setVisibility(View.VISIBLE);
                matchPwd2.setImageResource(StringUtils.isGoodPWD(password3) ? R.mipmap.pay_success : R.mipmap.pay_faild);
                matchPwd2.setImageResource(password3.equals(password2) ? R.mipmap.pay_success : R.mipmap.pay_faild);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        password1 = password.getText().toString().trim();
        password2 = newPassword.getText().toString().trim();
        password3 = confirmNewPassword.getText().toString().trim();
        switch (v.getId()) {
            case R.id.forget_password:
                Intent intent = new Intent(this, ForgetPasswordActivity.class);
                intent.putExtra("status_login", true);
                startActivity(intent);
                break;
            case R.id.button_over:
                if (!(StringUtils.isGoodPWD(password1) || StringUtils.isGoodPWD(password2) || StringUtils.isGoodPWD(password3))) {
                    Toast.makeText(this, getResources().getString(R.string.password_6_16), Toast.LENGTH_LONG).show();
                    return;
                }
                if (!password2.equals(password3)) {
                    Toast.makeText(this, getResources().getString(R.string.password_and_repassword_are_incongruous), Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("id", "" + BaseApplication.getUserId());
                map.put("current_password", password1);
                map.put("password", password2);
                map.put("password_confirmation", password3);

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.getUrl(UrlUtils.urlUser + BaseApplication.getUserId() + "/password", map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {

                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        try {

                            if (!response.isNull("data")) {
                                Logger.e("验证成功");
                                Toast.makeText(ChangePasswordActivity.this, "密码修改成功，请用新密码重新登录", Toast.LENGTH_SHORT).show();
                                BaseApplication.clearToken();
                                setResult(Constant.RESPONSE_EXIT_LOGIN);
                                SPUtils.put(ChangePasswordActivity.this,"profile",BaseApplication.getProfile());
                                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                intent.putExtra("sign", "exit_login");
                                startActivity(intent);
                                finish();
                            } else {
                                JSONObject error = response.getJSONObject("error");
                                Toast.makeText(ChangePasswordActivity.this, error.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }));
                break;
        }
    }
}
