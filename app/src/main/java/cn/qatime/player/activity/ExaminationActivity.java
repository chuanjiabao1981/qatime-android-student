package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.Serializable;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.bean.exam.Examination;
import cn.qatime.player.bean.exam.Paper;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.ShareUtil;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.OrderPayBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author luntify
 * @date 2017/12/1 10:43
 * @Description 考试详情
 */

public class ExaminationActivity extends BaseActivity implements View.OnClickListener {
    private int id;
    private TextView name;
    private TextView price;
    private RelativeLayout handleLayout;
    private Examination data;
    private TextView buyCount;
    private TextView examTime;
    private TextView topicCount;
    private TextView examRange;
    private Button startStudy;
    private Button pay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examination);
        EventBus.getDefault().register(this);
        id = getIntent().getIntExtra("id", 0);
        initView();
        if (id != 0) {
            initData();
        }
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlExamPapers + id, null,
                new VolleyListener(ExaminationActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), Examination.class);
                        if (data != null && data.getData() != null) {
                            handleLayout.setVisibility(View.VISIBLE);
                            if (data.getData().getTicket() != null) {
                                pay.setVisibility(View.GONE);
                                startStudy.setVisibility(View.VISIBLE);
//                            } else {
                                //未购买
                            }
                            if (data.getData().getPaper() != null) {
                                Paper bean = data.getData().getPaper();
                                name.setText(bean.getName());
                                price.setText("￥" + bean.getPrice());
                                buyCount.setText("已购人数 " + bean.getUsers_count());
                                examTime.setText("考试时长:" + (bean.getDuration() == 0 ? 0 : bean.getDuration() / 60) + "分钟");
                                topicCount.setText("考题数量:共" + bean.getTopics_count() + "小题");
                                examRange.setText("适考范围:" + bean.getGrade_category() + bean.getSubject());
                            }
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    private void initView() {
        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        buyCount = (TextView) findViewById(R.id.buy_count);

        examTime = (TextView) findViewById(R.id.exam_time);
        topicCount = (TextView) findViewById(R.id.topic_count);
        examRange = (TextView) findViewById(R.id.exam_range);

        handleLayout = (RelativeLayout) findViewById(R.id.handle_layout);
        pay = (Button) findViewById(R.id.pay);
        startStudy = (Button) findViewById(R.id.start_study);
        pay.setOnClickListener(this);
        startStudy.setOnClickListener(this);

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == 0) {
                    Toast.makeText(ExaminationActivity.this, "id为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ShareUtil.getInstance(ExaminationActivity.this, UrlUtils.getBaseUrl() + "live_studio/courses/" + id, name.getText().toString(), "考试课课程", new ShareUtil.ShareListener() {
                    @Override
                    public void onSuccess(SHARE_MEDIA platform) {

                    }
                }).open();
            }
        });
    }

    private void payRemedial() {
        Intent intent = new Intent(ExaminationActivity.this, OrderConfirmActivity.class);
        intent.putExtra("courseType", "exam");
        intent.putExtra("id", id);
        intent.putExtra("coupon", getIntent().getStringExtra("coupon"));
        OrderPayBean bean = new OrderPayBean();
        bean.name = data.getData().getPaper().getName();
        bean.current_price = data.getData().getPaper().getPrice();

        intent.putExtra("data", bean);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay:
                payRemedial();
                break;
            case R.id.start_study:
                Intent intent = new Intent(ExaminationActivity.this, TipsBeforeExaminationActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", data.getData().getPaper().getName());
                intent.putExtra("category", data.getData().getPaper().getGrade_category());
                intent.putExtra("subject", data.getData().getPaper().getSubject());
                intent.putExtra("duration", data.getData().getPaper().getDuration());
                intent.putExtra("count", data.getData().getPaper().getTopics_count());

                intent.putExtra("data", (Serializable) data.getData().getPaper().getCategories());
                startActivity(intent);
                break;
        }
    }

    @Subscribe
    public void onEvent(PayResultState code) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

