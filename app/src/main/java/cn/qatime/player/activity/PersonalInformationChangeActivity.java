package cn.qatime.player.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import libraryextra.bean.GradeBean;
import libraryextra.bean.ImageItem;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.SchoolBean;
import libraryextra.transformation.GlideCircleTransform;
import cn.qatime.player.utils.Constant;
import libraryextra.utils.DialogUtils;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.view.MDatePickerDialog;
import libraryextra.utils.LogUtils;
import libraryextra.utils.StringUtils;
import cn.qatime.player.utils.UpLoadUtil;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.view.CustomProgressDialog;

public class PersonalInformationChangeActivity extends BaseActivity implements View.OnClickListener {
    ImageView headsculpture;
    TextView replace;
    EditText name;
    RadioButton men;
    RadioButton women;
    RadioGroup radiogroup;
    Spinner spinner;
    TextView complete;
    private Uri captureUri;
    private EditText describe;
    private TextView birthday;

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    private String imageUrl = "";
    private String select = "";//生日所选日期
    private SchoolBean schoolBean;
    private GradeBean gradeBean;
    private CustomProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information_change);
        setTitle(getResources().getString(R.string.change_information));
        initView();
        //获取基本数据信息,对应id与学校 年级
//        String school = FileUtil.readFile(getCacheDir() + "/school.txt");
//        if (!StringUtils.isNullOrBlanK(school)) {
//            schoolBean = JsonUtils.objectFromJson(school, SchoolBean.class);
//        }
        String gradeString = FileUtil.readFile(getCacheDir() + "/grade.txt");
//        LogUtils.e("班级基础信息" + gradeString);
        if (!StringUtils.isNullOrBlanK(gradeString)) {
            gradeBean = JsonUtils.objectFromJson(gradeString, GradeBean.class);
        }

        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinner, gradeBean.getData().getGrades()));

        replace.setOnClickListener(this);
        birthday.setOnClickListener(this);
        complete.setOnClickListener(this);
        PersonalInformationBean data = (PersonalInformationBean) getIntent().getSerializableExtra("data");
        if (data != null && data.getData() != null) {
            initData(data);
        }

    }

    private void initData(PersonalInformationBean data) {
        Glide.with(PersonalInformationChangeActivity.this).load(data.getData().getAvatar_url()).placeholder(R.mipmap.personal_information_head).transform(new GlideCircleTransform(PersonalInformationChangeActivity.this)).crossFade().into(headsculpture);
        name.setText(data.getData().getName());
        Editable etext = name.getText();
        Selection.setSelection(etext, etext.length());
        if (!StringUtils.isNullOrBlanK(data.getData().getGender())) {
            if (data.getData().getGender().equals("male")) {
                men.setChecked(true);
                women.setChecked(false);
            } else {
                men.setChecked(false);
                women.setChecked(true);
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
            for (int i = 0; i < gradeBean.getData().getGrades().size(); i++) {
                if (data.getData().getGrade().equals(gradeBean.getData().getGrades().get(i))) {
                    spinner.setSelection(i);
                    break;
                }
            }
        }
//            if (!StringUtils.isNullOrBlanK(data.getData().getProvince()) && !StringUtils.isNullOrBlanK(data.getData().getCity())) {
//                region.setText(data.getData().getProvince() + " " + data.getData().getCity());
//            }
//                            school
        describe.setText(data.getData().getDesc());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.replace://去选择图片
                final Intent intent = new Intent(PersonalInformationChangeActivity.this, PictureSelectActivity.class);
                startActivityForResult(intent, Constant.REQUEST_PICTURE_SELECT);
                break;
            case R.id.birthday://生日

                MDatePickerDialog dataDialog = null;

                try {
                    dataDialog = new MDatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            select = (year + "-" + ((monthOfYear + 1) >= 10 ? String.valueOf((monthOfYear + 1)) : ("0" + (monthOfYear + 1))) + "-" + ((dayOfMonth) >= 10 ? String.valueOf((dayOfMonth)) : ("0" + (dayOfMonth))));
                            try {
                                birthday.setText(format.format(parse.parse(select)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, parse.parse(select).getYear() + 1900, parse.parse(select).getMonth() + 1, parse.parse(select).getDay());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dataDialog.show();
                break;
            case R.id.complete://完成
                UpLoadUtil util = new UpLoadUtil(PersonalInformationChangeActivity.this) {
                    @Override
                    public void httpStart() {
                        progress = DialogUtils.startProgressDialog(progress, PersonalInformationChangeActivity.this);
                        progress.setCanceledOnTouchOutside(false);
                        progress.setCancelable(false);
                    }

                    @Override
                    protected void httpSuccess(String result) {
                        Intent data = new Intent();
                        data.putExtra("data", result);
                        setResult(Constant.RESPONSE, data);
                        DialogUtils.dismissDialog(progress);
                        Toast.makeText(PersonalInformationChangeActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    protected void httpFailed(String result) {

                    }
                };
                String url = UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/update";
                String filePath = imageUrl;
                if (StringUtils.isNullOrBlanK(BaseApplication.getUserId())) {
                    Toast.makeText(PersonalInformationChangeActivity.this, "id为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String sName = name.getText().toString();
                if (StringUtils.isNullOrBlanK(sName)) {
                    Toast.makeText(this, "名字不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String grade = gradeBean.getData().getGrades().get(spinner.getSelectedItemPosition());
                if (StringUtils.isNullOrBlanK(grade)) {
                    Toast.makeText(this, "年级不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String gender = radiogroup.getCheckedRadioButtonId() == men.getId() ? "male" : "female";
                String birthday = select.equals(parse.format(new Date())) ? "" : select;
                String desc = describe.getText().toString();
                util.execute(url, filePath, sName, grade, gender, birthday, desc);
                break;
        }
    }

    private void initView() {
        headsculpture = (ImageView) findViewById(R.id.head_sculpture);
        replace = (TextView) findViewById(R.id.replace);
        name = (EditText) findViewById(R.id.name);
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode()== KeyEvent.KEYCODE_ENTER);
            }
        });
        men = (RadioButton) findViewById(R.id.men);
        women = (RadioButton) findViewById(R.id.women);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        spinner = (Spinner) findViewById(R.id.spinner);
        describe = (EditText) findViewById(R.id.describe);
        describe.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        birthday = (TextView) findViewById(R.id.birthday);
        complete = (TextView) findViewById(R.id.complete);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_PICTURE_SELECT) {
            if (resultCode == Constant.RESPONSE_CAMERA) {//拍照返回的照片
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                    if (data.getData() != null) {
                        captureUri = data.getData();
                    } else {
                        captureUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                    }
                    if (captureUri != null && !StringUtils.isNullOrBlanK(captureUri.toString())) {
                        Intent intent = new Intent(PersonalInformationChangeActivity.this, CropImageActivity.class);
                        intent.putExtra("id", captureUri.toString());
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
            LogUtils.e("裁剪", "回来");
            if (data != null) {
                imageUrl = data.getStringExtra("bitmap");
                LogUtils.e(imageUrl);
                if (new File(imageUrl).exists()) {
                    LogUtils.e("回来成功");
                }
                if (!StringUtils.isNullOrBlanK(imageUrl)) {
                    Glide.with(this).load(Uri.fromFile(new File(imageUrl))).transform(new GlideCircleTransform(this)).crossFade().into(headsculpture);
                }
            }
        }
    }
}
