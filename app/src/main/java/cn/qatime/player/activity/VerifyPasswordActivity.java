package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2016/8/17.
 */
public class VerifyPasswordActivity extends BaseActivity implements View.OnClickListener {
    private Button buttonOver;
    private EditText password;



    private void assignViews() {
        password = (EditText) findViewById(R.id.password);
        buttonOver = (Button) findViewById(R.id.button_over);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_password);
        initView();
    }

    private void initView() {
        setTitles(getResources().getString(R.string.parent_phone_number));
        assignViews();
        password.setHint(StringUtils.getSpannedString(this, R.string.hint_input_password));
        buttonOver.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_over:
                if (!StringUtils.isGoodPWD(password.getText().toString().trim())) {
                    Toast.makeText(this, getResources().getString(R.string.password_format_error), Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String,String> map = new HashMap<>();
                map.put("current_password", password.getText().toString());
                addToRequestQueue(new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlPersonalInformation+ BaseApplication.getUserId() + "/verify_current_password", map), null, new VolleyListener(this) {
                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                    @Override
                    protected void onSuccess(JSONObject response) {
                        try {
                            String data = response.getString("data");
                            if(!StringUtils.isNullOrBlanK(data)){
                                Intent intent = new Intent(VerifyPasswordActivity.this,ParentPhoneActivity.class);
                                intent.putExtra("ticket_token",data);
                                intent.putExtra("phoneP",getIntent().getStringExtra("phoneP"));
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(VerifyPasswordActivity.this, getString(R.string.password_error), Toast.LENGTH_SHORT).show();
                    }
                }, new VolleyErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        Toast.makeText(getApplicationContext(), getResourceString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }));
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
