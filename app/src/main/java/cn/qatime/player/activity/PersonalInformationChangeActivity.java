package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UpLoadUtil;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.CityBean;
import libraryextra.bean.GradeBean;
import libraryextra.bean.ImageItem;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.bean.ProvincesBean;
import libraryextra.bean.SchoolBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.DialogUtils;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.view.CustomProgressDialog;
import libraryextra.view.MDatePickerDialog;
import libraryextra.view.WheelView;

public class PersonalInformationChangeActivity extends BaseActivity implements View.OnClickListener {
    ImageView headsculpture;
    View replace;
    EditText name;
    TextView men;
    TextView women;
    TextView textGrade;
    TextView complete;
    private EditText describe;
    private TextView birthday;
    private TextView region;
    private View regionView;
    private View birthdayView;
    private View gradeView;

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    private String imageUrl = "";
    private String select = "2000-01-01";//生日所选日期
    private CustomProgressDialog progress;
    private AlertDialog alertDialog;
    private String gender = "";
    private List<String> grades;
    private ProvincesBean.DataBean regionProvince;
    private CityBean.Data regionCity;
    private ProvincesBean provincesBean;
    private CityBean cityBean;
    private SchoolBean schoolBean;
    private TextView school;
    private View schoolView;
    private SchoolBean.Data schoolData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information_change);
        setTitles(getResources().getString(R.string.change_information));
        initView();
        provincesBean = JsonUtils.objectFromJson(FileUtil.readFile(getFilesDir() + "/provinces.txt").toString(), ProvincesBean.class);
        cityBean = JsonUtils.objectFromJson(FileUtil.readFile(getFilesDir() + "/cities.txt").toString(), CityBean.class);
        schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getFilesDir() + "/school.txt").toString(), SchoolBean.class);
        String gradeString = FileUtil.readFile(getFilesDir() + "/grade.txt");
