package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.NumberFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.ExclusiveFilesBean;
import cn.qatime.player.utils.Constant;
import io.reactivex.disposables.Disposable;
import io.vov.vitamio.utils.FileUtils;
import libraryextra.rx.HttpManager;
import libraryextra.rx.callback.DownloadProgressCallBack;
import libraryextra.rx.exception.ApiException;
import libraryextra.rx.request.DownloadRequest;
import libraryextra.utils.DataCleanUtils;

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
    private ExclusiveFilesBean file;
    private Disposable disposable;
    private boolean complete;
    private NumberFormat nt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_file_detail);
        initView();
        initData();
    }

    private void initData() {
        file = (ExclusiveFilesBean) getIntent().getSerializableExtra("file");
        name.setText(file.getName());
        size.setText(file.getSize());
        time.setText(file.getTime());
        saveFile = new File(Constant.FILEPATH + "/" + file.getName());
        if (saveFile.exists()) {
            download.setText("打开文件");
        }
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!saveFile.exists()) {
                    downFile();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(disposable!=null){
            disposable.dispose();
         /*   if(!complete){
                FileUtils.deleteDir(saveFile);
            }*/
        }
        FileUtils.deleteDir(saveFile);
        super.onDestroy();
    }

    private void downFile() {
        nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(2);
        DownloadRequest downloadRequest = HttpManager.downLoad(file.getUrl());
        disposable = downloadRequest.savePath(Constant.FILEPATH).
                saveName(file.getName())
                .execute(new DownloadProgressCallBack<String>() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        progress.setText("下载中:" + DataCleanUtils.getFormatSize(bytesRead)
                                + "/" + DataCleanUtils.getFormatSize(contentLength)+"("+nt.format((double) bytesRead/contentLength)+")");
                    }

                    @Override
                    public void onComplete(String path) {
                        download.setText("打开文件");
                        download.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        complete=true;
                        Toast.makeText(ExclusiveFileDetailActivity.this, path, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStart() {
                        download.setVisibility(View.GONE);
                        progress.setVisibility(View.VISIBLE);
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
        download = (TextView) findViewById(R.id.download);
        progress = (TextView) findViewById(R.id.progress);
    }
}
