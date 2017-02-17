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
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.GradeBean;
import libraryextra.bean.RemedialClassBean;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.KeyBoardUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.MDatePickerDialog;

public class FragmentRemedialClassAll extends BaseFragment implements View.OnClickListener {

    LinearLayout timesort;
    TextView timetext;
    LinearLayout subjectsort;
    TextView subjecttext;
    LinearLayout classsort;
    TextView classtext;
    View screen;
    PullToRefreshGridView grid;
    private CommonAdapter<RemedialClassBean.Data> adapter;
    private List<RemedialClassBean.Data> list = new ArrayList<>();
    private PopupWindow pop;
    //按时间排列方式
    private String timesorttype = "";
    private EditText priceLow;
    private EditText priceHigh;
    private TextView beginClassTime;
    private TextView endcLassTime;


    private String class_date_floor = "";
    //开课日期结束区间
    private String class_date_ceil = "";
    //辅导班状态
    private String status = "";
    private int page = 1;
    private SimpleDateFormat parseDate = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat df = new DecimalFormat("#.00");
    private GradeBean gradeBean;
    private int timesortposition;
    private View started;
    private View recruiting;
    private CheckedTextView startedText;
    private View startedSelected;
    private CheckedTextView recruitingText;
    private View recruitingSelected;
    private View screenPopView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remedial_class_all, container, false);
        initView(view);
        initData(1);
        String gradeString = FileUtil.readFile(getActivity().getFilesDir() + "/grade.txt");
