package cn.qatime.player.flow.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author luntify
 * @date 2017/12/13 15:51
 * @Description:
 */

public class FlowRecordBaseLayout extends FlowBaseLayout {
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
        listenQuestion(1, 2, "");
    }

    @Override
    protected void answerQuestion() {
        super.answerQuestion();
        //开启语音按钮
    }

    @Override
    protected final void nextScreen() {

    }
}
