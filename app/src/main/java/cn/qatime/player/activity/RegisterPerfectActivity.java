package cn.qatime.player.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;


public class RegisterPerfectActivity extends BaseActivity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_perfect);
        setTitle(getResources().getString(R.string.information_perfect));
    }

//    @Override
//    public void onClick(View v) {
//        Intent intent=new
//    }
}
