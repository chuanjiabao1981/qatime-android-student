package cn.qatime.player.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.qatime.player.R;
import cn.qatime.player.activity.LoginActivity;

public class BaseFragment extends Fragment {
    private RequestQueue Queue;
    protected boolean isLoad = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Queue = Volley.newRequestQueue(getActivity());
    }

    public void onShow() {
    }

    /**
     * 设备已在其他地方登陆
     */
    public void tokenOut() {

        Dialog dialog = new Dialog(getActivity(), R.style.Transparent);
        View view = View.inflate(getActivity(), R.layout.activity_out_alertdialog, null);
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

    public void out() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
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
