package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.MyFilesBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.MyVideoThumbLoader;
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
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
            public MyVideoThumbLoader mVideoThumbLoader = new MyVideoThumbLoader();

            @Override
            public void convert(ViewHolder holder, MyFilesBean.DataBean item, int position) {
                holder.setText(R.id.name, item.getName());
                holder.setText(R.id.size, DataCleanUtils.getFormatSize(Double.valueOf(item.getFile_size())));
                holder.setText(R.id.time, "上传时间:" + parse.format(new Date(item.getCreated_at()*1000)));
                if (item.getExt_name().equals("doc") || item.getExt_name().equals("docx")) {
                    holder.setImageResource(R.id.image, R.mipmap.word);
                } else if (item.getExt_name().equals("xls") || item.getExt_name().equals("xlsx")) {
                    holder.setImageResource(R.id.image, R.mipmap.excel);
                }else if (item.getExt_name().equals("pdf")) {
                    holder.setImageResource(R.id.image, R.mipmap.pdf);
                } else if (item.getExt_name().equals("mp4")) {
                    mVideoThumbLoader.showThumbByAsyncTask(item.getFile_url(), (ImageView) holder.getView(R.id.image));
//                    holder.setImageBitmap(R.id.image, ImageUtil.getVideoThumbnail(item.getFile_url()));
                } else if (item.getExt_name().equals("jpg") || item.getExt_name().equals("png")) {
                    Glide.with(ExclusiveFilesActivity.this).load(item.getFile_url()).placeholder(R.mipmap.unknown).centerCrop().crossFade().dontAnimate().into(((ImageView) holder.getView(R.id.image)));
                } else {
                    holder.setImageResource(R.id.image, R.mipmap.unknown);
                }
            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExclusiveFilesActivity.this, ExclusiveFileDetailActivity.class);
                intent.putExtra("id", fileList.get(position - 1).getId());
                intent.putExtra("courseId", courseId);
                intent.putExtra("file", fileList.get(position-1));
                startActivity(intent);
            }
        });
    }
}
