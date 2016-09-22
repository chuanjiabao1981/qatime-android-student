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
import java.util.Date;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.SchoolBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.SPUtils;
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
//2015-10-08 15:04:25.0 ---> 10月08日 15:04
//    format.format(parse.parse("2015-10-08 15:04:25.0");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        setTitle(getResources().getString(R.string.personal_information));
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
                    setValue(sData);
                    Intent intent = new Intent();
                    BaseApplication.getProfile().getData().getUser().setAvatar_url(sData.getData().getAvatar_url());
                    BaseApplication.getProfile().getData().getUser().setEx_big_avatar_url(sData.getData().getEx_big_avatar_url());
                    SPUtils.putObject(this, "profile", BaseApplication.getProfile());
                    intent.putExtra("url", sData.getData().getAvatar_url());
                    setResult(Constant.RESPONSE, intent);
                }
            }
        }
    }

    private void initData() {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", null,
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
        name.setText(bean.getData().getName());
        if (!StringUtils.isNullOrBlanK(bean.getData().getGender())) {
            if (bean.getData().getGender().equals("male")) {
                sex.setText(getResources().getString(R.string.male));
            } else {
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
//        if (!StringUtils.isNullOrBlanK(bean.getData().getProvince()) && !StringUtils.isNullOrBlanK(bean.getData().getCity())) {
//            region.setText(bean.getData().getProvince() + " " + bean.getData().getCity());
//        }else {
//            region.setText("");}


        SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getCacheDir() + "/school.txt").toString(), SchoolBean.class);

        if (schoolBean != null && schoolBean.getData() != null) {
            for (int i = 0; i < schoolBean.getData().size(); i++) {
                if (bean.getData().getSchool() == schoolBean.getData().get(i).getId()) {
                    school.setText(schoolBean.getData().get(i).getName());
                    break;
                }
            }
        } else {
            school.setText("");
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
        school = (TextView) findViewById(R.id.school);
        describe = (TextView) findViewById(R.id.describe);
    }
}
