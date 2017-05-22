package cn.qatime.player.fragment;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.CitySelectActivity;
import cn.qatime.player.activity.InteractCourseDetailActivity;
import cn.qatime.player.activity.MainActivity;
import cn.qatime.player.activity.PayPSWForgetActivity;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.activity.TeacherDataActivity;
import cn.qatime.player.activity.VideoCoursesActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.BannerRecommendBean;
import cn.qatime.player.bean.BusEvent;
import cn.qatime.player.bean.EssenceContentBean;
import cn.qatime.player.bean.LiveTodayBean;
import cn.qatime.player.bean.RecentPublishedBean;
import cn.qatime.player.bean.TeacherRecommendBean;
import cn.qatime.player.holder.BaseViewHolder;
import cn.qatime.player.qrcore.core.CaptureActivity;
import cn.qatime.player.utils.AMapLocationUtils;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.adapter.CommonAdapter;
import libraryextra.adapter.ViewHolder;
import libraryextra.bean.CashAccountBean;
import libraryextra.bean.CityBean;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.DensityUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.ListViewForScrollView;
import libraryextra.view.TagViewPager;

public class FragmentHomeMainPage extends BaseFragment implements View.OnClickListener {

    private TagViewPager tagViewpagerImg;
    private GridView gridviewTeacher;
    private ListViewForScrollView listViewEssenceContent;
    private List<EssenceContentBean.DataBean> listEssenceContent = new ArrayList<>();
    private CommonAdapter<EssenceContentBean.DataBean> essenceContentAdapter;
    private CommonAdapter<RecentPublishedBean.DataBean.StartRankBean> startRankAdapter;
    private CommonAdapter<RecentPublishedBean.DataBean.PublishedRankBean> publisheRankAdapter;
    private ArrayList<TeacherRecommendBean.DataBean> listRecommendTeacher = new ArrayList<>();
    private CommonAdapter<TeacherRecommendBean.DataBean> teacherAdapter;
    private TextView cityName;
    private List<CityBean.Data> listCity;
    private CityBean.Data locationCity;
    private AMapLocationUtils utils;
    private List<BannerRecommendBean.DataBean> listBanner = new ArrayList<>();

