package cn.qatime.player.utils;

import android.os.AsyncTask;

import com.orhanobut.logger.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

import libraryextra.utils.CustomMultipartEntity;
import libraryextra.utils.FileUtil;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2016/8/12 10:00
 * @Description
 */
public abstract class DownLoadUtil extends AsyncTask<Void, Long, String> implements CustomMultipartEntity.ProgressListener {
    private final String url;
    private File file;
    private long contentLength;

    public DownLoadUtil(String url, File file) {
        this.url = url;
        this.file = file;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        httpStart();
    }

    public abstract void httpStart();


    @Override
    protected String doInBackground(Void... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = client.execute(httpget);

            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            FileUtil.writeFile(is,file.getAbsolutePath(),false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Long... progress) {

    }

    @Override
    protected void onPostExecute(String result) {
        try {
            if (!StringUtils.isNullOrBlanK(result)) {
                if (new JSONObject(result).getInt("status") == 0) {
                    // TODO: 2016/8/26 int == 500  Internal Server Error?
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
     * 下载进度
     *
     * @param progress
     */
    protected abstract void httpProgress(Long progress);

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
            publishProgress(num / contentLength * 100);
        }
    }
}
