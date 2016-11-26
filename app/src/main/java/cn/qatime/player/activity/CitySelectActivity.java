package cn.qatime.player.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import cn.qatime.player.R;
import cn.qatime.player.adapter.CitySelectAdapter;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.AMapLocationUtils;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.CityBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.SPUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.SideBar;

/**
 * @author Tianhaoranly
 * @date 2016/11/9 19:54
 * @Description:
 */
public class CitySelectActivity extends BaseActivity implements View.OnClickListener {
    private TextView currentCity;
    private TextView textDialog;
    private ListView listView;
    private SideBar sidebar;
    private ArrayList<CityBean.Data> list;
    private CitySelectAdapter adapter;
    private ArrayList<String> listLately;
    private CityBean.Data locationCity;
    private AMapLocationUtils utils;
    private View locationView;
    private HashMap<String,Integer> letterMap = new HashMap<>();

    private void assignViews() {
        currentCity = (TextView) findViewById(R.id.current_city);
        textDialog = (TextView) findViewById(R.id.text_dialog);
        listView = (ListView) findViewById(R.id.listView);
        sidebar = (SideBar) findViewById(R.id.sidebar);
        locationView = findViewById(R.id.location);

        locationView.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);
        setTitle("切换城市");
        assignViews();
        initView();
        initData();
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlAppconstantInformation + "/cities", null,
                new VolleyListener(this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        CityBean cityBean = JsonUtils.objectFromJson(response.toString(), CityBean.class);
                        if (cityBean != null && cityBean.getData() != null) {
                            list.clear();
                            list.addAll(cityBean.getData());
                            for (CityBean.Data item : list) {
                                if (StringUtils.isNullOrBlanK(item.getName())) {
                                    item.setFirstLetter("");
                                } else {
                                    item.setFirstLetter(StringUtils.getPYIndexStr(item.getName().substring(0, 1)));
                                }
                            }
                            Collections.sort(list, new Comparator<CityBean.Data>() {
                                @Override
                                public int compare(CityBean.Data lhs, CityBean.Data rhs) {
                                    return lhs.getFirstLetter().compareTo(rhs.getFirstLetter());
                                }
                            });
                            int position = 2;
                            for (CityBean.Data item : list) {
                                if (!letterMap.containsKey(item.getFirstLetter())) {
                                    letterMap.put(item.getFirstLetter(), position);
                                }
                                position++;
                            }
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

    private void initLocation() {
        locationView.setEnabled(false);
        Toast.makeText(CitySelectActivity.this, "正在获取您的位置...", Toast.LENGTH_SHORT).show();
        //如果没有被赋值，则默认全国
        utils = new AMapLocationUtils(getApplicationContext(), new AMapLocationUtils.LocationListener() {
            @Override
            public void onLocationBack(String result) {
                utils.stopLocation();
                if (result.length() > 0 && result.endsWith("市")) {
                    result = result.substring(0, result.length() - 1);
                }
                for (CityBean.Data item : list) {
                    if (result.equals(item.getName())) {
                        locationCity = item;
                    }
                }
                if (locationCity == null) {//如果没有被赋值，则默认全国
                    locationCity = new CityBean.Data("全国");
                    Toast.makeText(CitySelectActivity.this, "您所在的地区尚未加盟，\n已为您切换至全国", Toast.LENGTH_SHORT).show();
                }

                setCityAndHistory(locationCity);
                Logger.e("location", result);
                Logger.e("locationCity", locationCity.getName());
            }
        });
        utils.startLocation();
    }

    private void initView() {
        currentCity.setText(BaseApplication.getCurrentCity().getName());

        list = new ArrayList<>();
        ArrayList<String> lately = SPUtils.getObject(this, "listLately", ArrayList.class);
        if (lately == null || lately.size() == 0) {
            listLately = new ArrayList<>();
            listLately.add("全国");//默认选择
        } else {
            listLately = lately;
        }
        adapter = new CitySelectAdapter(this,letterMap, listLately, list, R.layout.item_city_lately, R.layout.item_city_all, R.layout.item_city_list) {
            @Override
            public void setCityName(CityBean.Data data) {
                setCityAndHistory(data);
            }
        };
        listView.setAdapter(adapter);
        sidebar.setTextView(textDialog);
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                textDialog.setText(s);
                if (s.equals("最近")) {
                    listView.setSelection(0);
                } else if (s.equals("全国")) {
                    listView.setSelection(1);
                } else {
                    int position = adapter.getPositionByLetter(s);
                    if (position >= 0) {
                        listView.setSelection(position);
                    } else {
                        listView.setSelection(listView.getFirstVisiblePosition());
                    }
                }
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                String s = adapter.getLetterByPosition(listView.getFirstVisiblePosition());
                sidebar.setChooseText(s);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                String s = adapter.getLetterByPosition(firstVisibleItem);
                sidebar.setChooseText(s);
            }
        });
    }

    private void setCityAndHistory(CityBean.Data data) {
        currentCity.setText(data.getName());
        if (listLately.contains(data.getName())) {
            listLately.remove(data.getName());
            listLately.add(0, data.getName());
        } else {
            listLately.add(0, data.getName());
            if (listLately.size() > 8) {
                listLately.remove(8);
            }
        }
//                adapter.notifyDataSetChanged();
        BaseApplication.setCurrentCity(data);
        Intent intent = new Intent();
        setResult(Constant.RESPONSE_CITY_SELECT, intent);
        finish();
    }

    @Override
    public void finish() {
        SPUtils.putObject(this, "listLately", listLately);
        super.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.location:
                initLocation();
                break;
        }
    }
}