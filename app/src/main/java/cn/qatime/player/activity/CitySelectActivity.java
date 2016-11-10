package cn.qatime.player.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.qatime.player.R;
import cn.qatime.player.adapter.CitySelectAdapter;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.CityBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.SideBar;

/**
 * @author Tianhaoranly
 * @date 2016/11/9 19:54
 * @Description:
 */
public class CitySelectActivity extends BaseActivity {
    private TextView currentCity;
    private TextView textDialog;
    private ListView listView;
    private SideBar sidebar;
    private ArrayList<CityBean.Data> list;
    private CitySelectAdapter adapter;

    private void assignViews() {
        currentCity = (TextView) findViewById(R.id.current_city);
        textDialog = (TextView) findViewById(R.id.text_dialog);
        listView = (ListView) findViewById(R.id.listView);
        sidebar = (SideBar) findViewById(R.id.sidebar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);
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
//                            list.clear();
                            list.add(new CityBean.Data("去"));
                            list.add(new CityBean.Data("我"));
                            list.add(new CityBean.Data("额"));
                            list.add(new CityBean.Data("人"));
                            list.add(new CityBean.Data("人"));
                            list.add(new CityBean.Data("天"));
                            list.add(new CityBean.Data("有"));
                            list.add(new CityBean.Data("u"));
                            list.add(new CityBean.Data("i"));
                            list.add(new CityBean.Data("哦"));
                            list.add(new CityBean.Data("哦"));
                            list.add(new CityBean.Data("哦"));
                            list.add(new CityBean.Data("票"));
                            list.add(new CityBean.Data("啊"));
                            list.add(new CityBean.Data("是"));
                            list.add(new CityBean.Data("的"));
                            list.add(new CityBean.Data("的"));
                            list.add(new CityBean.Data("的"));
                            list.add(new CityBean.Data("发"));
                            list.add(new CityBean.Data("给"));
                            list.add(new CityBean.Data("胡"));
                            list.add(new CityBean.Data("就"));
                            list.add(new CityBean.Data("卡"));
                            list.add(new CityBean.Data("了"));
                            list.add(new CityBean.Data("了"));
                            list.add(new CityBean.Data("中"));
                            list.add(new CityBean.Data("下"));
                            list.add(new CityBean.Data("才"));
                            list.add(new CityBean.Data("才"));
                            list.add(new CityBean.Data("v"));
                            list.add(new CityBean.Data("v"));
                            list.add(new CityBean.Data("不"));
                            list.add(new CityBean.Data("能"));
                            list.add(new CityBean.Data("能"));
                            list.add(new CityBean.Data("没"));
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

    private void initView() {
        list = new ArrayList<>();
        adapter = new CitySelectAdapter(this, list, R.layout.item_city_lately, R.layout.item_city_all, R.layout.item_city_list) {
            @Override
            public void setCityName(String s) {
                currentCity.setText(s);
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
                    }
                }
            }
        });
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                String s = adapter.getLetterByPosition(firstVisibleItem);
//                sidebar.setChooseText(s);
//            }
//        });
    }
}
