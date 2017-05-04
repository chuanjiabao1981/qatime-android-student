package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.ProvincesBean;
import cn.qatime.player.config.UserPreferences;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.cache.UserInfoCache;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.SPUtils;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.CityBean;
import libraryextra.bean.GradeBean;
import libraryextra.bean.Profile;
import libraryextra.utils.AppUtils;
import libraryextra.utils.DialogUtils;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.CustomProgressDialog;
import libraryextra.view.WheelView;

/**
 * @author lungtify
 * @Time 2016/12/1 11:29
 * @Describe 微信绑定页面
 */

public class WeChatBindActivity extends BaseActivity implements View.OnClickListener {
    private EditText phone;
    private EditText code;
    private Button getCode;
    private EditText password;
    private TextView grade;
    private TextView region;
    private CheckBox checkBox;
    private TextView agreement;
    private Button next;
    private TimeCount time;
    private String openid;
    private Profile profile;
    private CustomProgressDialog progress;
    private AlertDialog alertDialog;
    private List<String> grades;
    private CityBean.Data city;
    private ProvincesBean.DataBean province;

    private void assignViews() {
        phone = (EditText) findViewById(R.id.phone);
        code = (EditText) findViewById(R.id.code);
        getCode = (Button) findViewById(R.id.get_code);
        password = (EditText) findViewById(R.id.password);
        grade = (TextView) findViewById(R.id.grade);
        region = (TextView) findViewById(R.id.region);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        agreement = (TextView) findViewById(R.id.agreement);
        next = (Button) findViewById(R.id.next);

        time = new TimeCount(60000, 1000);
        getCode.setOnClickListener(this);
        grade.setOnClickListener(this);
        region.setOnClickListener(this);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                next.setEnabled(isChecked);
            }
        });
        next.setOnClickListener(this);

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isPhone(phone.getText().toString().trim())) {
                    if (!time.ticking) {
                        getCode.setEnabled(true);
                    }
                } else {
                    getCode.setEnabled(false);
                    if (phone.getText().toString().length() == 11) {
                        Toast.makeText(WeChatBindActivity.this, R.string.phone_number_is_incorrect, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_wechat);
        setTitles(getString(R.string.bind));
        openid = getIntent().getStringExtra("openid");
        assignViews();
        grades = new ArrayList<>();
        String gradeString = FileUtil.readFile(getFilesDir() + "/grade.txt");
        if (!StringUtils.isNullOrBlanK(gradeString)) {
            GradeBean gradeBean = JsonUtils.objectFromJson(gradeString, GradeBean.class);
            grades = gradeBean.getData().getGrades();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code://获取验证码
                if (StringUtils.isPhone(phone.getText().toString().trim())) {
                    time.start();
                    getCode();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.next:
                next();
                break;
            case R.id.grade:
                showGradePickerDialog();
                break;
            case R.id.region:
                Intent regionIntent = new Intent(this, RegionSelectActivity1.class);
                startActivityForResult(regionIntent, Constant.REQUEST_REGION_SELECT);
                break;
            case R.id.agreement:
                //// TODO: 2016/8/24 点击协议查看
                break;
        }
    }

    private void showGradePickerDialog() {
        if (alertDialog == null) {
            final View view = View.inflate(WeChatBindActivity.this, R.layout.dialog_grade_picker, null);
            final WheelView wheelView = (WheelView) view.findViewById(R.id.grade);
            wheelView.setOffset(1);
            wheelView.setItems(grades);
            wheelView.setSeletion(grades.indexOf(grade.getText()));
            wheelView.setonItemClickListener(new WheelView.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    alertDialog.dismiss();
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(WeChatBindActivity.this);
            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setContentView(view);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    grade.setText(wheelView.getSeletedItem());
                }
            });
        } else {
            alertDialog.show();
        }
    }

    private void getCode() {
        Map<String, String> map = new HashMap<>();
        map.put("send_to", phone.getText().toString().trim());
        map.put("key", "register_captcha");
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlGetCode, map), null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                Toast.makeText(WeChatBindActivity.this, getResourceString(R.string.code_send_success), Toast.LENGTH_SHORT).show();
                Logger.e("验证码发送成功" + phone.getText().toString().trim() + "---" + response.toString());
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(WeChatBindActivity.this, getResourceString(R.string.code_send_failed), Toast.LENGTH_SHORT).show();
            }


        }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });
        addToRequestQueue(request);
    }

    private void next() {
        if (StringUtils.isNullOrBlanK(phone.getText().toString().trim())) {//账号为空
            Toast.makeText(this, getResources().getString(R.string.account_can_not_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!StringUtils.isPhone(phone.getText().toString().trim())) {//手机号不正确
            Toast.makeText(this, getResources().getString(R.string.phone_number_is_incorrect), Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isNullOrBlanK(code.getText().toString().trim())) { //验证码
            Toast.makeText(this, getResources().getString(R.string.enter_the_verification_code), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isGoodPWD(password.getText().toString().trim())) {
            Toast.makeText(this, getResources().getString(R.string.password_6_16), Toast.LENGTH_LONG).show();
            return;
        }
        if (StringUtils.isNullOrBlanK(grade.getText().toString().trim())) {
            Toast.makeText(this, getResourceString(R.string.grade_can_not_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("login_mobile", phone.getText().toString().trim());
        map.put("captcha_confirmation", code.getText().toString().trim());
        map.put("password", password.getText().toString().trim());
        map.put("accept", "" + (checkBox.isChecked() ? 1 : 0));
        map.put("type", "Student");
        map.put("client_type", "app");
        map.put("province_id", province.getId());
        map.put("city_id", city.getId());
        try {
            map.put("grade", URLEncoder.encode(grade.getText().toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        map.put("openid", openid);


        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlWeChatRegister, map),
                null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {
                tokenOut();
            }

            @Override
            protected void onSuccess(JSONObject response) {
                profile = JsonUtils.objectFromJson(response.toString(), Profile.class);

                if (profile != null && profile.getData() != null && profile.getData().getUser() != null && profile.getData().getUser().getId() != 0) {
                    PushAgent.getInstance(WeChatBindActivity.this).addAlias(String.valueOf(profile.getData().getUser().getId()), "student", new UTrack.ICallBack() {
                        @Override
                        public void onMessage(boolean b, String s) {

                        }
                    });
                    String deviceToken = PushAgent.getInstance(WeChatBindActivity.this).getRegistrationId();
                    if (!StringUtils.isNullOrBlanK(deviceToken)) {
                        Map<String, String> m = new HashMap<>();
                        m.put("user_id", String.valueOf(profile.getData().getUser().getId()));
                        m.put("device_token", deviceToken);
                        m.put("device_model", Build.MODEL);
                        try {
                            m.put("app_name", URLEncoder.encode(AppUtils.getAppName(WeChatBindActivity.this), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        m.put("app_version", AppUtils.getVersionName(WeChatBindActivity.this));
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlDeviceInfo, m), null,
                                new VolleyListener(WeChatBindActivity.this) {

                                    @Override
                                    protected void onSuccess(JSONObject response) {
                                    }

                                    @Override
                                    protected void onError(JSONObject response) {

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
                }
                if (profile != null && !TextUtils.isEmpty(profile.getData().getRemember_token())) {
                    Logger.e("登录", response.toString());
                    //登录成功且有个人信息  设置profile
                    BaseApplication.setProfile(profile);
                    loginAccount();//登陆云信
                } else {
                    finish();
                }
            }

            @Override
            protected void onError(JSONObject response) {

                String result = "";
                try {
                    result = response.getJSONObject("error").getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Logger.e("注册失败--" + result);
                JSONObject error = null;
                try {
                    error = response.getJSONObject("error");
                    if (error.getString("msg").contains("Captcha confirmation")) {
                        Toast.makeText(WeChatBindActivity.this, getResourceString(R.string.code_error), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(WeChatBindActivity.this, getResourceString(R.string.phone_already_bind), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
            }
        });

        addToRequestQueue(request);
    }

    /**
     * 手机号已注册提示
     */
    private void dialogReTry() {
        alertDialog = new AlertDialog.Builder(WeChatBindActivity.this).create();
        View view = View.inflate(WeChatBindActivity.this, R.layout.dialog_cancel_or_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(R.string.wechat_bind_error_alerady_used);
        ((TextView) view.findViewById(R.id.cancel)).setText(R.string.new_phone_to_regist);
        ((TextView) view.findViewById(R.id.confirm)).setText(R.string.login);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                clearData();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
    }

    /**
     * 清除数据
     */
    private void clearData() {
        phone.setText("");
        code.setText("");
        password.setText("");
        grade.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_REGION_SELECT && resultCode == Constant.RESPONSE_REGION_SELECT) {
            city = (CityBean.Data) data.getSerializableExtra("region_city");
            province = (ProvincesBean.DataBean) data.getSerializableExtra("region_province");
            if (city != null && province != null) {
                region.setText(province.getName() + city.getName());
            }
        }
    }

    /**
     * 登陆云信
     */
    private void loginAccount() {
        progress = DialogUtils.startProgressDialog(progress, this, "登录中...");
        String account = BaseApplication.getAccount();
        String token = BaseApplication.getAccountToken();

        if (!StringUtils.isNullOrBlanK(account) && !StringUtils.isNullOrBlanK(token)) {
            NIMClient.getService(AuthService.class).login(new LoginInfo(account, token)).setCallback(new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo o) {
                    DialogUtils.dismissDialog(progress);
                    Logger.e("云信登录成功" + o.getAccount());
                    // 初始化消息提醒
                    NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

                    NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());
                    //缓存
                    UserInfoCache.getInstance().clear();
                    TeamDataCache.getInstance().clear();

                    UserInfoCache.getInstance().buildCache();
                    TeamDataCache.getInstance().buildCache();

                    UserInfoCache.getInstance().registerObservers(true);
                    TeamDataCache.getInstance().registerObservers(true);
//                  FriendDataCache.getInstance().registerObservers(true);
                }

                @Override
                public void onFailed(int code) {
                    DialogUtils.dismissDialog(progress);
                    profile.getData().setRemember_token("");
                    SPUtils.putObject(WeChatBindActivity.this, "profile", profile);
                    Logger.e(code + "code");
                }

                @Override
                public void onException(Throwable throwable) {
                    DialogUtils.dismissDialog(progress);
                    Logger.e(throwable.getMessage());
//                    BaseApplication.clearToken();
                    profile.getData().setRemember_token("");
                    SPUtils.putObject(WeChatBindActivity.this, "profile", profile);
                }
            });
        }
        if (getIntent().getIntExtra("register_action", Constant.REGIST_1) == Constant.REGIST_1) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        DialogUtils.dismissDialog(progress);
        setResult(Constant.RESPONSE);
        finish();

    }


    private class TimeCount extends CountDownTimer {
        public boolean ticking;

        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            ticking = false;
            getCode.setText(getResources().getString(R.string.get_verification_code));
            getCode.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            ticking = true;
            getCode.setEnabled(false);//防止重复点击
            getCode.setText(millisUntilFinished / 1000 + "s");
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
