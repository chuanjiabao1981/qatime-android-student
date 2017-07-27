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

public class ExclusiveScreeningConditionActivity extends BaseActivity {
    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

    private TextView start;
    private TextView end;
    private Button next;
    private int courseChecked = 0;
    private int typeChecked=0;
    //    private int tasteChecked = 0;
//    private CommonAdapter<String> tasteAdapter;
    private CommonAdapter<String> courseAdapter;
    private String startSelect = null;//选择开始时间
    private String endSelect = null;//选择结束时间
    private List<String> courseData;
    private List<String> courseType;
    private CommonAdapter<String> typeAdapter;


    private void assignViews() {
        GridView courseGrid = (GridView) findViewById(R.id.course_grid);
        GridView courseTypeGrid = (GridView) findViewById(R.id.course_type);
        start = (TextView) findViewById(R.id.start);
        end = (TextView) findViewById(R.id.end);
        next = (Button) findViewById(R.id.next);
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
                    ((TextView) holder.getView(R.id.text)).setTextColor(0xff999999);
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
        courseType = new ArrayList<>();
        courseType.add("不限");
        courseType.add("收费课程");
        courseType.add("免费课程");
        typeAdapter = new CommonAdapter<String>(this, courseType, R.layout.item_screening_condition) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.text, item);
                if (typeChecked == position) {
                    ((TextView) holder.getView(R.id.text)).setBackgroundResource(R.drawable.text_background_red);
                    ((TextView) holder.getView(R.id.text)).setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    ((TextView) holder.getView(R.id.text)).setBackgroundResource(R.drawable.text_background);
                    ((TextView) holder.getView(R.id.text)).setTextColor(0xff999999);
                }
            }
        };
        courseTypeGrid.setAdapter(typeAdapter);
        courseTypeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeChecked = position;
                typeAdapter.notifyDataSetChanged();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startSelect == null) {
                    startSelect = parse.format(new Date());
                }
                try {
                    MDatePickerDialog dataDialog = new MDatePickerDialog(ExclusiveScreeningConditionActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    MDatePickerDialog dataDialog = new MDatePickerDialog(ExclusiveScreeningConditionActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        courseChecked = getStatusValue(getIntent().getStringExtra("courseStatus"));
        if (courseChecked != 0) {
            courseAdapter.notifyDataSetChanged();
        }
       typeChecked = getTypesValue(getIntent().getStringExtra("sellType"));
        if (typeChecked != 0) {
            typeAdapter.notifyDataSetChanged();
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


    private int getStatusValue(String courseStatus) {
        if (StringUtils.isNullOrBlanK(courseStatus)) return 0;
        int result = 0;
        switch (courseStatus) {
            case "":
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

    private int getTypesValue(String sellType) {
        if (StringUtils.isNullOrBlanK(sellType)) return 0;
        int result = 0;
        switch (sellType) {
            case "":
                result = 0;
                break;
            case "charge":
                result = 1;
                break;
            case "free":
                result = 2;
                break;
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exclusive_screening_condition);

        assignViews();
        setTitles("筛选");
        setRightText("重置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseChecked = 0;
                courseAdapter.notifyDataSetChanged();
                typeChecked = 0;
                typeAdapter.notifyDataSetChanged();
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
                intent.putExtra("courseStatus", getStatus());
                intent.putExtra("sellType", getTypeStatus());
                intent.putExtra("startTime", startSelect);
                intent.putExtra("endTime", endSelect);
                setResult(Constant.RESPONSE, intent);
                finish();
            }
        });
    }

    private String getStatus() {
        String result = null;
        switch (courseData.get(courseChecked)) {
            case "不限":
                result = "";
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

    private String getTypeStatus() {
        String result = null;
        switch (courseType.get(typeChecked)) {
            case "不限":
                result = "";
                break;
            case "免费课程":
                result = "free";
                break;
            case "收费课程":
                result = "charge";
                break;
        }
        return result;
    }

}
