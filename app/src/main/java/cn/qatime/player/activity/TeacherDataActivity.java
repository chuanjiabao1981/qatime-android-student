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

import java.text.DecimalFormat;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.TeacherDataBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.RemedialClassBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
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
    private List<TeacherDataBean.DataBean.Course> list1 = new ArrayList<>();
    private List<RemedialClassBean.Data> list2 = new ArrayList<>();
    private TextView teachAge;
    private TextView school;
    private TextView category;
    private TextView subject;
    private TextView province;
    private TextView city;
    private TextView town;
    private int page = 0;
    private CommonAdapter<TeacherDataBean.DataBean.Course> adapter1;
    private GridViewForScrollView gridSame;
    private CommonAdapter<RemedialClassBean.Data> adapter2;
    private PullToRefreshScrollView scroll;
    private CommonAdapter<TeacherDataBean.DataBean.Course> adapter;
    private DecimalFormat df = new DecimalFormat("#.00");

    private void assignViews() {
        scroll = (PullToRefreshScrollView) findViewById(R.id.scroll);
        scroll.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        banner = (ImageView) findViewById(R.id.banner);
        headSculpture = (ImageView) findViewById(R.id.head_sculpture);
        sex = (TextView) findViewById(R.id.sex);
        name = (TextView) findViewById(R.id.name);
        teachAge = (TextView) findViewById(R.id.teach_age);
        school = (TextView) findViewById(R.id.school);
        category = (TextView) findViewById(R.id.category);
        subject = (TextView) findViewById(R.id.subject);
        province = (TextView) findViewById(R.id.province);
        city = (TextView) findViewById(R.id.city);
        town = (TextView) findViewById(R.id.town);
        describe = (TextView) findViewById(R.id.describe);
        grid = (GridViewForScrollView) findViewById(R.id.grid);
        gridSame = (GridViewForScrollView) findViewById(R.id.grid_same);
        scroll.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initDataSame(2);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_data);
        assignViews();
        initData(1);
        initDataSame(1);
        initGrid();
    }

    private void initGrid() {
        adapter1 = new CommonAdapter<TeacherDataBean.DataBean.Course>(this, list1, R.layout.item_teacher_data) {

            @Override
            public void convert(ViewHolder helper, TeacherDataBean.DataBean.Course item, int position) {
                if (item == null) {
                    Logger.e("item數據空");
                    return;
                }
                Glide.with(TeacherDataActivity.this).load(item.getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().dontAnimate().into(((ImageView) helper.getView(R.id.image)));
                helper.setText(R.id.grade, item.getGrade());
                helper.setText(R.id.subject, item.getSubject());
                helper.setText(R.id.course_title, item.getName());
                String price = df.format(item.getPrice());
                if (price.startsWith(".")) {
                    price = "0" + price;
                }
                helper.setText(R.id.price, "￥" + price);
            }
        };
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TeacherDataActivity.this, RemedialClassDetailActivity.class);
                intent.putExtra("id", list1.get(position).getId());
                startActivity(intent);
            }
        });
        grid.setAdapter(adapter1);

        adapter2 = new CommonAdapter<RemedialClassBean.Data>(this, list2, R.layout.item_teacher_data) {

            @Override
            public void convert(ViewHolder helper,RemedialClassBean.Data item, int position) {
                if (item == null) {
                    Logger.e("item數據空");
                    return;
                }
                Glide.with(TeacherDataActivity.this).load(item.getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().dontAnimate().into(((ImageView) helper.getView(R.id.image)));
                helper.setText(R.id.grade, item.getGrade());
                helper.setText(R.id.subject, item.getSubject());
                helper.setText(R.id.course_title, item.getName());
                String price = df.format(item.getPrice());
                if (price.startsWith(".")) {
                    price = "0" + price;
                }
                helper.setText(R.id.price, "￥" + price);
            }
        };
        gridSame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TeacherDataActivity.this, RemedialClassDetailActivity.class);
                intent.putExtra("id", list2.get(position).getId());
                startActivity(intent);
            }
        });
        gridSame.setAdapter(adapter2);
    }

    /**
     * 相似课程数据初始化
     */
    private void initDataSame(final int type) {
        if (type == 1) {
            page = 1;
        }
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("per_page", "10");
        map.put("sort_by", "");
        try {
            map.put("subject", URLEncoder.encode(subject.getText().toString(), "UTF-8"));
//            map.put("grade", URLEncoder.encode(category.getText().toString(), "UTF-8"));
            // TODO: 2016/11/15 等待接口 通过category查询辅导班
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass, map), null,
                new VolleyListener(TeacherDataActivity.this) {


                    @Override
                    protected void onSuccess(JSONObject response) {
                        if (type == 1) {
                            list2.clear();
                        }
                        scroll.onRefreshComplete();

                        try {
                            RemedialClassBean data = JsonUtils.objectFromJson(response.toString(), RemedialClassBean.class);
                            list2.addAll(data.getData());
                            adapter2.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        scroll.onRefreshComplete();
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                scroll.onRefreshComplete();
            }
        });

        addToRequestQueue(request);
    }

    /**
     * @param type 1刷新
     *             2加载更多
     */
    private void initData(final int type) {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlTeacherInformation + getIntent().getIntExtra("teacherId", 0) + "/profile", null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Logger.e(response.toString());
                        if (type == 1) {
                            list1.clear();
                        }
                        try {
                            TeacherDataBean bean = JsonUtils.objectFromJson(response.toString(), TeacherDataBean.class);
                            if (bean != null && bean.getData() != null) {
                                String name = bean.getData().getName();
                                if (name != null) {
                                    setTitle(name);
                                    TeacherDataActivity.this.name.setText(name);
                                }
                                String desc = bean.getData().getDesc();
                                describe.setText(StringUtils.isNullOrBlanK(desc)?"暂无":desc);
                                teachAge.setText(getTeachingYear(bean.getData().getTeaching_years()));
                                category.setText(bean.getData().getCategory());
                                subject.setText(bean.getData().getSubject());
                                province.setText(bean.getData().getProvince());
                                city.setText(bean.getData().getCity());
                                town.setText(bean.getData().getTown());
                                sex.setText(getSex(bean.getData().getGender()));
                                sex.setTextColor(getSexColor(bean.getData().getGender()));
                                Glide.with(TeacherDataActivity.this).load(bean.getData().getAvatar_url()).placeholder(R.mipmap.error_header_rect).crossFade().into(headSculpture);
                                school.setText(bean.getData().getSchool());
                                list1.addAll(bean.getData().getCourses());
                            }
                            adapter1.notifyDataSetChanged();
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

    private int getSexColor(String gender) {
        if ("male".equals(gender)) {
            return 0xff00ccff;
        } else if ("female".equals(gender)) {
            return 0xffff9966;
        }
        return 0xffff9966;
    }

    private String getSex(String gender) {
        if ("male".equals(gender)) {
            return "♂";
        } else if ("female".equals(gender)) {
            return "♀";
        }
        return "";
    }

    private String getTeachingYear(String teaching_years) {
        switch (teaching_years) {
            case "within_three_years":
                return getResourceString(R.string.within_three_years) + "教龄";
            case "within_ten_years":
                return getResourceString(R.string.within_ten_years) + "教龄";
            case "within_twenty_years":
                return getResourceString(R.string.within_twenty_years) + "教龄";
        }
        return "";
    }

}
