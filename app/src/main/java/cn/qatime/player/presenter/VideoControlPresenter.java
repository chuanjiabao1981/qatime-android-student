package cn.qatime.player.presenter;

import android.content.pm.ActivityInfo;

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
    public void playOrPause() {

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
    public void fullScreen() {
        vai.setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}
