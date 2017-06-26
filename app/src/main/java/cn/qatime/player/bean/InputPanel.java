package cn.qatime.player.bean;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.model.Team;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.activity.NEVideoPlayerActivity;
import cn.qatime.player.activity.PictureSelectActivity;
import cn.qatime.player.adapter.BiaoqingAdapter;
import cn.qatime.player.utils.Constant;
import libraryextra.bean.ImageItem;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.StringUtils;
import libraryextra.view.TagViewPager;

import static android.view.View.GONE;

/**
 * @author lungtify
 * @Time 2016/12/9 21:02
 * @Describe 输入框编辑栏
 */

public class InputPanel implements View.OnClickListener, IAudioRecordCallback {
    private final Activity context;
    private final View rootView;
    private final InputPanelListener listener;
    private final String sessionId;
    private String[][] biaoqingTags = new String[3][28];
    private LinearLayout inputEmojiLayout;
    private EditText content;
    private ImageView emoji;
    private Button send;
    private ImageView imageSelect;
    private TagViewPager tagViewPager;
    private Runnable runnable;
    private Handler hd = new Handler();
    private Team team;
    private boolean isMute = false;
    private OnInputShowListener onInputShowListener;

    private View buttonAudioMessage;
    private View buttonTextMessage;
    private Button audioRecord;

    private boolean touched = false; // 是否按着
    private boolean started = false;//录音是否开始
    private boolean cancelled = false;//录音是否可取消
    // 语音
    private AudioRecorder audioMessageHelper;
    private Chronometer time;
    private TextView timerTip;
    private RelativeLayout audioAnimLayout;

    private long start = 0;
    private long end = 0;
    private ImageView alertImage;
    private AudioRecordListener audioRecordListener;

    public void onPause() {
        // 停止录音
        if (audioMessageHelper != null) {
            onEndAudioRecord(true);
        }
    }

    public interface InputPanelListener {
        void ChatMessage(IMMessage message);

        boolean isShowTime();//是否是正在直播，是就禁用语音聊天
    }

    public interface AudioRecordListener {
        void audioRecordStart();

        void audioRecordStop();
    }

    public void setOnAudioRecordListener(AudioRecordListener audioRecordListener) {
        this.audioRecordListener = audioRecordListener;
    }

    public interface OnInputShowListener {
        void OnInputShow();
    }

    public void setOnInputShowListener(OnInputShowListener onTouchListener) {
        this.onInputShowListener = onTouchListener;
    }

