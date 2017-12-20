package cn.qatime.player.flow.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.qatime.player.R;
import cn.qatime.player.utils.ACache;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.vov.vitamio.MediaPlayer;

/**
 * @author luntify
 * @date 2017/12/12 16:30
 * @Description:
 */

public abstract class FlowBaseLayout extends LinearLayout {
    private Disposable d;
    private MediaPlayer player;
    private TextView countdown;

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

    }

    protected final void listenQuestion(final int count, final int total, final String path) {
        countdown = findViewById(R.id.countdown);
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
                        FlowBaseLayout.this.d = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        countdown.setText(aLong + "秒后将自动播放语音（" + count + "/" + total + "）");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        FlowBaseLayout.this.d = null;
                        countdown.setText("正在播放语音...（" + count + "/" + total + "）");
                        playMp3(count, total, path);
                    }
                });
    }

    private void playMp3(final int count, final int total, final String path) {
//        player = new MediaPlayer(getContext());
//        try {
//            player.setDataSource(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        player.prepareAsync();
//        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.start();
//            }
//        });
//        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mp.release();
//                player = null;
        if (count < total) {
            listenQuestion(count + 1, total, path);
        } else {
            answerQuestion();
        }
//            }
//        });
    }

    protected void answerQuestion() {
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
                        FlowBaseLayout.this.d = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        countdown.setText(aLong + "秒后关闭当前答题");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        FlowBaseLayout.this.d = null;
                        nextScreen();
                    }
                });
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

    protected void saveAnswer(Integer id, String answer) {
        HashMap<Integer, String> map = (HashMap<Integer, String>) ACache.get(getContext()).getAsObject("answer");
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(id, answer);
        ACache.get(getContext()).put("answer", map);
    }
}
