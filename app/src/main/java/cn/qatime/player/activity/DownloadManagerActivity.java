package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.DownloadManagerFileAdapter;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;

/**
 * Created by lenovo on 2017/8/22.
 */

public class DownloadManagerActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listView;
    private DownloadManagerFileAdapter adapter;
    private List<File> list = new ArrayList<>();
    private TextView rightText;
    private ImageView rightImage;
    private View bottomLayout;
    private Button selectAll;
    private Button deleteAll;

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
        rightImage.setImageResource(R.mipmap.calendar);
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
        getFilesList(File);
        listView = (PullToRefreshListView) findViewById(R.id.list);
        adapter = new DownloadManagerFileAdapter(this, list, R.layout.item_file_download_manager);
        adapter.setSelectListener(new DownloadManagerFileAdapter.SelectChangeListener() {
            @Override
            public void update(int count) {
                deleteAll.setText("删除(" + (count==0?"":count) + ")");
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
                if (adapter.isSelectAll()) {
                    selectAll.setText("取消全选");
                } else {
                    selectAll.setText("全选");
                }
                break;
            case R.id.delete_all:
                adapter.removeFile();
                break;
        }
    }
}
