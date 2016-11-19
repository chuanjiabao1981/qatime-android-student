package cn.qatime.player.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
    private float resultX;
    private float resultY;
    private final long DOUBLE_CLICK_TIME = 350;//双击等待时间
    private long firstClick;
    private int count;
    private long lastClick;


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
                if (firstClick != 0 && System.currentTimeMillis() - firstClick > DOUBLE_CLICK_TIME) {
                    count = 0;
                }
                count++;
                if (count == 1) {
                    firstClick = System.currentTimeMillis();
                } else if (count == 2) {
                    lastClick = System.currentTimeMillis();
                    // 两次点击小于300ms 也就是连续点击
                    if (lastClick - firstClick < DOUBLE_CLICK_TIME) {// 判断是否是执行了双击事件
                        if (mOnDoubleClickListener != null) {
                            mOnDoubleClickListener.onDoubleClick();
                        }
                    }
                }
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


    public interface OnDoubleClickListener {
        void onDoubleClick();
    }

    private OnDoubleClickListener mOnDoubleClickListener;

    public void setOnDoubleClickListener(OnDoubleClickListener l) {
        mOnDoubleClickListener = l;
    }

}
