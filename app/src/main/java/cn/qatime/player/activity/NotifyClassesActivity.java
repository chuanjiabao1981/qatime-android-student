package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.utils.LogUtils;

/**
 * Created by lenovo on 2016/8/22.
 */
public class NotifyClassesActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private CheckBox cb_1;
    private CheckBox cb_2;
    private CheckBox sms;
    private CheckBox sys;
    private Spinner hours;
    private Spinner minute;
    private List<String> al_hours;
    private List<String> al_minute;

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
        for (int i = 0; i < 24; i++) {
            str = String.valueOf(i);
            if (i <= 9) {
                str = "0" + str;
            }
            str += "小时";
            al_hours.add(str);
        }
        al_minute.add("00分钟");
        al_minute.add("02分钟");
        al_minute.add("03分钟");
        al_minute.add("04分钟");
        al_minute.add("05分钟");
        for (int i = 10; i <= 50; i += 5) {
            str = String.valueOf(i);
            str += "分钟";
            al_minute.add(str);
        }
        hours.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_time, al_hours));
        minute.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_time, al_minute));

        //TODO 初始化并记录时间

    }

    private void initView() {
        setContentView(R.layout.activity_notify_classes);
        setTitle("课程提醒");
        cb_1 = (CheckBox) findViewById(R.id.cb_1);
        cb_2 = (CheckBox) findViewById(R.id.cb_2);
        sms = (CheckBox) findViewById(R.id.sms);
        sys = (CheckBox) findViewById(R.id.sys);
        hours = (Spinner) findViewById(R.id.spinner_hours);
        minute = (Spinner) findViewById(R.id.spinner_minute);

        cb_1.setOnCheckedChangeListener(this);
        cb_2.setOnCheckedChangeListener(this);
        sms.setOnCheckedChangeListener(this);
        sys.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_1:
                LogUtils.e("cb_1 click");
                if (isChecked) {
                    LogUtils.e("cb_1 checked");
                } else {
                    LogUtils.e("cb_1 unchecked");
                }
                break;
            case R.id.sms:

                LogUtils.e("sms click");
                if (isChecked) {
                    LogUtils.e("sms checked");
                } else {
                    LogUtils.e("sms unchecked");
                }
                break;
            case R.id.sys:

                LogUtils.e("sys click");
                if (isChecked) {
                    LogUtils.e("sys checked");
                } else {
                    LogUtils.e("sys unchecked");
                }
                break;
            case R.id.cb_2:

                LogUtils.e("cb_2 click");
                if (isChecked) {
                    LogUtils.e("cb_2 checked");
                } else {
                    LogUtils.e("cb_2 unchecked");
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.sms:
                LogUtils.e("sms checked");

                break;
            case R.id.sys:
                LogUtils.e("sys checked");

                break;
        }

    }

    @Override
    public void onClick(View v) {

    }
}
