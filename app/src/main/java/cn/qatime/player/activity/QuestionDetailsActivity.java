package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.view.ExpandView;
import libraryextra.bean.ImageItem;

/**
 * Created by lenovo on 2017/8/18.
 */

public class QuestionDetailsActivity extends BaseActivity implements View.OnClickListener {

    private List<ImageItem> list;
    private String audioUrl;
    private String content;
    private String head;
    private ExpandView expandView;
    private TextView author;
    private TextView createTime;
    private TextView questionName;
    private ExpandView replyView;


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
        questionName = (TextView) findViewById(R.id.question_name);
        createTime = (TextView) findViewById(R.id.create_time);
        author = (TextView) findViewById(R.id.author);
        findViewById(R.id.expand).setOnClickListener(this);
    }

    private void initData() {
        head = getIntent().getStringExtra("head");
        content = getIntent().getStringExtra("content");
        audioUrl = getIntent().getStringExtra("audioUrl");
        list = (List<ImageItem>) getIntent().getSerializableExtra("image");

        questionName.setText(head);

        expandView.initExpandView(content,audioUrl,list,true);
        replyView.initExpandView(content,audioUrl,list,true);
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
