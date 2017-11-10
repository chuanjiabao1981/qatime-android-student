package cn.qatime.player.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.netease.neliveplayer.util.file.FileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.MyFilesBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.MyVideoThumbLoader;
import cn.qatime.player.utils.UrlUtils;
import io.reactivex.disposables.Disposable;
import libraryextra.rx.HttpManager;
import libraryextra.rx.callback.DownloadProgressCallBack;
import libraryextra.rx.exception.ApiException;
import libraryextra.utils.DataCleanUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2017/8/15.
 */

public class ExclusiveFileDetailActivity extends BaseActivity {

    private TextView name;
    private TextView size;
    private TextView time;
    private TextView download;
    private TextView progress;
    private File saveFile;
    private MyFilesBean.DataBean file;
    private Disposable disposable;
    private boolean complete;
    private NumberFormat nt;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String fileId;
    private String courseId;
    private ImageView image;
    private View downloadLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_file_detail);

        initView();
        initFileData();
    }

    private void initData() {
        setTitles(saveFile.getName());

        String extName = saveFile.getName().substring(saveFile.getName().lastIndexOf(".") + 1, saveFile.getName().length());
        if (extName.equals("doc") || extName.equals("docx")) {
            image.setImageResource(R.mipmap.word);
        } else if (extName.equals("xls") || extName.equals("xlsx")) {
            image.setImageResource(R.mipmap.excel);
        } else if (extName.equals("pdf")) {
            image.setImageResource(R.mipmap.pdf);
        } else if (extName.equals("mp4")) {
            MyVideoThumbLoader mVideoThumbLoader = new MyVideoThumbLoader();
            if (saveFile.exists()) {
                mVideoThumbLoader.showThumbByAsyncTask(saveFile, image);
            } else {
                mVideoThumbLoader.showThumbByAsyncTask(file.getFile_url(), image);
            }
        } else if (extName.equals("jpg") || extName.equals("png")) {
            if (saveFile.exists()) {
                Glide.with(this).load(saveFile.getAbsolutePath()).placeholder(R.mipmap.unknown).centerCrop().crossFade().dontAnimate().into(image);
            } else {
                Glide.with(this).load(file.getFile_url()).placeholder(R.mipmap.unknown).centerCrop().crossFade().dontAnimate().into(image);
            }
        } else {
            image.setImageResource(R.mipmap.unknown);
        }

        if (saveFile.exists()) {
            download.setText("打开文件");
        }


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!saveFile.exists()) {
                    downFile();
                } else {
                    Toast.makeText(ExclusiveFileDetailActivity.this, "打开文件", Toast.LENGTH_SHORT).show();
                    openFile(saveFile);
                }
            }
        });
    }

    private void initFileData() {
        String path = getIntent().getStringExtra("path");
        if (StringUtils.isNullOrBlanK(path)) {
            courseId = getIntent().getStringExtra("courseId");
            fileId = getIntent().getStringExtra("id");
            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlGroups+courseId + "/files/" + fileId
                    , null, new VolleyListener(ExclusiveFileDetailActivity.this) {
                @Override
                protected void onTokenOut() {
                    tokenOut();
                }

                @Override
                protected void onSuccess(JSONObject response) {
                    try {
                        file = JsonUtils.objectFromJson(response.getJSONObject("data").toString(), MyFilesBean.DataBean.class);
                        name.setText(file.getName());
                        size.setText(DataCleanUtils.getFormatSize(Double.valueOf(file.getFile_size())));
                        time.setText("上传时间:" + parse.format(new Date(file.getCreated_at()*1000)));
                        File dir = new File(Constant.FILEPATH + "/" + courseId);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }

                        saveFile = new File(dir, file.getName().replace("." + file.getExt_name(), "_" + fileId + "." + file.getExt_name()));
                        initData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected void onError(JSONObject response) {

                }
            }, new VolleyErrorListener());
            addToRequestQueue(request);
        } else {
            saveFile = new File(path);
            name.setText(saveFile.getName());
            size.setText(DataCleanUtils.getFormatSize(saveFile.length()));
            time.setText("下载时间:" + parse.format(new Date(saveFile.lastModified())));
            initData();
        }
    }

    public void openFile(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String name = file.getName();
        String type = FileUtil.getMimeType(name.substring(name.lastIndexOf("."), name.length()));
        //设置intent的data和Type属性。
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, "com.qatime.player.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, type);
        //跳转
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "找不到打开此文件的应用！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    private void downFile() {
        if (file == null || StringUtils.isNullOrBlanK(file.getFile_url())) {
            return;
        }
        disposable = HttpManager.downLoad(file.getFile_url()).savePath(saveFile.getParent()).
                saveName(saveFile.getName())
                .execute(new DownloadProgressCallBack<String>() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        progress.setText("下载中...(" + DataCleanUtils.getFormatSize(bytesRead)
                                + "/" + DataCleanUtils.getFormatSize(contentLength) + ")");
                        int pro = (int) (bytesRead / contentLength * 100);
                        progressBar.setProgress(pro);
                    }

                    @Override
                    public void onComplete(String path) {
                        download.setText("打开文件");
                        download.setVisibility(View.VISIBLE);
                        downloadLayout.setVisibility(View.GONE);
                        complete = true;
                        Toast.makeText(ExclusiveFileDetailActivity.this, path, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStart() {
                        download.setVisibility(View.GONE);
                        downloadLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onTokenOut() {

                    }
                });
    }

    private void initView() {
        name = (TextView) findViewById(R.id.name);
        size = (TextView) findViewById(R.id.size);
        time = (TextView) findViewById(R.id.time);
        image = (ImageView) findViewById(R.id.image);
        download = (TextView) findViewById(R.id.download);
        progress = (TextView) findViewById(R.id.progress);
        downloadLayout = findViewById(R.id.download_layout);
        progressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
    }
}
