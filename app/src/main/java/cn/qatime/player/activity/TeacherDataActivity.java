package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.TeacherDataBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.RemedialClassBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.GridViewForScrollView;

/**
 * @author lungtify
 * @Time 2016/10/25 16:23
 * @Describe
 */
public class TeacherDataActivity extends BaseActivity {
    private ImageView banner;
    private ImageView headSculpture;
    private TextView sex;
    private TextView name;
    private TextView describe;
    private GridViewForScrollView grid;
    private List<RemedialClassBean.Data> list = new ArrayList<>();
    private TextView teachAge;
    private TextView school;
    private int page = 0;
    private CommonAdapter<RemedialClassBean.Data> adapter;

    private void assignViews() {
        PullToRefreshScrollView scroll = (PullToRefreshScrollView) findViewById(R.id.scroll);
        scroll.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        banner = (ImageView) findViewById(R.id.banner);
        headSculpture = (ImageView) findViewById(R.id.head_sculpture);
        sex = (TextView) findViewById(R.id.sex);
        name = (TextView) findViewById(R.id.name);
        teachAge = (TextView) findViewById(R.id.teach_age);
        school = (TextView) findViewById(R.id.school);
        describe = (TextView) findViewById(R.id.describe);
        grid = (GridViewForScrollView) findViewById(R.id.grid);
        scroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_data);
        getIntent().getStringExtra("teacherId");
        assignViews();
        initData(1);
        adapter = new CommonAdapter<RemedialClassBean.Data>(this, list, R.layout.item_teacher_data) {

            @Override
            public void convert(ViewHolder helper, RemedialClassBean.Data item, int position) {
                if (item == null) {
                    Logger.e("item數據空");
                    return;
                }
                Glide.with(TeacherDataActivity.this).load(item.getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().dontAnimate().into(((ImageView) helper.getView(R.id.image)));
                helper.setText(R.id.subject, item.getSubject());
                helper.setText(R.id.title,item.getName());
                helper.setText(R.id.count, String.valueOf(item.getBuy_tickets_count()));
            }
        };
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TeacherDataActivity.this, RemedialClassDetailActivity.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }
        });
        grid.setAdapter(adapter);
    }

    /**
     * @param type 1刷新
     *             2加载更多
     */
    private void initData(final int type) {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlTeacherInformation + getIntent().getStringExtra("teacherId") + "/profile", null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Logger.e(response.toString());
                        if (type == 1) {
                            list.clear();
                        }
                        try {
                            TeacherDataBean bean = JsonUtils.objectFromJson(response.toString(), TeacherDataBean.class);
                            if (bean != null && bean.getData() != null) {
                                setTitle(bean.getData().getName());
                                name.setText(bean.getData().getName());
                                describe.setText(bean.getData().getDesc());
                                teachAge.setText(getTeachingYear(bean.getData().getTeaching_years()));
                                Glide.with(TeacherDataActivity.this).load(bean.getData().getAvatar_url()).placeholder(R.mipmap.personal_information_head).transform(new GlideCircleTransform(TeacherDataActivity.this)).crossFade().into(headSculpture);
                                school.setText(bean.getData().getSchool());
                                list.addAll(bean.getData().getCourses());
                            }
                            adapter.notifyDataSetChanged();
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

    private String getTeachingYear(String teaching_years) {
        switch (teaching_years) {
            case "within_three_years":
                return getResourceString(R.string.within_three_years);
            case "within_ten_years":
                return getResourceString(R.string.within_ten_years);
            case "within_twenty_years":
                return getResourceString(R.string.within_twenty_years);
        }
        return "";
    }

}
