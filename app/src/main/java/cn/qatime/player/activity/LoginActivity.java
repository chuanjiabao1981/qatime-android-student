package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.Profile;
import cn.qatime.player.utils.CheckUtil;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.SPUtils;
import cn.qatime.player.utils.StringUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VolleyErrorListener;
import cn.qatime.player.view.CheckView;

/**
 * 登陆页
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private int errornum = 0;
    private CheckView checkview;
    private int[] checkNum = null;
    private View checklayout;
    private EditText checkcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checklayout = findViewById(R.id.checklayout);
        checkview = (CheckView) findViewById(R.id.checkview);
        checkcode = (EditText) findViewById(R.id.checkcode);
        username = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.pass);
        Button login = (Button) findViewById(R.id.login);
        Button register = (Button) findViewById(R.id.register);
        View loginerror = findViewById(R.id.login_error);
        View reload = findViewById(R.id.reload);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        loginerror.setOnClickListener(this);
        reload.setOnClickListener(this);
        checkview.setOnClickListener(this);

        if (!StringUtils.isNullOrBlanK(SPUtils.get(LoginActivity.this, "username", ""))) {
            username.setText(SPUtils.get(LoginActivity.this, "username", "").toString());
            if (!StringUtils.isNullOrBlanK(SPUtils.get(LoginActivity.this, "password", ""))) {
                password.setText(SPUtils.get(LoginActivity.this, "password", "").toString());
            }
        }
        String sign = getIntent().getStringExtra("sign");//从系统设置退出登录页面跳转而来，清除用户登录信息
        if (!StringUtils.isNullOrBlanK(sign) && sign.equals("exit_login")) {
            username.setText("");
            password.setText("");
        }
//        username.setText("15617685965@163.com");
//        password.setText("123456");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login://登陆
                if (checklayout.getVisibility() == View.VISIBLE) {
                    if (CheckUtil.checkNum(checkcode.getText().toString(), checkNum)) {
                        login();
                    } else {
                        Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    login();
                }

                break;
            case R.id.register://注册
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_error://忘记密码
                intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.reload://重新换验证码
                initCheckNum();
                break;
            case R.id.checkview://重新换验证码
                initCheckNum();
                break;
        }
    }

    private void login() {

        if (TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(password.getText().toString())) {
            Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("email", username.getText().toString());
        map.put("password", password.getText().toString());
        map.put("client_type", "app");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlLogin, map), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        LogUtils.e("登录", jsonObject.toString());
                        SPUtils.put(LoginActivity.this, "username", username.getText().toString());
                        SPUtils.put(LoginActivity.this, "password", password.getText().toString());
                        Gson gson = new Gson();
                        Profile profile = gson.fromJson(jsonObject.toString(), Profile.class);
                        if (profile != null && !TextUtils.isEmpty(profile.getData().getRemember_token())) {
                            BaseApplication.setProfile(profile);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //没有数据或token
                        }
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                LogUtils.e(volleyError.getMessage());
                password.setText("");
                //当密码错误5次以上，开始使用验证码
                errornum++;
                if (errornum >= 5) {
                    checklayout.setVisibility(View.VISIBLE);
                    initCheckNum();
                }
            }
        });
        addToRequestQueue(request);
    }

    /**
     * 刷新验证码
     */
    private void initCheckNum() {
        checkNum = CheckUtil.getCheckNum();
        checkview.setCheckNum(checkNum);
        checkview.invaliChenkNum();
    }
}
