package cn.qatime.player.flow.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.orhanobut.logger.Logger;

import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.bean.exam.Categories;
import cn.qatime.player.bean.exam.Topics;
import cn.qatime.player.flow.screen.FlowChooseScreen;
import cn.qatime.player.flow.screen.FlowExplainScreen;
import cn.qatime.player.utils.ACache;
import flow.Flow;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2017/12/12 14:47
 * @Description:
 */

public class FlowChooseLayout extends FlowBaseLayout {
    public FlowChooseLayout(Context context) {
        super(context);
    }

    public FlowChooseLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowChooseLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FlowChooseLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        FlowChooseScreen screen = Flow.getKey(this);
        if (screen == null) return;
        List<Categories> categories = (List<Categories>) ACache.get(getContext()).getAsObject("exam");
        Topics data = null;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getType().equals("Exam::ListenSelection")) {
                screen.max = categories.get(i).getTopics().size();
                if (categories.get(i).getRead_time() > 0) {
                    readTime = categories.get(i).getRead_time();
                }
                if (categories.get(i).getPlay_times() > 0) {
                    playTimes = categories.get(i).getPlay_times();
                }
                if (categories.get(i).getInterval_time() > 0) {
                    intervalTime = categories.get(i).getInterval_time();
                }
                if (categories.get(i).getWaiting_time() > 0) {
                    waitingTime = categories.get(i).getWaiting_time();
                }

                data = categories.get(i).getTopics().get(screen.index);
                if (data.getAttach() != null) {
                    path = data.getAttach().getUrl();
                }
                break;
            }
        }
        int total = 0;
        for (int i = 0; i < categories.size(); i++) {
            total += categories.get(i).getTopics_count();
        }
//        Logger.e("readTime" + readTime + "    playTimes" + playTimes + "   intervalTime" + intervalTime + "  waitingTime" + waitingTime + "   path" + StringUtils.isNullOrBlanK(path));
        if (data == null) return;

        listenQuestion();
        TextView name1 = findViewById(R.id.name1);
        TextView name2 = findViewById(R.id.name2);
        RadioGroup radioGroup1 = findViewById(R.id.radioGroup1);
        RadioGroup radioGroup2 = findViewById(R.id.radioGroup2);
        name1.setText(data.getTopics().get(0).getTitle());
        name2.setText(data.getTopics().get(1).getTitle());

        if (radioGroup1.getChildCount() > 0) radioGroup1.removeAllViews();
        radioGroup1.setOnCheckedChangeListener(null);
        for (int i = 0; i < data.getTopics().get(0).getTopic_options().size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(15, 0, 0, 0);
            radioButton.setLayoutParams(lp);
            radioButton.setText(data.getTopics().get(0).getTopic_options().get(i).getTitle());
            radioGroup1.addView(radioButton);
        }
        final Topics finalData = data;
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                if (i > 0) {
                    saveAnswer(finalData.getTopics().get(0).getTopic_options().get(group.indexOfChild(group.findViewById(i))).getTopic_id(), finalData.getTopics().get(0).getTopic_options().get(group.indexOfChild(group.findViewById(i))).getTitle());
//                    Logger.e(i + "选择" + finalData.getTopics().get(0).getTopic_options().get(group.indexOfChild(group.findViewById(i))).getTitle());
                }
            }
        });

        if (radioGroup2.getChildCount() > 0) radioGroup2.removeAllViews();
        radioGroup2.setOnCheckedChangeListener(null);
        for (int i = 0; i < data.getTopics().get(1).getTopic_options().size(); i++) {
            RadioButton radioButton = new RadioButton(getContext());
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(15, 0, 0, 0);
            radioButton.setLayoutParams(lp);
            radioButton.setText(data.getTopics().get(1).getTopic_options().get(i).getTitle());
            radioGroup2.addView(radioButton);
        }

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                if (i > 0) {
                    saveAnswer(finalData.getTopics().get(1).getTopic_options().get(group.indexOfChild(group.findViewById(i))).getTopic_id(), finalData.getTopics().get(1).getTopic_options().get(group.indexOfChild(group.findViewById(i))).getTitle());
//                    Logger.e(i + "选择" + finalData.getTopics().get(1).getTopic_options().get(group.indexOfChild(group.findViewById(i))).getTitle());
                }
            }
        });
        initBottom(screen.passed, total - screen.passed, total);
    }

    @Override
    protected void nextScreen() {
        FlowChooseScreen screen = Flow.getKey(this);
        if (screen.index < screen.max - 1) {
            Flow.get(this).set(new FlowChooseScreen(screen.index + 1, screen.passed + 2));
        } else
            Flow.get(this).set(new FlowExplainScreen(1, screen.passed + 2));
    }
}
