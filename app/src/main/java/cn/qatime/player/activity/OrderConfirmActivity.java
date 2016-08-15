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

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.PersonalInformationBean;
import cn.qatime.player.bean.RemedialClassDetailBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.JsonUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VolleyErrorListener;
import cn.qatime.player.utils.VolleyListener;

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
    private String payType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
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
        name.setText(data.getData().getName());
        project.setText("科目类型：" + data.getData().getSubject());
        grade.setText("年级类型：" + data.getData().getGrade());
        classnumber.setText("课时总数：" + data.getData().getPreset_lesson_count());
        teacher.setText("授课教师:" + data.getData().getTeacher().getName());
        classstarttime.setText("开课时间:" + data.getData().getLive_start_time());
        classendtime.setText("结课时间:" + data.getData().getLive_end_time());
        status.setText("当前状态" + data.getData().getStatus());
        // TODO: 2016/8/12  image teachway price
//        teachway.setText("授课方式"+data.getData().);
        price.setText("价格"+data.getData().getPrice());
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
