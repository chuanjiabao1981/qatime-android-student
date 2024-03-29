package cn.qatime.player.im.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 实心方块
 * <p/>
 */
public class MyFillRect extends Action {
    public MyFillRect(Float x, Float y, Integer color, Integer size) {
        super(x, y, color, size);
    }

    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawRect(startX, startY, stopX, stopY, paint);
    }

    public void onMove(float mx, float my) {
        stopX = mx;
        stopY = my;
    }
}
