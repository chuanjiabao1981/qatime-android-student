package cn.qatime.player.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;


import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


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
    private LinearLayout listitem;
    private TextView grade;
    private TextView teacher;
    private TextView classnumber;
    private TextView status;
    private int id;
    private TextView price;
    private TextView payprice;
    private int priceNumber = 0;
    //    yyyy-MM-dd'T'HH:mm:ss.SSSZ  yyyy年MM月dd日 HH时mm分ss秒 E
    private int classid;
    DecimalFormat df = new DecimalFormat("#.00");

//    SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+xx:oo");
//    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd THH:mm:ss-www-xx-oo");

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
        classid = data.id;
        Glide.with(PersonalMyOrderUnpaidDetailActivity.this).load(data.image).placeholder(R.mipmap.photo).centerCrop().crossFade().into(image);
        if (data.status.equals("unpaid")) {//等待付款
            status.setText(getResources().getString(R.string.waiting_for_payment));
        } else {//空
            status.setText("        ");
        }
        if (StringUtils.isNullOrBlanK(data.name)) {
            name.setText("    ");
        } else {
            name.setText(data.name);
        }
        if (StringUtils.isNullOrBlanK(data.grade)) {
            grade.setText("年级");
        } else {
            grade.setText(data.grade);
        }
        if (StringUtils.isNullOrBlanK(data.subject)) {
            subject.setText("科目");
        } else {
            subject.setText(data.subject);
        }
        if (StringUtils.isNullOrBlanK(data.teacher)) {
            teacher.setText("老师");
        } else {
            teacher.setText(data.teacher);
        }

        ordernumber.setText(getIntent().getStringExtra("id"));
        //创建时间
        Logger.e(getIntent().getStringExtra("created_at"));
        if (StringUtils.isNullOrBlanK(getIntent().getStringExtra("created_at"))) {
            buildtime.setText("为空");
        } else {
            try {
                DateFormat format = new SimpleDateFormat("EEE MMM dd  yyyy HH:mm:ss z");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                buildtime.setText(format.parse((getIntent().getStringExtra("created_at"))).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        int payType = getIntent().getIntExtra("payType", 0);//支付方式
        if (payType == 1) {
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
        payprice.setText("￥" + price + " ");
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
        listitem = (LinearLayout) findViewById(R.id.list_item);//内详情点击
        cancelorder = (TextView) findViewById(R.id.cancel_order);
        listitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalMyOrderUnpaidDetailActivity.this, RemedialClassDetailActivity.class);
                intent.putExtra("id", classid);
                intent.putExtra("page", 0);
                startActivity(intent);
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/9/1 付款
            }
        });
        cancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(getIntent().getStringExtra("id"));
            }
        });

    }

    protected void dialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalMyOrderUnpaidDetailActivity.this);
        builder.setMessage("确认取消订单吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                CancelOrder(id);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void CancelOrder(String id) {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.urlPaylist + "/" + id + "/cancel", null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Toast.makeText(PersonalMyOrderUnpaidDetailActivity.this, "订单已成功取消", Toast.LENGTH_SHORT).show();
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

