package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.NotifyStatusBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.WheelView;

/**
 * Created by lenovo on 2016/8/22.
 */
public class NotifyCourseActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox sms;
    private CheckBox sys;
    private TextView textHours;
    private TextView textMinute;
    private List<String> al_hours;
    private List<String> al_minute;
    private String hour;
    private String minute;
    private View time;
    private AlertDialog alertDialog;
    private WheelView wheelHours;
    private WheelView wheelMinutes;
    private String emailStatus;
    /**
     * 状态是否初始化完成
     */
    private boolean initOver=false;
    /**
     * 状态是否改变
     */
    private boolean hasChanged = false;


    private void assignViews() {
        sms = (CheckBox) findViewById(R.id.sms);
        sys = (CheckBox) findViewById(R.id.sys);
        textHours = (TextView) findViewById(R.id.hours);
        textMinute = (TextView) findViewById(R.id.minute);
        time = findViewById(R.id.time);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        al_hours = new ArrayList<>();
        al_minute = new ArrayList<>();
        String str;
        for (int i = 0; i <= 24; i++) {
            str = String.valueOf(i);
            if (i <= 9) {
                str = "0" + str;
            }
            str += getResourceString(R.string.hour);
            al_hours.add(str);
        }
        al_minute.add("00" + getResourceString(R.string.minute));
        al_minute.add("01" + getResourceString(R.string.minute));
        al_minute.add("02" + getResourceString(R.string.minute));
        al_minute.add("03" + getResourceString(R.string.minute));
        al_minute.add("04" + getResourceString(R.string.minute));
        al_minute.add("05" + getResourceString(R.string.minute));
        for (int i = 10; i <= 50; i += 5) {
            str = String.valueOf(i);
            str += getResourceString(R.string.minute);
            al_minute.add(str);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_notify_course);
        setTitles(getResourceString(R.string.notify_classes));
        assignViews();
        initStatus();

        sys.setOnCheckedChangeListener(this);
        sms.setOnCheckedChangeListener(this);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
    }

    private void initStatus() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlUser + BaseApplication.getUserId() + "/notifications/settings", null,
                new VolleyListener(NotifyCourseActivity.this) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        NotifyStatusBean notifyStatusBean = JsonUtils.objectFromJson(response.toString(), NotifyStatusBean.class);
                        if (notifyStatusBean.getData() != null) {
                            sms.setChecked(notifyStatusBean.getData().isMessage());
                            sys.setChecked(notifyStatusBean.getData().isNotice());
                            int before_hours = notifyStatusBean.getData().getBefore_hours();
                            int before_minutes = notifyStatusBean.getData().getBefore_minutes();
                            emailStatus = notifyStatusBean.getData().isMessage() ? "1" : "0";
                            hour = (before_hours < 10 ? "0" : "") + before_hours + getResourceString(R.string.hour);
                            minute = (before_minutes < 10 ? "0" : "") + before_minutes +  getResourceString(R.string.minute);
                            textHours.setText(hour);
                            textMinute.setText(minute);
                            initOver = true;
                        }
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

    private void showTimePickerDialog() {
        if (alertDialog == null) {
            View view = View.inflate(NotifyCourseActivity.this, R.layout.dialog_time_picker, null);
            wheelHours = (WheelView) view.findViewById(R.id.dialog_hours);
            wheelHours.setOffset(1);
            wheelHours.setItems(al_hours);
            wheelHours.setSeletion(al_hours.indexOf(hour));
            wheelMinutes = (WheelView) view.findViewById(R.id.dialog_minute);
            wheelMinutes.setOffset(1);
            wheelMinutes.setItems(al_minute);
            wheelMinutes.setSeletion(al_minute.indexOf(minute));
            AlertDialog.Builder builder = new AlertDialog.Builder(NotifyCourseActivity.this);
            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setContentView(view);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if(initOver){
                        hasChanged = true;
                    }
                    textHours.setText(wheelHours.getSeletedItem());
                    textMinute.setText(wheelMinutes.getSeletedItem());
                }
            });
            wheelHours.setonItemClickListener(new WheelView.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    alertDialog.dismiss();
                }
            });
            wheelMinutes.setonItemClickListener(new WheelView.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    alertDialog.dismiss();
                }
            });
        } else {
            alertDialog.show();
        }
    }

    @Override
    public void finish() {
        if (hasChanged) {
            Map<String, String> map = new HashMap<>();
            map.put("notice", sys.isChecked() ? "1" : "0");
            map.put("message", sms.isChecked() ? "1" : "0");
            map.put("email", emailStatus);
            map.put("before_hours", textHours.getText().toString().replace(getResourceString(R.string.hour), ""));
            map.put("before_minutes", textMinute.getText().toString().replace(getResourceString(R.string.minute), ""));
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.getUrl(UrlUtils.urlUser + BaseApplication.getUserId() + "/notifications/settings", map), null,
                    new VolleyListener(NotifyCourseActivity.this) {

                        @Override
                        protected void onSuccess(JSONObject response) {
                            Logger.e("保存成功");
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
        super.finish();
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(initOver){
            hasChanged = true;
        }
    }
}
