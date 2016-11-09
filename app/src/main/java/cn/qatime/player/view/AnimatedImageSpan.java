package cn.qatime.player.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.style.DynamicDrawableSpan;

/**
 * @author lungtify
 * @date 2016/9/6 14:18
 * @Description:
 */
public class AnimatedImageSpan extends DynamicDrawableSpan {
    private Drawable mDrawable;

    public AnimatedImageSpan(Drawable d) {
        super();
        mDrawable = d;
        // 用handler 通知继续下一帧
        final Handler mHandler = new Handler();
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                ((GifDrawable) mDrawable).nextFrame();
                // 设置下一个的延迟取决于当前帧的持续时间
                mHandler.postDelayed(this,
                        ((GifDrawable) mDrawable).getFrameDuration());
            }
        });
    }

    @Override
    public Drawable getDrawable() {
        return ((GifDrawable) mDrawable).getDrawable();
    }

    /**
     * 代码跟父类代码相似，就是getCachedDrawable()替换成getDrawable（）,因为前者里面的图片是WeakReference，
     * 容易被gc回收，所以这里要避免这个问题
     */
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();

        if (fm != null) {
            fm.ascent = -rect.bottom;
            fm.descent = 0;

            fm.top = fm.ascent;
            fm.bottom = 0;
        }

        return rect.right;
    }

    /**
     * 代码跟父类代码相似，就是getCachedDrawable()替换成getDrawable（）,因为前者里面的图片是WeakReference，
     * 容易被gc回收，所以这里要避免这个问题
     */
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();

        int transY = bottom - b.getBounds().bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }

        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}