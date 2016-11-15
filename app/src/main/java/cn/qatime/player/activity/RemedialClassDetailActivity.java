package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.bean.PayResultState;
import cn.qatime.player.config.UserPreferences;
import cn.qatime.player.fragment.FragmentClassDetailClassInfo;
import cn.qatime.player.fragment.FragmentClassDetailTeacherInfo;
import cn.qatime.player.fragment.FragmentClassDetailClassList;
import cn.qatime.player.im.cache.TeamDataCache;
import cn.qatime.player.im.cache.UserInfoCache;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.bean.OrderPayBean;
import libraryextra.bean.PersonalInformationBean;
import libraryextra.bean.Profile;
import libraryextra.bean.RemedialClassDetailBean;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.SPUtils;
import libraryextra.utils.ScreenUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;
import libraryextra.view.SimpleViewPagerIndicator;

public class RemedialClassDetailActivity extends BaseFragmentActivity implements View.OnClickListener {
    ImageView image;
    private int id;
    private String[] mTitles = new String[]{"辅导概况", "教师资料", "课程列表"};
    private SimpleViewPagerIndicator mIndicator;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private Button audition;
    private Button pay;
    private TextView name;
    private TextView title;
    private RemedialClassDetailBean data;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private int pager = 0;
    TextView price;
    TextView studentnumber;
    DecimalFormat df = new DecimalFormat("#.00");
    private AlertDialog alertDialog;
    private Button startStudy;
    private View startStudyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedial_class_detail);

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
        image = (ImageView) findViewById(R.id.image);
        name = (TextView) findViewById(R.id.name);
        image.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenWidth(this) * 5 / 8));

        fragBaseFragments.add(new FragmentClassDetailClassInfo());
        fragBaseFragments.add(new FragmentClassDetailTeacherInfo());
        fragBaseFragments.add(new FragmentClassDetailClassList());



