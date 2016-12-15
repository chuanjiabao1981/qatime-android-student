package cn.qatime.player.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.umeng.message.PushAgent;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.MainActivity;
import libraryextra.utils.StringUtils;

/**
 * 基础类
 */
public class BaseActivity extends AppCompatActivity {
    private RequestQueue Queue;
    private AlertDialog alertDialog;
    protected boolean destroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        Queue = BaseApplication.getRequestQueue();
    }

    public void setTitle(String text) {
        if (StringUtils.isNullOrBlanK(text)) {
            throw new IllegalStateException("text can not be a null object");
        }
        if (findViewById(R.id.title) != null) {
            ((TextView) findViewById(R.id.title)).setText(text);
        }
    }

    public void setRightImage(int resource, View.OnClickListener listener) {
        if (findViewById(R.id.right) != null) {
            ((ImageView) findViewById(R.id.right)).setImageResource(resource);
            findViewById(R.id.right).setOnClickListener(listener);
        }

    }

    public void setRightText(String text, View.OnClickListener listener) {
        if (StringUtils.isNullOrBlanK(text)) {
            throw new IllegalArgumentException("text can not be a null object");
        }
        if (findViewById(R.id.right_text) != null) {
            ((TextView) findViewById(R.id.right_text)).setText(text);
            findViewById(R.id.right_text).setOnClickListener(listener);
        }

    }

    public void backClick(View v) {
        this.finish();
    }


    /**
     * 设备已在其他地方登陆
     */
    public void tokenOut() {
        BaseApplication.clearToken();
        if (alertDialog == null) {
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
            alertDialog = builder.create();
            View view = View.inflate(this, R.layout.dialog_confirm, null);
            TextView text = (TextView) view.findViewById(R.id.text);
            text.setText(getResourceString(R.string.login_has_expired));
            Button confirm = (Button) view.findViewById(R.id.confirm);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    out();
                }
            });
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    out();
                }
            });
            alertDialog.show();
            alertDialog.setContentView(view);
//            WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
//            attributes.width= ScreenUtils.getScreenWidth(getApplicationContext())- DensityUtils.dp2px(getApplicationContext(),20)*2;
//            alertDialog.getWindow().setAttributes(attributes);
        }
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    private void out() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("out", "out");
        startActivity(intent);
//        this.finish();
    }

    public <T> Request<T> addToRequestQueue(Request<T> request) {
        return Queue.add(request);
    }

    public void cancelAll(final Object tag) {
        Queue.cancelAll(tag);
    }

    public void cancelAll(final RequestQueue.RequestFilter filter) {
        Queue.cancelAll(filter);
    }

    protected String getResourceString(int id) {
        return getResources().getString(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }
}
