package cn.qatime.player.bean;

/**
 * @author lungtify
 * @Time 2016/12/1 14:36
 * @Describe 直播查询状态
 */

public enum VideoState {
    /**
     * 未直播  UNPLAY
     * 直播中  PLAYING
     * 已关闭  CLOSED
     */
    UNPLAY(0), PLAYING(1), CLOSED(2);

    VideoState(int i) {
    }
}
