package cn.qatime.player.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.InputStream;

import libraryextra.utils.GifHelper;

/**
 * @author lungtify
 */
public class GifDrawable extends AnimationDrawable {

    private int mCurrentIndex = 0;
    private UpdateListener mListener;

    public GifDrawable(Context context, InputStream source, UpdateListener listener) {
        mListener = listener;

        GifHelper decoder = new GifHelper();
        decoder.read(source);
        // 遍历gif图像里面的每一帧，放进animation frame中
        for (int i = 0; i < decoder.getFrameCount(); i++) {
            Bitmap bitmap = decoder.getFrame(i);
            BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
            // 为每一帧设置边界
            drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            addFrame(drawable, decoder.getDelay(i));
            if (i == 0) {
                // 为容器设置一个边界
                setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            }
        }
    }

    /**
     * 继续下一帧，同时通知监听器
     */
    public void nextFrame() {
        // 当循环到最后一帧时，索引就会为0，注意索引比帧的数量小1
        mCurrentIndex = (mCurrentIndex + 1) % getNumberOfFrames();
        if (mListener != null) {
            mListener.update();
        }
    }

    /**
     * 返回当前帧的显示时间
     *
     * @return
     */
    public int getFrameDuration() {
        return getDuration(mCurrentIndex);
    }

    /**
     * 返回当前帧的图片
     *
     * @return
     */
    public Drawable getDrawable() {
        return getFrame(mCurrentIndex);
    }

    /**
     * 该接口通知监听器更新/重绘界面
     *
     * @author moon
     */
    public interface UpdateListener {
        void update();
    }

    public void setUpdateListener(UpdateListener listener) {
        this.mListener = listener;
    }
}