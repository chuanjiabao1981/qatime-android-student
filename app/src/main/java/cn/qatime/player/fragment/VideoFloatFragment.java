package cn.qatime.player.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import cn.qatime.player.R;
import libraryextra.utils.StringUtils;


/**
 * @author lungtify
 * @Time 2016/11/2 15:35
 * @Describe 播放器控制框
 */
public class VideoFloatFragment extends Fragment implements View.OnClickListener {
    private boolean showState = true;//是否是显示状态
    private boolean isDanmuOn = true;//弹幕默认开启
    private boolean isSubBig = true;//副窗口默认大
    private boolean ismain = true;//video1 是否在主显示view上
    private boolean isSubOpen = true;//副窗口开关
    private boolean isMute = false;//被禁言
    private boolean isPlaying = false;//正在播放


    private int orientation = Configuration.ORIENTATION_PORTRAIT;

    private Activity act;

    private final int sDefaultVanishTime = 3000;

    private View mainControl;
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
    private TextView subSwitch;

    private Handler hd = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            vanish();
        }
    };
    private CallBack callback;
    private ImageView danmuSwitch;
    private View exit;


    private void assignViews(View view) {
        exit = view.findViewById(R.id.player_exit);
        mainControl = view.findViewById(R.id.main_control);
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
        danmuSwitch = (ImageView) view.findViewById(R.id.danmu_switch);
        viewChange = (ImageView) view.findViewById(R.id.view_change);
        ivSwitch = (ImageView) view.findViewById(R.id.iv_switch);
        zoom = (ImageView) view.findViewById(R.id.zoom);
        subSwitch = (TextView) view.findViewById(R.id.sub_switch);
        startVanishTimer();
    }

    private void registerListener() {
        exit.setOnClickListener(this);
        mainControl.setOnClickListener(this);
        play.setOnClickListener(this);
        refresh.setOnClickListener(this);
        danmuSwitch.setOnClickListener(this);
        viewChange.setOnClickListener(this);
        ivSwitch.setOnClickListener(this);
        zoom.setOnClickListener(this);
        subSwitch.setOnClickListener(this);
        comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    hd.removeCallbacks(runnable);
                }
            }
        });
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isMute) {
                    Toast.makeText(act, R.string.team_send_message_not_allow, Toast.LENGTH_SHORT).show();
                    comment.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        commit.setOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_media_controller, null, false);
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
                assert callback != null;
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
                if (isPlaying) {
                    callback.pause();
                } else {
                    callback.play();
                }
                break;
            case R.id.refresh://刷新按钮
                if (callback != null) {
                    callback.refresh();
                }
                break;
            case R.id.danmu_switch://弹幕开关
                if (isDanmuOn) {
                    isDanmuOn = false;
                    danmuSwitch.setImageResource(R.mipmap.danmu_off);
                    assert callback != null;
                    callback.shutDanmaku();
                } else {
                    isDanmuOn = true;
                    danmuSwitch.setImageResource(R.mipmap.danmu_on);
                    assert callback != null;
                    callback.showDanmaku();
                }
                break;
            case R.id.view_change://改变大小布局
                changeBigOrSmall();
                break;
            case R.id.iv_switch://切换视频
                if (ismain) {
                    ismain = false;
                    if (isSubBig) {
                        callback.changeMain2Sub();
                    } else {
                        callback.changeMain2Floating();
                    }
                } else {
                    ismain = true;
                    if (isSubBig) {
                        callback.changeSub2Main();
                    } else {
                        callback.changeFloating2Main();
                    }
                }
                break;
            case R.id.zoom:
                if (callback != null) {
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        orientation = Configuration.ORIENTATION_LANDSCAPE;
                        callback.fullScreen();
                        zoom.setVisibility(View.GONE);
                        danmuSwitch.setEnabled(true);//当横屏时   弹幕开关可用
                        //横平时 评论框出现
                        commentLayout.setVisibility(View.VISIBLE);
                        viewChange.setVisibility(View.GONE);
                        toolbarLayout.setVisibility(View.VISIBLE);
                        viewCount.setVisibility(View.GONE);
                    }
                }
                break;
            case R.id.sub_switch://副窗口开关
                if (callback == null) return;
                if (isSubOpen) {
                    isSubOpen = false;
                    callback.changeSubOpen(false);
                    subSwitch.setText("开");
                    viewChange.setVisibility(View.GONE);
                    ivSwitch.setVisibility(View.GONE);
                } else {
                    isSubOpen = true;
                    callback.changeSubOpen(true);
                    subSwitch.setText("关");
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        viewChange.setVisibility(View.GONE);
                    } else {
                        viewChange.setVisibility(View.VISIBLE);
                    }
                    ivSwitch.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.commit://发送
                if (callback == null) return;
                if (StringUtils.isNullOrBlanK(comment.getText().toString().trim())) {
                    return;
                }
