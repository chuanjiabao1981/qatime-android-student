package cn.qatime.player.activity;

import android.os.Bundle;
import android.widget.EditText;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.utils.StringUtils;

/**
 * Created by lenovo on 2016/8/22.
 */
public class FeedBackActivity extends BaseActivity {

    private EditText opinion;
    private EditText contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        setContentView(R.layout.activity_feedback);
        setTitle("意见反馈");
        opinion = (EditText) findViewById(R.id.opinion);
        contact = (EditText) findViewById(R.id.contact);
        opinion.setHint(StringUtils.getSpannedString(this, R.string.hint_opinion));
        contact.setHint(StringUtils.getSpannedString(this, R.string.hint_contact));
    }
}
