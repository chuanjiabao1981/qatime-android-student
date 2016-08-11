package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

public class PersonalInformationActivity extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        setTitle(getResources().getString(R.string.personal_information));
        setRightImage(R.drawable.personal_change_information, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalInformationActivity.this, PersonalInformationChangeActivity.class);
                startActivity(intent);
            }
        });

    }
}
