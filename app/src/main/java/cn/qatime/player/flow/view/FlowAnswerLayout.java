package cn.qatime.player.flow.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jakewharton.rxbinding2.widget.RxRadioGroup;
import com.orhanobut.logger.Logger;

import cn.qatime.player.R;
import cn.qatime.player.flow.screen.FlowAnswerScreen;
import cn.qatime.player.flow.screen.FlowChooseScreen;
import flow.Flow;
import io.reactivex.functions.Consumer;

/**
 * @author luntify
 * @date 2017/12/12 14:47
 * @Description:
 */

public class FlowAnswerLayout extends FlowRecordBaseLayout {
    public FlowAnswerLayout(Context context) {
        super(context);
    }

    public FlowAnswerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowAnswerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FlowAnswerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        FlowAnswerScreen screen = Flow.getKey(this);

        initBottom(2, 1, 3);
    }

}
