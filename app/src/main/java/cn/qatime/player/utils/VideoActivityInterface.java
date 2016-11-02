package cn.qatime.player.utils;

/**
 * @author lungtify
 * @Time 2016/11/2 16:25
 * @Describe
 */
public interface VideoActivityInterface {
    void changeSubViewBig();

    void changeSubViewSmall();

    void startVideo();

    void pauseVideo();

    void switchMainFloat();

    void setOrientation();

    void showDanmaku();

    void shutDanmaku();

    void switchMainSub();

    /**
     * 刷新按钮
     */
    void refresh();

}
