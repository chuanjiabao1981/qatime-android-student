package cn.qatime.player.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.qatime.player.R;
import cn.qatime.player.activity.LoginActivity;
import cn.qatime.player.activity.MainActivity;
import libraryextra.utils.StringUtils;

/**
 * 基础fragment类
 */
public class BaseFragmentActivity extends FragmentActivity {
    private RequestQueue Queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Queue = Volley.newRequestQueue(this);
    }

    public void setTitle(String text) {
        if (StringUtils.isNullOrBlanK(text)) {
            throw new IllegalArgumentException("text can not be a null object");
        }
        if (findViewById(R.id.title) != null) {
            ((TextView) findViewById(R.id.title)).setText(text);
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

    public void setRightImage(int resource, View.OnClickListener listener) {
        if (findViewById(R.id.right) != null) {
            ((ImageView) findViewById(R.id.right)).setImageResource(resource);
            findViewById(R.id.right).setOnClickListener(listener);
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
        final Dialog dialog = new Dialog(this, R.style.Transparent);
        View view = View.inflate(this, R.layout.activity_out_alertdialog, null);
        view.findViewById(R.id.alert_dialog_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("out", "out");
        startActivity(intent);
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
}
