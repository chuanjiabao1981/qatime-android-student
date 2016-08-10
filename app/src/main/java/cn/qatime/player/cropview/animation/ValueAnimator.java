package cn.qatime.player.cropview.animation;

/**
 * @author luntify
 * @date 2016/8/10 20:10
 * @Description
 */
public interface ValueAnimator {
    void startAnimation(long duration);
    void cancelAnimation();
    boolean isAnimationStarted();
    void addAnimatorListener(ValueAnimatorListener animatorListener);
}
