package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;


import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.OrderDetailBean;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;


public class PersonalMyOrderUnpaidDetailActivity extends BaseActivity {

    private TextView subject;
    private TextView progress;
    private TextView time;
    private TextView ordernumber;
    private TextView buildtime;
    private TextView paytype;
    private TextView pay;
    private TextView cancelorder;
    private TextView name;
    private ImageView image;
    private TextView project;
    private TextView grade;
    private TextView teacher;
    private TextView classnumber;
    private TextView status;
    private int id;
    private TextView price;
    private TextView payprice;
    private int priceNumber = 0;
    private int classid;
    DecimalFormat df = new DecimalFormat("#.00");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_order_unpaid_detail);
        setTitle(getResources().getString(R.string.detail_of_order));
        initView();
        OrderDetailBean data = (OrderDetailBean) getIntent().getSerializableExtra("data");
        if (data != null) {
            setValue(data);
        }
//        pay.setOnClickListener(this);
    }

    private void setValue(OrderDetailBean data) {
        Glide.with(PersonalMyOrderUnpaidDetailActivity.this).load(data.image).placeholder(R.mipmap.photo).fitCenter().crossFade().into(image);
        if (data.status.equals("unpaid")) {//等待付款
            status.setText(getResources().getString(R.string.waiting_for_payment));
        }
        else {//空
            status.setText("        ");
        }
        if (StringUtils.isNullOrBlanK(data.name)) {
            name.setText("    ");
        } else {
            name.setText(data.name);
        }
        if (StringUtils.isNullOrBlanK(data.grade)) {
            grade.setText("    ");
        } else {
            grade.setText(data.grade);
        }
        if (StringUtils.isNullOrBlanK(data.subject)) {
            subject.setText("    ");
        } else {
            subject.setText(data.subject);
        }
        if (StringUtils.isNullOrBlanK(data.teacher)) {
            teacher.setText("    ");
        } else {
            teacher.setText(data.teacher);
        }

        ordernumber.setText(getIntent().getStringExtra("id"));
        if (StringUtils.isNullOrBlanK(getIntent().getStringExtra("created_at"))) {
            buildtime.setText("为空");
        }//创建时间
        else {
            buildtime.setText((getIntent().getStringExtra("created_at")));

        }
        String payType = getIntent().getStringExtra("payType");//支付方式
        if (payType.equals("1")) {
            paytype.setText("微信支付");
        } else {
            paytype.setText("支付宝支付");
        }
        progress.setText(data.Completed_lesson_count + "/" + data.Preset_lesson_count);
        String price = df.format(data.price);
        if (price.startsWith(".")) {
            price = "0" + price;
        }
        PersonalMyOrderUnpaidDetailActivity.this.payprice.setText(price);
        payprice.setText(" " + price + " ");
    }

    public void initView() {
        status = (TextView) findViewById(R.id.status);
        name = (TextView) findViewById(R.id.name);
        image = (ImageView) findViewById(R.id.image);
        subject = (TextView) findViewById(R.id.subject);
        grade = (TextView) findViewById(R.id.grade);
        teacher = (TextView) findViewById(R.id.teacher);
        progress = (TextView) findViewById(R.id.progress);//进度
        ordernumber = (TextView) findViewById(R.id.order_number);//订单编号
        buildtime = (TextView) findViewById(R.id.build_time);//创建时间
//        paytime = (TextView) findViewById(R.id.pay_time);//支付时间
        paytype = (TextView) findViewById(R.id.pay_type);//支付方式
        payprice = (TextView) findViewById(R.id.pay_price);//支付价格
        pay = (TextView) findViewById(R.id.pay);
        cancelorder = (TextView) findViewById(R.id.cancel_order);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/9/1 付款
            }
        });
        cancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDataCancelOrder(getIntent().getStringExtra("id"));
            }
        });

    }

    private void initDataCancelOrder(String id) {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PATCH, UrlUtils.urlPaylist + "/" + id + "/cancel", null,
//            http://testing.qatime.cn/api/v1/payment/orders/201608311659310128/cancel

                new VolleyListener(PersonalMyOrderUnpaidDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Toast.makeText(PersonalMyOrderUnpaidDetailActivity.this, "订单取消成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(PersonalMyOrderUnpaidDetailActivity.this, "取消订单失败，请稍后再试", Toast.LENGTH_SHORT).show();
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

}

