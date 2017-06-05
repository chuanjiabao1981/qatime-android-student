package cn.qatime.player.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.FrameLayout;

import libraryextra.utils.ScreenUtils;

/**
 * @author lungtify
 * @Time 2016/11/1 15:26
 * @Describe 用于拖动view
 */
public class VideoFrameLayout extends FrameLayout {
    private float lastX;
    private float lastY;
    private float resultX;
    private float resultY;


    public VideoFrameLayout(Context context) {
        super(context);
    }

    public VideoFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
                resultX = getX() + dx;
                resultY = getY() + dy;
                setX(resultX);
                setY(resultY);
                this.lastX = rawX;
                this.lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                float[] floatX = new float[]{resultX, resultX};
                float[] floatY = new float[]{resultY, resultY};

                if (resultX < 0) {
                    floatX[0] = resultX;
                    floatX[1] = 0;
                } else if (resultX >= screenW - getWidth()) {
                    floatX[0] = resultX;
                    floatX[1] = screenW - getWidth();
                }
                if (resultY < 0) {
                    floatY[0] = resultY;
                    floatY[1] = 0;
                } else if (resultY >= screenH - getHeight()) {
                    floatY[0] = resultY;
                    floatY[1] = screenH - getHeight();
                }
                ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "translationX", floatX).setDuration(300L);
                ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "translationY", floatY).setDuration(300L);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(animatorX).with(animatorY);
                animatorSet.start();
                break;
        }
        return true;
    }

}
