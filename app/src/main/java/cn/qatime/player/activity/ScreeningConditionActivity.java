package cn.qatime.player.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.StringUtils;
import libraryextra.view.MDatePickerDialog;

/**
 * @author lungtify
 * @Time 2017/3/15 14:46
 * @Describe 筛选条件页面
 */

public class ScreeningConditionActivity extends BaseActivity {
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

    private TextView start;
    private TextView end;
    private Button next;
    private int timeChecked = 0;
    private int courseChecked = 0;
    //    private int tasteChecked = 0;
//    private CommonAdapter<String> tasteAdapter;
    private CommonAdapter<String> timeAdapter;
    private CommonAdapter<String> courseAdapter;
    private String startSelect = null;//选择开始时间
    private String endSelect = null;//选择结束时间
    private List<String> timeData;
    private List<String> courseData;

    private void assignViews() {
        GridView timeGrid = (GridView) findViewById(R.id.time_grid);
        GridView courseGrid = (GridView) findViewById(R.id.course_grid);
//        GridView tasteGrid = (GridView) findViewById(R.id.taste_grid);
        start = (TextView) findViewById(R.id.start);
        end = (TextView) findViewById(R.id.end);
        next = (Button) findViewById(R.id.next);
        timeData = new ArrayList<>();
        timeData.add("所有");
        timeData.add("近一个月");
        timeData.add("近两个月");
        timeData.add("近三个月");
        timeData.add("近半年");
        timeData.add("近一年");
        timeAdapter = new CommonAdapter<String>(this, timeData, R.layout.item_screening_condition) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.text, item);
                if (timeChecked == position) {
                    ((TextView) holder.getView(R.id.text)).setBackgroundResource(R.drawable.text_background_red);
                    ((TextView) holder.getView(R.id.text)).setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    ((TextView) holder.getView(R.id.text)).setBackgroundResource(R.drawable.text_background);
                    ((TextView) holder.getView(R.id.text)).setTextColor(0xff666666);
                }
            }
        };
        timeGrid.setAdapter(timeAdapter);
        timeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                timeChecked = position;
                timeAdapter.notifyDataSetChanged();
            }
        });
        courseData = new ArrayList<>();
        courseData.add("不限");
        courseData.add("招生中");
        courseData.add("开课中");
        courseAdapter = new CommonAdapter<String>(this, courseData, R.layout.item_screening_condition) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.text, item);
                if (courseChecked == position) {
                    ((TextView) holder.getView(R.id.text)).setBackgroundResource(R.drawable.text_background_red);
                    ((TextView) holder.getView(R.id.text)).setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    ((TextView) holder.getView(R.id.text)).setBackgroundResource(R.drawable.text_background);
                    ((TextView) holder.getView(R.id.text)).setTextColor(0xff666666);
                }
            }
        };
        courseGrid.setAdapter(courseAdapter);
        courseGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                courseChecked = position;
                courseAdapter.notifyDataSetChanged();
            }
        });
