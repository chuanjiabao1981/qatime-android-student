package cn.qatime.player.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
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
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.JsonUtils;
import cn.qatime.player.utils.KeyBoardUtils;
import cn.qatime.player.utils.MDatePickerDialog;
import cn.qatime.player.utils.ScreenUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VolleyErrorListener;
import cn.qatime.player.utils.VolleyListener;

public class Fragment12 extends BaseFragment implements View.OnClickListener {

    LinearLayout timesort;
    TextView timetext;
    LinearLayout subjectsort;
    TextView subjecttext;
    LinearLayout classsort;
    TextView classtext;
    ImageView screen;
    PullToRefreshGridView grid;
    private CommonAdapter<RemedialClassBean.Data> adapter;
    private List<RemedialClassBean.Data> list = new ArrayList<>();
    private PopupWindow pop;
    //按时间排列方式
    private String timesorttype = "";
    private View screenLayout;
    private EditText priceLow;
    private EditText priceHigh;
    private EditText subjectLow;
    private EditText subjectHigh;
    private TextView beginClassYear;
    private TextView beginClassMonth;
    private TextView beginClassDay;
    private TextView endcLassYear;
    private TextView endcLassMonth;
    private TextView endClassDay;
    private Spinner spinner;
    private Button cancel;
    private Button submit;


    //    //价格开始区间
//    private String price_floor = "";
//    //价格结束区间
//    private String price_ceil = "";
    //开课日期开始区间
    private String class_date_floor = "";
    //开课日期结束区间
    private String class_date_ceil = "";
    //辅导班状态
    private String status = "";
    private int page = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment12, container, false);
        initView(view);
        initData(1);
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

        grid = (PullToRefreshGridView) view.findViewById(R.id.grid);
        grid.setMode(PullToRefreshBase.Mode.BOTH);
        grid.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        grid.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        grid.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        grid.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        grid.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        grid.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));
        adapter = new CommonAdapter<RemedialClassBean.Data>(getActivity(), list, R.layout.item_fragment12) {
            @Override
            public void convert(ViewHolder helper, RemedialClassBean.Data item, int position) {
                ((ImageView) helper.getView(R.id.image)).setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(getActivity()) / 2, ScreenUtils.getScreenWidth(getActivity()) / 2));
