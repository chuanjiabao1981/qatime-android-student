package cn.qatime.player.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.base.BaseApplication;

/**
 * @author luntify
 * @date 2016/8/11 16:20
 * @Description
 */
public class DaYiJsonObjectRequest extends JsonObjectRequest {
    public DaYiJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        Logger.e(url);
    }

    public DaYiJsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
        Logger.e(url);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> map = new HashMap<>();
        map.put("Remember-Token", BaseApplication.getProfile().getToken());
        return map;
    }
}
