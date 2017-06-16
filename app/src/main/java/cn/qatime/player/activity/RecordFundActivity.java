package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.base.BaseFragmentActivity;
import cn.qatime.player.fragment.FragmentFundRecordRecharge;
import cn.qatime.player.fragment.FragmentFundRecordRefund;
import cn.qatime.player.fragment.FragmentFundRecordWithdrawCash;
import cn.qatime.player.fragment.FragmentFundRecordConsumption;
import cn.qatime.player.utils.Constant;
import libraryextra.view.FragmentLayoutWithLine;

/**
 * @author Tianhaoranly
 * @date 2016/9/27 17:14
 * @Description:
 */
public class RecordFundActivity extends BaseFragmentActivity{
    private int[] tab_text = {R.id.tab_text1, R.id.tab_text2,R.id.tab_text3,R.id.tab_text4};
    FragmentLayoutWithLine fragmentlayout;
    private ArrayList<Fragment> fragBaseFragments = new ArrayList<>();
    private int page;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_fund);
        setTitles(getResources().getString(R.string.my_record_fund));
        setRightImage(R.mipmap.phone, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog == null) {
                    View view = View.inflate(RecordFundActivity.this, R.layout.dialog_cancel_or_confirm, null);
                    TextView text = (TextView) view.findViewById(R.id.text);
                    text.setText(getResourceString(R.string.call_customer_service_phone) +  Constant.phoneNumber);
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
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ Constant.phoneNumber));
                            startActivity(intent);
                            alertDialog.dismiss();
                        }
                    });
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RecordFundActivity.this);
                    alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.setContentView(view);
                } else {
                    alertDialog.show();
                }
            }
        });
        page = getIntent().getIntExtra("page",0);
        initView();
    }


    private void initView() {
        fragBaseFragments.add(new FragmentFundRecordRecharge());
        fragBaseFragments.add(new FragmentFundRecordWithdrawCash());
        fragBaseFragments.add(new FragmentFundRecordConsumption());
        fragBaseFragments.add(new FragmentFundRecordRefund());

        fragmentlayout = (FragmentLayoutWithLine) findViewById(R.id.fragmentlayout);

        fragmentlayout.setScorllToNext(true);
        fragmentlayout.setScorll(true);
        fragmentlayout.setWhereTab(1);
        fragmentlayout.setTabHeight(4, getResources().getColor(R.color.colorPrimary));
        fragmentlayout.setOnChangeFragmentListener(new FragmentLayoutWithLine.ChangeFragmentListener() {
            @Override
            public void change(int lastPosition, int position, View lastTabView, View currentTabView) {
                ((TextView) lastTabView.findViewById(tab_text[lastPosition])).setTextColor(0xff999999);
                ((TextView) currentTabView.findViewById(tab_text[position])).setTextColor(0xff333333);
                ((BaseFragment) fragBaseFragments.get(position)).onShow();
            }
        });
        fragmentlayout.setAdapter(fragBaseFragments, R.layout.tablayout_fund_record, 0x0311);
        fragmentlayout.getViewPager().setOffscreenPageLimit(1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentlayout.setCurrenItem(page);
            }
        }, 200);

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
}
