package cn.qatime.player.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.AppUtils;
import libraryextra.utils.DataCleanUtils;
import libraryextra.utils.DownFileUtil;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyListener;

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
    private android.app.AlertDialog alertDialog;
    private String apkUrl;
    private String downLoadLinks;
    private View updateView;

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
        setTitles(getResources().getString(R.string.system_setting));
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
                Map<String, String> map = new HashMap<>();
                map.put("category", "student_client");
                map.put("platform", "android");
                map.put("version", AppUtils.getVersionName(this));
//                map.put("version", "0.0.1");
                addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlcheckUpdate, map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        if (response.isNull("data")) {
                            Toast.makeText(SystemSettingActivity.this, getResourceString(R.string.is_newest_version), Toast.LENGTH_SHORT).show();
                        } else {
                            Logger.e(response.toString());
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SystemSettingActivity.this);
                            updateView = View.inflate(SystemSettingActivity.this, R.layout.dialog_check_update, null);
                            Button down = (Button) updateView.findViewById(R.id.download);
                            View x = updateView.findViewById(R.id.image_x);
                            TextView newVersion = (TextView) updateView.findViewById(R.id.new_version);
                            TextView desc = (TextView) updateView.findViewById(R.id.desc);
                            desc.setMovementMethod(ScrollingMovementMethod.getInstance());
                            try {
                                x.setOnClickListener(SystemSettingActivity.this);
                               //boolean updateEnforce = response.getJSONObject("data").getBoolean("enforce");
//                                if (!updateEnforce) {
//                                    TextView pleaseUpdate = (TextView) updateView.findViewById(R.id.please_update);
//                                    pleaseUpdate.setVisibility(View.VISIBLE);
//                                    x.setVisibility(View.GONE);
//                                }
                                String descStr = response.getJSONObject("data").getString("description");
                                desc.setText(StringUtils.isNullOrBlanK(descStr) ? "\n" : descStr);
                                downLoadLinks = response.getJSONObject("data").getString("download_links");
                                newVersion.setText("(V" + response.getJSONObject("data").getString("version")+")");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            down.setOnClickListener(SystemSettingActivity.this);
                            alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.setContentView(updateView);
                            alertDialog.setCancelable(false);
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(SystemSettingActivity.this, getResourceString(R.string.check_for_update_failed), Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SystemSettingActivity.this, getResourceString(R.string.check_for_update_failed_check_net), Toast.LENGTH_SHORT).show();
                    }
                }));
                break;
            case R.id.clean_cache:
                //TODO 弹出对话框提示
                Toast.makeText(SystemSettingActivity.this, getString(R.string.clean_cache_success) + cacheSize.getText().toString(), Toast.LENGTH_SHORT).show();
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
                alertDialog.dismiss();
                //TODO 更新版本
                Toast.makeText(SystemSettingActivity.this, getResourceString(R.string.start_download), Toast.LENGTH_SHORT).show();
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
                break;
            case R.id.image_x:
                alertDialog.dismiss();
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
