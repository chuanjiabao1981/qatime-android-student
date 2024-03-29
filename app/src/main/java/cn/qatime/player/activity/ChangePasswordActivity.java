package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2016/8/17.
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private TextView forgetPassword;
    private EditText password;
    private EditText newPassword;
    private EditText confirmNewPassword;
    private Button buttonOver;
    private String password1;
    private String password2;
    private String password3;


    private void assignViews() {
        password = (EditText) findViewById(R.id.password);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        confirmNewPassword = (EditText) findViewById(R.id.confirm_new_password);
        buttonOver = (Button) findViewById(R.id.button_over);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initView();
    }

    private void initView() {
        setTitles(getResources().getString(R.string.change_password));
        assignViews();

        password.setHint(StringUtils.getSpannedString(this, R.string.hint_input_current_password));
        newPassword.setHint(StringUtils.getSpannedString(this, R.string.input_new_password));
        confirmNewPassword.setHint(StringUtils.getSpannedString(this, R.string.confirm_new_password));


        forgetPassword.setOnClickListener(this);
        buttonOver.setOnClickListener(this);
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
                map.put("id", "" + BaseApplication.getInstance().getUserId());
                map.put("current_password", password1);
                map.put("password", password2);
                map.put("password_confirmation", password3);

                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.getUrl(UrlUtils.urlUser + BaseApplication.getInstance().getUserId() + "/password", map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        BaseApplication.getInstance().clearToken();
                        Toast.makeText(ChangePasswordActivity.this, getResourceString(R.string.change_password_success), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        intent.putExtra("sign", "exit_login");
                        startActivity(intent);
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(ChangePasswordActivity.this, getResourceString(R.string.password_error), Toast.LENGTH_SHORT).show();
                    }
                }, new VolleyErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }));
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
