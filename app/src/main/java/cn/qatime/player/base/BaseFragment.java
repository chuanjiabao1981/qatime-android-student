package cn.qatime.player.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import cn.qatime.player.R;
import cn.qatime.player.activity.MainActivity;
import cn.qatime.player.utils.MPermission;

public class BaseFragment extends Fragment {
    private RequestQueue Queue = BaseApplication.getInstance().getRequestQueue();
    protected boolean isLoad = false;
    protected boolean initOver = false;
    private AlertDialog alertDialog;
    private Handler hd = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (initOver) {
                hd.removeCallbacks(this);
                onShow();
            } else {
                hd.postDelayed(this, 200);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onShow() {
        //如果还没initOver,等待initOver
        hd.postDelayed(runnable, 200);
    }

    /**
     * 设备已在其他地方登录
     */
    public void tokenOut() {
        BaseApplication.getInstance().clearToken();
        if (getActivity() != null) {
            if (alertDialog == null) {
                View view = View.inflate(getActivity(), R.layout.dialog_confirm, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                alertDialog = builder.create();
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
            } else {
                if (!alertDialog.isShowing()) {
                    alertDialog.show();
                }
            }
        }
    }

    public void out() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("out", "out");
        startActivity(intent);
//        getActivity().finish();
    }

    public <T> Request<T> addToRequestQueue(Request<T> request) {
        request.setTag(this);
        return Queue.add(request);
    }

    @Override
    public void onDestroy() {
        cancelAll(this);
        super.onDestroy();
    }

    public void cancelAll(final Object tag) {
        Queue.cancelAll(tag);
    }

    public void cancelAll(final RequestQueue.RequestFilter filter) {
        Queue.cancelAll(filter);
    }

    protected String getResourceString(int id) {
        return BaseApplication.getInstance().getString(id);
    }

    protected <T extends View> T findViewById(int resId) {
        return (T) (getView().findViewById(resId));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
