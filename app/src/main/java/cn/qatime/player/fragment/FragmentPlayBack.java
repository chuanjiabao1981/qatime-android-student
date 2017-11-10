package cn.qatime.player.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import cn.qatime.player.R;
import cn.qatime.player.activity.PlayBackActivity;
import cn.qatime.player.base.BaseFragment;
import libraryextra.utils.DateUtils;

/**
 * @author lungtify
 * @Time 2017/6/12 15:10
 * @Describe
 */

public class FragmentPlayBack extends BaseFragment implements View.OnClickListener {
    private View exit;
    private View mainControl;
    private RelativeLayout playToolbar;
    private LinearLayout bottomLayout;
    private ImageView play;
    private SeekBar seekBar;
    private ImageView zoom;
    private TextView time;
    private CallBack callback;

    private final int sDefaultVanishTime = 5000;
    private final int HIDE = 1001;
    private boolean mDragging;
    private final int PROGRESS = 1002;
    private boolean showState = true;//是否是显示状态

    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HIDE:
                    vanish();
                    break;
                case PROGRESS:
                    setProgress();
                    if (!mDragging && showState) {
                        msg = obtainMessage(PROGRESS);
                        sendMessageDelayed(msg, 1000);
                        setPlayOrPause(callback.isPlaying());
                    }
                    break;
            }
        }
    };
    private PlayBackActivity act;
    private Runnable lastRunnable;

    private void setProgress() {
        if (callback == null || mDragging)
            return;
        Logger.e("setProgress");
        long position = callback.getCurrentPosition();
        long duration = callback.getDuration();
        if (seekBar != null) {
            try {
                seekBar.setProgress(Integer.parseInt(String.valueOf(position)));
            } catch (Exception e) {
            }
        }
        if (time != null) {
            if (duration > 0)
                time.setText(DateUtils.stringForTime_ms(duration));
            else
                time.setText("--:--:--");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_back, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignViews(view);
        registerListener();
    }

    private void registerListener() {
        exit.setOnClickListener(this);
        mainControl.setOnClickListener(this);
        play.setOnClickListener(this);
        zoom.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                if (!fromUser || !callback.isPrepared()) return;

//                String time = stringForTime(newposition);

                Logger.e("progress" + progress + "*******durcation" + callback.getDuration());
                hd.removeCallbacks(lastRunnable);
                lastRunnable = new Runnable() {
                    @Override
                    public void run() {
                        callback.seekTo(progress);
                    }
                };
                hd.postDelayed(lastRunnable, 200);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (callback.isPrepared()) {
                    mDragging = true;
                    hd.removeMessages(HIDE);
                    hd.removeMessages(PROGRESS);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (callback.isPrepared()) {
                    callback.seekTo((callback.getDuration() * seekBar.getProgress()) / 1000);
                    mDragging = false;
                    startVanishTimer();
                    hd.removeMessages(PROGRESS);
                    hd.sendEmptyMessageDelayed(PROGRESS, 1000);
                } else {
                    seekBar.setProgress(0);
                }
            }
        });
    }

    private void assignViews(View view) {
        exit = view.findViewById(R.id.player_exit);
        mainControl = view.findViewById(R.id.main_control);
        playToolbar = (RelativeLayout) view.findViewById(R.id.play_toolbar);
        bottomLayout = (LinearLayout) view.findViewById(R.id.bottom_layout);
        play = (ImageView) view.findViewById(R.id.play);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        zoom = (ImageView) view.findViewById(R.id.zoom);
        time = (TextView) view.findViewById(R.id.time);
        startVanishTimer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_exit://返回键
                if (callback == null) {
                    return;
                }
                callback.exit();
                break;
            case R.id.main_control:
                if (showState) {
                    new Runnable() {
                        @Override
                        public void run() {
                            vanish();
                        }
                    }.run();
                } else {
                    new Runnable() {
                        @Override
                        public void run() {
                            showUp();
                            startVanishTimer();
                        }
                    }.run();
                }
                break;
            case R.id.play:
                if (callback == null) {
                    return;
                }
                if (callback.isPlaying()) {
                    callback.pause();
                    play.setImageResource(R.mipmap.nemediacontroller_pause);
                } else {
                    callback.play();
                    play.setImageResource(R.mipmap.nemediacontroller_play);
                }
                break;
            case R.id.zoom:
                if (callback == null) {
                    return;
                }
                if (callback.isPortrait()) {
                    callback.zoomBig();
                    zoom.setImageResource(R.mipmap.narrow);
                } else {
                    callback.zoomSmall();
                }
                break;
        }
    }

    public void setBuffering(int percent, long duration) {
        seekBar.setMax(Integer.parseInt(String.valueOf(duration)));
//        Logger.e(duration + "****buffering" + Integer.parseInt(String.valueOf(duration)) + "**percent" + Integer.parseInt(String.valueOf(percent * duration / 100)));
        seekBar.setSecondaryProgress(Integer.parseInt(String.valueOf(percent * duration / 100)));
        time.setText(DateUtils.stringForTime_ms(duration));
    }

    public interface CallBack {
        void exit();

        boolean isPlaying();

        void pause();

        void play();

        boolean isPrepared();

        long getCurrentPosition();

        long getDuration();

        void seekTo(long progress);

        boolean isPortrait();

        void zoomSmall();

        void zoomBig();
    }

    private void vanish() {
        if (act == null) {
            return;
        }
        showState = false;
        float[] arrayOfFloat1 = new float[2];
        arrayOfFloat1[0] = 0.0F;
        arrayOfFloat1[1] = -playToolbar.getHeight();
        ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(playToolbar, "translationY", arrayOfFloat1).setDuration(300L);

        float[] arrayOfFloat2 = new float[2];
        arrayOfFloat2[0] = 0.0F;
        arrayOfFloat2[1] = bottomLayout.getHeight();
        ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(bottomLayout, "translationY", arrayOfFloat2).setDuration(300L);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(localObjectAnimator1).with(localObjectAnimator2);
        animatorSet.start();
    }

    /**
     * 显示控制框
     */
    private void showUp() {
        showState = true;
        float[] arrayOfFloat1 = new float[2];
        arrayOfFloat1[0] = -playToolbar.getHeight();
        arrayOfFloat1[1] = 0.0F;
        ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(playToolbar, "translationY", arrayOfFloat1).setDuration(300L);

        float[] arrayOfFloat2 = new float[2];
        arrayOfFloat2[0] = bottomLayout.getHeight();
        arrayOfFloat2[1] = 0.0F;
        ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(bottomLayout, "translationY", arrayOfFloat2).setDuration(300L);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(localObjectAnimator1).with(localObjectAnimator2);
        animatorSet.start();
        if (callback.isPlaying()) {
            hd.sendEmptyMessageDelayed(PROGRESS, 1000);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        act = (PlayBackActivity) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            act = (PlayBackActivity) context;
        }
    }

    public void startVanishTimer() {
        if (!showState) {
            return;
        }
        hd.removeMessages(HIDE);
        hd.sendEmptyMessageDelayed(HIDE, sDefaultVanishTime);
    }

    /**
     * @param isplaying 正在播放
     */
    public void setPlayOrPause(boolean isplaying) {
        if (isplaying) {
            play.setImageResource(R.mipmap.nemediacontroller_play);
        } else {
            play.setImageResource(R.mipmap.nemediacontroller_pause);
        }
    }

    /**
     * 横竖屏改编后改变zoom图标
     */
    public void refreshZoom() {
        if (callback == null) {
            return;
        }
        if (callback.isPortrait()) {
            zoom.setImageResource(R.mipmap.enlarge);
        } else {
            zoom.setImageResource(R.mipmap.narrow);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hd.removeMessages(HIDE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hd.removeMessages(HIDE);
        hd.removeMessages(PROGRESS);
    }

    public void setCallback(CallBack callback) {
        this.callback = callback;
    }
}
