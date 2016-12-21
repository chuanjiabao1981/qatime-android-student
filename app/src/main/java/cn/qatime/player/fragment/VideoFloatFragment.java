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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.p.d;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;
import com.orhanobut.logger.Logger;

import cn.qatime.player.R;
import cn.qatime.player.activity.NEVideoPlayerActivity;
import libraryextra.utils.KeyBoardUtils;
import libraryextra.utils.StringUtils;


/**
 * @author lungtify
 * @Time 2016/11/2 15:35
 * @Describe 播放器控制框
 */
public class VideoFloatFragment extends Fragment implements View.OnClickListener {
    private final String sessionId;
    private boolean showState = true;//是否是显示状态
    private boolean isDanmuOn = true;//弹幕默认开启
    private boolean isSubBig = true;//副窗口默认大
    private boolean ismain = true;//video1 是否在主显示view上
    private boolean isSubOpen = true;//副窗口开关
    private boolean isMute = false;//被禁言
    private boolean isPlaying = false;//正在播放


    private int orientation = Configuration.ORIENTATION_PORTRAIT;

    private Activity act;

    private final int sDefaultVanishTime = 5000;

    private View mainControl;
    private RelativeLayout playToolbar;
    private TextView videoName;
    //    private TextView viewCount;
    private ImageView play;
    private LinearLayout commentLayout;
    private ImageView refresh;
    private EditText comment;
    private Button commit;
    private ImageView viewChange;
    private ImageView ivSwitch;
    private ImageView zoom;
    private ImageView subSwitch;

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
    private LinearLayout bottomLayout;
    private Team team;


    public VideoFloatFragment(String sessionId) {
        this.sessionId = sessionId;
    }


