package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
public class RegionSelectActivity3 extends BaseActivity {
    private List<RegionBean.CitiesBean.CountiesBean> countiesList;
    private ListView list;
    private CommonAdapter<RegionBean.CitiesBean.CountiesBean> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_select1);
        setTitles("选择地区");
        countiesList = (List<RegionBean.CitiesBean.CountiesBean>) getIntent().getSerializableExtra("counties");
        for (RegionBean.CitiesBean.CountiesBean item : countiesList) {
            if (StringUtils.isNullOrBlanK(item.getAreaName())) {
                item.setFirstLetter("");
                item.setFirstLetters("");
            } else {
                item.setFirstLetter(PinyinUtils.getPinyinFirstLetter(item.getAreaName()).toUpperCase());
                item.setFirstLetters(PinyinUtils.getPinyinFirstLetters(item.getAreaName()));
            }
        }
        Collections.sort(countiesList, new Comparator<RegionBean.CitiesBean.CountiesBean>() {
            @Override
            public int compare(RegionBean.CitiesBean.CountiesBean lhs, RegionBean.CitiesBean.CountiesBean rhs) {
                return lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
            }
        });
        list = (ListView) findViewById(R.id.list);
        adapter = new CommonAdapter<RegionBean.CitiesBean.CountiesBean>(RegionSelectActivity3.this, countiesList, R.layout.item_region) {

            @Override
            public void convert(ViewHolder holder, RegionBean.CitiesBean.CountiesBean item, int position) {
                holder.setText(R.id.region_text, item.getAreaName());
            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra("region", countiesList.get(position).getAreaName());
                setResult(Constant.RESPONSE_REGION_SELECT, data);
                finish();
            }
        });

    }
}