//        fragmentlayout = (FragmentLayoutWithLine)findViewById(R.id.fragmentlayout);
//
//        fragmentlayout.setScorllToNext(true);
//        fragmentlayout.setScorll(true);
//        fragmentlayout.setWhereTab(1);
//        fragmentlayout.setTabHeight(4,0xffff9999);
//        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
//            @Override
//            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
//                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff666666);
//                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
//            }
//        });
//        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_remedial_class_detail, 0x0911);


        audition = (Button) findViewById(R.id.audition);
        pay = (Button) findViewById(R.id.pay);
        startStudy = (Button) findViewById(R.id.start_study);
        startStudyView =findViewById(R.id.start_study_view);
        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        studentnumber = (TextView) findViewById(R.id.student_number);
        audition.setOnClickListener(this);
        pay.setOnClickListener(this);
        startStudy.setOnClickListener(this);

        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.id_stickynavlayout_indicator);
        mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_viewpager);
        mIndicator.setTitles(mTitles);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTitles.length;
            }

            @Override
            public Fragment getItem(int position) {
                return fragBaseFragments.get(position);
            }

        };

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(pager);
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
    }


    private void initData() {
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlRemedialClass + "/" + id, null,
                new VolleyListener(RemedialClassDetailActivity.this) {


                    @Override
                    protected void onSuccess(JSONObject response) {
                        data = JsonUtils.objectFromJson(response.toString(), RemedialClassDetailBean.class);
                        Glide.with(RemedialClassDetailActivity.this).load(data.getData().getPublicize()).placeholder(R.mipmap.photo).fitCenter().crossFade().into(image);
                        if (data.getData() != null) {
                            String price = df.format(data.getData().getPrice());
                            if (price.startsWith(".")) {
                                price = "0" + price;
                            }
                            name.setText(data.getData().getName());
                            title.setText(data.getData().getName());
                            RemedialClassDetailActivity.this.price.setText("￥" + price);
                            studentnumber.setText("报名人数 " + data.getData().getBuy_tickets_count());
                        }
                        if (data != null) {
                            ((FragmentClassDetailClassInfo) fragBaseFragments.get(0)).setData(data);
                            ((FragmentClassDetailTeacherInfo) fragBaseFragments.get(1)).setData(data);
                            ((FragmentClassDetailClassList) fragBaseFragments.get(2)).setData(data);
                            if (data.getData() != null) {
                                if (data.getData().getIs_tasting()) {
                                    audition.setEnabled(false);
                                    audition.setText(getResources().getString(R.string.Joined_the_audition));
                                } else {
                                    audition.setEnabled(true);
                                    audition.setText(getResources().getString(R.string.Join_the_audition));
                                }

                                if (data.getData().getIs_bought()) {
                                    pay.setEnabled(false);
                                    pay.setText(getResources().getString(R.string.purchased));
                                    audition.setEnabled(false);
                                    startStudyView.setVisibility(View.VISIBLE);
                                } else {
                                    pay.setEnabled(true);
                                    pay.setText(getResources().getString(R.string.purchase_now));
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
                }

                , new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
        addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.audition:
                joinAudition();
                break;
            case R.id.start_study:
                Intent intent = new Intent(RemedialClassDetailActivity.this, NEVideoPlayerActivity.class);
                intent.putExtra("url", data.getData().getBoard());
                intent.putExtra("id", data.getData().getId());
                intent.putExtra("sessionId", data.getData().getChat_team_id());
                startActivity(intent);
                break;
            case R.id.pay:
                if("teaching".equals(data.getData().getStatus())){
                    if (alertDialog == null) {
                        View view = View.inflate(RemedialClassDetailActivity.this, R.layout.dialog_cancel_or_confirm, null);
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
                                payRemedial();
                                alertDialog.dismiss();
                            }
                        });
                        AlertDialog.Builder builder = new AlertDialog.Builder(RemedialClassDetailActivity.this);
                        alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.setContentView(view);
//                        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
//                        attributes.width= ScreenUtils.getScreenWidth(getApplicationContext())- DensityUtils.dp2px(getApplicationContext(),20)*2;
//                        alertDialog.getWindow().setAttributes(attributes);
                    } else {
                        alertDialog.show();
                    }
                }else{
                    payRemedial();
                };
                break;
        }
    }

    private void payRemedial() {
        Intent intent = new Intent(RemedialClassDetailActivity.this, OrderConfirmActivity.class);
        intent.putExtra("id", id);
        OrderPayBean bean = new OrderPayBean();
        bean.image = data.getData().getPublicize();
        bean.name = data.getData().getName();
        bean.subject = data.getData().getSubject();
        bean.grade = data.getData().getGrade();
        bean.classnumber = data.getData().getPreset_lesson_count();
        bean.teacher = data.getData().getTeacher().getName();
        bean.classendtime = data.getData().getLive_end_time();
        bean.status = data.getData().getStatus();
        bean.classstarttime = data.getData().getLive_start_time();
        bean.price = data.getData().getPrice();

        intent.putExtra("data", bean);
        startActivity(intent);
    }

    private void joinAudition() {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlRemedialClass + "/" + id + "/taste", map), null,
                new VolleyListener(RemedialClassDetailActivity.this) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        //已加入试听
                        audition.setEnabled(false);
                        audition.setText(getResources().getString(R.string.Joined_the_audition));
                        if (StringUtils.isNullOrBlanK(BaseApplication.getAccount()) || StringUtils.isNullOrBlanK(BaseApplication.getAccountToken())) {
                            DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.urlPersonalInformation + BaseApplication.getUserId() + "/info", null,
                                    new VolleyListener(RemedialClassDetailActivity.this) {
                                        @Override
                                        protected void onSuccess(JSONObject response) {
                                            PersonalInformationBean bean = JsonUtils.objectFromJson(response.toString(), PersonalInformationBean.class);
                                            if (bean != null && bean.getData() != null && bean.getData().getChat_account() != null) {
                                                Profile profile = BaseApplication.getProfile();
                                                profile.getData().getUser().setChat_account(bean.getData().getChat_account());
                                                BaseApplication.setProfile(profile);

                                                String account = BaseApplication.getAccount();
                                                String token = BaseApplication.getAccountToken();

                                                if (!StringUtils.isNullOrBlanK(account) && !StringUtils.isNullOrBlanK(token)) {
                                                    AbortableFuture<LoginInfo> loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
                                                    loginRequest.setCallback(new RequestCallback<LoginInfo>() {
                                                        @Override
                                                        public void onSuccess(LoginInfo o) {
                                                            Logger.e("云信登录成功" + o.getAccount());
                                                            // 初始化消息提醒
                                                            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

                                                            NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());
                                                            //缓存
                                                            UserInfoCache.getInstance().clear();
                                                            TeamDataCache.getInstance().clear();

                                                            UserInfoCache.getInstance().buildCache();
                                                            TeamDataCache.getInstance().buildCache();

                                                            UserInfoCache.getInstance().registerObservers(true);
                                                            TeamDataCache.getInstance().registerObservers(true);
                                                        }

                                                        @Override
                                                        public void onFailed(int code) {
//                                                            BaseApplication.clearToken();
                                                            Profile profile = BaseApplication.getProfile();
                                                            profile.getData().setRemember_token("");
                                                            SPUtils.putObject(RemedialClassDetailActivity.this, "profile", profile);
                                                        }

                                                        @Override
                                                        public void onException(Throwable throwable) {
                                                            Logger.e(throwable.getMessage());
                                                            BaseApplication.clearToken();
                                                        }
                                                    });
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
                        }
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

    @Subscribe
    public void onEvent(PayResultState code) {
//        if (!StringUtils.isNullOrBlanK(event) && event.equals("pay_success")) {
//
//            finish();
//        }
            finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
