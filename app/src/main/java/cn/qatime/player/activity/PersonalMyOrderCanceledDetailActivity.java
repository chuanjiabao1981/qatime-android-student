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
import cn.qatime.player.bean.MyOrderBean;
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
    private TextView grade;
    private TextView teacher;
    private ImageView status;
    private int classid;
    private TextView payprice;
    DecimalFormat df = new DecimalFormat("#.00");
    SimpleDateFormat parseISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private MyOrderBean.DataBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_my_order_canceled_detail);
        setTitles(getResources().getString(R.string.detail_of_order));
        initView();

        data = (MyOrderBean.DataBean) getIntent().getSerializableExtra("data");

        if (data != null) {
            setValue(data);
        }
    }

    private void setValue(MyOrderBean.DataBean data) {
//        商品信息
        if("LiveStudio::Course".equals(data.getProduct_type())){
            classid = data.getProduct().getId();
            if (StringUtils.isNullOrBlanK(data.getProduct().getName())) {
                name.setText(getResourceString(R.string.cancel_order_name));
            } else {
                name.setText(data.getProduct().getName());
            }
            if (StringUtils.isNullOrBlanK(data.getProduct().getGrade())) {
                grade.setText("直播课/"+getResourceString(R.string.grade));
            } else {
                grade.setText("直播课/" +data.getProduct().getGrade());
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
            progress.setText(String.format(getString(R.string.lesson_count),data.getProduct().getPreset_lesson_count()));
        }else if("LiveStudio::InteractiveCourse".equals(data.getProduct_type())){
            classid = data.getProduct_interactive_course().getId();
            if (StringUtils.isNullOrBlanK(data.getProduct_interactive_course().getName())) {
                name.setText(getResourceString(R.string.cancel_order_name));
            } else {
                name.setText(data.getProduct_interactive_course().getName());
            }
            if (StringUtils.isNullOrBlanK(data.getProduct_interactive_course().getGrade())) {
                grade.setText("一对一/"+getResourceString(R.string.grade));
            } else {
                grade.setText("一对一/"+data.getProduct_interactive_course().getGrade());
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
                if(data.getProduct_interactive_course().getTeachers().size()>1){
                    sp.append("...");
                }
                teacher.setText(data.getProduct_interactive_course().getTeachers().get(0).getName());
            }
            progress.setText(String.format(getString(R.string.lesson_count),data.getProduct_interactive_course().getLessons_count()));
        }else if("LiveStudio::VideoCourse".equals(data.getProduct_type())){
            classid = data.getProduct_video_course().getId();
            if (StringUtils.isNullOrBlanK(data.getProduct_video_course().getName())) {
                name.setText(getResourceString(R.string.cancel_order_name));
            } else {
                name.setText(data.getProduct_video_course().getName());
            }
            if (StringUtils.isNullOrBlanK(data.getProduct_video_course().getGrade())) {
                grade.setText("视频课/"+getResourceString(R.string.grade));
            } else {
                grade.setText("视频课/"+data.getProduct_video_course().getGrade());
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
            progress.setText(String.format(getString(R.string.lesson_count),data.getProduct_video_course().getPreset_lesson_count()));
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
                paytype.setText(getResourceString(R.string.wexin_payment));
            } else if (payType.equals("alipay")) {
                paytype.setText(getResourceString(R.string.alipay_payment));
            } else {
                paytype.setText(getResourceString(R.string.account_payment));
            }
        }
        payprice.setText("￥" + data.getAmount());
    }

    public void initView() {
        name = (TextView) findViewById(R.id.name);
        subject = (TextView) findViewById(R.id.subject);
        grade = (TextView) findViewById(R.id.grade);
        teacher = (TextView) findViewById(R.id.teacher);
        status = (ImageView) findViewById(R.id.status);
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
                Intent intent = new Intent();
                if("LiveStudio::Course".equals(data.getProduct_type())){
                    intent.setClass(PersonalMyOrderCanceledDetailActivity.this, RemedialClassDetailActivity.class);
                }else if("LiveStudio::InteractiveCourse".equals(data.getProduct_type())){
                    intent.setClass(PersonalMyOrderCanceledDetailActivity.this, InteractCourseDetailActivity.class);
                }else if("LiveStudio::VideoCourse".equals(data.getProduct_type())){
                    intent.setClass(PersonalMyOrderCanceledDetailActivity.this, VideoCoursesActivity.class);
                }
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

