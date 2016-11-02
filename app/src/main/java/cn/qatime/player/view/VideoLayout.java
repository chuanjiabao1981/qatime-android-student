package cn.qatime.player.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;

/**
 * @author lungtify
 * @Time 2016/11/1 15:26
 * @Describe 用于拖动view
 */
public class VideoLayout extends RelativeLayout {
    private float lastX;
    private float lastY;

    public VideoLayout(Context context) {
        super(context);
    }

    public VideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                this.lastX = rawX;
                this.lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = rawX - lastX;
                float dy = rawY - lastY;
                setX(getX() + dx);
                setY(getY() + dy);
                this.lastX = rawX;
                this.lastY = rawY;
                break;
        }
        return true;
    }
}
