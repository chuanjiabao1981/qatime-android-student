package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.HashMap;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.flow.BasicDispatcher;
import cn.qatime.player.flow.BasicKeyParceler;
import cn.qatime.player.flow.screen.FlowExplainScreen;
import cn.qatime.player.utils.ACache;
import flow.Dispatcher;
import flow.Flow;
import libraryextra.view.CustomAlertDialog;

/**
 * @author luntify
 * @date 2017/12/1 10:43
 * @Description 考试考题页面
 */

public class ExaminationIngActivity extends BaseFragmentActivity {

    private BasicDispatcher dispatcher;

    @Override
    protected void attachBaseContext(Context newBase) {
        dispatcher = new BasicDispatcher(this);
        newBase = Flow.configure(newBase, this)
                .dispatcher(dispatcher)
                .defaultKey(new FlowExplainScreen(2, 10))
                .keyParceler(new BasicKeyParceler())
                .install();
        super.attachBaseContext(newBase);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination_ing);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();
    }

    private void initView() {
        setRightText("交卷", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dispatcher != null) {
                    dispatcher.pause();
                }
                if (alertDialog == null) {
                    View view = View.inflate(ExaminationIngActivity.this, R.layout.dialog_cancel_or_confirm, null);
                    Button cancel = (Button) view.findViewById(R.id.cancel);
                    Button confirm = (Button) view.findViewById(R.id.confirm);
                    TextView text = (TextView) view.findViewById(R.id.text);
                    text.setText(R.string.continue_buy_lasses_lesson);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dispatcher.reStart();
                            alertDialog.dismiss();
                        }
                    });
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            submitAnswer();
                            alertDialog.dismiss();
                        }
                    });
                    AlertDialog.Builder builder = new AlertDialog.Builder(ExaminationIngActivity.this);
                    alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.setContentView(view);
                } else {
                    alertDialog.show();
                }
            }
        });
    }

    private void submitAnswer() {
        HashMap<Integer, String> map = (HashMap<Integer, String>) ACache.get(ExaminationIngActivity.this).getAsObject("answer");
        if (map == null) {
            map = new HashMap<>();
        }
        Logger.e("answer"+map.values().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
