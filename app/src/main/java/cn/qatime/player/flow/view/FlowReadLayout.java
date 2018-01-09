package cn.qatime.player.flow.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.bean.exam.Categories;
import cn.qatime.player.bean.exam.Topics;
import cn.qatime.player.flow.screen.FlowReadScreen;
import cn.qatime.player.utils.ACache;
import flow.Flow;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2017/12/12 14:47
 * @Description:
 */

public class FlowReadLayout extends FlowRecordBaseLayout {
    private Topics data = null;

    public FlowReadLayout(Context context) {
        super(context);
    }

    public FlowReadLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowReadLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FlowReadLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        FlowReadScreen screen = Flow.getKey(this);

        if (screen == null) return;
        List<Categories> categories = (List<Categories>) ACache.get(getContext()).getAsObject("exam");

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getType().equals("Exam::ListenSpeak")) {
                if (categories.get(i).getRead_time() > 0) {
                    readTime = categories.get(i).getRead_time();
                }
                if (categories.get(i).getWaiting_time() > 0) {
                    waitingTime = categories.get(i).getWaiting_time();
                }
                data = categories.get(i).getTopics().get(screen.index);
                break;
            }
        }
        int total = 0;
        for (int i = 0; i < categories.size(); i++) {
            total += categories.get(i).getTopics_count();
        }
        Logger.e("readTime" + readTime + "    playTimes" + playTimes + "   intervalTime" + intervalTime + "  waitingTime" + waitingTime + "   path" + StringUtils.isNullOrBlanK(path));

        if (data == null) return;

        listenQuestion();
        TextView name = findViewById(R.id.name);
        name.setText(data.getTitle());

        initBottom(screen.passed, total - screen.passed, total);
    }

    @Override
    protected void nextScreen() {
        stopRecord();
        EventBus.getDefault().post("exam_complete");
    }

    @Override
    protected void stopRecord() {
        super.stopRecord();
        saveAnswer(data.getId(), audioFileName);
    }

    @Override
    protected boolean needPlay() {
        return false;
    }
}
