package cn.qatime.player.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
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

import cn.qatime.player.R;

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
    private Handler hd = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            vanish(false);
        }
    };

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
        videoDefinition = (TextView) view.findViewById(R.id.video_definition);
        zoom = (ImageView) view.findViewById(R.id.zoom);
        list = (ListView) view.findViewById(R.id.list);

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

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                hd.removeCallbacks(runnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                startVanishTimer();
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
                if (callback==null)return;
                callback.playOrPause();
                break;
            case R.id.courses_list:
                hd.removeCallbacks(runnable);
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
        hd.removeCallbacks(runnable);
        hd.postDelayed(runnable, sDefaultVanishTime);
    }

    /**
     * 消失
     *
     * @param shouldWithList
     */
    private void vanish(boolean shouldWithList) {
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

    public interface Callback {
        void fullScreen();

        void exit();//返回键

        void playOrPause();
    }
}
