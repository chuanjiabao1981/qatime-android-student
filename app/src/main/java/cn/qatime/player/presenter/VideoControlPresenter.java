package cn.qatime.player.presenter;

import android.content.pm.ActivityInfo;

import com.netease.nimlib.sdk.msg.model.IMMessage;

import cn.qatime.player.fragment.VideoFloatFragment;
import cn.qatime.player.utils.VideoActivityInterface;

/**
 * @author lungtify
 * @Time 2016/11/3 15:11
 * @Describe
 */
public class VideoControlPresenter implements VideoFloatFragment.CallBack {
    private final VideoActivityInterface vai;

    public VideoControlPresenter(VideoActivityInterface inter) {
        this.vai = inter;
    }

    @Override
    public void refresh() {
        assert vai != null;
        vai.refresh();
    }

    @Override
    public void changeSubBig() {
        assert vai != null;
        vai.changeSubBig();
    }

    @Override
    public void changeSubSmall() {
        assert vai != null;
        vai.changeSubSmall();
    }

    @Override
    public void showDanmaku() {
        assert vai != null;
        vai.showDanmaku();
    }

    @Override
    public void shutDanmaku() {
        assert vai != null;
        vai.shutDanmaku();
    }

    @Override
    public void zoom() {
        assert vai != null;
        vai.zoom();
    }

    @Override
    public void changeMain2Sub() {
        assert vai != null;
        vai.changeMain2Sub();
    }

    @Override
    public void changeMain2Floating() {
        assert vai != null;
        vai.changeMain2Floating();
    }

    @Override
    public void changeSub2Main() {
        assert vai != null;
        vai.changeSub2Main();
    }

    @Override
    public void changeFloating2Main() {
        assert vai != null;
        vai.changeFloating2Main();
    }

    @Override
    public void exit() {
        assert vai != null;
        vai.exit();
    }

    /**
     * 副窗口开关
     *
     * @param open true 开   false 关
     */
    @Override
    public void changeSubOpen(boolean open) {
        assert vai != null;
        vai.changeSubOpen(open);
    }

    @Override
    public void sendMessage(IMMessage message) {
        assert vai != null;
        vai.sendMessage(message);
    }

    @Override
    public void pause() {
        assert vai != null;
        vai.pause();
    }

    @Override
    public void play() {
        assert vai != null;
        vai.play();
    }

    @Override
    public boolean isPlaying() {
        return vai != null && vai.isPlaying();
    }

    @Override
    public boolean isPortrait() {
        return vai != null && vai.isPortrait();
    }
}
