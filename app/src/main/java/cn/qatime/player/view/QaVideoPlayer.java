package cn.qatime.player.view;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;



/**
 * 对播放器进行封装
 */
public class QaVideoPlayer extends FrameLayout {
    private int flag;// 用户原始是否可旋转，退出是需将用户设置还原
    private NEVideoView videoView;


    public QaVideoPlayer(Context context) {
        super(context);
        init();
    }

    public QaVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QaVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        flag = Settings.System.getInt(((Activity) QaVideoPlayer.this.getContext()).getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        videoView = new NEVideoView(this.getContext());
        ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        videoView.setLayoutParams(params);
        this.addView(videoView);
//        controller = View.inflate(this.getContext(), R.layout.media_controller_portrait, null);
    }
}
