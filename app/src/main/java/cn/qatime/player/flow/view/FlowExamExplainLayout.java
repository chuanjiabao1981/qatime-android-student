package cn.qatime.player.flow.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import cn.qatime.player.R;
import cn.qatime.player.flow.screen.FlowAnswerScreen;
import cn.qatime.player.flow.screen.FlowExplainScreen;
import cn.qatime.player.flow.screen.FlowChooseScreen;
import cn.qatime.player.flow.screen.FlowReadScreen;
import cn.qatime.player.flow.screen.FlowRecordScreen;
import flow.Flow;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author luntify
 * @date 2017/12/11 15:17
 * @Description:
 */

public class FlowExamExplainLayout extends LinearLayout {
    private Disposable d;

    public FlowExamExplainLayout(Context context) {
        super(context);
    }

    public FlowExamExplainLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowExamExplainLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlowExamExplainLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final FlowExplainScreen screen = Flow.getKey(this);
        if (screen == null) return;

        TextView name = findViewById(R.id.name);
        TextView describe = findViewById(R.id.describe);
        Button next = findViewById(R.id.next);
        final TextView timer = findViewById(R.id.timer);
        if (screen.getIndex() == 0) {
            name.setText("Ⅰ.听后选择");
            describe.setText("听对话或独白，根据所听内容从每题A、B、C中选择最佳答案，并点击进行选择；听每段对话或独白前你有5s中时间阅读每小题，听完后你有5s时间作答，每段对话或独白你讲听两遍。");
        } else if (screen.getIndex() == 1) {
            name.setText("Ⅱ.听后回答");
            describe.setText("听对话或独白，根据所听内容口头回答问题。在听到结束提示音后你有10秒时间作答，每段对话你可以听两遍。");
        } else if (screen.getIndex() == 2) {
            name.setText("Ⅲ.听后记录&转述");
            describe.setText("听短文，记录并转述短文内容。记录-听2遍短文并根据所听内容和提示，将所缺的关键信息填写到相应位置上，每空只需填写一个词。转述-听第三遍短文，根据所听内容和提示进行转述。");
        } else if (screen.getIndex() == 3) {
            name.setText("Ⅳ.朗读短文");
            describe.setText("Ⅳ.朗读短文");
        }
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (d != null) {
                    d.dispose();
                    d = null;
                }
                if (screen.getIndex() == 0) {
                    Flow.get(FlowExamExplainLayout.this).set(new FlowChooseScreen(0, screen.passed));
                } else if (screen.getIndex() == 1) {
                    Flow.get(FlowExamExplainLayout.this).set(new FlowAnswerScreen(0, screen.passed));
                } else if (screen.getIndex() == 2) {
                    Flow.get(FlowExamExplainLayout.this).set(new FlowRecordScreen(0, screen.passed));
                } else if (screen.getIndex() == 3) {
                    Flow.get(FlowExamExplainLayout.this).set(new FlowReadScreen(0, screen.passed));
                }
            }
        });
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(11)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return 10 - aLong;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        FlowExamExplainLayout.this.d = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        timer.setText(aLong + "秒后自动进入");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        FlowExamExplainLayout.this.d = null;
                        if (screen.getIndex() == 0) {
                            Flow.get(FlowExamExplainLayout.this).set(new FlowChooseScreen(0, screen.passed));
                        } else if (screen.getIndex() == 1) {
                            Flow.get(FlowExamExplainLayout.this).set(new FlowAnswerScreen(0, screen.passed));
                        } else if (screen.getIndex() == 2) {
                            Flow.get(FlowExamExplainLayout.this).set(new FlowRecordScreen(0, screen.passed));
                        } else if (screen.getIndex() == 3) {
                            Flow.get(FlowExamExplainLayout.this).set(new FlowReadScreen(0, screen.passed));
                        }
                    }
                });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (d != null) {
            d.dispose();
            d = null;
        }
    }
}
