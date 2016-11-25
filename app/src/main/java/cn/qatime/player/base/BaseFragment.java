package cn.qatime.player.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.activity.MainActivity;

public class BaseFragment extends Fragment {
    private RequestQueue Queue;
    protected boolean isLoad = false;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Queue = BaseApplication.getRequestQueue();
    }

    public void onShow() {
    }

    /**
     * 设备已在其他地方登陆
     */
    public void tokenOut() {
        BaseApplication.clearToken();
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            alertDialog = builder.create();
            View view = View.inflate(getActivity(), R.layout.dialog_confirm, null);
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
//            attributes.width= ScreenUtils.getScreenWidth(getActivity())- DensityUtils.dp2px(getActivity(),20)*2;
//            alertDialog.getWindow().setAttributes(attributes);
        }
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }

    public void out() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("out", "out");
        startActivity(intent);
//        getActivity().finish();
    }

    private List<Request> requestList = new ArrayList<>();//记录当前页访问的url

    public <T> Request<T> addToRequestQueue(Request<T> request) {
        requestList.add(request);
        return Queue.add(request);
    }

    @Override
    public void onDestroy() {
        for (Request request : requestList) {
            request.cancel();
        }
        super.onDestroy();
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
