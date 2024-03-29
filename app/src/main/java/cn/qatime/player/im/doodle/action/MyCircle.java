package cn.qatime.player.im.doodle.action;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 空心圆
 * <p/>
 */
public class MyCircle extends Action {
    private float radius;

    public MyCircle(Float x, Float y, Integer color, Integer size, float radius) {
        super(x, y, color, size);
        this.radius = radius;
    }

    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(size);
        canvas.drawCircle((startX + stopX) / 2, (startY + stopY) / 2, radius,
                paint);
    }

    public void onMove(float mx, float my) {
        stopX = mx;
        stopY = my;
        radius = (float) ((Math.sqrt((mx - startX) * (mx - startX)
                + (my - startY) * (my - startY))) / 2);
    }
}
