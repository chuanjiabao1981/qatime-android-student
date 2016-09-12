package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.utils.SPUtils;

/**
 * Created by lenovo on 2016/8/22.
 */
public class NotifyCourseActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox sms;
    private CheckBox sys;
    private Spinner spinnerHours;
    private Spinner spinnerMinute;
    private List<String> al_hours;
    private List<String> al_minute;
    private String hour;
    private String minute;


    private void assignViews() {
        cb1 = (CheckBox) findViewById(R.id.cb_1);
        cb2 = (CheckBox) findViewById(R.id.cb_2);
        sms = (CheckBox) findViewById(R.id.sms);
        sys = (CheckBox) findViewById(R.id.sys);
        spinnerHours = (Spinner) findViewById(R.id.spinner_hours);
        spinnerMinute = (Spinner) findViewById(R.id.spinner_minute);
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
        spinnerHours.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_time, al_hours));
        spinnerMinute.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_time, al_minute));
        hour = al_hours.get((int) spinnerHours.getSelectedItemId()).replace(getResourceString(R.string.hour), "");
        minute = al_minute.get((int) spinnerHours.getSelectedItemId()).replace(getResourceString(R.string.minute), "");
        spinnerHours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hour = al_hours.get((int) spinnerHours.getSelectedItemId()).replace(getResourceString(R.string.hour), "");
                SPUtils.put(NotifyCourseActivity.this, "notify_hour", hour);
                Logger.e("hour=" + hour);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerMinute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                minute = al_minute.get((int) spinnerMinute.getSelectedItemId()).replace(getResourceString(R.string.minute), "");
                Logger.e("minute=" + minute);
                SPUtils.put(NotifyCourseActivity.this, "notify_minute", minute);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String notify_hour = (String) SPUtils.get(this, "notify_hour", "00");
        String notify_minute = (String) SPUtils.get(this, "notify_minute", "00");

        spinnerHours.setSelection(al_hours.indexOf(notify_hour + getResourceString(R.string.hour)));
        spinnerMinute.setSelection(al_minute.indexOf(notify_minute + getResourceString(R.string.minute)));
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
    public void backClick(View v) {
        SPUtils.put(this, "alarm", hour + ":" + minute);
        super.backClick(v);
    }
}