//                Glide.with(getActivity()).load(item.getPush_address()).into(((ImageView) helper.getView(R.id.image)));
//                helper.setText(R.id.name, item.getName());
//                helper.setText(R.id.subject, item.getSubject());
//                helper.setText(R.id.grade, item.getGrade());
            }
        };

        grid.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                page = 1;
                initData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page++;
                initData(2);
            }
        });

        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }
        });

        //开课时间
        View beginClassLayout = view.findViewById(R.id.begin_class_layout);
        //筛选框
        screenLayout = view.findViewById(R.id.screen_layout);
        priceLow = (EditText) view.findViewById(R.id.price_low);
        priceHigh = (EditText) view.findViewById(R.id.price_high);

        subjectLow = (EditText) view.findViewById(R.id.subject_low);
        subjectHigh = (EditText) view.findViewById(R.id.subject_high);

        beginClassYear = (TextView) view.findViewById(R.id.begin_class_year);
        beginClassMonth = (TextView) view.findViewById(R.id.begin_class_month);
        beginClassDay = (TextView) view.findViewById(R.id.begin_class_day);
        View endClassLayout = view.findViewById(R.id.end_class_layout);
        endcLassYear = (TextView) view.findViewById(R.id.end_class_year);
        endcLassMonth = (TextView) view.findViewById(R.id.end_class_month);
        endClassDay = (TextView) view.findViewById(R.id.end_class_day);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        cancel = (Button) view.findViewById(R.id.cancel);
        submit = (Button) view.findViewById(R.id.submit);

        beginClassLayout.setOnClickListener(this);
        endClassLayout.setOnClickListener(this);
        screenLayout.setOnClickListener(this);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        status = "all";
                        break;
                    case 1:
                        status = "preview";
                        break;
                    case 2:
                        status = "teaching";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 加载数据
     *
     * @param type
     */
    private void initData(final int type) {
        if (type == 1) {
            page = 1;
        }
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("per_page", "10");
        if (!TextUtils.isEmpty(timesorttype)) {
            map.put("sort_by", timesorttype);
        }

        if (!subjecttext.getText().equals(getResources().getString(R.string.by_subject))) {
            try {
                map.put("subject", URLEncoder.encode(subjecttext.getText().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (!classtext.getText().equals(getResources().getString(R.string.by_grade))) {
            try {
                map.put("grade", URLEncoder.encode(classtext.getText().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        map.put("price_floor", priceLow.getText().toString());
        map.put("price_ceil", priceHigh.getText().toString());
        map.put("class_date_floor", subjectLow.getText().toString());
        map.put("class_date_ceil", subjectHigh.getText().toString());
        map.put("status", status);
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass, map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        if (type == 1) {
                            list.clear();
                        }
                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        grid.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                        grid.onRefreshComplete();

                        try {
                            RemedialClassBean data = JsonUtils.objectFromJson(response.toString(), RemedialClassBean.class);
                            list.addAll(data.getData());
                            adapter.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        clearScreenData();
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                grid.onRefreshComplete();
            }
        });

        addToRequestQueue(request);
    }

    /**
     * 请求完数据后，清空弹框内的数据
     */
    private void clearScreenData() {
        priceLow.setText("");
        priceHigh.setText("");
        subjectLow.setText("");
        subjectHigh.setText("");
        status = "";
        class_date_floor = "";
        class_date_ceil = "";
    }

    public void showPop(View popView) {
        if (screenLayout.getVisibility() == View.VISIBLE) {
            screenLayout.setVisibility(View.GONE);
        }
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
                        if (timeList.get(position).equals(getResources().getString(R.string.in_price_low_to_high))) {
                            timetext.setText(getResources().getString(R.string.by_price_up));
                        } else if (timeList.get(position).equals(getResources().getString(R.string.in_price_high_to_low))) {
                            timetext.setText(getResources().getString(R.string.by_price_down));
                        } else {
                            timetext.setText(timeList.get(position));
                        }

                        timesorttype = "";
                        initData(1);
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
                        initData(1);
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
                        initData(1);
                        pop.dismiss();
                    }
                });
                showPop(popView);
                break;

            case R.id.screen://筛选
                if (screenLayout.getVisibility() == View.VISIBLE) {
                    screenLayout.setVisibility(View.GONE);
                } else {
                    screenLayout.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.begin_class_layout://开课时间
                MDatePickerDialog dataDialog = new MDatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        class_date_floor = (year + "-" + ((monthOfYear + 1) >= 10 ? String.valueOf((monthOfYear + 1)) : ("0" + (monthOfYear + 1))) + "-" + ((dayOfMonth) >= 10 ? String.valueOf((dayOfMonth)) : ("0" + (dayOfMonth))));
                        beginClassYear.setText(String.valueOf(year));
                        beginClassMonth.setText((monthOfYear + 1) >= 10 ? String.valueOf((monthOfYear + 1)) : ("0" + (monthOfYear + 1)));
                        beginClassDay.setText((dayOfMonth) >= 10 ? String.valueOf((dayOfMonth)) : ("0" + (dayOfMonth)));
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dataDialog.show();
                break;
            case R.id.end_class_layout://开课时间end
                dataDialog = new MDatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        class_date_ceil = (year + "-" + ((monthOfYear + 1) >= 10 ? String.valueOf((monthOfYear + 1)) : ("0" + (monthOfYear + 1))) + "-" + ((dayOfMonth) >= 10 ? String.valueOf((dayOfMonth)) : ("0" + (dayOfMonth))));
                        endcLassYear.setText(String.valueOf(year));
                        endcLassMonth.setText((monthOfYear + 1) >= 10 ? String.valueOf((monthOfYear + 1)) : ("0" + (monthOfYear + 1)));
                        endClassDay.setText((dayOfMonth) >= 10 ? String.valueOf((dayOfMonth)) : ("0" + (dayOfMonth)));
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                dataDialog.show();
                break;
            case R.id.screen_layout://筛选狂
            case R.id.cancel://取消按钮
                screenLayout.setVisibility(View.GONE);
                break;
            case R.id.submit://提交
                initData(1);
                screenLayout.setVisibility(View.GONE);
                KeyBoardUtils.closeKeybord(getActivity());
                break;
//            case R.id.class_text:
//                break;
        }
    }
}
