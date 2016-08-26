package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyListener;

public class SecurityManagerActivity extends BaseActivity implements View.OnClickListener {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initData() {

        // TODO: 2016/8/26 改为学生信息URL
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {

            }

            @Override
            protected void onSuccess(JSONObject response) {
                Logger.e("学生信息：  " + response.toString());
                PersonalInformationBean bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);

            }

            @Override
            protected void onError(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        addToRequestQueue(request);

    }

    private void setValue(PersonalInformationBean bean) {
// TODO: 2016/8/26 设置学生信息
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_phone_number://绑定手机
                Intent intent = new Intent(this, UnbindPhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.bind_email://绑定邮箱
                intent = new Intent(this, BindEmailActivity.class);
                startActivity(intent);
                break;
            case R.id.parent_phone_number://家长手机
                intent = new Intent(this, ParentPhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.change_password://修改密码
                intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
        }
    }

}
