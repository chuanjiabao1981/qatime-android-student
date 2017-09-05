package cn.qatime.player.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.PictureSelectAdapter;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.ImageBucket;
import libraryextra.bean.ImageItem;
import libraryextra.utils.AlbumHelper;
import libraryextra.utils.ScreenUtils;

/**
 * @author luntify
 * @date 2016/8/10 20:36
 * @Description 图片选   择页面
 */
public class PictureSelectActivity extends BaseActivity {

    private List<ImageItem> detailList = new ArrayList<>();

    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            selected(imagesBucketList.get(0));//选中第一个
            /**
             * 为底部的布局设置点击事件，弹出popupWindow
             */
            bottomLyout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initFolderList();
                }
            });

        }
    };
    private AlbumHelper helper;
    private PictureSelectAdapter adapter;
    private int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 1;
    private boolean cameraGone;
    private String capturePath;
    private List<ImageBucket> imagesBucketList;
    private View bottomLyout;
    private TextView chooseDir;
    private TextView totalCount;
    private GridView gridView;
    private boolean mIsFolderViewInit;
    private boolean mIsFolderViewShow;
    private ListView mFolderListView;
    private CommonAdapter<ImageBucket> folderAdapter;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_select);
        setTitles(getResources().getString(R.string.select_picture));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
            } else {
                initImagesData();
            }
        } else {
            initImagesData();
        }
        cameraGone = getIntent().getBooleanExtra("gonecamera", false);//设置是否显示照相
        initView();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_SOME_FEATURES_PERMISSIONS) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initImagesData();
            }
        } else if (requestCode == 2) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "未取得拍照权限", Toast.LENGTH_SHORT).show();
            } else {
                openCamera();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initView() {
        bottomLyout = findViewById(R.id.id_bottom_ly);
        chooseDir = (TextView) findViewById(R.id.id_choose_dir);
        totalCount = (TextView) findViewById(R.id.id_total_count);

        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new PictureSelectAdapter(this, detailList, cameraGone);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapter.isCameraGone()) {
                    Intent data = new Intent();
                    data.putExtra("data", detailList.get(position));
                    setResult(Constant.RESPONSE_PICTURE_SELECT, data);
                    finish();
                } else {
                    if (position == 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(PictureSelectActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(PictureSelectActivity.this, new String[]{
                                        android.Manifest.permission.CAMERA}, 2);
                            } else {
                                openCamera();
                            }
                        } else {
                            openCamera();
                        }

                    } else {
                        Intent data = new Intent();
                        data.putExtra("data", detailList.get(position - 1));
                        setResult(Constant.RESPONSE_PICTURE_SELECT, data);
                        finish();
                    }
                }
            }
        });
    }

    private void openCamera() {
        // ##########拍照##########
        Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
        String out_file_path = Constant.CACHEPATH;
        File dir = new File(out_file_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        capturePath = out_file_path + "/" + System.currentTimeMillis() + ".jpg";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
            imageUri = FileProvider.getUriForFile(this, "com.qatime.player.fileprovider", new File(capturePath));//通过FileProvider创建一个content类型的Uri，进行封装
            getImageByCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getImageByCamera.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            imageUri = Uri.fromFile(new File(capturePath));
        }
        getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        getImageByCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(getImageByCamera, Constant.REQUEST_CAMERA);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_CAMERA) {//拍照返回
            if (data == null) {
                data = new Intent();
            }
            if (new File(capturePath).exists()) {
                data.putExtra("url", capturePath);
                setResult(Constant.RESPONSE_CAMERA, data);
            }
            finish();
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void initImagesData() {
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
                imagesBucketList = helper.getImagesBucketList(false);
                int remove = -1;
                for (int i = 0; i < imagesBucketList.size(); i++) {
                    if (imagesBucketList.get(i).bucketName.equals("qatime")) {
                        remove = i;
                        break;
                    }
                }
                if (remove != -1) {
                    imagesBucketList.remove(remove);
                }
                if (imagesBucketList.size() <= 0) return;
                ImageBucket firstBucket = new ImageBucket();
                firstBucket.imageList = new ArrayList<>();
                for (int i = 0; i < imagesBucketList.size(); i++) {
                    imagesBucketList.get(i).setIsSelected(false);
                    for (int j = 0; j < imagesBucketList.get(i).imageList.size(); j++) {
                        firstBucket.imageList.add(imagesBucketList.get(i).imageList.get(j));
                    }
                }
                firstBucket.bucketName = "所有图片";
                firstBucket.count = firstBucket.imageList.size();
                firstBucket.setIsSelected(true);
                imagesBucketList.add(0, firstBucket);

                hd.sendEmptyMessage(1);//通知bottomlayout可以点击了
            }
        }).start();
    }

    private void getImages(ImageBucket folder) {
        detailList.clear();
        for (int j = 0; j < folder.imageList.size(); j++) {
            detailList.add(folder.imageList.get(j));
        }
        Logger.e(detailList.size() + "张图");
        adapter.notifyDataSetChanged();
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

    public void selected(ImageBucket folder) {
        getImages(folder);
        chooseDir.setText(folder.bucketName);
        totalCount.setText(folder.count + "张");
    }

    /**
     * 显示或者隐藏文件夹列表
     */
    private void initFolderList() {
        //初始化文件夹列表
        if (!mIsFolderViewInit) {
            ViewStub folderStub = (ViewStub) findViewById(R.id.floder_stub);
            folderStub.inflate();


            View dimLayout = findViewById(R.id.dim_layout);
            mFolderListView = (ListView) findViewById(R.id.id_list_dir);
            folderAdapter = new CommonAdapter<ImageBucket>(PictureSelectActivity.this, imagesBucketList,
                    R.layout.list_dir_item) {
                @Override
                public void convert(ViewHolder holder, ImageBucket item, int position) {
                    holder.setText(R.id.id_dir_item_name, item.bucketName);
                    holder.setImageByUrl(R.id.id_dir_item_image,
                            item.getFirstImagePath());
                    holder.setText(R.id.id_dir_item_count, item.count + "张");
                    holder.getView(R.id.id_dir_item_flag).setVisibility(item.isSelected() ? View.VISIBLE : View.INVISIBLE);
                }
            };
            mFolderListView.setAdapter(folderAdapter);
            mFolderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (ImageBucket folder : imagesBucketList) {
                        folder.setIsSelected(false);
                    }
                    ImageBucket folder = imagesBucketList.get(position);
                    folder.setIsSelected(true);
                    folderAdapter.notifyDataSetChanged();
                    if (position == 0) {
                        adapter.setCameraGone(false);
                    } else {
                        adapter.setCameraGone(true);
                    }
                    toggle();
                    selected(folder);
                }
            });
            dimLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mIsFolderViewShow) {
                        toggle();
                        return true;
                    } else {
                        return false;
                    }
                }
            });


            initAnimation(dimLayout);
            mIsFolderViewInit = true;
        }
        toggle();
    }

    /**
     * 弹出或者收起文件夹列表
     */
    private void toggle() {
        if (mIsFolderViewShow) {
            outAnimatorSet.start();
            mIsFolderViewShow = false;
        } else {
            inAnimatorSet.start();
            mIsFolderViewShow = true;
        }
    }


    /**
     * 初始化文件夹列表的显示隐藏动画
     */
    AnimatorSet inAnimatorSet = new AnimatorSet();
    AnimatorSet outAnimatorSet = new AnimatorSet();

    private void initAnimation(View dimLayout) {
        ObjectAnimator alphaInAnimator, alphaOutAnimator, transInAnimator, transOutAnimator;
        //获取actionBar的高
        TypedValue tv = new TypedValue();
        int height = ScreenUtils.getScreenHeight(this) - bottomLyout.getHeight();
        alphaInAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0f, 0.7f);
        alphaOutAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0.7f, 0f);
        transInAnimator = ObjectAnimator.ofFloat(mFolderListView, "translationY", height, 0);
        transOutAnimator = ObjectAnimator.ofFloat(mFolderListView, "translationY", 0, height);

        LinearInterpolator linearInterpolator = new LinearInterpolator();

        inAnimatorSet.play(transInAnimator).with(alphaInAnimator);
        inAnimatorSet.setDuration(300);
        inAnimatorSet.setInterpolator(linearInterpolator);
        outAnimatorSet.play(transOutAnimator).with(alphaOutAnimator);
        outAnimatorSet.setDuration(300);
        outAnimatorSet.setInterpolator(linearInterpolator);
    }
}
