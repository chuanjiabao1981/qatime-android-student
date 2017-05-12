package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.SchoolBean;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;

/**
 * @author Tianhaoranly
 * @date 2017/5/8 16:18
 * @Description:
 */
public class SchoolSelectActivity extends BaseActivity {
    private List<SchoolBean.Data> schoolList;
    private ListView list;
    private CommonAdapter<SchoolBean.Data> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_select);
        setTitles("选择学校");


        initView();
        initData();
    }

    private void initData() {
        SchoolBean schoolBean = JsonUtils.objectFromJson(FileUtil.readFile(getFilesDir() + "/school.txt").toString(), SchoolBean.class);
        if (schoolBean != null && schoolBean.getData() != null) {
            schoolList.addAll(schoolBean.getData());
        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        list = (ListView) findViewById(R.id.list);
        list.setEmptyView(View.inflate(this, R.layout.empty_view, null));
        schoolList = new ArrayList<>();
        adapter = new CommonAdapter<SchoolBean.Data>(SchoolSelectActivity.this, schoolList, R.layout.item_region) {

            @Override
            public void convert(ViewHolder holder, SchoolBean.Data item, int position) {
                holder.setText(R.id.region_text, item.getName());
            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra("school", schoolList.get(position));
                setResult(Constant.RESPONSE_SCHOOL_SELECT, data);
                finish();
            }
        });
    }
}
