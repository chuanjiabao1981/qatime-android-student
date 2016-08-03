package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.RemedialClassBean;
import cn.qatime.player.bean.RemedialClassDetailBean;
import cn.qatime.player.fragment.FragmentRemedialClassDetail1;
import cn.qatime.player.fragment.FragmentRemedialClassDetail2;
import cn.qatime.player.fragment.FragmentRemedialClassDetail3;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VolleyErrorListener;
import cn.qatime.player.view.FragmentLayoutWithLine;

public class RemedialClassDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    ImageView image;
    FragmentLayoutWithLine fragmentlayout;
    private int id;

    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2, R.id.tab_text3};
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private Button audition;
    private Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedial_class_detail);
        id = getIntent().getIntExtra("id", 0);
        initView();
        initData();
    }


    private void initView() {
        image = (ImageView) findViewById(R.id.image);
        audition = (Button) findViewById(R.id.audition);
        pay = (Button) findViewById(R.id.pay);

        fragBaseFragments.add(new FragmentRemedialClassDetail1());
        fragBaseFragments.add(new FragmentRemedialClassDetail2());
        fragBaseFragments.add(new FragmentRemedialClassDetail3());

        fragmentlayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(6, 0xff000000);
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int positon, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff858585);
                ((TextView) currentTabView.findViewById(tab_text[positon])).setTextColor(0xff222222);
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_remedial_class_detail, 0x0012);
        audition.setOnClickListener(this);
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("Remember-Token", BaseApplication.getProfile().getToken());
        map.put("id", String.valueOf(id));
//        map.put("password", password.getText().toString());
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass + "/" + id, map), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        LogUtils.e(jsonObject.toString());
                        Gson gson = new Gson();
                        RemedialClassDetailBean data = gson.fromJson(jsonObject.toString(), RemedialClassDetailBean.class);
                        ((FragmentRemedialClassDetail1) fragBaseFragments.get(0)).setData(data);
                        ((FragmentRemedialClassDetail2) fragBaseFragments.get(1)).setData(data);
                        ((FragmentRemedialClassDetail3) fragBaseFragments.get(2)).setData(data);
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audition:
                break;
            case R.id.pay:
//                Intent intent = new Intent(RemedialClassDetailActivity.this,)
                break;
        }
    }
}
