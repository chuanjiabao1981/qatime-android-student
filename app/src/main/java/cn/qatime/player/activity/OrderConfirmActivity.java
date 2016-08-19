package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.bean.OrderPayBean;
import libraryextra.utils.StringUtils;

public class OrderConfirmActivity extends BaseActivity implements View.OnClickListener {
    TextView name;
    ImageView image;
    TextView project;
    TextView grade;
    TextView teacher;
    TextView classnumber;
    TextView classstarttime;
    TextView classendtime;
    TextView status;
    TextView price;
    TextView payprice;
    private Button pay;
    private RadioButton wechatPay;
    private RadioButton aliPay;
    private RadioGroup radioGroup;
    private int id;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private String payType = "1";
    private int priceNumber = 0;

    DecimalFormat df = new DecimalFormat("#.00");

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
            classstarttime.setText(getResources().getString(R.string.class_start_time) + format.format(parse.parse(data.classstarttime)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            classendtime.setText(getResources().getString(R.string.class_end_time) + format.format(parse.parse(data.classendtime)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (data.status.equals("preview")) {
            status.setText(getResources().getString(R.string.status_preview));
        } else if (data.status.equals("teaching")) {
            status.setText(getResources().getString(R.string.status_teaching));
        } else {
            status.setText(getResources().getString(R.string.status_over));
        }

        String price = df.format(data.price);
        if (price.startsWith(".")) {
            price = "0" + price;
        }
        OrderConfirmActivity.this.price.setText("价  格：" + price);
        payprice.setText(" " + price + " ");

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(OrderConfirmActivity.this, OrderPayActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("price", priceNumber);
        intent.putExtra("payType", payType);
        startActivity(intent);
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
        status = (TextView) findViewById(R.id.status);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        wechatPay = (RadioButton) findViewById(R.id.wechat_pay);
        aliPay = (RadioButton) findViewById(R.id.alipay);
        price = (TextView) findViewById(R.id.price);
        payprice = (TextView) findViewById(R.id.pay_price);
        pay = (Button) findViewById(R.id.pay);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == wechatPay.getId()) {
                    payType = "1";
                } else {
                    payType = "0";
                }

                //TODO 集成完支付宝后，去掉下面这段
                if (checkedId == aliPay.getId()) {
                    Toast.makeText(OrderConfirmActivity.this, "暂不支持支付宝支付", Toast.LENGTH_SHORT).show();
                    wechatPay.setChecked(true);
                    aliPay.setChecked(false);
                    payType = "1";
                }
            }
        });
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
