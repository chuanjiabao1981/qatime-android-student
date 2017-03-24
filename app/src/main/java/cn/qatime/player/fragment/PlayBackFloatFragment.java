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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.qatime.player.R;
import cn.qatime.player.view.NEVideoView;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.StringUtils;

/**
 * @author lungtify
 * @Time 2017/1/10 10:28
 * @Describe
 */

public class PlayBackFloatFragment extends Fragment implements View.OnClickListener {
    private boolean showState = true;//是否是显示状态

    private final Callback callback;
    private RelativeLayout playToolbar;
    private LinearLayout bottomLayout;
    private ImageView play;
    private SeekBar seekbar;
    private TextView time;
    private TextView videoDefinition;
    private ImageView playExit;

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
                    vanish();
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
    private long mDuration = 0;
    private Runnable lastRunnable;
    //    private TextView currentTime;
    private View definition;
    private TextView sd;
    private TextView hds;
    private TextView uhd;
    private TextView name;
    private String nameValue;

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
//        if (currentTime != null)
//            currentTime.setText(stringForTime(position));

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
        name = (TextView) view.findViewById(R.id.name);
        playToolbar = (RelativeLayout) view.findViewById(R.id.play_toolbar);
        bottomLayout = (LinearLayout) view.findViewById(R.id.bottom_layout);
        play = (ImageView) view.findViewById(R.id.play);
        seekbar = (SeekBar) view.findViewById(R.id.seekbar);
        time = (TextView) view.findViewById(R.id.time);
//        currentTime = (TextView) view.findViewById(R.id.current_time);
        videoDefinition = (TextView) view.findViewById(R.id.video_definition);
        definition = view.findViewById(R.id.definition);
        sd = (TextView) view.findViewById(R.id.sd);
        hds = (TextView) view.findViewById(R.id.hd);
        uhd = (TextView) view.findViewById(R.id.uhd);

        seekbar.setMax(1000);

        hd.sendEmptyMessageDelayed(HIDE, 1500);
        if (!StringUtils.isNullOrBlanK(nameValue)) {
            name.setText(nameValue);
        }
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
        videoDefinition.setOnClickListener(this);
        sd.setOnClickListener(this);
        hds.setOnClickListener(this);
        uhd.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;

                final long newposition = (mDuration * progress) / 1000;
//                String time = stringForTime(newposition);
                hd.removeCallbacks(lastRunnable);
                lastRunnable = new Runnable() {
                    @Override
                    public void run() {
                        mPlayer.seekTo(newposition);
                    }
                };
                hd.postDelayed(lastRunnable, 200);
//                PlayBackFloatFragment.this.currentTime.setText(time);
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
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.video_definition:
                if (definition.getVisibility() == View.GONE) {
                    definition.setVisibility(View.VISIBLE);
                    hd.removeMessages(HIDE);
                } else {
                    startVanishTimer();
                    definition.setVisibility(View.GONE);
                }
                break;
            case R.id.sd:
                videoDefinition.setText(getResources().getString(R.string.sd));
                sd.setTextColor(0xffff5842);
                hds.setTextColor(0xffffffff);
                uhd.setTextColor(0xffffffff);
                definition.setVisibility(View.GONE);
                startVanishTimer();

                break;
            case R.id.hd:
                videoDefinition.setText(getResources().getString(R.string.hd));
                sd.setTextColor(0xffffffff);
                hds.setTextColor(0xffff5842);
                uhd.setTextColor(0xffffffff);
                definition.setVisibility(View.GONE);
                startVanishTimer();

                break;
            case R.id.uhd:
                videoDefinition.setText(getResources().getString(R.string.uhd));
                sd.setTextColor(0xffffffff);
                hds.setTextColor(0xffffffff);
                uhd.setTextColor(0xffff5842);
                definition.setVisibility(View.GONE);
                startVanishTimer();

                break;
            case R.id.play_exit:
                if (callback == null) return;
                callback.exit();
                break;
            case R.id.playback_control:
                if (showState) {
                    new Runnable() {
                        @Override
                        public void run() {
                            if (definition.getVisibility() == View.VISIBLE) {
                                definition.setVisibility(View.GONE);
                            }
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
                if (callback == null) return;
                callback.playOrPause();
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
     */
    private void vanish() {
        if (definition.getVisibility() == View.VISIBLE) {
            definition.setVisibility(View.GONE);
        }
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

    public void setName(String value) {
        this.nameValue = value;
        if (name != null) {
            name.setText(value);
        }
    }

    public interface Callback {
        void exit();//返回键

        void playOrPause();
    }
}
