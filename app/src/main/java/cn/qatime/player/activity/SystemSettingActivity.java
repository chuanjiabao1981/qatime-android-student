package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.AppUtils;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.DataCleanUtils;
import cn.qatime.player.utils.DownFileUtil;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.DensityUtils;
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
                Map<String, String> map = new HashMap<>();
                map.put("category", "student_client");
                map.put("platform", "android");
                map.put("version", AppUtils.getVersionName(this));
//                map.put("version", "0.0.1");
                BaseApplication.getRequestQueue().add(new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlcheckUpdate, map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {

                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        if (response.isNull("data")) {
                            Toast.makeText(SystemSettingActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
                        } else {
                            //TODO 获取更新信信息0
                            Logger.e(response.toString());
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SystemSettingActivity.this);
                            final View view = View.inflate(SystemSettingActivity.this, R.layout.dialog_check_update, null);
                            Button down = (Button) view.findViewById(R.id.download);
                            View x = view.findViewById(R.id.image_x);
                            TextView newVersion = (TextView) view.findViewById(R.id.new_version);
                            TextView desc = (TextView) view.findViewById(R.id.desc);
                            desc.setMaxHeight(DensityUtils.dp2px(SystemSettingActivity.this, 300));
                            try {
                                if (!response.getJSONObject("data").getBoolean("enforce")) {
                                    x.setOnClickListener(SystemSettingActivity.this);
                                }
                                String descStr = response.getJSONObject("data").getString("description");
                                desc.setText(StringUtils.isNullOrBlanK(descStr) ? "性能优化" : descStr);
                                downLoadLinks = response.getJSONObject("data").getString("download_links");
                                newVersion.setText("V" + response.getJSONObject("data").getString("version"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            down.setOnClickListener(SystemSettingActivity.this);
                            alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.setContentView(view);
                            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    if (DensityUtils.px2dp(SystemSettingActivity.this, alertDialog.getWindow().getAttributes().height) > 500) {
                                        alertDialog.getWindow().setLayout(DensityUtils.dp2px(SystemSettingActivity.this, 300), DensityUtils.dp2px(SystemSettingActivity.this, 500));
                                    } else {
                                        alertDialog.getWindow().setLayout(DensityUtils.dp2px(SystemSettingActivity.this, 300), alertDialog.getWindow().getAttributes().height);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(SystemSettingActivity.this, "检查更新失败", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SystemSettingActivity.this, "检查更新失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                }));
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
                DownFileUtil downFileUtil = new DownFileUtil(this, downLoadLinks, "qatime.apk", "", "答疑时间.apk") {
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
                break;
            case R.id.image_x:
                alertDialog.dismiss();
                break;
        }

    }
}
