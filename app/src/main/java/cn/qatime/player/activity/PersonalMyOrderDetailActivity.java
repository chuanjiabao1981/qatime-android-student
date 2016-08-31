package cn.qatime.player.activity;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;

import javax.security.auth.Subject;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.bean.OrderDetailBean;
import libraryextra.bean.OrderPayBean;
import libraryextra.utils.StringUtils;


public class PersonalMyOrderDetailActivity extends BaseActivity {

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
    DecimalFormat df = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_order_detail);
        setTitle(getResources().getString(R.string.detail_of_order));
        initView();

        EventBus.getDefault().register(this);

        OrderDetailBean data = (OrderDetailBean) getIntent().getSerializableExtra("data");
        id = getIntent().getIntExtra("id", 0);
        if (data != null) {
            setValue(data);
            priceNumber = data.price;
        }
//        pay.setOnClickListener(this);
    }

    private void setValue(OrderDetailBean data) {
        Glide.with(PersonalMyOrderDetailActivity.this).load(data.image).placeholder(R.mipmap.photo).fitCenter().crossFade().into(image);
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
        progress.setText(data.Completed_lesson_count + "/" + data.Preset_lesson_count);
        String price = df.format(data.price);
        if (price.startsWith(".")) {
            price = "0" + price;
        }
        PersonalMyOrderDetailActivity.this.payprice.setText(price);
        payprice.setText(" " + price + " ");

    }

    public void initView() {

        name = (TextView) findViewById(R.id.name);
        image = (ImageView) findViewById(R.id.image);
        subject = (TextView) findViewById(R.id.subject);
        grade = (TextView) findViewById(R.id.grade);
        teacher = (TextView) findViewById(R.id.teacher);
        progress = (TextView) findViewById(R.id.progress);//进度
        time = (TextView) findViewById(R.id.time);//倒计时
        ordernumber = (TextView) findViewById(R.id.order_number);//订单编号
        buildtime = (TextView) findViewById(R.id.build_time);//创建时间
        paytime = (TextView) findViewById(R.id.pay_time);//支付时间
        paytype = (TextView) findViewById(R.id.pay_type);//支付方式
        payprice = (TextView) findViewById(R.id.pay_price);//支付价格
        pay = (TextView) findViewById(R.id.pay);
        cancelorder = (TextView) findViewById(R.id.cancel_order);

    }

    @Subscribe
    public void onEvent(String event) {
        if (!StringUtils.isNullOrBlanK(event) && event.equals("pay_success")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

