package cn.qatime.player.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.BitmapDecoder;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.ImageUtil;
import cn.qatime.player.view.BaseZoomableImageView;
import cn.qatime.player.view.ImageGestureListener;
import libraryextra.bean.ImageItem;
import libraryextra.rx.HttpManager;
import libraryextra.rx.callback.DownloadProgressCallBack;
import libraryextra.rx.exception.ApiException;


public class WatchPictureActivity extends BaseActivity {


    private List<ImageItem> imageItems;
    private ViewPager imageViewPager;
    private RelativeLayout loadingLayout;
    private PagerAdapter adapter;
    private int position;
    private boolean newPageSelected = false;
    private BaseZoomableImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); // 去除标题  必须在setContentView()方法之前调用
        setContentView(R.layout.activity_watch_picture);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏
        imageItems = (ArrayList<ImageItem>) getIntent().getSerializableExtra("imageItems");
        position = getIntent().getIntExtra("position", 0);
        imageViewPager = (ViewPager) findViewById(R.id.view_pager_image);
        loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
         setViewPagerAdapter();
    }

    private void setViewPagerAdapter() {
        adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return imageItems == null ? 0 : imageItems.size();
            }

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View layout = (View) object;
                BaseZoomableImageView iv = (BaseZoomableImageView) layout.findViewById(R.id.watch_image_view);
                iv.clear();
                container.removeView(layout);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return (view == object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ViewGroup layout;
                layout = (ViewGroup) LayoutInflater.from(WatchPictureActivity.this).inflate(R.layout.image_layout_multi_touch, null);
                layout.setBackgroundColor(Color.BLACK);
                container.addView(layout);
                layout.setTag(position);
                if (position == WatchPictureActivity.this.position) {
                    onViewPagerSelected(position);
                }
                return layout;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };

        imageViewPager.setAdapter(adapter);
        imageViewPager.setOffscreenPageLimit(2);
        imageViewPager.setCurrentItem(position);
        imageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0f && newPageSelected) {
                    newPageSelected = false;
                    onViewPagerSelected(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                newPageSelected = true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void onViewPagerSelected(int position) {
        updateCurrentImageView(position);
        onImageViewFound(image);
        showPic(position);
    }

    // 设置图片点击事件
    protected void onImageViewFound(BaseZoomableImageView imageView) {
        imageView.setImageGestureListener(new ImageGestureListener() {

            @Override
            public void onImageGestureSingleTapConfirmed() {
                onImageViewTouched();
            }

            @Override
            public void onImageGestureLongPress() {
            }

            @Override
            public void onImageGestureFlingDown() {
                finish();
            }
        });
    }

    // 图片单击
    protected void onImageViewTouched() {
        finish();
    }



    // 初始化每个view的image
    protected void updateCurrentImageView(final int position) {
        View currentLayout = imageViewPager.findViewWithTag(position);
        if (currentLayout == null) {
            ViewCompat.postOnAnimation(imageViewPager, new Runnable() {

                @Override
                public void run() {
                    updateCurrentImageView(position);
                }
            });
            return;
        }

        image = (BaseZoomableImageView) currentLayout.findViewById(R.id.watch_image_view);
    }

    private void showPic(int position) {
        String currentImagePath = imageItems.get(position).imagePath;
        String cacheFile = Constant.CACHEIMAGEPATH + currentImagePath.substring(currentImagePath.lastIndexOf("/"), currentImagePath.length());
        if (new File(currentImagePath).exists()) {
            setThumbnail(currentImagePath);
        } else if (new File(cacheFile).exists()) {
            setThumbnail(cacheFile);
        } else {//尝试下载图片到本地
            loadingLayout.setVisibility(View.VISIBLE);
            downFile(currentImagePath, cacheFile);
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WatchPictureActivity.this, "图片获取失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onTokenOut() {

                    }
                });
    }

    private void setThumbnail(String path) {
        loadingLayout.setVisibility(View.GONE);
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