//                if (isMute) {
//                    Toast.makeText(act, R.string.team_send_message_not_allow, Toast.LENGTH_SHORT).show();
//                    comment.setText("");
//                    return;
//                }
                callback.sendMessage(comment.getText().toString().trim());
                comment.setText("");
                break;
        }
    }

    /**
     * 当前屏幕横竖屏状态
     */
    public void setPortrait() {
        this.orientation = Configuration.ORIENTATION_PORTRAIT;
        if (zoom != null) {
            zoom.setVisibility(View.VISIBLE);
        }
        if (isSubBig) {
            danmuSwitch.setEnabled(true);
        } else {
            isDanmuOn = false;
            callback.shutDanmaku();
            danmuSwitch.setImageResource(R.mipmap.danmu_off);
            danmuSwitch.setEnabled(false);
        }
        commentLayout.setVisibility(View.GONE);
        viewChange.setVisibility(View.VISIBLE);
        toolbarLayout.setVisibility(View.GONE);
        viewCount.setVisibility(View.VISIBLE);
    }

    /**
     * 副窗口变大还是变小
     */
    private void changeBigOrSmall() {
        if (callback == null) {
            return;
        }
        if (isSubBig) {//小窗口出现
            isSubBig = false;
            callback.changeSubSmall();
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏时弹幕开关可用
                danmuSwitch.setEnabled(true);
            } else {//转为小窗口竖屏时  弹幕关闭,并不可用
                danmuSwitch.setEnabled(false);
                danmuSwitch.setImageResource(R.mipmap.danmu_off);
                isDanmuOn = false;
                callback.shutDanmaku();
            }
        } else {
            isSubBig = true;
            callback.changeSubBig();
            danmuSwitch.setEnabled(true);
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
     */
    private void vanish() {
        if (act == null) {
            return;
        }
        showState = false;
        float[] arrayOfFloat1 = new float[2];
        arrayOfFloat1[0] = 0.0F;
        arrayOfFloat1[1] = (-playToolbar.getHeight());
        ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(playToolbar, "translationY", arrayOfFloat1).setDuration(300L);

        float[] arrayOfFloat2 = new float[2];
        arrayOfFloat2[0] = 0.0F;
        arrayOfFloat2[1] = bottomLayout.getHeight();
        ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(bottomLayout, "translationY", arrayOfFloat2).setDuration(300L);

        float[] arrayOfFloat3 = new float[2];
        arrayOfFloat3[0] = 0.0F;
        arrayOfFloat3[1] = (subSwitch.getWidth());
        ObjectAnimator localObjectAnimator3 = ObjectAnimator.ofFloat(subSwitch, "translationX", arrayOfFloat3).setDuration(300L);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(localObjectAnimator1).with(localObjectAnimator2).with(localObjectAnimator3);
        animatorSet.start();
    }

    /**
     * 显示控制框
     */
    private void showUp() {
        showState = true;
        float[] arrayOfFloat1 = new float[2];
        arrayOfFloat1[0] = (-playToolbar.getHeight());
        arrayOfFloat1[1] = 0.0F;
        ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(playToolbar, "translationY", arrayOfFloat1).setDuration(300L);

        float[] arrayOfFloat2 = new float[2];
        arrayOfFloat2[0] = bottomLayout.getHeight();
        arrayOfFloat2[1] = 0.0F;
        ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(bottomLayout, "translationY", arrayOfFloat2).setDuration(300L);

        float[] arrayOfFloat3 = new float[2];
        arrayOfFloat3[0] = subSwitch.getWidth();
        arrayOfFloat3[1] = 0.0F;
        ObjectAnimator localObjectAnimator3 = ObjectAnimator.ofFloat(subSwitch, "translationX", arrayOfFloat3).setDuration(300L);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(localObjectAnimator1).with(localObjectAnimator2).with(localObjectAnimator3);
        animatorSet.start();
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
        hd.removeCallbacks(runnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setCallback(CallBack callback) {
        this.callback = callback;
    }

    public void setSubBig(boolean subBig) {
        isSubBig = subBig;
        if (!isSubBig) {//小窗口出现
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏时弹幕开关可用
                danmuSwitch.setEnabled(true);
            } else {//转为小窗口竖屏时  弹幕关闭,并不可用
                danmuSwitch.setEnabled(false);
                danmuSwitch.setImageResource(R.mipmap.danmu_off);
                isDanmuOn = false;
                callback.shutDanmaku();
            }
        } else {
            danmuSwitch.setEnabled(true);
        }
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
        if (isPlaying) {
            play.setImageResource(R.mipmap.nemediacontroller_play);
        } else {
            play.setImageResource(R.mipmap.nemediacontroller_pause);
        }
    }

    public void setMute(boolean mute) {
        isMute = mute;
        if (comment == null) {
            return;
        }
        if (isMute) {
            comment.setHint(R.string.have_muted);
        } else {
            comment.setHint("");
        }
    }

    public interface CallBack {
        void refresh();

        void changeSubBig();

        void changeSubSmall();

        void showDanmaku();

        void shutDanmaku();

        void fullScreen();

        void changeMain2Sub();

        void changeMain2Floating();

        void changeSub2Main();

        void changeFloating2Main();

        void exit();

        void changeSubOpen(boolean open);

        void sendMessage(String message);

        void pause();

        void play();
    }
}
