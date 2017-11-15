package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.TeacherSearchBean;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2017/6/9 15:57
 * @Describe
 */

public class TeacherSearchActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshListView listView;
    private TextView all;
    private TextView high;
    private TextView middle;
    private TextView primary;
    private TextView subject;
    private CommonAdapter<TeacherSearchBean.DataBean> adapter;
    private List<TeacherSearchBean.DataBean> datas = new ArrayList<>();
    private String category = "";
    private String subjectValue = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_search);
        setTitles("全部老师");
        initView();
        initData();
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        if (!StringUtils.isNullOrBlanK(category)) {
            map.put("category_eq", category);
        }
        if (!StringUtils.isNullOrBlanK(subjectValue)) {
            map.put("subject_eq", subjectValue);
        }
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.getUrl(UrlUtils.teachers, map), null, new VolleyListener(TeacherSearchActivity.this) {
            @Override
            protected void onTokenOut() {

            }

            @Override
            protected void onSuccess(JSONObject response) {
                datas.clear();
                String label = DateUtils.formatDateTime(TeacherSearchActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                listView.onRefreshComplete();
                TeacherSearchBean data = JsonUtils.objectFromJson(response.toString(), TeacherSearchBean.class);
                assert data != null;
                datas.addAll(data.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onError(JSONObject response) {

            }
        }, new VolleyErrorListener());
        addToRequestQueue(request);
    }

    private void initView() {
        setRightImage(R.mipmap.search, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherSearchActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        all = (TextView) findViewById(R.id.all);
        high = (TextView) findViewById(R.id.high);
        middle = (TextView) findViewById(R.id.middle);
        primary = (TextView) findViewById(R.id.primary);
        subject = (TextView) findViewById(R.id.subject);
        all.setOnClickListener(this);
        high.setOnClickListener(this);
        middle.setOnClickListener(this);
        primary.setOnClickListener(this);
        subject.setOnClickListener(this);
        listView = (PullToRefreshListView) findViewById(R.id.list);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));
        listView.setEmptyView(View.inflate(TeacherSearchActivity.this, R.layout.empty_view, null));
        adapter = new CommonAdapter<TeacherSearchBean.DataBean>(TeacherSearchActivity.this, datas, R.layout.item_teacher_search) {
            @Override
            public void convert(ViewHolder holder, TeacherSearchBean.DataBean item, int position) {
                Glide.with(TeacherSearchActivity.this).load(item.getEx_big_avatar_url()).bitmapTransform(new GlideCircleTransform(TeacherSearchActivity.this)).placeholder(R.mipmap.error_header).crossFade().into((ImageView) holder.getView(R.id.image));
                if (!StringUtils.isNullOrBlanK(item.getName())) {
                    holder.setText(R.id.name, item.getName());
                }
                holder.setImageResource(R.id.sex, "male".equals(item.getGender()) ? R.mipmap.male : R.mipmap.female);
                if (!StringUtils.isNullOrBlanK(item.getTeaching_years())) {
                    holder.setText(R.id.teaching_years, getTeachingYear(item.getTeaching_years()));
                }
                StringBuilder info = new StringBuilder();
                if (!StringUtils.isNullOrBlanK(item.getCategory())) {
                    info.append(item.getCategory());
                }
                if (!StringUtils.isNullOrBlanK(item.getSubject())) {
                    info.append(item.getSubject());
                }
                info.append(" | ");
                if (!StringUtils.isNullOrBlanK(item.getProvince())) {
                    info.append(item.getProvince());
                }
                if (!StringUtils.isNullOrBlanK(item.getCity())) {
                    info.append(item.getCity());
                }
                if (!StringUtils.isNullOrBlanK(item.getSchool())) {
                    info.append(item.getSchool());
                }

                holder.setText(R.id.info, info.toString());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TeacherSearchActivity.this, TeacherDataActivity.class);
                intent.putExtra("teacherId", datas.get(position - 1).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all:
                all.setTextColor(0xffff5842);
                high.setTextColor(0xff666666);
                middle.setTextColor(0xff666666);
                primary.setTextColor(0xff666666);
                category = "";
                initData();
                break;
            case R.id.high:
                all.setTextColor(0xff666666);
                high.setTextColor(0xffff5842);
                middle.setTextColor(0xff666666);
                primary.setTextColor(0xff666666);
                try {
                    category = URLEncoder.encode(high.getText().toString(), "UTF-8");
                    initData();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.middle:
                all.setTextColor(0xff666666);
                high.setTextColor(0xff666666);
                middle.setTextColor(0xffff5842);
                primary.setTextColor(0xff666666);
                try {
                    category = URLEncoder.encode(middle.getText().toString(), "UTF-8");
                    initData();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.primary:
                all.setTextColor(0xff666666);
                high.setTextColor(0xff666666);
                middle.setTextColor(0xff666666);
                primary.setTextColor(0xffff5842);
                try {
                    category = URLEncoder.encode(primary.getText().toString(), "UTF-8");
                    initData();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.subject:
                showPopupWindow(subject);
                break;
        }
    }

    public void showPopupWindow(View parent) {
        //加载布局
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(TeacherSearchActivity.this).inflate(R.layout.dialog_teacher_search, null);
        //找到布局的控件
        GridView grid = (GridView) layout.findViewById(R.id.grid);
        //设置适配器
        final List<String> data = new ArrayList<>();
        data.add("全科");
        data.add("语文");
        data.add("数学");
        data.add("英语");
        data.add("物理");
        data.add("化学");
        data.add("生物");
        data.add("地理");
        data.add("历史");
        data.add("政治");
        data.add("科学");
        grid.setAdapter(new CommonAdapter<String>(TeacherSearchActivity.this, data, R.layout.item_text) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.text, item);
            }
        });
        // 实例化popupWindow
        final PopupWindow popupWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //控制键盘是否可以获得焦点
        popupWindow.setFocusable(true);
        //设置popupWindow弹出窗体的背景
        popupWindow.showAsDropDown(parent, 0, 1);
        //监听
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                subject.setText("  " + data.get(arg2));
                if (arg2 == 0) {
                    subjectValue = "";
                } else {
                    try {
                        subjectValue = URLEncoder.encode(data.get(arg2), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                initData();
                popupWindow.dismiss();
            }
        });
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
}
