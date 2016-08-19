package cn.qatime.player.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.qatime.player.R;
import cn.qatime.player.activity.LoginActivity;
import libraryextra.utils.StringUtils;

/**
 * 基础类
 */
public class BaseActivity extends AppCompatActivity {
    private RequestQueue Queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Queue = Volley.newRequestQueue(this);
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
        if (BaseApplication.getProfile() != null && BaseApplication.getProfile().getData() != null && BaseApplication.getProfile().getData() != null) {
            BaseApplication.getProfile().getData().setRemember_token("");
        }
        Dialog dialog = new Dialog(this, R.style.Transparent);
        View view = View.inflate(this, R.layout.activity_out_alertdialog, null);
        view.findViewById(R.id.alert_dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                out();
            }
        });
        dialog.setContentView(view);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                out();
            }
        });
        dialog.show();
    }

    private void out() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
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
}
