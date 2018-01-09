package cn.qatime.player.flow.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.bean.exam.Categories;
import cn.qatime.player.bean.exam.Topics;
import cn.qatime.player.flow.screen.FlowRePostScreen;
import cn.qatime.player.flow.screen.FlowRecordScreen;
import cn.qatime.player.utils.ACache;
import flow.Flow;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2017/12/12 14:47
 * @Description:
 */

public class FlowRecordLayout extends FlowBaseLayout {
    Topics data = null;

    public FlowRecordLayout(Context context) {
        super(context);
    }

    public FlowRecordLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowRecordLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FlowRecordLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        FlowRecordScreen screen = Flow.getKey(this);
        if (screen == null) return;
        List<Categories> categories = (List<Categories>) ACache.get(getContext()).getAsObject("exam");

        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getType().equals("Exam::ListenWriteReport")) {

                data = categories.get(i).getTopics().get(0).getTopics().get(screen.index);
                if (data.getRead_time() > 0) {
                    readTime = data.getRead_time();
                }
                if (data.getPlay_times() > 0) {
                    playTimes = data.getPlay_times();
                }
                if (data.getInterval_time() > 0) {
                    intervalTime = data.getInterval_time();
                }
                if (data.getWaiting_time() > 0) {
                    waitingTime = data.getWaiting_time();
                }

                if (categories.get(i).getTopics().get(0).getAttach() != null) {
                    path = categories.get(i).getTopics().get(0).getAttach().getUrl();
                }
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
        for (int i = 0; i < data.getTopics().size(); i++) {
            int resId = 0;
            try {
                Field field = R.mipmap.class.getDeclaredField("edit" + (i + 1));
                resId = Integer.parseInt(field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            saveAnswer(data.getTopics().get(i).getId(), ((EditText) findViewById(resId)).getText().toString());
        }
        FlowRecordScreen screen = Flow.getKey(this);
//        if (screen.index < screen.max - 1) {
//            Flow.get(this).set(new FlowRecordScreen(screen.index + 1, screen.passed + 1));
//        } else
        Flow.get(this).set(new FlowRePostScreen(1, screen.passed + data.getTopics_count()));
    }
}
