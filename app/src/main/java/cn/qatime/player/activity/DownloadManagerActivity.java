package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.ListViewSelectAdapter;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.MyVideoThumbLoader;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.DataCleanUtils;

/**
 * Created by lenovo on 2017/8/22.
 */

public class DownloadManagerActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listView;
    private ListViewSelectAdapter<File> adapter;
    private List<File> list = new ArrayList<>();
    private TextView rightText;
    private ImageView rightImage;
    private View bottomLayout;
    private Button selectAll;
    private Button deleteAll;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final boolean singleMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager);
        setTitles("我的下载");
        rightText = (TextView) findViewById(R.id.right_text);
        rightImage = (ImageView) findViewById(R.id.right);
        bottomLayout = findViewById(R.id.bottom_layout);
        rightText.setText("取消");
        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRight();
            }
        });
        rightImage.setImageResource(R.mipmap.trash);
        rightImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRight();
            }
        });

        selectAll = (Button) findViewById(R.id.select_all);
        deleteAll = (Button) findViewById(R.id.delete_all);

        selectAll.setOnClickListener(this);
        deleteAll.setOnClickListener(this);
        initView();
    }

    private void updateRight() {
        if (adapter.isCheckboxShow()) {
            adapter.showCheckbox(false);
            rightText.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
            rightImage.setVisibility(View.VISIBLE);
        } else {
            adapter.showCheckbox(true);
            rightImage.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.VISIBLE);
            rightText.setVisibility(View.VISIBLE);
        }
    }


    private void initView() {
        File File = new File(Constant.FILEPATH);
        if(!File.exists()){
            File.mkdirs();
        }
        getFilesList(File);
        listView = (PullToRefreshListView) findViewById(R.id.list);
        listView.setEmptyView(View.inflate(this, R.layout.empty_view, null));
        adapter = new ListViewSelectAdapter<File>(this, list, R.layout.item_file_download_manager, singleMode) {
            MyVideoThumbLoader mVideoThumbLoader = new MyVideoThumbLoader();

            @Override
            public void convert(ViewHolder helper, File item, int position) {
                helper.setText(R.id.name, getItem(position).getName());
                helper.setText(R.id.size, DataCleanUtils.getFormatSize(getItem(position).length()));
                helper.setText(R.id.time, "下载时间:" + parse.format(new Date(getItem(position).lastModified())));
                String extName = item.getName().substring(item.getName().lastIndexOf(".") + 1, item.getName().length());

                if (extName.equals("doc") || extName.equals("docx")) {
                    helper.setImageResource(R.id.image, R.mipmap.word);
                } else if (extName.equals("xls") || extName.equals("xlsx")) {
                    helper.setImageResource(R.id.image, R.mipmap.excel);
                } else if (extName.equals("pdf")) {
                    helper.setImageResource(R.id.image, R.mipmap.pdf);
                } else if (extName.equals("mp4")) {
                    mVideoThumbLoader.showThumbByAsyncTask(item, (ImageView) helper.getView(R.id.image));
//                    holder.setImageBitmap(R.id.image, ImageUtil.getVideoThumbnail(item.getFile_url()));
                } else if (extName.equals("jpg") || extName.equals("png")) {
                    Glide.with(DownloadManagerActivity.this).load(item).placeholder(R.mipmap.unknown).centerCrop().crossFade().dontAnimate().into(((ImageView) helper.getView(R.id.image)));
                } else {
                    helper.setImageResource(R.id.image, R.mipmap.unknown);
                }
            }
        };
        adapter.setSelectListener(new ListViewSelectAdapter.SelectChangeListener<File>() {
            @Override
            public void update(File item, boolean isChecked) {
                int count = adapter.getSelectedList().size();
                deleteAll.setText(count == 0 ? "删除" : ("删除(" + count + ")"));
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!adapter.isCheckboxShow()) {
                    Intent intent = new Intent(DownloadManagerActivity.this, ExclusiveFileDetailActivity.class);
                    intent.putExtra("path", list.get(position - 1).getAbsolutePath());
                    startActivity(intent);
                } else {
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkedView);
                    checkBox.setChecked(!checkBox.isChecked());
                }
            }
        });
        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!adapter.isCheckboxShow()) {
                    updateRight();
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (adapter.isCheckboxShow()) {
            updateRight();
            return;
        }
        super.onBackPressed();
    }

    private void getFilesList(File file) {
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                getFilesList(files[i]);
            } else {
                list.add(files[i]);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_all:
                adapter.selectAll(!adapter.isSelectAll());
                adapter.notifyDataSetChanged();
                if (adapter.isSelectAll()) {
                    selectAll.setText("取消全选");
                } else {
                    selectAll.setText("全选");
                }
                break;
            case R.id.delete_all:
                for (File file : adapter.getSelectedList()) {
                    file.delete();
                    list.remove(file);
                }
                adapter.selectAll(false);
                adapter.notifyDataSetChanged();
                deleteAll.setText("删除");
                break;
        }
    }
}
