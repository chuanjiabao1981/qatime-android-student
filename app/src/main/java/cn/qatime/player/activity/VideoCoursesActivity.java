package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.bean.VideoCoursesDetailsBean;
import cn.qatime.player.fragment.FragmentVideoCoursesClassInfo;
import cn.qatime.player.fragment.FragmentVideoCoursesClassList;
import cn.qatime.player.fragment.FragmentVideoCoursesTeacherInfo;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.ShareUtil;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.view.SimpleViewPagerIndicator;
import libraryextra.bean.OrderPayBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author lungtify
 * @Time 2017/4/10 16:27
 * @Describe 视频课详情
 */

public class VideoCoursesActivity extends BaseFragmentActivity implements View.OnClickListener {
    private String[] mTitles;
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private ViewPager mViewPager;
    private int id;
    private VideoCoursesDetailsBean data;
    private DecimalFormat df = new DecimalFormat("#.00");
    private TextView name;
    private TextView price;
    private TextView studentNumber;
    private RelativeLayout handleLayout;
    private Button auditionStart;
    private LinearLayout startStudyView;
    private AlertDialog alertDialog;
    private TextView freeTaste;
    private TextView couponFree;
    private Button startStudy;
    private PopupWindow pop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_courses);
        id = getIntent().getIntExtra("id", 0);//联网id
        EventBus.getDefault().register(this);
        initView();
        initData();
        initMenu();
    }

    /**

     */
    private void initMenu() {
        if (pop == null) {
            setRightImage(R.mipmap.exclusive_menu, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop.showAsDropDown(v);
                    backgroundAlpha(0.9f);
                }
            });
            View popView = View.inflate(this, R.layout.course_detail_pop_menu, null);
            View menu1 = popView.findViewById(R.id.menu_1);
            View menu2 = popView.findViewById(R.id.menu_2);
            View menu3 = popView.findViewById(R.id.menu_3);
            View menu4 = popView.findViewById(R.id.menu_4);
            View menu5 = popView.findViewById(R.id.menu_5);
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);
            menu5.setOnClickListener(this);
            pop = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1);
                }
            });
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlVideoCourses + id + "/detail", null,
                new VolleyListener(VideoCoursesActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), VideoCoursesDetailsBean.class);

                        if (data != null && data.getData() != null && data.getData().getVideo_course() != null) {
                            handleLayout.setVisibility(View.VISIBLE);
                            name.setText(data.getData().getVideo_course().getName());
                            setTitles(data.getData().getVideo_course().getName());
                            studentNumber.setText("学习人数" + data.getData().getVideo_course().getBuy_tickets_count());

                            if (data.getData().getVideo_course().getSell_type().equals("charge")) {
                                String priceStr = df.format(Float.valueOf(data.getData().getVideo_course().getPrice()));
                                if (priceStr.startsWith(".")) {
                                    priceStr = "0" + priceStr;
                                }
                                price.setText("￥" + priceStr);

                                if (data.getData().getTicket() != null && data.getData().getTicket().getStatus().equals("active")) {//已购买
                                    startStudyView.setVisibility(View.VISIBLE);
                                    startStudy.setText("观看");
                                } else {//未购买
                                    if (data.getData().getVideo_course().isOff_shelve()) {//已下架
                                        price.setText("已下架");
                                        handleLayout.setVisibility(View.GONE);
                                    } else {
                                        if (data.getData().getVideo_course().getTaste_count() > 0) {//显示进入试听按钮(没有加入试听按钮)
                                            auditionStart.setVisibility(View.VISIBLE);
                                        } else {
                                            auditionStart.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            } else if (data.getData().getVideo_course().getSell_type().equals("free")) {
                                price.setText("免费");
                                startStudyView.setVisibility(View.VISIBLE);
                                if (data.getData().getTicket() != null && data.getData().getTicket().getStatus().equals("active")) {
                                    startStudy.setText("观看");
                                } else {
                                    if (data.getData().getVideo_course().isOff_shelve()) {
                                        price.setText("已下架");
                                        handleLayout.setVisibility(View.GONE);
                                    } else {
                                        startStudy.setBackgroundResource(R.drawable.button_bg_selector_red_with_stork);
                                        startStudy.setTextColor(getResources().getColor(R.color.colorPrimary));
                                        startStudy.setText("立即学习");
                                    }
                                }
                            }

                            if (data.getData().getVideo_course().getIcons() != null) {
                                if (!data.getData().getVideo_course().getIcons().isFree_taste()) {
                                    freeTaste.setVisibility(View.GONE);
                                }
                                if (!data.getData().getVideo_course().getIcons().isCoupon_free()) {
                                    couponFree.setVisibility(View.GONE);
                                }
                            }
                            ((FragmentVideoCoursesClassInfo) fragBaseFragments.get(0)).setData(data);
                            ((FragmentVideoCoursesTeacherInfo) fragBaseFragments.get(1)).setData(data);
                            ((FragmentVideoCoursesClassList) fragBaseFragments.get(2)).setData(data);

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
        freeTaste = (TextView) findViewById(R.id.free_taste);
        couponFree = (TextView) findViewById(R.id.coupon_free);
        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        studentNumber = (TextView) findViewById(R.id.student_number);
        handleLayout = (RelativeLayout) findViewById(R.id.handle_layout);
        auditionStart = (Button) findViewById(R.id.audition_start);
        Button pay = (Button) findViewById(R.id.pay);
        startStudyView = (LinearLayout) findViewById(R.id.start_study_view);
        startStudy = (Button) findViewById(R.id.start_study);

        auditionStart.setOnClickListener(this);
        pay.setOnClickListener(this);
        startStudy.setOnClickListener(this);

        fragBaseFragments.add(new FragmentVideoCoursesClassInfo());
        fragBaseFragments.add(new FragmentVideoCoursesTeacherInfo());
        fragBaseFragments.add(new FragmentVideoCoursesClassList());

        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mTitles = new String[]{getString(R.string.remedial_detail), getString(R.string.teacher_detail), "课时安排"};
        mIndicator.setTitles(mTitles);

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return fragBaseFragments.get(position);
            }
        };

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mIndicator.select(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mIndicator.setOnItemClickListener(new SimpleViewPagerIndicator.OnItemClickListener() {
            @Override
            public void OnClick(int position) {
                mViewPager.setCurrentItem(position);
            }
        });
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mAdapter);
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == 0) {
                    Toast.makeText(VideoCoursesActivity.this, "id为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ShareUtil.getInstance(VideoCoursesActivity.this, UrlUtils.getBaseUrl() + "live_studio/video_courses/" + id, name.getText().toString(), "视频课课程", new ShareUtil.ShareListener() {
                    @Override
                    public void onSuccess(SHARE_MEDIA platform) {

                    }

                }).open();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (data == null || data.getData().getVideo_course() == null) {
            return;
        }
        Intent intent;
        switch (v.getId()) {
            case R.id.audition_start:
                if (BaseApplication.getInstance().isLogined()) {
                    if (data.getData().getTicket() == null) {//未加入试听
                        joinAudition();
                    } else {
                        intent = new Intent(VideoCoursesActivity.this, VideoCoursesPlayActivity.class);
                        intent.putExtra("id", data.getData().getVideo_course().getId());
                        intent.putExtra("tasting", true);
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(VideoCoursesActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
            case R.id.start_study:
                if (BaseApplication.getInstance().isLogined()) {
                    if (data.getData().getVideo_course().getSell_type().equals("free")) {
                        if (data.getData().getTicket() == null) {//免费课程未购买则购买
                            joinMyFreeVideo();//获取免费课程票号
                        } else {//已购买进入播放页
                            intent = new Intent(VideoCoursesActivity.this, VideoCoursesPlayActivity.class);
                            intent.putExtra("id", data.getData().getVideo_course().getId());
                            intent.putExtra("tasting", false);
                            startActivity(intent);
                        }
                    } else {
                        intent = new Intent(VideoCoursesActivity.this, VideoCoursesPlayActivity.class);
                        intent.putExtra("id", data.getData().getVideo_course().getId());
                        intent.putExtra("tasting", false);
                        startActivity(intent);
                    }
                } else {
                    intent = new Intent(VideoCoursesActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
            case R.id.pay:
                if (BaseApplication.getInstance().isLogined()) {
                    if ("teaching".equals(data.getData().getVideo_course().getStatus())) {
                        if (alertDialog == null) {
                            View view = View.inflate(VideoCoursesActivity.this, R.layout.dialog_cancel_or_confirm, null);
                            Button cancel = (Button) view.findViewById(R.id.cancel);
                            Button confirm = (Button) view.findViewById(R.id.confirm);
                            TextView text = (TextView) view.findViewById(R.id.text);
                            text.setText(R.string.continue_buy_lasses_lesson);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                            confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    payRemedial();
                                    alertDialog.dismiss();
                                }
                            });
                            AlertDialog.Builder builder = new AlertDialog.Builder(VideoCoursesActivity.this);
                            alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.setContentView(view);
                        } else {
                            alertDialog.show();
                        }
                    } else {
                        payRemedial();
                    }
                } else {
                    intent = new Intent(VideoCoursesActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                break;
            case R.id.menu_5:
                intent = new Intent(VideoCoursesActivity.this, MembersActivity.class);
                intent.putExtra("type", "video");
                intent.putExtra("id", id);
                startActivity(intent);
                pop.dismiss();
                break;
        }
    }

    private void payRemedial() {
        Intent intent = new Intent(VideoCoursesActivity.this, OrderConfirmActivity.class);
        intent.putExtra("courseType", "video");
        intent.putExtra("id", id);
        intent.putExtra("coupon", getIntent().getStringExtra("coupon"));
        OrderPayBean bean = new OrderPayBean();
        bean.name = data.getData().getVideo_course().getName();
        bean.subject = data.getData().getVideo_course().getSubject();
        bean.grade = data.getData().getVideo_course().getGrade();
        bean.classnumber = data.getData().getVideo_course().getVideo_lessons_count();
        bean.teacher = data.getData().getVideo_course().getTeacher().getName();
        bean.current_price = Float.valueOf(data.getData().getVideo_course().getPrice());

        intent.putExtra("data", bean);
        startActivity(intent);
    }

    @Subscribe
    public void onEvent(PayResultState code) {
//        if (!StringUtils.isNullOrBlanK(event) && event.equals("pay_success")) {
//
//            finish();
//        }
        finish();
    }

    private void joinMyFreeVideo() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.urlVideoCourses + id + "/deliver_free", null,
                new VolleyListener(VideoCoursesActivity.this) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        Toast.makeText(VideoCoursesActivity.this, "已成功加入我的视频课", Toast.LENGTH_SHORT).show();
                        startStudy.setBackgroundResource(R.drawable.button_bg_selector_red);
                        startStudy.setTextColor(Color.WHITE);
                        startStudy.setText("观看");
                        data.getData().setTicket(new VideoCoursesDetailsBean.DataBean.TicketBean());
                        data.getData().getTicket().setStatus("active");
                        Intent intent = new Intent(VideoCoursesActivity.this, VideoCoursesPlayActivity.class);
                        intent.putExtra("id", data.getData().getVideo_course().getId());
                        intent.putExtra("tasting", false);
                        startActivity(intent);
                    }

                    @Override
                    protected void onError(JSONObject response) {
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                }

                , new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    private void joinAudition() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.urlVideoCourses + id + "/taste", null,
                new VolleyListener(VideoCoursesActivity.this) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        Toast.makeText(VideoCoursesActivity.this, "已成功加入我的试听", Toast.LENGTH_SHORT).show();
                        data.getData().setTicket(new VideoCoursesDetailsBean.DataBean.TicketBean());
                        data.getData().getTicket().setStatus("inactive");
                        Intent intent = new Intent(VideoCoursesActivity.this, VideoCoursesPlayActivity.class);
                        intent.putExtra("id", data.getData().getVideo_course().getId());
                        intent.putExtra("tasting", true);
                        startActivity(intent);
                    }

                    @Override
                    protected void onError(JSONObject response) {
//                            if(response.getJSONObject("error").getInt("code")==3004){//CourseTasteLimit
                        Toast.makeText(VideoCoursesActivity.this, R.string.the_course_not_support_audition, Toast.LENGTH_SHORT).show();
//                                            }
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
