package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.SPUtils;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.AppUtils;
import libraryextra.utils.DownFileUtil;
import libraryextra.utils.FileUtil;
import libraryextra.utils.NetUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * 起始页
 */
public class StartActivity extends BaseActivity implements View.OnClickListener {
    private AlertDialog alertDialog;
    private String downLoadLinks;
    private boolean updateEnforce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ((TextView) findViewById(R.id.version)).setText("V " + AppUtils.getVersionName(this));
        if (!NetUtils.isConnected(StartActivity.this)) {
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (NetUtils.checkExternalStoragePermission(this)) {
                GetGradeslist();//加载年纪列表
                GetSchoolslist();
                getProviceslist();
                getCitylist();

                checkUpdate();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);

            }
        } else {
            GetGradeslist();//加载年纪列表
            GetSchoolslist();
            getProviceslist();
            getCitylist();

            checkUpdate();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (NetUtils.checkExternalStoragePermission(this)) {
                    GetGradeslist();//加载年纪列表
                    GetSchoolslist();
                    getProviceslist();
                    getCitylist();

                    checkUpdate();
                } else {
                    Toast.makeText(this, "未取得权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    /**
     * 删除上次更新存储在本地的apk
     */
    private void removeOldApk() {
        File fileName = new File(Environment.getExternalStorageDirectory() + "/qatime.apk");
        if (fileName != null && fileName.exists() && fileName.isFile()) {
            fileName.delete();
        }
    }

    private void checkUpdate() {
        //TODO 检查版本，进行更新
        removeOldApk();

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
                    BaseApplication.newVersion = false;
                } else {
                    BaseApplication.newVersion = true;
                    Logger.e(response.toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                    final View view = View.inflate(StartActivity.this, R.layout.dialog_check_update, null);
                    Button down = (Button) view.findViewById(R.id.download);
                    View x = view.findViewById(R.id.image_x);
                    TextView newVersion = (TextView) view.findViewById(R.id.new_version);
                    TextView desc = (TextView) view.findViewById(R.id.desc);
                    desc.setMovementMethod(ScrollingMovementMethod.getInstance());
                    alertDialog = builder.create();
                    try {
                        x.setOnClickListener(StartActivity.this);
                        updateEnforce = response.getJSONObject("data").getBoolean("enforce");
                        if (updateEnforce) {
                            TextView pleaseUpdate = (TextView) view.findViewById(R.id.please_update);
                            pleaseUpdate.setVisibility(View.VISIBLE);
                            x.setVisibility(View.GONE);
//                            Toast.makeText(StartActivity.this, "重大更新，请先进行升级", Toast.LENGTH_SHORT).show();
//                            alertDialog.setCancelable(false);
                        }
                        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (updateEnforce) {
                                    finish();
                                } else {
                                    startApp();
                                }
                            }
                        });
                        String descStr = response.getJSONObject("data").getString("description");
                        desc.setText(StringUtils.isNullOrBlanK(descStr) ? "\n" : descStr);
                        downLoadLinks = response.getJSONObject("data").getString("download_links");
                        newVersion.setText("(V" + response.getJSONObject("data").getString("version") + ")");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    down.setOnClickListener(StartActivity.this);
                    alertDialog.show();
                    alertDialog.setContentView(view);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                }
            }

            @Override
            protected void onError(JSONObject response) {
                Toast.makeText(StartActivity.this, getResourceString(R.string.check_for_update_failed), Toast.LENGTH_SHORT).show();
                startApp();
            }
        }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                Toast.makeText(StartActivity.this, getResourceString(R.string.check_for_update_failed_check_net), Toast.LENGTH_SHORT).show();
                startApp();
            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download:
                alertDialog.dismiss();
                Toast.makeText(StartActivity.this, getResourceString(R.string.start_download), Toast.LENGTH_SHORT).show();
                DownFileUtil downFileUtil = new DownFileUtil(this, downLoadLinks, "qatime.apk", "", "qatime.apk") {
                    @Override
                    public void downOK() {
                        DownFileUtil.insertAPK(Environment.getExternalStorageDirectory() + "/qatime.apk", getApplicationContext());
                    }

                    @Override
                    public void downChange(long current, long max) {

                    }
                };
                downFileUtil.downFile();
//                startApp();
                break;
            case R.id.image_x:
                alertDialog.dismiss();
//                startApp();
                break;
        }
    }

    private void startApp() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getSharedPreferences("first", MODE_PRIVATE).getBoolean("firstlogin", true)) {
                    Logger.e("第一次登陆");
                    StartActivity.this.startActivity(new Intent(StartActivity.this, GuideActivity.class));
                    StartActivity.this.finish();
                } else {
                    Logger.e("no第一次登陆");
                    if (!StringUtils.isNullOrBlanK(BaseApplication.getProfile().getToken())) {//token不空  直接自动登录到mianactivity
                        Logger.e("token----" + BaseApplication.getProfile().getToken());
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                        StartActivity.this.startActivity(intent);
                        StartActivity.this.finish();
                    }
                }
            }
        }, 2000);
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

    //省份列表
    public void getProviceslist() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlAppconstantInformation + "/provinces", null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        boolean value = FileUtil.writeFile(new ByteArrayInputStream(response.toString().getBytes()), getFilesDir().getAbsolutePath() + "/provinces.txt", true);
                        SPUtils.put(StartActivity.this, "provinces", value);
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

    //城市列表
    public void getCitylist() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlAppconstantInformation + "/cities", null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        boolean value = FileUtil.writeFile(new ByteArrayInputStream(response.toString().getBytes()), getFilesDir().getAbsolutePath() + "/cities.txt", true);
                        SPUtils.put(StartActivity.this, "cities", value);
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

    //学校列表
    public void GetSchoolslist() {

        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.urlAppconstantInformation + "/schools", null,
                new VolleyListener(StartActivity.this) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        boolean value = FileUtil.writeFile(new ByteArrayInputStream(response.toString().getBytes()), getFilesDir().getAbsolutePath() + "/school.txt", true);
                        SPUtils.put(StartActivity.this, "school", value);
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
