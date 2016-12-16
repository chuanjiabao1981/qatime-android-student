package cn.qatime.player.view;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.base.BaseActivity;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.UrlUtils;
import libraryextra.utils.VolleyErrorListener;
import libraryextra.utils.VolleyListener;


/**
 * @author Tianhaoranly
 * @date 2016/12/15 13:07
 * @Description:
 */
public class PayPopView {
    private String title;
    private String price;
    private PayEditText payEditText;
    private CustomKeyboard customKeyboard;
    private BaseActivity activity;

    private static final String[] KEY = new String[]{
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "", "0", "<<"
    };
    private View view;

    public PayPopView(String title, String price, BaseActivity activity) {
        this.title = title;
        this.price = price;
        this.activity = activity;
        init();
    }

    private void init() {
        view = View.inflate(activity, R.layout.dialog_pay_password, null);
        payEditText = (PayEditText) view.findViewById(R.id.PayEditText_pay);
        customKeyboard = (CustomKeyboard) view.findViewById(R.id.KeyboardView_pay);
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
                map.put("current_pament_password", password);
                DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(Request.Method.POST, UrlUtils.getUrl(UrlUtils.cashAccounts + BaseApplication.getUserId() + "/password/ticket_token", map), null,
                        new VolleyListener(activity) {
                            @Override
                            protected void onSuccess(JSONObject response) {
                                Toast.makeText(activity, "密码正确", Toast.LENGTH_SHORT).show();
                            }

                            protected void onError(JSONObject response) {
                                payEditText.clear();
                                try {
                                    int errorCode = response.getJSONObject("error").getInt("code");
                                    if (errorCode == 2005) {
                                        Toast.makeText(activity, "密码验证失败", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, "请先设置支付密码", Toast.LENGTH_SHORT).show();
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

        PopupWindow pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
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
}
