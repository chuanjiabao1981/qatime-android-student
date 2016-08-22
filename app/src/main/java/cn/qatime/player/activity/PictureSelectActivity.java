package cn.qatime.player.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.PictureSelectAdapter;
import cn.qatime.player.base.BaseActivity;
import libraryextra.bean.ImageBucket;
import libraryextra.bean.ImageItem;
import cn.qatime.player.utils.AlbumHelper;
import cn.qatime.player.utils.Constant;
import libraryextra.utils.LogUtils;

/**
 * @author luntify
 * @date 2016/8/10 20:36
 * @Description 图片选择页面
 */
public class PictureSelectActivity extends BaseActivity {

    private List<ImageItem> detailList = new ArrayList<>();

    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
        }
    };
    private AlbumHelper helper;
    private GridView gridView;
    private PictureSelectAdapter adapter;
    private int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_select);
        setTitle(getResources().getString(R.string.select_picture));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
            } else {
                getImages();
            }
        }
        initView();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_SOME_FEATURES_PERMISSIONS) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImages();
                } else {//未给权限

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new PictureSelectAdapter(this, detailList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // ##########拍照##########
                    Intent newIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(newIntent, Constant.REQUEST_CAMERA);
                } else {
                    Intent data = new Intent();
                    data.putExtra("data", detailList.get(position - 1));
                    setResult(Constant.RESPONSE_PICTURE_SELECT, data);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_CAMERA) {//拍照返回
            setResult(Constant.RESPONSE_CAMERA, data);
            finish();
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, getResources().getString(R.string.no_external_storage), Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
//        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ImageBucket> list = helper.getImagesBucketList(false);
                int remove = -1;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).bucketName.equals("qatime")) {
                        remove = i;
                        break;
                    }
                }
                if (remove != -1) {
                    list.remove(remove);
                }
                detailList.clear();
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < list.get(i).imageList.size(); j++) {
                        detailList.add(list.get(i).imageList.get(j));
                    }
                }
                LogUtils.e(detailList.size() + "张图");
                hd.sendEmptyMessage(1);
            }
        }).start();
    }
}