    private void assignViews(View view) {
        exit = view.findViewById(R.id.player_exit);
        mainControl = view.findViewById(R.id.main_control);
        playToolbar = (RelativeLayout) view.findViewById(R.id.play_toolbar);
        videoName = (TextView) view.findViewById(R.id.video_name);
//        viewCount = (TextView) view.findViewById(R.id.view_count);
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
        subSwitch = (ImageView) view.findViewById(R.id.sub_switch);
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
                            KeyBoardUtils.closeKeybord(act);
                            comment.clearFocus();
                            comment.setText("");
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
                    isPlaying = false;
                    callback.pause();
                    play.setImageResource(R.mipmap.nemediacontroller_pause);
                } else {
                    isPlaying = true;
                    callback.play();
                    play.setImageResource(R.mipmap.nemediacontroller_play);
                }
                break;
            case R.id.refresh://刷新按钮
                if (callback != null) {
                    callback.refresh();
                    ObjectAnimator animator = ObjectAnimator.ofFloat(refresh, "rotation", 0F, 360F).setDuration(300L);
                    animator.setRepeatCount(2);
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.start();
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
                switchVideo();
                break;
            case R.id.zoom:
                if (callback != null) {
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        orientation = Configuration.ORIENTATION_LANDSCAPE;
                        KeyBoardUtils.closeKeybord(act);
                        callback.fullScreen();
                        zoom.setVisibility(View.GONE);
                        danmuSwitch.setVisibility(View.VISIBLE); //当横屏时   弹幕开关可用
                        //横平时 评论框出现
                        commentLayout.setVisibility(View.VISIBLE);
                        viewChange.setVisibility(View.GONE);
                        videoName.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.sub_switch://副窗口开关
                if (callback == null) return;
                if (isSubOpen) {
                    isSubOpen = false;
                    Toast.makeText(act, getResources().getString(R.string.live_side_close), Toast.LENGTH_SHORT).show();
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                        if (isDanmuOn) {
                            isDanmuOn = false;
                            danmuSwitch.setImageResource(R.mipmap.danmu_off);
                        }
                        danmuSwitch.setVisibility(View.GONE);
                    }

                    callback.changeSubOpen(false);
                    subSwitch.setImageResource(R.mipmap.float_subvideo_open);
                    viewChange.setVisibility(View.GONE);
                    ivSwitch.setVisibility(View.GONE);
                } else {
                    isSubOpen = true;
                    Toast.makeText(act, getResources().getString(R.string.live_side_open), Toast.LENGTH_SHORT).show();
                    callback.changeSubOpen(true);
                    subSwitch.setImageResource(R.mipmap.float_subvideo_close);
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        viewChange.setVisibility(View.GONE);
                    } else {
                        danmuSwitch.setVisibility(View.VISIBLE);
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
                if (!isAllowSendMessage()) {
                    return;
                }

                if (isMute) {
                    Toast.makeText(act, R.string.team_send_message_not_allow, Toast.LENGTH_SHORT).show();
                    comment.setText("");
                    return;
                }
                // 创建文本消息
                IMMessage message = MessageBuilder.createTextMessage(
                        sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                        SessionTypeEnum.Team, // 聊天类型，单聊或群组
                        comment.getText().toString().trim() // 文本内容
                );
                if (act.getClass().equals(NEVideoPlayerActivity.class)) {
                    if (((NEVideoPlayerActivity) act).limitMessage.size() >= 1) {
                        if (message.getTime() - ((NEVideoPlayerActivity) act).limitMessage.get(0).getTime() < 2000) {
                            Toast.makeText(act, getResources().getString(R.string.please_talk_later), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            ((NEVideoPlayerActivity) act).limitMessage.add(message);
                            if (((NEVideoPlayerActivity) act).limitMessage.size() > 1) {
                                ((NEVideoPlayerActivity) act).limitMessage.remove(0);
                            }
                        }
                    } else {
                        ((NEVideoPlayerActivity) act).limitMessage.add(message);
                    }
                }

                callback.sendMessage(message);
                comment.setText("");
                break;
        }
    }

    public boolean isAllowSendMessage() {
        if (team == null || !team.isMyTeam()) {
            Toast.makeText(act, R.string.team_send_message_not_allow, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 切换视频
     */
    public void switchVideo() {
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
    }

    /**
     * 当前屏幕横竖屏状态
     */
    public void setPortrait() {
        this.orientation = Configuration.ORIENTATION_PORTRAIT;
        if (zoom != null) {
            zoom.setVisibility(View.VISIBLE);
        }
//        if (isSubOpen) {
        if (isSubBig) {
            if (danmuSwitch.getVisibility() != View.VISIBLE) {
                danmuSwitch.setVisibility(View.VISIBLE);
            }
        } else {
            isDanmuOn = false;
            callback.shutDanmaku();
            danmuSwitch.setImageResource(R.mipmap.danmu_off);
            if (danmuSwitch.getVisibility() != View.GONE) {
                danmuSwitch.setVisibility(View.GONE);
            }
        }
//        }
        commentLayout.setVisibility(View.GONE);
        if (isSubOpen) {
            viewChange.setVisibility(View.VISIBLE);
        } else {
            viewChange.setVisibility(View.GONE);
        }
        comment.setText("");
        videoName.setVisibility(View.GONE);
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
                danmuSwitch.setVisibility(View.VISIBLE);
            } else {//转为小窗口竖屏时  弹幕关闭,并不可用
                danmuSwitch.setVisibility(View.GONE);
                danmuSwitch.setImageResource(R.mipmap.danmu_off);
                isDanmuOn = false;
                callback.shutDanmaku();
            }
        } else {
            isSubBig = true;
            callback.changeSubBig();
            danmuSwitch.setVisibility(View.VISIBLE);
        }
        viewChange.setImageResource(isSubBig ? R.mipmap.float_change_big : R.mipmap.float_change_small);
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
        arrayOfFloat1[1] = -playToolbar.getHeight();
        ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(playToolbar, "translationY", arrayOfFloat1).setDuration(300L);

        float[] arrayOfFloat2 = new float[2];
        arrayOfFloat2[0] = 0.0F;
        arrayOfFloat2[1] = bottomLayout.getHeight();
        ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(bottomLayout, "translationY", arrayOfFloat2).setDuration(300L);

        float[] arrayOfFloat3 = new float[2];
        arrayOfFloat3[0] = 0.0F;
        arrayOfFloat3[1] = subSwitch.getWidth();
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
        arrayOfFloat1[0] = -playToolbar.getHeight();
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
        viewChange.setImageResource(isSubBig ? R.mipmap.float_change_big : R.mipmap.float_change_small);
        if (!isSubBig) {//小窗口出现
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏时弹幕开关可用
                danmuSwitch.setVisibility(View.VISIBLE);
            } else {//转为小窗口竖屏时  弹幕关闭,并不可用
                danmuSwitch.setVisibility(View.GONE);
                danmuSwitch.setImageResource(R.mipmap.danmu_off);
                isDanmuOn = false;
                callback.shutDanmaku();
            }
        } else {
            danmuSwitch.setVisibility(View.VISIBLE);
        }
    }

    public void setSubOpen(boolean subOpen) {
        isSubOpen = subOpen;
        if (isSubOpen) {
            Toast.makeText(act, getResources().getString(R.string.live_side_open), Toast.LENGTH_SHORT).show();
            subSwitch.setImageResource(R.mipmap.float_subvideo_close);
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                viewChange.setVisibility(View.GONE);
            } else {
                danmuSwitch.setVisibility(View.VISIBLE);
                viewChange.setVisibility(View.VISIBLE);
            }
            ivSwitch.setVisibility(View.VISIBLE);
        } else {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                if (isDanmuOn) {
                    isDanmuOn = false;
                    danmuSwitch.setImageResource(R.mipmap.danmu_off);
                }
                danmuSwitch.setVisibility(View.GONE);
            }
            Toast.makeText(act, getResources().getString(R.string.live_side_close), Toast.LENGTH_SHORT).show();
            subSwitch.setImageResource(R.mipmap.float_subvideo_open);
            viewChange.setVisibility(View.GONE);
            ivSwitch.setVisibility(View.GONE);
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

    public void setDanmuOn(boolean danmuOn) {
        isDanmuOn = danmuOn;
        Logger.e("控制框弹幕状态" + isDanmuOn);
        if (isDanmuOn) {
            danmuSwitch.setImageResource(R.mipmap.danmu_on);
        } else {
            danmuSwitch.setImageResource(R.mipmap.danmu_off);
        }
    }

    public void setTeam(Team team) {
        this.team = team;
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

        void sendMessage(IMMessage message);

        void pause();

        void play();

        boolean isPlaying();
    }
}
