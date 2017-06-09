package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.CityBean;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.ProvincesBean;
import libraryextra.bean.SchoolBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class PersonalInformationActivity extends BaseActivity {
    ImageView headsculpture;
    TextView name;
    TextView sex;
    TextView birthday;
    TextView grade;
    TextView school;
    TextView describe;

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    private PersonalInformationBean bean;
    private TextView region;
//2015-10-08 15:04:25.0 ---> 10月08日 15:04
//    format.format(parse.parse("2015-10-08 15:04:25.0");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        setTitles(getResources().getString(R.string.personal_information));
        setRightImage(R.mipmap.personal_change_information, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalInformationActivity.this, PersonalInformationChangeActivity.class);
                intent.putExtra("data", bean);
                startActivityForResult(intent, Constant.REQUEST);
            }
        });
        initView();
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST && resultCode == Constant.RESPONSE) {
            if (!StringUtils.isNullOrBlanK(data.getStringExtra("data"))) {
                PersonalInformationBean sData = JsonUtils.objectFromJson(data.getStringExtra("data"), PersonalInformationBean.class);
                if (sData != null && sData.getData() != null) {
                    bean = sData;
                    setValue(sData);
                    setResult(Constant.RESPONSE);
                }
            }
        }
    }

    private void initData() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getInstance().getUserId() + "/info", null,
                new VolleyListener(PersonalInformationActivity.this) {


                    @Override
                    protected void onSuccess(JSONObject response) {
                        bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);
                        if (bean != null && bean.getData() != null) {
                            setValue(bean);
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    private void setValue(PersonalInformationBean bean) {
        Glide.with(PersonalInformationActivity.this).load(bean.getData().getAvatar_url()).placeholder(R.mipmap.personal_information_head).transform(new GlideCircleTransform(PersonalInformationActivity.this)).crossFade().into(headsculpture);
        name.setText(StringUtils.isNullOrBlanK(bean.getData().getName()) ? "null" : bean.getData().getName());
        if (!StringUtils.isNullOrBlanK(bean.getData().getGender())) {
            if (bean.getData().getGender().equals("male")) {
                sex.setText(getResources().getString(R.string.male));
            } else if (bean.getData().getGender().equals("female")) {
                sex.setText(getResources().getString(R.string.female));
            }
        } else {
            sex.setText("");
        }
        if (!StringUtils.isNullOrBlanK(bean.getData().getBirthday())) {
            try {
                birthday.setText(format.format(parse.parse(bean.getData().getBirthday())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            birthday.setText(format.format(new Date()));
        }
        if (!StringUtils.isNullOrBlanK(bean.getData().getGrade())) {
            grade.setText(bean.getData().getGrade());
        } else {
            grade.setText("");
        }
        String regionStr = "";
        String json = FileUtil.readFile(getFilesDir() + "/provinces.txt").toString();
        ProvincesBean provincesBean = JsonUtils.objectFromJson(json, ProvincesBean.class);
        String json1 = FileUtil.readFile(getFilesDir() + "/cities.txt").toString();
        CityBean cityBean = JsonUtils.objectFromJson(json1, CityBean.class);
        String json2 = FileUtil.readFile(getFilesDir() + "/school.txt").toString();
        SchoolBean schoolBean = JsonUtils.objectFromJson(json2, SchoolBean.class);

        if (provincesBean != null && provincesBean.getData() != null) {
            for (int i = 0; i < provincesBean.getData().size(); i++) {
                if (provincesBean.getData().get(i).getId().equals(bean.getData().getProvince())) {
                    regionStr += provincesBean.getData().get(i).getName();
                    break;
                }
            }
        }
        if (cityBean != null && cityBean.getData() != null) {
            for (int i = 0; i < cityBean.getData().size(); i++) {
                if (cityBean.getData().get(i).getId().equals(bean.getData().getCity())) {
                    regionStr += cityBean.getData().get(i).getName();
                    break;
                }
            }
        }
        region.setText(regionStr);
        if (schoolBean != null && schoolBean.getData() != null) {
            for (int i = 0; i < schoolBean.getData().size(); i++) {
                if (schoolBean.getData().get(i).getId() == bean.getData().getSchool()) {
                    school.setText(schoolBean.getData().get(i).getName());
                    break;
                }
            }
        }
        if (!StringUtils.isNullOrBlanK(bean.getData().getDesc())) {
            describe.setText(bean.getData().getDesc());
        } else {
            describe.setText("");
        }
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
