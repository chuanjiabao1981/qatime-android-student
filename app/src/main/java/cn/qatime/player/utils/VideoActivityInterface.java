package cn.qatime.player.utils;

import com.netease.nimlib.sdk.msg.model.IMMessage;

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

    void zoom();

    void changeSubSmall();

    void changeSubBig();

    //切换视频
    void changeFloating2Main();

    void changeMain2Sub();

    void changeMain2Floating();

    void changeSub2Main();

    /***********************************************************/

    void pause();

    void exit();//返回

    void changeSubOpen(boolean open);//副窗口开关

    void sendMessage(IMMessage message);//发送消息

    void play();

    boolean isPlaying();

    boolean isPortrait();
}
