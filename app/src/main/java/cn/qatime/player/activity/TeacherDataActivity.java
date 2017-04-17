package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.TeacherDataBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.transformation.GlideCircleTransform;
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
    //    private ImageView banner;
    private ImageView headSculpture;
    private ImageView sex;
    private TextView name;
    private TextView describe;
    private GridViewForScrollView grid;
    private List<TeacherDataBean.DataBean.Course> list = new ArrayList<>();
    private TextView teachAge;
    private TextView school;
    private TextView category;
    private TextView subject;
    private TextView province;
    private TextView city;
    private TextView town;
    private CommonAdapter<TeacherDataBean.DataBean.Course> adapter;
    private DecimalFormat df = new DecimalFormat("#.00");
    private int teacherId;
    private View relEmpty;

    private void assignViews() {
        PullToRefreshScrollView scroll = (PullToRefreshScrollView) findViewById(R.id.scroll);
        scroll.setMode(PullToRefreshBase.Mode.DISABLED);
//        banner = (ImageView) findViewById(R.id.banner);
        headSculpture = (ImageView) findViewById(R.id.head_sculpture);
        sex = (ImageView) findViewById(R.id.sex);
        name = (TextView) findViewById(R.id.name);
        teachAge = (TextView) findViewById(R.id.teach_age);
        school = (TextView) findViewById(R.id.school);
        category = (TextView) findViewById(R.id.category);
        subject = (TextView) findViewById(R.id.subject);
        province = (TextView) findViewById(R.id.province);
        city = (TextView) findViewById(R.id.city);
        town = (TextView) findViewById(R.id.town);
        relEmpty = findViewById(R.id.rel_empty);
        describe = (TextView) findViewById(R.id.describe);
        grid = (GridViewForScrollView) findViewById(R.id.grid);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_data);
        assignViews();

        teacherId = getIntent().getIntExtra("teacherId", 0);

        initData(1);
        adapter = new CommonAdapter<TeacherDataBean.DataBean.Course>(this, list, R.layout.item_teacher_data) {

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
                String price = df.format(item.getCurrent_price());
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
                intent.putExtra("id", list.get(position).getId());
                startActivityForResult(intent, Constant.REQUEST);
            }
        });
        grid.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int newTeacherId = intent.getIntExtra("teacherId", 0);
        if (newTeacherId != teacherId) {
            teacherId = newTeacherId;
            initData(1);
        }
    }

    /**
     * @param type 1刷新
     *             2加载更多
     */
    private void initData(final int type) {

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlTeacherInformation + teacherId + "/profile", null,
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
                                String name = bean.getData().getName();
                                if (name != null) {
                                    setTitles(name);
                                    TeacherDataActivity.this.name.setText(name);
                                }
                                describe.setText(StringUtils.isNullOrBlanK(bean.getData().getDesc()) ? getString(R.string.not_available) : bean.getData().getDesc());
                                teachAge.setText(getTeachingYear(bean.getData().getTeaching_years()));
                                category.setText(bean.getData().getCategory());
                                subject.setText(bean.getData().getSubject());
                                province.setText(bean.getData().getProvince());
                                city.setText(bean.getData().getCity());
                                town.setText(bean.getData().getTown());
                                sex.setImageResource("male".equals(bean.getData().getGender())?R.mipmap.male:R.mipmap.female);
//                                sex.setText(getSex(bean.getData().getGender()));
//                                sex.setTextColor(getSexColor(bean.getData().getGender()));
                                Glide.with(TeacherDataActivity.this).load(bean.getData().getAvatar_url()).transform(new GlideCircleTransform(TeacherDataActivity.this)).placeholder(R.mipmap.error_header_rect).crossFade().into(headSculpture);
                                school.setText(bean.getData().getSchool());
                                if (bean.getData().getCourses() != null && bean.getData().getCourses().size() > 0) {
                                    list.addAll(bean.getData().getCourses());
                                } else {
                                    relEmpty.setVisibility(View.VISIBLE);
                                }
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
                return getResourceString(R.string.within_three_years) + getString(R.string.teach_age);
            case "within_ten_years":
                return getResourceString(R.string.within_ten_years) + getString(R.string.teach_age);
            case "within_twenty_years":
                return getResourceString(R.string.within_twenty_years) + getString(R.string.teach_age);
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
