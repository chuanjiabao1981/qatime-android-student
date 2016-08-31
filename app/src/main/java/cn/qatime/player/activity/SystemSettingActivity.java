package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.AppUtils;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DataCleanUtils;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.SPUtils;

/**
 * @author luntify
 * @date 2016/8/10 10:34
 * @Description
 */
public class SystemSettingActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout learningProcess;
    private LinearLayout notifySetting;
    private LinearLayout checkUpdate;
    private TextView version;
    private LinearLayout cleanCache;
    private TextView cacheSize;
    private LinearLayout feedback;
    private LinearLayout about;
    private Button exit;
    private String totalCacheSize;
    private AlertDialog alertDialog;
    private String apkUrl;

    private void assignViews() {
        learningProcess = (LinearLayout) findViewById(R.id.learning_process);
        notifySetting = (LinearLayout) findViewById(R.id.notify_setting);
        checkUpdate = (LinearLayout) findViewById(R.id.check_update);
        version = (TextView) findViewById(R.id.version);
        cleanCache = (LinearLayout) findViewById(R.id.clean_cache);
        cacheSize = (TextView) findViewById(R.id.cache_size);
        feedback = (LinearLayout) findViewById(R.id.feedback);
        about = (LinearLayout) findViewById(R.id.about);
        exit = (Button) findViewById(R.id.exit);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        initView();
        initData();
    }

    private void initView() {
        setTitle(getResources().getString(R.string.system_setting));
        assignViews();

        version = (TextView) findViewById(R.id.version);
        cacheSize = (TextView) findViewById(R.id.cache_size);


        exit.setOnClickListener(this);
        notifySetting.setOnClickListener(this);
        checkUpdate.setOnClickListener(this);
        cleanCache.setOnClickListener(this);
        learningProcess.setOnClickListener(this);
        feedback.setOnClickListener(this);
        about.setOnClickListener(this);


    }

    private void initData() {
        version.setText(getResources().getString(R.string.current_version) + AppUtils.getVersionName(this));
        setCache();
    }

    private void setCache() {
        try {
            totalCacheSize = DataCleanUtils.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cacheSize.setText(totalCacheSize);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:// 退出登录
                BaseApplication.clearToken();
                Intent intent = new Intent(SystemSettingActivity.this, MainActivity.class);
                intent.putExtra("sign", "exit_login");
                startActivity(intent);
//                finish();
                break;
            case R.id.notify_setting:

                intent = new Intent(SystemSettingActivity.this, NotifySettingActivity.class);
                startActivity(intent);
                break;
            case R.id.check_update:
                //TODO 检查版本，进行更新
//                addToRequestQueue(new DaYiJsonObjectRequest(0, "", null, new VolleyListener(this) {
//                    @Override
//                    protected void onTokenOut() {
//
//                    }
//
//                    @Override
//                    protected void onSuccess(JSONObject response) {
//
//                        //TODO 获取更新信信息
//                        AlertDialog.Builder builder = new AlertDialog.Builder(SystemSettingActivity.this);
//                        View view = View.inflate(SystemSettingActivity.this, R.layout.dialog_check_update, null);
//                        Button down = (Button) view.findViewById(R.id.download);
//                        down.setOnClickListener(SystemSettingActivity.this);
//                        alertDialog = builder.create();
//
//                        alertDialog.show();
//                        alertDialog.setContentView(view);
//                        alertDialog.getWindow().setLayout(DensityUtils.dp2px(SystemSettingActivity.this, 300), DensityUtils.dp2px(SystemSettingActivity.this, 500));
//                    }
//
//                    @Override
//                    protected void onError(JSONObject response) {
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//
//                    }
//                }));

                //TODO 获取更新信信息
                AlertDialog.Builder builder = new AlertDialog.Builder(SystemSettingActivity.this);
                View view = View.inflate(SystemSettingActivity.this, R.layout.dialog_check_update, null);
                Button down = (Button) view.findViewById(R.id.download);
                View x = view.findViewById(R.id.text_x);
                x.setOnClickListener(SystemSettingActivity.this);
                down.setOnClickListener(SystemSettingActivity.this);
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setContentView(view);
                alertDialog.getWindow().setLayout(DensityUtils.dp2px(SystemSettingActivity.this, 300),DensityUtils.dp2px(SystemSettingActivity.this, 500));
                break;
            case R.id.clean_cache:
                //TODO 弹出对话框提示

                DataCleanUtils.clearAllCache(this);
                setCache();
                break;
            case R.id.learning_process:
                intent = new Intent(SystemSettingActivity.this, LearningProcessActivity.class);
                startActivity(intent);
                break;
            case R.id.feedback:
                intent = new Intent(SystemSettingActivity.this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                Logger.e("about click");
                intent = new Intent(SystemSettingActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.download:
                //TODO 更新版本
                Toast.makeText(SystemSettingActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                new Thread() {
                    @Override
                    public void run() {
//                FileOutputStream fos = new FileOutputStream(ApkFile);
//
//                try {
//                    URL url = new URL(apkUrl);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.connect();
//                    apkLength = conn.getContentLength();
//                    System.out.println();
//                    InputStream is = conn.getInputStream();
//                    apkCurrentDownload = 0;
//                    byte buf[] = new byte[1024];
//                    int length = -1;
//                    while ((length = is.read(buf)) != -1) {
//                        apkCurrentDownload += length;
//                        progress = (int) (((float) apkCurrentDownload / apkLength) * 100);
//                        //更新进度
//                        mHandler.sendEmptyMessage(DOWN_UPDATE);
//                        fos.write(buf, 0, length);
//                        if (apkCurrentDownload == apkLength) {
//                            //下载完成通知安装
//                            mHandler.sendEmptyMessage(DOWN_OVER);
//                            break;
//                        }
//                        if (interceptFlag) {
//                            ApkFile.delete();
//                            break;
//                        }
//                    }
//                    fos.close();
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                    }
                }.start();

                alertDialog.dismiss();
                break;
            case R.id.text_x:
                alertDialog.dismiss();
                break;
        }

    }
}
