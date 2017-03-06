package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.RegionBean;
import cn.qatime.player.utils.AMapLocationUtils;
import cn.qatime.player.utils.Constant;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.PinyinUtils;
import libraryextra.utils.StringUtils;

/**
 * @author Tianhaoranly
 * @date 2017/2/28 19:25
 * @Description:
 */
public class RegionSelectActivity1 extends BaseActivity {


    private List<RegionBean> regionList;
    private ListView list;
    private CommonAdapter<RegionBean> adapter;
    private String selectCity = "";
    private AMapLocationUtils utils;
    private TextView currentRegion;
    private TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_select);
        setTitles("选择地区");
        initView();
        initData();
    }

    private void initData() {
        // TODO: 2017/3/6 从网络获取省份列表
        new Thread() {
            @Override
            public void run() {
                try {
                    regionList.addAll(JsonUtils.listFromJson(getAssets().open("city.json"), RegionBean.class));
                    for (RegionBean item : regionList) {
                        if (StringUtils.isNullOrBlanK(item.getAreaName())) {
                            item.setFirstLetter("");
                            item.setFirstLetters("");
                        } else {
                            item.setFirstLetter(PinyinUtils.getPinyinFirstLetter(item.getAreaName()).toUpperCase());
                            item.setFirstLetters(PinyinUtils.getPinyinFirstLetters(item.getAreaName()));
                        }
                    }
                    Collections.sort(regionList, new Comparator<RegionBean>() {
                        @Override
                        public int compare(RegionBean lhs, RegionBean rhs) {
                            return lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            adapter.notifyDataSetInvalidated();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        utils = new AMapLocationUtils(getApplicationContext(), new AMapLocationUtils.LocationListener() {
            @Override
            public void onLocationBack(String[] result) {
                location.setText(result[0] + "" + result[1] + "" + result[2]);
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent data = new Intent();
                        data.putExtra("region",location.getText().toString());
                        setResult(Constant.RESPONSE_REGION_SELECT, data);
                        finish();
                    }
                });
            }
        });
        utils.startLocation();
    }

    private void initView() {
        list = (ListView) findViewById(R.id.list);
        currentRegion = (TextView) findViewById(R.id.current_region);
        location = (TextView) findViewById(R.id.location);


        currentRegion.setText(getIntent().getStringExtra("region"));

        regionList = new ArrayList<>();
        adapter = new CommonAdapter<RegionBean>(RegionSelectActivity1.this, regionList, R.layout.item_region) {

            @Override
            public void convert(ViewHolder holder, RegionBean item, int position) {
                holder.setText(R.id.region_text, item.getAreaName());
            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectCity = regionList.get(position).getAreaName();
                Intent intent = new Intent(RegionSelectActivity1.this, RegionSelectActivity2.class);
                intent.putExtra("cities", (Serializable) (regionList.get(position).getCities()));
                startActivityForResult(intent, Constant.REQUEST_REGION_SELECT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_REGION_SELECT && resultCode == Constant.RESPONSE_REGION_SELECT) {
            data.putExtra("region", selectCity + data.getStringExtra("region"));
            setResult(Constant.RESPONSE_REGION_SELECT, data);
            finish();
        }
    }
}
