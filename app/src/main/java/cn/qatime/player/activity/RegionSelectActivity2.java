package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.CityBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.PinyinUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2017/3/1 10:38
 * @Description:
 */
public class RegionSelectActivity2 extends BaseActivity {
    private List<CityBean.Data> citiesList;
    private ListView list;
    private CommonAdapter<CityBean.Data> adapter;
    private String provincesId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_select1);
        setTitles("选择地区");
        provincesId = getIntent().getStringExtra("provinces_id");
        list = (ListView) findViewById(R.id.list);
        citiesList = new ArrayList<>();
        adapter = new CommonAdapter<CityBean.Data>(RegionSelectActivity2.this, citiesList, R.layout.item_region) {

            @Override
            public void convert(ViewHolder holder, CityBean.Data item, int position) {
                holder.setText(R.id.region_text, item.getName());
            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent data = new Intent();
                data.putExtra("region", citiesList.get(position).getName());
                setResult(Constant.RESPONSE_REGION_SELECT, data);
                finish();
            }
        });

        initData();
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlAppconstantInformation + "/cities", null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        CityBean cityBean = JsonUtils.objectFromJson(response.toString(), CityBean.class);
                        if (cityBean != null && cityBean.getData() != null) {
                            citiesList.clear();
                            for (CityBean.Data data : cityBean.getData()) {
                                if (data.getProvince_id().equals(provincesId)) {
                                    citiesList.add(data);
                                }
                            }

                            for (CityBean.Data item : citiesList) {
                                if (StringUtils.isNullOrBlanK(item.getName())) {
                                    item.setFirstLetter("");
                                    item.setFirstLetters("");
                                } else {
                                    item.setFirstLetter(PinyinUtils.getPinyinFirstLetter(item.getName()).toUpperCase());
                                    item.setFirstLetters(PinyinUtils.getPinyinFirstLetters(item.getName()));
                                }
                            }
                            Collections.sort(citiesList, new Comparator<CityBean.Data>() {
                                @Override
                                public int compare(CityBean.Data lhs, CityBean.Data rhs) {
                                    return lhs.getFirstLetters().compareTo(rhs.getFirstLetters());
                                }
                            });
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }
}
