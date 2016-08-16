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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.RemedialClassDetailBean;

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
    TextView teachway;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        setTitle(getResources().getString(R.string.order_confirm));
        initView();
        RemedialClassDetailBean data = (RemedialClassDetailBean) getIntent().getSerializableExtra("data");
        id = getIntent().getIntExtra("id", 0);
        if (data != null) {
            setValue(data);
//            initData(data.getData().getId());
        }
        pay.setOnClickListener(this);
    }


    private void setValue(RemedialClassDetailBean data) {
        Glide.with(OrderConfirmActivity.this).load(data.getData().getPublicize()).placeholder(R.mipmap.photo).fitCenter().crossFade().into(image);

        name.setText(data.getData().getName());
        project.setText("科目类型：" + data.getData().getSubject());
        grade.setText("年级类型：" + data.getData().getGrade());
        classnumber.setText("课时总数：" + data.getData().getPreset_lesson_count());
        teacher.setText("授课教师：" + data.getData().getTeacher().getName());
        try {
            classstarttime.setText("开课时间：" + format.format(parse.parse(data.getData().getLive_start_time())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            classendtime.setText("结课时间：" + format.format(parse.parse(data.getData().getLive_end_time())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (data.getData().getStatus().equals("preview")) {
            status.setText("当前状态：招生中");
        } else if (data.getData().getStatus().equals("teaching")) {
            status.setText("当前状态：已开课");
        } else {
            status.setText("当前状态：已结束");
        }
        // TODO: 2016/8/12  image teachway price
        teachway.setText("授课方式：");
        price.setText("价格："+data.getData().getPrice());
        payprice.setText(" "+data.getData().getPrice()+" ");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(OrderConfirmActivity.this, OrderPayActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("payType", payType);
        startActivity(intent);
    }

    private void initView() {
        name = (TextView) findViewById(R.id.name);
        image = (ImageView) findViewById(R.id.image);
        project = (TextView) findViewById(R.id.project);
        grade = (TextView) findViewById(R.id.grade);
        teacher = (TextView) findViewById(R.id.teacher);
        classnumber = (TextView) findViewById(R.id.class_number);
        classstarttime = (TextView) findViewById(R.id.class_start_time);
        classendtime = (TextView) findViewById(R.id.class_end_time);
        status = (TextView) findViewById(R.id.status);
        teachway = (TextView) findViewById(R.id.teach_way);

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
}
