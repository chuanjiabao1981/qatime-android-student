package cn.qatime.player.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.bean.OrderDetailBean;
import libraryextra.utils.StringUtils;


public class PersonalMyOrderPaidDetailActivity extends BaseActivity {

    private TextView subject;
    private TextView progress;
    private TextView time;
    private TextView ordernumber;
    private TextView buildtime;
    private TextView paytype;
    private TextView pay;
    private TextView paytime;
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
        setContentView(R.layout.activity_personal_my_order_paid_detail);
        setTitle(getResources().getString(R.string.detail_of_order));
        initView();
        OrderDetailBean data = (OrderDetailBean) getIntent().getSerializableExtra("data");
        if (data != null) {
            setValue(data);
        }
    }

    private void setValue(OrderDetailBean data) {
        Glide.with(PersonalMyOrderPaidDetailActivity.this).load(data.image).placeholder(R.mipmap.photo).fitCenter().crossFade().into(image);
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
        // TODO: 2016/9/1 支付时间
        buildtime.setText(format.format(getIntent().getStringExtra("created_at")));//创建时间
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
        PersonalMyOrderPaidDetailActivity.this.payprice.setText(price);
        payprice.setText(" " + price + " ");
    }

    public void initView() {

        name = (TextView) findViewById(R.id.name);
        image = (ImageView) findViewById(R.id.image);
        subject = (TextView) findViewById(R.id.subject);
        grade = (TextView) findViewById(R.id.grade);
        teacher = (TextView) findViewById(R.id.teacher);
        progress = (TextView) findViewById(R.id.progress);//进度
        ordernumber = (TextView) findViewById(R.id.order_number);//订单编号
        buildtime = (TextView) findViewById(R.id.build_time);//创建时间
        paytime = (TextView) findViewById(R.id.pay_time);//支付时间
        paytype = (TextView) findViewById(R.id.pay_type);//支付方式
        payprice = (TextView) findViewById(R.id.pay_price);//支付价格
    }

}

