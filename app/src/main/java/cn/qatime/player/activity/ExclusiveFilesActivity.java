package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.VolleyError;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.MyFilesBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.DataCleanUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2017/8/15.
 */

public class ExclusiveFilesActivity extends BaseActivity {

    private PullToRefreshListView list;
    private CommonAdapter<MyFilesBean.DataBean> adapter;
    private List<MyFilesBean.DataBean> fileList = new ArrayList<>();
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_files);
        setTitles("课件管理");
        courseId = getIntent().getIntExtra("id", 0);
        initView();
        initData();
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("cate", "");
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlGroups + courseId + "/files", map), null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        fileList.clear();
                        try {
                            MyFilesBean data = JsonUtils.objectFromJson(response.toString(), MyFilesBean.class);
                            if (data != null) {
                                fileList.addAll(data.getData());
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    private void initView() {
        list = (PullToRefreshListView) findViewById(R.id.list);
        list.setMode(PullToRefreshBase.Mode.BOTH);
        list.setEmptyView(View.inflate(this, R.layout.empty_view, null));
        adapter = new CommonAdapter<MyFilesBean.DataBean>(this, fileList, R.layout.item_exclusive_files) {
            @Override
            public void convert(ViewHolder helper, MyFilesBean.DataBean item, int position) {
                helper.setText(R.id.name, item.getName());
                helper.setText(R.id.size, DataCleanUtils.getFormatSize(Double.valueOf(item.getFile_size())));
//                helper.setText(R.id.time, item.getTime());
            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExclusiveFilesActivity.this, ExclusiveFileDetailActivity.class);
                intent.putExtra("id", fileList.get(position - 1).getId());
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });
    }
}
