package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.RegionBean;
import cn.qatime.player.utils.Constant;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.PinyinUtils;
import libraryextra.utils.StringUtils;

/**
 * @author Tianhaoranly
 * @date 2017/3/1 10:38
 * @Description:
 */
public class RegionSelectActivity2 extends BaseActivity {
    private List<RegionBean.CitiesBean> citiesList;
    private ListView list;
    private CommonAdapter<RegionBean.CitiesBean> adapter;
    private String selectCountry = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_select1);
        setTitles("选择地区");
        citiesList = (List<RegionBean.CitiesBean>) getIntent().getSerializableExtra("cities");
        for (RegionBean.CitiesBean item : citiesList) {
            if (StringUtils.isNullOrBlanK(item.getAreaName())) {
                item.setFirstLetter("");
                item.setFirstLetters("");
            } else {
                item.setFirstLetter(PinyinUtils.getPinyinFirstLetter(item.getAreaName()).toUpperCase());
                item.setFirstLetters(PinyinUtils.getPinyinFirstLetters(item.getAreaName()));
            }
        }
        Collections.sort(citiesList, new Comparator<RegionBean.CitiesBean>() {
            @Override
            public int compare(RegionBean.CitiesBean lhs, RegionBean.CitiesBean rhs) {
                return lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
            }
        });
        list = (ListView) findViewById(R.id.list);
        adapter = new CommonAdapter<RegionBean.CitiesBean>(RegionSelectActivity2.this, citiesList, R.layout.item_region) {

            @Override
            public void convert(ViewHolder holder, RegionBean.CitiesBean item, int position) {
                holder.setText(R.id.region_text, item.getAreaName());
            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectCountry = citiesList.get(position).getAreaName();
                Intent intent = new Intent(RegionSelectActivity2.this, RegionSelectActivity3.class);
                intent.putExtra("counties", (Serializable) (citiesList.get(position).getCounties()));
                startActivityForResult(intent, Constant.REQUEST_REGION_SELECT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_REGION_SELECT && resultCode == Constant.RESPONSE_REGION_SELECT) {
            data.putExtra("region", selectCountry + data.getStringExtra("region"));
            setResult(Constant.RESPONSE_REGION_SELECT, data);
            finish();
        }
    }
}
