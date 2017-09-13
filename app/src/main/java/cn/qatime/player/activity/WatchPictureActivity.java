package cn.qatime.player.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.BitmapDecoder;
import cn.qatime.player.utils.ImageUtil;
import cn.qatime.player.view.MultiTouchZoomableImageView;


public class WatchPictureActivity extends BaseActivity {


    private MultiTouchZoomableImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除标题  必须在setContentView()方法之前调用
        setContentView(R.layout.activity_watch_picture);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏
        image = (MultiTouchZoomableImageView) findViewById(R.id.watch_image_view);
      setThumbnail(getIntent().getStringExtra("src"));
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
