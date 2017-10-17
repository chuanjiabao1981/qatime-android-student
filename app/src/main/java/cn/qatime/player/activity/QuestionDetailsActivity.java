package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.AttachmentsBean;
import cn.qatime.player.bean.QuestionsBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.ExpandView;
import libraryextra.bean.ImageItem;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

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
    private QuestionsBean.DataBean.AnswerBean answerBean;


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
        String id = getIntent().getStringExtra("id");
        addToRequestQueue(new DaYiJsonObjectRequest(UrlUtils.urlQuestions+id,null,new VolleyListener(QuestionDetailsActivity.this){

            @Override
            protected void onTokenOut() {

            }

            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    question = JsonUtils.objectFromJson(response.getString("data"), QuestionsBean.DataBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (question == null) {
                    Toast.makeText(QuestionDetailsActivity.this, "问题获取失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTitles(question.getTitle());
                questionName.setText(question.getTitle());
                long time = question.getCreated_at() * 1000L;
                createTime.setText("创建时间 "+parse.format(new Date(time)));
                author.setText(question.getUser_name());
                List<AttachmentsBean> questionAttachments = question.getAttachments();
                AttachmentsBean audioAttachments = new AttachmentsBean();
                List<ImageItem> imageAttachments = new ArrayList<>();
                if (questionAttachments != null && questionAttachments.size() > 0) {
                    for (AttachmentsBean answerAttachment : questionAttachments) {
                        if ("mp3".equals(answerAttachment.file_type)) {
                            audioAttachments = answerAttachment;
                        } else {
                            ImageItem imageItem = new ImageItem();
                            imageItem.imagePath = answerAttachment.file_url;
                            imageAttachments.add(imageItem);
                        }
                    }
                }
                expandView.initExpandView(question.getBody(), audioAttachments.file_url, imageAttachments, true);
                if("resolved".equals(question.getStatus())){//已回复
                    findViewById(R.id.reply_layout).setVisibility(View.VISIBLE);
                    long replyT = question.getAnswer().getCreated_at() * 1000L;
                    replyTime.setText("回复时间 "+parse.format(new Date(replyT)));
                    if (question.getAnswer() != null) {
                        List<AttachmentsBean> answerAttachments = question.getAnswer().getAttachments();
                        AttachmentsBean answerAudioAttachments = new AttachmentsBean();
                        List<ImageItem> answerImageAttachments = new ArrayList<>();
                        if (answerAttachments != null && answerAttachments.size() > 0) {
                            for (AttachmentsBean answerAttachment : answerAttachments) {
                                if ("mp3".equals(answerAttachment.file_type)) {
                                    answerAudioAttachments = answerAttachment;
                                } else {
                                    ImageItem imageItem = new ImageItem();
                                    imageItem.imagePath = answerAttachment.file_url;
                                    answerImageAttachments.add(imageItem);
                                }
                            }
                        }
                        replyView.initExpandView(question.getAnswer().getBody(), answerAudioAttachments.file_url, answerImageAttachments, true);
                    } else {
                        replyView.initExpandView("无", null, null, true);
                    }
                }
            }

            @Override
            protected void onError(JSONObject response) {

            }
        },new VolleyErrorListener()));
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
}
