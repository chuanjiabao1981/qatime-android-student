package cn.qatime.player.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.CustomKeyboard;
import cn.qatime.player.view.PayEditText;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.SPUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class SecurityManagerActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout bindPhoneNumber;
    private TextView phoneNumberM;
    private LinearLayout bindEmail;
    private TextView email;
    private LinearLayout parentPhoneNumber;
    private TextView phoneNumberP;
    private LinearLayout changePassword;
    private LinearLayout changePayPassword;
    private View bindWeChat;
    private TextView weChat;
    private IWXAPI api;
    private String openid;
    private PayEditText payEditText;
    private CustomKeyboard customKeyboard;
    private AlertDialog alertDialog;
    private TextView payPswText;

    private void assignViews() {
        bindPhoneNumber = (LinearLayout) findViewById(R.id.bind_phone_number);
        bindEmail = (LinearLayout) findViewById(R.id.bind_email);
        bindWeChat = findViewById(R.id.bind_wechat);
        weChat = (TextView) findViewById(R.id.wechat);
        email = (TextView) findViewById(R.id.email);
        payPswText = (TextView) findViewById(R.id.pay_psw);
        parentPhoneNumber = (LinearLayout) findViewById(R.id.parent_phone_number);
        phoneNumberP = (TextView) findViewById(R.id.phone_number_p);
        phoneNumberM = (TextView) findViewById(R.id.phone_number_m);
        changePassword = (LinearLayout) findViewById(R.id.change_password);
        changePayPassword = (LinearLayout) findViewById(R.id.change_pay_password);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_manager);

        EventBus.getDefault().register(this);
        initView();
        initData();
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constant.APP_ID);
    }

    private void initData() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                Logger.e("学生信息：  " + response.toString());
                PersonalInformationBean bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);
                if (bean != null && bean.getData() != null) {
                    setValue(bean);
                }
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(SecurityManagerActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);

    }

    private void setValue(PersonalInformationBean bean) {
        enableClick(true);
        String parentPhone = bean.getData().getParent_phone();
        if (parentPhone != null) {
            phoneNumberP.setText("" + parentPhone);
            phoneNumberP.setTextColor(0xff666666);
        } else {
            phoneNumberP.setText(getResourceString(R.string.not_bind));
            phoneNumberP.setTextColor(Color.RED);
        }
        String email = bean.getData().getEmail();
        if (email != null) {
            this.email.setText("" + email);
            this.email.setTextColor(0xff666666);
        } else {
            this.email.setText(getResourceString(R.string.not_bind));
            this.email.setTextColor(Color.RED);
        }
        // TODO: 2017/3/7 将支付密码信息整合到接口中
        if (BaseApplication.getCashAccount() != null && BaseApplication.getCashAccount().getData() != null) {
            if (BaseApplication.getCashAccount().getData().isHas_password()) {
                // TODO: 2017/3/7 判断密码是否可用
                long changeAt = (long) SPUtils.get(this, "pay_pwd_change_at", 0l);

                int diff = 24 - (int) ((System.currentTimeMillis() - changeAt) / (1000 * 3600));
                if (diff <= 24&&diff > 0) {
                    payPswText.setText(getString(R.string.new_pay_password_invalid, diff));
                    payPswText.setTextColor(0xff999999);
                } else {
                    payPswText.setText("");
                }
            } else {
                payPswText.setText(getResourceString(R.string.not_set));
                payPswText.setTextColor(Color.RED);
            }
        }
        openid = bean.getData().getOpenid();
        if (!StringUtils.isNullOrBlanK(openid)) {
            weChat.setTextColor(0xff666666);
            weChat.setBackgroundResource(R.drawable.shape_wechat_bind_background_able);
            weChat.setText(getResourceString(R.string.bind_cancel));
        } else {
            weChat.setTextColor(0xffffffff);
            weChat.setBackgroundResource(R.drawable.shape_wechat_bind_background_unable);
            weChat.setText(getResourceString(R.string.bind_rightnow));
        }
        String loginMobile = bean.getData().getLogin_mobile();
        if (loginMobile != null) {
            phoneNumberM.setText("" + loginMobile);
            phoneNumberM.setTextColor(0xff666666);
        } else {
            phoneNumberM.setText(getResourceString(R.string.not_bind));
            phoneNumberM.setTextColor(Color.RED);
        }
    }


    private void initView() {
        setTitles(getResources().getString(R.string.security_management));
        assignViews();

        enableClick(false);
        bindPhoneNumber.setOnClickListener(this);
        bindEmail.setOnClickListener(this);
        bindWeChat.setOnClickListener(this);
        parentPhoneNumber.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        changePayPassword.setOnClickListener(this);
    }

    private void enableClick(boolean b) {
        bindPhoneNumber.setEnabled(b);
        bindEmail.setEnabled(b);
        bindWeChat.setEnabled(b);
        parentPhoneNumber.setEnabled(b);
        changePassword.setEnabled(b);
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
            case R.id.bind_wechat://绑定微信
                if (!StringUtils.isNullOrBlanK(openid)) {
                    dialogCancel();
                } else {
                    //绑定
                    if (!api.isWXAppInstalled()) {
                        Toast.makeText(this, R.string.wechat_not_installed, Toast.LENGTH_SHORT).show();
                    } else if (!api.isWXAppSupportAPI()) {
                        Toast.makeText(this, R.string.wechat_not_support, Toast.LENGTH_SHORT).show();
                    } else {
                        SendAuth.Req req = new SendAuth.Req();
                        req.scope = "snsapi_userinfo";
                        req.state = "wechat_info";
                        api.sendReq(req);
                    }
                }
                break;
            case R.id.parent_phone_number://家长手机
                intent = new Intent(this, ParentPhoneActivity.class);
                intent.putExtra("phoneP", phoneNumberP.getText());
                startActivity(intent);
                break;
            case R.id.change_password://修改密码
                intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.change_pay_password://修改支付密码
                dialogNotify();
                break;
        }
    }


    @Subscribe
    public void onEvent(String code) {
        if (code.equals("pay_pwd_change")) {
            // TODO: 2017/3/7 判断密码是否可用
            long changeAt = (long) SPUtils.get(this, "pay_pwd_change_at", 0l);

            int diff = 24 - (int) ((System.currentTimeMillis() - changeAt) / (1000 * 3600));
            if (diff <= 24 && diff > 0) {
                payPswText.setText(getString(R.string.new_pay_password_invalid, diff));
                payPswText.setTextColor(0xff666666);
            } else {
                payPswText.setText("");
            }
        } else {
            //收到微信登錄code
            Map<String, String> map = new HashMap<>();
            map.put("code", code);
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST,
                    UrlUtils.getUrl(UrlUtils.urlUser + BaseApplication.getUserId() + "/wechat", map), null, new VolleyListener(SecurityManagerActivity.this) {
                @Override
                protected void onTokenOut() {
                    tokenOut();
                }

                @Override
                protected void onSuccess(JSONObject response) {
                    Logger.e("微信綁定" + response.toString());
                    initData();
                }

                @Override
                protected void onError(JSONObject response) {
                    enableClick(true);
                    Toast.makeText(SecurityManagerActivity.this, R.string.bind_error, Toast.LENGTH_SHORT).show();
                }
            }, new VolleyErrorListener());
            addToRequestQueue(request);
        }
    }

    private void dialogNotify() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(this, R.layout.dialog_cancel_or_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(R.string.change_pay_password_notify);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setText(R.string.continue_anyway);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                changePayPSW();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
    }


    private void changePayPSW() {
//        PayPopView payPopView = new PayPopView("","",getWindow());
//        payPopView.showPop();
        if (BaseApplication.getCashAccount() != null && BaseApplication.getCashAccount().getData() != null) {
            if (BaseApplication.getCashAccount().getData().isHas_password()) {
                startActivity(new Intent(this, PayPSWVerifyActivity.class));
            } else {
                startActivity(new Intent(this, PayPSWForgetActivity.class));
            }
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            Logger.e("未获取到支付密码状态");
        }
    }

    private void dialogCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(this, R.layout.dialog_cancel_or_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(R.string.cancel_bind_weixin_notify);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                enableClick(true);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                cancelBindWechat();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
    }

    /**
     * 取消綁定wechat
     */
    private void cancelBindWechat() {
        Map<String, String> map = new HashMap<>();
        map.put("openid", openid);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.DELETE,
                UrlUtils.getUrl(UrlUtils.urlUser + BaseApplication.getUserId() + "/wechat", map), null, new VolleyListener(SecurityManagerActivity.this) {
            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                Logger.e("quxiao" + response.toString());
                initData();
            }

            @Override
            protected void onError(JSONObject response) {
                enableClick(true);
            }
        }, new VolleyErrorListener());
        addToRequestQueue(request);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
