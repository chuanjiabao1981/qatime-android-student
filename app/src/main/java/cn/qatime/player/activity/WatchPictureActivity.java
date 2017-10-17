package cn.qatime.player.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.BitmapDecoder;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.ImageUtil;
import cn.qatime.player.view.MultiTouchZoomableImageView;
import libraryextra.rx.HttpManager;
import libraryextra.rx.callback.DownloadProgressCallBack;
import libraryextra.rx.exception.ApiException;


public class WatchPictureActivity extends BaseActivity {


    private MultiTouchZoomableImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除标题  必须在setContentView()方法之前调用
        setContentView(R.layout.activity_watch_picture);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏
        image = (MultiTouchZoomableImageView) findViewById(R.id.watch_image_view);
        String src = getIntent().getStringExtra("src");
        String cacheFile =Constant.CACHEIMAGEPATH+ src.substring(src.lastIndexOf("/"),src.length());
        if(new File(src).exists()){
            setThumbnail(src);
        }else if(new File(cacheFile).exists()){
            setThumbnail(cacheFile);
        }else {//尝试下载图片到本地
            downFile(src,cacheFile);
        }
    }

    private void downFile(String path, final String cacheFile) {
        HttpManager.downLoad(path).savePath(Constant.CACHEIMAGEPATH).
                saveName(new File(cacheFile).getName())
                .execute(new DownloadProgressCallBack<String>() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                    }

                    @Override
                    public void onComplete(String path) {
                        setThumbnail(cacheFile);
                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onError(ApiException e) {
                        Toast.makeText(WatchPictureActivity.this, "图片获取失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTokenOut() {

                    }
                });
    }

    private void setThumbnail(String path) {
        Bitmap bitmap = null;
        if (!TextUtils.isEmpty(path)) {
            bitmap = BitmapDecoder.decodeSampledForDisplay(path);
            bitmap = ImageUtil.rotateBitmapInNeeded(path, bitmap);
        }
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
            return;
        }

        image.setImageBitmap(ImageUtil.getBitmapFromDrawableRes(this, getImageResOnLoading()));
    }

    private int getImageResOnLoading() {
        return R.mipmap.image_default;
    }
}
