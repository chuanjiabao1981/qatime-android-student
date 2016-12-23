package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.bean.OrderDetailBean;
import libraryextra.utils.StringUtils;


public class PersonalMyOrderPaidDetailActivity extends BaseActivity {

    private TextView subject;
    private TextView progress;
    private TextView ordernumber;
    private TextView buildtime;
    private TextView paytype;
    private TextView paytime;
    private LinearLayout listitem;
    private TextView name;
    private ImageView image;
    private TextView grade;
    private TextView teacher;
    private ImageView status;
    private TextView payprice;
    private int classid;
    DecimalFormat df = new DecimalFormat("#.00");
    SimpleDateFormat parseISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_order_paid_detail);
        setTitle(getResources().getString(R.string.detail_of_order));
        initView();
        OrderDetailBean data = (OrderDetailBean) getIntent().getSerializableExtra("data");
        if (data != null) {
            setValue(data);
        }
    }

    private void setValue(OrderDetailBean data) {
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
                buildtime.setText(parse.format(parseISO.parse(getIntent().getStringExtra("pay_at"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        String payType = getIntent().getStringExtra("payType");//支付方式
        if (payType.equals("weixin")) {
            paytype.setText(getResourceString(R.string.wechat_payment));
        } else {
            paytype.setText(getResourceString(R.string.alipay_payment));
        }
        if (data.status.equals("paid")) {//正在交易
            status.setImageResource(R.mipmap.paying);
        } else if (data.status.equals("shipped")) {//正在交易
            status.setImageResource(R.mipmap.paying);
        } else {//交易完成
            status.setImageResource(R.mipmap.complete_pay);
        }
        progress.setText("共" + data.Preset_lesson_count + "课");
        String price = df.format(data.current_price);
        if (price.startsWith(".")) {
            price = "0" + price;
        }
        PersonalMyOrderPaidDetailActivity.this.payprice.setText(price);
        payprice.setText("￥" + price + " ");
    }

    public void initView() {

        name = (TextView) findViewById(R.id.name);
        image = (ImageView) findViewById(R.id.image);
        subject = (TextView) findViewById(R.id.subject);
        grade = (TextView) findViewById(R.id.grade);
        status = (ImageView) findViewById(R.id.status);
        teacher = (TextView) findViewById(R.id.teacher);
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

