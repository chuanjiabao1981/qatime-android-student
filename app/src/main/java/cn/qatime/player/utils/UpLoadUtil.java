package cn.qatime.player.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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

import cn.qatime.player.activity.PersonalInformationChangeActivity;
import cn.qatime.player.base.BaseApplication;

/**
 * @author luntify
 * @date 2016/8/12 10:00
 * @Description
 */
public abstract class UpLoadUtil extends AsyncTask<String, String, String> implements CustomMultipartEntity.ProgressListener {
    private final Context context;
    private long contentLength;

    public UpLoadUtil(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        httpStart();
    }

    public abstract void httpStart();


    @Override
    protected String doInBackground(String... params) {
        String json = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            // 设置通信协议版本
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            httpclient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "utf-8");
            ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);

            HttpPost httppost = new HttpPost(params[0]);
            httppost.setHeader("Remember-Token", BaseApplication.getProfile().getToken());

            File file = new File(params[1]);


            CustomMultipartEntity mpEntity = new CustomMultipartEntity(this); // 文件传输
            contentLength = mpEntity.getContentLength();

            mpEntity.addPart("id", new StringBody(params[2], contentType));
            mpEntity.addPart("name", new StringBody(params[3], contentType));
            mpEntity.addPart("grade", new StringBody(params[4], contentType));
            if (file.exists()) {
                FileBody fileBody = new FileBody(file);
                mpEntity.addPart("avatar", fileBody);
            }
            if (!StringUtils.isNullOrBlanK(params[5])) {
                mpEntity.addPart("gender", new StringBody(params[5], contentType));
            }
            if (!StringUtils.isNullOrBlanK(params[6])) {
                mpEntity.addPart("birthday", new StringBody(params[6], contentType));
            }
            if (!StringUtils.isNullOrBlanK(params[7])) {
                mpEntity.addPart("desc", new StringBody(params[7], contentType));
            }
            httppost.setEntity(mpEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            System.out.println(response.getStatusLine());// 通信Ok

            if (resEntity != null) {
                json = EntityUtils.toString(resEntity, "utf-8");
            }
            LogUtils.e("json", json + "****");
            httpclient.getConnectionManager().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
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
            LogUtils.e("总大小" + contentLength);
            LogUtils.e("已上传" + num);
            publishProgress(String.valueOf(num / contentLength * 100) + "%");
        }
    }
}
