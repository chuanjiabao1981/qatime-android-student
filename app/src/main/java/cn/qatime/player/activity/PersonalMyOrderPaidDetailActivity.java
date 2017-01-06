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
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.OrderDetailBean;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;


public class PersonalMyOrderPaidDetailActivity extends BaseActivity {

    private TextView subject;
    private TextView progress;
    private TextView ordernumber;
    private TextView buildtime;
    private TextView paytype;
    private TextView paytime;
    private LinearLayout listitem;
    private TextView name;
    private TextView grade;
    private TextView teacher;
    private TextView Refund;
    private ImageView status;
    private TextView payprice;
    private int classid;
    DecimalFormat df = new DecimalFormat("#.00");
    SimpleDateFormat parseISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private OrderDetailBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_order_paid_detail);
        setTitle(getResources().getString(R.string.detail_of_order));
        initView();
        data = (OrderDetailBean) getIntent().getSerializableExtra("data");
        if (data != null) {
            setValue();
        }
    }

    private void setValue() {
        classid = data.id;
        if (StringUtils.isNullOrBlanK(data.name)) {
            name.setText(getResourceString(R.string.cancel_order_name));
        } else {
            name.setText(data.name);
        }
        if (StringUtils.isNullOrBlanK(data.grade)) {
            grade.setText(getResourceString(R.string.grade));
        } else {
            grade.setText(data.grade);
        }
        if (StringUtils.isNullOrBlanK(data.subject)) {
            subject.setText(getResourceString(R.string.subject));
        } else {
            subject.setText(data.subject);
        }
        if (StringUtils.isNullOrBlanK(data.teacher)) {
            teacher.setText(getResourceString(R.string.cancel_order_teacher));
        } else {
            teacher.setText(data.teacher);
        }
        ordernumber.setText(getIntent().getStringExtra("id"));
//创建时间
        if (StringUtils.isNullOrBlanK(getIntent().getStringExtra("created_at"))) {
            buildtime.setText(getResourceString(R.string.is_null));
        } else {
            try {
                buildtime.setText(parse.format(parseISO.parse(getIntent().getStringExtra("created_at"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //支付时间
        if (StringUtils.isNullOrBlanK(getIntent().getStringExtra("pay_at"))) {
            paytime.setText(getResourceString(R.string.is_null));
        } else {
            try {
                paytime.setText(parse.format(parseISO.parse(getIntent().getStringExtra("pay_at"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String payType = getIntent().getStringExtra("payType");//支付方式
        if (payType.equals("weixin")) {
            paytype.setText(getResourceString(R.string.wechat_payment));
        } else if (payType.equals("alipay")) {
            paytype.setText(getResourceString(R.string.alipay_payment));
        } else {
            paytype.setText(getResourceString(R.string.account_payment));
        }

        if (data.status.equals("refunding")) {//退款中
            status.setImageResource(R.mipmap.refunding);
            Refund.setText("取消退款");
        } else {
            Refund.setText("申请退款");
            if (data.status.equals("paid")) {//正在交易
                status.setImageResource(R.mipmap.paying);
            } else if (data.status.equals("shipped")) {//正在交易
                status.setImageResource(R.mipmap.paying);
            } else {//交易完成
                status.setImageResource(R.mipmap.complete_pay);
            }
        }
        progress.setText("共" + data.Preset_lesson_count + "课");
        PersonalMyOrderPaidDetailActivity.this.payprice.setText(data.amount);
        payprice.setText("￥" + data.amount + " ");

        Refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("refunding".equals(data.status)) {
                    //取消退款
                    dialog();
                } else {
                    //获取退款信息
                    applyRefund();
                }
            }
        });
    }

    public void initView() {
        name = (TextView) findViewById(R.id.name);
        subject = (TextView) findViewById(R.id.subject);
        grade = (TextView) findViewById(R.id.grade);
        status = (ImageView) findViewById(R.id.status);
        teacher = (TextView) findViewById(R.id.teacher);
        Refund = (TextView) findViewById(R.id.button_refund);
        progress = (TextView) findViewById(R.id.progress);//进度
        ordernumber = (TextView) findViewById(R.id.order_number);//订单编号
        buildtime = (TextView) findViewById(R.id.build_time);//创建时间
        paytime = (TextView) findViewById(R.id.pay_time);//支付时间
        paytype = (TextView) findViewById(R.id.pay_type);//支付方式
        listitem = (LinearLayout) findViewById(R.id.list_item);//内详情点击

        payprice = (TextView) findViewById(R.id.pay_price);//支付价格
        listitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalMyOrderPaidDetailActivity.this, RemedialClassDetailActivity.class);
                intent.putExtra("id", classid);
                intent.putExtra("page", 0);
                startActivity(intent);
            }
        });
    }

    private void applyRefund() {
        Map<String, String> map = new HashMap<>();
        map.put("order_id", ordernumber.getText().toString());
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getUserId() + "/refunds/info", map), null,
                new VolleyListener(PersonalMyOrderPaidDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Intent intent = new Intent(PersonalMyOrderPaidDetailActivity.this, ApplyRefundActivity.class);
                        intent.putExtra("response", response.toString());
                        intent.putExtra("order_id", ordernumber.getText().toString());
                        intent.putExtra("name", data.name);
                        intent.putExtra("preset_lesson_count", data.Preset_lesson_count);
                        intent.putExtra("completed_lesson_count", data.Completed_lesson_count);
                        startActivityForResult(intent, Constant.REQUEST);
                    }

                    @Override
                    protected void onError(JSONObject response) {
//                        Toast.makeText(PersonalMyOrderPaidDetailActivity.this, getResourceString(R.string.order_cancel_failed), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                Logger.e(volleyError.getMessage());
            }
        });
        addToRequestQueue(request);

    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalMyOrderPaidDetailActivity.this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(PersonalMyOrderPaidDetailActivity.this, R.layout.dialog_cancel_or_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText("是否确认取消退款");
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
                cancelRefund();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
//        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
//        attributes.width= ScreenUtils.getScreenWidth(PersonalMyOrderPaidDetailActivity.this)- DensityUtils.dp2px(PersonalMyOrderPaidDetailActivity.this,20)*2;
//        alertDialog.getWindow().setAttributes(attributes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==Constant.REQUEST&&resultCode==Constant.RESPONSE) {
            setResult(Constant.RESPONSE);
            finish();
        }
    }

    private void cancelRefund() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.urlpayment + BaseApplication.getUserId() + "/refunds/" + ordernumber.getText().toString() + "/cancel", null,
                new VolleyListener(PersonalMyOrderPaidDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Toast.makeText(PersonalMyOrderPaidDetailActivity.this, "取消退款申请成功", Toast.LENGTH_SHORT).show();
                        setResult(Constant.RESPONSE);
                        finish();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(PersonalMyOrderPaidDetailActivity.this, "取消退款申请失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                Logger.e(volleyError.getMessage());
            }
        });
        addToRequestQueue(request);
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

