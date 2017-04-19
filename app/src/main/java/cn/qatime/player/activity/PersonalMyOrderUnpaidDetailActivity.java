package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
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
    private LinearLayout listitem;
    private TextView grade;
    private TextView teacher;
    //    private ImageView status;
    private TextView payprice;
    private int classid;
    DecimalFormat df = new DecimalFormat("#.00");
    private MyOrderBean.DataBean data;
    SimpleDateFormat parseISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_order_unpaid_detail);
        setTitles(getResources().getString(R.string.detail_of_order));
        initView();
        EventBus.getDefault().register(this);
        data = (MyOrderBean.DataBean) getIntent().getSerializableExtra("data");

        if (data != null) {
            setValue();
        }
    }

    private void setValue() {
        //商品信息
        if ("LiveStudio::Course".equals(data.getProduct_type())) {
            classid = data.getProductLIveCourseBean().getId();
            if (StringUtils.isNullOrBlanK(data.getProductLIveCourseBean().getName())) {
                name.setText(getResourceString(R.string.cancel_order_name));
            } else {
                name.setText(data.getProductLIveCourseBean().getName());
            }
            if (StringUtils.isNullOrBlanK(data.getProductLIveCourseBean().getGrade())) {
                grade.setText("直播课/" +getResourceString(R.string.grade));
            } else {
                grade.setText("直播课/" + data.getProductLIveCourseBean().getGrade());
            }
            if (StringUtils.isNullOrBlanK(data.getProductLIveCourseBean().getSubject())) {
                subject.setText(getResourceString(R.string.subject));
            } else {
                subject.setText(data.getProductLIveCourseBean().getSubject());
            }
            if (StringUtils.isNullOrBlanK(data.getProductLIveCourseBean().getTeacher_name())) {
                teacher.setText(getResourceString(R.string.cancel_order_teacher));
            } else {
                teacher.setText(data.getProductLIveCourseBean().getTeacher_name());
            }
            progress.setText(String.format(getResourceString(R.string.lesson_count), data.getProductLIveCourseBean().getPreset_lesson_count()));

        } else if ("LiveStudio::InteractiveCourse".equals(data.getProduct_type())) {
            classid = data.getProduct_interactive_course().getId();
            if (StringUtils.isNullOrBlanK(data.getProduct_interactive_course().getName())) {
                name.setText(getResourceString(R.string.cancel_order_name));
            } else {
                name.setText(data.getProduct_interactive_course().getName());
            }
            if (StringUtils.isNullOrBlanK(data.getProduct_interactive_course().getGrade())) {
                grade.setText("一对一/" +getResourceString(R.string.grade));
            } else {
                grade.setText("一对一/" + data.getProduct_interactive_course().getGrade());
            }
            if (StringUtils.isNullOrBlanK(data.getProduct_interactive_course().getSubject())) {
                subject.setText(getResourceString(R.string.subject));
            } else {
                subject.setText(data.getProduct_interactive_course().getSubject());
            }
            if (StringUtils.isNullOrBlanK(data.getProduct_interactive_course().getTeachers().get(0).getName())) {
                teacher.setText(getResourceString(R.string.cancel_order_teacher));
            } else {
                StringBuffer sp = new StringBuffer();
                sp.append(data.getProduct_interactive_course().getTeachers().get(0).getName());
                if (data.getProduct_interactive_course().getTeachers().size() > 1) {
                    sp.append("...");
                }
                teacher.setText(data.getProduct_interactive_course().getTeachers().get(0).getName());
            }
            progress.setText(String.format(getResourceString(R.string.lesson_count), data.getProduct_interactive_course().getLessons_count()));
        }


        ordernumber.setText(data.getId());
        //创建时间
        Logger.e(data.getCreated_at());
        if (StringUtils.isNullOrBlanK(data.getCreated_at())) {
            buildtime.setText(getResourceString(R.string.is_null));
        } else {
            try {
                buildtime.setText(parse.format(parseISO.parse(data.getCreated_at())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String payType = data.getPay_type();//支付方式
        if (payType.equals("weixin")) {
            paytype.setText(getResourceString(R.string.wexin_payment));
        } else if (payType.equals("alipay")) {
            paytype.setText(getResourceString(R.string.alipay_payment));
        } else {
            paytype.setText(getResourceString(R.string.account_payment));
        }
        payprice.setText("￥" + data.getAmount());
    }

    public void initView() {
//        status = (ImageView) findViewById(R.id.status);
        name = (TextView) findViewById(R.id.name);
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
                if (data.getPay_type().equals("weixin")) {
                    IWXAPI api = WXAPIFactory.createWXAPI(PersonalMyOrderUnpaidDetailActivity.this, null);
                    if (!api.isWXAppInstalled()) {
                        Toast.makeText(PersonalMyOrderUnpaidDetailActivity.this, R.string.wechat_not_installed, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (data.getPay_type().equals("alipay")) {
                    return;
                } else if (data.getPay_type().equals("account")) {
                    if (Double.valueOf(data.getAmount()) > Double.valueOf(BaseApplication.getCashAccount().getData().getBalance())) {
                        Toast.makeText(PersonalMyOrderUnpaidDetailActivity.this, R.string.amount_not_enough, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Intent intent = new Intent(PersonalMyOrderUnpaidDetailActivity.this, OrderPayActivity.class);
                if (data.getPay_type().equals("weixin")) {
                    intent.putExtra("data", data.getApp_pay_params());
                } else if (data.getPay_type().equals("alipay")) {
                    intent.putExtra("data", data.getApp_pay_str());
                }
                intent.putExtra("id", data.getId());
                intent.putExtra("time", data.getCreated_at());
                intent.putExtra("price", data.getAmount());
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
        text.setText(R.string.confirm_cancel_order);
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
        setResult(Constant.RESPONSE);//支付成功刷新订单
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

