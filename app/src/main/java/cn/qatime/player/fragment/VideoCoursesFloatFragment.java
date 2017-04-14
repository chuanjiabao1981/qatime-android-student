package cn.qatime.player.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
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

/**
 * @author lungtify
 * @Time 2016/11/2 15:35
 * @Describe 播放器控制框
 */
public class VideoCoursesFloatFragment extends Fragment implements View.OnClickListener {
    private boolean showState = true;//是否是显示状态

    private Activity act;

    private final int sDefaultVanishTime = 5000;
    private final int HIDE = 1001;
    private boolean mDragging;
    private final int PROGRESS = 1002;

    private View mainControl;
    private RelativeLayout playToolbar;
    //    private TextView videoName;
    //    private TextView viewCount;
    private ImageView play;

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
    private Runnable lastRunnable;
    private ImageView zoom;

    private void setProgress() {
        if (callback == null || mDragging)
            return;
        Logger.e("setProgress");
        long position = callback.getCurrentPosition();
        long duration = callback.getDuration();
        if (seekBar != null) {
            seekBar.setProgress(Integer.parseInt(String.valueOf(position)));
        }
        if (time != null && duration > 0)
            time.setText(stringForTime(duration));
        else
            time.setText("--:--:--");

    }

    private CallBack callback;
    private View exit;
    private LinearLayout bottomLayout;
    private TextView videoName;
    private SeekBar seekBar;
    private TextView time;
    private TextView definition;
    private ListView list;
    private View listSwitch;


    public VideoCoursesFloatFragment() {
    }


    private void assignViews(View view) {
        exit = view.findViewById(R.id.player_exit);
        mainControl = view.findViewById(R.id.main_control);
        playToolbar = (RelativeLayout) view.findViewById(R.id.play_toolbar);
        videoName = (TextView) view.findViewById(R.id.video_name);
        listSwitch = view.findViewById(R.id.list_switch);
        bottomLayout = (LinearLayout) view.findViewById(R.id.bottom_layout);
        play = (ImageView) view.findViewById(R.id.play);
        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        zoom = (ImageView) view.findViewById(R.id.zoom);
        time = (TextView) view.findViewById(R.id.time);
        definition = (TextView) view.findViewById(R.id.definition);
        list = (ListView) view.findViewById(R.id.list);

        startVanishTimer();
    }

    private void registerListener() {
        exit.setOnClickListener(this);
        mainControl.setOnClickListener(this);
        play.setOnClickListener(this);
        zoom.setOnClickListener(this);
        listSwitch.setOnClickListener(this);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_courses_media_controller, null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignViews(view);
        registerListener();
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
            case R.id.list_switch:
                list.setVisibility(View.VISIBLE);
                break;
            case R.id.zoom:
                callback.zoom();
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
        if (act == null) {
            return;
        }
        if (list.getVisibility() == View.VISIBLE) {
            list.setVisibility(View.GONE);
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
        act = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            act = (Activity) context;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setCallback(CallBack callback) {
        this.callback = callback;
    }

    public void setNameAndCount(String name, int size) {
        videoName.setText(name);
    }

    public void setBuffering(int percent, long duration) {
        seekBar.setMax(Integer.parseInt(String.valueOf(duration)));
//        Logger.e(duration + "****buffering" + Integer.parseInt(String.valueOf(duration)) + "**percent" + Integer.parseInt(String.valueOf(percent * duration / 100)));
        seekBar.setSecondaryProgress(Integer.parseInt(String.valueOf(percent * duration / 100)));
        time.setText(stringForTime(duration));
    }

    private static String stringForTime(long position) {
        int totalSeconds = (int) ((position / 1000.0) + 0.5);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return String.format(Locale.CHINESE, "%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void setPortrait(boolean isPortrait) {
        if (isPortrait) {
            zoom.setVisibility(View.VISIBLE);
            listSwitch.setVisibility(View.GONE);
            definition.setVisibility(View.GONE);
            if (list.getVisibility() == View.VISIBLE) {
                list.setVisibility(View.GONE);
            }
        } else {
            zoom.setVisibility(View.GONE);
            listSwitch.setVisibility(View.VISIBLE);
            definition.setVisibility(View.VISIBLE);

        }
    }

    public interface CallBack {
        void exit();

        void pause();

        void play();

        boolean isPlaying();

        boolean isPortrait();

        long getCurrentPosition();

        long getDuration();

        void seekTo(long position);

        void zoom();

        boolean isPrepared();
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
}