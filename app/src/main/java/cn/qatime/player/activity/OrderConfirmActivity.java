package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.RemedialClassBean;
import cn.qatime.player.bean.RemedialClassDetailBean;

public class OrderConfirmActivity extends BaseActivity implements View.OnClickListener {
    private Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        RemedialClassDetailBean data = (RemedialClassDetailBean) getIntent().getSerializableExtra("data");

        pay = (Button) findViewById(R.id.pay);
        pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(OrderConfirmActivity.this,OrderPayActivity.class);
        startActivity(intent);
    }
}