//        LogUtils.e("班级基础信息" + gradeString);
        grades = new ArrayList<>();
        if (!StringUtils.isNullOrBlanK(gradeString)) {
            GradeBean gradeBean = JsonUtils.objectFromJson(gradeString, GradeBean.class);
            grades = gradeBean.getData().getGrades();
        }


        PersonalInformationBean data = (PersonalInformationBean) getIntent().getSerializableExtra("data");
        if (data != null && data.getData() != null) {
            initData(data);
        }

        replace.setOnClickListener(this);
        men.setOnClickListener(this);
        women.setOnClickListener(this);
        birthdayView.setOnClickListener(this);
        complete.setOnClickListener(this);
        gradeView.setOnClickListener(this);
        regionView.setOnClickListener(this);
        schoolView.setOnClickListener(this);
    }

    private void initData(PersonalInformationBean data) {
        Glide.with(PersonalInformationChangeActivity.this).load(data.getData().getAvatar_url()).placeholder(R.mipmap.personal_information_head).transform(new GlideCircleTransform(PersonalInformationChangeActivity.this)).crossFade().into(headsculpture);
        name.setText(data.getData().getName());
        Editable etext = name.getText();
        imageUrl = data.getData().getAvatar_url();
        Selection.setSelection(etext, etext.length());
        if (!StringUtils.isNullOrBlanK(data.getData().getGender())) {
            if (data.getData().getGender().equals("male")) {
                men.setSelected(true);
                women.setSelected(false);
            } else {
                men.setSelected(false);
                women.setSelected(true);
            }
        }
        if (!StringUtils.isNullOrBlanK(data.getData().getBirthday())) {
            try {
                birthday.setText(format.format(parse.parse(data.getData().getBirthday())));
                select = data.getData().getBirthday();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            birthday.setText(format.format(new Date()));
            select = parse.format(new Date());
        }
        if (!StringUtils.isNullOrBlanK(data.getData().getGrade())) {

            for (int i = 0; i < grades.size(); i++) {
                if (data.getData().getGrade().equals(grades.get(i))) {
                    textGrade.setText(data.getData().getGrade());
                    break;
                }
            }
        }
        if (provincesBean != null && provincesBean.getData() != null) {
            for (int i = 0; i < provincesBean.getData().size(); i++) {
                if (provincesBean.getData().get(i).getId().equals(data.getData().getProvince())) {
                    regionProvince = provincesBean.getData().get(i);
                    break;
                }
            }
        }
        if (cityBean != null && cityBean.getData() != null) {
            for (int i = 0; i < cityBean.getData().size(); i++) {
                if (cityBean.getData().get(i).getId().equals(data.getData().getCity())) {
                    regionCity = cityBean.getData().get(i);
                    break;
                }
            }
        }
        if (regionCity != null && regionProvince != null) {
            region.setText(regionProvince.getName() + regionCity.getName());
        }

        if (schoolBean != null && schoolBean.getData() != null) {
            for (int i = 0; i < schoolBean.getData().size(); i++) {
                if (schoolBean.getData().get(i).getId() == data.getData().getSchool()) {
                    schoolData = schoolBean.getData().get(i);
                    school.setText(schoolBean.getData().get(i).getName());
                    break;
                }
            }
        }
        describe.setText(data.getData().getDesc());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.region_view:
                Intent regionIntent = new Intent(this, RegionSelectActivity1.class);
//                if (regionProvince != null && regionCity != null) {
//                    regionIntent.putExtra("region_province", regionProvince);
//                    regionIntent.putExtra("region_city", regionCity);
//                }
                startActivityForResult(regionIntent, Constant.REQUEST_REGION_SELECT);
                break;
            case R.id.school_view:
                Intent schoolIntent = new Intent(this, SchoolSelectActivity.class);
                startActivityForResult(schoolIntent, Constant.REQUEST_SCHOOL_SELECT);
                break;
            case R.id.men:
                men.setSelected(true);
                women.setSelected(false);
                gender = "male";
                break;
            case R.id.women:
                women.setSelected(true);
                men.setSelected(false);
                gender = "female";
                break;
            case R.id.grade_view:
                showGradePickerDialog();
                break;
            case R.id.replace://去选择图片
                final Intent intent = new Intent(PersonalInformationChangeActivity.this, PictureSelectActivity.class);
                startActivityForResult(intent, Constant.REQUEST_PICTURE_SELECT);
                break;
            case R.id.birthday_view://生日
                try {

                    MDatePickerDialog dataDialog = new MDatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            select = (year + "-" + ((monthOfYear + 1) >= 10 ? String.valueOf((monthOfYear + 1)) : ("0" + (monthOfYear + 1))) + "-" + ((dayOfMonth) >= 10 ? String.valueOf((dayOfMonth)) : ("0" + (dayOfMonth))));
                            try {
                                birthday.setText(format.format(parse.parse(select)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, parse.parse(select).getYear() + 1900, parse.parse(select).getMonth() + 1, parse.parse(select).getDate());
                    dataDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dataDialog.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.complete://完成
                String url = UrlUtils.urlPersonalInformation + BaseApplication.getUserId();
                UpLoadUtil util = new UpLoadUtil(url) {
                    @Override
                    public void httpStart() {
                        progress = DialogUtils.startProgressDialog(progress, PersonalInformationChangeActivity.this);
                        progress.setCanceledOnTouchOutside(false);
                        progress.setCancelable(false);
                    }

                    @Override
                    protected void httpSuccess(String result) {
                        DialogUtils.dismissDialog(progress);
                        PersonalInformationBean sData = JsonUtils.objectFromJson(result, PersonalInformationBean.class);

                        BaseApplication.getProfile().getData().getUser().setAvatar_url(sData.getData().getAvatar_url());
                        Profile profile = BaseApplication.getProfile();
                        Profile.User user = profile.getData().getUser();
                        user.setId(sData.getData().getId());
                        user.setName(sData.getData().getName());
                        user.setNick_name(sData.getData().getNick_name());
                        user.setAvatar_url(sData.getData().getAvatar_url());
                        user.setEx_big_avatar_url(sData.getData().getEx_big_avatar_url());
                        user.setEmail(sData.getData().getEmail());
                        user.setLogin_mobile(sData.getData().getLogin_mobile());
                        user.setChat_account(sData.getData().getChat_account());

                        profile.getData().setUser(user);
                        BaseApplication.setProfile(profile);

                        Intent data = new Intent();
                        data.putExtra("data", result);
                        setResult(Constant.RESPONSE, data);
                        Toast.makeText(PersonalInformationChangeActivity.this, getResources().getString(R.string.change_information_successful), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    protected void httpFailed(String result) {
                        Toast.makeText(PersonalInformationChangeActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        DialogUtils.dismissDialog(progress);
                    }
                };

                if (StringUtils.isNullOrBlanK(BaseApplication.getUserId())) {
                    Toast.makeText(PersonalInformationChangeActivity.this, getResources().getString(R.string.id_is_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                String sName = name.getText().toString();
                if (StringUtils.isNullOrBlanK(sName)) {
                    Toast.makeText(this, getResources().getString(R.string.name_can_not_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isNullOrBlanK(imageUrl)) {
                    Toast.makeText(this, getResources().getString(R.string.hader_can_not_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                String grade = textGrade.getText().toString();
                if (StringUtils.isNullOrBlanK(grade)) {
                    Toast.makeText(this, getResources().getString(R.string.grade_can_not_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (regionCity == null || regionProvince == null) {
                    Toast.makeText(this, "请选择城市", Toast.LENGTH_SHORT).show();
                    return;
                }
                String birthday = select.equals(parse.format(new Date())) ? "" : select;
                String desc = describe.getText().toString();
                Map<String, String> map = new HashMap<>();

                map.put("name", sName);
                map.put("grade", grade);
                map.put("avatar", imageUrl);
                map.put("gender", gender);
                map.put("birthday", birthday);
                map.put("province_id", regionProvince.getId());
                map.put("city_id", regionCity.getId());
                if(schoolData!=null){
                    map.put("school_id", schoolData.getId() + "");
                }
                map.put("desc", desc);
                Logger.e("--" + sName + "--" + grade + "--" + imageUrl + "--" + gender + "--" + birthday + "--" + desc + "--");
                util.execute(map);
                break;
        }
    }

    private void showGradePickerDialog() {
        if (alertDialog == null) {
            final View view = View.inflate(PersonalInformationChangeActivity.this, R.layout.dialog_grade_picker, null);
            final WheelView grade = (WheelView) view.findViewById(R.id.grade);
            grade.setOffset(1);
            grade.setItems(grades);
            grade.setSeletion(grades.indexOf(textGrade.getText()));
            grade.setonItemClickListener(new WheelView.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    alertDialog.dismiss();
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInformationChangeActivity.this);
            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setContentView(view);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    textGrade.setText(grade.getSeletedItem());
                }
            });
//            WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
//            attributes.width= ScreenUtils.getScreenWidth(getApplicationContext())- DensityUtils.dp2px(getApplicationContext(),20)*2;
//            alertDialog.getWindow().setAttributes(attributes);
        } else {
            alertDialog.show();
        }
    }

    private void initView() {
        headsculpture = (ImageView) findViewById(R.id.head_sculpture);
        replace = findViewById(R.id.replace);
        name = (EditText) findViewById(R.id.name);
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
            }
        });
        men = (TextView) findViewById(R.id.men);
        women = (TextView) findViewById(R.id.women);
        textGrade = (TextView) findViewById(R.id.text_grade);
        describe = (EditText) findViewById(R.id.describe);
        describe.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        birthday = (TextView) findViewById(R.id.birthday);
        birthdayView = findViewById(R.id.birthday_view);
        gradeView = findViewById(R.id.grade_view);
        complete = (TextView) findViewById(R.id.complete);
        region = (TextView) findViewById(R.id.region);
        regionView = findViewById(R.id.region_view);
        school = (TextView) findViewById(R.id.school);
        schoolView = findViewById(R.id.school_view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_PICTURE_SELECT) {
            if (resultCode == Constant.RESPONSE_CAMERA) {//拍照返回的照片
                if (data != null) {
                    String url = data.getStringExtra("url");

                    if (url != null && !StringUtils.isNullOrBlanK(url)) {
                        Bitmap bitmap = BitmapFactory.decodeFile(url);
                        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                        bitmap.recycle();
                        Intent intent = new Intent(PersonalInformationChangeActivity.this, CropImageActivity.class);
                        intent.putExtra("id", uri.toString());
                        startActivityForResult(intent, Constant.PHOTO_CROP);
                    }
                }
            } else if (resultCode == Constant.RESPONSE_PICTURE_SELECT) {//选择照片返回的照片
                if (data != null) {
                    ImageItem image = (ImageItem) data.getSerializableExtra("data");
                    if (image != null && !StringUtils.isNullOrBlanK(image.imageId)) {
                        Intent intent = new Intent(PersonalInformationChangeActivity.this, CropImageActivity.class);
                        intent.putExtra("id", "content://media/external/images/media/" + image.imageId);
                        startActivityForResult(intent, Constant.PHOTO_CROP);
                    }
                }

            }
        } else if (resultCode == Constant.PHOTO_CROP) {
            Logger.e("裁剪", "回来");
            if (data != null) {
                imageUrl = data.getStringExtra("bitmap");
                Logger.e(imageUrl);
                if (new File(imageUrl).exists()) {
                    Logger.e("回来成功");
                }
                if (!StringUtils.isNullOrBlanK(imageUrl)) {
                    Glide.with(this).load(Uri.fromFile(new File(imageUrl))).transform(new GlideCircleTransform(this)).crossFade().into(headsculpture);
                }
            }
        } else if (requestCode == Constant.REQUEST_REGION_SELECT && resultCode == Constant.RESPONSE_REGION_SELECT) {
            regionCity = (CityBean.Data) data.getSerializableExtra("region_city");
            regionProvince = (ProvincesBean.DataBean) data.getSerializableExtra("region_province");
            if (regionCity != null && regionProvince != null) {
                region.setText(regionProvince.getName() + regionCity.getName());
            }
        } else if (requestCode == Constant.REQUEST_SCHOOL_SELECT && resultCode == Constant.RESPONSE_SCHOOL_SELECT) {
            schoolData = (SchoolBean.Data) data.getSerializableExtra("school");
            if (schoolData != null) {
                school.setText(schoolData.getName());
            }
        }
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
