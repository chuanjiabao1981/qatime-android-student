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
import cn.qatime.player.bean.ProvincesBean;
import cn.qatime.player.utils.AMapLocationUtils;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
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


        utils = new AMapLocationUtils(getApplicationContext(), new AMapLocationUtils.LocationListener() {
            @Override
            public void onLocationBack(String[] result) {
                location.setText(result[0] + "" + result[1] + "" + result[2]);
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent data = new Intent();
                        data.putExtra("region", location.getText().toString());
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
                selectCity = regionList.get(position).getName();
                Intent intent = new Intent(RegionSelectActivity1.this, RegionSelectActivity2.class);
                intent.putExtra("provinces_id", regionList.get(position).getId());
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
