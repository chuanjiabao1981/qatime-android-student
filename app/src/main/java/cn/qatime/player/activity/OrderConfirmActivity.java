package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.AppPayParamsBean;
import libraryextra.bean.OrderConfirmBean;
import libraryextra.bean.OrderPayBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class OrderConfirmActivity extends BaseActivity implements View.OnClickListener {
    TextView name;
    ImageView image;
    TextView project;
    TextView grade;
    TextView teacher;
    TextView classnumber;
    TextView classstarttime;
    TextView classendtime;
    //    TextView status;
    TextView price;
    TextView payprice;
    private Button pay;
    private ImageView wechatPay;
    private int id;
    private String payType = "weixin";
    private int priceNumber = 0;
    DecimalFormat df = new DecimalFormat("#.00");
    private SimpleDateFormat parse1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat parse2 = new SimpleDateFormat("yyyy-MM-dd");
    private AlertDialog alertDialog;
    private ImageView aliPay;
    private ImageView account;
    private View alipayLayout;
    private View wechatLayout;
    private View accountLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        setTitle(getResources().getString(R.string.order_confirm));
        initView();

        EventBus.getDefault().register(this);

        OrderPayBean data = (OrderPayBean) getIntent().getSerializableExtra("data");
        id = getIntent().getIntExtra("id", 0);
        if (data != null) {
            setValue(data);
            priceNumber = data.price;
//            initData(data.getData().getId());
        }
        pay.setOnClickListener(this);
    }


    private void setValue(OrderPayBean data) {
        Glide.with(OrderConfirmActivity.this).load(data.image).placeholder(R.mipmap.photo).fitCenter().crossFade().into(image);

        name.setText(data.name);
        project.setText(getResources().getString(R.string.subject_type) + data.subject);
        grade.setText(getResources().getString(R.string.grade_type) + data.grade);
        classnumber.setText(getResources().getString(R.string.total_class_hours) + data.classnumber);
        teacher.setText(getResources().getString(R.string.teacher) + data.teacher);
        try {
            classstarttime.setText(getResources().getString(R.string.class_start_time) + parse2.format(parse1.parse(data.classstarttime)));
            classendtime.setText(getResources().getString(R.string.class_end_time) + parse2.format(parse1.parse(data.classendtime)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        if (data.status.equals("preview")) {
//            status.setText(getResources().getString(R.string.status_preview));
//        } else if (data.status.equals("teaching")) {
//            status.setText(getResources().getString(R.string.status_teaching));
//        } else {
//            status.setText(getResources().getString(R.string.status_over));
//        }
        String price = df.format(data.price);
        if (price.startsWith(".")) {
            price = "0" + price;
        }
        OrderConfirmActivity.this.price.setText(getResourceString(R.string.order_price) + price);
        payprice.setText(" " + price + " ");

    }

    @Override
    public void onClick(View v) {
        // TODO: 2016/10/8 余额支付验证
        pay.setEnabled(false);
        Map<String, String> map = new HashMap<>();
        map.put("pay_type", payType);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlPayPrepare + id + "/orders", map), null,
                new VolleyListener(OrderConfirmActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        OrderConfirmBean data = JsonUtils.objectFromJson(response.toString(), OrderConfirmBean.class);
                        if (payType.equals("weixin")) {
                            if (data != null) {
                                Intent intent = new Intent(OrderConfirmActivity.this, OrderPayActivity.class);
                                intent.putExtra("price", priceNumber);
                                intent.putExtra("id", data.getData().getId());
                                intent.putExtra("time", data.getData().getCreated_at());
                                intent.putExtra("type", payType);
                                AppPayParamsBean app_pay_params = data.getData().getApp_pay_params();
                                intent.putExtra("data", app_pay_params);
                                startActivity(intent);
                                pay.setEnabled(true);
                            } else {
                                dialog();
                            }
                        } else if (payType.equals("alipay")) {
                            if (data != null) {
                                Intent intent = new Intent(OrderConfirmActivity.this, OrderPayActivity.class);
                                intent.putExtra("price", priceNumber);
                                intent.putExtra("id", data.getData().getId());
                                intent.putExtra("time", data.getData().getCreated_at());
                                intent.putExtra("type", payType);
                                String app_pay_params = data.getData().getApp_pay_str();
                                intent.putExtra("data", app_pay_params);
                                startActivity(intent);
                                pay.setEnabled(true);
                            } else {
                                dialog();
                            }
                        } else if (payType.equals("account")) {
                            //余额支付成功  status---failed交易失败  shipped交易成功
                            // TODO: 2016/10/8 余额支付  订单?  校验?
                            try {
                                if (response.getJSONObject("data").getString("status").equals("shipped")) {
                                    EventBus.getDefault().post(PayResultState.SUCCESS);
                                    finish();
                                } else {
                                    Toast.makeText(OrderConfirmActivity.this, "余额不足", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        pay.setEnabled(true);
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        dialog();
                        pay.setEnabled(true);
                    }

                    @Override
                    protected void onTokenOut() {
                        pay.setEnabled(true);
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                Toast.makeText(OrderConfirmActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
        addToRequestQueue(request);


    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_confirm, null);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
//        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
//        attributes.width= ScreenUtils.getScreenWidth(getApplicationContext())- DensityUtils.dp2px(getApplicationContext(),20)*2;
//        alertDialog.getWindow().setAttributes(attributes);
    }

    public void initView() {

        name = (TextView) findViewById(R.id.name);
        image = (ImageView) findViewById(R.id.image);
        project = (TextView) findViewById(R.id.project);
        grade = (TextView) findViewById(R.id.grade);
        teacher = (TextView) findViewById(R.id.teacher);
        classnumber = (TextView) findViewById(R.id.class_number);
        classstarttime = (TextView) findViewById(R.id.class_start_time);
        classendtime = (TextView) findViewById(R.id.class_end_time);
//        status = (TextView) findViewById(R.id.status);
        wechatLayout = findViewById(R.id.wechat_layout);
        alipayLayout = findViewById(R.id.alipay_layout);
        accountLayout = findViewById(R.id.account_layout);
        wechatPay = (ImageView) findViewById(R.id.wechat_pay);
        aliPay = (ImageView) findViewById(R.id.alipay);
        account = (ImageView) findViewById(R.id.account);
        price = (TextView) findViewById(R.id.price);
        payprice = (TextView) findViewById(R.id.pay_price);
        pay = (Button) findViewById(R.id.pay);
        alipayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "alipay";
                aliPay.setImageResource(R.drawable.shape_select_circle_select);
                wechatPay.setImageResource(R.drawable.shape_select_circle_normal);
                account.setImageResource(R.drawable.shape_select_circle_normal);

                //TODO 集成完支付宝后，去掉下面这段
                Toast.makeText(OrderConfirmActivity.this, getResourceString(R.string.not_support_alipay), Toast.LENGTH_SHORT).show();
                wechatPay.setImageResource(R.drawable.shape_select_circle_select);
                aliPay.setImageResource(R.drawable.shape_select_circle_normal);
                payType = "weixin";
            }
        });
        wechatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "weixin";
                wechatPay.setImageResource(R.drawable.shape_select_circle_select);
                aliPay.setImageResource(R.drawable.shape_select_circle_normal);
                account.setImageResource(R.drawable.shape_select_circle_normal);
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "account";
                account.setImageResource(R.drawable.shape_select_circle_select);
                aliPay.setImageResource(R.drawable.shape_select_circle_normal);
                wechatPay.setImageResource(R.drawable.shape_select_circle_normal);
            }
        });
    }

    @Subscribe
    public void onEvent(PayResultState code) {
//        if (!StringUtils.isNullOrBlanK(event) && event.equals("pay_success")) {
//
//            finish();
//        }
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
