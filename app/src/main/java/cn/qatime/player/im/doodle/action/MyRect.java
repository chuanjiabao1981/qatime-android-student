package cn.qatime.player.im.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 方框
 * <p/>
 */
public class MyRect extends Action {
    public MyRect(Float x, Float y, Integer color, Integer size) {
        super(x, y, color, size);
    }

    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawRect(startX, startY, stopX, stopY, paint);
    }

    public void onMove(float mx, float my) {
        stopX = mx;
        stopY = my;
    }
}
