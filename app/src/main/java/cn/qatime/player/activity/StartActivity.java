package cn.qatime.player.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.config.UserPreferences;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.cache.UserInfoCache;
import cn.qatime.player.utils.AppUtils;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.DownFileUtil;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.FileUtil;
import libraryextra.utils.SPUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * 起始页
 */
public class StartActivity extends BaseActivity {
    private AlertDialog alertDialog;
    private String downLoadLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        GetGradeslist();//加载年纪列表
        checkUpdate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private void checkUpdate() {
        //TODO 检查版本，进行更新
        Map<String, String> map = new HashMap<>();
        map.put("category", "student_client");
        map.put("platform", "android");
        map.put("version", AppUtils.getVersionName(this));
//        map.put("version", "0.0.1");
        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlcheckUpdate, map), null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {

            }

            @Override
            protected void onSuccess(JSONObject response) {
                if (response.isNull("data")) {
                    startApp();
                    BaseApplication.newVersion=false;
                } else {
                    BaseApplication.newVersion=true;
                    Logger.e(response.toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                    final View view = View.inflate(StartActivity.this, R.layout.dialog_check_update, null);
                    Button down = (Button) view.findViewById(R.id.download);
                    View x = view.findViewById(R.id.image_x);
                    TextView newVersion = (TextView) view.findViewById(R.id.new_version);

                    TextView desc = (TextView) view.findViewById(R.id.desc);
                    alertDialog = builder.create();
                    try {
                        x.setOnClickListener(StartActivity.this::onClick);
                        final boolean enforce = response.getJSONObject("data").getBoolean("enforce");
                        if (enforce) {
                            TextView pleaseUpdate = (TextView) view.findViewById(R.id.please_update);
                            pleaseUpdate.setVisibility(View.VISIBLE);
//                            Toast.makeText(StartActivity.this, "重大更新，请先进行升级", Toast.LENGTH_SHORT).show();
//                            alertDialog.setCancelable(false);
                        }
                        alertDialog.setOnDismissListener(dialog -> {
                            if (enforce) {
                                finish();
                            } else {
                                startApp();
                            }
                        });
                        String descStr = response.getJSONObject("data").getString("description");
                        desc.setText(StringUtils.isNullOrBlanK(descStr) ? getResourceString(R.string.performance_optimization) : descStr);
                        downLoadLinks = response.getJSONObject("data").getString("download_links");
                        newVersion.setText("V" + response.getJSONObject("data").getString("version"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    down.setOnClickListener(StartActivity.this::onClick);
                    alertDialog.show();
                    view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                        if (DensityUtils.px2dp(StartActivity.this, alertDialog.getWindow().getAttributes().height) > 500) {
                            alertDialog.getWindow().setLayout(DensityUtils.dp2px(StartActivity.this, 300), DensityUtils.dp2px(StartActivity.this, 500));
                        } else {
                            alertDialog.getWindow().setLayout(DensityUtils.dp2px(StartActivity.this, 300), alertDialog.getWindow().getAttributes().height);
                        }
                    });
                    alertDialog.setContentView(view);
                }
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(StartActivity.this, getResourceString(R.string.check_for_update_failed), Toast.LENGTH_SHORT).show();
                startApp();
            }
        }, volleyError -> {
            Toast.makeText(StartActivity.this, getResourceString(R.string.check_for_update_failed_check_net), Toast.LENGTH_SHORT).show();
            startApp();
        }));

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download:
                //TODO 更新版本
                Toast.makeText(StartActivity.this, getResourceString(R.string.start_download), Toast.LENGTH_SHORT).show();
                DownFileUtil downFileUtil = new DownFileUtil(this, downLoadLinks, "qatime.apk", "", "qatime.apk") {
                    @Override
                    public void downOK() {
                        DownFileUtil.insertAPK("", getApplicationContext());
                    }

                    @Override
                    public void downChange(long current, long max) {

                    }
                };
                downFileUtil.downFile();
                alertDialog.dismiss();
//                startApp();
                break;
            case R.id.image_x:
                alertDialog.dismiss();
//                startApp();
                break;
        }
    }

    private void startApp() {
        new Handler().postDelayed(() -> {
            if (getSharedPreferences("first", MODE_PRIVATE).getBoolean("firstlogin", true)) {
                Logger.e("第一次登陆");
                StartActivity.this.startActivity(new Intent(StartActivity.this, GuideActivity.class));
                StartActivity.this.finish();
            } else {
                Logger.e("no第一次登陆");
                if (!StringUtils.isNullOrBlanK(BaseApplication.getProfile().getToken())) {//token不空  直接自动登录到mianactivity
                    Logger.e("token----" + BaseApplication.getProfile().getToken());
                    loginAccount();
                } else {
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    StartActivity.this.startActivity(intent);
                    StartActivity.this.finish();
                }
            }
        }, 2000);
    }
    private void loginAccount() {
        String account = BaseApplication.getAccount();
        String token = BaseApplication.getAccountToken();

        if (!StringUtils.isNullOrBlanK(account) && !StringUtils.isNullOrBlanK(token)) {
            NIMClient.getService(AuthService.class).login(new LoginInfo(account, token)).setCallback(new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo o) {
                    Logger.e("云信登录成功" + o.getAccount());
                    // 初始化消息提醒
                    NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

                    NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());
                    //缓存
                    UserInfoCache.getInstance().clear();
                    TeamDataCache.getInstance().clear();
                    //                FriendDataCache.getInstance().clear();

                    UserInfoCache.getInstance().buildCache();
                    TeamDataCache.getInstance().buildCache();
                    //好友维护,目前不需要
                    //                FriendDataCache.getInstance().buildCache();

                    UserInfoCache.getInstance().registerObservers(true);
                    TeamDataCache.getInstance().registerObservers(true);
//                                                FriendDataCache.getInstance().registerObservers(true);

                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                @Override
                public void onFailed(int code) {
                    BaseApplication.clearToken();
                    Logger.e(code + "code");
                    if (code == 302 || code == 404) {
                        Toast.makeText(StartActivity.this, R.string.account_or_password_error, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StartActivity.this, getResourceString(R.string.login_failed) + code, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onException(Throwable throwable) {
                    Logger.e(throwable.getMessage());
                    BaseApplication.clearToken();
                }
            });
        } else {//没有云信账号,直接登录
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //年级列表
    public void GetGradeslist() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlAppconstantInformation + "/grades", null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        boolean value = FileUtil.writeFile(new ByteArrayInputStream(response.toString().getBytes()), getFilesDir().getAbsolutePath() + "/grade.txt", true);
                        SPUtils.put(StartActivity.this, "grade", value);
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