    /**
     * @param context
     * @param rootView
     * @param showInput 初始化时,是否显示输入框
     * @param sessionId 群组id
     */
    public InputPanel(final Activity context, InputPanelListener listener, View rootView, boolean showInput, String sessionId) {
        this.context = context;
        this.rootView = rootView;
        this.listener = listener;
        this.sessionId = sessionId;

        assignViews(showInput);
        initAudioRecordButton();

        for (int i = 0; i < 3; i++) {
            for (int j = 27 * i; j < 27 * (i + 1); j++) {
                biaoqingTags[i][j - 27 * i] = "em_" + String.valueOf(j + 1);
            }
            biaoqingTags[i][27] = "emoji_delete";
        }

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, DensityUtils.dp2px(context, 180));
        tagViewPager.setLayoutParams(params);
        tagViewPager.setId(R.id.viewPager);
        tagViewPager.setVisibility(GONE);
        tagViewPager.init(R.drawable.shape_biaoqing_tag_select, R.drawable.shape_biaoqing_tag_nomal, 16, 8, 2, 40);
        tagViewPager.setAutoNext(false, 0);
        tagViewPager.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, int position) {
                GridView mGridView = new GridView(context);
                mGridView.setLayoutParams(params);
                mGridView.setNumColumns(7);
                mGridView.setBackgroundColor(Color.WHITE);
                mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
                mGridView.setAdapter(new BiaoqingAdapter(context, getList(position)));
                mGridView.setOnItemClickListener(new ItemClickListener(position));
                container.addView(mGridView);
                return mGridView;
            }
        });
        tagViewPager.setAdapter(3);
    }

    /**
     * @param position 第几项
     * @return 链表
     */
    private ArrayList<BiaoQingData> getList(int position) {
        ArrayList<BiaoQingData> list = new ArrayList<>();
        for (int i = 0; i < biaoqingTags[position].length; i++) {
            BiaoQingData data = new BiaoQingData();
            data.setTag(biaoqingTags[position][i]);
            int resId = 0;
            try {
                Field field = R.mipmap.class.getDeclaredField(biaoqingTags[position][i]);
                resId = Integer.parseInt(field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            data.setResourceId(resId);
            list.add(data);
        }
        return list;
    }

    private void assignViews(boolean showInput) {
        buttonAudioMessage = rootView.findViewById(R.id.buttonAudioMessage);
        buttonTextMessage = rootView.findViewById(R.id.buttonTextMessage);
        audioRecord = (Button) rootView.findViewById(R.id.audioRecord);
        audioAnimLayout = (RelativeLayout) rootView.findViewById(R.id.layoutPlayAudio);
        time = (Chronometer) rootView.findViewById(R.id.timer);
        alertImage = (ImageView) rootView.findViewById(R.id.alert_image);
        timerTip = (TextView) rootView.findViewById(R.id.timer_tip);

        inputEmojiLayout = (LinearLayout) rootView.findViewById(R.id.input_emoji_layout);
        content = (EditText) rootView.findViewById(R.id.content);
        emoji = (ImageView) rootView.findViewById(R.id.emoji);
        send = (Button) rootView.findViewById(R.id.send);
        imageSelect = (ImageView) rootView.findViewById(R.id.image_select);
        tagViewPager = (TagViewPager) rootView.findViewById(R.id.tagViewPager);
        inputEmojiLayout.setVisibility(showInput ? View.VISIBLE : GONE);

        runnable = new Runnable() {
            @Override
            public void run() {
                tagViewPager.setVisibility(View.VISIBLE);
                emoji.setImageResource(R.mipmap.keybord);

                buttonAudioMessage.setVisibility(View.VISIBLE);
                buttonTextMessage.setVisibility(View.GONE);
                audioRecord.setVisibility(GONE);
                content.setVisibility(View.VISIBLE);

            }
        };
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onInputShowListener != null) {
                    onInputShowListener.OnInputShow();
                }
                if (tagViewPager.getVisibility() == GONE) {
                    hd.postDelayed(runnable, 50);
                    closeInput();
                } else {
                    closeEmojiAndShowInput(true);
                }
            }
        });
        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (onInputShowListener != null) {
                    onInputShowListener.OnInputShow();
                }
                closeEmojiAndShowInput(false);
                return false;
            }
        });
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                changeSendStatus();
            }
        });
        buttonAudioMessage.setOnClickListener(this);
        buttonTextMessage.setOnClickListener(this);
        send.setOnClickListener(this);
        imageSelect.setOnClickListener(this);
    }

    /**
     * @param closeAudio 是否需要关闭语音按钮
     */
    private void closeEmojiAndShowInput(boolean closeAudio) {
        if (closeAudio) {
            audioRecord.setVisibility(GONE);
            content.setVisibility(View.VISIBLE);
        }
        tagViewPager.setVisibility(GONE);
        emoji.setImageResource(R.mipmap.biaoqing);
        content.requestFocus();
        openInput();
        buttonAudioMessage.setVisibility(View.VISIBLE);
        buttonTextMessage.setVisibility(View.GONE);
    }

    /**
     * 刷新发送按钮状态
     */
    private void changeSendStatus() {
        if (StringUtils.isNullOrBlanK(content.getText().toString().trim())) {
            send.setVisibility(GONE);
            imageSelect.setVisibility(View.VISIBLE);
            imageSelect.setEnabled(true);
        } else {
            send.setVisibility(View.VISIBLE);
            imageSelect.setVisibility(View.INVISIBLE);
            imageSelect.setEnabled(false);
        }
    }


    /**
     * 关闭输入法
     */
    private void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(content.getWindowToken(), 0);
    }

    /**
     * 打开输入发
     */

    private void openInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 接受软键盘输入的编辑文本或其它视图
        inputMethodManager.showSoftInput(content, 0);
    }

    public void closeEmojiAndInput() {
        emoji.setImageResource(R.mipmap.biaoqing);
        if (tagViewPager.getVisibility() == View.VISIBLE) {
            tagViewPager.setVisibility(GONE);
        }
        closeInput();
    }

    public boolean isEmojiShow() {
        return tagViewPager != null && tagViewPager.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAudioMessage:
                switchToAudioLayout();
                onInputShowListener.OnInputShow();
                break;
            case R.id.buttonTextMessage:
                switchToTextLayout();// 显示文本发送的布局
                onInputShowListener.OnInputShow();
                break;
            case R.id.send://发送按钮
                if (!isAllowSendMessage()) {
                    return;
                }
                if (StringUtils.isNullOrBlanK(content.getText().toString())) {
                    Toast.makeText(context, context.getResources().getString(R.string.message_can_not_null), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkMute()) {
                    return;
                }
                // 创建文本消息
                IMMessage message = MessageBuilder.createTextMessage(
                        sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                        SessionTypeEnum.Team, // 聊天类型，单聊或群组
                        content.getText().toString().trim() // 文本内容
                );

                if (context.getClass().equals(NEVideoPlayerActivity.class)) {
                    if (((NEVideoPlayerActivity) context).limitMessage.size() >= 1) {
                        if (message.getTime() - ((NEVideoPlayerActivity) context).limitMessage.get(0).getTime() < 2000) {
                            Toast.makeText(context, context.getResources().getString(R.string.please_talk_later), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            ((NEVideoPlayerActivity) context).limitMessage.add(message);
                            if (((NEVideoPlayerActivity) context).limitMessage.size() > 1) {
                                ((NEVideoPlayerActivity) context).limitMessage.remove(0);
                            }
                        }
                    } else {
                        ((NEVideoPlayerActivity) context).limitMessage.add(message);
                    }
                }
                if (listener != null) {
                    listener.ChatMessage(message);
                }
//                Logger.e(content.getText().toString().trim());
                clearInputValue();
                break;
            case R.id.image_select://选择照片
                closeEmojiAndInput();
                Intent intent = new Intent(context, PictureSelectActivity.class);
//                intent.putExtra("gonecamera", true);
                context.startActivityForResult(intent, Constant.REQUEST);
                break;
        }
    }

    private void switchToTextLayout() {
        audioRecord.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);

        if (tagViewPager.getVisibility() == View.VISIBLE) {
            tagViewPager.setVisibility(GONE);
            emoji.setImageResource(R.mipmap.biaoqing);
        }
        content.requestFocus();
        openInput();

        buttonTextMessage.setVisibility(GONE);
        buttonAudioMessage.setVisibility(View.VISIBLE);
    }

    private void switchToAudioLayout() {
        clearInputValue();
        content.setVisibility(GONE);
        audioRecord.setVisibility(View.VISIBLE);

        closeEmojiAndInput();

        buttonAudioMessage.setVisibility(View.GONE);
        buttonTextMessage.setVisibility(View.VISIBLE);
    }

    /**
     * 检查是否被禁言
     *
     * @return true 被禁言
     */
    public boolean checkMute() {
        if (isMute) {
            Toast.makeText(context, context.getResources().getString(R.string.have_muted), Toast.LENGTH_SHORT).show();
            clearInputValue();
            return true;
        }
        return false;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setMute(boolean mute) {
        this.isMute = mute;
        if (isMute) {
            content.setHint(R.string.have_muted);
        } else {
            content.setHint("");
        }
    }

    /**
     * ****************************** 语音 ***********************************
     */
    private void initAudioRecordButton() {

        audioRecord.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (listener.isShowTime()) {
                    Toast.makeText(context, "正在直播中，禁用语音聊天", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(context,new String[]{
                                    android.Manifest.permission.RECORD_AUDIO},1);
                            return true;
                        }
                    }
                    touched = true;
                    if (audioRecordListener != null) {
                        audioRecordListener.audioRecordStart();
                    }
                    initAudioRecord();
                    onStartAudioRecord();
                    start = System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                    touched = false;
                    if (audioRecordListener != null) {
                        audioRecordListener.audioRecordStop();
                    }
                    end = System.currentTimeMillis();
                    if (end - start < 800) {
                        tooShortAudioRecord();
                    } else {
                        onEndAudioRecord(isCancelled(v, event));
                    }
                    start = 0;
                    end = 0;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    touched = false;
                    cancelAudioRecord(isCancelled(v, event));
                }

                return false;
            }
        });
    }

    private void tooShortAudioRecord() {
        context.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        updateTimerTip(RecorderState.TOOSHORT);
        time.stop();
        time.setBase(SystemClock.elapsedRealtime());
        audioMessageHelper.completeRecord(true);
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                audioAnimLayout.setVisibility(GONE);
            }
        }, 500);


        audioRecord.setText(R.string.record_audio);
        audioRecord.setBackgroundResource(R.drawable.shape_input_radius);
    }

    // 上滑取消录音判断
    private static boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        return event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth() || event.getRawY() < location[1] - 40;

    }

    /**
     * 初始化AudioRecord
     */
    private void initAudioRecord() {
        if (audioMessageHelper == null) {
            audioMessageHelper = new AudioRecorder(context, RecordType.AMR, 60, this);
        }
    }

    /**
     * 开始语音录制
     */
    private void onStartAudioRecord() {
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        audioMessageHelper.startRecord();
        cancelled = false;

        if (!touched) {
            return;
        }

        audioRecord.setText(R.string.record_audio_end);
        audioRecord.setBackgroundResource(R.drawable.shape_input_radius);

        updateTimerTip(RecorderState.NORMAL);
        playAudioRecordAnim();
    }


    private Runnable getVoiceLevel = new Runnable() {
        @Override
        public void run() {

            updateTimerTip(RecorderState.NORMAL); // 初始化语音动画状态
//            Logger.e("getVoiceLevel");
//                hd.postDelayed(this, 200);
        }
    };

    /**
     * 结束语音录制
     *
     * @param cancel
     */
    private void onEndAudioRecord(boolean cancel) {
        context.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        hd.removeCallbacks(getVoiceLevel);
        audioMessageHelper.completeRecord(cancel);
        stopAudioRecordAnim();
        audioRecord.setText(R.string.record_audio);
        audioRecord.setBackgroundResource(R.drawable.shape_input_radius);
    }

    /**
     * 取消语音录制
     *
     * @param cancel
     */
    private void cancelAudioRecord(boolean cancel) {
        // reject
        if (!started) {
            return;
        }
        // no change
        if (cancelled == cancel) {
            return;
        }

        cancelled = cancel;
        updateTimerTip(cancel ? RecorderState.CANCEL : RecorderState.NORMAL);
    }

    /**
     * 开始语音录制动画
     */
    private void playAudioRecordAnim() {
        audioAnimLayout.setVisibility(View.VISIBLE);
        time.setBase(SystemClock.elapsedRealtime());
        time.start();
    }

    /**
     * 结束语音录制动画
     */
    private void stopAudioRecordAnim() {
        audioAnimLayout.setVisibility(View.GONE);
        time.stop();
        time.setBase(SystemClock.elapsedRealtime());
    }

    /**
     * 正在进行语音录制和取消语音录制，界面展示
     *
     * @param state
     */
    private void updateTimerTip(RecorderState state) {
        if (state == RecorderState.CANCEL) {
            time.setVisibility(View.VISIBLE);
            hd.removeCallbacks(getVoiceLevel);
            alertImage.setImageResource(R.mipmap.record_cancel);
            timerTip.setText(R.string.recording_cancel_tip);
            timerTip.setBackgroundResource(R.drawable.shape_timer_tip);
        } else if (state == RecorderState.TOOSHORT) {
            time.setVisibility(View.INVISIBLE);
            hd.removeCallbacks(getVoiceLevel);
            alertImage.setImageResource(R.mipmap.record_too_short);
            timerTip.setText(R.string.recording_too_short);
            timerTip.setBackgroundResource(0);
        } else if (state == RecorderState.NORMAL) {
            time.setVisibility(View.VISIBLE);
            hd.postDelayed(getVoiceLevel, 300);
            timerTip.setText(R.string.recording_cancel);
            timerTip.setBackgroundResource(0);
            alertImage.setImageResource(getVoice(audioMessageHelper.getCurrentRecordMaxAmplitude()));
        }
    }

    private int getVoice(int amplitude) {
        if (amplitude > 20000) {
            return R.mipmap.record7;
        } else if (amplitude <= 15000 && amplitude > 10000) {
            return R.mipmap.record6;
        } else if (amplitude <= 10000 && amplitude > 8000) {
            return R.mipmap.record5;
        } else if (amplitude <= 8000 && amplitude > 5000) {
            return R.mipmap.record4;
        } else if (amplitude <= 5000 && amplitude > 3000) {
            return R.mipmap.record3;
        } else if (amplitude <= 3000 && amplitude > 1000) {
            return R.mipmap.record2;
        } else
            return R.mipmap.record1;
    }

    @Override
    public void onRecordReady() {
        Logger.e("onRecordReady");
    }

    @Override
    public void onRecordStart(File file, RecordType recordType) {
        started = true;
        if (!touched) {
            return;
        }

        audioRecord.setText(R.string.record_audio_end);
        audioRecord.setBackgroundResource(R.drawable.shape_input_radius);

//        updateTimerTip(RecorderState.NORMAL); // 初始化语音动画状态
//        playAudioRecordAnim();
        Logger.e("onRecordStart");
    }

    @Override
    public void onRecordSuccess(File audioFile, long audioLength, RecordType recordType) {
        IMMessage audioMessage = MessageBuilder.createAudioMessage(sessionId, SessionTypeEnum.Team, audioFile, audioLength);
        listener.ChatMessage(audioMessage);

    }

    @Override
    public void onRecordFail() {
        if (started) {
            Toast.makeText(context, R.string.recording_error, Toast.LENGTH_SHORT).show();
        }
        Logger.e("onRecordFail");
    }

    @Override
    public void onRecordCancel() {

    }

    @Override
    public void onRecordReachedMaxTime(int maxTime) {
        stopAudioRecordAnim();
        audioMessageHelper.handleEndRecord(true, maxTime);
    }

    private enum RecorderState {
        TOOSHORT, NORMAL, CANCEL

    }

    public boolean isRecording() {
        return audioMessageHelper != null && audioMessageHelper.isRecording();
    }

    public void clearInputValue() {
        content.setText("");
        changeSendStatus();
    }

    /**
     * 关闭表情输入法等  & 不显示输入框
     */
    public void goneInput() {
        closeEmojiAndInput();
        inputEmojiLayout.setVisibility(GONE);
    }

    /**
     * 显示输入框
     */
    public void visibilityInput() {
        inputEmojiLayout.setVisibility(View.VISIBLE);
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener {

        private final int position;

        ItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == ((BiaoqingAdapter) (parent.getAdapter())).getList().size() - 1) {
                //动作按下
                int action = KeyEvent.ACTION_DOWN;
                //code:删除，其他code也可以，例如 code = 0
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                content.onKeyDown(KeyEvent.KEYCODE_DEL, event); //抛给系统处理了
                return;
            }
            if (this.position != 2 || position < 21) {
                BiaoQingData item = ((BiaoqingAdapter) (parent.getAdapter())).getList().get(position);
                ImageSpan is;
                SpannableString sp;
                is = new ImageSpan(content.getContext(), item.getResourceId());
                sp = new SpannableString(item.getCode());
                sp.setSpan(is, 0, item.getCode().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                content.append(sp);
            }
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constant.RESPONSE_CAMERA) {//拍照返回的照片
            if (data != null) {
                String url = data.getStringExtra("url");
                if (url != null && !StringUtils.isNullOrBlanK(url)) {
                    File file = new File(url);
                    if (file.exists()) {
                        listener.ChatMessage(MessageBuilder.createImageMessage(sessionId, SessionTypeEnum.Team, file, file.getName()));
                    }
                }
            }
        } else if (resultCode == Constant.RESPONSE_PICTURE_SELECT) {//选择照片返回的照片
            if (data != null) {
                ImageItem image = (ImageItem) data.getSerializableExtra("data");
                if (image != null && !StringUtils.isNullOrBlanK(image.imagePath)) {
                    if (!isAllowSendMessage()) {
                        return;
                    }
                    if (checkMute()) {
                        return;
                    }
                    File file = new File(image.imagePath);
                    if (file.exists()) {
                        listener.ChatMessage(MessageBuilder.createImageMessage(sessionId, SessionTypeEnum.Team, file, file.getName()));
                    }
                }
            }
        }
    }


    private boolean isAllowSendMessage() {
        if (team == null || !team.isMyTeam()) {
            Toast.makeText(context, R.string.team_send_message_not_allow, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
