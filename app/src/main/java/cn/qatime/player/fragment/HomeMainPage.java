package cn.qatime.player.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.MainActivity;
import cn.qatime.player.activity.MessageFragmentActivity;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.activity.TeacherDataActivity;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.RemedialClassBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.TagViewPager;

public class HomeMainPage extends BaseFragment implements View.OnClickListener {


    private TagViewPager tagViewpagerImg;
    private TagViewPager tagViewpagerSubject;
    private ImageView refreshTeacher;
    private GridView gridviewTeacher;
    private View allClass;
    private GridView gridviewClass;
    private List<GridView> gvSub;
    private int page = 1;
    private List<RemedialClassBean.Data> listNews = new ArrayList<>();
    private List<RemedialClassBean.Data> listHots = new ArrayList<>();
    private BaseAdapter adapter;
    private ImageView message;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_main_page, container, false);
        assignViews(view);
        return view;
    }

    private void assignViews(View view) {
        tagViewpagerImg = (TagViewPager) view.findViewById(R.id.tag_viewpager_img);
        tagViewpagerSubject = (TagViewPager) view.findViewById(R.id.tag_viewpager_subject);
        refreshTeacher = (ImageView) view.findViewById(R.id.refresh_teacher);
        gridviewTeacher = (GridView) view.findViewById(R.id.gridview_teacher);
        allClass = view.findViewById(R.id.all_class);
        gridviewClass = (GridView) view.findViewById(R.id.gridview_class);
        message = (ImageView) view.findViewById(R.id.message);

        initTagImg();
        initTagViewpagerSubject();
        initGridTeacher();
        initGridClass();
        refreshTeacher.setOnClickListener(this);
        allClass.setOnClickListener(this);
        message.setOnClickListener(this);

    }

    private void initTagImg() {
        final int imageIds[] = {R.mipmap.home_ad, R.mipmap.home_ad, R.mipmap.home_ad};
        tagViewpagerImg.init(R.drawable.shape_photo_tag_select, R.drawable.shape_photo_tag_nomal, 16, 8, 2, 40);
        tagViewpagerImg.setAutoNext(true, 4000);
//        viewPager.setId(1252);
        tagViewpagerImg.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, int position) {
                ImageView iv = new ImageView(getActivity());
                iv.setClickable(true);
                iv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                iv.setId(position);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setImageResource(imageIds[position]);
                container.addView(iv);
                return iv;
            }
        });
        tagViewpagerImg.setAdapter(imageIds.length, 0);
    }

    private void initTagViewpagerSubject() {
        final List<String> strings = Arrays.asList(getResources().getStringArray(R.array.subject));
        tagViewpagerSubject.init(0, 0, 16, 8, 2, 40);
        tagViewpagerSubject.setAutoNext(false, 0);
//        viewPager.setId(1252);
        tagViewpagerSubject.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, final int position) {
                GridView grid = new GridView(getContext());
                grid.setNumColumns(5);
                grid.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT));
                grid.setPadding(0, 20, 0, 0);
                grid.setBackgroundColor(Color.WHITE);
                grid.setGravity(Gravity.CENTER);
                grid.setAdapter(new CommonAdapter<String>(getContext(), strings, R.layout.item_subject) {
                    @Override
                    public int getCount() {
                        return (strings.size() - 10 * position) > 10 ? 10 : (strings.size() - 10 * position);
                    }

                    @Override
                    public void convert(ViewHolder holder, String item, int positionG) {
                        String s = strings.get(position * 10 + positionG);
                        holder.setText(R.id.subject_text, s);
                    }
                });
                grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int positionG, long id) {
                        String s = strings.get(position * 10 + positionG);
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.setCurrentPosition(1, s);
                    }
                });
                container.addView(grid);
                return grid;
            }
        });
        Logger.e("size" + (strings.size() - 1) / 10 + 1);
        tagViewpagerSubject.setAdapter((strings.size() - 1) / 10 + 1, 0);
    }

    private void initGridTeacher() {
        final List<String> strings = Arrays.asList(getResources().getStringArray(R.array.subject));
        gridviewTeacher.setAdapter(new CommonAdapter<String>(getContext(), strings, R.layout.item_subject) {
            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public void convert(ViewHolder holder, String item, int position) {
                String s = strings.get(position);
                holder.setText(R.id.subject_text, s);
            }
        });
        gridviewTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TeacherDataActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initGridClass() {
        initDataNews(1);
        initDataHots(1);

        final List<String> strings = Arrays.asList(getResources().getStringArray(R.array.subject));
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return listNews.size() + listHots.size();
            }

            @Override
            public Object getItem(int position) {
                if (position % 2 == 0) {
                    return listNews.get(position / 2);
                } else {
                    return listHots.get(position / 2);
                }
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder helper = ViewHolder.get(getContext(), convertView, parent, R.layout.item_class_recommend, position);
                ((ImageView) helper.getView(R.id.class_recommend_img)).setLayoutParams(new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(getActivity()) / 2, ScreenUtils.getScreenWidth(getActivity()) / 2 * 5 / 8));
                if (position % 2 == 0) {
                    Glide.with(getActivity()).load(listNews.get(position / 2).getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().dontAnimate().into(((ImageView) helper.getView(R.id.class_recommend_img)));
                    helper.setText(R.id.class_recommend_text, "最新");
                } else {
                    Glide.with(getActivity()).load(listHots.get(position / 2).getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().dontAnimate().into(((ImageView) helper.getView(R.id.class_recommend_img)));
                    helper.setText(R.id.class_recommend_text, "最热");
                }
                return helper.getConvertView();
            }
        };
        gridviewClass.setAdapter(adapter);
        gridviewClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                if (position % 2 == 0) {
                    intent.putExtra("id", listNews.get(position / 2).getId());
                } else {
                    intent.putExtra("id", listHots.get(position / 2).getId());
                }
                startActivity(intent);
            }
        });
