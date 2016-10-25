package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.view.WheelView;
import libraryextra.utils.SPUtils;

/**
 * Created by lenovo on 2016/8/22.
 */
public class NotifyCourseActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox cb1;
    private CheckBox cb2;
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


    private void assignViews() {
        cb1 = (CheckBox) findViewById(R.id.cb_1);
        cb2 = (CheckBox) findViewById(R.id.cb_2);
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
        int j = 0;
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
        hour = (String) SPUtils.get(this, "notify_hour", "00小时");
        minute = (String) SPUtils.get(this, "notify_minute", "00分钟");
        textHours.setText(hour);
        textMinute.setText(minute);
    }

    private void initView() {
        setContentView(R.layout.activity_notify_course);
        setTitle(getResourceString(R.string.notify_classes));
        assignViews();
        cb1.setChecked((Boolean) SPUtils.get(this, "notify_course", true));
        cb2.setChecked((Boolean) SPUtils.get(this, "notify_public", true));
        sms.setChecked((Boolean) SPUtils.get(this, "notify_sms", true));
        sys.setChecked((Boolean) SPUtils.get(this, "notify_sys", true));


        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        sms.setOnCheckedChangeListener(this);
        sys.setOnCheckedChangeListener(this);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
    }

    private void showTimePickerDialog() {
        if (alertDialog == null) {
            final View view = View.inflate(NotifyCourseActivity.this, R.layout.dialog_time_picker, null);
            final WheelView hours = (WheelView) view.findViewById(R.id.hours);
            hours.setOffset(1);
            hours.setItems(al_hours);
            hours.setSeletion(al_hours.indexOf(hour));
            final WheelView minutes = (WheelView) view.findViewById(R.id.minute);
            minutes.setOffset(1);
            minutes.setItems(al_minute);
            minutes.setSeletion(al_minute.indexOf(minute));
            AlertDialog.Builder builder = new AlertDialog.Builder(NotifyCourseActivity.this);
            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setContentView(view);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    textHours.setText(hours.getSeletedItem());
                    textMinute.setText(minutes.getSeletedItem());
                }
            });
//            WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
//            attributes.width= ScreenUtils.getScreenWidth(getApplicationContext())- DensityUtils.dp2px(getApplicationContext(),20)*2;
//            alertDialog.getWindow().setAttributes(attributes);
        } else {
            alertDialog.show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_1:
                Logger.e("cb_1 click");

                SPUtils.put(this, "notify_course", isChecked);
                break;

            case R.id.sms:

                Logger.e("sms click");

                SPUtils.put(this, "notify_sms", isChecked);
                break;
            case R.id.sys:

                Logger.e("sys click");

                SPUtils.put(this, "notify_sys", isChecked);
                break;
            case R.id.cb_2:
                Logger.e("cb_2 click");
                SPUtils.put(this, "notify_public", isChecked);
                break;
        }
    }

    @Override
    protected void onStop() {
        hour = textHours.getText().toString();
        minute = textMinute.getText().toString();
        SPUtils.put(this, "notify_hour", hour);
        SPUtils.put(this, "notify_minute", minute);
        super.onStop();
    }
}
