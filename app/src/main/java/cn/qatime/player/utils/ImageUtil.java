package cn.qatime.player.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.TextUtils;

import java.io.IOException;

/**
 * @author lungtify
 * @Time 2016/12/13 20:47
 * @Describe
 */

public class ImageUtil {

    /**
     * 下载失败与获取失败时都统一显示默认下载失败图片
     *
     * @return
     */
    public static Bitmap getBitmapFromDrawableRes(Context context, int res) {
        try {
            return getBitmapImmutableCopy(context.getResources(), res);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final Bitmap getBitmapImmutableCopy(Resources res, int id) {
        return getBitmap(res.getDrawable(id)).copy(Bitmap.Config.RGB_565, false);
    }

    public static final Bitmap getBitmap(Drawable dr) {
        if (dr == null) {
            return null;
        }
        if (dr instanceof BitmapDrawable) {
            return ((BitmapDrawable) dr).getBitmap();
        }

        return null;
    }

    public static Bitmap rotateBitmapInNeeded(String path, Bitmap srcBitmap) {
        if (TextUtils.isEmpty(path) || srcBitmap == null) {
            return null;
        }

        ExifInterface localExifInterface;
        try {
            localExifInterface = new ExifInterface(path);
            int rotateInt = localExifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            float rotate = getImageRotate(rotateInt);
            if (rotate != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                Bitmap dstBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                        srcBitmap.getWidth(), srcBitmap.getHeight(), matrix,
                        false);
                if (dstBitmap == null) {
                    return srcBitmap;
                } else {
                    if (srcBitmap != null && !srcBitmap.isRecycled()) {
                        srcBitmap.recycle();
                    }
                    return dstBitmap;
                }
            } else {
                return srcBitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return srcBitmap;
        }
    }

    /**
     * 获得旋转角度
     *
     * @param rotate
     * @return
     */
    private static float getImageRotate(int rotate) {
        float f;
        if (rotate == 6) {
            f = 90.0F;
        } else if (rotate == 3) {
            f = 180.0F;
        } else if (rotate == 8) {
            f = 270.0F;
        } else {
            f = 0.0F;
        }
        return f;
    }
}
