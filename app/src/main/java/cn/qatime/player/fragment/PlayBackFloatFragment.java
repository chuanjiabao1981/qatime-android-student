package cn.qatime.player.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.Locale;

import cn.qatime.player.R;
import cn.qatime.player.view.NEVideoView;

/**
 * @author lungtify
 * @Time 2017/1/10 10:28
 * @Describe
 */

public class PlayBackFloatFragment extends Fragment implements View.OnClickListener {
    private boolean showState = true;//是否是显示状态

    private final Callback callback;
    private RelativeLayout playToolbar;
    private ImageView coursesList;
    private LinearLayout bottomLayout;
    private ImageView play;
    private SeekBar seekbar;
    private TextView time;
    private TextView videoDefinition;
    private ImageView zoom;
    private ImageView playExit;
    private ListView list;

    private final int sDefaultVanishTime = 5000;
    private final int HIDE = 1001;
    private final int PROGRESS = 1002;
    private boolean mDragging;

    @SuppressLint("HandlerLeak")
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HIDE:
                    vanish(false);
                    break;
                case PROGRESS:
                    long pos = setProgress();
                    if (!mDragging && showState) {
                        msg = obtainMessage(PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                        updatePausePlay();
                    }
                    break;
            }
        }
    };
    private NEVideoView mPlayer;
    private int mDuration = 0;
    private Runnable lastRunnable;
    private TextView currentTime;

    private long setProgress() {
        if (mPlayer == null || mDragging)
            return 0;

        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (seekbar != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                seekbar.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            seekbar.setSecondaryProgress(percent * 10);
        }
        mDuration = duration;
        if (time != null && duration > 0)
            time.setText(stringForTime(duration));
        else
            time.setText("--:--:--");
        if (currentTime != null)
            currentTime.setText(stringForTime(position));

        return position;
    }

    private static String stringForTime(long position) {
        int totalSeconds = (int) ((position / 1000.0) + 0.5);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds).toString();
    }

    public PlayBackFloatFragment(Callback callback) {
        this.callback = callback;
    }

    private void assignViews(View view) {
        playExit = (ImageView) view.findViewById(R.id.play_exit);
        playToolbar = (RelativeLayout) view.findViewById(R.id.play_toolbar);
        coursesList = (ImageView) view.findViewById(R.id.courses_list);
        bottomLayout = (LinearLayout) view.findViewById(R.id.bottom_layout);
        play = (ImageView) view.findViewById(R.id.play);
        seekbar = (SeekBar) view.findViewById(R.id.seekbar);
        time = (TextView) view.findViewById(R.id.time);
        currentTime = (TextView) view.findViewById(R.id.current_time);
        videoDefinition = (TextView) view.findViewById(R.id.video_definition);
        zoom = (ImageView) view.findViewById(R.id.zoom);
        list = (ListView) view.findViewById(R.id.list);
        seekbar.setMax(1000);

        hd.sendEmptyMessageDelayed(HIDE, 1500);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playback_controller, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignViews(view);
        initListener(view);

    }

    private void initListener(View view) {
        view.setOnClickListener(this);
        playExit.setOnClickListener(this);
        play.setOnClickListener(this);
        coursesList.setOnClickListener(this);
        zoom.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;

                final long newposition = mDuration * progress / 1000;
                String time = stringForTime(newposition);
                hd.removeCallbacks(lastRunnable);
                lastRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mPlayer.seekTo(newposition);
                    }
                };
                hd.postDelayed(lastRunnable, 200);
                PlayBackFloatFragment.this.currentTime.setText(time);
                Logger.e(mDuration + "**" + progress + "**" + newposition + "***" + (mDuration * progress / 1000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mDragging = true;
                hd.removeMessages(HIDE);
                hd.removeMessages(PROGRESS);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPlayer.seekTo((mDuration * seekBar.getProgress()) / 1000);
                mDragging = false;
                startVanishTimer();
                hd.removeMessages(PROGRESS);
                hd.sendEmptyMessageDelayed(PROGRESS, 1000);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_exit:
                if (callback == null) return;
                callback.exit();
                break;
            case R.id.playback_control:
                if (list.getVisibility() == View.VISIBLE) {
                    showUp(true);
                    startVanishTimer();
                } else {
                    if (showState) {
                        new Runnable() {
                            @Override
                            public void run() {
                                vanish(false);
                            }
                        }.run();
                    } else {
                        new Runnable() {
                            @Override
                            public void run() {
                                showUp(false);
                                startVanishTimer();
                            }
                        }.run();
                    }
                }
                break;
            case R.id.play:
                if (callback == null) return;
                callback.playOrPause();
                break;
            case R.id.courses_list:
                hd.removeMessages(HIDE);
                vanish(true);
                break;
            case R.id.zoom:
                if (callback == null) return;
                callback.fullScreen();
                break;
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
     * 消失
     *
     * @param shouldWithList
     */
    private void vanish(boolean shouldWithList) {
        hd.removeMessages(PROGRESS);
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
        if (shouldWithList) {
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    list.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    /**
     * 显示控制框
     *
     * @param shouldWithList 是否先隐藏list
     */
    private void showUp(boolean shouldWithList) {
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
        if (shouldWithList) {
            list.setVisibility(View.GONE);
        }
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                hd.sendEmptyMessage(PROGRESS);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void updatePausePlay() {
        setPlayOrPause(mPlayer != null && mPlayer.isPlaying());
    }

    public void setPortrait(boolean rotate) {
        if (rotate) {//竖屏
            coursesList.setVisibility(View.GONE);
            zoom.setVisibility(View.VISIBLE);
            videoDefinition.setVisibility(View.GONE);
        } else {
            coursesList.setVisibility(View.VISIBLE);
            zoom.setVisibility(View.GONE);
            videoDefinition.setVisibility(View.VISIBLE);
        }
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

    public void setMediaPlayer(NEVideoView mediaPlayer) {
        this.mPlayer = mediaPlayer;
    }

    public interface Callback {
        void fullScreen();

        void exit();//返回键

        void playOrPause();
    }
}
