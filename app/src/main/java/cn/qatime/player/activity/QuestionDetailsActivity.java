package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.QuestionsBean;
import cn.qatime.player.view.ExpandView;

/**
 * Created by lenovo on 2017/8/18.
 */

public class QuestionDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ExpandView expandView;
    private TextView author;
    private TextView createTime;
    private TextView questionName;
    private ExpandView replyView;
    private QuestionsBean.DataBean question;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private TextView replyTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        initView();
        initData();
    }

    private void initView() {
        expandView = (ExpandView) findViewById(R.id.question_view);
        replyView = (ExpandView) findViewById(R.id.reply_view);
        questionName = (TextView) findViewById(R.id.question_name  );
        createTime = (TextView) findViewById(R.id.create_time);
        replyTime = (TextView) findViewById(R.id.reply_time);
        author = (TextView) findViewById(R.id.author);
        findViewById(R.id.expand).setOnClickListener(this);
    }

    private void initData() {
        question = (QuestionsBean.DataBean) getIntent().getSerializableExtra("detail");
        setTitles(question.getTitle());
        questionName.setText(question.getTitle());
        long time = question.getCreated_at() * 1000L;
        createTime.setText("创建时间 "+parse.format(new Date(time)));
        author.setText(question.getUser_name());

        expandView.initExpandView(question.getBody(),null,null,true);
        if("resolved".equals(question.getStatus())){//已回复
            findViewById(R.id.reply_layout).setVisibility(View.VISIBLE);
            long replyT = question.getAnswer().getCreated_at() * 1000L;
            replyTime.setText("回复时间 "+parse.format(new Date(replyT)));
            replyView.initExpandView(question.getAnswer().getBody(),null,null,true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expand:
                if(expandView.isExpand()){
                    expandView.collapse();
                }else {
                    expandView.expand();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(expandView!=null){
            expandView.onDestroy();
        }
    }
}
