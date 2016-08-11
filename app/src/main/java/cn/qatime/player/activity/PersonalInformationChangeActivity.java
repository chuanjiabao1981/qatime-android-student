package cn.qatime.player.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.net.URI;
import java.text.ParseException;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.ImageItem;
import cn.qatime.player.bean.PersonalInformationBean;
import cn.qatime.player.transformation.GlideCircleTransform;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.StringUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information_change);
        initView();
        replace.setOnClickListener(this);

        PersonalInformationBean data = (PersonalInformationBean) getIntent().getSerializableExtra("data");
        if (data != null && data.getData() != null) {
            Glide.with(PersonalInformationChangeActivity.this).load(data.getData().getSmall_avatar_url()).placeholder(R.drawable.personal_information_head).transform(new GlideCircleTransform(PersonalInformationChangeActivity.this)).crossFade().into(headsculpture);
            name.setText(data.getData().getName());
            if (!StringUtils.isNullOrBlanK(data.getData().getGender())) {
                if (data.getData().getGender().equals("male")) {
                   men.setChecked(true);
                    women.setChecked(false);
                } else {
                    men.setChecked(false);
                    women.setChecked(true);
                }
            }
//            try {
//                birthday.setText(format.format(parse.parse(data.getData().getBirthday())));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            if (!StringUtils.isNullOrBlanK(data.getData().getGrade())) {
//                grade.setText(data.getData().getGrade());
            }
//            if (!StringUtils.isNullOrBlanK(data.getData().getProvince()) && !StringUtils.isNullOrBlanK(data.getData().getCity())) {
//                region.setText(data.getData().getProvince() + " " + data.getData().getCity());
//            }
//                            school
            describe.setText(data.getData().getDesc());
        }
    }

    private void initView() {
        headsculpture = (ImageView) findViewById(R.id.head_sculpture);
        replace = (TextView) findViewById(R.id.replace);
        name = (EditText) findViewById(R.id.name);
        men = (RadioButton) findViewById(R.id.men);
        women = (RadioButton) findViewById(R.id.women);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        spinner = (Spinner) findViewById(R.id.spinner);
        describe = (EditText) findViewById(R.id.describe);
        complete = (TextView) findViewById(R.id.complete);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.replace://去选择图片
                Intent intent = new Intent(PersonalInformationChangeActivity.this, PictureSelectActivity.class);
                startActivityForResult(intent, Constant.REQUEST_PICTURE_SELECT);
                break;
        }
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
//                    LogUtils.e(image.imageId);
//                    LogUtils.e(image.imagePath);
//                    LogUtils.e(image.thumbnailPath);
//                    if (!new File("file://" + image.imagePath).exists()) {
//                        Toast.makeText(this, "您所选图片不可用", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
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
                String imageUrl = data.getStringExtra("bitmap");
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
