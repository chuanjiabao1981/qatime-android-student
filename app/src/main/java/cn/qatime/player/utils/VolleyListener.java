package cn.qatime.player.utils;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import cn.qatime.player.base.BaseApplication;

/**
 * @author luntify
 * @date 2016/8/11 15:52
 * @Description
 */
public abstract class VolleyListener implements Response.Listener<JSONObject> {
    private Context context;

    public VolleyListener(Context context) {
        this.context = context;
    }

    @Override
    public void onResponse(JSONObject response) {
        LogUtils.e("result-----    "+response.toString());
        try {
            if (response.getInt("status") == 0) {
                onError(response);
                LogUtils.e(response.getJSONObject("error").get("msg").toString());
            } else {
                onSuccess(response);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e("请求********************", "********************异常");
            Toast.makeText(context, "请求出错", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 成功返回
     * @param response
     */
    protected abstract void onSuccess(JSONObject response);

    /**
     * 错误返回
     * @param response
     */
    protected abstract void onError(JSONObject response);
}
