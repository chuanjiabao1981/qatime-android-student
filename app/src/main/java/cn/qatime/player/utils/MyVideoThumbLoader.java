package cn.qatime.player.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.util.HashMap;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;

public class MyVideoThumbLoader {
    //创建cache
    private LruCache<String, Bitmap> lruCache;
    private int TAG = 0x7f0e0100;

    @SuppressLint("NewApi")
    public MyVideoThumbLoader() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取最大的运行内存
        int maxSize = maxMemory / 4;
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //这个方法会在每次存入缓存的时候调用
                return value.getByteCount();
            }
        };
    }

    public void addVideoThumbToCache(String path, Bitmap bitmap) {
        if (getVideoThumbToCache(path) == null) {
            //当前地址没有缓存时，就添加
            lruCache.put(path, bitmap);
        }
    }

    public Bitmap getVideoThumbToCache(String path) {

        return lruCache.get(path);

    }

    public void showThumbByAsyncTask(String path, ImageView imgView) {
        imgView.setTag(TAG, path);
        if (getVideoThumbToCache(path) == null) {
            //异步加载
            new MyBobAsyncTask(imgView, path, VideoType.URL).execute(path);
        } else {
            imgView.setImageBitmap(getVideoThumbToCache(path));
        }

    }

    public void showThumbByAsyncTask(File file, ImageView imgView) {
        String path = file.getAbsolutePath();
        imgView.setTag(TAG, path);
        if (getVideoThumbToCache(path) == null) {
            //异步加载
            new MyBobAsyncTask(imgView, path, VideoType.FILE).execute(path);
        } else {
            imgView.setImageBitmap(getVideoThumbToCache(path));
        }

    }

    class MyBobAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imgView;
        private String path;
        private VideoType type;

        public MyBobAsyncTask(ImageView imageView, String path, VideoType type) {
            this.imgView = imageView;
            this.path = path;
            this.type = type;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = createVideoThumbnail(params[0], 150, 150, type);
            //加入缓存中
            if (getVideoThumbToCache(params[0]) == null) {
                addVideoThumbToCache(path, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imgView.getTag(TAG).equals(path)) {
                imgView.setImageBitmap(bitmap);
            }
        }
    }

    private Bitmap createVideoThumbnail(String videoPath, int width,
                                        int height, VideoType type) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (type == VideoType.FILE) {
                retriever.setDataSource(videoPath);
            } else {
                retriever.setDataSource(videoPath, new HashMap<String, String>());
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }

        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), R.mipmap.unknown);
        }
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    private enum VideoType {
        FILE, URL;
    }
}
