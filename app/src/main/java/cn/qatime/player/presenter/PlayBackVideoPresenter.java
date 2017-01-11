package cn.qatime.player.presenter;

import cn.qatime.player.fragment.PlayBackFloatFragment;
import cn.qatime.player.utils.PlayBackVideoInterface;

/**
 * @author lungtify
 * @Time 2017/1/10 11:46
 * @Describe
 */
public class PlayBackVideoPresenter implements PlayBackFloatFragment.Callback {
    private final PlayBackVideoInterface vi;

    public PlayBackVideoPresenter(PlayBackVideoInterface playBackVideoInterface) {
        this.vi = playBackVideoInterface;
    }

    @Override
    public void fullScreen() {
        if (vi == null) return;
        vi.fullScreen();
    }

    @Override
    public void exit() {
        if (vi == null) return;
        vi.exit();
    }

    @Override
    public void playOrPause() {
        if (vi == null) return;
        vi.playOrPause();
    }
}
