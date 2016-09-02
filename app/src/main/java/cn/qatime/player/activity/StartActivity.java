package cn.qatime.player.activity;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
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
public class StartActivity extends Activity implements View.OnClickListener {
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
        map.put("title", "qatime");
        map.put("platform", "android");
        map.put("version", AppUtils.getVersionName(this));
        BaseApplication.getRequestQueue().add(new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlcheckUpdate, map), null, new VolleyListener(this) {
            @Override
            protected void onTokenOut() {

            }

            @Override
            protected void onSuccess(JSONObject response) {
                if (response.isNull("data")) {
                    startApp();
                } else {
                    //                              TODO 获取更新信信息
                    AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                    View view = View.inflate(StartActivity.this, R.layout.dialog_check_update, null);
                    Button down = (Button) view.findViewById(R.id.download);
                    down.setOnClickListener(StartActivity.this);
                    AlertDialog alertDialog = builder.show();
                    alertDialog.setContentView(view);
                    alertDialog.getWindow().setLayout(DensityUtils.dp2px(StartActivity.this, 300), DensityUtils.dp2px(StartActivity.this, 500));
                }

            }

            @Override
            protected void onError(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(StartActivity.this, "检查更新失败,请检查网络连接", Toast.LENGTH_SHORT).show();
            }
        }));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download:
                //TODO 更新版本
                Toast.makeText(StartActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                DownFileUtil downFileUtil = new DownFileUtil(this, "http://www.baidu.com", "111.html", "111", "111") {
                    @Override
                    public void downOK() {
                        DownFileUtil.insertAPK("", getApplicationContext());
                    }

                    @Override
                    public void downChange(long current, long max) {

                    }
                };
                downFileUtil.downFile();

                break;
            case R.id.text_x:
                startApp();
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
                        StartActivity.this.startActivity(new Intent(StartActivity.this, MainActivity.class));
                        StartActivity.this.finish();
                    } else {
                        StartActivity.this.startActivity(new Intent(StartActivity.this, LoginActivity.class));
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

                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        BaseApplication.getRequestQueue().add(request);
    }
}
