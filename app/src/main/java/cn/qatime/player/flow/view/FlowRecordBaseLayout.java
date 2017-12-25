package cn.qatime.player.flow.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.RecorderUtil;

/**
 * @author luntify
 * @date 2017/12/13 15:51
 * @Description:
 */

public abstract class FlowRecordBaseLayout extends FlowBaseLayout {
    private Button submit;
    private TextView status;
    protected String audioFileName;
    private RecorderUtil recorderUtil;
    private boolean isRecording = false;

    private Handler mp3Handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RecorderUtil.RECORDER_OK:
//                    audioAttachment.file_url = audioFileName;
//                    audioAttachment.file_type = "mp3";
//                    play.setImageResource(R.mipmap.refresh);
//                    addAttachments(audioAttachment);
                    break;
                case RecorderUtil.RECORDER_NG://error
                    Toast.makeText(getContext(), "录音失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    });

    public FlowRecordBaseLayout(Context context) {
        super(context);
    }

    public FlowRecordBaseLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowRecordBaseLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlowRecordBaseLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        submit = findViewById(R.id.submit);
        status = findViewById(R.id.status);
        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!isRecording) {
//                    recordAudio();
//                } else {

                nextScreen();

//                }
            }
        });
    }

    @Override
    protected void showSubmitButton() {
        super.showSubmitButton();
        //显示提交按钮
        submit.setVisibility(VISIBLE);
    }

    @Override
    protected void answerQuestion() {
        super.answerQuestion();
        //倒计时开始录音
        recordAudio();
    }

    private void recordAudio() {
        audioFileName = Constant.CACHEPATH + "/audio";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File files = new File(audioFileName);
            if (!files.exists()) {
                files.mkdirs();
            }
            File[] file = files.listFiles();
            for (File f : file) {
                f.delete();
            }
        }
        audioFileName = audioFileName + "/" + new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + ".mp3";
        try {
            new File(audioFileName).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorderUtil = new RecorderUtil(Constant.CACHEPATH + "/audio", mp3Handler);
        isRecording = recorderUtil.startMp3Recording(audioFileName);
        status.setVisibility(isRecording ? VISIBLE : GONE);
    }

    protected void stopRecord() {
        if (!isRecording) return;
        stopTimer();
        isRecording = false;
//        Toast.makeText(this, "停止录音", Toast.LENGTH_SHORT).show();
        status.setVisibility(GONE);
        recorderUtil.stopRawRecording();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        audioFileName = "";
    }
}
