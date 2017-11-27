package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.fragment.FragmentInteractDetailClassInfo;
import cn.qatime.player.fragment.FragmentInteractDetailClassList;
import cn.qatime.player.fragment.FragmentInteractDetailTeachersInfo;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.MPermission;
import cn.qatime.player.utils.MPermissionUtil;
import cn.qatime.player.utils.ShareUtil;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.annotation.OnMPermissionDenied;
import cn.qatime.player.utils.annotation.OnMPermissionGranted;
import cn.qatime.player.utils.annotation.OnMPermissionNeverAskAgain;
import cn.qatime.player.view.SimpleViewPagerIndicator;
import libraryextra.bean.InteractCourseDetailBean;
import libraryextra.bean.OrderPayBean;
import libraryextra.bean.TeacherBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.NetUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

public class InteractCourseDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    private int id;
    private String[] mTitles;
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private TextView name;
    private TextView title;
    private InteractCourseDetailBean data;
    private ViewPager mViewPager;
    private int pager = 0;
    private TextView price;
    private DecimalFormat df = new DecimalFormat("#.00");
    private AlertDialog alertDialog;
    private View startStudyView;
    private View handleLayout;
    private PopupWindow pop;
    private InteractCourseDetailBean playInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interact_course_detail);

        id = getIntent().getIntExtra("id", 0);//联网id
        pager = getIntent().getIntExtra("pager", 0);
        initView();
        if (id == 0) {
            Toast.makeText(this, getResources().getString(R.string.no_tutorial_classes_ID), Toast.LENGTH_SHORT).show();
            return;
        }
        initData();

    }


    public void initView() {
        EventBus.getDefault().register(this);
        name = (TextView) findViewById(R.id.name);

        fragBaseFragments.add(new FragmentInteractDetailClassInfo());
        fragBaseFragments.add(new FragmentInteractDetailTeachersInfo());
        fragBaseFragments.add(new FragmentInteractDetailClassList());

        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        handleLayout = findViewById(R.id.handle_layout);
        Button pay = (Button) findViewById(R.id.pay);
        Button startStudy = (Button) findViewById(R.id.start_study);
        startStudyView = findViewById(R.id.start_study_view);


        pay.setOnClickListener(this);
        startStudy.setOnClickListener(this);

        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mTitles = new String[]{getString(R.string.remedial_detail), getString(R.string.teachers_detail), getString(R.string.course_arrangement)};
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


        mViewPager.setOffscreenPageLimit(2);
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

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(pager);

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id == 0) {
                    Toast.makeText(InteractCourseDetailActivity.this, "id为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ShareUtil.getInstance(InteractCourseDetailActivity.this, UrlUtils.getBaseUrl() + "live_studio/interactive_courses/" + id, name.getText().toString(), "一对一课程", new ShareUtil.ShareListener() {
                    @Override
                    public void onSuccess(SHARE_MEDIA platform) {

                    }

                }).open();
            }
        });
    }

    /**
     * @param tasteOrBought 已购买或已试听
     * @param status        课程状态
     */
    private void initMenu(boolean tasteOrBought, String status) {
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
            if (!tasteOrBought) {
                menu1.setVisibility(View.GONE);
            }
            if (Constant.CourseStatus.completed.equals(status)) {
                menu1.setVisibility(View.GONE);
            }
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);
            menu1.setOnClickListener(this);
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
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlInteractCourses + id + "/detail", null,
                new VolleyListener(InteractCourseDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), InteractCourseDetailBean.class);

                        if (data != null && data.getData() != null && data.getData().getInteractive_course() != null && data.getData().getInteractive_course().getLive_start_time() != null) {
                            handleLayout.setVisibility(View.VISIBLE);
                            name.setText(data.getData().getInteractive_course().getName());
                            title.setText(data.getData().getInteractive_course().getName());
                            String priceStr;
                            priceStr = df.format(Double.valueOf(data.getData().getInteractive_course().getPrice()));
                            if (priceStr.startsWith(".")) {
                                priceStr = "0" + priceStr;
                            }
                            price.setText("￥" + priceStr);

                            ((FragmentInteractDetailClassInfo) fragBaseFragments.get(0)).setData(data);
                            ((FragmentInteractDetailTeachersInfo) fragBaseFragments.get(1)).setData(data);
                            ((FragmentInteractDetailClassList) fragBaseFragments.get(2)).setData(data);


                            if (data.getData().getTicket() != null && "active".equals(data.getData().getTicket().getStatus())) {//已购买
                                initMenu(true, data.getData().getInteractive_course().getStatus());
                                startStudyView.setVisibility(View.VISIBLE);
                                if (Constant.CourseStatus.completed.equals(data.getData().getInteractive_course().getStatus())) {
                                    handleLayout.setVisibility(View.GONE);//已结束的课程隐藏操作按钮
                                }
                            } else {//未购买
                                initMenu(false, data.getData().getInteractive_course().getStatus());
                                if (data.getData().getInteractive_course().isOff_shelve() || Constant.CourseStatus.completed.equals(data.getData().getInteractive_course().getStatus())) {//已下架或未购买已结束显示已下架
                                    handleLayout.setVisibility(View.GONE);
                                    price.setText("已下架");
                                }
                            }
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
        DaYiJsonObjectRequest requestPlay = new DaYiJsonObjectRequest(UrlUtils.urlInteractCourses + id + "/detail", null,
                new VolleyListener(InteractCourseDetailActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        playInfo = JsonUtils.objectFromJson(response.toString(), InteractCourseDetailBean.class);

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
        addToRequestQueue(requestPlay);
    }

    @Override
    public void onClick(View v) {
        if (data == null || data.getData() == null) {
            return;
        }
        Intent intent;
        switch (v.getId()) {
            case R.id.start_study:
                if (BaseApplication.getInstance().isLogined()) {
                    if ("init".equals(data.getData().getInteractive_course().getStatus()) || "published".equals(data.getData().getInteractive_course().getStatus())) {
                        Toast.makeText(this, getString(R.string.published_course_unable_enter) + getString(R.string.study), Toast.LENGTH_SHORT).show();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (NetUtils.checkPermission(InteractCourseDetailActivity.this).size() > 0) {
                                requestLivePermission();
                            } else {
                                toNext();
                            }
                        } else {
                            toNext();
                        }
                    }
                } else {
                    intent = new Intent(InteractCourseDetailActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    startActivity(intent);
                }
                break;
            case R.id.pay:
                if (BaseApplication.getInstance().isLogined()) {
                    if ("teaching".equals(data.getData().getInteractive_course().getStatus())) {
                        if (alertDialog == null) {
                            View view = View.inflate(InteractCourseDetailActivity.this, R.layout.dialog_cancel_or_confirm, null);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(InteractCourseDetailActivity.this);
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
                    intent = new Intent(InteractCourseDetailActivity.this, LoginActivity2.class);
                    intent.putExtra("activity_action", Constant.LoginAction.toRemedialClassDetail);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
                break;
            case R.id.menu_1:

                if (playInfo == null || playInfo.getData() == null || playInfo.getData().getInteractive_course() == null || playInfo.getData().getInteractive_course().getChat_team() == null ||
                        StringUtils.isNullOrBlanK(playInfo.getData().getInteractive_course().getChat_team().getTeam_id())) {
                    Toast.makeText(this, "id为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent = new Intent(InteractCourseDetailActivity.this, MessageActivity.class);
                intent.putExtra("sessionId", playInfo.getData().getInteractive_course().getChat_team().getTeam_id());
                intent.putExtra("sessionType", SessionTypeEnum.None);
                intent.putExtra("courseId", id);
                intent.putExtra("name", data.getData().getInteractive_course().getName());
                intent.putExtra("type", "custom");
                startActivity(intent);
                pop.dismiss();
                break;
            case R.id.menu_5:
                intent = new Intent(InteractCourseDetailActivity.this, MembersActivity.class);
                intent.putExtra("members", playInfo.getData().getInteractive_course().getChat_team());
                startActivity(intent);
                pop.dismiss();
                break;
        }
    }

    private void toNext() {
        Intent intent = new Intent(InteractCourseDetailActivity.this, InteractiveLiveActivity.class);
        intent.putExtra("id", data.getData().getInteractive_course().getId());
        startActivity(intent);
    }

    private void requestLivePermission() {
        MPermission.with(this)
                .addRequestCode(100)
                .permissions(NetUtils.checkPermission(InteractCourseDetailActivity.this).toArray(new String[NetUtils.checkPermission(InteractCourseDetailActivity.this).size()]))
                .request();
    }

    @OnMPermissionGranted(100)
    public void onLivePermissionGranted() {
//        Toast.makeText(InteractiveLiveActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
        toNext();
    }

    @OnMPermissionDenied(100)
    public void onLivePermissionDenied() {
        List<String> deniedPermissions = MPermission.getDeniedPermissions(this, NetUtils.checkPermission(InteractCourseDetailActivity.this).toArray(new String[NetUtils.checkPermission(InteractCourseDetailActivity.this).size()]));
        String tip = "您拒绝了权限" + MPermissionUtil.toString(deniedPermissions) + "，无法开启直播";
        Toast.makeText(InteractCourseDetailActivity.this, tip, Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionNeverAskAgain(100)
    public void onLivePermissionDeniedAsNeverAskAgain() {
        List<String> deniedPermissions = MPermission.getDeniedPermissionsWithoutNeverAskAgain(this, NetUtils.checkPermission(InteractCourseDetailActivity.this).toArray(new String[NetUtils.checkPermission(InteractCourseDetailActivity.this).size()]));
        List<String> neverAskAgainPermission = MPermission.getNeverAskAgainPermissions(this, NetUtils.checkPermission(InteractCourseDetailActivity.this).toArray(new String[NetUtils.checkPermission(InteractCourseDetailActivity.this).size()]));
        StringBuilder sb = new StringBuilder();
        sb.append("无法开启直播，请到系统设置页面开启权限");
        sb.append(MPermissionUtil.toString(neverAskAgainPermission));
        if (deniedPermissions != null && !deniedPermissions.isEmpty()) {
            sb.append(",下次询问请授予权限");
            sb.append(MPermissionUtil.toString(deniedPermissions));
        }

        Toast.makeText(InteractCourseDetailActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
    }

    private void payRemedial() {
        Intent intent = new Intent(InteractCourseDetailActivity.this, OrderConfirmActivity.class);
        intent.putExtra("courseType", "interact");
        intent.putExtra("id", id);
        intent.putExtra("coupon", getIntent().getStringExtra("coupon"));
        OrderPayBean bean = new OrderPayBean();
        bean.name = data.getData().getInteractive_course().getName();
        bean.subject = data.getData().getInteractive_course().getSubject();
        bean.grade = data.getData().getInteractive_course().getGrade();
        bean.classnumber = data.getData().getInteractive_course().getLessons_count();
        List<TeacherBean> teachers = data.getData().getInteractive_course().getTeachers();
//        StringBuffer teacherNames = new StringBuffer();
//        for (int i = 0; i < teachers.size(); i++) {
//            teacherNames.append(teachers.get(0).getName());
//            if (i != teachers.size() - 1) {
//                teacherNames.append("/");
//            }
//        }
//        bean.teacher = teacherNames.toString();
        bean.teacher = teachers.get(0).getName() + "...";
        bean.current_price = Float.valueOf(data.getData().getInteractive_course().getPrice());

        intent.putExtra("data", bean);
        startActivity(intent);
    }


    @Subscribe
    public void onEvent(PayResultState code) {
//        if (!StringUtils.isNullOrBlanK(event) && event.equals("pay_success")) {
//
//            finish();
//        }
        setResult(Constant.RESPONSE);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
