package cn.qatime.player.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.CommonAdapter;
import cn.qatime.player.adapter.PictureSelectAdaper;
import cn.qatime.player.adapter.ViewHolder;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.ImageBucket;
import cn.qatime.player.bean.ImageItem;
import cn.qatime.player.utils.AlbumHelper;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.LogUtils;

/**
 * @author luntify
 * @date 2016/8/10 20:36
 * @Description 图片选择页面
 */
public class PictureSelectActivity extends BaseActivity {

    private List<ImageItem> detailList = new ArrayList<>();
    private Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
        }
    };
    private AlbumHelper helper;
    private GridView gridView;
    private PictureSelectAdaper adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_select);

        initView();
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        getImages();
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new PictureSelectAdaper(this, detailList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // ##########拍照##########
                    Intent newIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(newIntent, Constant.REQUEST_CAMARE);
                } else {

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==Constant.REQUEST_CAMARE){//拍照返回
            setResult(Constant.RESPONSE_CAMERA,data);
            finish();
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
//        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ImageBucket> list = helper.getImagesBucketList(false);
                detailList.clear();
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < list.get(i).imageList.size(); j++) {
                        detailList.add(list.get(i).imageList.get(j));
                    }
                }
                hd.sendEmptyMessage(1);
            }
        }).start();


    }
}
