package cn.qatime.player.utils;

import android.os.AsyncTask;

import com.orhanobut.logger.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import cn.qatime.player.base.BaseApplication;
import libraryextra.utils.CustomMultipartEntity;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2016/8/12 10:00
 * @Description
 */
public abstract class UpLoadUtil extends AsyncTask<Map<String, String>, String, String> implements CustomMultipartEntity.ProgressListener {
    private final String url;
    private long contentLength;

    public UpLoadUtil(String url) {
        this.url = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        httpStart();
    }

    public abstract void httpStart();


    @Override
    protected String doInBackground(Map<String, String>... params) {
        String json = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            // 设置通信协议版本
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");
            ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);

            HttpPut httpPut = new HttpPut(url);

            httpPut.setHeader("Remember-Token", getHttpTokenHeader());

            Map<String, String> item = params[0];
            Iterator<Map.Entry<String, String>> iterator = item.entrySet().iterator();

            CustomMultipartEntity mpEntity = new CustomMultipartEntity(this); // 文件传输
            contentLength = mpEntity.getContentLength();

            while (iterator.hasNext()) {
                Map.Entry entry = iterator.next();
                if (!StringUtils.isNullOrBlanK(entry.getKey()) && !StringUtils.isNullOrBlanK(entry.getValue())) {
                    if (entry.getKey().toString().equals("avatar")) {
                        File file = new File(entry.getValue().toString());
                        if (file.exists()) {
                            FileBody fileBody = new FileBody(file);
                            mpEntity.addPart("avatar", fileBody);
                        }
                    } else {
                        mpEntity.addPart(entry.getKey().toString(), new StringBody(entry.getValue().toString(), contentType));
                    }
                }
            }
            httpPut.setEntity(mpEntity);

            System.out.println("executing request " + httpPut.getRequestLine());
            HttpResponse response = httpclient.execute(httpPut);
            HttpEntity resEntity = response.getEntity();
            System.out.println(response.getStatusLine());// 通信Ok

            if (resEntity != null) {
                json = EntityUtils.toString(resEntity, "utf-8");
            }
            Logger.e("json", json + "****");
            httpclient.getConnectionManager().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 还未登入的话需要重写返回token
     * @return
     */
    public String getHttpTokenHeader() {
        return BaseApplication.getProfile().getToken();
    }

    @Override
    protected void onProgressUpdate(String... values) {

    }

    @Override
    protected void onPostExecute(String result) {
        try {
            if (!StringUtils.isNullOrBlanK(result)) {
                if (new JSONObject(result).getInt("status") == 0) {
                    httpFailed(result);
                } else {
                    httpSuccess(result);
                }
            } else {
                httpFailed(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            httpFailed(result);
        }
    }

    /**
     * 成功回调
     *
     * @param result
     */
    protected abstract void httpSuccess(String result);

    /**
     * s失败
     *
     * @param result
     */
    protected abstract void httpFailed(String result);

    @Override
    public void transferred(long num) {
        if (contentLength > 0) {
            Logger.e("总大小" + contentLength);
            Logger.e("已上传" + num);
            publishProgress(String.valueOf(num / contentLength * 100) + "%");
        }
    }
}
