package cn.qatime.player.utils;

import android.app.DatePickerDialog;
import android.content.Context;

/**
 * 时间选择器
 */
public class MDatePickerDialog extends DatePickerDialog {

    public MDatePickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    @Override
    protected void onStop() {
        //super.onStop();
    }
}