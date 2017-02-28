package cn.qatime.player.activity;

import android.os.Bundle;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.bean.RegionBean;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.utils.JsonUtils;

/**
 * @author Tianhaoranly
 * @date 2017/2/28 19:25
 * @Description:
 */
public class RegionSelectActivity1 extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_select1);
        List<RegionBean> arrayList = new ArrayList<>();
        try {
            arrayList = JsonUtils.listFromJson(getAssets().open("city.json"), RegionBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(new CommonAdapter<RegionBean>(RegionSelectActivity1.this, arrayList, R.layout.item_region) {

            @Override
            public void convert(ViewHolder holder, RegionBean item, int position) {
                holder.setText(R.id.region_text, item.getAreaName());
            }
        });

    }
}
