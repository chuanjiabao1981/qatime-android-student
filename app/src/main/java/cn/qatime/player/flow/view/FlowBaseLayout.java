package cn.qatime.player.flow.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cn.qatime.player.R;
import cn.qatime.player.utils.ACache;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import libraryextra.utils.StringUtils;

/**
 * @author luntify
 * @date 2017/12/12 16:30
 * @Description:
 */

public abstract class FlowBaseLayout extends LinearLayout {
    private Disposable d;
    private MediaPlayer player;
    private TextView countdown;
    protected int playTime = 1;//播放第几次
    protected int playTimes = 2;//一共播放几次
    protected int readTime = 10;
    protected int intervalTime = 3;
    protected int waitingTime = 10;
    protected String path = "";
    private int currentTime;//暂停时的秒数
    private playState currentState;//暂停时的状态

    public FlowBaseLayout(Context context) {
        super(context);
    }

    public FlowBaseLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowBaseLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FlowBaseLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        countdown = findViewById(R.id.countdown);
    }

    protected final void listenQuestion() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(playTime == 1 ? readTime + 1 : intervalTime + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return (playTime == 1 ? readTime : intervalTime) - aLong;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        FlowBaseLayout.this.d = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        currentState = playState.timer;
                        currentTime = aLong.intValue();
                        if (needPlay()) {
                            countdown.setText(aLong + "秒后将自动播放语音（" + playTime + "/" + playTimes + "）");
                        } else {
                            countdown.setText(aLong + "秒后停止短文浏览");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        FlowBaseLayout.this.d = null;
                        if (needPlay()) {
                            countdown.setText("正在播放语音...（" + playTime + "/" + playTimes + "）");
                            playMp3();
                        } else {
                            answerQuestion();
                        }
                    }
                });
    }

    protected boolean needPlay() {
        return true;
    }

    private void playMp3() {
        player = new MediaPlayer();
        if (StringUtils.isNullOrBlanK(path)) {
            Toast.makeText(getContext(), "播放地址不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            player.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (currentState == playState.playing) {
                    if (currentTime > 0 && player.getDuration() > currentTime) {
                        player.seekTo(currentTime);
                    }
                }
                mp.start();
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();

                player = null;
                if (playTime < playTimes) {
                    playTime += 1;
                    listenQuestion();
                } else {
                    answerQuestion();
                }
            }
        });
    }

    protected void stopTimer() {
        if (d != null) {
            d.dispose();
            d = null;
        }
    }

    protected void answerQuestion() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(waitingTime + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return waitingTime - aLong;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        FlowBaseLayout.this.d = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        currentState = playState.answering;
                        currentTime = aLong.intValue();
                        if (aLong <= waitingTime / 3) {
                            showSubmitButton();
                        }
                        countdown.setText(aLong + "秒后关闭当前答题");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        FlowBaseLayout.this.d = null;
                        currentState = playState.complete;
                        nextScreen();
                    }
                });
    }

    protected void showSubmitButton() {

    }

    protected abstract void nextScreen();

    protected final void initBottom(int i, int i1, int i2) {
        TextView answered = findViewById(R.id.answered);
        TextView unanswer = findViewById(R.id.unanswer);
        TextView total = findViewById(R.id.total);
        answered.setText("已答" + i);
        unanswer.setText("未答" + i1);
        total.setText("共" + i2 + "小题");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (player != null) {
            if (player.isPlaying()) {
                player.release();
                player = null;
            }
        }
        if (d != null) {
            d.dispose();
            d = null;
        }
    }

    public void pause() {
        if (d != null) {
            d.dispose();
            d = null;
        }
        if (player != null) {
            currentState = playState.playing;
            currentTime = player.getCurrentPosition();
            player.release();
            player = null;
        }
    }

    public void reStart() {
        if (currentState == playState.timer) {
            if (playTime == 1) {
                readTime = currentTime;
            } else
                intervalTime = currentTime;
            listenQuestion();
        } else if (currentState == playState.playing) {
            playMp3();
        } else if (currentState == playState.answering) {
            waitingTime = currentTime;
            answerQuestion();
        }
    }

    protected void saveAnswer(Integer id, String answer) {
        HashMap<Integer, String> map = (HashMap<Integer, String>) ACache.get(getContext()).getAsObject("answer");

        map.put(id, answer);
        ACache.get(getContext()).put("answer", map);
    }

    public boolean isComplete() {
        return currentState == playState.complete;
    }

    public enum playState {
        timer,//倒计时
        playing,//播放中
        answering,//回答问题中
        complete//试卷已做完
    }
}
