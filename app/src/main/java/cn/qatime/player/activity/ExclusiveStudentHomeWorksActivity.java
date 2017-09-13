package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.qatime.player.R;
import cn.qatime.player.adapter.ListViewSelectAdapter;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.StudentHomeWorksBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * Created by lenovo on 2017/9/8.
 * 专属课作业列表
 */

public class ExclusiveStudentHomeWorksActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listView;
    public ListViewSelectAdapter<StudentHomeWorksBean.DataBean> adapter;
    private List<StudentHomeWorksBean.DataBean> list = new ArrayList();
    private Set<StudentHomeWorksBean.DataBean> selectSet = new HashSet<>();
    private TextView rightText;
    private Button bottom;
    public final boolean singleMode = true;
    private int courseId;
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_students_homeworks);
        courseId = getIntent().getIntExtra("courseId", 0);
        initView();
        initData();
    }


    private void initView() {
        setTitles("课程作业");
        rightText = (TextView) findViewById(R.id.right_text);
        bottom = (Button) findViewById(R.id.bottom_button);
        bottom.setOnClickListener(this);

        listView = (PullToRefreshListView) findViewById(R.id.list);
        listView.setEmptyView(View.inflate(this, R.layout.empty_view, null));
        adapter = new ListViewSelectAdapter<StudentHomeWorksBean.DataBean>(this, list, R.layout.item_exclusive_homeworks, singleMode) {
            @Override
            public void convert(ViewHolder holder, StudentHomeWorksBean.DataBean item, int position) {
                long time = item.getHomework().getCreated_at() * 1000L;
                holder.setText(R.id.name, item.getTitle())
                        .setText(R.id.time,"创建时间"+parse.format(new Date(time)))
                        .setText(R.id.status,getStatus(item.getStatus()));
            }
        };
//        adapter.setSelectListener(new ListViewSelectAdapter.SelectChangeListener<StudentHomeWorksBean.DataBean>() {
//            @Override
//            public void update(StudentHomeWorksBean.DataBean item, boolean isChecked) {
//                ExclusiveHomeWorksActivity.this.update(item, isChecked);
//            }
//        });
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExclusiveStudentHomeWorksActivity.this, HomeWorkDetailActivity.class);
                intent.putExtra("item", list.get(position - 1));
                startActivityForResult(intent, Constant.REQUEST);
            }
        });
//        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                updateCheckbox();
//                return true;
//            }
//        });
    }

    private String getStatus(String status) {
        if ("pending".equals(status)) return "未交";
        else if ("submitted".equals(status)) return "待批";
        else return "已批";
    }

    private void initData() {
        //学生提交的作业
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlGroups + courseId + "/student_homeworks", null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        list.clear();
                        try {
                            StudentHomeWorksBean data = JsonUtils.objectFromJson(response.toString(), StudentHomeWorksBean.class);
                            if (data != null) {
                                list.addAll(data.getData());
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_button:

//                    Intent intent = new Intent(this, ExclusiveHomeWorkCreateActivity.class);
//                    intent.putExtra("courseId", courseId);
//                    startActivityForResult(intent, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initData();
    }
}

