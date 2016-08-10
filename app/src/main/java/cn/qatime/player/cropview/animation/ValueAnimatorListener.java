package cn.qatime.player.cropview.animation;

/**
 * @author luntify
 * @date 2016/8/10 20:10
 * @Description
 */
public interface ValueAnimatorListener {
    void onAnimationStarted();

    void onAnimationUpdated(float scale);

    void onAnimationFinished();
}
