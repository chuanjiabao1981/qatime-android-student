package cn.qatime.player.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;

/**
 * @author luntify
 * @date 2016/8/15 21:07
 * @Description
 */
public class AboutUsActivity extends BaseActivity {

    private View call;
    private TextView phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
    }

    private void initView() {
        setTitle(getResources().getString(R.string.about_us));
        call = findViewById(R.id.call_phone);
        phone = (TextView) findViewById(R.id.phone);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AboutUsActivity.this);
                builder.setTitle(getResourceString(R.string.alert));
                builder.setMessage(getResourceString(R.string.call_customer_service_phone) + phone.getText().toString() + "?");
                builder.setNegativeButton(getResourceString(R.string.call_right_now), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone.getText()));
                        startActivity(intent);
                    }
                });
                builder.setPositiveButton(getResourceString(R.string.cancel), null);
                builder.show();

            }
        });
    }
}
