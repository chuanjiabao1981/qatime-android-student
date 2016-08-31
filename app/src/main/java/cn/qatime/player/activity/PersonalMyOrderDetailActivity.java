package cn.qatime.player.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import javax.security.auth.Subject;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;


public class PersonalMyOrderDetailActivity extends BaseActivity {
    private ImageView image;
    private TextView teacher;
    private TextView grade;
    private TextView subject;
    private TextView progress;
    private TextView time;
    private TextView ordernumber;
    private TextView buildtime;
    private TextView paytype;
    private TextView classname;
    private TextView pay;
    private TextView payprice;
    private TextView paytime;
    private TextView cancelorder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_order_detail);
        setTitle(getResources().getString(R.string.detail_of_order));
    }

    public void initView() {

        classname = (TextView) findViewById(R.id.classname);
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
}

