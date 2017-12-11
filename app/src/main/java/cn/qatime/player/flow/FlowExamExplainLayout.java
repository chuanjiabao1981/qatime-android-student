package cn.qatime.player.flow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.qatime.player.R;
import flow.Flow;

/**
 * @author luntify
 * @date 2017/12/11 15:17
 * @Description:
 */

public class FlowExamExplainLayout extends LinearLayout {
    public FlowExamExplainLayout(Context context) {
        super(context);
    }

    public FlowExamExplainLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowExamExplainLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlowExamExplainLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        FlowExplainScreen screen = Flow.getKey(this);
        TextView name = findViewById(R.id.name);
    }
}
