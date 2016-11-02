package cn.qatime.player.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import cn.qatime.player.R;


/**
 * @author lungtify
 * @Time 2016/11/2 15:35
 * @Describe 播放器控制框
 */
public class VideoFloatFragment extends Fragment {
    private boolean showState;//是否是显示状态
    private Timer timer;
    private Activity act;


    private RelativeLayout playToolbar;
    private LinearLayout toolbarLayout;
    private TextView videoName;
    private TextView definition;
    private TextView viewCount;
    private RadioGroup radiogroup;
    private RadioButton radio1;
    private RadioButton radio2;
    private LinearLayout bottomLayout;
    private ImageView play;
    private LinearLayout commentLayout;
    private ImageView refresh;
    private EditText comment;
    private Button commit;
    private ImageView viewChange;
    private ImageView ivSwitch;
    private ImageView zoom;
    private ImageView subSwitch;

    private void assignViews(View view) {
        playToolbar = (RelativeLayout) view.findViewById(R.id.play_toolbar);
        toolbarLayout = (LinearLayout) view.findViewById(R.id.toolbar_layout);
        videoName = (TextView) view.findViewById(R.id.video_name);
        definition = (TextView) view.findViewById(R.id.definition);
        viewCount = (TextView) view.findViewById(R.id.view_count);
        radiogroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        radio1 = (RadioButton) view.findViewById(R.id.radio1);
        radio2 = (RadioButton) view.findViewById(R.id.radio2);
        bottomLayout = (LinearLayout) view.findViewById(R.id.bottom_layout);
        play = (ImageView) view.findViewById(R.id.play);
        commentLayout = (LinearLayout) view.findViewById(R.id.comment_layout);
        refresh = (ImageView) view.findViewById(R.id.refresh);
        comment = (EditText) view.findViewById(R.id.comment);
        commit = (Button) view.findViewById(R.id.commit);
        viewChange = (ImageView) view.findViewById(R.id.view_change);
        ivSwitch = (ImageView) view.findViewById(R.id.iv_switch);
        zoom = (ImageView) view.findViewById(R.id.zoom);
        subSwitch = (ImageView) view.findViewById(R.id.sub_switch);
        startVanishTimer();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_media_controller, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assignViews(view);
    }

    public void startVanishTimer() {
        if (!showState) {
            return;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                vanish();
            }
        }, 5000);

    }

    /**
     * 消失
     */
    private void vanish() {
        if (act == null) {
            return;
        }
        showState = false;
        RelativeLayout localRelativeLayout1 = playToolbar;
        float[] arrayOfFloat1 = new float[2];
        arrayOfFloat1[0] = 0.0F;
        arrayOfFloat1[1] = (-playToolbar.getHeight());
        ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(localRelativeLayout1, "translationY", arrayOfFloat1).setDuration(300L);
        View localRelativeLayout2 = bottomLayout;
        float[] arrayOfFloat2 = new float[2];
        arrayOfFloat2[0] = 0.0F;
        arrayOfFloat2[1] = bottomLayout.getHeight();
        ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(localRelativeLayout2, "translationY", arrayOfFloat2).setDuration(300L);
        localObjectAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                stopVanishTimer();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(localObjectAnimator1).with(localObjectAnimator2);
    }

    private void stopVanishTimer() {
        if ((!showState) && (timer != null)) {
            timer.cancel();
            timer.purge();
            timer = null;
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
        stopVanishTimer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopVanishTimer();
    }
}
