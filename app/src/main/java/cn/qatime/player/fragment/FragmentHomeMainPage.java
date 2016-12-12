package cn.qatime.player.fragment;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.CitySelectActivity;
import cn.qatime.player.activity.LoginActivity;
import cn.qatime.player.activity.MainActivity;
import cn.qatime.player.activity.MessageFragmentActivity;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.activity.TeacherDataActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.BannerRecommendBean;
import cn.qatime.player.bean.ClassRecommendBean;
import cn.qatime.player.bean.TeacherRecommendBean;
import cn.qatime.player.utils.AMapLocationUtils;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.CityBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.TagViewPager;

public class FragmentHomeMainPage extends BaseFragment implements View.OnClickListener {


    private TagViewPager tagViewpagerImg;
    private ImageView refreshTeacher;
    private GridView gridviewTeacher;
    private View allClass;
    private GridView gridviewClass;
    private int page = 1;
    private List<ClassRecommendBean.DataBean> listRecommendClass = new ArrayList<>();
    private BaseAdapter classAdapter;
    private ImageView message;
    private ArrayList<TeacherRecommendBean.DataBean> listRecommendTeacher = new ArrayList<>();
    private CommonAdapter<TeacherRecommendBean.DataBean> teacherAdapter;
    private GridView gridviewSubject;
    private View citySelect;
    private TextView cityName;
    private List<CityBean.Data> listCity;
    private CityBean.Data locationCity;
    private AMapLocationUtils utils;
    private List<BannerRecommendBean.DataBean> listBanner = new ArrayList<>();
    private BannerRecommendBean.DataBean noBanner;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_main_page, container, false);
        assignViews(view);
        return view;
    }

    private void assignViews(View view) {
        tagViewpagerImg = (TagViewPager) view.findViewById(R.id.tag_viewpager_img);
        refreshTeacher = (ImageView) view.findViewById(R.id.refresh_teacher);
        gridviewTeacher = (GridView) view.findViewById(R.id.gridview_teacher);
        gridviewSubject = (GridView) view.findViewById(R.id.gridview_subject);
        cityName = (TextView) view.findViewById(R.id.city_name);
        allClass = view.findViewById(R.id.all_class);
        citySelect = view.findViewById(R.id.city_select);
        gridviewClass = (GridView) view.findViewById(R.id.gridview_class);
        message = (ImageView) view.findViewById(R.id.message);

        setCity();

        initTagImg();
        initTagViewpagerSubject();
        initGridTeacher();
        initGridClass();
        initLocationData();
        refreshTeacher.setOnClickListener(this);
        allClass.setOnClickListener(this);
        message.setOnClickListener(this);
        citySelect.setOnClickListener(this);
    }

    private void setCity() {
        cityName.setText(BaseApplication.getCurrentCity().getName());
        page = 1;//重置为第一页
        initBannerData();
        initTeacherData();
        initClassData();
    }

    private void initTagImg() {
//        final int imageIds[] = {R.mipmap.no_banner};
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(getActivity()), ScreenUtils.getScreenWidth(getActivity()) / 3);
        tagViewpagerImg.setLayoutParams(params);
        noBanner = new BannerRecommendBean.DataBean();
        listBanner.add(noBanner);
        tagViewpagerImg.init(R.drawable.shape_photo_tag_select, R.drawable.shape_photo_tag_nomal, 16, 8, 4, 30);
        tagViewpagerImg.setAutoNext(true, 3000);
