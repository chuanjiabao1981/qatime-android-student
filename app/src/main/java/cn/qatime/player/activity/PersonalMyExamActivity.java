package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * @author luntify
 * @date 2017/12/5 15:39
 * @Description:
 */

public class PersonalMyExamActivity extends BaseActivity implements View.OnClickListener {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_exam);
        setTitles("我的试卷");
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
        findViewById(R.id.examine).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.examine:
                startActivity(new Intent(PersonalMyExamActivity.this, PersonalMyExamSubmittedActivity.class));
                break;
        }
    }
}