//        List<String> tasteData = new ArrayList<>();
//        tasteData.add("不限");
//        tasteData.add("免费试听");
//        tasteData.add("无试听");
//        tasteAdapter = new CommonAdapter<String>(this, tasteData, R.layout.item_screening_condition) {
//            @Override
//            public void convert(ViewHolder holder, String item, int position) {
//                holder.setText(R.id.text, item);
//                if (tasteChecked == position) {
//                    ((TextView) holder.getView(R.id.text)).setBackgroundColor(0xffcccccc);
//                } else {
//                    ((TextView) holder.getView(R.id.text)).setBackgroundResource(R.drawable.text_background_red);
//                }
//            }
//        };
//        tasteGrid.setAdapter(tasteAdapter);
//        tasteGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                tasteChecked = position;
//                tasteAdapter.notifyDataSetChanged();
//            }
//        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startSelect == null) {
                    startSelect = parse.format(new Date());
                }
                try {
                    MDatePickerDialog dataDialog = new MDatePickerDialog(ScreeningConditionActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            startSelect = (year + "-" + ((monthOfYear + 1) >= 10 ? String.valueOf((monthOfYear + 1)) : ("0" + (monthOfYear + 1))) + "-" + ((dayOfMonth) >= 10 ? String.valueOf((dayOfMonth)) : ("0" + (dayOfMonth))));
                            try {
                                start.setText(format.format(parse.parse(startSelect)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, parse.parse(startSelect).getYear() + 1900, parse.parse(startSelect).getMonth(), parse.parse(startSelect).getDate());
                    if (StringUtils.isNullOrBlanK(endSelect)) {
                        dataDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    } else {
                        dataDialog.getDatePicker().setMaxDate(parse.parse(endSelect).getTime());
                    }
                    dataDialog.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endSelect == null) {
                    endSelect = parse.format(new Date());
                }
                try {
                    MDatePickerDialog dataDialog = new MDatePickerDialog(ScreeningConditionActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            endSelect = (year + "-" + ((monthOfYear + 1) >= 10 ? String.valueOf((monthOfYear + 1)) : ("0" + (monthOfYear + 1))) + "-" + ((dayOfMonth) >= 10 ? String.valueOf((dayOfMonth)) : ("0" + (dayOfMonth))));
                            try {
                                end.setText(format.format(parse.parse(endSelect)));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, parse.parse(endSelect).getYear() + 1900, parse.parse(endSelect).getMonth(), parse.parse(endSelect).getDate());
                    dataDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    if (!StringUtils.isNullOrBlanK(startSelect)) {
                        dataDialog.getDatePicker().setMinDate(parse.parse(startSelect).getTime());
                    }
                    dataDialog.show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        initData();
    }

    private void initData() {
        timeChecked = getRangeValue(getIntent().getStringExtra("range"));
        if (timeChecked != 0) {
            timeAdapter.notifyDataSetChanged();
        }
        courseChecked = getStatusValue(getIntent().getStringExtra("courseStatus"));
        if (courseChecked != 0) {
            courseAdapter.notifyDataSetChanged();
        }
        if (!StringUtils.isNullOrBlanK(getIntent().getStringExtra("startTime"))) {
            startSelect = getIntent().getStringExtra("startTime");
            try {
                start.setText(format.format(parse.parse(startSelect)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (!StringUtils.isNullOrBlanK(getIntent().getStringExtra("endTime"))) {
            endSelect = getIntent().getStringExtra("endTime");
            try {
                end.setText(format.format(parse.parse(endSelect)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private int getRangeValue(String range) {
        if (StringUtils.isNullOrBlanK(range)) return 0;
        int result = 0;
        switch (range) {
            case "1_months":
                result = 1;
                break;
            case "2_months":
                result = 2;
                break;
            case "3_months":
                result = 3;
                break;
            case "6_months":
                result = 4;
                break;
            case "1_year":
                result = 5;
                break;

        }
        return result;
    }

    private int getStatusValue(String courseStatus) {
        if (StringUtils.isNullOrBlanK(courseStatus)) return 0;
        int result = 0;
        switch (courseStatus) {
            case "all":
                result = 0;
                break;
            case "published":
                result = 1;
                break;
            case "teaching":
                result = 2;
                break;
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening_condition);

        assignViews();
        setTitles("筛选");
        setRightText("重置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeChecked = 0;
                timeAdapter.notifyDataSetChanged();
                courseChecked = 0;
                courseAdapter.notifyDataSetChanged();
//                tasteChecked = 0;
//                tasteAdapter.notifyDataSetChanged();
                start.setText("选择开始时间");
                end.setText("选择结束时间");
                startSelect = null;
                endSelect = null;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("range", getRange());
                intent.putExtra("courseStatus", getStatus());
                intent.putExtra("startTime", startSelect);
                intent.putExtra("endTime", endSelect);
                setResult(Constant.RESPONSE, intent);
                finish();
            }
        });
    }

    private String getRange() {
        String result = null;
        switch (timeData.get(timeChecked)) {
            case "所有":
                result = null;
                break;
            case "近一个月":
                result = "1_months";
                break;
            case "近两个月":
                result = "2_months";
                break;
            case "近三个月":
                result = "3_months";
                break;
            case "近半年":
                result = "6_months";
                break;
            case "近一年":
                result = "1_year";
                break;
        }
        return result;
    }

    private String getStatus() {
        String result = null;
        switch (courseData.get(courseChecked)) {
            case "不限":
                result = "all";
                break;
            case "招生中":
                result = "published";
                break;
            case "开课中":
                result = "teaching";
                break;
        }
        return result;
    }

}