//        LogUtils.e("班级基础信息" + gradeString);
        if (!StringUtils.isNullOrBlanK(gradeString)) {
            gradeBean = JsonUtils.objectFromJson(gradeString, GradeBean.class);
        }
        return view;
    }

    private void initView(View view) {
        timesort = (LinearLayout) view.findViewById(R.id.time_sort);
        timetext = (TextView) view.findViewById(R.id.time_text);
        subjectsort = (LinearLayout) view.findViewById(R.id.subject_sort);
        subjecttext = (TextView) view.findViewById(R.id.subject_text);
        classsort = (LinearLayout) view.findViewById(R.id.class_sort);
        classtext = (TextView) view.findViewById(R.id.class_text);
        screen = view.findViewById(R.id.screen);

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
        adapter = new CommonAdapter<RemedialClassBean.Data>(getActivity(), list, R.layout.item_fragment_remedial_class_all) {
            @Override
            public void convert(ViewHolder helper, RemedialClassBean.Data item, int position) {
                if (item == null) {
                    Logger.e("item數據空");
                    return;
                }
                Glide.with(getActivity()).load(item.getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().dontAnimate().into(((ImageView) helper.getView(R.id.image)));
                String price = df.format(item.getCurrent_price());
                if (price.startsWith(".")) {
                    price = "0" + price;
                }
                helper.setText(R.id.name, item.getName()).setText(R.id.price, price).setText(R.id.teacher, item.getTeacher_name());
            }
        };
        grid.setEmptyView(View.inflate(getActivity(), R.layout.empty_view, null));
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
                startActivityForResult(intent, Constant.REQUEST);
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

        if (!subjecttext.getText().equals(getResources().getString(R.string.by_subject)) && !subjecttext.getText().equals("全部")) {
            try {
                map.put("subject", URLEncoder.encode(subjecttext.getText().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }


        if (!classtext.getText().equals(getResources().getString(R.string.by_grade)) && !classtext.getText().equals("全部")) {
            try {
                map.put("grade", URLEncoder.encode(classtext.getText().toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String lowPrice = priceLow == null ? "" : priceLow.getText().toString();
        String highPrice = priceHigh == null ? "" : priceHigh.getText().toString();
        if (!StringUtils.isNullOrBlanK(lowPrice) && !StringUtils.isNullOrBlanK(highPrice)) {//两个都不为空再比较
            if (Integer.valueOf(lowPrice) > Integer.valueOf(highPrice)) {
                String temp = lowPrice;
                lowPrice = highPrice;
                highPrice = temp;
                priceHigh.setText(highPrice);
                priceLow.setText(lowPrice);
            }
        }
        map.put("price_floor", lowPrice);
        map.put("price_ceil", highPrice);
        if (!StringUtils.isNullOrBlanK(class_date_floor) && !StringUtils.isNullOrBlanK(class_date_ceil)) {
            try {
                if (parseDate.parse(class_date_ceil).before(parseDate.parse(class_date_floor))) {
                    String temp = class_date_floor;
                    class_date_floor = class_date_ceil;
                    class_date_ceil = temp;
                    endcLassTime.setText(class_date_ceil);
                    beginClassTime.setText(class_date_floor);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("class_date_floor", class_date_floor);
        map.put("class_date_ceil", class_date_ceil);
        map.put("status", status);
        try {
            map.put("city_name",URLEncoder.encode(BaseApplication.getCurrentCity().getName(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass, map), null,
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
                    }

                    @Override
                    protected void onError(JSONObject response) {
                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        grid.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                        grid.onRefreshComplete();
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
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
//        timesorttype = "";
        priceLow.setText("");
        priceHigh.setText("");
//        subjectLow.setText("");
//        subjectHigh.setText("");
        status = "";
        class_date_floor = "";
        class_date_ceil = "";
        startedText.setChecked(true);
        recruitingText.setChecked(true);
        beginClassTime.setText("");
        endcLassTime.setText("");
        beginClassTime.setHint(R.string.start_time);
        endcLassTime.setHint(R.string.end_time);
        startedSelected.setVisibility(View.VISIBLE);
        recruitingSelected.setVisibility(View.VISIBLE);

    }

    public void showPop(View popView) {
        pop = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pop.setBackgroundDrawable(new ColorDrawable());
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.downDialogstyle);
        pop.showAtLocation(getActivity().findViewById(R.id.fragmentlayout), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
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
                timeList.add(getResourceString(R.string.by_time));
                timeList.add(getResourceString(R.string.in_price_low_to_high));
                timeList.add(getResourceString(R.string.in_price_high_to_low));
                timeList.add(getResourceString(R.string.in_number));
                listView.setAdapter(new CommonAdapter<String>(getActivity(), timeList, R.layout.item_pop_fragment12) {
                    @Override
                    public void convert(ViewHolder holder, String item, int position) {
                        TextView view = holder.getView(R.id.text);
                        TextView select = holder.getView(R.id.select);
                        view.setText(item);
                        if (position == timesortposition) {
                            view.setTextColor(0xffbe0b0b);
                            select.setTextColor(0xffbe0b0b);
                            select.setVisibility(View.VISIBLE);
                        } else {
                            view.setTextColor(0xff999999);
                            select.setTextColor(0xff999999);
                            select.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (timeList.get(position).equals(getResourceString(R.string.in_price_low_to_high))) {
                            timetext.setText(getResources().getString(R.string.by_price_up));
                        } else if (timeList.get(position).equals(getResourceString(R.string.in_price_high_to_low))) {
                            timetext.setText(getResources().getString(R.string.by_price_down));
                        } else {
                            timetext.setText(timeList.get(position));
                        }
                        timesortposition = position;
                        if (position == 0) {
                            timesorttype = "";
                        } else if (position == 1) {
                            timesorttype = "price.asc";
                        } else if (position == 2) {
                            timesorttype = "price.desc";
                        } else {
                            timesorttype = "buy_tickets_count.asc";
                        }
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
                subjectList.add(getResourceString(R.string.whole));
                subjectList.add(getResourceString(R.string.chinese));
                subjectList.add(getResourceString(R.string.math));
                subjectList.add(getResourceString(R.string.english));
                subjectList.add(getResourceString(R.string.physics));
                subjectList.add(getResourceString(R.string.chemistry));
                subjectList.add(getResourceString(R.string.geography));
                subjectList.add(getResourceString(R.string.politics));
                subjectList.add(getResourceString(R.string.history));
                subjectList.add(getResourceString(R.string.science));
                subjectList.add(getResourceString(R.string.biology));
                listView.setAdapter(new CommonAdapter<String>(getActivity(), subjectList, R.layout.item_pop_fragment12) {
                    @Override
                    public void convert(ViewHolder holder, String item, int position) {
                        TextView view = holder.getView(R.id.text);
                        TextView select = holder.getView(R.id.select);
                        view.setText(item);
                        if (subjecttext.getText().toString().equals(item) || (subjecttext.getText().toString().equals(getResourceString(R.string.by_subject)) && position == 0)) {
                            view.setTextColor(0xffbe0b0b);
                            select.setTextColor(0xffbe0b0b);
                            select.setVisibility(View.VISIBLE);
                        } else {
                            view.setTextColor(0xff999999);
                            select.setTextColor(0xff999999);
                            select.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (getResourceString(R.string.whole).equals(subjectList.get(position))) {
                            subjecttext.setText(getResourceString(R.string.by_subject));
                        } else {
                            subjecttext.setText(subjectList.get(position));
                        }
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
                classList.add(getResourceString(R.string.whole));
                if (gradeBean != null && gradeBean.getData() != null && gradeBean.getData().getGrades() != null && gradeBean.getData().getGrades().size() > 0) {
                    classList.addAll(gradeBean.getData().getGrades());
                } else {
                    classList.add(getResourceString(R.string.high3));
                    classList.add(getResourceString(R.string.high2));
                    classList.add(getResourceString(R.string.high1));
                    classList.add(getResourceString(R.string.middle3));
                    classList.add(getResourceString(R.string.middle2));
                    classList.add(getResourceString(R.string.middle1));
                    classList.add(getResourceString(R.string.primary6));
                    classList.add(getResourceString(R.string.primary5));
                    classList.add(getResourceString(R.string.primary4));
                    classList.add(getResourceString(R.string.primary3));
                    classList.add(getResourceString(R.string.primary2));
                    classList.add(getResourceString(R.string.primary1));
                }

                listView.setAdapter(new CommonAdapter<String>(getActivity(), classList, R.layout.item_pop_fragment12) {
                    @Override
                    public void convert(ViewHolder holder, String item, int position) {
                        TextView view = holder.getView(R.id.text);
                        TextView select = holder.getView(R.id.select);
                        view.setText(item);
                        if (classtext.getText().toString().equals(item) || (classtext.getText().toString().equals(getResourceString(R.string.by_grade)) && position == 0)) {
                            view.setTextColor(0xffbe0b0b);
                            select.setTextColor(0xffbe0b0b);
                            select.setVisibility(View.VISIBLE);
                        } else {
                            view.setTextColor(0xff999999);
                            select.setTextColor(0xff999999);
                            select.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (getResourceString(R.string.whole).equals(classList.get(position))) {
                            classtext.setText(getResourceString(R.string.by_grade));
                        } else {
                            classtext.setText(classList.get(position));
                        }
                        initData(1);
                        pop.dismiss();
                    }
                });
                showPop(popView);
                break;

            case R.id.screen://筛选
                if (screenPopView == null) {
                    screenPopView = View.inflate(getActivity(), R.layout.pop_fragment12_screen, null);
                    //筛选框
                    priceLow = (EditText) screenPopView.findViewById(R.id.price_low);
                    priceHigh = (EditText) screenPopView.findViewById(R.id.price_high);

//                    subjectLow = (EditText) screenPopView.findViewById(R.id.subject_low);
//                    subjectHigh = (EditText) screenPopView.findViewById(R.id.subject_high);

                    beginClassTime = (TextView) screenPopView.findViewById(R.id.begin_class_time);
                    endcLassTime = (TextView) screenPopView.findViewById(R.id.end_class_time);
                    started = screenPopView.findViewById(R.id.started);
                    recruiting = screenPopView.findViewById(R.id.recruiting);
                    startedText = (CheckedTextView) screenPopView.findViewById(R.id.started_text);
                    startedSelected = screenPopView.findViewById(R.id.started_selected);
                    recruitingText = (CheckedTextView) screenPopView.findViewById(R.id.recruiting_text);
                    recruitingSelected = screenPopView.findViewById(R.id.recruiting_selected);
                    clearScreenData();
                    Button reset = (Button) screenPopView.findViewById(R.id.reset);
                    Button submit = (Button) screenPopView.findViewById(R.id.submit);
                    started.setOnClickListener(this);
                    recruiting.setOnClickListener(this);
                    beginClassTime.setOnClickListener(this);
                    endcLassTime.setOnClickListener(this);
                    submit.setOnClickListener(this);
                    reset.setOnClickListener(this);
                }
                showPop(screenPopView);
                break;
            case R.id.begin_class_time://开课时间
                MDatePickerDialog beginPickerDialog = new MDatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        class_date_floor = (year + "-" + ((monthOfYear + 1) >= 10 ? String.valueOf((monthOfYear + 1)) : ("0" + (monthOfYear + 1))) + "-" + ((dayOfMonth) >= 10 ? String.valueOf((dayOfMonth)) : ("0" + (dayOfMonth))));
                        beginClassTime.setText(class_date_floor);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                beginPickerDialog.show();
                break;
            case R.id.end_class_time://开课时间end
                MDatePickerDialog endPickerDialog = new MDatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        class_date_ceil = (year + "-" + ((monthOfYear + 1) >= 10 ? String.valueOf((monthOfYear + 1)) : ("0" + (monthOfYear + 1))) + "-" + ((dayOfMonth) >= 10 ? String.valueOf((dayOfMonth)) : ("0" + (dayOfMonth))));
                        endcLassTime.setText(class_date_ceil);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                endPickerDialog.show();
                break;
            case R.id.reset://取消按钮
//                pop.dismiss();
                Logger.e("reset *************** screen");
                clearScreenData();
                break;
            case R.id.submit://提交
                setStatus();
                initData(1);
                pop.dismiss();
                KeyBoardUtils.closeKeybord(getActivity());
                break;

            case R.id.started:
                startedText.setChecked(!startedText.isChecked());
                started.setBackgroundResource(startedText.isChecked() ? R.drawable.text_background_select : R.drawable.text_background_normal);
                startedSelected.setVisibility(startedText.isChecked() ? View.VISIBLE : View.INVISIBLE);
                break;
            case R.id.   recruiting:
                recruitingText.setChecked(!recruitingText.isChecked());
                recruiting.setBackgroundResource(recruitingText.isChecked() ? R.drawable.text_background_select : R.drawable.text_background_normal);
                recruitingSelected.setVisibility(recruitingText.isChecked() ? View.VISIBLE : View.INVISIBLE);
                break;

        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    private void setStatus() {
        if (startedText.isChecked() && recruitingText.isChecked()) {
            status = "all";
        } else if (!startedText.isChecked() && recruitingText.isChecked()) {
            status = "published";
        } else if (startedText.isChecked() && !recruitingText.isChecked()) {
            status = "teaching";
        } else {
            status = "all";
        }
    }

    public void initDataAsSubject(String s) {
        //全部设置字体为科目
        if (s == null || getResourceString(R.string.whole).equals(s)) {
            s = getResourceString(R.string.subject);
        }
        subjecttext.setText(s);
        initData(1);
    }
}
