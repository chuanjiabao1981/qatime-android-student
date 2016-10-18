package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.MyOrderBean;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;


public class PersonalMyOrderUnpaidDetailActivity extends BaseActivity {

    private TextView subject;
    private TextView progress;
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
    private TextView status;
    private TextView payprice;
    private int classid;
    DecimalFormat df = new DecimalFormat("#.00");
    private MyOrderBean.Data data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_order_unpaid_detail);
        setTitle(getResources().getString(R.string.detail_of_order));
        initView();
        EventBus.getDefault().register(this);
        data = (MyOrderBean.Data) getIntent().getSerializableExtra("data");

        if (data != null) {
            setValue();
        }
    }

    private void setValue() {
        classid = data.getProduct().getId();
        Glide.with(PersonalMyOrderUnpaidDetailActivity.this).load(data.getProduct().getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().into(image);
        if (data.getStatus().equals("unpaid")) {//等待付款
            status.setText(getResources().getString(R.string.waiting_for_payment));
        } else {//空
            status.setText("        ");
        }
        if (StringUtils.isNullOrBlanK(data.getProduct().getName())) {
            name.setText(getResourceString(R.string.cancel_order_name));
        } else {
            name.setText(data.getProduct().getName());
        }
        if (StringUtils.isNullOrBlanK(data.getProduct().getGrade())) {
            grade.setText(getResourceString(R.string.grade));
        } else {
            grade.setText(data.getProduct().getGrade());
        }
        if (StringUtils.isNullOrBlanK(data.getProduct().getSubject())) {
            subject.setText(getResourceString(R.string.subject));
        } else {
            subject.setText(data.getProduct().getSubject());
        }
        if (StringUtils.isNullOrBlanK(data.getProduct().getTeacher_name())) {
            teacher.setText(getResourceString(R.string.cancel_order_teacher));
        } else {
            teacher.setText(data.getProduct().getTeacher_name());
        }

        ordernumber.setText(data.getId());
        //创建时间
        Logger.e(data.getCreated_at());
        if (StringUtils.isNullOrBlanK(data.getCreated_at())) {
            buildtime.setText(getResourceString(R.string.is_null));
        } else {
            buildtime.setText(data.getCreated_at());
        }
        String payType = data.getPay_type();//支付方式
        if (payType.equals("weixin")) {
            paytype.setText(getResourceString(R.string.wechat_payment));
        } else {
            paytype.setText(getResourceString(R.string.alipay_payment));
        }
        progress.setText(data.getProduct().getCompleted_lesson_count() + "/" + data.getProduct().getPreset_lesson_count());
        String price = df.format(data.getProduct().getPrice());
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
                //TODO: 2016/9/1 付款
                Intent intent = new Intent(PersonalMyOrderUnpaidDetailActivity.this, OrderPayActivity.class);
                if (data.getPay_type().equals("weixin")) {
                    intent.putExtra("data", data.getApp_pay_params());
                } else if (data.getPay_type().equals("alipay")) {
                    intent.putExtra("data", data.getApp_pay_str());
                }
                intent.putExtra("id", data.getId());
                intent.putExtra("time", data.getCreated_at());
                intent.putExtra("price", data.getProduct().getPrice());
                intent.putExtra("type", data.getPay_type());
                startActivity(intent);
            }
        });
        cancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog(data.getId());
            }
        });

    }

    protected void dialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_cancel_or_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText("是否确认取消此订单？");
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelOrder(id);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
//        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
//        attributes.width= ScreenUtils.getScreenWidth(getActivity())- DensityUtils.dp2px(getActivity(),20)*2;
//        alertDialog.getWindow().setAttributes(attributes);
    }

    private void CancelOrder(String id) {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.urlPaylist + "/" + id + "/cancel", null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Toast.makeText(PersonalMyOrderUnpaidDetailActivity.this, getResourceString(R.string.order_cancel_success), Toast.LENGTH_SHORT).show();
                        setResult(Constant.RESPONSE);//取消成功刷新订单
                        finish();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(PersonalMyOrderUnpaidDetailActivity.this, getResourceString(R.string.order_cancel_failed), Toast.LENGTH_SHORT).show();
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
    @Subscribe
    public void onEvent(PayResultState state) {
        Intent intent = new Intent(this,OrderPayResultActivity.class);
        intent.putExtra("orderId",ordernumber.getText().toString());
        intent.putExtra("price",payprice.getText().toString());
        intent.putExtra("state",state);
        startActivity(intent);
        setResult(Constant.RESPONSE);//支付成功刷新订单
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

