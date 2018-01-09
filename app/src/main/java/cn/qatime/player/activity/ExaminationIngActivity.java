package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.apache.http.client.methods.HttpPut;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.flow.BasicDispatcher;
import cn.qatime.player.flow.BasicKeyParceler;
import cn.qatime.player.flow.screen.FlowExplainScreen;
import cn.qatime.player.utils.ACache;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import flow.Flow;
import libraryextra.rx.HttpManager;
import libraryextra.rx.callback.SimpleCallBack;
import libraryextra.rx.model.HttpParams;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * @author luntify
 * @date 2017/12/1 10:43
 * @Description 考试考题页面
 */

public class ExaminationIngActivity extends BaseFragmentActivity {

    private BasicDispatcher dispatcher;
    private boolean firstLoad = true;
    private int id;

    @Override
    protected void attachBaseContext(Context newBase) {
        dispatcher = new BasicDispatcher(this);
        newBase = Flow.configure(newBase, this)
                .dispatcher(dispatcher)
                .defaultKey(new FlowExplainScreen(0, 0))
                .keyParceler(new BasicKeyParceler())
                .install();
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_ing);
        EventBus.getDefault().register(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //清空之前保存的答案
        ACache.get(ExaminationIngActivity.this).put("answer", new HashMap<Integer, String>());
        id = getIntent().getIntExtra("id", 0);
        if (id == 0) {
            Toast.makeText(this, "试卷id为空", Toast.LENGTH_SHORT).show();
            return;
        }
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!firstLoad) {
            if (dispatcher != null) {
                dispatcher.reStart();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dispatcher != null) {
            dispatcher.pause();
        }
        firstLoad = false;
    }

    private void initView() {
        setRightText("交卷", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dispatcher != null) {
                    dispatcher.pause();
                }
                showDialog();
            }
        });
    }

    private void showDialog() {

        View view = View.inflate(ExaminationIngActivity.this, R.layout.dialog_exam_cancel_or_confirm, null);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        TextView status = (TextView) view.findViewById(R.id.status);
        TextView text = (TextView) view.findViewById(R.id.text);
        if (dispatcher.isComplete()) {
            status.setText("恭喜，已成功完成考试！");
            status.setTextColor(0xff00b200);
        } else {
            status.setText("考试尚未完成！");
            status.setTextColor(0xffe50000);
        }

        text.setText("你可以选择提交试卷等待判卷 或 放弃判卷直接关闭考试");
        cancel.setText("放弃");
        confirm.setText("交卷");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatcher.reStart();
                alertDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submitAnswer();
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.e("error" + e.getMessage());
                }
                alertDialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(ExaminationIngActivity.this);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        alertDialog.setContentView(view);
    }

    private void submitAnswer() throws JSONException, IOException {
        HashMap<Integer, String> map = (HashMap<Integer, String>) ACache.get(ExaminationIngActivity.this).getAsObject("answer");
        HttpParams params = new HttpParams();
        int i = 0;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            params.put("answers_attributes[" + i + "][topic_id]", String.valueOf(entry.getKey()));
            if (entry.getValue().endsWith(".mp3")) {
                params.put("answers_attributes[" + i + "][content]", ReaderJson(entry.getValue()));
            } else {
                params.put("answers_attributes[" + i + "][content]", entry.getValue());
            }
            i += 1;
        }
        Logger.e("value" + params.toString());
        HttpManager.put(UrlUtils.getBaseUrl() + "api/v1/exam/results/" + id)
                .params(params)
                .execute(new SimpleCallBack<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        Toast.makeText(ExaminationIngActivity.this, "交卷成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onTokenOut() {
                        tokenOut();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(String status) {
        if (status.equals("exam_complete")) {
            showDialog();
        }
    }

    private static String ReaderJson(String filePath) throws IOException {
        byte[] data = null;
        // 读取音频字节数组
        try {
            InputStream in = new FileInputStream(filePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码
        return Base64.encodeToString(data, Base64.NO_WRAP);

    }
}
