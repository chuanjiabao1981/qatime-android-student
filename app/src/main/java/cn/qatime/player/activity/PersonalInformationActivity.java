package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.PersonalInformationBean;
import cn.qatime.player.transformation.GlideCircleTransform;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.JsonUtils;
import cn.qatime.player.utils.StringUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VolleyErrorListener;
import cn.qatime.player.utils.VolleyListener;

public class PersonalInformationActivity extends BaseActivity {
    ImageView headsculpture;
    TextView name;
    TextView sex;
    TextView birthday;
    TextView grade;
    TextView region;
    TextView school;
    TextView describe;

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    private PersonalInformationBean bean;
//2015-10-08 15:04:25.0 ---> 10月08日 15:04
//    format.format(parse.parse("2015-10-08 15:04:25.0");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        setTitle(getResources().getString(R.string.personal_information));
        setRightImage(R.drawable.personal_change_information, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalInformationActivity.this, PersonalInformationChangeActivity.class);
                intent.putExtra("data",bean);
                startActivity(intent);
            }
        });
        initView();
        initData();
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(BaseApplication.getUserId()));

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", map), null,
                new VolleyListener(PersonalInformationActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                         bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);
                        if (bean != null && bean.getData() != null) {
                            Glide.with(PersonalInformationActivity.this).load(bean.getData().getSmall_avatar_url()).placeholder(R.drawable.personal_information_head).transform(new GlideCircleTransform(PersonalInformationActivity.this)).crossFade().into(headsculpture);
                            name.setText(bean.getData().getName());
                            if (!StringUtils.isNullOrBlanK(bean.getData().getGender())) {
                                if (bean.getData().getGender().equals("male")) {
                                    sex.setText(getResources().getString(R.string.male));
                                } else {
                                    sex.setText(getResources().getString(R.string.female));
                                }
                            }
                            try {
                                birthday.setText(format.format(parse.parse(bean.getData().getBirthday())));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (!StringUtils.isNullOrBlanK(bean.getData().getGrade())) {
                                grade.setText(bean.getData().getGrade());
                            }
                            if (!StringUtils.isNullOrBlanK(bean.getData().getProvince()) && !StringUtils.isNullOrBlanK(bean.getData().getCity())) {
                                region.setText(bean.getData().getProvince() + " " + bean.getData().getCity());
                            }
                            //TODO
//                            school
                            describe.setText(bean.getData().getDesc());
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    private void initView() {
        headsculpture = (ImageView) findViewById(R.id.head_sculpture);
        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        birthday = (TextView) findViewById(R.id.birthday);
        grade = (TextView) findViewById(R.id.grade);
        region = (TextView) findViewById(R.id.region);
        school = (TextView) findViewById(R.id.school);
        describe = (TextView) findViewById(R.id.describe);
    }
}
