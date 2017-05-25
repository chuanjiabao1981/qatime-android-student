package cn.qatime.player.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.bean.OrderRefundBean;
import cn.qatime.player.utils.Constant;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.JsonUtils;
import libraryextra.utils.StringUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;

/**
 * @author Tianhaoranly
 * @date 2017/1/3 16:20
 * @Description:
 */
public class ApplyRefundActivity extends BaseActivity {

    private OrderRefundBean orderRefundBean;
    private TextView orderId;
    private TextView productName;
    private TextView progress;
    private TextView price;
    private TextView usedAmount;
    private TextView refundType;
    private TextView refundAmount;
    private TextView confirm;
    private TextView reason;


    private void assignViews() {
        orderId = (TextView) findViewById(R.id.order_id);
        productName = (TextView) findViewById(R.id.product_name);
        progress = (TextView) findViewById(R.id.progress);
        price = (TextView) findViewById(R.id.price);
        usedAmount = (TextView) findViewById(R.id.used_amount);
        refundType = (TextView) findViewById(R.id.refund_type);
        refundAmount = (TextView) findViewById(R.id.refund_amount);
        reason = (TextView) findViewById(R.id.reason);
        confirm = (TextView) findViewById(R.id.confirm);
        reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReasonDialog();
            }
        });
    }

    private void showReasonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择退款原因");
        final String[] str = {"买错了，不想买了", "对课程内容不满意", "对授课老师不满意", "没有时间学习", "其他"};
        builder.setSingleChoiceItems(str, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reason.setText(str[which]);
                        dialog.dismiss();
                    }
                }
        );
        builder.show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        final AlertDialog alertDialog = builder.create();
//        View v = View.inflate(getActivity(), R.layout.dialog_team_notify_alert, null);
//        v.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//        ((TextView) v.findViewById(R.id.text)).setText(items.get(position - 1).isMute() ? getResourceString(R.string.resume_alert) : getResourceString(R.string.nolongger_alert));
//        v.findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//
//                NIMClient.getService(TeamService.class).muteTeam(items.get(position - 1).getContactId(), !items.get(position - 1).isMute()).setCallback(new RequestCallback<Void>() {
//                    @Override
//                    public void onSuccess(Void param) {
//                        Team team = TeamDataCache.getInstance().getTeamById(items.get(position - 1).getContactId());
//                        items.get(position - 1).setMute(team.mute());
////                                notificationConfigText.setText(team.mute() ? getString(R.string.close) : getString(R.string.open));
//                        adapter.notifyDataSetChanged();
//                    }
//
//                    @Override
//                    public void onFailed(int code) {
//                        Logger.e("muteTeam failed code:" + code);
//                    }
//
//                    @Override
//                    public void onException(Throwable exception) {
//
//                    }
//                });
//            }
//        });
//        v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//        alertDialog.setCanceledOnTouchOutside(true);
//        alertDialog.show();
//        alertDialog.setContentView(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_refund);
        assignViews();
        initView();
    }

    private void initView() {
        setTitles(getString(R.string.refund_apply));
        String response = getIntent().getStringExtra("response");
        orderRefundBean = JsonUtils.objectFromJson(response, OrderRefundBean.class);

        orderId.setText(getIntent().getStringExtra("order_id"));
        productName.setText(getIntent().getStringExtra("name"));
        int preset_lesson_count = getIntent().getIntExtra("preset_lesson_count", 0);
        int closed_lessons_count = getIntent().getIntExtra("closed_lessons_count", 0);
        progress.setText((preset_lesson_count - closed_lessons_count) + "/" + preset_lesson_count);
        price.setText("￥" + orderRefundBean.getData().getAmount());
        usedAmount.setText("￥" + (Double.valueOf(orderRefundBean.getData().getAmount()) - Double.valueOf(orderRefundBean.getData().getRefund_amount())));
        String pay_type = orderRefundBean.getData().getPay_type();
        if ("weixin".equals(pay_type)) {
            refundType.setText(R.string.refund_to_weixin);
        } else if ("alipay".equals(pay_type)) {
            refundType.setText(R.string.refund_to_alipay);
        } else {
            refundType.setText(R.string.refund_to_account);
        }
        refundAmount.setText("￥" + orderRefundBean.getData().getRefund_amount());

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmRefund();
            }
        });
    }

    /**
     * 提交退款申请
     */
    private void confirmRefund() {
        if (StringUtils.isNullOrBlanK(reason.getText().toString())) {
            Toast.makeText(ApplyRefundActivity.this, R.string.please_enter_refund_reason, Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("order_id", getIntent().getStringExtra("order_id"));
        map.put("reason", reason.getText().toString());
        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getUserId() + "/refunds", map), null,
                new VolleyListener(ApplyRefundActivity.this) {
                    @Override
                    protected void onSuccess(JSONObject response) {
                        Toast.makeText(ApplyRefundActivity.this, R.string.refund_application_successful, Toast.LENGTH_SHORT).show();
                        setResult(Constant.RESPONSE);
                        finish();
                    }

                    @Override
                    protected void onError(JSONObject response) {
//                        Toast.makeText(getActivity(), getResourceString(R.string.order_cancel_failed), Toast.LENGTH_SHORT).show();
                        try {
                            if (response.getJSONObject("error").getInt("code") == 3002) {
                                Toast.makeText(ApplyRefundActivity.this, R.string.not_enough_amount_of_refund, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }
                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                Logger.e(volleyError.getMessage());
            }
        });
        addToRequestQueue(request);

    }
}
