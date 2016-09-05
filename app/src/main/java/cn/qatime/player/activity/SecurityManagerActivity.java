package cn.qatime.player.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyListener;

public class SecurityManagerActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout bindPhoneNumber;
    private TextView phoneNumberM;
    private LinearLayout bindEmail;
    private TextView email;
    private LinearLayout parentPhoneNumber;
    private TextView phoneNumberP;
    private LinearLayout changePassword;

    private void assignViews() {
        bindPhoneNumber = (LinearLayout) findViewById(R.id.bind_phone_number);
        bindEmail = (LinearLayout) findViewById(R.id.bind_email);
        email = (TextView) findViewById(R.id.email);
        parentPhoneNumber = (LinearLayout) findViewById(R.id.parent_phone_number);
        phoneNumberP = (TextView) findViewById(R.id.phone_number_p);
        phoneNumberM = (TextView) findViewById(R.id.phone_number_m);
        changePassword = (LinearLayout) findViewById(R.id.change_password);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_manager);

        initView();
        initData();
    }

    private void initData() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {

            }

            @Override
            protected void onSuccess(JSONObject response) {
                Logger.e("学生信息：  " + response.toString());
                PersonalInformationBean bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);
                setValue(bean);
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(SecurityManagerActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        addToRequestQueue(request);

    }

    private void setValue(PersonalInformationBean bean) {
        String parentPhone = bean.getData().getParent_phone();
        if (parentPhone != null) {
            phoneNumberP.setText("" + parentPhone);
            phoneNumberP.setTextColor(Color.BLACK);
        } else {
            phoneNumberP.setText("未绑定");
            phoneNumberP.setTextColor(Color.RED);
        }
        String email = bean.getData().getEmail();
        if (email != null) {
            this.email.setText("" + email);
            this.email.setTextColor(Color.BLACK);
        } else {
            this.email.setText("未绑定");
            this.email.setTextColor(Color.RED);
        }

        String loginMobile = bean.getData().getLogin_mobile();
        if (loginMobile != null) {
            phoneNumberM.setText("" + loginMobile);
            phoneNumberM.setTextColor(Color.BLACK);
        } else {
            phoneNumberM.setText("未绑定");
            phoneNumberM.setTextColor(Color.RED);
        }
    }


    private void initView() {
        setTitle(getResources().getString(R.string.security_management));
        assignViews();

        bindPhoneNumber.setOnClickListener(this);
        bindEmail.setOnClickListener(this);
        parentPhoneNumber.setOnClickListener(this);
        changePassword.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_phone_number://绑定手机
                Intent intent = new Intent(this, VerifyPhoneActivity.class);
                intent.putExtra("next", "phone");
                startActivityForResult(intent, Constant.REQUEST_EXIT_LOGIN);
                break;
            case R.id.bind_email://绑定邮箱
                intent = new Intent(this, VerifyPhoneActivity.class);
                intent.putExtra("next", "email");
                startActivity(intent);
                break;
            case R.id.parent_phone_number://家长手机
                intent = new Intent(this, ParentPhoneActivity.class);
                intent.putExtra("phoneP", phoneNumberP.getText());
                startActivity(intent);
                break;
            case R.id.change_password://修改密码
                intent = new Intent(this, ChangePasswordActivity.class);
                startActivityForResult(intent, Constant.REQUEST_EXIT_LOGIN);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_EXIT_LOGIN && resultCode == Constant.RESPONSE_EXIT_LOGIN) {
            setResult(Constant.RESPONSE_EXIT_LOGIN);
            finish();
        }
    }
}
