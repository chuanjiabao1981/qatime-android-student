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
import cn.qatime.player.bean.MyOrderBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;


public class   PersonalMyOrderPaidDetailActivity extends BaseActivity {

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
    private TextView refund;
    private ImageView status;
    private TextView payprice;
    private int classid;
    DecimalFormat df = new DecimalFormat("#.00");
    SimpleDateFormat parseISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private MyOrderBean.DataBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_order_paid_detail);
        setTitles(getResources().getString(R.string.detail_of_order));
        initView();
        data = (MyOrderBean.DataBean) getIntent().getSerializableExtra("data");
        if (data != null) {
            setValue();
        }
    }

    private void setValue() {
//        商品信息
        if ("LiveStudio::Course".equals(data.getProduct_type())) {
            classid = data.getProduct().getId();
            if (StringUtils.isNullOrBlanK(data.getProduct().getName())) {
                name.setText(getResourceString(R.string.cancel_order_name));
            } else {
                name.setText(data.getProduct().getName());
            }
            if (StringUtils.isNullOrBlanK(data.getProduct().getGrade())) {
                grade.setText("直播课/" + getResourceString(R.string.grade));
            } else {
                grade.setText("直播课/" + data.getProduct().getGrade());
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
            progress.setText(String.format(getString(R.string.lesson_count), data.getProduct().getPreset_lesson_count()));
        } else if ("LiveStudio::InteractiveCourse".equals(data.getProduct_type())) {
            classid = data.getProduct_interactive_course().getId();
            if (StringUtils.isNullOrBlanK(data.getProduct_interactive_course().getName())) {
                name.setText(getResourceString(R.string.cancel_order_name));
            } else {
                name.setText(data.getProduct_interactive_course().getName());
            }
            if (StringUtils.isNullOrBlanK(data.getProduct_interactive_course().getGrade())) {
                grade.setText("一对一/" + getResourceString(R.string.grade));
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
            progress.setText(String.format(getString(R.string.lesson_count), data.getProduct_interactive_course().getLessons_count()));
        } else if ("LiveStudio::VideoCourse".equals(data.getProduct_type())) {
            classid = data.getProduct_video_course().getId();
            if (StringUtils.isNullOrBlanK(data.getProduct_video_course().getName())) {
                name.setText(getResourceString(R.string.cancel_order_name));
            } else {
                name.setText(data.getProduct_video_course().getName());
            }
            if (StringUtils.isNullOrBlanK(data.getProduct_video_course().getGrade())) {
                grade.setText("视频课/" + getResourceString(R.string.grade));
            } else {
                grade.setText("视频课/" + data.getProduct_video_course().getGrade());
            }
            if (StringUtils.isNullOrBlanK(data.getProduct_video_course().getSubject())) {
                subject.setText(getResourceString(R.string.subject));
            } else {
                subject.setText(data.getProduct_video_course().getSubject());
            }
            if (StringUtils.isNullOrBlanK(data.getProduct_video_course().getTeacher().getName())) {
                teacher.setText(getResourceString(R.string.cancel_order_teacher));
            } else {
                teacher.setText(data.getProduct_video_course().getTeacher().getName());
            }
            progress.setText(String.format(getString(R.string.lesson_count), data.getProduct_video_course().getPreset_lesson_count()));
            refund.setVisibility(View.GONE);
            findViewById(R.id.tip).setVisibility(View.VISIBLE);
        }

        ordernumber.setText(data.getId());
//创建时间
        if (StringUtils.isNullOrBlanK(data.getCreated_at())) {
            buildtime.setText(getResourceString(R.string.is_null));
        } else {
            try {
                buildtime.setText(parse.format(parseISO.parse(data.getCreated_at())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //支付时间
        if (StringUtils.isNullOrBlanK(data.getPay_at())) {
            paytime.setText(getResourceString(R.string.is_null));
        } else {
            try {
                paytime.setText(parse.format(parseISO.parse(data.getPay_at())));
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

        if (data.getStatus().equals("refunding")) {//退款中
            status.setImageResource(R.mipmap.refunding);
            refund.setText(R.string.cancel_refund);
            refund.setTextColor(0xffaaaaaa);
            refund.setBackgroundResource(R.drawable.button_background_light);
        } else {
            refund.setText(R.string.apply_refund);
            refund.setTextColor(0xffff5842);
            refund.setBackgroundResource(R.drawable.button_background_normal);
            if (data.getStatus().equals("paid")) {//正在交易
                status.setImageResource(R.mipmap.paying);
            } else if (data.getStatus().equals("shipped")) {//正在交易
                status.setImageResource(R.mipmap.paying);
            } else {//交易完成
                status.setImageResource(R.mipmap.complete_pay);
            }
        }

        payprice.setText("￥" + data.getAmount());

        refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("refunding".equals(data.getStatus())) {
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
        refund = (TextView) findViewById(R.id.button_refund);
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

    /**
     * 申请退款
     */
    private void applyRefund() {
        if ("LiveStudio::VideoCourse".equals(data.getProduct_type())) {
            Toast.makeText(PersonalMyOrderPaidDetailActivity.this, "此订单类型暂不支持退款", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("order_id", ordernumber.getText().toString());
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getUserId() + "/refunds/info", map), null,
                new VolleyListener(PersonalMyOrderPaidDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Intent intent = new Intent(PersonalMyOrderPaidDetailActivity.this, ApplyRefundActivity.class);
                        intent.putExtra("response", response.toString());
                        intent.putExtra("order_id", ordernumber.getText().toString());

                        if ("LiveStudio::Course".equals(data.getProduct_type())) {
                            intent.putExtra("name", data.getProduct().getName());
                            intent.putExtra("preset_lesson_count", data.getProduct().getPreset_lesson_count());
                            intent.putExtra("closed_lessons_count", data.getProduct().getClosed_lessons_count());
                        } else if ("LiveStudio::InteractiveCourse".equals(data.getProduct_type())) {
                            intent.putExtra("name", data.getProduct_interactive_course().getName());
                            intent.putExtra("preset_lesson_count", data.getProduct_interactive_course().getLessons_count());
                            intent.putExtra("closed_lessons_count", data.getProduct_interactive_course().getClosed_lessons_count());
                        } else if ("LiveStudio::VideoCourse".equals(data.getProduct_type())) {
//                            intent.putExtra("name", data.getProduct_video_course().getName());
//                            intent.putExtra("preset_lesson_count", data.getProduct_video_course().getPreset_lesson_count());
//                            intent.putExtra("closed_lessons_count", data.getProduct_video_course().getClosed_lessons_count());
                            Logger.e("error");
                        }

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
        text.setText(R.string.confirm_cancel_refund);
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
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            setResult(Constant.RESPONSE);
            finish();
        }
    }

    private void cancelRefund() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.PUT, UrlUtils.urlpayment + BaseApplication.getUserId() + "/refunds/" + ordernumber.getText().toString() + "/cancel", null,
                new VolleyListener(PersonalMyOrderPaidDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Toast.makeText(PersonalMyOrderPaidDetailActivity.this, R.string.cancel_refund_success, Toast.LENGTH_SHORT).show();
                        setResult(Constant.RESPONSE);
                        finish();
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        Toast.makeText(PersonalMyOrderPaidDetailActivity.this, R.string.cancel_refund_error, Toast.LENGTH_SHORT).show();
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

