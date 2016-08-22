package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.utils.LogUtils;

/**
 * Created by lenovo on 2016/8/22.
 */
public class NotifyClassesActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    private CheckBox cb_1;
    private CheckBox cb_2;
    private RadioGroup rg;
    private RadioButton sms;
    private RadioButton sys;
    private TextView hours;
    private TextView minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        setContentView(R.layout.activity_notify_classes);
        setTitle("课程提醒");
        cb_1 = (CheckBox) findViewById(R.id.cb_1);
        cb_2 = (CheckBox) findViewById(R.id.cb_2);
        rg = (RadioGroup) findViewById(R.id.rg);
        sms = (RadioButton) findViewById(R.id.sms);
        sys = (RadioButton) findViewById(R.id.sys);
        hours = (TextView) findViewById(R.id.hours);
        minute = (TextView) findViewById(R.id.minute);

        rg.setOnCheckedChangeListener(this);
        cb_1.setOnCheckedChangeListener(this);
        cb_2.setOnCheckedChangeListener(this);
        hours.setOnClickListener(this);
        minute.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hours:
                LogUtils.e("hours click");

                break;
            case R.id.minute:

                LogUtils.e("minute click");
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_1:
                LogUtils.e("cb_1 click");
                if (isChecked){
                    LogUtils.e("cb_1 checked");
                }else{
                    LogUtils.e("cb_1 unchecked");
                }
                break;
            case R.id.cb_2:

                LogUtils.e("cb_2 click");
                if (isChecked){
                    LogUtils.e("cb_2 checked");
                }else{
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
}
