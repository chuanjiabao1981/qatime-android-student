package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author luntify
 * @date 2017/12/5 10:51
 * @Description: 考前提示
 */

public class TipsBeforeExaminationActivity extends BaseActivity implements View.OnClickListener {
    private Disposable d;
    private TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_before_examination);
        setTitles("模拟考试");
        initView();
    }

    private void initView() {
        TextView describe = (TextView) findViewById(R.id.describe);
        describe.setText("考试说明：\n" +
                "1. 模式考试下不能修改答案，请谨慎作答；\n" +
                "2. 为保证考试评测准确，请勿使用其它辅助工具或进行作弊；\n" +
                "3. 考试过程中有可能需要使用语音作答，请保持周围环境安静无噪音（如因噪音过大造成不能听清一律判为回答错误）；\n" +
                "4. 考试过程中不要使用电话通话或音视频活动等功能，以免造成mic被占用不能录音回答；\n" +
                "5. 考试结束并提交请根据提示耐心等待判卷结果。");
        findViewById(R.id.start_exam).setOnClickListener(this);
        timer = (TextView) findViewById(R.id.timer);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_exam:
                if (d != null) return;
                Observable.interval(0, 1, TimeUnit.SECONDS)
                        .take(6)
                        .map(new Function<Long, Long>() {
                            @Override
                            public Long apply(Long aLong) throws Exception {
                                return 5 - aLong;
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                TipsBeforeExaminationActivity.this.d = d;
                                timer.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onNext(Long aLong) {
                                timer.setText(String.valueOf(aLong));
                            }

                            @Override
                            public void onError(Throwable e) {
                                timer.setVisibility(View.GONE);

                            }

                            @Override
                            public void onComplete() {
                                timer.setVisibility(View.GONE);
                                TipsBeforeExaminationActivity.this.d = null;
                                // TODO: 2017/12/5 转业面
                            }
                        });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (d != null) {
            d.dispose();
        }
    }
}
