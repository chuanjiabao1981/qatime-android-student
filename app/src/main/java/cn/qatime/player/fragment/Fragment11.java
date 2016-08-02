package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.adapter.CommonAdapter;
import cn.qatime.player.adapter.ViewHolder;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.RemedialClassBean;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.ScreenUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VolleyErrorListener;

public class Fragment11 extends BaseFragment {
    private GridView grid;
    private List<RemedialClassBean.Data> list = new ArrayList<>();
    private CommonAdapter<RemedialClassBean.Data> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment11, container, false);
        initview(view);
        initData();
        return view;
    }

    private void initview(View view) {
        grid = (GridView) view.findViewById(R.id.grid);
        adapter = new CommonAdapter<RemedialClassBean.Data>(getActivity(), list, R.layout.item_fragment11) {
            @Override
            public void convert(ViewHolder helper, RemedialClassBean.Data item, int position) {
                ((ImageView) helper.getView(R.id.image)).setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(getActivity())/2,ScreenUtils.getScreenWidth(getActivity())/2));
//                Glide.with(getActivity()).load(item.getPush_address()).into(((ImageView) helper.getView(R.id.image)));
                helper.setText(R.id.name,item.getName());
                helper.setText(R.id.subject,item.getSubject());
                helper.setText(R.id.grade,item.getGrade());

            }
        };
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),RemedialClassDetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("Remember-Token",BaseApplication.getProfile().getToken());
//        map.put("password", password.getText().toString());
//        map.put("client_type", "app");
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass, map), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        LogUtils.e(jsonObject.toString());
                        try {
                            Gson gson = new Gson();
                            RemedialClassBean data = gson.fromJson(jsonObject.toString(), RemedialClassBean.class);
                            list.addAll(data.getData());
                            adapter.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
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
