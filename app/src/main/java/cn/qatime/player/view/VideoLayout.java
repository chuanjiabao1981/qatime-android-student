package cn.qatime.player.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;

import libraryextra.utils.ScreenUtils;

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

        int screenW = ScreenUtils.getScreenWidth(getContext());
        int screenH = ScreenUtils.getScreenHeight(getContext());

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
                float resultX = getX() + dx;
                float resultY = getY() + dy;
                if (resultX < 0) {
                    resultX = 0;
                } else if (resultX >= screenW - getWidth()) {
                    resultX = screenW - getWidth();
                }
                if (resultY < 0) {
                    resultY = 0;
                } else if (resultY >= screenH - getHeight()) {
                    resultY = screenH - getHeight();
                }
                setX(resultX);
                setY(resultY);
                this.lastX = rawX;
                this.lastY = rawY;
                break;
        }
        return true;
    }
}
