package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import libraryextra.utils.BitmapUtil;
import libraryextra.view.TagViewPager;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity {

    private TagViewPager viewPager;
    private int width;
    private int height;
    private int imageIds[] = {R.mipmap.index1, R.mipmap.index2,R.mipmap.index3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;//宽度
        height = dm.heightPixels;//高度


        setContentView(R.layout.activity_guide);
        viewPager = (TagViewPager) findViewById(R.id.tagViewPager);
        viewPager.init(R.drawable.shape_photo_tag_select, R.drawable.shape_photo_tag_nomal, 16, 8, 2, 40);
        viewPager.setAutoNext(false, 0);
//        viewPager.setId(1252);
        viewPager.setOnGetView((container, position) -> {
            ImageView iv = new ImageView(GuideActivity.this);
            iv.setClickable(true);
            iv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            iv.setId(position);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
//				iv.setImageResource(imageIds[position]);
            iv.setImageBitmap(BitmapUtil.decodeBitmapFromResource(getResources(), imageIds[position], width, height));
            container.addView(iv);
            if (position == imageIds.length - 1) {
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                        GuideActivity.this.startActivity(intent);
                        getSharedPreferences("first", MODE_PRIVATE).edit().putBoolean("firstlogin",false).commit();
                        GuideActivity.this.finish();
                    }
                });
            }
            return iv;
        });
        viewPager.setAdapter(imageIds.length, 0);
    }}

