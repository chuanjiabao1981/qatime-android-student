package cn.qatime.player.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UpLoadUtil;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.GradeBean;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.DialogUtils;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.view.CustomProgressDialog;
import libraryextra.view.MDatePickerDialog;


public class RegisterPerfectActivity extends BaseActivity implements View.OnClickListener {
    ImageView headsculpture;
    TextView sethead;
    EditText name;
    RadioButton men;
    RadioButton women;
    RadioGroup radiogroup;
    Spinner spinner;
    TextView complete;
    private String imageUrl = "";
    private TextView birthday;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
    private String select = "";//生日所选日期
    private GradeBean gradeBean;
    private CustomProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_perfect);
        setTitle(getResources().getString(R.string.information_perfect));
        initView();
        String gradeString = FileUtil.readFile(getCacheDir() + "/grade.txt");
        if (!StringUtils.isNullOrBlanK(gradeString)) {
            gradeBean = JsonUtils.objectFromJson(gradeString, GradeBean.class);
        }

        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinner, gradeBean.getData().getGrades()));

        sethead.setOnClickListener(this);
        birthday.setOnClickListener(this);
        complete.setOnClickListener(this);
        PersonalInformationBean data = (PersonalInformationBean) getIntent().getSerializableExtra("data");
        if (data != null && data.getData() != null) {
            initData(data);
        }

    }

    private void initData(PersonalInformationBean data) {
        Glide.with(RegisterPerfectActivity.this).load(data.getData().getAvatar_url()).placeholder(R.mipmap.personal_information_head).transform(new GlideCircleTransform(RegisterPerfectActivity.this)).crossFade().into(headsculpture);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.replace://去选择图片
//                final Intent intent = new Intent(RegisterPerfectActivity.this, PictureSelectActivity.class);
//                startActivityForResult(intent, Constant.REQUEST_PICTURE_SELECT);
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
//                dataDialog.show();
                break;
            case R.id.complete://完成
                UpLoadUtil util = new UpLoadUtil(RegisterPerfectActivity.this) {
                    @Override
                    public void httpStart() {
                        progress = DialogUtils.startProgressDialog(progress, RegisterPerfectActivity.this);
                        progress.setCanceledOnTouchOutside(false);
                        progress.setCancelable(false);
                    }

                    @Override
                    protected void httpSuccess(String result) {
                        Intent data = new Intent();
                        data.putExtra("data", result);
                        setResult(Constant.RESPONSE, data);
                        DialogUtils.dismissDialog(progress);
                        Toast.makeText(RegisterPerfectActivity.this, getResources().getString(R.string.change_information_successful), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    protected void httpFailed(String result) {

                    }
                };
                String url = UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/update";
//                String filePath = imageUrl;
                if (StringUtils.isNullOrBlanK(BaseApplication.getUserId())) {
                    Toast.makeText(RegisterPerfectActivity.this, getResources().getString(R.string.id_is_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                String sName = name.getText().toString();
                if (StringUtils.isNullOrBlanK(sName)) {
                    Toast.makeText(this, getResources().getString(R.string.name_can_not_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                String grade = gradeBean.getData().getGrades().get(spinner.getSelectedItemPosition());
                if (StringUtils.isNullOrBlanK(grade)) {
                    Toast.makeText(this, getResources().getString(R.string.grade_can_not_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                String gender = radiogroup.getCheckedRadioButtonId() == men.getId() ? "male" : "female";
                String birthday = select.equals(parse.format(new Date())) ? "" : select;

                util.execute(url, /*filePath,*/ sName, grade, gender, birthday);
                break;
        }
    }

    private void initView() {
        headsculpture = (ImageView) findViewById(R.id.head_sculpture);
        sethead = (TextView) findViewById(R.id.set_head);
        name = (EditText) findViewById(R.id.name);
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
        men = (RadioButton) findViewById(R.id.men);
        women = (RadioButton) findViewById(R.id.women);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        spinner = (Spinner) findViewById(R.id.spinner);
        birthday = (TextView) findViewById(R.id.birthday);
        complete = (TextView) findViewById(R.id.complete);
    }

}
