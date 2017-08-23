package cn.qatime.player.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.neliveplayer.util.file.FileUtil;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.ExclusiveFilesBean;
import cn.qatime.player.utils.Constant;
import io.reactivex.disposables.Disposable;
import libraryextra.rx.HttpManager;
import libraryextra.rx.callback.DownloadProgressCallBack;
import libraryextra.rx.exception.ApiException;
import libraryextra.utils.DataCleanUtils;
import libraryextra.utils.StringUtils;

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
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_file_detail);
        id = getIntent().getIntExtra("id", 0);
        initView();
        initData();
    }

    private void initData() {
        String path = getIntent().getStringExtra("path");
        if (StringUtils.isNullOrBlanK(path)) {
            file = (ExclusiveFilesBean) getIntent().getSerializableExtra("file");
            name.setText(file.getName());
            size.setText(file.getSize());
            time.setText(file.getTime());
            File dir = new File(Constant.FILEPATH + "/" + id);
            saveFile = new File(dir, file.getName());
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } else {
            saveFile = new File(path);
            name.setText(saveFile.getName());
            size.setText(DataCleanUtils.getFormatSize(saveFile.length()));
            time.setText("下载时间:" + parse.format(new Date(saveFile.lastModified())));
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
        if (file == null || StringUtils.isNullOrBlanK(file.getUrl())) {
            return;
        }
        nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(2);

        disposable = HttpManager.downLoad(file.getUrl()).savePath(Constant.FILEPATH).
                saveName(file.getName())
                .execute(new DownloadProgressCallBack<String>() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        progress.setText("下载中:" + DataCleanUtils.getFormatSize(bytesRead)
                                + "/" + DataCleanUtils.getFormatSize(contentLength) + "(" + nt.format((double) bytesRead / contentLength) + ")");
                    }

                    @Override
                    public void onComplete(String path) {
                        download.setText("打开文件");
                        download.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        complete = true;
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
