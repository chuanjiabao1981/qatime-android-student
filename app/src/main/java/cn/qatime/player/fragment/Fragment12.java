package cn.qatime.player.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.adapter.CommonAdapter;
import cn.qatime.player.adapter.ViewHolder;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.RemedialClassBean;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.ScreenUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VolleyErrorListener;

public class Fragment12 extends BaseFragment implements View.OnClickListener {

    LinearLayout timesort;
    TextView timetext;
    LinearLayout subjectsort;
    TextView subjecttext;
    LinearLayout classsort;
    TextView classtext;
    ImageView screen;
    GridView grid;
    private CommonAdapter<RemedialClassBean.Data> adapter;
    private List<RemedialClassBean.Data> list = new ArrayList<>();
    private PopupWindow pop;
    //按时间排列方式
    private String timesorttype = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment12, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        timesort = (LinearLayout) view.findViewById(R.id.time_sort);
        timetext = (TextView) view.findViewById(R.id.time_text);
        subjectsort = (LinearLayout) view.findViewById(R.id.subject_sort);
        subjecttext = (TextView) view.findViewById(R.id.subject_text);
        classsort = (LinearLayout) view.findViewById(R.id.class_sort);
        classtext = (TextView) view.findViewById(R.id.class_text);
        screen = (ImageView) view.findViewById(R.id.screen);
        timesort.setOnClickListener(this);
        subjectsort.setOnClickListener(this);
        classsort.setOnClickListener(this);
        screen.setOnClickListener(this);
        grid = (GridView) view.findViewById(R.id.grid);
        adapter = new CommonAdapter<RemedialClassBean.Data>(getActivity(), list, R.layout.item_fragment12) {
            @Override
            public void convert(ViewHolder helper, RemedialClassBean.Data item, int position) {
                ((ImageView) helper.getView(R.id.image)).setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(getActivity()) / 2, ScreenUtils.getScreenWidth(getActivity()) / 2));
//                Glide.with(getActivity()).load(item.getPush_address()).into(((ImageView) helper.getView(R.id.image)));
                helper.setText(R.id.name, item.getName());
                helper.setText(R.id.subject, item.getSubject());
                helper.setText(R.id.grade, item.getGrade());
            }
        };
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }
        });
    }

    /**
     * 加载数据
     */
    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("Remember-Token", BaseApplication.getProfile().getToken());
        if (!TextUtils.isEmpty(timesorttype)) {
            map.put("sort_by", timesorttype);
        }

        if (!subjecttext.getText().equals("科目")) {
            try {
                map.put("subject", URLEncoder.encode(subjecttext.getText().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (!classtext.getText().equals("年级")) {
            try {
                map.put("grade", URLEncoder.encode(classtext.getText().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass, map), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        LogUtils.e(jsonObject.toString());
                        try {
                            Gson gson = new Gson();
                            RemedialClassBean data = gson.fromJson(jsonObject.toString(), RemedialClassBean.class);
                            list.addAll(data.getData());
                            adapter.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    public void showPop(View popView) {

        pop = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pop.setBackgroundDrawable(new ColorDrawable());
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.downDialogstyle);
        pop.showAtLocation(getActivity().findViewById(R.id.fragmentlayout), Gravity.BOTTOM, 0, 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_sort://按时间排序
                View popView = View.inflate(getActivity(), R.layout.pop_fragment12, null);
                ListView listView = (ListView) popView.findViewById(R.id.list);
                final List<String> timeList = new ArrayList<>();
                timeList.add("综合排序");
                timeList.add("按价格-低到高");
                timeList.add("按价格-高到低");
                timeList.add("按购买人数");
                listView.setAdapter(new CommonAdapter<String>(getActivity(), timeList, R.layout.item_pop_fragment12) {
                    @Override
                    public void convert(ViewHolder holder, String item, int position) {
                        holder.setText(R.id.text, item);
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //TODO 待定 排序规则
                        timesorttype = "";
                        initData();
                        pop.dismiss();
                    }
                });
                showPop(popView);
                break;
            case R.id.subject_sort://按科目排序
                popView = View.inflate(getActivity(), R.layout.pop_fragment12, null);
                listView = (ListView) popView.findViewById(R.id.list);
                final List<String> subjectList = new ArrayList<>();
                subjectList.add("语文");
                subjectList.add("数学");
                subjectList.add("英语");
                subjectList.add("物理");
                subjectList.add("化学");
                subjectList.add("生物");
                subjectList.add("地理");
                subjectList.add("政治");
                subjectList.add("历史");
                subjectList.add("科学");
                listView.setAdapter(new CommonAdapter<String>(getActivity(), subjectList, R.layout.item_pop_fragment12) {
                    @Override
                    public void convert(ViewHolder holder, String item, int position) {
                        holder.setText(R.id.text, item);
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        subjecttext.setText(subjectList.get(position));
                        initData();
                        pop.dismiss();
                    }
                });
                showPop(popView);
                break;
            case R.id.class_sort://按班级排序
                popView = View.inflate(getActivity(), R.layout.pop_fragment12, null);
                listView = (ListView) popView.findViewById(R.id.list);
                final List<String> classList = new ArrayList<>();
                classList.add("高三");
                classList.add("高二");
                classList.add("高一");
                classList.add("初三");
                classList.add("初二");
                classList.add("初一");
                classList.add("六年级");
                classList.add("五年级");
                classList.add("四年级");
                classList.add("三年级");
                classList.add("二年级");
                classList.add("一年级");
                listView.setAdapter(new CommonAdapter<String>(getActivity(), classList, R.layout.item_pop_fragment12) {
                    @Override
                    public void convert(ViewHolder holder, String item, int position) {
                        holder.setText(R.id.text, item);
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        classtext.setText(classList.get(position));
                        initData();
                        pop.dismiss();
                    }
                });
                showPop(popView);
                break;

            case R.id.screen:
                popView = View.inflate(getActivity(), R.layout.pop_from_up_fragment12, null);
                Spinner spinner = (Spinner) popView.findViewById(R.id.spinner);
                spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                });
                break;
        }
    }
}
