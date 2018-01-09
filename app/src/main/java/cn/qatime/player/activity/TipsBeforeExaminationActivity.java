package cn.qatime.player.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.exam.ExamResult;
import cn.qatime.player.utils.ACache;
import cn.qatime.player.utils.LogCatHelper;
import cn.qatime.player.utils.MPermission;
import cn.qatime.player.utils.MPermissionUtil;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.annotation.OnMPermissionDenied;
import cn.qatime.player.utils.annotation.OnMPermissionGranted;
import cn.qatime.player.utils.annotation.OnMPermissionNeverAskAgain;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import libraryextra.rx.HttpManager;
import libraryextra.rx.callback.SimpleCallBack;

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
        TextView name = (TextView) findViewById(R.id.name);
        TextView range = (TextView) findViewById(R.id.range);
        TextView time = (TextView) findViewById(R.id.time);
        TextView describe = (TextView) findViewById(R.id.describe);
        TextView count = (TextView) findViewById(R.id.count);
        name.setText(getIntent().getStringExtra("name"));
        range.setText("适考范围：" + getIntent().getStringExtra("category") + getIntent().getStringExtra("subject"));
        time.setText("考试时长：" + (getIntent().getIntExtra("duration", 0) == 0 ? 0 : (getIntent().getIntExtra("duration", 0) / 60)) + "分钟");
        count.setText("考题数量：共" + getIntent().getIntExtra("count", 0) + "题");
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
                                MPermission.with(TipsBeforeExaminationActivity.this)
                                        .addRequestCode(200)
                                        .permissions(new String[]{
                                                Manifest.permission.RECORD_AUDIO})
                                        .request();

                            }
                        });
                break;
        }
    }

    @OnMPermissionGranted(200)
    public void onLivePermissionGranted() {
        getResultId();

    }

    private void getResultId() {
        HttpManager.post(UrlUtils.urlExamPapers + getIntent().getIntExtra("id", 0) + "/results")
                .execute(new SimpleCallBack<ExamResult>() {
                    @Override
                    public void onSuccess(ExamResult o) {
                        ACache.get(TipsBeforeExaminationActivity.this).put("exam", (Serializable) o.getPaper().getCategories(), 60 * 90);
                        Intent intent = new Intent(TipsBeforeExaminationActivity.this, ExaminationIngActivity.class);
                        intent.putExtra("id", o.getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onTokenOut() {
                        tokenOut();
                    }
                });
    }

    @OnMPermissionDenied(200)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO});
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法开始考试";
        Toast.makeText(TipsBeforeExaminationActivity.this, tip, Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionNeverAskAgain(200)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, new String[]{Manifest.permission.RECORD_AUDIO});
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO});
        StringBuilder sb = new StringBuilder();
        sb.append("无法开始考试，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }

        Toast.makeText(TipsBeforeExaminationActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (d != null) {
            d.dispose();
        }
    }
}
