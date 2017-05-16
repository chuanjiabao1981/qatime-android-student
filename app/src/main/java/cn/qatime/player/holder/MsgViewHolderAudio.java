package cn.qatime.player.holder;

import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import cn.qatime.player.R;
import cn.qatime.player.adapter.BaseMultiItemFetchLoadAdapter;
import cn.qatime.player.audio.MessageAudioControl;
import cn.qatime.player.audio.Playable;
import libraryextra.utils.DateUtils;
import libraryextra.utils.DensityUtils;

public class MsgViewHolderAudio extends MsgViewHolderBase {

    public MsgViewHolderAudio(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    public static int MAX_AUDIO_TIME_SECOND = 120;
    public static final int CLICK_TO_PLAY_AUDIO_DELAY = 500;

    private TextView durationLabel;
    private View containerView;
    private View unreadIndicator;
    private ImageView animationView;

    private MessageAudioControl audioControl;

    @Override
    protected int getContentResId() {
        return R.layout.item_message_audio;
    }

    @Override
    protected void inflateContentView() {
        durationLabel = findViewById(R.id.message_item_audio_duration);
        containerView = findViewById(R.id.message_item_audio_container);
        unreadIndicator = findViewById(R.id.message_item_audio_unread_indicator);
        animationView = findViewById(R.id.message_item_audio_playing_animation);
        animationView.setBackgroundResource(0);
        audioControl = MessageAudioControl.getInstance(context);
    }

    @Override
    protected void bindContentView() {
        layoutByDirection();

        refreshStatus();

        controlPlaying();
    }

    @Override
    protected void onItemClick() {
        if (audioControl != null) {
            if (message.getDirect() == MsgDirectionEnum.In && message.getAttachStatus() != AttachStatusEnum.transferred) {
                return;
            }

            if (message.getStatus() != MsgStatusEnum.read) {
                // 将未读标识去掉,更新数据库
                unreadIndicator.setVisibility(View.GONE);
            }

            initPlayAnim(); // 设置语音播放动画

            audioControl.startPlayAudioDelay(CLICK_TO_PLAY_AUDIO_DELAY, message, onPlayListener);

            audioControl.setPlayNext(true, adapter, message);
        }
    }

    private void layoutByDirection() {
        if (isReceivedMessage()) {
            setGravity(animationView, Gravity.LEFT | Gravity.CENTER_VERTICAL);
            setGravity(durationLabel, Gravity.RIGHT | Gravity.CENTER_VERTICAL);

            containerView.setBackgroundResource(R.drawable.chatfrom_bg_normal);
            containerView.setPadding(DensityUtils.dip2px(context, 15), DensityUtils.dip2px(context, 8), DensityUtils.dip2px(context, 10), DensityUtils.dip2px(context, 8));
            animationView.setBackgroundResource(R.drawable.audio_animation_list_left);

        } else {
            setGravity(animationView, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            setGravity(durationLabel, Gravity.LEFT | Gravity.CENTER_VERTICAL);
            unreadIndicator.setVisibility(View.GONE);

            containerView.setBackgroundResource(R.drawable.chatto_bg_normal);
            containerView.setPadding(DensityUtils.dip2px(context, 10), DensityUtils.dip2px(context, 8), DensityUtils.dip2px(context, 15), DensityUtils.dip2px(context, 8));
            animationView.setBackgroundResource(R.drawable.audio_animation_list_right);
        }
    }

    private void refreshStatus() {// 消息状态
        AudioAttachment attachment = (AudioAttachment) message.getAttachment();
        MsgStatusEnum status = message.getStatus();
        AttachStatusEnum attachStatus = message.getAttachStatus();

        // alert button
        if (TextUtils.isEmpty(attachment.getPath())) {
            if (attachStatus == AttachStatusEnum.fail || status == MsgStatusEnum.fail) {
                alertButton.setVisibility(View.VISIBLE);
            } else {
                alertButton.setVisibility(View.GONE);
            }
        }

        // progress bar indicator
        if (status == MsgStatusEnum.sending || attachStatus == AttachStatusEnum.transferring) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }

        // unread indicator
        if (isReceivedMessage() && attachStatus == AttachStatusEnum.transferred && status != MsgStatusEnum.read) {
            unreadIndicator.setVisibility(View.VISIBLE);
        } else {
            unreadIndicator.setVisibility(View.GONE);
        }
    }

    private void controlPlaying() {
        final AudioAttachment msgAttachment = (AudioAttachment) message.getAttachment();
        long duration = msgAttachment.getDuration();
        setAudioBubbleWidth(duration);

        if (!isMessagePlaying(audioControl, message)) {
            if (audioControl.getAudioControlListener() != null
                    && audioControl.getAudioControlListener().equals(onPlayListener)) {
                audioControl.changeAudioControlListener(null);
            }

            updateTime(duration);
            stop();
        } else {
            audioControl.changeAudioControlListener(onPlayListener);
            play();
        }
    }

    private int getAudioMaxEdge() {
        return (int) (0.6 * DensityUtils.screenMin(context));
    }

    private int getAudioMinEdge() {
        return (int) (0.1875 * DensityUtils.screenMin(context));
    }

    private void setAudioBubbleWidth(long milliseconds) {
        long seconds = DateUtils.getSecondsByMilliseconds(milliseconds);

        int currentBubbleWidth = calculateBubbleWidth(seconds, MAX_AUDIO_TIME_SECOND);
        ViewGroup.LayoutParams layoutParams = containerView.getLayoutParams();
        layoutParams.width = currentBubbleWidth;
        containerView.setLayoutParams(layoutParams);
    }

    private int calculateBubbleWidth(long seconds, int MAX_TIME) {
        int maxAudioBubbleWidth = getAudioMaxEdge();
        int minAudioBubbleWidth = getAudioMinEdge();

        int currentBubbleWidth;
        if (seconds <= 0) {
            currentBubbleWidth = minAudioBubbleWidth;
        } else if (seconds > 0 && seconds <= MAX_TIME) {
            currentBubbleWidth = (int) ((maxAudioBubbleWidth - minAudioBubbleWidth) * (2.0 / Math.PI)
                    * Math.atan(seconds / 10.0) + minAudioBubbleWidth);
        } else {
            currentBubbleWidth = maxAudioBubbleWidth;
        }

        if (currentBubbleWidth < minAudioBubbleWidth) {
            currentBubbleWidth = minAudioBubbleWidth;
        } else if (currentBubbleWidth > maxAudioBubbleWidth) {
            currentBubbleWidth = maxAudioBubbleWidth;
        }

        return currentBubbleWidth;
    }

    private void updateTime(long milliseconds) {
        long seconds = DateUtils.getSecondsByMilliseconds(milliseconds);

        if (seconds >= 0) {
            durationLabel.setText(seconds + "\"");
        } else {
            durationLabel.setText("");
        }
    }

    private boolean isMessagePlaying(MessageAudioControl audioControl, IMMessage message) {
        return audioControl.getPlayingAudio() != null && audioControl.getPlayingAudio().isTheSame(message);
    }

    private MessageAudioControl.AudioControlListener onPlayListener = new MessageAudioControl.AudioControlListener() {

        @Override
        public void updatePlayingProgress(Playable playable, long curPosition) {
            if (curPosition > playable.getDuration()) {
                return;
            }
            updateTime(curPosition);
        }

        @Override
        public void onAudioControllerReady(Playable playable) {
            play();
        }

        @Override
        public void onEndPlay(Playable playable) {
            updateTime(playable.getDuration());

            stop();
        }
    };

    private void play() {
        if (animationView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) animationView.getBackground();
            animation.start();
        }
    }

    private void stop() {
        if (animationView.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) animationView.getBackground();
            animation.stop();

            endPlayAnim();
        }
    }

    private void initPlayAnim() {
        if (isReceivedMessage()) {
            animationView.setBackgroundResource(R.drawable.audio_animation_list_left);
        } else {
            animationView.setBackgroundResource(R.drawable.audio_animation_list_right);
        }
    }

    private void endPlayAnim() {
        if (isReceivedMessage()) {
            animationView.setBackgroundResource(R.mipmap.audio_animation_list_left_3);
        } else {
            animationView.setBackgroundResource(R.mipmap.audio_animation_list_right_3);
        }
    }

//    @Override
//    public void reclaim() {
//        super.reclaim();
//        if (audioControl.getAudioControlListener() != null
//                && audioControl.getAudioControlListener().equals(onPlayListener)) {
//            audioControl.changeAudioControlListener(null);
//        }
//    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }
}
