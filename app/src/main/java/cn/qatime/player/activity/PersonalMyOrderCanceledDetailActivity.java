package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.bean.OrderDetailBean;
import libraryextra.utils.StringUtils;


public class PersonalMyOrderCanceledDetailActivity extends BaseActivity {

    private TextView subject;
    private TextView progress;
    private TextView ordernumber;
    private TextView buildtime;
    private TextView paytype;
    private TextView reorder;
    private LinearLayout listitem;
    private TextView name;
    private ImageView image;
    private TextView grade;
    private TextView teacher;
    private TextView status;
    private int classid;
    private TextView payprice;
    DecimalFormat df = new DecimalFormat("#.00");
    SimpleDateFormat parseISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_order_canceled_detail);
        setTitle(getResources().getString(R.string.detail_of_order));
        initView();


        OrderDetailBean data = (OrderDetailBean) getIntent().getSerializableExtra("data");


        if (data != null) {
            setValue(data);

        }
    }

    private void setValue(OrderDetailBean data) {
        classid = getIntent().getIntExtra("id", 0);

        Glide.with(PersonalMyOrderCanceledDetailActivity.this).load(data.image).placeholder(R.mipmap.photo).centerCrop().crossFade().into(image);
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

        if (data.status.equals("refunded")) {//交易关闭
            status.setText(getResources().getString(R.string.deal_closed));
        } else if (data.status.equals("canceled")) {//交易关闭
            status.setText(getResources().getString(R.string.deal_closed));
        } else if (data.status.equals("expired")) {//交易关闭
            status.setText(getResources().getString(R.string.deal_closed));
        } else {//空
            status.setText("        ");
        }
        ordernumber.setText(getIntent().getStringExtra("order_id"));
        if (StringUtils.isNullOrBlanK(getIntent().getStringExtra("created_at"))) {
            buildtime.setText("        ");
        }//创建时间
        else {
            try {
                buildtime.setText(parse.format(parseISO.parse(getIntent().getStringExtra("created_at"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNullOrBlanK(getIntent().getStringExtra("created_at"))) {
            paytype.setText("        ");

        } else {
            String payType = getIntent().getStringExtra("payType");//支付方式
            if (payType.equals("weixin")) {
                paytype.setText(getResourceString(R.string.wechat_payment));
            } else {
                paytype.setText(getResourceString(R.string.alipay_payment));
            }
        }
        progress.setText(data.Completed_lesson_count + "/" + data.Preset_lesson_count);
        String price = df.format(data.current_price);
        if (price.startsWith(".")) {
            price = "0" + price;
        }
        PersonalMyOrderCanceledDetailActivity.this.payprice.setText(price);
        payprice.setText("￥" + price + " ");
    }

    public void initView() {

        name = (TextView) findViewById(R.id.name);
        image = (ImageView) findViewById(R.id.image);
        subject = (TextView) findViewById(R.id.subject);
        grade = (TextView) findViewById(R.id.grade);
        teacher = (TextView) findViewById(R.id.teacher);
        status = (TextView) findViewById(R.id.status);
        progress = (TextView) findViewById(R.id.progress);//进度
        ordernumber = (TextView) findViewById(R.id.order_number);//订单编号
        buildtime = (TextView) findViewById(R.id.build_time);//创建时间
        paytype = (TextView) findViewById(R.id.pay_type);//支付方式
        payprice = (TextView) findViewById(R.id.pay_price);//支付价格
        reorder = (TextView) findViewById(R.id.reorder);
        listitem = (LinearLayout) findViewById(R.id.list_item);//内详情点击
        listitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalMyOrderCanceledDetailActivity.this, RemedialClassDetailActivity.class);
                intent.putExtra("id", classid);
                intent.putExtra("page", 0);
                startActivity(intent);
            }
        });
        reorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalMyOrderCanceledDetailActivity.this, RemedialClassDetailActivity.class);
                intent.putExtra("id", classid);
                intent.putExtra("page", 0);
                startActivity(intent);
            }
        });
    }

}

