package cn.qatime.player.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.UUID;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.cropview.CropImageView;
import libraryextra.cropview.callback.LoadCallback;
import libraryextra.cropview.callback.SaveCallback;
import cn.qatime.player.utils.Constant;
import libraryextra.utils.DialogUtils;
import libraryextra.utils.StringUtils;
import libraryextra.view.CustomProgressDialog;

/**
 * 图片裁剪页面
 */
public class CropImageActivity extends BaseActivity {
    private CropImageView cropper;
    CropImageView.CropMode cropMode;
    private CustomProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        setTitles(getResources().getString(R.string.clip_photo));
        String id = getIntent().getStringExtra("id");
        if (StringUtils.isNullOrBlanK(id)) {
            finish();
        }
        int CropModes = getIntent().getIntExtra("CropMode", -1);
        cropper = (CropImageView) findViewById(R.id.copper);
        cropper.startLoad(Uri.parse(id), new LoadCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });
        cropper.setCropMode(CropImageView.CropMode.SQUARE);
        if (CropModes != -1) {
            switch (CropModes) {
                case 0:
                    cropMode = CropImageView.CropMode.FIT_IMAGE;
                    break;
                case 1:
                    cropMode = CropImageView.CropMode.RATIO_4_3;
                    break;
                case 2:
                    cropMode = CropImageView.CropMode.RATIO_3_4;
                    break;
                case 3:
                    cropMode = CropImageView.CropMode.SQUARE;
                    break;
                case 4:
                    cropMode = CropImageView.CropMode.RATIO_16_9;
                    break;
                case 5:
                    cropMode = CropImageView.CropMode.RATIO_9_16;
                    break;
                case 6:
                    cropMode = CropImageView.CropMode.FREE;
                    break;
                case 7:
                    cropMode = CropImageView.CropMode.CUSTOM;
                    break;
                case 8:
                    cropMode = CropImageView.CropMode.CIRCLE;
                    break;
                case 9:
                    cropMode = CropImageView.CropMode.CIRCLE_SQUARE;
                    break;
            }
            cropper.setCropMode(cropMode);
        }

        setRightText(getResources().getString(R.string.use), new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                File dir = new File(Constant.CACHEPATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                v.setClickable(false);
                progress = DialogUtils.startProgressDialog(progress, CropImageActivity.this);
                progress.setCanceledOnTouchOutside(false);
                progress.setCancelable(false);
                cropper.startCrop(Uri.fromFile(new File(Constant.CACHEPATH + "/" + UUID.randomUUID().toString().replace("-", "") + ".jpg")), null, new SaveCallback() {
                    @Override
                    public void onSuccess(Uri outputUri) {
                        Intent intent = new Intent();
                        intent.putExtra("bitmap", outputUri.getPath());
                        setResult(Constant.PHOTO_CROP, intent);
                        v.setClickable(true);
                        DialogUtils.dismissDialog(progress);
                        finish();

                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
