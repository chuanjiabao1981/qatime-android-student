package cn.qatime.player.view;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.transformation.GlideCircleTransform;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;


/**
 * @author Tianhaoranly
 * @date 2016/12/15 13:07
 * @Description:
 */
public class PayPopView {
    public static int WITHDRAW_CASH = 0;//提现
    public static int PAY_ORDER = 1;//支付
    private int type;
    private String url;
    private String title;
    private String price;
    private PayEditText payEditText;
    private CustomKeyboard customKeyboard;
    private BaseActivity activity;
    private OnPayPSWVerifyListener listener;
    private PopupWindow pop;
    private TextView textPrice;
    private TextView textTitle;
    private View close;
    private ImageView header;

    public OnPayPSWVerifyListener getOnPayPSWVerifyListener() {
        return listener;
    }

    public void setOnPayPSWVerifyListener(OnPayPSWVerifyListener listener) {
        this.listener = listener;
    }

    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "", "0", "<<"
    };
    private View view;

    public PayPopView(int type, String title, String price, BaseActivity activity) {
        this.type = type;
        this.title = title;
        this.price = price;
        this.activity = activity;
        init();
    }

    private void init() {
        view = View.inflate(activity, R.layout.dialog_pay_password, null);
        header = (ImageView) view.findViewById(R.id.header);
        close = view.findViewById(R.id.close);
        textTitle = (TextView) view.findViewById(R.id.title);
        textPrice = (TextView) view.findViewById(R.id.price);
        payEditText = (PayEditText) view.findViewById(R.id.PayEditText_pay);
        customKeyboard = (CustomKeyboard) view.findViewById(R.id.KeyboardView_pay);


        textTitle.setText(title);
        textPrice.setText(price);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });

        if (BaseApplication.getProfile().getData() != null && BaseApplication.getProfile().getData().getUser() != null) {
            Glide.with(activity).load(BaseApplication.getProfile().getData().getUser().getEx_big_avatar_url()).placeholder(R.mipmap.personal_information_head).crossFade().transform(new GlideCircleTransform(activity)).into(header);
        }

        //设置键盘
        customKeyboard.setKeyboardKeys(KEY);
        customKeyboard.setOnClickKeyboardListener(new CustomKeyboard.OnClickKeyboardListener() {
            @Override
            public void onKeyClick(int position, String value) {
                if (position < 11 && position != 9) {
                    payEditText.add(value);
                } else if (position == 11) {
                    payEditText.remove();
                }
            }
        });
        /**
         * 当密码输入完成时的回调
         */
        payEditText.setOnInputChangeListener(new PayEditText.OnInputChangeListener() {
            @Override
            public void onInputFinished(String password) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("password", password);
                if(type == WITHDRAW_CASH){
                    url= UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getUserId() + "/withdraws/ticket_token", map);
                }else if(type == PAY_ORDER){
                    // TODO: 2017/1/10 待修改
                    url= UrlUtils.getUrl(UrlUtils.urlpayment + BaseApplication.getUserId() + "/withdraws/ticket_token", map);
                }
                DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(url, null,
                        new VolleyListener(activity) {
                            @Override
                            protected void onSuccess(JSONObject response) {
                                if (listener != null) {
                                    try {
                                        listener.onSuccess(response.getString("data"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            protected void onError(JSONObject response) {
                                Logger.e(response.toString());
                                payEditText.clear();
                                try {
                                    int errorCode = response.getJSONObject("error").getInt("code");
                                    if (listener != null) {
                                        listener.onError(errorCode);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            protected void onTokenOut() {
                                activity.tokenOut();
                            }
                        }, new VolleyErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        if (listener != null) {
                            listener.onError(0);
                        }

                    }
                });
                activity.addToRequestQueue(request);
            }

            @Override
            public void onInputAdd(String value) {

            }

            @Override
            public void onInputRemove() {

            }
        });
        payEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customKeyboard.setVisibility(customKeyboard.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
//                KeyBoardUtils.openKeybord(editText,PayPSWVerifyActivity.this);
            }
        });
    }

    public void showPop() {

        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pop.setBackgroundDrawable(new ColorDrawable());
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.downDialogstyle);
        pop.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.7f);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void dismiss() {
        pop.dismiss();
    }

    public interface OnPayPSWVerifyListener {
        void onSuccess(String ticket_token);

        /**
         * @param errorCode 0：serverError
         */
        void onError(int errorCode);
    }
}
