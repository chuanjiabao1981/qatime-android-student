package cn.qatime.player.utils;

/**
 * @author lungtify
 * @Time 2016/11/2 16:25
 * @Describe
 */
public interface VideoActivityInterface {
    void showDanmaku();

    void shutDanmaku();

    /**
     * 刷新按钮
     */
    void refresh();

    void setOrientation(int orientation);

    void changeSubSmall();

    void changeSubBig();
}
