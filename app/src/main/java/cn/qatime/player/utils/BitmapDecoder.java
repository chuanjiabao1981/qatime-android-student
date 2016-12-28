package cn.qatime.player.utils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.netease.neliveplayer.util.sys.ScreenUtil;

import libraryextra.utils.DensityUtils;

/**
 * @author lungtify
 * @Time 2016/12/13 20:53
 * @Describe
 */

public class BitmapDecoder {
    public static Bitmap decodeSampledForDisplay(String pathName) {
        return decodeSampledForDisplay(pathName, true);
    }

    public static Bitmap decodeSampledForDisplay(String pathName, boolean withTextureLimit) {
        float ratio = 5f;
        int[][] reqBounds = new int[][]{
                new int[]{ScreenUtil.screenWidth * 2, ScreenUtil.screenHeight},
                new int[]{ScreenUtil.screenWidth, ScreenUtil.screenHeight * 2},
                new int[]{(int) (ScreenUtil.screenWidth * 1.414), (int) (ScreenUtil.screenHeight * 1.414)},
        };

        // decode bound
        int[] bound = decodeBound(pathName);
        // pick request bound
        int[] reqBound = pickReqBoundWithRatio(bound, reqBounds, ratio);

        int width = bound[0];
        int height = bound[1];
        int reqWidth = reqBound[0];
        int reqHeight = reqBound[1];

        // calculate sample size
        int sampleSize = calculateSampleSize(width, height, reqWidth, reqHeight);

        if (withTextureLimit) {
            // adjust sample size
            sampleSize = adjustSampleSizeWithTexture(sampleSize, width, height);
        }

        int RETRY_LIMIT = 5;
        Bitmap bitmap = decodeSampled(pathName, sampleSize);
        while (bitmap == null && RETRY_LIMIT > 0) {
            sampleSize++;
            RETRY_LIMIT--;
            bitmap = decodeSampled(pathName, sampleSize);
        }

        return bitmap;
    }

    public static Bitmap decodeSampled(String pathName, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        // RGB_565
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // sample size
        options.inSampleSize = sampleSize;

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(pathName, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }

        return checkInBitmap(bitmap, options, pathName);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static Bitmap checkInBitmap(Bitmap bitmap,
                                        BitmapFactory.Options options, String path) {
        boolean honeycomb = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
        if (honeycomb && bitmap != options.inBitmap && options.inBitmap != null) {
            options.inBitmap.recycle();
            options.inBitmap = null;
        }

        if (bitmap == null) {
            try {
                bitmap = BitmapFactory.decodeFile(path, options);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    private static int[] pickReqBoundWithRatio(int[] bound, int[][] reqBounds, float ratio) {
        float hRatio = bound[1] == 0 ? 0 : (float) bound[0] / (float) bound[1];
        float vRatio = bound[0] == 0 ? 0 : (float) bound[1] / (float) bound[0];

        if (hRatio >= ratio) {
            return reqBounds[0];
        } else if (vRatio >= ratio) {
            return reqBounds[1];
        } else {
            return reqBounds[2];
        }
    }

    public static final int adjustSampleSizeWithTexture(int sampleSize, int width, int height) {
        int textureSize = DensityUtils.getTextureSize();

        if ((textureSize > 0) && ((width > sampleSize) || (height > sampleSize))) {
            while ((width / (float) sampleSize) > textureSize || (height / (float) sampleSize) > textureSize) {
                sampleSize++;
            }

            // 2的指数对齐
            sampleSize = DensityUtils.roundup2n(sampleSize);
        }

        return sampleSize;
    }


    public static int[] decodeBound(String pathName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        return new int[]{options.outWidth, options.outHeight};
    }

    public static int calculateSampleSize(int width, int height, int reqWidth, int reqHeight) {
        // can't proceed
        if (width <= 0 || height <= 0) {
            return 1;
        }
        // can't proceed
        if (reqWidth <= 0 && reqHeight <= 0) {
            return 1;
        } else if (reqWidth <= 0) {
            reqWidth = (int) (width * reqHeight / (float) height + 0.5f);
        } else if (reqHeight <= 0) {
            reqHeight = (int) (height * reqWidth / (float) width + 0.5f);
        }

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            if (inSampleSize == 0) {
                inSampleSize = 1;
            }

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }

        return inSampleSize;
    }
}
