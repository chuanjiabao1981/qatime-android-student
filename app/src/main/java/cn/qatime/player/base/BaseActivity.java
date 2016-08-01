package cn.qatime.player.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import cn.qatime.player.R;
/**
 * 基础类 
 */
public class BaseActivity extends AppCompatActivity {
    protected RequestQueue Queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Queue = Volley.newRequestQueue(this);
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