    private BannerRecommendBean.DataBean noBanner;
    private PullToRefreshScrollView scrollView;
    private RecyclerView recyclerGrade;
    private List<String> gradeList;
    private RecyclerView.Adapter gradeAdapter;
    private RecyclerView recyclerToday;
    private RecyclerView.Adapter todayAdapter;
    private List<LiveTodayBean.DataBean> todayList;
    private ListViewForScrollView listViewStartRank;//近期开课
    private ListViewForScrollView listViewPublishedRank;//最新发布
    private List<RecentPublishedBean.DataBean.StartRankBean> listStartRank = new ArrayList<>();
    private List<RecentPublishedBean.DataBean.PublishedRankBean> listPublishedRank = new ArrayList<>();
    private View cashAccountSafe;
    private View close;
    private boolean closed = false;//是否手动关闭未设置支付密码提示

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_home_main_page, container, false);
        assignViews(view);
        return view;
    }

    private void assignViews(View view) {
        scrollView = (PullToRefreshScrollView) view.findViewById(R.id.scroll);
        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        scrollView.getLoadingLayoutProxy(true, false).setPullLabel(getResourceString(R.string.pull_to_refresh));
        scrollView.getLoadingLayoutProxy(false, true).setPullLabel(getResourceString(R.string.pull_to_load));
        scrollView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResourceString(R.string.refreshing));
        scrollView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResourceString(R.string.loading));
        scrollView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResourceString(R.string.release_to_refresh));
        scrollView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResourceString(R.string.release_to_load));

        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String label = DateUtils.formatDateTime(
                                getActivity(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        // Update the LastUpdatedLabel
                        scrollView.getLoadingLayoutProxy(false, true)
                                .setLastUpdatedLabel(label);
                        scrollView.onRefreshComplete();
                    }
                }, 200);
                setCity();
            }
        });

        tagViewpagerImg = (TagViewPager) view.findViewById(R.id.tag_viewpager_img);
        gridviewTeacher = (GridView) view.findViewById(R.id.gridview_teacher);
        cityName = (TextView) view.findViewById(R.id.city_name);
        View citySelect = view.findViewById(R.id.city_select);
        listViewEssenceContent = (ListViewForScrollView) view.findViewById(R.id.listview_class);
        listViewStartRank = (ListViewForScrollView) view.findViewById(R.id.listview_class1);
        listViewPublishedRank = (ListViewForScrollView) view.findViewById(R.id.listview_class2);
        View scan = view.findViewById(R.id.scan);
        cashAccountSafe = view.findViewById(R.id.cash_account_safe);
        close = view.findViewById(R.id.close);
        recyclerGrade = (RecyclerView) view.findViewById(R.id.recycler_grade);
        recyclerToday = (RecyclerView) view.findViewById(R.id.recycler_today);
        view.findViewById(R.id.more1).setOnClickListener(this);
        view.findViewById(R.id.more2).setOnClickListener(this);
        view.findViewById(R.id.more3).setOnClickListener(this);

        initBanner();
        initGrade();
        initToady();
        initEssence();

        initTeacher();
        initStartRank();
        initPublishedRank();

        setCity();
        initLocationData();
        initCashAccountSafe();
        scan.setOnClickListener(this);
        citySelect.setOnClickListener(this);
    }

    @Subscribe
    public void onEvent(BusEvent event) {
        if (BusEvent.ON_REFRESH_CASH_ACCOUNT == event && !closed)
            initCashAccountSafe();
    }

    private void initCashAccountSafe() {
        CashAccountBean cashAccount = BaseApplication.getCashAccount();
        if (cashAccount != null && cashAccount.getData() != null) {
            if (!cashAccount.getData().isHas_password()) {
                cashAccountSafe.setVisibility(View.VISIBLE);
                cashAccountSafe.setOnClickListener(this);
                close.setOnClickListener(this);
            }else{
                cashAccountSafe.setVisibility(View.GONE);
            }
        }
    }

    private void initPublishedRank() {
        //最新发布
        publisheRankAdapter = new CommonAdapter<RecentPublishedBean.DataBean.PublishedRankBean>(getContext(), listPublishedRank, R.layout.item_course_rank) {
            @Override
            public void convert(ViewHolder holder, RecentPublishedBean.DataBean.PublishedRankBean item, int position) {
                holder.setImageByUrl(R.id.image, item.getPublicize(), R.mipmap.photo)
                        .setText(R.id.title, item.getName())
                        .setText(R.id.teacher, item.getTeacher_name())
                        .setText(R.id.grade_subject, item.getGrade() + item.getSubject());
            }
        };
        listViewPublishedRank.setAdapter(publisheRankAdapter);
        ViewGroup parent = (ViewGroup) listViewPublishedRank.getParent();
        View inflate = View.inflate(getActivity(), R.layout.empty_view, null);
        inflate.setBackgroundColor(0xffffffff);
        parent.addView(inflate, parent.indexOfChild(listViewPublishedRank) + 1);
        listViewPublishedRank.setEmptyView(inflate);
        listViewPublishedRank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int courseId = listPublishedRank.get(position).getId();
                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                intent.putExtra("id", courseId);
                startActivity(intent);
            }
        });
    }

    private void initStartRank() {
        //近期开课
        startRankAdapter = new CommonAdapter<RecentPublishedBean.DataBean.StartRankBean>(getContext(), listStartRank, R.layout.item_course_rank) {
            @Override
            public void convert(ViewHolder holder, RecentPublishedBean.DataBean.StartRankBean item, int position) {
                holder.setImageByUrl(R.id.image, item.getPublicize(), R.mipmap.photo)
                        .setText(R.id.title, item.getName())
                        .setText(R.id.teacher, item.getTeacher_name())
                        .setText(R.id.grade_subject, item.getGrade() + item.getSubject());
            }
        };
        listViewStartRank.setAdapter(startRankAdapter);
        ViewGroup parent = (ViewGroup) listViewStartRank.getParent();
        View inflate = View.inflate(getActivity(), R.layout.empty_view, null);
        parent.addView(inflate, parent.indexOfChild(listViewStartRank) + 1);
        inflate.setBackgroundColor(0xffffffff);
        listViewStartRank.setEmptyView(inflate);
        listViewStartRank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int courseId = listStartRank.get(position).getId();
                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                intent.putExtra("id", courseId);
                startActivity(intent);
            }
        });
    }

    private void initEssence() {
        essenceContentAdapter = new CommonAdapter<EssenceContentBean.DataBean>(getContext(), listEssenceContent, R.layout.item_essence_content) {
            @Override
            public void convert(ViewHolder holder, EssenceContentBean.DataBean item, int position) {
                if ("LiveStudio::Course".equals(item.getTarget_type())) {
                    holder.setImageByUrl(R.id.image, item.getLogo_url(), R.mipmap.photo)
                            .setText(R.id.course_title, item.getLive_studio_course().getName())
                            .setText(R.id.grade_subject, item.getLive_studio_course().getGrade() + item.getLive_studio_course().getSubject())
                            .setText(R.id.teacher, item.getLive_studio_course().getTeacher_name());
                } else if ("LiveStudio::InteractiveCourse".equals(item.getTarget_type())) {
                    holder.setImageByUrl(R.id.image, item.getLogo_url(), R.mipmap.photo)
                            .setText(R.id.course_title, item.getLive_studio_interactive_course().getName())
                            .setText(R.id.grade_subject, item.getLive_studio_interactive_course().getGrade() + item.getLive_studio_interactive_course().getSubject())
                            .setText(R.id.teacher, item.getLive_studio_interactive_course().getTeachers().get(0).getName());
                }else if ("LiveStudio::VideoCourse".equals(item.getTarget_type())) {
                    holder.setImageByUrl(R.id.image, item.getLogo_url(), R.mipmap.photo)
                            .setText(R.id.course_title, item.getLive_studio_video_course().getName())
                            .setText(R.id.grade_subject, item.getLive_studio_video_course().getGrade() + item.getLive_studio_video_course().getSubject())
                            .setText(R.id.teacher, item.getLive_studio_video_course().getTeacher_name());
                }

                holder.getView(R.id.reason1).setVisibility(StringUtils.isNullOrBlanK(item.getTag_one()) ? View.GONE : View.VISIBLE);
                holder.setText(R.id.reason1, getTags(item.getTag_one()));
                holder.getView(R.id.reason2).setVisibility(StringUtils.isNullOrBlanK(item.getTag_two()) ? View.GONE : View.VISIBLE);
                holder.setText(R.id.reason2, getTags(item.getTag_two()));
            }
        };
        listViewEssenceContent.setAdapter(essenceContentAdapter);
        ViewGroup parent = (ViewGroup) listViewEssenceContent.getParent();
        View inflate = View.inflate(getActivity(), R.layout.empty_view, null);
        inflate.setBackgroundColor(0xffffffff);
        parent.addView(inflate, parent.indexOfChild(listViewEssenceContent) + 1);
        listViewEssenceContent.setEmptyView(inflate);
        listViewEssenceContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int courseId = 0;
                Intent intent = null;
                if ("LiveStudio::Course".equals(listEssenceContent.get(position).getTarget_type())) {
                    courseId = listEssenceContent.get(position).getLive_studio_course().getId();
                    intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                } else if ("LiveStudio::InteractiveCourse".equals(listEssenceContent.get(position).getTarget_type())) {
                    courseId = listEssenceContent.get(position).getLive_studio_interactive_course().getId();
                    intent = new Intent(getActivity(), InteractCourseDetailActivity.class);
                }else if ("LiveStudio::VideoCourse".equals(listEssenceContent.get(position).getTarget_type())) {
                    courseId = listEssenceContent.get(position).getLive_studio_video_course().getId();
                    intent = new Intent(getActivity(), VideoCoursesActivity.class);
                }
                intent.putExtra("id", courseId);
                startActivity(intent);
            }
        });
    }

    private String getTags(String tag) {
        String result = "";
        if (StringUtils.isNullOrBlanK(tag)) return result;
        switch (tag) {
            case "star_teacher":
                result = "名师";
                break;
            case "best_seller":
                result = "畅销";
                break;
            case "free_tastes":
                result = "试听";
                break;
            case "join_cheap":
                result = "试听";
                break;
        }
        return result;
    }

    private void initToady() {
        todayList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerToday.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.right = DensityUtils.dip2px(getActivity(), 10);
                outRect.bottom = 0;
                outRect.top = 0;
                outRect.left = DensityUtils.dip2px(getActivity(), 10);

                if (parent.getChildPosition(view) == 0)
                    outRect.left = 0;
                if (parent.getChildAdapterPosition(view) == todayList.size())
                    outRect.right = 0;
            }
        });
        recyclerToday.setLayoutManager(layoutManager);
        todayAdapter = new RecyclerView.Adapter<BaseViewHolder>() {
            @Override
            public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View item = View.inflate(getActivity(), R.layout.item_home_today, null);
                return new BaseViewHolder(item);
            }

            @Override
            public void onBindViewHolder(BaseViewHolder holder, final int position) {
                if (todayList.size() > 0) {
                    LiveTodayBean.DataBean item = todayList.get(position);
                    holder.setText(R.id.teaching_name, item.getName())
                            .setImageByUrl(R.id.image, item.getCourse().getPublicize(), R.mipmap.photo)
                            .setText(R.id.time, item.getLive_time())
                            .setText(R.id.status, getTodayStatusText(item.getStatus()))
                            .setTextColor(R.id.status, getTodayStatusColor(item.getStatus()));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int courseId = todayList.get(position).getCourse().getId();
                            Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                            intent.putExtra("id", courseId);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public int getItemCount() {
                return todayList.size() > 0 ? todayList.size() : 1;
            }
        };
        recyclerToday.setAdapter(todayAdapter);

    }

    private void initGrade() {
        gradeList = new ArrayList<>();
        gradeList.add("高三");
        gradeList.add("高二");
        gradeList.add("高一");
        gradeList.add("初三");
        gradeList.add("初二");
        gradeList.add("初一");
        gradeList.add("六年级");
        gradeList.add("五年级");
        gradeList.add("四年级");
        gradeList.add("三年级");
        gradeList.add("二年级");
        gradeList.add("一年级");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerGrade.setLayoutManager(layoutManager);
        gradeAdapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View textView = View.inflate(getActivity(), R.layout.item_home_grade, null);
                return new BaseViewHolder(textView);
            }

            @Override
            public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
                String info = gradeList.get(position);
                TextView textView = (TextView) holder.itemView;
                textView.setText(info);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.setCurrentPosition(1, position);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return gradeList.size();
            }
        };
        recyclerGrade.setAdapter(gradeAdapter);
    }


    private void setCity() {
        cityName.setText(BaseApplication.getCurrentCity().getName());

        initBannerData();
        initEssenceData();//精选内容
        initToadyData();//今日直播
        initTeacherData();
        initRecentPublished();//最新发布,近期开课
    }

    private void initEssenceData() {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("city_name", URLEncoder.encode(BaseApplication.getCurrentCity().getName(), "UTF-8"));
            map.put("per_page", "4");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRecommend + "index_choiceness_item" + "/items", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        EssenceContentBean data = JsonUtils.objectFromJson(response.toString(), EssenceContentBean.class);
                        listEssenceContent.clear();
                        if (data != null && data.getData() != null) {
                            listEssenceContent.addAll(data.getData());
                            essenceContentAdapter.notifyDataSetChanged();
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

    /**
     * 今日直播数据
     */
    private void initToadyData() {
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.lessons + "today", null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        LiveTodayBean data = JsonUtils.objectFromJson(response.toString(), LiveTodayBean.class);
                        todayList.clear();
                        if (data != null && data.getData() != null) {
                            todayList.addAll(data.getData());
                            todayAdapter.notifyDataSetChanged();
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

    /**
     * 最新发布,近期开课
     */
    private void initRecentPublished() {
        Map<String, String> map = new HashMap<>();
        map.put("count", "2");
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRecentPublished, map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        RecentPublishedBean data = JsonUtils.objectFromJson(response.toString(), RecentPublishedBean.class);
                        listStartRank.clear();
                        listPublishedRank.clear();
                        if (data != null && data.getData() != null) {
                            listStartRank.addAll(data.getData().getStart_rank());
                            listPublishedRank.addAll(data.getData().getPublished_rank());
                            startRankAdapter.notifyDataSetChanged();
                            publisheRankAdapter.notifyDataSetChanged();
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


    private void initBanner() {
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
            map.put("city_name", URLEncoder.encode(BaseApplication.getCurrentCity().getName(), "UTF-8"));
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


    private void initTeacher() {
        teacherAdapter = new CommonAdapter<TeacherRecommendBean.DataBean>(getContext(), listRecommendTeacher, R.layout.item_grid_teacher) {
            @Override
            public void convert(ViewHolder holder, TeacherRecommendBean.DataBean item, int position) {
                if (item != null) {
                    Glide.with(getActivity()).load(item.getTeacher().getAvatar_url()).error(R.mipmap.error_header).centerCrop().bitmapTransform(new GlideCircleTransform(getActivity())).crossFade().dontAnimate().into(((ImageView) holder.getView(R.id.teacher_img)));
                    holder.setText(R.id.teacher_text, item.getTeacher().getName());
                } else {
                    Glide.with(getActivity()).load(R.mipmap.error_header).centerCrop().bitmapTransform(new GlideCircleTransform(getActivity())).crossFade().dontAnimate().into(((ImageView) holder.getView(R.id.teacher_img)));
                    holder.setText(R.id.teacher_text, getString(R.string.teacher_name));
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

    //gridview横向布局方法
    public void horizontalLayout() {
        int size = listRecommendTeacher.size();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int screenWidth = ScreenUtils.getScreenWidth(getActivity());
        int itemWidth = (int) ((screenWidth - density * 10 * 2) / 5);
        int allWidth = itemWidth * size;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                allWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gridviewTeacher.setLayoutParams(params);// 设置GirdView布局参数
        gridviewTeacher.setColumnWidth(itemWidth);// 列表项宽
        gridviewTeacher.setStretchMode(GridView.NO_STRETCH);
        gridviewTeacher.setNumColumns(size);//总长度
    }

    private void initTeacherData() {
        Map<String, String> map = new HashMap<>();
        try {
            map.put("city_name", URLEncoder.encode(BaseApplication.getCurrentCity().getName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRecommend + "index_teacher_recommend" + "/items", map), null,
                new VolleyListener(getActivity()) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        TeacherRecommendBean teacherRecommendBean = JsonUtils.objectFromJson(response.toString(), TeacherRecommendBean.class);
                        listRecommendTeacher.clear();
                        if (teacherRecommendBean != null && teacherRecommendBean.getData() != null && teacherRecommendBean.getData().size() > 0) {
                            listRecommendTeacher.addAll(teacherRecommendBean.getData());
                        }
                        if (listRecommendTeacher.size() < 5) {
                            while (listRecommendTeacher.size() < 5) {
                                listRecommendTeacher.add(null);
                            }
                        }
                        horizontalLayout();
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


    private int getTodayStatusColor(String status) {
        if ("ready".equals(status)) {
            return 0xff4873ff;
        } else if ("closed".equals(status)) {
            return 0xff999999;
        } else {
            return 0xffff5842;
        }
    }

    private String getTodayStatusText(String status) {
        if ("ready".equals(status)) {
            return "尚未直播";
        } else if ("closed".equals(status)) {
            return "直播结束";
        } else {
            return "正在直播";
        }
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        MainActivity mainActivity = (MainActivity) getActivity();
        Intent intent;
        switch (v.getId()) {
            case R.id.more1:
            case R.id.more2:
            case R.id.more3:
                mainActivity.setCurrentPosition(1, 0);
                break;
            case R.id.scan:
                intent = new Intent(getActivity(), CaptureActivity.class);
                mainActivity.startActivityForResult(intent, Constant.REQUEST);
                break;
            case R.id.city_select:
                intent = new Intent(getActivity(), CitySelectActivity.class);
                startActivityForResult(intent, Constant.REQUEST);
                break;
            case R.id.cash_account_safe:
                intent = new Intent(getActivity(), PayPSWForgetActivity.class);
                startActivity(intent);
                break;
            case R.id.close:
                closed = true;
                cashAccountSafe.setVisibility(View.GONE);
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
                                public void onLocationBack(String[] result) {
                                    if (result != null && result.length > 1) {
                                        for (CityBean.Data item : listCity) {
                                            if (result[2].equals(item.getName()) || result[1].equals(item.getName())) {//需先对比区,区不对应往上对比市,不可颠倒
                                                locationCity = item;
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), R.string.position_locate_error, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    CityBean.Data currentCity = BaseApplication.getCurrentCity();
                                    if (locationCity != null) {
                                        if (!currentCity.equals(locationCity)) {
                                            if (locationCity.getWorkstations_count() != 0) {
                                                dialogCity();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), R.string.position_locate_error, Toast.LENGTH_SHORT).show();
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
        text.setText(getString(R.string.position_locate_success) + locationCity.getName());
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
