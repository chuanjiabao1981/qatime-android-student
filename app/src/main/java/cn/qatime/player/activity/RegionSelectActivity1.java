package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.AMapLocationUtils;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.CityBean;
import libraryextra.bean.ProvincesBean;
import libraryextra.utils.FileUtil;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.PinyinUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2017/2/28 19:25
 * @Description:
 */
public class RegionSelectActivity1 extends BaseActivity {


    private List<ProvincesBean.DataBean> regionList;
    private ListView list;
    private CommonAdapter<ProvincesBean.DataBean> adapter;
    private ProvincesBean.DataBean selectProvince;
    private TextView location;
//    private ProvincesBean.DataBean currentProvince;
//    private CityBean.Data currentCity;
    private List<CityBean.Data> cityList;
    private CityBean.Data locationCity;
    private ProvincesBean.DataBean locationProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_select);
        setTitles("选择地区");
//        currentProvince = (ProvincesBean.DataBean) getIntent().getSerializableExtra("region_province");
//        currentCity = (CityBean.Data) getIntent().getSerializableExtra("region_city");
        CityBean cityBean = JsonUtils.objectFromJson(FileUtil.readFile(getFilesDir() + "/cities.txt").toString(), CityBean.class);
        if (cityBean != null) {
            cityList = cityBean.getData();
        }
        initView();
        initData();
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlAppconstantInformation + "/provinces", null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        ProvincesBean provincesBean = JsonUtils.objectFromJson(response.toString(), ProvincesBean.class);
                        if (provincesBean != null && provincesBean.getData() != null) {
                            regionList.addAll(provincesBean.getData());
                            for (ProvincesBean.DataBean item : regionList) {
                                if (StringUtils.isNullOrBlanK(item.getName())) {
                                    item.setFirstLetter("");
                                    item.setFirstLetters("");
                                } else {
                                    item.setFirstLetter(PinyinUtils.getPinyinFirstLetter(item.getName()).toUpperCase());
                                    item.setFirstLetters(PinyinUtils.getPinyinFirstLetters(item.getName()));
                                }
                            }
                            Collections.sort(regionList, new Comparator<ProvincesBean.DataBean>() {
                                @Override
                                public int compare(ProvincesBean.DataBean lhs, ProvincesBean.DataBean rhs) {
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


        AMapLocationUtils utils = new AMapLocationUtils(getApplicationContext(), new AMapLocationUtils.LocationListener() {
            @Override
            public void onLocationBack(String[] result) {
                for (CityBean.Data item : cityList) {
                    if (result[2].equals(item.getName()) || result[1].equals(item.getName())) {//需先对比区,区不对应往上对比市,不可颠倒
                        locationCity = item;
                    }
                }
                for (ProvincesBean.DataBean province : regionList) {
                    if (province.getId().equals(locationCity.getProvince_id())) {
                        locationProvince = province;
                    }
                }
                location.setText(locationProvince.getName() + locationCity.getName());
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent data = new Intent();
                        data.putExtra("region_city", locationCity);
                        data.putExtra("region_province", locationProvince);
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
        location = (TextView) findViewById(R.id.location);
//        TextView currentRegion = (TextView) findViewById(R.id.current_region);
//        if (currentCity != null && currentProvince != null) {
//            currentRegion.setText(currentProvince.getName() + currentCity.getName());
//        }
        regionList = new ArrayList<>();
        adapter = new CommonAdapter<ProvincesBean.DataBean>(RegionSelectActivity1.this, regionList, R.layout.item_region) {

            @Override
            public void convert(ViewHolder holder, ProvincesBean.DataBean item, int position) {
                holder.setText(R.id.region_text, item.getName());
            }
        };
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectProvince = regionList.get(position);
                Intent intent = new Intent(RegionSelectActivity1.this, RegionSelectActivity2.class);
                intent.putExtra("provinces_id", regionList.get(position).getId());
                startActivityForResult(intent, Constant.REQUEST_REGION_SELECT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_REGION_SELECT && resultCode == Constant.RESPONSE_REGION_SELECT) {
            data.putExtra("region_city", (CityBean.Data) data.getSerializableExtra("region_city"));
            data.putExtra("region_province", selectProvince);
            setResult(Constant.RESPONSE_REGION_SELECT, data);
            finish();
        }
    }
}
