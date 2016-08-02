package cn.qatime.player.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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
public void backClick(View v){
    this.finish();
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
