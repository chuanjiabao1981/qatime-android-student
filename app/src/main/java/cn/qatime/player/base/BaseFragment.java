package cn.qatime.player.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class BaseFragment extends Fragment {
    private RequestQueue Queue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Queue = Volley.newRequestQueue(getActivity());
    }

    public <T> Request<T> addToRequestQueue(Request<T> request){
        return Queue.add(request);
    }
    public void cancelAll(final Object tag) {
        Queue.cancelAll(tag);
    }
    public void cancelAll(final RequestQueue.RequestFilter filter) {
        Queue.cancelAll(filter);
    }
}