//        viewPager.setResourceId(1252);
        tagViewpagerImg.setOnGetView(new TagViewPager.OnGetView() {
            @Override
            public View getView(ViewGroup container, int position) {
//                Logger.e("position:" + position + "url:" + listBanner.get(position).getLogo_url());
                ImageView iv = new ImageView(getActivity());
                iv.setClickable(true);
                iv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                iv.setId(position);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(getActivity()).load(listBanner.get(position).getLogo_url()).placeholder(R.mipmap.no_banner).into(iv);
                container.addView(iv);
                return iv;
            }
        });
        tagViewpagerImg.setAdapter(listBanner.size(), 0);
    }

    private void initBannerData() {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("city_name", URLEncoder.encode(BaseApplication.getCurrentCity().getName(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRecommend + "index_banner" + "/items", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        BannerRecommendBean bannerRecommendBean = JsonUtils.objectFromJson(response.toString(), BannerRecommendBean.class);
                        listBanner.clear();
                        if (bannerRecommendBean != null && bannerRecommendBean.getData() != null && bannerRecommendBean.getData().size() > 0) {
                            listBanner.addAll(bannerRecommendBean.getData());
                        }
                        if (listBanner.size() == 0) {
                            listBanner.add(noBanner);
                        }
                        tagViewpagerImg.notifyChanged(listBanner.size());
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

    private void initTagViewpagerSubject() {
        String[] subject = getResources().getStringArray(R.array.subject);
        final int[] icons = {R.mipmap.chinese, R.mipmap.math, R.mipmap.english, R.mipmap.physics, R.mipmap.chemistry, R.mipmap.biology, R.mipmap.history, R.mipmap.geography, R.mipmap.politics, R.mipmap.science};
        final List<String> strings = Arrays.asList(subject);
        gridviewSubject.setAdapter(new CommonAdapter<String>(getContext(), strings, R.layout.item_grid_subject) {
            @Override
            public int getCount() {
                return strings.size();
            }

            @Override
            public void convert(ViewHolder holder, String item, int position) {
                String s = strings.get(position);
                holder.setText(R.id.subject_text, s);
                holder.setImageResource(R.id.subject_img, icons[position]);
            }
        });
        gridviewSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = strings.get(position);
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.setCurrentPosition(1, s);
            }
        });
    }

    private void initGridTeacher() {
        teacherAdapter = new CommonAdapter<TeacherRecommendBean.DataBean>(getContext(), listRecommendTeacher, R.layout.item_grid_teacher) {
            @Override
            public int getCount() {
                return listRecommendTeacher.size();
            }

            @Override
            public void convert(ViewHolder holder, TeacherRecommendBean.DataBean item, int position) {
                if (item != null) {
                    Glide.with(getActivity()).load(item.getTeacher().getAvatar_url()).error(R.mipmap.error_header).centerCrop().bitmapTransform(new GlideCircleTransform(getActivity())).crossFade().dontAnimate().into(((ImageView) holder.getView(R.id.teacher_img)));
                    holder.setText(R.id.teacher_text, item.getTeacher().getName());
                } else {
                    Glide.with(getActivity()).load(R.mipmap.error_header).centerCrop().bitmapTransform(new GlideCircleTransform(getActivity())).crossFade().dontAnimate().into(((ImageView) holder.getView(R.id.teacher_img)));
                    holder.setText(R.id.teacher_text, "教师名");
                }
            }
        };
        gridviewTeacher.setAdapter(teacherAdapter);
        gridviewTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listRecommendTeacher.get(position) != null) {
                    Intent intent = new Intent(getActivity(), TeacherDataActivity.class);
                    intent.putExtra("teacherId", listRecommendTeacher.get(position).getTeacher().getId());
                    startActivityForResult(intent, Constant.REQUEST);
                }

            }
        });
    }

    private void initTeacherData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("per_page", String.valueOf(5));
        try {
            map.put("city_name", URLEncoder.encode(BaseApplication.getCurrentCity().getName(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRecommend + "index_teacher_recommend" + "/items", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        TeacherRecommendBean teacherRecommendBean = JsonUtils.objectFromJson(response.toString(), TeacherRecommendBean.class);
                        //teacherRecommendBean.getData()==null有两种情况  1、没有更多的老师。2、本来就没有老师
                        page++;
                        listRecommendTeacher.clear();
                        if (teacherRecommendBean != null && teacherRecommendBean.getData() != null && teacherRecommendBean.getData().size() > 0) {
                            listRecommendTeacher.addAll(teacherRecommendBean.getData());
                        }
                        if (listRecommendTeacher.size() < 5) {
                            page = 1; //一旦size小于5，说明没有更多推荐老师了，将page重置
                            while (listRecommendTeacher.size() < 5) {
                                listRecommendTeacher.add(null);

                            }
                        }
                        teacherAdapter.notifyDataSetChanged();
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


    private void initGridClass() {
        classAdapter = new CommonAdapter<ClassRecommendBean.DataBean>(getContext(), listRecommendClass, R.layout.item_class_recommend) {
            @Override
            public int getCount() {
                return listRecommendClass.size();
            }

            @Override
            public void convert(ViewHolder holder, ClassRecommendBean.DataBean item, int position) {
                if (item != null) {
                    Glide.with(getActivity()).load(item.getLive_studio_course().getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().dontAnimate().into(((ImageView) holder.getView(R.id.class_recommend_img)));
                    holder.setText(R.id.course_title, item.getLive_studio_course().getName());
                    holder.setText(R.id.grade, item.getLive_studio_course().getGrade());
                    holder.setText(R.id.subject, item.getLive_studio_course().getSubject());
                    holder.setText(R.id.count, item.getLive_studio_course().getBuy_tickets_count() + "人报名");
                    ((TextView) holder.getView(R.id.reason)).setText(getReason(item.getReason()));
                    ((TextView) holder.getView(R.id.reason)).setBackgroundColor(getReasonBackground(item.getReason()));
                    ((TextView) holder.getView(R.id.reason)).setVisibility(View.VISIBLE);
                } else {
                    Glide.with(getActivity()).load(R.mipmap.photo).placeholder(R.mipmap.photo).centerCrop().crossFade().dontAnimate().into(((ImageView) holder.getView(R.id.class_recommend_img)));
                    holder.setText(R.id.course_title, "暂无辅导班数据");
                    holder.setText(R.id.grade, "年级");
                    holder.setText(R.id.subject, "科目");
                    holder.setText(R.id.count, "0人报名");
                    ((TextView) holder.getView(R.id.reason)).setVisibility(View.INVISIBLE);
                }
            }
        };
        gridviewClass.setAdapter(classAdapter);
        gridviewClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listRecommendClass.get(position) != null) {
                    Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                    intent.putExtra("id", listRecommendClass.get(position).getLive_studio_course().getId());
                    startActivityForResult(intent, Constant.REQUEST);
                }
            }
        });
    }

    private String getReason(String reason) {
        if ("latest".equals(reason)) {
            return "最新";
        } else if ("hottest".equals(reason)) {
            return "最热";
        }
        return "";
    }

    private int getReasonBackground(String reason) {
        if ("latest".equals(reason)) {
            return 0xff66cccc;
        } else if ("hottest".equals(reason)) {
            return 0xffff9999;
        }
        return 0x00000000;
    }

    private void initClassData() {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(1));
        map.put("per_page", String.valueOf(6));
        try {
            map.put("city_name", URLEncoder.encode(BaseApplication.getCurrentCity().getName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRecommend + "index_live_studio_course_recommend" + "/items", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        ClassRecommendBean classRecommendBean = JsonUtils.objectFromJson(response.toString(), ClassRecommendBean.class);
                        listRecommendClass.clear();
                        if (classRecommendBean != null && classRecommendBean.getData() != null) {
                            listRecommendClass.addAll(classRecommendBean.getData());
                        }
                        while (listRecommendClass.size() < 6) {
                            listRecommendClass.add(null);
                        }
                        classAdapter.notifyDataSetChanged();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_class:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.setCurrentPosition(1, getResourceString(R.string.whole));
                break;
            case R.id.refresh_teacher:
                ObjectAnimator animator = ObjectAnimator.ofFloat(refreshTeacher, "rotation", 0F, 360F).setDuration(300L);
                animator.setRepeatCount(2);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.start();
                initTeacherData();
                break;
            case R.id.message:
                Intent intent;
                if (BaseApplication.isLogined()) {
                    intent = new Intent(getActivity(), MessageFragmentActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("sign", Constant.VISITORTOLOGIN);
                    intent.putExtra("action", Constant.LoginAction.toMessage);
                    startActivityForResult(intent, Constant.REQUEST);
                }
                break;
            case R.id.city_select:
                intent = new Intent(getActivity(), CitySelectActivity.class);
                startActivityForResult(intent, Constant.REQUEST);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constant.RESPONSE_CITY_SELECT) {
            setCity();
        }
    }


    private void initLocationData() {
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.urlAppconstantInformation + "/cities", null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        CityBean cityBean = JsonUtils.objectFromJson(response.toString(), CityBean.class);
                        if (cityBean != null && cityBean.getData() != null) {
                            listCity = cityBean.getData();
//                                    如果没有被赋值，则默认全国
                            utils = new AMapLocationUtils(getActivity(), new AMapLocationUtils.LocationListener() {
                                @Override
                                public void onLocationBack(String result) {
                                    utils.stopLocation();

                                    for (CityBean.Data item : listCity) {
                                        if (result.equals(item.getName())) {
                                            locationCity = item;
                                        }
                                    }
                                    CityBean.Data currentCity = BaseApplication.getCurrentCity();
                                    if (locationCity != null) {
                                        if (!currentCity.equals(locationCity)) {
                                            if (locationCity.getWorkstations_count() != 0) {
                                                dialogCity();
//                                            } else {
//                                                BaseApplication.setCurrentCity(locationCity);
//                                                setCity();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "暂未获取到城市信息", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            utils.startLocation();
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

    private void dialogCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(getActivity(), R.layout.dialog_cancel_or_confirm, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        text.setText("答疑时间已开通本地工作站，是否\n切换为" + locationCity.getName() + "？");
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button confirm = (Button) view.findViewById(R.id.confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                BaseApplication.setCurrentCity(locationCity);
                setCity();
            }
        });
        alertDialog.show();
        alertDialog.setContentView(view);
    }
}
