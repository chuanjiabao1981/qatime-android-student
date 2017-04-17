package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.CashAccountBean;
import cn.qatime.player.bean.CouponVerifyBean;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.AppPayParamsBean;
import libraryextra.bean.OrderConfirmBean;
import libraryextra.bean.OrderPayBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class OrderConfirmActivity extends BaseActivity implements View.OnClickListener {
    TextView name;
    TextView project;
    TextView grade;
    TextView teacher;
    TextView classnumber;
    TextView price;
    TextView payprice;
    private Button pay;
    private ImageView wechatPay;
    private int id;
    private String payType = "weixin";
    private float priceNumber = 0;
    DecimalFormat df = new DecimalFormat("#.00");
    private AlertDialog alertDialog;
    private ImageView aliPay;
    private ImageView account;
    private View alipayLayout;
    private View wechatLayout;
    private View accountLayout;
    private View couponLayout;
    private View confirmCoupon;
    private EditText couponCode;
    private String coupon;
    private LinearLayout couponPriceLayout;
    private TextView couponPrice;
    private TextView balance;
    private String courseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        setTitles(getResources().getString(R.string.order_confirm));
        coupon = getIntent().getStringExtra("coupon");
        courseType = getIntent().getStringExtra("courseType");


        initView();

        EventBus.getDefault().register(this);

        OrderPayBean data = (OrderPayBean) getIntent().getSerializableExtra("data");
        id = getIntent().getIntExtra("id", 0);
        if (data != null) {
            setValue(data);
            priceNumber = data.current_price;
        }
        pay.setOnClickListener(this);
    }


    private void setValue(OrderPayBean data) {

        name.setText(data.name);
        project.setText(data.subject);
        grade.setText(data.grade);
        classnumber.setText("共" + data.classnumber + "课");
        teacher.setText(data.teacher);

        String price = df.format(data.current_price);
        if (price.startsWith(".")) {
            price = "0" + price;
        }
        OrderConfirmActivity.this.price.setText(price + "元");
        payprice.setText(" " + price + " ");
        if (!StringUtils.isNullOrBlanK(coupon)) {
            couponLayout.setVisibility(View.GONE);
            couponCode.setText(coupon);
            couponCode.clearFocus();
            verifyCoupon();
        }

        CashAccountBean cashAccount = BaseApplication.getCashAccount();
        if (cashAccount != null && cashAccount.getData() != null) {
            String currentBalance = cashAccount.getData().getBalance();
            if (currentBalance.startsWith(".")) {
                currentBalance = "0" + currentBalance;
            }
            balance.setText("当前余额" + currentBalance + "元");
        }
    }

    @Override
    public void onClick(View v) {
        if (payType.equals("weixin")) {
            IWXAPI api = WXAPIFactory.createWXAPI(this, null);
            if (!api.isWXAppInstalled()) {
                Toast.makeText(this, R.string.wechat_not_installed, Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (payType.equals("alipay")) {
            return;
        } else if (payType.equals("account")) {
            if (priceNumber > Double.valueOf(BaseApplication.getCashAccount().getData().getBalance())) {
                Toast.makeText(this, R.string.amount_not_enough, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        pay.setEnabled(false);
        Map<String, String> map = new HashMap<>();
        map.put("pay_type", payType);
        if (!StringUtils.isNullOrBlanK(coupon)) {
            map.put("coupon_code", coupon);
        }
        String url = UrlUtils.getUrl(UrlUtils.urlCourses + id + "/orders", map);
        if (!StringUtils.isNullOrBlanK(courseType)&&"interact".equals(courseType)) {
            url = UrlUtils.getUrl(UrlUtils.urlInteractCourses + id + "/orders", map);
        }else if(!StringUtils.isNullOrBlanK(courseType)&&"video".equals(courseType)){
            url = UrlUtils.getUrl(UrlUtils.urlVideoCourses + id + "/orders", map);
        }

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, url, null,
                new VolleyListener(OrderConfirmActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        OrderConfirmBean confirmBean = JsonUtils.objectFromJson(response.toString(), OrderConfirmBean.class);
                        if (payType.equals("weixin")) {
                            if (confirmBean != null) {
                                Intent intent = new Intent(OrderConfirmActivity.this, OrderPayActivity.class);
                                intent.putExtra("price", confirmBean.getData().getAmount());
                                intent.putExtra("id", confirmBean.getData().getId());
                                intent.putExtra("time", confirmBean.getData().getCreated_at());
                                intent.putExtra("type", payType);
                                AppPayParamsBean app_pay_params = confirmBean.getData().getApp_pay_params();
                                intent.putExtra("data", app_pay_params);
                                startActivity(intent);
                                pay.setEnabled(true);
                            } else {
                                dialog();
                            }
                        } else if (payType.equals("alipay")) {
                            if (confirmBean != null) {
                                Intent intent = new Intent(OrderConfirmActivity.this, OrderPayActivity.class);
                                intent.putExtra("price", confirmBean.getData().getAmount());
                                intent.putExtra("id", confirmBean.getData().getId());
                                intent.putExtra("time", confirmBean.getData().getCreated_at());
                                intent.putExtra("type", payType);
                                String app_pay_params = confirmBean.getData().getApp_pay_str();
                                intent.putExtra("data", app_pay_params);
                                startActivity(intent);
                                pay.setEnabled(true);
                            } else {
                                dialog();
                            }
                        } else if (payType.equals("account")) {
                            //余额支付成功  status---failed交易失败  shipped交易成功
//                            try {
                            Intent intent = new Intent(OrderConfirmActivity.this, OrderPayActivity.class);
                            intent.putExtra("price", confirmBean.getData().getAmount());
                            intent.putExtra("id", confirmBean.getData().getId());
                            intent.putExtra("time", confirmBean.getData().getCreated_at());
                            intent.putExtra("data", name.getText().toString());
                            intent.putExtra("type", payType);
                            startActivity(intent);
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
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText(R.string.create_order_error);
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
        project = (TextView) findViewById(R.id.project);
        grade = (TextView) findViewById(R.id.grade);
        teacher = (TextView) findViewById(R.id.teacher);
        classnumber = (TextView) findViewById(R.id.class_number);
        wechatLayout = findViewById(R.id.wechat_layout);
        alipayLayout = findViewById(R.id.alipay_layout);
        accountLayout = findViewById(R.id.account_layout);
        balance = (TextView) findViewById(R.id.balance);

        couponLayout = findViewById(R.id.coupon_layout);
        couponLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponLayout.setVisibility(View.GONE);
            }
        });
        couponCode = (EditText) findViewById(R.id.coupon_code);
        confirmCoupon = findViewById(R.id.confirm_coupon);
        confirmCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isNullOrBlanK(couponCode.getText().toString())) {
                    coupon = couponCode.getText().toString();
                    verifyCoupon();
                }
            }
        });

        couponPriceLayout = (LinearLayout) findViewById(R.id.coupon_price_layout);
        couponPrice = (TextView) findViewById(R.id.coupon_price);
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
                aliPay.setImageResource(R.mipmap.pay_status_selected);
                wechatPay.setImageResource(R.mipmap.pay_status_normal);
                account.setImageResource(R.mipmap.pay_status_normal);
            }
        });
        wechatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "weixin";
                wechatPay.setImageResource(R.mipmap.pay_status_selected);
                aliPay.setImageResource(R.mipmap.pay_status_normal);
                account.setImageResource(R.mipmap.pay_status_normal);
            }
        });
        accountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payType = "account";
                account.setImageResource(R.mipmap.pay_status_selected);
                aliPay.setImageResource(R.mipmap.pay_status_normal);
                wechatPay.setImageResource(R.mipmap.pay_status_normal);
            }
        });
    }

    /**
     * 验证优惠吗
     */
    private void verifyCoupon() {
//        "http://192.168.1.107:3000/api/v1/payment/coupons/"
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.urlCoupon + coupon + "/verify", null,
                new VolleyListener(OrderConfirmActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        CouponVerifyBean data = JsonUtils.objectFromJson(response.toString(), CouponVerifyBean.class);
                        if (data != null && data.getData() != null) {
                            couponPriceLayout.setVisibility(View.VISIBLE);
                            couponPrice.setText(data.getData().getPrice());
                            double couponprice = priceNumber - Double.valueOf(data.getData().getPrice());
                            payprice.setText(" " + couponprice + " ");
                        }

                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(OrderConfirmActivity.this, "优惠码不正确", Toast.LENGTH_SHORT).show();
                        couponPriceLayout.setVisibility(View.GONE);
                        String price = df.format(priceNumber);
                        if (price.startsWith(".")) {
                            price = "0" + price;
                        }
                        OrderConfirmActivity.this.price.setText(getResourceString(R.string.order_price) + price);
                        payprice.setText(" " + price + " ");

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }

                , new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    private String getStatus(String status) {
        if (status == null) {
            return getString(R.string.recruiting);
        }
        if (status.equals("published")) {
            return getString(R.string.recruiting);
        } else if (status.equals("init")) {
            return getString(R.string.recruiting);
        } else if (status.equals("teaching")) {
            return getString(R.string.teaching);
        } else if (status.equals(Constant.CourseStatus.completed) || status.equals(Constant.CourseStatus.finished)) {//未开始
            return getString(R.string.completed);
        }
        return getString(R.string.recruiting);
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
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
