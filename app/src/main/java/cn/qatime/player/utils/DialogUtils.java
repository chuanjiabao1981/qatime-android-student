package cn.qatime.player.utils;

import android.content.Context;

import cn.qatime.player.view.CustomProgressDialog;


/**
 */
public class DialogUtils {

    public static CustomProgressDialog startProgressDialog(CustomProgressDialog progressDialog, Context context, String text) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(context);
            progressDialog.setMessage(text);
        }
        progressDialog.show();
        return progressDialog;
    }

    public static CustomProgressDialog startProgressDialog(CustomProgressDialog progressDialog, Context context) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(context);
        }
        progressDialog.show();
        return progressDialog;
    }

    public static void dismissDialog(CustomProgressDialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