//        View view = gridviewClass.getAdapter().getView(0, null, gridviewClass);
//        view.measure(0, 0);
//        ViewGroup.LayoutParams layoutParams = gridviewClass.getLayoutParams();
//        layoutParams.height = view.getMeasuredHeight() * 4;
//        gridviewClass.setLayoutParams(layoutParams);
    }

    private void initDataHots(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("per_page", "4");
        map.put("sort_by", "buy_tickets_count.asc");
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass, map), null,
                new VolleyListener(getActivity()) {


                    @Override
                    protected void onSuccess(JSONObject response) {
//                        if (type == 1) {
//                            listHots.clear();
//                        }
//                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
//                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                        grid.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
//                        grid.onRefreshComplete();

                        try {
                            RemedialClassBean data = JsonUtils.objectFromJson(response.toString(), RemedialClassBean.class);
                            if (data != null) {
                                listHots.addAll(data.getData());
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
//                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
//                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                        grid.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
//                        grid.onRefreshComplete();
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
//                grid.onRefreshComplete();
            }
        });

        addToRequestQueue(request);
    }

    private void initDataNews(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("per_page", "4");
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass, map), null,
                new VolleyListener(getActivity()) {


                    @Override
                    protected void onSuccess(JSONObject response) {
                        if (type == 1) {
                            listNews.clear();
                        }
//                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
//                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                        grid.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
//                        grid.onRefreshComplete();
//
                        try {
                            RemedialClassBean data = JsonUtils.objectFromJson(response.toString(), RemedialClassBean.class);
                            if (data != null) {
                                listNews.addAll(data.getData());
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {
//                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
//                                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//                        grid.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
//                        grid.onRefreshComplete();
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
//                grid.onRefreshComplete();
            }
        });
        addToRequestQueue(request);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_class:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.setCurrentPosition(1, "全部");
                break;
            case R.id.refresh_teacher:
                initGridTeacher();
                break;
            case R.id.message:
                Intent intent = new Intent(getActivity(), MessageFragmentActivity.class);
                startActivity(intent);
                break;
        }
    }
}
