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

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information_change);
        initView();

        replace.setOnClickListener(this);
    }

    private void initView() {
        headsculpture = (ImageView) findViewById(R.id.head_sculpture);
        replace = (TextView) findViewById(R.id.replace);
        name = (EditText) findViewById(R.id.name);
        men = (RadioButton) findViewById(R.id.men);
        women = (RadioButton) findViewById(R.id.women);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        spinner = (Spinner) findViewById(R.id.spinner);
        complete = (TextView) findViewById(R.id.complete);
        men.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.replace://去选择图片
                Intent intent = new Intent(PersonalInformationChangeActivity.this,PictureSelectActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== Constant.RESPONSE_CAMERA){//拍照返回的照片
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
        }else if (true){
            LogUtils.e("裁剪", "回来");
            if (data != null) {
                String imageUrl = data.getStringExtra("bitmap");
                if (!StringUtils.isNullOrBlanK(imageUrl)) {
//                    postFile(imageUrl);
                }
            }
        }
    }
}
