package cn.qatime.player.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.config.UserPreferences;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.cache.UserInfoCache;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UpLoadUtil;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.WheelView;
import libraryextra.bean.GradeBean;
import libraryextra.bean.ImageItem;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.DialogUtils;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.SPUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.CustomProgressDialog;
import libraryextra.view.MDatePickerDialog;


public class RegisterPerfectActivity extends BaseActivity implements View.OnClickListener {
    ImageView headsculpture;
    TextView sethead;
    EditText name;
    RadioButton men;
    RadioButton women;
    RadioGroup radiogroup;
    TextView textGrade;
    TextView complete;
    private String imageUrl = "";
    private TextView birthday;
    private View birthdayView;
    private View textGradeView;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

    private String select = "1990-01-01";//生日所选日期
    private GradeBean gradeBean;
    private CustomProgressDialog progress;
    private View changeHeadSculpture;
    private Uri captureUri;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_perfect);
        setTitle(getResources().getString(R.string.information_perfect));
        initView();

        String gradeString = FileUtil.readFile(getFilesDir() + "/grade.txt");
        if (!StringUtils.isNullOrBlanK(gradeString)) {
            gradeBean = JsonUtils.objectFromJson(gradeString, GradeBean.class);
        }

        changeHeadSculpture.setOnClickListener(this);
        textGradeView.setOnClickListener(this);
        birthdayView.setOnClickListener(this);
        textGrade.setOnClickListener(this);
        complete.setOnClickListener(this);
        PersonalInformationBean data = (PersonalInformationBean) getIntent().getSerializableExtra("data");
        if (data != null && data.getData() != null) {
            initData(data);
        }

    }

    private void initData(PersonalInformationBean data) {


        Editable etext = name.getText();
        Selection.setSelection(etext, etext.length());
        birthday.setText(format.format(new Date()));
        select = parse.format(new Date());


        for (int i = 0; i < gradeBean.getData().getGrades().size(); i++) {
            if (data.getData().getGrade().equals(gradeBean.getData().getGrades().get(i))) {
                textGrade.setText(data.getData().getGrade());
                break;

            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grade_view:
                showGradePickerDialog();
                break;
            case R.id.change_head_sculpture://去选择图片
                final Intent intent = new Intent(RegisterPerfectActivity.this, PictureSelectActivity.class);
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
                    }, parse.parse(select).getYear() + 1900, parse.parse(select).getMonth() + 1, parse.parse(select).getDay());
                    dataDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    dataDialog.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                break;
            case R.id.complete://完成
                String url = UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/profile";

                UpLoadUtil util = new UpLoadUtil(url) {
                    @Override
                    public void httpStart() {
                        progress = DialogUtils.startProgressDialog(progress, RegisterPerfectActivity.this);
                        progress.setCanceledOnTouchOutside(false);
                        progress.setCancelable(false);
                    }

                    @Override
                    protected void httpSuccess(final String result) {
                        Intent data = new Intent();
                        data.putExtra("data", result);
                        DialogUtils.dismissDialog(progress);
                        Toast.makeText(RegisterPerfectActivity.this, getResourceString(R.string.regiset_success), Toast.LENGTH_SHORT).show();
                        Map<String, String> map = new HashMap<>();
                        Intent intent = getIntent();
                        final String username = intent.getStringExtra("username");
                        String password = intent.getStringExtra("password");
                        map.put("login_account", username);
                        map.put("password", password);
                        map.put("client_type", "app");
                        map.put("client_cate", "student_client");
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlLogin, map), null,
                                new VolleyListener(RegisterPerfectActivity.this) {
                                    @Override
                                    protected void onTokenOut() {
                                        tokenOut();
                                    }

                                    @Override
                                    protected void onSuccess(JSONObject response) {
                                        try {
                                            JSONObject data = response.getJSONObject("data");
                                            if (data.has("result")) {
                                                if (data.getString("result") != null && data.getString("result").equals("failed")) {
                                                    Toast.makeText(RegisterPerfectActivity.this, getResources().getString(R.string.account_or_password_error), Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Logger.e("登录", response.toString());
                                                SPUtils.put(RegisterPerfectActivity.this, "username", username);
                                                Profile profile = JsonUtils.objectFromJson(response.toString(), Profile.class);
                                                if (profile != null && !TextUtils.isEmpty(profile.getData().getRemember_token())) {
                                                    BaseApplication.setProfile(profile);
                                                    loginAccount();
                                                } else {
                                                    //没有数据或token
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    protected void onError(JSONObject response) {
                                        Toast.makeText(RegisterPerfectActivity.this, getResourceString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                                    }
                                }, new VolleyErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                super.onErrorResponse(volleyError);
                            }
                        });
                        addToRequestQueue(request);
                    }

                    @Override
                    protected void httpFailed(String result) {
                        Toast.makeText(RegisterPerfectActivity.this, getResourceString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        DialogUtils.dismissDialog(progress);
                    }
                };

                if (StringUtils.isNullOrBlanK(imageUrl) || (!StringUtils.isNullOrBlanK(imageUrl) && !new File(imageUrl).exists())) {
                    Toast.makeText(this, getResourceString(R.string.please_set_head), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isNullOrBlanK(BaseApplication.getUserId())) {
                    Toast.makeText(RegisterPerfectActivity.this, getResources().getString(R.string.id_is_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                String sName = name.getText().toString().trim();
                if (StringUtils.isNullOrBlanK(sName)) {
                    Toast.makeText(this, getResources().getString(R.string.name_can_not_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                String grade = textGrade.getText().toString();
                if (StringUtils.isNullOrBlanK(grade)) {
                    Toast.makeText(this, getResources().getString(R.string.grade_can_not_be_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                String gender = radiogroup.getCheckedRadioButtonId() == men.getId() ? "male" : "female";
                String birthday = select.equals(parse.format(new Date())) ? "" : select;
                Map<String, String> map = new HashMap<>();
                map.put("name", sName);
                map.put("grade", grade);
                map.put("avatar", imageUrl);
                map.put("gender", gender);
                map.put("birthday", birthday);

                util.execute(map);
                break;
        }
    }
    private void showGradePickerDialog() {
        if (alertDialog == null) {
            final View view = View.inflate(RegisterPerfectActivity.this, R.layout.dialog_grade_picker, null);
            final WheelView grade = (WheelView) view.findViewById(R.id.grade);
            grade.setOffset(1);
            grade.setItems(gradeBean.getData().getGrades());
            grade.setSeletion(gradeBean.getData().getGrades().indexOf(textGrade.getText()));
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPerfectActivity.this);
            alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setContentView(view);
            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    textGrade.setText(grade.getSeletedItem());
                }
            });
            alertDialog.getWindow().setLayout(DensityUtils.dp2px(RegisterPerfectActivity.this, 350), ActionBar.LayoutParams.WRAP_CONTENT);
        } else {
            alertDialog.show();
        }
    }
    private void loginAccount() {
        String account = BaseApplication.getAccount();
        String token = BaseApplication.getAccountToken();

        if (!StringUtils.isNullOrBlanK(account) && !StringUtils.isNullOrBlanK(token)) {
            NIMClient.getService(AuthService.class).login(new LoginInfo(account, token)).setCallback(new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo o) {
                    Logger.e("云信登录成功" + o.getAccount());
                    // 初始化消息提醒
                    NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

                    NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());
                    //缓存
                    UserInfoCache.getInstance().clear();
                    TeamDataCache.getInstance().clear();
                    //                FriendDataCache.getInstance().clear();

                    UserInfoCache.getInstance().buildCache();
                    TeamDataCache.getInstance().buildCache();
                    //好友维护,目前不需要
                    //                FriendDataCache.getInstance().buildCache();

                    UserInfoCache.getInstance().registerObservers(true);
                    TeamDataCache.getInstance().registerObservers(true);
//                                                FriendDataCache.getInstance().registerObservers(true);

                    Intent intent = new Intent(RegisterPerfectActivity.this, MainActivity.class);
                    startActivity(intent);
                    setResult(Constant.REGIST);
                    finish();
                }

                @Override
                public void onFailed(int code) {
                    BaseApplication.clearToken();
                    Logger.e(code + "code");
                    if (code == 302 || code == 404) {
                        Toast.makeText(RegisterPerfectActivity.this, R.string.account_or_password_error, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterPerfectActivity.this, getResourceString(R.string.login_failed) + code, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onException(Throwable throwable) {
                    Logger.e(throwable.getMessage());
                    BaseApplication.clearToken();
                }
            });
        } else {//没有云信账号,直接登录
            Intent intent = new Intent(RegisterPerfectActivity.this, MainActivity.class);
            startActivity(intent);
            setResult(Constant.REGIST);
            finish();
        }
    }

    private void initView() {
        headsculpture = (ImageView) findViewById(R.id.head_sculpture);
        changeHeadSculpture = findViewById(R.id.change_head_sculpture);
        sethead = (TextView) findViewById(R.id.set_head);
        name = (EditText) findViewById(R.id.name);
        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
            }
        });
        men = (RadioButton) findViewById(R.id.men);
        women = (RadioButton) findViewById(R.id.women);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        textGrade = (TextView) findViewById(R.id.text_grade);
        birthday = (TextView) findViewById(R.id.birthday);
        textGradeView = findViewById(R.id.grade_view);
        birthdayView = findViewById(R.id.birthday_view);
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
                        Intent intent = new Intent(this, CropImageActivity.class);
                        intent.putExtra("id", captureUri.toString());
                        startActivityForResult(intent, Constant.PHOTO_CROP);
                    }
                }
            } else if (resultCode == Constant.RESPONSE_PICTURE_SELECT) {//选择照片返回的照片
                if (data != null) {
                    ImageItem image = (ImageItem) data.getSerializableExtra("data");
                    if (image != null && !StringUtils.isNullOrBlanK(image.imageId)) {
                        Intent intent = new Intent(this, CropImageActivity.class);
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
        }
    }
}
